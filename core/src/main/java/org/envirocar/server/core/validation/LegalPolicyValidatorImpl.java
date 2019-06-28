package org.envirocar.server.core.validation;

import com.google.common.base.Suppliers;
import org.envirocar.server.core.dao.PrivacyStatementDao;
import org.envirocar.server.core.dao.TermsDao;
import org.envirocar.server.core.dao.TermsOfUseDao;
import org.envirocar.server.core.entities.PrivacyStatement;
import org.envirocar.server.core.entities.Terms;
import org.envirocar.server.core.entities.TermsOfUseInstance;
import org.envirocar.server.core.entities.User;

import javax.inject.Inject;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * {@link EntityValidator} that validates that a {@link User} has accepted
 * the latest {@link TermsOfUseInstance} and {@link PrivacyStatement}.
 */
public class LegalPolicyValidatorImpl implements LegalPolicyValidator {

    private final Supplier<PrivacyStatement> latestPrivacyStatement;
    private final Supplier<TermsOfUseInstance> latestTermsOfUse;

    /**
     * Creates a new {@link LegalPolicyValidatorImpl}.
     *
     * @param latestPrivacyStatement The {@link Supplier} for the latest {@link PrivacyStatement}.
     * @param latestTermsOfUse       The {@link Supplier} for the latest {@link TermsOfUseInstance}.
     */
    public LegalPolicyValidatorImpl(Supplier<PrivacyStatement> latestPrivacyStatement,
                                    Supplier<TermsOfUseInstance> latestTermsOfUse) {
        this.latestPrivacyStatement = Objects.requireNonNull(latestPrivacyStatement);
        this.latestTermsOfUse = Objects.requireNonNull(latestTermsOfUse);
    }

    /**
     * Creates a new {@link LegalPolicyValidatorImpl}.
     *
     * @param privacyStatementDao The {@link PrivacyStatementDao} to retrieve the latest {@link PrivacyStatement}.
     * @param termsOfUseDao       The {@link TermsOfUseDao} to retrieve the latest {@link TermsOfUseInstance}.
     */
    @Inject
    public LegalPolicyValidatorImpl(PrivacyStatementDao privacyStatementDao, TermsOfUseDao termsOfUseDao) {
        this(createSupplier(privacyStatementDao), createSupplier(termsOfUseDao));
    }

    @Override
    public void validateTermsOfUse(User user) throws TermsOfUseException {
        TermsOfUseInstance termsOfUse = this.latestTermsOfUse.get();
        if (termsOfUse != null) {
            String acceptedVersion = user.getTermsOfUseVersion();
            String currentVersion = termsOfUse.getIssuedDate();
            if (!currentVersion.equals(acceptedVersion)) {
                throw new TermsOfUseException(currentVersion, acceptedVersion);
            }
        }
    }

    @Override
    public void validatePrivacyStatement(User user) throws PrivacyStatementException {
        PrivacyStatement privacyStatement = this.latestPrivacyStatement.get();
        if (privacyStatement != null) {
            String acceptedVersion = user.getPrivacyStatementVersion();
            String currentVersion = privacyStatement.getIssuedDate();
            if (!currentVersion.equals(acceptedVersion)) {
                throw new PrivacyStatementException(currentVersion, acceptedVersion);
            }
        }
    }

    private static <T extends Terms> Supplier<T> createSupplier(TermsDao<T, ?> dao) {
        return Suppliers.memoizeWithExpiration(Objects.requireNonNull(dao)::getLatest, 1, TimeUnit.DAYS)::get;
    }
}
