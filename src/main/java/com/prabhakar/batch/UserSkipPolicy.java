package com.prabhakar.batch;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.validator.ValidationException;

import java.io.FileNotFoundException;

public class UserSkipPolicy implements SkipPolicy {
    @Override
    public boolean shouldSkip(Throwable throwable, int i) throws SkipLimitExceededException {
        return throwable instanceof ValidationException;
    }
}
