package com.emadabel.missingarchexample.utilities;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.emadabel.missingarchexample.data.network.ApiResponse;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveDataCallAdapter<R> implements
        CallAdapter<R, LiveData<ApiResponse<R>>> {

    private Type responseType;

    @Override
    public Type responseType() {
        return this.responseType;
    }

    @Override
    public LiveData<ApiResponse<R>> adapt(Call<R> call) {
        return (LiveData) (new LiveData() {
            private AtomicBoolean started = new AtomicBoolean(false);

            @Override
            protected void onActive() {
                super.onActive();
                if (this.started.compareAndSet(false, true)) {
                    call.enqueue((Callback)(new Callback() {
                        public void onResponse(@NotNull Call callx, @NotNull Response response) {
                            postValue(ApiResponse.create(response));
                        }

                        public void onFailure(@NotNull Call callx, @NotNull Throwable throwable) {
                            postValue((ApiResponse)ApiResponse.create(throwable));
                        }
                    }));
                }
            }
        });
    }

    public LiveDataCallAdapter(@NonNull Type responseType) {
        super();
        this.responseType = responseType;
    }
}
