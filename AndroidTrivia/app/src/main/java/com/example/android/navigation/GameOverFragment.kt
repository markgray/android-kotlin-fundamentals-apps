/*
 * Copyright 2018, The Android Open Source Project
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

package com.example.android.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.android.navigation.databinding.FragmentGameOverBinding

/**
 * This is the [Fragment] we navigate to if the user loses the game. It just displays a message and
 * provides a "Try Again" button that naviagates back to `GameFragment` when it is clicked.
 */
class GameOverFragment : Fragment() {
    /**
     * Called to have the fragment instantiate its user interface view. This will be called between
     * [onCreate] and [onActivityCreated]. We initialize our [FragmentGameOverBinding] variable
     * `val binding` by having the [DataBindingUtil.inflate] method use our [LayoutInflater]
     * parameter [inflater] to inflate our layout file `R.layout.fragment_game_over`, using our
     * [ViewGroup] parameter [container] for its LayoutParams without attaching to it. We then set
     * the `OnClickListener` of the `tryAgainButton` button of `binding` to a lambda which navigates
     * to the `GameFragment`. Finally we return the `root` property of `binding` (the outermost
     * [View] in the layout file associated with the Binding) to the caller.
     *
     * @param inflater The [LayoutInflater] object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI will be attached to. The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     * saved state as given here, but we do not override [onSaveInstanceState] so do not use.
     *
     * @return Return the [View] for the fragment's UI.
     */
    @Suppress("RedundantNullableReturnType") // Method we overrride returns View?
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentGameOverBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_game_over,
            container,
            false
        )

        binding.tryAgainButton.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(GameOverFragmentDirections.actionGameOverFragmentToGameFragment())
        }
        return binding.root
    }
}
