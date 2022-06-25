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

package com.example.android.devbyteviewer.domain

import com.example.android.devbyteviewer.util.smartTruncate

/**
 * Domain objects are plain Kotlin data classes that represent the things in our app. These are the
 * objects that should be displayed on screen, or manipulated by the app.
 *
 * See database package for objects that are mapped to the database
 *
 * See network package for objects that parse or prepare network calls
 */

/**
 * [DevByteVideo] objects represent a devbyte video that can be played.
 *
 * @param title the title of the video
 * @param description the description of the video
 * @param url the YouTube Url for the video.
 * @param updated the date that the video was last updated
 * @param thumbnail the Url for the app:imageUrl binding adapter attribute of the `ImageView` in the
 * layout file devbyte_item.xml, the adapter is used to display images from URL using Glide.
 */
data class DevByteVideo(
    /**
     * the title of the video
     */
    val title: String,
    /**
     * the description of the video
     */
    val description: String,
    /**
     * the YouTube Url for the video.
     */
    val url: String,
    /**
     * the date that the video was last updated
     */
    val updated: String,
    /**
     * the Url for the thumbnail image
     */
    val thumbnail: String
) {

    /**
     * Short description is used for displaying truncated descriptions in the UI. We return the
     * [String] returned by the `smartTruncate` extension function when it truncates our
     * [description] property to 200 characters.
     */
    val shortDescription: String
        get() = description.smartTruncate(200)
}