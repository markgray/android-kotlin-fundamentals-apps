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

package com.example.android.trackmysleepquality.sleepquality

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.databinding.FragmentSleepQualityBinding

/**
 * Fragment that displays a list of clickable icons, each representing a sleep quality rating. Once
 * the user taps an icon, the quality is set in the current sleepNight and the database is updated.
 */
class SleepQualityFragment : Fragment() {

    /**
     * Called to have the fragment instantiate its user interface view. First we use the
     * [DataBindingUtil.inflate] method to inflate our layout file R.layout.fragment_sleep_quality
     * into a [FragmentSleepQualityBinding] to initialize our variable `val binding`.
     * Then we initialize our [Application] variable `val application` to the application that owns
     * this activity, and our [SleepQualityFragmentArgs] variable `val arguments` to the instance
     * that the [SleepQualityFragmentArgs.fromBundle] method creates from the arguments supplied
     * when the fragment was instantiated (the safe args [Long] for the `sleepNightKey` argument
     * will be stored in the `sleepNightKey` property of `arguments`). We initialize our
     * [SleepDatabaseDao] variable `val dataSource` to the `sleepDatabaseDao` property of our
     * singleton [SleepDatabase] instance (creating the database if need be) (`dataSource` will
     * allow us to access the Room SQLite methods defined in the [SleepDatabaseDao]). We initialize
     * our [SleepQualityViewModelFactory] variable `val viewModelFactory` with an instance constructed
     * to use the `sleepNightKey` entry of `arguments` as the `nightId` of the `SleepNight` row, and
     * `dataSource` as the [SleepDatabaseDao]. We then use `viewModelFactory` as the Factory which
     * will be used to instantiate new ViewModels in a call to the [ViewModelProvider.get] method in
     * order to initialize our [SleepQualityViewModel] variable `val sleepQualityViewModel` to our
     * singleton [SleepQualityViewModel]. We set the `sleepQualityViewModel` variable of `binding`
     * to this `sleepQualityViewModel` (this will allow DataBinding binding expressions in the
     * associated layout file to use the [LiveData] of our ViewModel). We add an [Observer] to
     * the `navigateToSleepTracker` [LiveData] wrapped [Boolean] property of `sleepQualityViewModel`
     * whose lambda will, if the property transitions to `true`, find our `NavController` and use
     * it to navigate to `SleepTrackerFragment`, and then call the `doneNavigating` method of
     * `sleepQualityViewModel` to reset `navigateToSleepTracker` to `false` to make sure we only
     * navigate once, even if the device has a configuration change.
     *
     * Finally we return the outermost [View] in the layout file associated with `binding` to the
     * caller.
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

    @Suppress("RedundantNullableReturnType") // The method we override returns nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentSleepQualityBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_sleep_quality,
            container,
            false
        )

        val application: Application = requireNotNull(this.activity).application
        val arguments = SleepQualityFragmentArgs.fromBundle(requireArguments())

        // Create an instance of the ViewModel Factory.
        val dataSource: SleepDatabaseDao = SleepDatabase.getInstance(application).sleepDatabaseDao
        val viewModelFactory = SleepQualityViewModelFactory(arguments.sleepNightKey, dataSource)

        // Get a reference to the ViewModel associated with this fragment.
        val sleepQualityViewModel =
            ViewModelProvider(this, viewModelFactory)[SleepQualityViewModel::class.java]

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.sleepQualityViewModel = sleepQualityViewModel

        // Add an Observer to the state variable for Navigating when a Quality icon is tapped.
        sleepQualityViewModel.navigateToSleepTracker.observe(viewLifecycleOwner) { doNavigation: Boolean? ->
            if (doNavigation == true) { // Observed state is true.
                this.findNavController().navigate(
                    SleepQualityFragmentDirections.actionSleepQualityFragmentToSleepTrackerFragment())
                // Reset state to make sure we only navigate once, even if the device
                // has a configuration change.
                sleepQualityViewModel.doneNavigating()
            }
        }

        return binding.root
    }
}
