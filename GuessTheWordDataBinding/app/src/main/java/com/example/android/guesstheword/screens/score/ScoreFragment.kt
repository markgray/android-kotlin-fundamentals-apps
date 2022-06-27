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

package com.example.android.guesstheword.screens.score

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.ScoreFragmentBinding

/**
 * Fragment where the final score is shown, after the game is over
 */
class ScoreFragment : Fragment() {

    /**
     * Our handle to the singleton [ScoreViewModel] view model which contains our data.
     */
    private lateinit var viewModel: ScoreViewModel

    /**
     * The [ScoreViewModelFactory] which constructs our [ScoreViewModel] when necessary, passing the
     * constructor the safe args `score` which the `GameFragment` added to the navigation action it
     * used to navigate to us.
     */
    private lateinit var viewModelFactory: ScoreViewModelFactory

    /**
     * Called to have the fragment instantiate its user interface view. This will be called between
     * [onCreate] and [onActivityCreated]. It is recommended to only inflate the layout in this
     * method and move logic that operates on the returned View to [onViewCreated].
     *
     * We have the [DataBindingUtil.inflate] method use our [LayoutInflater] parameter [inflater] to
     * inflate our layout file [R.layout.score_fragment] with our [ViewGroup] parameter [container]
     * supplying the LayoutParams, and initialize our [ScoreFragmentBinding] variable `val binding`
     * to the binding object it returns. We initialize our [ScoreViewModelFactory] field
     * [viewModelFactory] with a new instance constructed using the `score` safe args passed us by
     * `GameFragment` as its argument. We then create the [ViewModelProvider] with `this` as the
     * `ViewModelStoreOwner`, and [viewModelFactory] as the Factory which will be used to instantiate
     * new ViewModels, and call its `get` method to retrieve an existing [ScoreViewModel] ViewModel
     * or create a new one in this fragments scope, and set our [ScoreViewModel] field [viewModel]
     * to it.
     *
     * We then set the `scoreViewModel` variable property of `binding` to our [ScoreViewModel] field
     * [viewModel] (this allows the binding expressions in the layout file to access all the data
     * in the `ViewModel`). We set the `LifecycleOwner` that should be used for observing changes of
     * `LiveData` in `binding` to a LifecycleOwner that represents this Fragment's View lifecycle.
     *
     * We add an `Observer` to the `eventPlayAgain` property of [viewModel] (the public read-only
     * version of `_eventPlayAgain`) whose lambda will, when it transitions to `true`, navigate to
     * the `GameFragment` and then call the `onPlayAgainComplete` method of [viewModel] to have it
     * reset its `_eventPlayAgain` property to `false`. Finally we return the outermost View in the
     * layout file associated with `binding` to the caller.
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
    @Suppress("RedundantNullableReturnType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate view and obtain an instance of the binding class.
        val binding: ScoreFragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.score_fragment,
            container,
            false
        )

        viewModelFactory = ScoreViewModelFactory(ScoreFragmentArgs.fromBundle(requireArguments()).score)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(ScoreViewModel::class.java)
        binding.scoreViewModel = viewModel

        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        // Navigates back to game when button is pressed
        @Suppress("RedundantSamConstructor")
        viewModel.eventPlayAgain.observe(viewLifecycleOwner, Observer { playAgain ->
            if (playAgain) {
                findNavController().navigate(ScoreFragmentDirections.actionRestart())
                viewModel.onPlayAgainComplete()
            }
        })

        return binding.root
    }
}
