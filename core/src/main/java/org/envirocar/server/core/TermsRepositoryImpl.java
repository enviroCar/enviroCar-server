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

import com.google.common.base.Suppliers;
import com.google.inject.Inject;
import org.envirocar.server.core.dao.PrivacyStatementDao;
import org.envirocar.server.core.dao.TermsDao;
import org.envirocar.server.core.dao.TermsOfUseDao;
import org.envirocar.server.core.entities.PrivacyStatement;
import org.envirocar.server.core.entities.Terms;
import org.envirocar.server.core.entities.TermsOfUseInstance;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class TermsRepositoryImpl implements TermsRepository {
    private final Supplier<Optional<PrivacyStatement>> latestPrivacyStatement;
    private final Supplier<Optional<TermsOfUseInstance>> latestTermsOfUse;

    @Inject
    public TermsRepositoryImpl(PrivacyStatementDao privacyStatementDao,
                               TermsOfUseDao termsOfUseDao) {
        latestTermsOfUse = createSupplier(termsOfUseDao);
        latestPrivacyStatement = createSupplier(privacyStatementDao);
    }

    @Override
    public Optional<PrivacyStatement> getLatestPrivacyStatement() {
        return latestPrivacyStatement.get();
    }

    @Override
    public Optional<TermsOfUseInstance> getLatestTermsOfUse() {
        return latestTermsOfUse.get();
    }

    private static <T extends Terms> Supplier<Optional<T>> createSupplier(TermsDao<T, ?> dao) {
        return Suppliers.memoizeWithExpiration(Objects.requireNonNull(dao)::getLatest, 8, TimeUnit.HOURS)::get;
    }
}
