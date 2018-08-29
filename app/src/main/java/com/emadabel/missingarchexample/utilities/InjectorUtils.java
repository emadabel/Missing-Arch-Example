package com.emadabel.missingarchexample.utilities;

import com.emadabel.missingarchexample.data.UserCache;
import com.emadabel.missingarchexample.data.UserRepository;
import com.emadabel.missingarchexample.data.network.WebClient;
import com.emadabel.missingarchexample.data.network.Webservice;
import com.emadabel.missingarchexample.ui.user.UserProfileViewModelFactory;

public class InjectorUtils {

    public static UserRepository provideRepository() {
        Webservice webservice = WebClient.getInstance().getWebservice();
        UserCache userCache = UserCache.getInstance();
        return new UserRepository(webservice, userCache);
    }

    public static UserProfileViewModelFactory provideUserProfileViewModelFactory() {
        UserRepository repository = provideRepository();
        return new UserProfileViewModelFactory(repository);
    }

}
