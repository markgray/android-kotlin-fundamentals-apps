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

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.devbyteviewer.domain.DevByteVideo

/**
 * Database entities go in this file. These define the SQLite tables in the database, and they
 * are read or written to using the DAO Data Access Object `VideoDao` defined in `Room.kt`,
 */

/**
 * DatabaseVideo represents a video entity in the database. Its SQLite table in the database is
 * named "databasevideo" (the default since the `@Entity` annotations lacks a "tableName" parameter).
 *
 * @param url the YouTube Url for the video, our PrimaryKey.
 * @param updated the date that the video was last updated
 * @param title the title of the video
 * @param description the description of the video
 * @param thumbnail the Url for the app:imageUrl binding adapter attribute of the `ImageView`
 */
@Entity
data class DatabaseVideo(
    @PrimaryKey
    val url: String,
    val updated: String,
    val title: String,
    val description: String,
    val thumbnail: String
)


/**
 * Map `DatabaseVideos` to `DevByteVideo` domain entities. Extension function that converts a list
 * of `DatabaseVideo` objects read from our database to a list of `DevByteVideo` objects containing
 * the exact same information but in a form usable by our `DevByteViewModel` for display to the user.
 */
fun List<DatabaseVideo>.asDomainModel(): List<DevByteVideo> {
    return map {
        DevByteVideo(
            url = it.url,
            title = it.title,
            description = it.description,
            updated = it.updated,
            thumbnail = it.thumbnail)
    }
}
