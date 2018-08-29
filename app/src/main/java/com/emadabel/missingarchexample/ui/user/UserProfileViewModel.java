package com.emadabel.missingarchexample.ui.user;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.emadabel.missingarchexample.data.UserRepository;
import com.emadabel.missingarchexample.data.model.User;

import timber.log.Timber;

public class UserProfileViewModel extends ViewModel {

    private LiveData<User> mUser;
    private UserRepository mRepository;

    public UserProfileViewModel(UserRepository repository) {
        this.mRepository = repository;
    }

    public void init(String userId) {
        if (this.mUser != null) {
            // ViewModel is created per Fragment so
            // we know the userId won't change
            Timber.d("Fragment recreated");
            return;
        }
        Timber.d("Getting the live data");
        mUser = mRepository.getUser(userId);
    }

    public LiveData<User> getUser() {
        return mUser;
    }
}
