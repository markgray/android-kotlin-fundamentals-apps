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
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.android.navigation.databinding.FragmentTitleBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
@Suppress("unused")
private const val ARG_PARAM1 = "param1"

@Suppress("unused")
private const val ARG_PARAM2 = "param2"

/**
 * The `startDestination` [Fragment] of our app, it just displays our Title "Android Trivia", and
 * a "Play" button which navigates to the `GameFragment` when clicked.
 */
class TitleFragment : Fragment() {

    /**
     * Called to have the fragment instantiate its user interface view. We initialize our
     * [FragmentTitleBinding] variable `val binding` to the binding returned by the method
     * [DataBindingUtil.inflate] when it uses our [LayoutInflater] parameter [inflater] to
     * inflate our layout file [R.layout.fragment_title] using our [ViewGroup] parameter
     * [container] for its LayoutParams without attaching to it. We then set the `OnClickListener`
     * of the `playButton` property of `binding` (the button with ID [R.id.playButton]) to a
     * lambda which finds the `NavController` associated with the [View] clicked and uses it to
     * navigate to the `GameFragment`. We call the [setHasOptionsMenu] method with `true` to
     * report that this fragment would like to participate in populating the options menu by
     * receiving a call to [onCreateOptionsMenu] and related methods. Finally we return the `root`
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
    @Suppress("RedundantNullableReturnType")
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

        binding.playButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_titleFragment_to_gameFragment)
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    /**
     * Initialize the contents of the Fragment host's standard options menu. First we call our
     * super's implementation of `onCreateOptionsMenu`, then we use our [MenuInflater] parameter
     * [inflater] to inflate our menu layout file [R.menu.options_menu] into our [Menu] parameter
     * [menu].
     *
     * @param menu The options menu in which you place your items.
     * @param inflater a [MenuInflater] you can use to inflate an XML menu file with.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)
    }

    /**
     * This hook is called whenever an item in your options menu is selected. We return the result
     * of calling the [NavigationUI.onNavDestinationSelected] method with our [MenuItem] parameter
     * [item] and the `NavController` associated with the root [View] of our [Fragment] (this
     * method will try to navigate to the fragment whose ID is the same as the item ID of [item]
     * which in our case is `aboutFragment`). If it was able to navigate to the destination
     * associated with the given MenuItem it returns `true` which we return to our caller, if it
     * fails it returns `false` and we return the value returned by our super's implementation of
     * `onOptionsItemSelected`.
     *
     * @param item The menu item that was selected.
     * @return [Boolean] Return `false` to allow normal menu processing to
     *         proceed, `true` to consume it here.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
            || super.onOptionsItemSelected(item)
    }

}
