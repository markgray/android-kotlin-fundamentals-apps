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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.FragmentSleepTrackerBinding
import com.google.android.material.snackbar.Snackbar

/**
 * A fragment with buttons to record start and end times for sleep, which are saved in a database.
 * Cumulative data is displayed in a [RecyclerView].  The Clear button will clear all data from the
 * database.
 */
class SleepTrackerFragment : Fragment() {

    /**
     * Called to have the fragment instantiate its user interface view. This will be called between
     * [onCreate] and [onActivityCreated]. We initialize our [FragmentSleepTrackerBinding] variable
     * `val binding` by having the [DataBindingUtil.inflate] method use our [LayoutInflater] parameter
     * [inflater] to inflate our layout file R.layout.fragment_sleep_tracker with our [ViewGroup]
     * parameter [container] supplying the LayoutParams. We initialize our [Application] variable
     * `val application` to the application that owns this activity. We use `application` in a call
     * to the [SleepDatabase.getInstance] to retrieve the singleton [SleepDatabase] and initialize
     * our [SleepDatabaseDao] variable `val dataSource` to the `sleepDatabaseDao` field of the
     * [SleepDatabase]. We then initialize our [SleepTrackerViewModelFactory] variable
     * `val viewModelFactory` to an instance constructed to use `dataSource` and `application`,
     * and then use `viewModelFactory` in a call to [ViewModelProvider] to retrieve the singleton
     * [SleepTrackerViewModel] (creating it if need be) and initialize our [SleepTrackerViewModel]
     * variable `val sleepTrackerViewModel` to it.
     *
     * We set the `sleepTrackerViewModel` variable of `binding` to `sleepTrackerViewModel` to allow
     * the binding to use the view model. We initialize our [SleepNightAdapter] variable
     * `val adapter` to a new instance constructed to use a [SleepNightListener] whose lambda
     * calls the `onSleepNightClicked` method of `sleepTrackerViewModel` with the `nightId` primary
     * key of the [SleepNight] whose view has been clicked, then set the adapter of the `sleepList`
     * [RecyclerView] of `binding` to it.
     *
     * We add an [Observer] to the [LiveData] wrapped [List] of [SleepNight] objects in the `nights`
     * field of `sleepTrackerViewModel` whose lambda calls the `addHeaderAndSubmitList` method of
     * `adapter` with that [List] of [SleepNight] objects whenever it is updated to a non-null value
     * (`addHeaderAndSubmitList` will create a list of `DataItem` objects with the first entry being
     * a `DataItem.Header` header item, followed by `DataItem.SleepNightItem` items constructed from
     * each of the [SleepNight] items in the `nights` list. The `DataItem.SleepNightItem` constructor
     * just stores the `nightId` property of the [SleepNight] in its [Long] field `id`, and
     * `DataItem.Header` sets `id` to [Long.MIN_VALUE], `addHeaderAndSubmitList` then call its
     * super's `submitList` method tosubmit the new list to be diffed, and displayed). We then set
     * the LifecycleOwner that should be used for observing changes of [LiveData] in `binding` to
     * `this`.
     *
     * We add an [Observer] to the state variable `showSnackBarEvent` of `sleepTrackerViewModel`
     * for showing a Snackbar message when the CLEAR button is pressed to a lambda which shows
     * a [Snackbar] informing the user that we just deleted all of his data, then calls the
     * `doneShowingSnackbar` method of `sleepTrackerViewModel` to reset `showSnackBarEvent` to
     * make sure the Snackbar is only shown once.
     *
     * We add an [Observer] to the state variable `navigateToSleepQuality` of `sleepTrackerViewModel`
     * for Navigating to the `SleepQualityFragment` when the STOP button is pressed. In the lambda
     * of the [Observer] we find a NavController for our fragment and call its `navigate` method
     * to have it navigate using a `ActionSleepTrackerFragmentToSleepQualityFragment` constructed
     * to pass the `nightId` primary key of the [SleepNight] value of `navigateToSleepQuality` as
     * the safe args for `SleepQualityFragment` when it is navigated to. Having navigated to the
     * `SleepQualityFragment` the lambda then calls the `doneNavigating` method of
     * `sleepTrackerViewModel` to reset it to `null` to make sure we only navigate once, even if
     * the device has a configuration change.
     *
     * We add an [Observer] to the state variable `navigateToSleepDetail` of `sleepTrackerViewModel`
     * for Navigating to the `SleepDetailFragment` when an item in the [RecyclerView] is clicked.
     * When `navigateToSleepDetail` transitions to a non-null value the lambda of the [Observer]
     * will find a NavController for our fragment and navigate to the `SleepDetailFragment` passing
     * it the safe args `nightId` primary key value that `navigateToSleepDetail` contains which
     * `SleepDetailFragment` can use to retrieve the [SleepNight] whose details it should display.
     * The lambda then calls the `onSleepDetailNavigated` method of `sleepTrackerViewModel` to reset
     * it to `null` to make sure we only navigate once, even if the device has a configuration change.
     *
     * We initialize our [GridLayoutManager] variable `val manager` to a new instance which has 3
     * columns in the grid. We set the `spanSizeLookup` property of `manager` (the source to get the
     * number of spans occupied by each item in the adapter) to a [GridLayoutManager.SpanSizeLookup]
     * anonymous class which overrides the `getSpanSize` method to return 3 columns for the item at
     * position 0, and 1 column for all other items. We then set the [RecyclerView.LayoutManager]
     * that the `sleepList` [RecyclerView] of `binding` will use to `manager`.
     *
     * Finally we return the outermost View of the layout file associated with `binding` to the
     * caller.
     *
     * @param inflater The [LayoutInflater] object that can be used to inflate
     * any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to. The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Suppress("RedundantNullableReturnType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentSleepTrackerBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_sleep_tracker,
            container,
            false
        )

        val application: Application = requireNotNull(this.activity).application

        // Create an instance of the ViewModel Factory.
        val dataSource: SleepDatabaseDao = SleepDatabase.getInstance(application).sleepDatabaseDao
        val viewModelFactory = SleepTrackerViewModelFactory(dataSource, application)

        // Get a reference to the ViewModel associated with this fragment.
        val sleepTrackerViewModel =
            ViewModelProvider(
                this, viewModelFactory).get(SleepTrackerViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.sleepTrackerViewModel = sleepTrackerViewModel

        val adapter = SleepNightAdapter(SleepNightListener { nightId ->
            //Toast.makeText(context, "${nightId}", Toast.LENGTH_LONG).show()
            sleepTrackerViewModel.onSleepNightClicked(nightId)
        })
        binding.sleepList.adapter = adapter


        @Suppress("RedundantSamConstructor")
        sleepTrackerViewModel.nights.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = this

        // Add an Observer on the state variable for showing a Snackbar message
        // when the CLEAR button is pressed.
        @Suppress("RedundantSamConstructor")
        sleepTrackerViewModel.showSnackBarEvent.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.cleared_message),
                    Snackbar.LENGTH_SHORT // How long to display the message.
                ).show()
                // Reset state to make sure the toast is only shown once, even if the device
                // has a configuration change.
                sleepTrackerViewModel.doneShowingSnackbar()
            }
        })

        // Add an Observer on the state variable for Navigating when STOP button is pressed.
        @Suppress("RedundantSamConstructor")
        sleepTrackerViewModel.navigateToSleepQuality.observe(viewLifecycleOwner, Observer { night ->
            night?.let {
                // We need to get the navController from this, because button is not ready, and it
                // just has to be a view. For some reason, this only matters if we hit stop again
                // after using the back button, not if we hit stop and choose a quality.
                // Also, in the Navigation Editor, for Quality -> Tracker, check "Inclusive" for
                // popping the stack to get the correct behavior if we press stop multiple times
                // followed by back.
                // Also: https://stackoverflow.com/questions/28929637/difference-and-uses-of-oncreate-oncreateview-and-onactivitycreated-in-fra
                this.findNavController().navigate(
                    SleepTrackerFragmentDirections
                        .actionSleepTrackerFragmentToSleepQualityFragment(night.nightId))
                // Reset state to make sure we only navigate once, even if the device
                // has a configuration change.
                sleepTrackerViewModel.doneNavigating()
            }
        })

        // Add an Observer on the state variable for Navigating when and item is clicked.
        @Suppress("RedundantSamConstructor")
        sleepTrackerViewModel.navigateToSleepDetail.observe(viewLifecycleOwner, Observer { night ->
            night?.let {

                this.findNavController().navigate(
                    SleepTrackerFragmentDirections
                        .actionSleepTrackerFragmentToSleepDetailFragment(night))
                sleepTrackerViewModel.onSleepDetailNavigated()
            }
        })

        val manager = GridLayoutManager(activity, 3)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = when (position) {
                0 -> 3
                else -> 1
            }
        }


        binding.sleepList.layoutManager = manager

        return binding.root
    }
}
