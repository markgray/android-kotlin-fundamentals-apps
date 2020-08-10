/*
 *  Copyright 2019, The Android Open Source Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.example.android.marsrealestate.detail

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.marsrealestate.databinding.FragmentDetailBinding
import com.example.android.marsrealestate.network.MarsProperty

/**
 * This [Fragment] shows the detailed information about a selected piece of Mars real estate.
 * It sets this information in the [DetailViewModel], which it gets as a Parcelable property
 * through Jetpack Navigation's SafeArgs.
 */
class DetailFragment : Fragment() {
    /**
     * Called to have the fragment instantiate its user interface view. This will be called between
     * [onCreate] and [onActivityCreated]. First we initialize our [Application] variable
     * `val application` to the application that owns this activity. We initialize our
     * [FragmentDetailBinding] variable `val binding` by having the [FragmentDetailBinding.inflate]
     * method use our [LayoutInflater] parameter [inflater] to inflate its associated layout file
     * layout/fragment_detail.xml and return a binding to that view. We set the LifecycleOwner that
     * should be used for observing changes of LiveData in `binding` to `this`. We initialize
     * our [MarsProperty] variable `val marsProperty` by using the `fromBundle` method of the
     * [DetailFragmentArgs] which is passing safe args to us to retrieve the arguments from the
     * arguments passed to our fragment, then retrieving the [MarsProperty] stored under the key
     * `selectedProperty` from that HashMap. We next initialize our [DetailViewModelFactory] variable
     * `val viewModelFactory` with an instance which will provide `marsProperty` as the [MarsProperty]
     * and `application` as the [Application] when it is asked to construct a [DetailViewModel].
     * We then set the `viewModel` variable of `binding` to the singleton instance of [DetailViewModel]
     * that a [ViewModelProvider] constructed to use `this` as the `ViewModelStoreOwner`, and
     * `viewModelFactory` as the Factory to use to instantiate a new ViewModel if necessary returns
     * when we call its `get` method for the [DetailViewModel] class. Finally we return the outermost
     * [View] in the layout file associated with `binding` to the caller.
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
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val application : Application = requireNotNull(activity).application
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val marsProperty : MarsProperty = DetailFragmentArgs
                .fromBundle(requireArguments())
                .selectedProperty
        val viewModelFactory = DetailViewModelFactory(marsProperty, application)
        binding.viewModel = ViewModelProvider(this, viewModelFactory)
                .get(DetailViewModel::class.java)

        return binding.root
    }
}