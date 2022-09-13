package com.prabhakar.batch;

import com.prabhakar.model.User;
import org.springframework.batch.core.SkipListener;

public class UserValidationListener implements SkipListener<User, User> {


    @Override
    public void onSkipInRead(Throwable throwable) {

    }

    @Override
    public void onSkipInWrite(User user, Throwable throwable) {

    }

    @Override
    public void onSkipInProcess(User user, Throwable throwable) {
        System.out.println("Skipped User");
        System.out.println(user.toString());

        System.out.println("Error message");
        System.out.println(throwable.getMessage());
    }
}
