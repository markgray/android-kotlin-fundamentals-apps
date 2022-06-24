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
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.android.navigation.databinding.ActivityMainBinding

/**
 * This is the launch activity for our game. It sets the content view for the application, configures
 * the navigation to use a navigation drawer, and arranges to have "navigate up" button handling
 * delegated to the `NavController` and to have it open our navigation drawer when our app is at
 * the top level.
 */
class MainActivity : AppCompatActivity() {
    /**
     * The [DrawerLayout] in our layout file with ID [R.id.drawerLayout] (aka binding property
     * `drawerLayout` in our [ActivityMainBinding] binding).
     */
    private lateinit var drawerLayout: DrawerLayout

    /**
     * Called when the activity is starting. First we call our super's implementation of `onCreate`.
     * We then initialize our [ActivityMainBinding] variable `val binding` to the binding object
     * that the [DataBindingUtil.setContentView] method returns when it inflates our layout file
     * [R.layout.activity_main] into a view which it sets as content view (binding object is
     * associated with the inflated content view of course). We intitialize our [DrawerLayout] field
     * [drawerLayout] to the `drawerLayout` property of `binding` (resourse ID [R.id.drawerLayout]
     * in our layout file). We initialize our `NavController` variable `val navController` to the
     * `NavController` in our layout file (the [androidx.navigation.fragment.NavHostFragment] with
     * ID [R.id.myNavHostFragment] in our layout file). We then call the method
     * [NavigationUI.setupActionBarWithNavController] to have it set up the `SupportActionBar` for
     * use with the NavController `navController`, with [drawerLayout] the DrawerLayout that should
     * be toggled from the home button. Finally we call the method
     * [NavigationUI.setupWithNavController] to designate the `NavigationView` in our layout file
     * whose `binding` property is `navView` (resource ID [R.id.navView]) to be the view that should
     * be kept in sync with changes to the NavController `navController`.
     *
     * @param savedInstanceState we do not override [onSaveInstanceState] so do not use.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(
            this,
            R.layout.activity_main
        )

        drawerLayout = binding.drawerLayout

        val navController = this.findNavController(R.id.myNavHostFragment)

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        NavigationUI.setupWithNavController(binding.navView, navController)

    }

    /**
     * This method is called whenever the user chooses to navigate Up within your application's
     * activity hierarchy from the action bar. We initialize our `NavController` variable
     * `val navController` to the `NavController` in our layout file (the
     * [androidx.navigation.fragment.NavHostFragment] with ID [R.id.myNavHostFragment] in our
     * layout file). Then we return the value returned by the [NavigationUI.navigateUp] method
     * when it handles the Up button by delegating its behavior to the NavController `navController`,
     * with [drawerLayout] as the [DrawerLayout] that should be opened if you are on the topmost
     * level of the app.
     *
     * @return `true` if Up navigation completed successfully and this Activity was finished,
     *         `false` otherwise.
     */
    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

}
