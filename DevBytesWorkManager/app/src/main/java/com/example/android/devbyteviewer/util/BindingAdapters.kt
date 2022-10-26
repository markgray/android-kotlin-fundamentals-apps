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

package com.example.android.devbyteviewer.util

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

/**
 * Binding adapter used to hide the spinner once data is available, it is called when both the
 * "app:isNetworkError" and the "app:playlist" attributes are used on a View, which they are in the
 * layout file layout/fragment_dev_byte.xml for the progress bar with ID R.id.loading_spinner.
 * (Note: if either attribute can be omitted use the annotation argument "requireAll = false")
 *
 * If our [playlist] parameter is not `null` we set the visibility of our [View] parameter [view]
 * to [View.GONE], otherwise we set it to [View.VISIBLE]. If our [Boolean] parameter [isNetWorkError]
 * is `true` set the visibility of [view] to [View.GONE].
 *
 * @param view the [View] on which "app:isNetworkError" and the "app:playlist" attributes are used
 * @param isNetWorkError the value of the "app:isNetworkError" attribute
 * @param playlist the value of the "app:playlist" attribute
 */
@Suppress("unused") // Unused but instructional
@BindingAdapter("isNetworkError", "playlist")
fun hideIfNetworkError(view: View, isNetWorkError: Boolean, playlist: Any?) {
    view.visibility = if (playlist != null) View.GONE else View.VISIBLE

    if (isNetWorkError) {
        view.visibility = View.GONE
    }
}

/**
 * Binding adapter used to display images from URL using Glide, it is called when the "app:imageUrl"
 * attribute is used on a [ImageView], which it is for the [ImageView] with ID R.id.video_thumbnail
 * in the layout file layout/devbyte_item.xml (which is used for items in the `RecyclerView` with
 * ID R.id.recycler_view in the layout file layout/fragment_dev_byte.xml).
 *
 * We use the [Glide.with] method to begin a load with Glide by passing in the context of our
 * [ImageView] parameter [imageView] and use the `RequestManager` for the top level application
 * that it returns to call its `load` method to obtain a request builder for loading a `Drawable`
 * from our URL parameter [url], and to use the `into` method of the builder to load the resource
 * into our [ImageView] parameter [imageView].
 *
 * @param imageView the [ImageView] we are to load the image into.
 * @param url the network Url for the image we are to download.
 */
@Suppress("unused") // Unused but instructional
@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, url: String) {
    Glide.with(imageView.context).load(url).into(imageView)
}