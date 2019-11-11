package com.emadabel.missingarchexample.data;

import android.arch.lifecycle.LiveData;

import com.emadabel.missingarchexample.data.database.UserDao;
import com.emadabel.missingarchexample.data.model.User;
import com.emadabel.missingarchexample.data.network.Webservice;

import java.io.IOException;
import java.util.concurrent.Executor;

import retrofit2.Response;
import timber.log.Timber;

public class UserRepository {

    private Webservice mWebservice;
    private UserCache mUserCache;
    private UserDao mUserDao;
    private Executor mExecutor;

    public UserRepository(Webservice webservice, UserCache userCache, UserDao userDao, Executor executor) {
        mWebservice = webservice;
        mUserCache = userCache;
        mUserDao = userDao;
        mExecutor = executor;
    }

    public LiveData<User> getUser(String userId) {
        LiveData<User> cached = mUserCache.get(userId);
        if (cached != null) {
            Timber.d("Getting the data from In-memory cache");
            return cached;
        }

        //final MutableLiveData<User> data = new MutableLiveData<>();
        mUserCache.put(userId, mUserDao.load(userId));

        refreshUser(userId);
        /*// this is still suboptimal but better than before.
        // a complete implementation must also handle the error cases.
        mWebservice.getUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                // error case is left out for brevity
                Timber.d("Successfully retrieved the data from the internet");
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Timber.d("Could not connect to the server");
            }
        });*/

        return mUserDao.load(userId);
    }

    private void refreshUser(String userId) {
        mExecutor.execute(() -> {
            // running in a background thread
            // check if user was fetched recently
            boolean userExists = mUserDao.hasUser(userId);
            if (!userExists) {
                try {
                    // refresh the data
                    Response<User> response = mWebservice.getUser(userId).execute();
                    // Update the database.The LiveData will automatically refresh so
                    // we don't need to do anything else here besides updating the database
                    mUserDao.save(response.body());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
