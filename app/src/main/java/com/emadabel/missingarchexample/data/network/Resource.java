package com.emadabel.missingarchexample.data.network;

import static com.emadabel.missingarchexample.data.network.Status.ERROR;
import static com.emadabel.missingarchexample.data.network.Status.LOADING;
import static com.emadabel.missingarchexample.data.network.Status.SUCCESS;

public class Resource<T> {

    public final Status status;
    public final T data;
    public final String message;

    public Resource(Status status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static  <T> Resource<T> success(T data) {
        return new Resource<>(SUCCESS, data, null);
    }

    public static  <T> Resource<T> error(String msg, T data) {
        return new Resource<>(ERROR, data, msg);
    }

    public static  <T> Resource<T> loading(T data) {
        return new Resource<>(LOADING, data, null);
    }
}
