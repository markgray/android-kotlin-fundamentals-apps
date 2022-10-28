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
 *
 */

package com.example.android.marsrealestate.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import kotlinx.coroutines.Deferred
import retrofit2.http.Query

/**
 * The values of this enum are used to append a "Query" parameter to the URL. Which [MarsApiFilter]
 * to use is selected in the `onOptionsItemSelected` override of `OverViewFragment` when the user
 * uses the option menu to select a filter. The `updateFilter` method of `OverviewViewModel` is
 * then called with the new [MarsApiFilter] to have the `getMarsRealEstateProperties` method reload
 * the [List] of [MarsProperty] from the internet using the new filter as the query.
 */
enum class MarsApiFilter(
    /**
     * The [String] that is the value of the enum constant.
     */
    val value: String) {
    /**
     * Causes the `getMarsRealEstateProperties` method to reload the [List] of [MarsProperty] from
     * the internet with only properties which can be rented.
     */
    SHOW_RENT("rent"),

    /**
     * Causes the `getMarsRealEstateProperties` method to reload the [List] of [MarsProperty] from
     * the internet with only properties which can be bought.
     */
    SHOW_BUY("buy"),

    /**
     * Causes the `getMarsRealEstateProperties` method to reload the [List] of [MarsProperty] from
     * the internet with all properties.
     */
    SHOW_ALL("all")
}

/**
 * The API base URL for our [Retrofit] instance.
 */
private const val BASE_URL = " https://android-kotlin-fun-mars-server.appspot.com/"

/**
 * Build the [Moshi] object that [Retrofit] will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility. The [KotlinJsonAdapterFactory] is a reflection adapter that uses
 * Kotlin’s reflection library to convert your Kotlin classes to and from JSON.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Use the [Retrofit] builder to build a retrofit object using our [Moshi] converter [moshi]. We
 * construct a new instance of [Retrofit.Builder] then add a converter factory for serialization
 * and deserialization of objects created from our [Moshi] object [moshi], add a
 * [CoroutineCallAdapterFactory] call adapter factory for supporting the service method return type
 * [Deferred] rather than `Call`, set the API base URL to [BASE_URL] then build the builder into a
 * [Retrofit] instance.
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

/**
 * A public interface that exposes the [getProperties] method
 */
@Suppress("DeferredIsResult") // A rose is a rose is a rose
interface MarsApiService {
    /**
     * Returns a Coroutine [Deferred] wrapped [List] of [MarsProperty] which can be fetched with
     * await() if in a Coroutine scope.
     *
     * The @GET annotation indicates that the "realestate" endpoint will be requested with the GET
     * HTTP method, and the "@Query" annotation appends a Query parameter to the URL.
     *
     * @param type the [MarsApiFilter] to use with the "filter" query parameter, one of "rent",
     * "buy" or "all".
     * @return a [Deferred] list of [MarsProperty] which can be made active by invoking `await`.
     */
    @GET("realestate")
    fun getProperties(@Query("filter") type: String):
    // The Coroutine Call Adapter allows us to return a Deferred, a Job with a result
        Deferred<List<MarsProperty>>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object MarsApi {
    /**
     * The lazy-initialized [MarsApiService] Retrofit service that is an implementation of the API
     * endpoints defined by the service interface in this [MarsApiService]
     */
    val retrofitService: MarsApiService by lazy { retrofit.create(MarsApiService::class.java) }
}
