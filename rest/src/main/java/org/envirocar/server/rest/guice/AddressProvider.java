/*
 * Copyright (C) 2013 The enviroCar project
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
package org.envirocar.server.rest.guice;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.envirocar.server.core.validation.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <c.autermann@52north.org>
 */
@Singleton
public class AddressProvider implements Provider<Optional<Set<String>>> {
    private static final Logger log = LoggerFactory
            .getLogger(AddressProvider.class);
    private static final String FILE = "/allowed_addresses.txt";
    private final Supplier<Optional<Set<String>>> addresses;

    public AddressProvider() {
        this.addresses = Suppliers.memoizeWithExpiration(
                new AddressSupplier(), 30, TimeUnit.SECONDS);
    }

    @Override
    public Optional<Set<String>> get() {
        return this.addresses.get();
    }

    private class AddressSupplier implements Supplier<Optional<Set<String>>> {
        @Override
        public Optional<Set<String>> get() {
            log.debug("Checking {} for addresses.", FILE);
            try {
                File f = new File(FILE);
                if (!f.exists()) {
                    log.debug("{} does not exist. All addresses allowed.", FILE);
                    return Optional.absent();
                } else {
                    log.debug("{} exists. Loading mail addresses.", FILE);
                    return Optional.of(Files.readLines(
                            f, Charsets.UTF_8, new AddressProcessor()));
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private class AddressProcessor implements LineProcessor<Set<String>> {
        private final Set<String> addresses = Sets.newHashSet();

        @Override
        public boolean processLine(String line) {
            String address = line.trim();
            if (UserValidator.EMAIL_PATTERN.matcher(address).matches()) {
                log.debug("Allowed address: {}", address);
                addresses.add(address);
            } else {
            }
            return true;
        }

        @Override
        public Set<String> getResult() {
            return Collections.unmodifiableSet(addresses);
        }
    }
}
