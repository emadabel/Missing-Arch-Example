package com.emadabel.missingarchexample.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.emadabel.missingarchexample.data.model.User;
import com.emadabel.missingarchexample.data.network.Webservice;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class UserRepository {

    private Webservice mWebservice;
    private UserCache mUserCache;

    public UserRepository(Webservice webservice, UserCache userCache) {
        mWebservice = webservice;
        mUserCache = userCache;
    }

    public LiveData<User> getUser(String userId) {
        LiveData<User> cached = mUserCache.get(userId);
        if (cached != null) {
            Timber.d("Getting the data from In-memory cache");
            return cached;
        }

        final MutableLiveData<User> data = new MutableLiveData<>();
        mUserCache.put(userId, data);
        // this is still suboptimal but better than before.
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
        });
        return data;
    }
}
