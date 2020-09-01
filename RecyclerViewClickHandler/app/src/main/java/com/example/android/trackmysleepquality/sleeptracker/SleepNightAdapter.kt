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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding

/**
 * The adapter we use for the [RecyclerView] with resource ID R.id.sleep_list in the layout file
 * layout/fragment_sleep_tracker.xml which displays the [SleepNight] records read from our database.
 * This class implements a [ListAdapter] for [RecyclerView]  which uses Data Binding to present
 * [List] data, including computing diffs between lists. Note that our [ListAdapter] super class
 * indirectly holds our dataset and we need to retrieve items using its `get` method rather than
 * directly from our [SleepTrackerViewModel] dataset field `nights`. An observer of `nights` calls
 * the `submitList` method of [ListAdapter] with `nights` to have it diffed and displayed whenever
 * the `LiveData` list of [SleepNight] changes.
 *
 * @param clickListener the [SleepNightListener] each item in our [RecyclerView] should use as its
 * `clickListener` variable. The binding expression for the "android:onClick" attribute of the
 * `ConstraintLayout` holding all the views of the layout/list_item_sleep_night.xml layout file
 * calls the `onClick` method of its `clickListener` variable with its `sleep` variable (the
 * [SleepNight] it displays).
 */
@Suppress("MemberVisibilityCanBePrivate")
class SleepNightAdapter(
        val clickListener: SleepNightListener
) : ListAdapter<SleepNight, SleepNightAdapter.ViewHolder>(SleepNightDiffCallback()) {

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the [ViewHolder] `itemView` to reflect the item at the given position.
     *
     * We call the `bind` method of our [ViewHolder] parameter [holder] to have it bind the
     * [ViewHolder] to the  [SleepNight] returned  by the [ListAdapter.getItem] method for our
     * [Int] parameter [position], as well as our [SleepNightListener] field [clickListener].
     * It will set the `sleep` variable of its [ListItemSleepNightBinding] to its [SleepNight]
     * parameter and the `clickListener` variable to its [SleepNightListener] parameter then call
     * the `executePendingBindings` of that binding to have it update the view held by the
     * [ViewHolder] to reflect the changes to its variables.
     *
     * @param holder The [ViewHolder] which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(
            val binding: ListItemSleepNightBinding
    ) : RecyclerView.ViewHolder(binding.root)
    {

        fun bind(item: SleepNight, clickListener: SleepNightListener) {
            binding.sleep = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSleepNightBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                )
                return ViewHolder(binding)
            }
        }
    }
}


class SleepNightDiffCallback : DiffUtil.ItemCallback<SleepNight>() {

    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem.nightId == newItem.nightId
    }

    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem == newItem
    }
}


class SleepNightListener(val clickListener: (sleepId: Long) -> Unit) {
    fun onClick(night: SleepNight) = clickListener(night.nightId)
}
