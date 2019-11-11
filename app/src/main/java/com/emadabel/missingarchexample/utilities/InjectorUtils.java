package com.emadabel.missingarchexample.utilities;

import android.content.Context;

import com.emadabel.missingarchexample.data.UserCache;
import com.emadabel.missingarchexample.data.UserRepository;
import com.emadabel.missingarchexample.data.database.MyDatabase;
import com.emadabel.missingarchexample.data.database.UserDao;
import com.emadabel.missingarchexample.data.network.WebClient;
import com.emadabel.missingarchexample.data.network.Webservice;
import com.emadabel.missingarchexample.ui.user.UserProfileViewModelFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class InjectorUtils {

    public static UserRepository provideRepository(Context context) {
        Webservice webservice = WebClient.getInstance().getWebservice();
        UserCache userCache = UserCache.getInstance();
        UserDao userDao = MyDatabase.getInstance(context.getApplicationContext()).mUserDao();
        Executor executor = Executors.newSingleThreadExecutor();
        return new UserRepository(webservice, userCache, userDao, executor);
    }

    public static UserProfileViewModelFactory provideUserProfileViewModelFactory(Context context) {
        UserRepository repository = provideRepository(context.getApplicationContext());
        return new UserProfileViewModelFactory(repository);
    }

}
