package com.emadabel.missingarchexample.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import com.emadabel.missingarchexample.data.database.UserDao;
import com.emadabel.missingarchexample.data.model.User;
import com.emadabel.missingarchexample.data.network.ApiResponse;
import com.emadabel.missingarchexample.data.network.NetworkBoundResource;
import com.emadabel.missingarchexample.data.network.Resource;
import com.emadabel.missingarchexample.data.network.Webservice;
import com.emadabel.missingarchexample.utilities.AppExecutors;

import timber.log.Timber;

public class UserRepository {

    private Webservice webservice;
    private UserCache userCache;
    private UserDao userDao;
    private AppExecutors appExecutors;

    public UserRepository(Webservice webservice, UserCache userCache, UserDao userDao, AppExecutors executors) {
        this.webservice = webservice;
        this.userCache = userCache;
        this.userDao = userDao;
        this.appExecutors = executors;
    }

    public LiveData<Resource<User>> getUser(String userId) {
        LiveData<User> cached = userCache.get(userId);
        if (cached != null) {
            Timber.d("Getting the data from In-memory cache");
            Resource<User> userResource = Resource.success(cached.getValue());
            MediatorLiveData<Resource<User>> resourceLiveData = new MediatorLiveData<>();
            resourceLiveData.setValue(userResource);
            return  resourceLiveData;
        }

        return new NetworkBoundResource<User, User>(appExecutors){
            @Override
            protected void saveCallResult(User item) {
                userDao.save(item);
            }

            @Override
            protected boolean shouldFetch(User data) {
                return data == null;
            }

            @Override
            protected LiveData<User> loadFromDb() {
                LiveData<User> data = userDao.load(userId);
                userCache.put(userId, data);
                return data;
            }

            @Override
            protected LiveData<ApiResponse<User>> createCall() {
                return webservice.getUser(userId);
            }
        }.asLiveData();

        //final MutableLiveData<User> data = new MutableLiveData<>();

        //refreshUser(userId);
        /*// this is still suboptimal but better than before.
        // a complete implementation must also handle the error cases.
        webservice.getUser(userId).enqueue(new Callback<User>() {
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

        //return userDao.load(userId);
    }

//    private void refreshUser(String userId) {
//        mExecutor.execute(() -> {
//            // running in a background thread
//            // check if user was fetched recently
//            boolean userExists = userDao.hasUser(userId);
//            if (!userExists) {
//                try {
//                    // refresh the data
//                    Response<User> response = webservice.getUser(userId).execute();
//                    // Update the database.The LiveData will automatically refresh so
//                    // we don't need to do anything else here besides updating the database
//                    userDao.save(response.body());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
}
