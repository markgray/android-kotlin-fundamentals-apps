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

package com.example.android.devbyteviewer.network

import com.example.android.devbyteviewer.database.DatabaseVideo
import com.example.android.devbyteviewer.domain.DevByteVideo
import com.squareup.moshi.JsonClass

/**
 * DataTransferObjects go in this file. These are responsible for parsing responses from the server
 * or formatting objects to send to the server. You should convert these to domain objects before
 * using them.
 *
 * See domain package for the plain Kotlin data classes that represent the things in our app.
 */

/**
 * VideoHolder holds a list of Videos.
 *
 * This is to parse first level of our network result which looks like
 *
 *     {
 *       "videos": []
 *     }
 *
 * The JsonClass(generateAdapter = true) annotation triggers the annotation processor to generate
 * an adapter for this type.
 *
 * @param videos list of [NetworkVideo] objects, each one representing a devbyte that can be played.
 */
@JsonClass(generateAdapter = true)
data class NetworkVideoContainer(val videos: List<NetworkVideo>)

/**
 * Videos represent a devbyte that can be played.
 *
 * @param title the title of the video
 * @param description the description of the video
 * @param url the YouTube Url for the video.
 * @param updated the date that the video was last updated
 * @param thumbnail the Url for the app:imageUrl binding adapter attribute of the `ImageView`
 * @param closedCaptions always `null` in our case (could be Url for closed captions?)
 */
@JsonClass(generateAdapter = true)
data class NetworkVideo(
        val title: String,
        val description: String,
        val url: String,
        val updated: String,
        val thumbnail: String,
        val closedCaptions: String?)

/**
 * Convert Network results to domain objects. This extension function converts its receiver's
 * `videos` list of [NetworkVideo] objects to a list of [DevByteVideo] domain objects.
 *
 * @return a list of [DevByteVideo] domain objects containing all the information in the receiver's
 * `videos` list of [NetworkVideo] objects
 */
@Suppress("unused")
fun NetworkVideoContainer.asDomainModel(): List<DevByteVideo> {
    return videos.map {
        DevByteVideo(
                title = it.title,
                description = it.description,
                url = it.url,
                updated = it.updated,
                thumbnail = it.thumbnail)
    }
}


/**
 * Convert Network results to database objects. This extension function converts its receiver's
 * `videos` list of [NetworkVideo] objects to a list of [DatabaseVideo] database objects.
 *
 * @return a list of [DatabaseVideo] database objects containing all the information in the
 * receiver's `videos` list of [NetworkVideo] objects
 */
fun NetworkVideoContainer.asDatabaseModel(): List<DatabaseVideo> {
    return videos.map {
        DatabaseVideo(
                title = it.title,
                description = it.description,
                url = it.url,
                updated = it.updated,
                thumbnail = it.thumbnail)
    }
}

