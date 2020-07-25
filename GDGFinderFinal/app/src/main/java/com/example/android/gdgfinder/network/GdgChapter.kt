/*
 * Copyright 2019, The Android Open Source Project
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

package com.example.android.gdgfinder.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
 * Moshi parses the JSON objects in the "data" array of our JSON file into a list of these kotlin
 * objects. The @Json annotation supplies the JSON field name when it differs from the kotlin one.
 */
@Parcelize
data class GdgChapter(
    @Json(name = "chapter_name") val name: String,
    @Json(name = "cityarea") val city: String,
    val country: String,
    val region: String,
    val website: String,
    val geo: LatLong
 ): Parcelable

/**
 * This class is the class Moshi parses the contents of the "geo" field's JSON object into.
 */
@Parcelize
data class LatLong(
    val lat: Double,
    @Json(name = "lng") val long: Double
) : Parcelable

/**
 * This class holds both the parsed "filters_" JSON array (a [List] of [String]) and the parsed
 * "data" JSON array (a [List] of [GdgChapter] objects) that are parsed from the JSON file when the
 * `getChapters` method of `GdgApiService` is called.
 */
@Parcelize
data class GdgResponse(
        @Json(name = "filters_") val filters: Filter,
        @Json(name = "data") val chapters: List<GdgChapter>
): Parcelable

/**
 * Moshi parses the "region" array of the JSON object "filters_" into this kotlin class.
 */
@Parcelize
data class Filter(
        @Json(name = "region") val regions: List<String>
): Parcelable

//"chapter_name": "GDG Bordj Bou-Arréridj",
//"cityarea": "Burj Bu Arririj",
//"country": "Algeria",
//"region": "Africa",
//"website": "https://www.meetup.com/GDG-BBA",
//"geo": {
//    "lat": 36.06000137,
//    "lng": 4.630000114
//}