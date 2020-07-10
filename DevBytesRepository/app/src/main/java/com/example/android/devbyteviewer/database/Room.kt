/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.devbyteviewer.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * The Data Access Object we use to read and write to our Room database.
 */
@Dao
interface VideoDao {
    /**
     * Query method for reading all of the `DatabaseVideo` entries in our "databasevideo" table into
     * a [LiveData] of a `List` of `DatabaseVideo`. It is used by the initializer of the `videos`
     * field of `VideosRepository` where it is mapped from a `LiveData<List<DatabaseVideo>>` to
     * a `LiveData<List<DevByteVideo>>` using the `Transformations.map` function and the
     * `asDomainModel` extension of `List<DatabaseVideo>`.
     *
     * @return a [LiveData] list of all of the [DatabaseVideo] objects in our database
     */
    @Query("select * from databasevideo")
    fun getVideos(): LiveData<List<DatabaseVideo>>

    /**
     * Insert method for inserting a List of [DatabaseVideo] entities into our database, it uses
     * the [OnConflictStrategy.REPLACE] strategy to replace the old data if there is a conflict.
     * It is used by the `refreshVideos` method of `VideosRepository` to write a list of
     * `NetworkVideo` objects encapsulated in a `NetworkVideoContainer` to our database after it
     * converts the objects to `DatabaseVideo` entities.
     *
     * @param videos the `List` of [DatabaseVideo] objects we are to write to our database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll( videos: List<DatabaseVideo>)
}



@Database(entities = [DatabaseVideo::class], version = 1)
abstract class VideosDatabase: RoomDatabase() {
    abstract val videoDao: VideoDao
}

private lateinit var INSTANCE: VideosDatabase

fun getDatabase(context: Context): VideosDatabase {
    synchronized(VideosDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                    VideosDatabase::class.java,
                    "videos").build()
        }
    }
    return INSTANCE
}
