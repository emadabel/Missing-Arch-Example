package com.emadabel.missingarchexample.data.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import com.emadabel.missingarchexample.utilities.AppExecutors;

public abstract class NetworkBoundResource<ResultType, RequestType> {

    private MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();
    private AppExecutors appExecutors;

    public NetworkBoundResource(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        init();
    }

    void init() {
        result.setValue(Resource.loading(null));
        LiveData<ResultType> dbSource = loadFromDb();
        result.addSource(dbSource, data -> {
            result.removeSource(dbSource);
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource);
            } else {
                result.addSource(dbSource, newData -> {
                    setValue(Resource.success(newData));
                });
            }
        });
    }

    private void setValue(Resource<ResultType> newValue) {
        if (result.getValue() != newValue) {
            result.setValue(newValue);
        }
    }

    private void fetchFromNetwork(LiveData<ResultType> dbSource) {
        LiveData<ApiResponse<RequestType>> apiResponse = createCall();
        result.addSource(dbSource, newData -> {
            setValue(Resource.loading(newData));
        });
        result.addSource(apiResponse, response -> {
            result.removeSource(apiResponse);
            result.removeSource(dbSource);
            if (response.getClass().isAssignableFrom(ApiSuccessResponse.class)) {
                appExecutors.diskIO().execute(() -> {
                    saveCallResult((RequestType) processResponse(((ApiSuccessResponse) response)));
                    appExecutors.mainThread().execute(() -> {
                        result.addSource(loadFromDb(), newData -> {
                            setValue(Resource.success(newData));
                        });
                    });
                });
            } else if (response.getClass().isAssignableFrom(ApiEmptyResponse.class)) {
                appExecutors.mainThread().execute(() -> {
                    result.addSource(loadFromDb(), newData -> {
                        setValue(Resource.success(newData));
                    });
                });
            } else if (response.getClass().isAssignableFrom(ApiErrorResponse.class)) {
                onFetchFailed();
                result.addSource(dbSource, newData -> {
                    setValue(Resource.error(((ApiErrorResponse) response).errorMessage, newData));
                });
            }
        });
    }

    protected void onFetchFailed() {}

    public LiveData<Resource<ResultType>> asLiveData() {
        return result;
    }

    protected RequestType processResponse(ApiSuccessResponse<RequestType> response) {
        return response.body;
    }

    protected abstract void saveCallResult(RequestType item);

    protected abstract boolean shouldFetch(ResultType data);

    protected abstract LiveData<ResultType> loadFromDb();

    protected abstract LiveData<ApiResponse<RequestType>> createCall();
}
