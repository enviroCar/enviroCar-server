/*
 * Copyright (C) 2013-2022 The enviroCar project
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
package org.envirocar.server.core.mail;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.envirocar.server.core.entities.User;

public class PlainMail extends UserMail {

    private final String subject;
    private final MailContent content;

    public PlainMail(User user, String subject, String content) {
        super(user);
        this.subject = Objects.requireNonNull(subject);
        this.content = DefaultMailContent.plain(content);
    }

    @Override
    public String getSubject() {
        return this.subject;
    }

    @Override
    public List<MailContent> getContents() {
        return Collections.singletonList(this.content);
    }

}
