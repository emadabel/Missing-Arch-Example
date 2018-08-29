package com.emadabel.missingarchexample.data;

import android.arch.lifecycle.LiveData;
import android.support.v4.util.LruCache;

import com.emadabel.missingarchexample.data.model.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import timber.log.Timber;

public class UserCache extends LruCache<String, LiveData<User>> {

    private static final Object LOCK = new Object();
    private static UserCache sInstance = null;

    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    private UserCache(int maxSize) {
        super(maxSize);
    }

    private static UserCache init() {
        int availableMemoryInByte = (int) (Runtime.getRuntime().maxMemory() / 1024);

        return new UserCache(availableMemoryInByte / 8);
    }

    @Override
    protected int sizeOf(String key, LiveData<User> value) {
        /* Reference: http://www.java2s.com/Code/Android/Development/Functionthatgetthesizeofanobject.htm */
        if (value == null)
            return -1;

        // Special output stream use to write the content
        // of an output stream to an internal byte array.
        ByteArrayOutputStream byteArrayOutputStream =
                new ByteArrayOutputStream();

        try {
            // Output stream that can write object
            ObjectOutputStream objectOutputStream =
                    new ObjectOutputStream(byteArrayOutputStream);

            // Write object and close the output stream
            objectOutputStream.writeObject(value);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (IOException e) {
            Timber.d("Could not write the object");
            e.printStackTrace();
        }

        // Get the byte array
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        return byteArray == null ? 0 : byteArray.length;
    }

    public static UserCache getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = init();
            }
        }
        return sInstance;
    }
}
