/*
 * Copyright (C) 2013-2018 The enviroCar project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.envirocar.server.core;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.envirocar.server.core.activities.Activities;
import org.envirocar.server.core.activities.Activity;
import org.envirocar.server.core.dao.ActivityDao;
import org.envirocar.server.core.dao.PrivacyStatementDao;
import org.envirocar.server.core.dao.TermsOfUseDao;
import org.envirocar.server.core.dao.UserDao;
import org.envirocar.server.core.entities.PasswordReset;
import org.envirocar.server.core.entities.Terms;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.entities.Users;
import org.envirocar.server.core.event.ChangedProfileEvent;
import org.envirocar.server.core.event.DeletedUserEvent;
import org.envirocar.server.core.event.PasswordResetEvent;
import org.envirocar.server.core.exception.*;
import org.envirocar.server.core.filter.ActivityFilter;
import org.envirocar.server.core.mail.Mailer;
import org.envirocar.server.core.mail.MailerException;
import org.envirocar.server.core.mail.PlainMail;
import org.envirocar.server.core.update.EntityUpdater;
import org.envirocar.server.core.util.PasswordEncoder;
import org.envirocar.server.core.util.pagination.Pagination;
import org.envirocar.server.core.validation.EntityValidator;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class UserServiceImpl implements UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final Path EMAIL_TEMPLATE_FILE = Paths.get("mail-verification-mail-template.txt");
    private static final String REGISTRATION_LINK_PLACEHOLDER = "{link}";
    private static final String REGISTRATION_USERNAME_PLACEHOLDER = "{name}";
    private static final String REGISTRATION_EXIRATION_TIME_PLACEHOLDER = "{expirationTime}";
    private static final String REGISTRATION_FALLBACK_SUBJECT = "enviroCar Registration Confirmation";
    private final ActivityDao activityDao;
    private final PasswordEncoder passwordEncoder;
    private final UserDao userDao;
    private final EntityValidator<User> userValidator;
    private final EntityUpdater<User> userUpdater;
    private final EventBus eventBus;
    private final Mailer mailer;
    private final Provider<ConfirmationLinkFactory> confirmationLinkFactory;

    private final PrivacyStatementDao privacyStatementDao;
    private final TermsOfUseDao termsOfUseDao;

    @Inject
    public UserServiceImpl(ActivityDao activityDao,
                           UserDao userDao,
                           PasswordEncoder passwordEncoder,
                           EntityValidator<User> userValidator,
                           EntityUpdater<User> userUpdater,
                           EventBus eventBus,
                           Mailer mailer,
                           Provider<ConfirmationLinkFactory> confirmationLinkFactory,
                           PrivacyStatementDao privacyStatementDao,
                           TermsOfUseDao termsOfUseDao) {
        this.activityDao = activityDao;
        this.passwordEncoder = passwordEncoder;
        this.userDao = userDao;
        this.userValidator = userValidator;
        this.userUpdater = userUpdater;
        this.eventBus = eventBus;
        this.mailer = mailer;
        this.confirmationLinkFactory = confirmationLinkFactory;
        this.privacyStatementDao = privacyStatementDao;
        this.termsOfUseDao = termsOfUseDao;
    }

    @Override
    public User createUser(User user) throws ValidationException,
            ResourceAlreadyExistException {
        userValidator.validateCreate(user);
        if (userDao.getByName(user.getName(), true) != null) {
            throw new ResourceAlreadyExistException("name already exists");
        }
        if (userDao.getByMail(user.getMail(), true) != null) {
            throw new ResourceAlreadyExistException("mail already exists");
        }
        // set the hashed password
        user.setToken(passwordEncoder.encode(user.getToken()));

        // FIXME check if terms of use is the current one
        if (user.hasAcceptedTermsOfUse() && !user.hasAcceptedTermsOfUseVersion()) {
            Optional.ofNullable(this.termsOfUseDao.getLatest())
                    .map(Terms::getIssuedDate).ifPresent(user::setTermsOfUseVersion);
        }
        // FIXME check if privacy statement is the current one
        if (user.hasAcceptedPrivacyStatement() && !user.hasPrivacyStatementVersion()) {
            Optional.ofNullable(this.privacyStatementDao.getLatest())
                    .map(Terms::getIssuedDate).ifPresent(user::setPrivacyStatementVersion);
        }

        User created = this.userDao.create(user);

        try {
            mailer.send(createVerificationMail(created));
        } catch (MailerException ex) {
            throw new RuntimeException(ex);
        }

        return user;
    }

    private Path getTemplatePath() {
        String home = System.getProperty("user.home");
        Path path;
        if (home != null && !home.isEmpty()) {
            Path homeDirectory = Paths.get(home);

            path = homeDirectory.resolve(EMAIL_TEMPLATE_FILE).toAbsolutePath();

            if (Files.exists(path)) {
                return path;
            } else {
                LOG.warn("Could not find mail verification template at {}", path);
            }
        } else {
            LOG.warn("user.home is not specified. Will try to use fallback resources.");
        }
        path = EMAIL_TEMPLATE_FILE.toAbsolutePath();
        if (Files.exists(path)) {
            return path;
        }
        LOG.warn("Could not find mail verification template at {}", path);
        return null;
    }

    @Override
    public User getUser(String name) throws UserNotFoundException {
        return getUser(name, false);
    }

    @Override
    public User getUser(String name, boolean includeUnconfirmed) throws UserNotFoundException {
        User user = this.userDao.getByName(name, includeUnconfirmed);
        if (user == null) {
            throw new UserNotFoundException(name);
        }
        return user;
    }

    @Override
    public Users getUsers(Pagination p) {
        return this.userDao.get(p);
    }

    @Override
    public User modifyUser(User user, User changes) throws UserNotFoundException,
            IllegalModificationException,
            ValidationException,
            ResourceAlreadyExistException {
        this.userValidator.validateUpdate(changes);
        if (changes.hasMail() && !changes.getMail().equals(user.getMail())) {
            if (this.userDao.getByMail(changes.getMail()) != null) {
                throw new ResourceAlreadyExistException();
            }
        }
        this.userUpdater.update(changes, user);
        this.userDao.save(user);
        this.eventBus.post(new ChangedProfileEvent(user));
        return user;
    }

    @Override
    public void deleteUser(User user, boolean deleteContent) {
        this.userDao.delete(user, deleteContent);
        this.eventBus.post(new DeletedUserEvent(user));
    }

    @Override
    public Activities getActivities(ActivityFilter request) {
        return this.activityDao.get(request);
    }

    @Override
    public Activity getActivity(ActivityFilter request, String id) {
        return this.activityDao.get(request, id);
    }

    @Override
    public void requestPasswordReset(User user) throws BadRequestException {
        User dbUser = this.userDao.getByName(user.getName());

        if (!user.equals(dbUser) || !user.getMail().equals(dbUser.getMail())) {
            throw new InvalidUserMailCombinationException();
        }

        PasswordReset code = this.userDao.requestPasswordReset(dbUser);

        /*
         * we got here without exception, fire an event
         */
        eventBus.post(new PasswordResetEvent(code.getCode(), user, code.getExpires()));
    }

    @Override
    public void resetPassword(User changes, String verificationCode) throws BadRequestException {
        User dbUser = this.userDao.getByName(changes.getName());

        if (!changes.equals(dbUser)) {
            throw new BadRequestException("Invalid username.");
        }

        try {
            this.userUpdater.update(changes, dbUser);
        } catch (IllegalModificationException e) {
            throw new InvalidUserMailCombinationException();
        }
        this.userDao.resetPassword(dbUser, verificationCode);
    }

    @Override
    public User confirmUser(String code) throws BadRequestException {
        if (code == null || code.isEmpty()) {
            throw new BadRequestException("missing verification code");
        }
        return this.userDao.confirm(code);
    }

    private PlainMail createVerificationMail(User user) {
        URI confirmationLink = confirmationLinkFactory.get().getConfirmationLink(user);
        String expirationTime = user.getExpirationDate().toString(ISODateTimeFormat.dateTimeNoMillis());
        String mailSubject = REGISTRATION_FALLBACK_SUBJECT;
        String mailBody = null;
        Path templatePath = getTemplatePath();
        if (templatePath != null) {
            try {
                List<String> lines = Files.readAllLines(templatePath, StandardCharsets.UTF_8);
                // first line is subject, second line empty, the rest is the body
                if (lines.size() < 3) {
                    LOG.error("At least 3 lines of content required to be a valid mail template file");
                } else {
                    mailSubject = lines.iterator().next();
                    String template = lines.stream().skip(2).collect(joining(System.lineSeparator()));
                    mailBody = template.replace(REGISTRATION_LINK_PLACEHOLDER, confirmationLink.toString())
                            .replace(REGISTRATION_USERNAME_PLACEHOLDER, user.getName())
                            .replace(REGISTRATION_EXIRATION_TIME_PLACEHOLDER, expirationTime);
                }
            } catch (IOException ex) {
                LOG.warn("Could not read mail template", ex);
            }
        }
        if (mailBody == null || mailBody.isEmpty()) {
            LOG.warn("Could not find mail template, just sending the link");
            mailBody = confirmationLink.toString();
        }
        return new PlainMail(user, mailSubject, mailBody);
    }

}
