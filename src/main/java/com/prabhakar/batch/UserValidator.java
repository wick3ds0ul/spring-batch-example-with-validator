package com.prabhakar.batch;

import com.prabhakar.model.User;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

public class UserValidator implements Validator<User> {

    @Override
    public void validate(User user) throws ValidationException {
        if (user.getName().length() > 3) {
            throw new ValidationException("User name cannot have more than 3 alphabets");
        }
    }
}
