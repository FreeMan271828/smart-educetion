package org.nuist.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;

@ControllerAdvice
public class GlobalExceptionHandlingConfiguration implements ProblemHandling, SecurityAdviceTrait {

    @Override
    public boolean isCausalChainsEnabled() {
        return true;
    }
}
