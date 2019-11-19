package com.emadabel.missingarchexample.data.network;

import android.arch.lifecycle.LiveData;

import com.emadabel.missingarchexample.data.model.User;

import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Webservice {
    /**
     * @GET declares an HTTP GET request
     * @Path("user") annotation on the userId parameter marks it as a
     * replacement for the {user} placeholder in the @GET path
     */
    @GET("/users/{user}")
    LiveData<ApiResponse<User>> getUser(@Path("user") String userId);
}
