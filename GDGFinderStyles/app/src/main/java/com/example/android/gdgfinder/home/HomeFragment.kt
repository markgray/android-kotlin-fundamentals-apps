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

package com.example.android.gdgfinder.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.gdgfinder.databinding.HomeFragmentBinding

/**
 * This is the starting "Home" fragment of our Navigation graph. Our layout file consists of some
 * `TextView`'s which describe the purpose of Google Developer Groups.
 */
class HomeFragment : Fragment() {

    companion object {
        /**
         * Called to create a new instance of [HomeFragment] which it returns.
         */
        @Suppress("unused")
        fun newInstance(): HomeFragment = HomeFragment()
    }

    /**
     * Our `ViewModel`. It holds a `LiveData` wrapped [Boolean] property `_navigateToSearch` which
     * we will eventually observe via its publicly exposed property `navigateToSearch` in order to
     * trigger the navigation to  the `GdgListFragment` [Fragment] when it transitions to `true`
     * (this UI functionality will be added for the Floating Action Button that is added in the next
     * code lab).
     */
    private lateinit var viewModel: HomeViewModel

    /**
     * Called to have the fragment instantiate its user interface view. We initialize our
     * [HomeFragmentBinding] variable `val binding` to the layout binding to our layout file
     * layout/home_fragment.xml that the [HomeFragmentBinding.inflate] method creates when it
     * uses our [LayoutInflater] parameter [inflater] to inflate that file. We initialize our
     * [HomeViewModel] field [viewModel] to the singleton instance returned by the `get` method
     * of [ViewModelProvider] for `this` `ViewModelStoreOwner`. Finally we return the outermost
     * [View] in the layout file associated with our [HomeFragmentBinding] variable `binding` as
     * our fragment's UI.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI will be attached to. The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Return the [View] for the fragment's UI.
     */
    @Suppress("RedundantNullableReturnType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = HomeFragmentBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        return binding.root
    }
}
