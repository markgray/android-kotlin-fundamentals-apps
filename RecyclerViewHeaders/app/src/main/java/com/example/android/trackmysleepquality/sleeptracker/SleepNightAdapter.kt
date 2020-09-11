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

package com.example.android.trackmysleepquality.sleeptracker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * View type of a [DataItem.Header] data item (the header at the beginning of our dataset).
 */
private const val ITEM_VIEW_TYPE_HEADER = 0

/**
 * View type of a [DataItem.SleepNightItem] data item (a real [SleepNight] in the `nights` list).
 */
private const val ITEM_VIEW_TYPE_ITEM = 1

/**
 * The adapter we use for the [RecyclerView] with resource ID R.id.sleep_list in the layout file
 * layout/fragment_sleep_tracker.xml which displays the [SleepNight] records read from our database,
 * as well as a "header" `TextView` displaying the string "Sleep Results" which occupies the entire
 * first row of the [RecyclerView].
 *
 * This class implements a [ListAdapter] for [RecyclerView]  which uses Data Binding to present
 * [List] data, including computing diffs between lists. Note that our [ListAdapter] super class
 * indirectly holds our dataset and we need to retrieve items using its `get` method rather than
 * directly from our [SleepTrackerViewModel] dataset field `nights`. An observer of `nights` calls
 * our [addHeaderAndSubmitList] method which appends a [DataItem.Header] header item to the list
 * `nights` then calls the [submitList] method of [ListAdapter] with that list to have it diffed
 * and displayed whenever the `LiveData` list of [SleepNight] changes.
 *
 * @param clickListener the [SleepNightListener] each item view binding in our [RecyclerView] should
 * use as its `clickListener` variable. The binding expression for the "android:onClick" attribute
 * of the `ConstraintLayout` holding all the views of the layout/list_item_sleep_night.xml layout
 * file calls the `onClick` method of its `clickListener` variable with its `sleep` variable (the
 * [SleepNight] it displays).
 */
@Suppress("MemberVisibilityCanBePrivate")
class SleepNightAdapter(
        val clickListener: SleepNightListener
): ListAdapter<DataItem, RecyclerView.ViewHolder>(SleepNightDiffCallback()) {

    /**
     * The [CoroutineScope] we use to launch a new coroutine in our [addHeaderAndSubmitList] method
     * which calls the [submitList] method of our [ListAdapter] super class using a suspend lambda
     * on the [Dispatchers.Main] `CoroutineDispatcher` ([submitList] needs to run on the UI thread).
     */
    private val adapterScope = CoroutineScope(Dispatchers.Default)

    /**
     * Prepends a [DataItem.Header] data item to its [List] of [SleepNight] parameter [list] (after
     * converting each [SleepNight] in [list] to a [DataItem.SleepNightItem]) then submits that list
     * of [DataItem] to be diffed and displayed.
     *
     * We launch a new coroutine on the [adapterScope] `CoroutineScope` without blocking the current
     * thread. In the lambda of the coroutine we initialize our [List] of [DataItem] variable
     * `val items` to a list holding only a [DataItem.Header] if [list] is `null` or if it is not
     * `null` to a list holding a [DataItem.Header] to which we append a list containing the results
     * of constructing a [DataItem.SleepNightItem] from each of the [SleepNight] objects in our
     * parameter [list]. Then using the [Dispatchers.Main] coroutine context we start a suspending
     * lambda, suspending until it completes, which calls the [submitList] method of our [ListAdapter]
     * super class to submit `items` to be diffed and displayed.
     *
     * Called by an Observer of the `LiveData` wrapped list of [SleepNight]'s field `nights` of the
     * `SleepTrackerViewModel` view model which is added to `nights` in the `onCreateView` override
     * of `SleepTrackerFragment`.
     *
     * @param list the list of [SleepNight] entries read from our database.
     */
    fun addHeaderAndSubmitList(list: List<SleepNight>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.SleepNightItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the [RecyclerView.ViewHolder] `itemView` to reflect the item at the
     * given position. We check to see if our [RecyclerView.ViewHolder] parameter [holder] is an
     * instance of our custom [SleepNightAdapter.ViewHolder] subclass, and it it is we initialize
     * our [DataItem.SleepNightItem] variable `val nightItem` with the [DataItem] that our super's
     * [getItem] method returns for our [Int] parameter [position], then we call the `bind` method
     * of our [RecyclerView.ViewHolder] parameter [holder] to have it "bind" to the [SleepNight]
     * field `sleepNight` of `nightItem`, and to "bind" to our [SleepNightListener] field [clickListener]
     * (it sets the `sleep` variable of the [ListItemSleepNightBinding] it holds to its view to
     * the [SleepNight] we pass and the `clickListener` variable to the [SleepNightListener], then
     * calls the `executePendingBindings` method of its [ListItemSleepNightBinding] to have it
     * update the Views that have expressions bound to these modified variables).
     *
     * @param holder The [RecyclerView.ViewHolder] which should be updated to represent the contents
     * of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
    */
    override fun onBindViewHolder(
            holder: RecyclerView.ViewHolder,
            position: Int
    ) {
        when (holder) {
            is ViewHolder -> {
                val nightItem = getItem(position) as DataItem.SleepNightItem
                holder.bind(nightItem.sleepNight, clickListener)
            }
        }
    }

    /**
     * Called when [RecyclerView] needs a new [ViewHolder] of the given type to represent an item.
     * When our [viewType] parameter is an [ITEM_VIEW_TYPE_HEADER] we return the [TextViewHolder]
     * returned by the [TextViewHolder.from] factory method when passed our [ViewGroup] parameter
     * [parent], and when our [viewType] parameter is an [ITEM_VIEW_TYPE_ITEM] we return the
     * [SleepNightAdapter.ViewHolder] returned by the [SleepNightAdapter.ViewHolder.from] factory
     * method when passed our [ViewGroup] parameter [parent] (they are both subclasses of
     * [RecyclerView.ViewHolder]). If [viewType] is neither of these we `thow` [ClassCastException].
     *
     * @param parent The [ViewGroup] into which the new View will be added after it is bound to
     * an adapter position.
     * @param viewType The view type of the new View.
     * @return A new [ViewHolder] that holds a View of the given view type.
     */
    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.SleepNightItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    class TextViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.header, parent, false)
                return TextViewHolder(view)
            }
        }
    }

    class ViewHolder private constructor(
            val binding: ListItemSleepNightBinding
    ) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: SleepNight, clickListener: SleepNightListener) {
            binding.sleep = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSleepNightBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class SleepNightDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

class SleepNightListener(val clickListener: (sleepId: Long) -> Unit) {
    fun onClick(night: SleepNight) = clickListener(night.nightId)
}

sealed class DataItem {
    data class SleepNightItem(val sleepNight: SleepNight): DataItem() {
        override val id = sleepNight.nightId
    }

    object Header: DataItem() {
        override val id = Long.MIN_VALUE
    }

    abstract val id: Long
}

