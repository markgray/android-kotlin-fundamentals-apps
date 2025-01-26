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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.marsrealestate.R
import com.example.android.marsrealestate.databinding.FragmentOverviewBinding

/**
 * This fragment shows the the status of the Mars real-estate web services transaction.
 */
class OverviewFragment : Fragment() {

    /**
     * Lazily initialize our [OverviewViewModel].
     */
    private val viewModel: OverviewViewModel by lazy {
        ViewModelProvider(this)[OverviewViewModel::class.java]
    }

    /**
     * Called to have the fragment instantiate its user interface view. This will be called between
     * [onCreate] and [onActivityCreated]. We initialize our [FragmentOverviewBinding] variable
     * `val binding` by having the [FragmentOverviewBinding.inflate] method use our [LayoutInflater]
     * parameter [inflater] to inflate the layout file it is associated with (the layout file
     * layout/fragment_overview.xml) into a [FragmentOverviewBinding] instance. the LifecycleOwner
     * that should be used for observing changes of LiveData in `binding` to `this` [OverviewFragment],
     * and set the `viewModel` variable property of `binding` to our [OverviewViewModel] field
     * [viewModel] (gives the binding expressions in `binding` access to our [OverviewViewModel]).
     * We initialize our [MenuHost] variable `val menuHost` to to the `FragmentActivity` this fragment
     * is currently associated with, then call its [MenuHost.addMenuProvider] method to add our
     * [MenuProvider] field [menuProvider] to add the [MenuProvider] to the [MenuHost]. Finally we
     * return the outermost [View] in the layout file associated with `binding` (the `root` property
     * of `binding`) to the caller.
     *
     * @param inflater The [LayoutInflater] object that can be used to inflate
     * any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's
     * UI will be attached to. The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Return the [View] for the fragment's UI, or null.
     */
    @Suppress("RedundantNullableReturnType") // The method we are overriding returns nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentOverviewBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(menuProvider, viewLifecycleOwner)
        return binding.root
    }

    /**
     * Our  [MenuProvider]
     */
    private val menuProvider: MenuProvider = object : MenuProvider {
        /**
         * Initialize the contents of the Fragment host's standard options menu. We use our
         * [MenuInflater] parameter [menuInflater] to inflate our menu layout file
         * `R.menu.overflow_menu` into our [Menu] parameter [menu].
         *
         * @param menu The options menu in which you place your items.
         * @param menuInflater a [MenuInflater] you can use to inflate an XML menu file with.
         */
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.overflow_menu, menu)
        }

        /**
         * Called by the [MenuHost] when a [MenuItem] is selected from the menu. We do not implement
         * anything yet, this will be done in `MarsRealEstateFinal`.
         *
         * @param menuItem the menu item that was selected
         * @return `true` if the given menu item is handled by this menu provider, `false` otherwise.
         */
        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return false
        }
    }
}
