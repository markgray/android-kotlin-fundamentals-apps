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

package com.example.android.devbyteviewer.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.devbyteviewer.R
import com.example.android.devbyteviewer.databinding.DevbyteItemBinding
import com.example.android.devbyteviewer.databinding.FragmentDevByteBinding
import com.example.android.devbyteviewer.domain.DevByteVideo
import com.example.android.devbyteviewer.viewmodels.DevByteViewModel

/**
 * Show a list of DevBytes on screen.
 */
class DevByteFragment : Fragment() {

    /**
     * One way to delay creation of the viewModel until an appropriate lifecycle method is to use
     * lazy. This requires that viewModel not be referenced before onActivityCreated, which we
     * do in this Fragment. To verify that [onActivityCreated] has been called we use the
     * [requireNotNull] method which will throw an [IllegalArgumentException] with the message
     * "You can only access the viewModel after onActivityCreated" if the the [FragmentActivity]
     * this fragment is currently associated is `null` and if it is not `null` we set our variable
     * `val activity` to our [FragmentActivity]. Then we initialize [viewModel] to the singleton
     * [DevByteViewModel] returned by the [ViewModelProvider.get] method when it uses the
     * [DevByteViewModel.Factory] to create it if it did not already exist.
     */
    private val viewModel: DevByteViewModel by lazy {
        val activity: FragmentActivity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated"
        }
        ViewModelProvider(
            this,
            DevByteViewModel.Factory(activity.application)
        )[DevByteViewModel::class.java]
    }

    /**
     * RecyclerView Adapter for converting a list of Video to cards.
     */
    private var viewModelAdapter: DevByteAdapter? = null

    /**
     * Called when all saved state has been restored into the view hierarchy of the fragment. This
     * can be used to do initialization based on saved state that you are letting the view hierarchy
     * track itself, such as whether check box widgets are currently checked. This is called after
     * [onViewCreated] and before [onStart].
     *
     * First we call our super's implementation of `onViewStateRestored`. Then we add a lambda as an
     * [Observer] to the `playlist` property of our [DevByteViewModel] field `viewModel` using the
     * `LifecycleOwner` that represents this Fragment's View as the `LifecycleOwner` which controls
     * the observer. This lambda calls a function block which sets the `videos` property of our
     * [DevByteAdapter] field [viewModelAdapter] to the List of `DevByteVideo` contained in
     * `playlist` whenever that `LiveData` property is updated to a non-null value.
     *
     * @param savedInstanceState we do not override [onSaveInstanceState] so do not use
     */
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        viewModel.playlist.observe(viewLifecycleOwner) { videos ->
            videos?.apply {
                viewModelAdapter?.videos = videos
            }
        }
    }

    /**
     * Called to have the fragment instantiate its user interface view. First we initialize our
     * [FragmentDevByteBinding] variable `val binding` to the binding returned by the method
     * [DataBindingUtil.inflate] when it uses our [LayoutInflater] parameter [inflater] to
     * inflate our layout file `R.layout.fragment_dev_byte` using our [ViewGroup] parameter
     * [container] for the `LayoutParams` without attaching to it. We set the `lifecycleOwner`
     * property of `binding` to a LifecycleOwner that represents this Fragment's View lifecycle,
     * and set the `viewModel` variable of `binding` to our [DevByteViewModel] field [viewModel].
     * We initialize our [DevByteAdapter] field [viewModelAdapter] to a new instance constructed
     * to use an instance of [VideoClick] to use as the click listener whose lambda block first
     * tries to initialize a `PackageManager` variable `val packageManager` with a new instance and
     * if it fails it discards the click. Then it initializes an [Intent] variable `var intent` with
     * a direct intent to launch the YouTube app using the `launchUri` extension property of the
     * [DevByteVideo] clicked to create the YouTube Uri. If `packageManager` is not able to
     * resolve the YouTube `intent` (the app is not installed) we set `intent` to an ACTION_VIEW
     * [Intent] for the parsed `url` property of the [DevByteVideo] clicked. Finally the lambda
     * starts an activity to handle `intent`.
     *
     * Next we find the [RecyclerView] in the `root` view property of `binding` with ID
     * `R.id.recycler_view` and set its `layoutManager` property to an instance of
     * [LinearLayoutManager] and set its `adapter` property to our [DevByteAdapter] field
     * [viewModelAdapter]. We set an [Observer] for the `eventNetworkError` [Boolean] `LiveData`
     * property of [viewModel] (event triggered for network error) whose lambda calls our
     * [onNetworkError] method to display a Toast error message if `eventNetworkError` is `true`.
     *
     * Finally we return the outermost [View] in the layout file associated with `binding` to the
     * caller.
     *
     * @param inflater The [LayoutInflater] object that can be used to inflate
     * any views in the fragment
     * @param container If non-null, this is the parent view that the fragment's
     * UI will be attached to. The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Return the [View] for the fragment's UI.
     */
    @SuppressLint("QueryPermissionsNeeded") // And it is requested in AndroidManifest.xml
    @Suppress("RedundantNullableReturnType")  // Method we override returns nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentDevByteBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dev_byte,
            container,
            false
        )
        // Set the lifecycleOwner so DataBinding can observe LiveData
        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        viewModelAdapter = DevByteAdapter(VideoClick {
            // When a video is clicked this block or lambda will be called by DevByteAdapter

            // context is not around, we can safely discard this click since the Fragment is no
            // longer on the screen
            val packageManager = context?.packageManager ?: return@VideoClick

            // Try to generate a direct intent to the YouTube app
            var intent = Intent(Intent.ACTION_VIEW, it.launchUri)
            if (intent.resolveActivity(packageManager) == null) {
                // YouTube app isn't found, use the web url
                intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
            }

            startActivity(intent)
        })

        binding.root.findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }


        // Observer for the network error.
        viewModel.eventNetworkError.observe(viewLifecycleOwner) { isNetworkError ->
            if (isNetworkError) onNetworkError()
        }

        return binding.root
    }

    /**
     * Method for displaying a Toast error message for network errors. If the value of the
     * `isNetworkErrorShown` property of our [DevByteViewModel] field [viewModel] is `false`
     * we toast the message "Network Error", then call the `onNetworkErrorShown` method of
     * [viewModel] to reset `_isNetworkErrorShown` to `true` (`onNetworkErrorShown` is the
     * publicly accessible read-only version of `_isNetworkErrorShown`).
     */
    private fun onNetworkError() {
        if (!(viewModel.isNetworkErrorShown.value ?: return)) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }

    /**
     * Helper extension property to generate YouTube app links. The getter initializes its [Uri]
     * variable `val httpUri` to the value returned by the [Uri.parse] method when it parses the
     * URI string in the [String] field `url` of our target [DevByteVideo]. Then we return the [Uri]
     * that the [Uri.parse] method returns when it parses the URI string formed by concatenating the
     * [String] "vnd.youtube:" to the query parameter "v" of `httpUri` (typical result returned:
     * "vnd.youtube:sYGKUtM2ga8"). Used in the lamdba block of the [VideoClick] click listener
     * passed to the constructor of the [DevByteAdapter] we initialize our [viewModelAdapter] field
     * to in our [onCreateView] override.
     */
    private val DevByteVideo.launchUri: Uri
        get() {
            val httpUri = Uri.parse(url)
            return Uri.parse("vnd.youtube:" + httpUri.getQueryParameter("v"))
        }
}

/**
 * Click listener for Videos. By giving the block a name it helps a reader understand what it does.
 * Used as the value of the `videoCallback` variable in the binding for each item View created from
 * the layout file layout/devbyte_item.xml for the `RecylerView`. The `onClick` override is called
 * with the [DevByteVideo] instance held by the view due to a binding expression for the
 * "android:onClick" attribute of the `R.id.clickableOverlay` view that overlays the entire
 * view group.
 *
 * @param block a lambda which takes a [DevByteVideo] and returns [Unit] (ie. void).
 */
class VideoClick(val block: (DevByteVideo) -> Unit) {
    /**
     * Called when a video is clicked
     *
     * @param video the [DevByteVideo] held by the view that was clicked
     */
    fun onClick(video: DevByteVideo): Unit = block(video)
}

/**
 * RecyclerView Adapter for setting up data binding on the items in the list.
 *
 * @param callback the [VideoClick] every element in our RecyclerView should use for its
 * `videoCallback` variable, and then call when its view is clicked.
 */
@Suppress("MemberVisibilityCanBePrivate") // Breaks kdoc linking
class DevByteAdapter(val callback: VideoClick) : RecyclerView.Adapter<DevByteViewHolder>() {

    /**
     * The videos that our Adapter will show. It is set to the value of the `LiveData` wrapped
     * `playlist` field of the [DevByteViewModel] field [DevByteFragment.viewModelAdapter] in the
     * lambda of the [Observer] added to it in the `onViewStateRestored` override of [DevByteFragment]
     * when it changes to a non-null value. Its size is returned by our [getItemCount] override, and
     * its contents are referenced in our [onBindViewHolder] override to retrieve the [DevByteVideo]
     * that the [DevByteViewHolder] should hold and display. The setter sets the backing field of
     * [videos] (`field` is the keyword used to access the backing field) to its `List<DevByteVideo>`
     * parameter `value` then calls the [notifyDataSetChanged] method to notify any registered
     * observers that the data set has changed. This will cause every element in our [RecyclerView]
     * to be invalidated.
     */
    var videos: List<DevByteVideo> = emptyList()
        @SuppressLint("NotifyDataSetChanged") // The whole List is changed as far as I can see.
        set(value) {
            field = value
            // For an extra challenge, update this to use the paging library.

            // Notify any registered observers that the data set has changed. This will cause every
            // element in our RecyclerView to be invalidated.
            notifyDataSetChanged()
        }

    /**
     * Called when [RecyclerView] needs a new [DevByteViewHolder] of the given type to represent
     * an item. We initialize our variable `val withDataBinding` to the [DevbyteItemBinding] binding
     * object that the [DataBindingUtil.inflate] returns when it inflates the `R.layout.devbyte_item`
     * layout file layout/devbyte_item.xml using our [ViewGroup] parameter [parent] for the layout
     * params without attaching to it. Then we return a new instance of [DevByteViewHolder]
     * constructed to use `withDataBinding` as the [DevbyteItemBinding] binding to the view it
     * should display in.
     *
     * @param parent The [ViewGroup] into which the new View will be added after it is bound to
     * an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevByteViewHolder {
        val withDataBinding: DevbyteItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            DevByteViewHolder.LAYOUT,
            parent,
            false
        )
        return DevByteViewHolder(withDataBinding)
    }

    /**
     * Returns the total number of items in the data set held by the adapter. We just return the
     * `size` of our `List<DevByteVideo>` field [videos].
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int = videos.size

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the "itemView" of the [DevByteViewHolder] parameter [holder] to
     * reflect the item at the position [position] in our data set. We use the [also] extension
     * function on the `viewDataBinding` field of [holder] to set the `video` variable of the
     * [DevbyteItemBinding] to the [DevByteVideo] at [position] in our data set [videos] list,
     * and its `videoCallback` variable to our [VideoClick] field [callback].
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: DevByteViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.video = videos[position]
            it.videoCallback = callback
        }
    }

}

/**
 * ViewHolder for DevByte items. All work is done by data binding. The class itself consists only
 * of the [DevbyteItemBinding] field [viewDataBinding] it is constructed to hold and a companion
 * object defining an alias for the resource ID of the layout file layout/devbyte_item.xml that is
 * the underlying View used to create a new [DevbyteItemBinding] for us to use.
 *
 * @param viewDataBinding the [DevbyteItemBinding] binding object for the view we are to display in.
 */
class DevByteViewHolder(val viewDataBinding: DevbyteItemBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        /**
         *
         */
        @LayoutRes
        val LAYOUT: Int = R.layout.devbyte_item
    }
}