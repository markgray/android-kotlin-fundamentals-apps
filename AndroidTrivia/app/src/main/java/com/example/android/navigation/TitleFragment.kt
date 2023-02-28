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
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.android.navigation.databinding.FragmentTitleBinding

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
     * navigate to the `GameFragment`. We initialize our [MenuHost] variable `val menuHost` to
     * to the `FragmentActivity` this fragment is currently associated with, then call its
     * [MenuHost.addMenuProvider] method to add our [MenuProvider] field [menuProvider] to add
     * the [MenuProvider] to the [MenuHost]. Finally we return the `root` property of `binding`
     * (the outermost [View] in the layout file associated with the Binding) to the caller.
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
    @Suppress("RedundantNullableReturnType") // Method we overrride returns View?
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        /**
         * The [FragmentTitleBinding] binding object associated with our inflated layout file
         * [R.layout.fragment_title].
         */
        val binding = DataBindingUtil.inflate<FragmentTitleBinding>(
            inflater,
            R.layout.fragment_title,
            container,
            false
        )

        binding.playButton.setOnClickListener { view: View ->
            view.findNavController().navigate(TitleFragmentDirections.actionTitleFragmentToGameFragment())
        }
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
         * [R.menu.options_menu] into our [Menu] parameter [menu].
         *
         * @param menu The options menu in which you place your items.
         * @param menuInflater a [MenuInflater] you can use to inflate an XML menu file with.
         */
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.options_menu, menu)
        }

        /**
         * This hook is called whenever an item in your options menu is selected. We return the result
         * of calling the [NavigationUI.onNavDestinationSelected] method with our [MenuItem] parameter
         * [menuItem] and the `NavController` associated with the root [View] of our [Fragment] (this
         * method will try to navigate to the fragment whose ID is the same as the item ID of
         * [menuItem] which in our case is `aboutFragment`). If it was able to navigate to the
         * destination associated with the given MenuItem it returns `true` which we return to our
         * caller, if it fails it returns `false` and we return the value returned by our super's
         * implementation of `onOptionsItemSelected`.
         *
         * @param menuItem The menu item that was selected.
         * @return [Boolean] Return `false` to allow normal menu processing to
         *         proceed, `true` to consume it here.
         */
        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return NavigationUI.onNavDestinationSelected(menuItem, requireView().findNavController())
        }
    }
}
