package org.envirocar.server.rest.rights;

import org.envirocar.server.core.entities.PolicyType;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HasAcceptedLatestLegalPolicies {

    PolicyType[] value() default {PolicyType.PRIVACY_STATEMENT, PolicyType.TERMS_OF_USE};

}
