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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.android.navigation.databinding.FragmentTitleBinding

/**
 * The `startDestination` [Fragment] of our app, it just displays our Title "Android Trivia", and
 * a "Play" button which will navigate to the `GameFragment` in a later codelab when clicked.
 */
class TitleFragment : Fragment() {
    /**
     * Called to have the fragment instantiate its user interface view. We initialize our
     * [FragmentTitleBinding] variable `val binding` to the binding returned by the method
     * [DataBindingUtil.inflate] when it uses our [LayoutInflater] parameter [inflater] to
     * inflate our layout file [R.layout.fragment_title] using our [ViewGroup] parameter
     * [container] for its LayoutParams without attaching to it. Finally we return the `root`
     * property of `binding` (the outermost [View] in the layout file associated with the Binding)
     * to the caller.
     *
     * @param inflater The [LayoutInflater] object that can be used to inflate
     * any views in the fragment
     * @param container If non-null, this is the parent view that the fragment's
     * UI will be attached to. The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     * @return Return the [View] for the fragment's UI.
     */
    @Suppress("RedundantNullableReturnType")  // Method we override returns nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentTitleBinding>(
            inflater,
            R.layout.fragment_title,
            container,
            false
        )
        return binding.root
    }


}
