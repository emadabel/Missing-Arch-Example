package com.emadabel.missingarchexample.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.emadabel.missingarchexample.data.model.User;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserDao {

    @Insert(onConflict = REPLACE)
    void save(User user);

    @Query("SELECT * FROM user WHERE userId = :userId")
    LiveData<User> load(String userId);

    @Query("SELECT CASE WHEN EXISTS (SELECT * FROM [user] WHERE userId = :userId) THEN CAST(1 AS BIT) ELSE CAST(0 AS BIT) END")
    boolean hasUser(String userId);
}
