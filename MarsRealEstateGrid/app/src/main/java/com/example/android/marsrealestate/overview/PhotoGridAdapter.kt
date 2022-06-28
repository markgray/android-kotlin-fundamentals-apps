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

package com.example.android.marsrealestate.overview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.marsrealestate.databinding.GridViewItemBinding
import com.example.android.marsrealestate.network.MarsProperty

/**
 * This class implements a [ListAdapter] for [RecyclerView]  which uses Data Binding to present
 * [List] data, including computing diffs between lists.
 */
class PhotoGridAdapter : ListAdapter<MarsProperty, PhotoGridAdapter.MarsPropertyViewHolder>(DiffCallback) {

    /**
     * The MarsPropertyViewHolder constructor takes the binding variable from the associated
     * GridViewItem, which nicely gives it access to the full [MarsProperty] information.
     *
     * @param binding the [GridViewItemBinding] for the view we are to display our item in
     */
    class MarsPropertyViewHolder(
        private var binding: GridViewItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        /**
         * Binds a [MarsProperty] to this [MarsPropertyViewHolder]. Called from the `onBindViewHolder`
         * override of [PhotoGridAdapter]. We set the `property` variable of our [GridViewItemBinding]
         * field [binding] to our parameter [marsProperty] then call the `executePendingBindings`
         * method of [binding] to have it evaluate the pending bindings, updating any Views that
         * have expressions bound to modified variables.
         *
         * @param marsProperty the [MarsProperty] item we are to display.
         */
        fun bind(marsProperty: MarsProperty) {
            binding.property = marsProperty
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [MarsProperty]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<MarsProperty>() {
        /**
         * Called to check whether two objects represent the same item. We return the results of
         * comparing our parameters [oldItem] and [newItem] using kotlin's referential equality
         * operator (`true` if both point to the same object).
         *
         * @param oldItem The item in the old list.
         * @param newItem The item in the new list.
         * @return `true` if the two items represent the same object or `false` if they are different.
         */
        override fun areItemsTheSame(oldItem: MarsProperty, newItem: MarsProperty): Boolean {
            return oldItem === newItem
        }

        /**
         * Called to check whether two items have the same data. This information is used to detect
         * if the contents of an item have changed. This method is called only if [areItemsTheSame]
         * returns `true` for these items. We return the results of comparing the `id` field of
         * our parameters [oldItem] and [newItem] using kotlin's structural equality operator
         * (`true` if both have identical `id` fields).
         *
         * @param oldItem The item in the old list.
         * @param newItem The item in the new list.
         * @return True if the contents of the items are the same or false if they are different.
         */
        override fun areContentsTheSame(oldItem: MarsProperty, newItem: MarsProperty): Boolean {
            return oldItem.id == newItem.id
        }
    }

    /**
     * Called when [RecyclerView] needs a new `ViewHolder` of the given type to represent
     * an item (invoked by the layout manager). We use the [GridViewItemBinding.inflate] method to
     * have it inflate the layout file it is associated with (layout/grid_view_item.xml) into a
     * [GridViewItemBinding] binding to the generated view and pass that binding to the constructor
     * of [MarsPropertyViewHolder], returning the instance it creates to our caller.
     *
     * @param parent The [ViewGroup] into which the new `View` will be added after it is bound to an
     * adapter position.
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MarsPropertyViewHolder {
        return MarsPropertyViewHolder(GridViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    /**
     * Called by [RecyclerView] to display the data at the specified position (invoked by the layout
     * manager). This method should update the contents of the [MarsPropertyViewHolder] to reflect
     * the item at the given position. We initialize our [MarsProperty] variable `val marsProperty`
     * to the [MarsProperty] at position [position] in our dataset, then we call the `bind` method
     * of our [MarsPropertyViewHolder] parameter [holder] to have it bind to `marsProperty`.
     *
     * @param holder The `ViewHolder` which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: MarsPropertyViewHolder, position: Int) {
        val marsProperty = getItem(position)
        holder.bind(marsProperty)
    }
}
