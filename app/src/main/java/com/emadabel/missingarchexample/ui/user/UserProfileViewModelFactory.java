package com.emadabel.missingarchexample.ui.user;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.emadabel.missingarchexample.data.UserRepository;

public class UserProfileViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final UserRepository mUserRepository;

    public UserProfileViewModelFactory(UserRepository repository) {
        this.mUserRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new UserProfileViewModel(mUserRepository);
    }
}
