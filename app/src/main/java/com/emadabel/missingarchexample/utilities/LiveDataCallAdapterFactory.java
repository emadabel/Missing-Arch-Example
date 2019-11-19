package com.emadabel.missingarchexample.utilities;

import android.arch.lifecycle.LiveData;
import android.support.annotation.Nullable;

import com.emadabel.missingarchexample.R;
import com.emadabel.missingarchexample.data.network.ApiResponse;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.CallAdapter;
import retrofit2.CallAdapter.Factory;
import retrofit2.Retrofit;

public class LiveDataCallAdapterFactory extends Factory {

    @Nullable
    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        if (Factory.getRawType(returnType) != LiveData.class) {
            return null;
        }
        Type observableType = Factory.getParameterUpperBound(0, (ParameterizedType) returnType);
        Class<?> rawObservableType = Factory.getRawType(observableType);
        if (rawObservableType != ApiResponse.class) {
            throw new  IllegalArgumentException("type must be a resource");
        }
        if (!(observableType instanceof ParameterizedType)) {
            throw new  IllegalArgumentException("resource must be parameterized");
        }
        Type bodyType = Factory.getParameterUpperBound(0, (ParameterizedType) observableType);
        return new  LiveDataCallAdapter<R>(bodyType);
    }
}
