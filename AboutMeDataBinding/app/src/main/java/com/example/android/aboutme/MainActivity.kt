/*
 * Copyright (C) 2018 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.aboutme

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.databinding.DataBindingUtil
import com.example.android.aboutme.databinding.ActivityMainBinding


/**
 * Main Activity of the AboutMe app. This app demonstrates:
 *  * LinearLayout with TextViews, ImageView, Button, EditText, and ScrollView
 *  * ScrollView to display scrollable text
 *  * Getting user input with an EditText.
 *  * Click handler for a Button to retrieve text from an EditText and set it in a TextView.
 *  * Setting the visibility status of a view.
 *  * Data binding between MainActivity and activity_main.xml. How to remove findViewById,
 *  and how to display data in views using the data binding object.
 */
class MainActivity : ComponentActivity() {

    /**
     * Binding handle associated with the inflated content view of our layout file (resource ID
     * [R.layout.activity_main] which we can use to reference views in it.
     */
    private lateinit var binding: ActivityMainBinding

    /**
     * Our instance of the data class [MyName], which holds both "name" and "nickname".
     */
    private val myName: MyName = MyName("Aleks Haecky")

    /**
     * Called when the activity is starting. First we call our super's implementation of `onCreate`.
     * We then initialize our [ActivityMainBinding] field [binding] to the binding associated with
     * our content view which the [DataBindingUtil.setContentView] method creates when it inflates
     * our layout file `layout/activity_main.xml` We then use [binding] to set the `myName` variable
     * of our UI to our [MyName] field [myName], and to set the `OnClickListener` of the
     * `doneButton` in our UI to a lambda which calls our [addNickname] method with the [View] that
     * was clicked (the `doneButton` itself of course).
     *
     * @param savedInstanceState we do not override [onSaveInstanceState] so do not use.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Apply the insets as a margin to the view.
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = insets.left
                bottomMargin = insets.bottom
                rightMargin = insets.right
                topMargin = insets.top
            }

            // Return CONSUMED if you don't want want the window insets to keep passing
            // down to descendant views.
            WindowInsetsCompat.CONSUMED
        }

        binding.myName = myName

        binding.doneButton.setOnClickListener {
            addNickname(it)
        }

    }

    /**
     * Click handler for the Done button. Demonstrates how data binding can be used to make code
     * much more readable by eliminating calls to findViewById and changing data in the binding
     * object. We use the `apply` extension function of our [ActivityMainBinding] field [binding]
     * to set the `nickname` field of its `myName` variable to the text contents of the
     * `nicknameEdit` `EditText` in the UI, call its `invalidateAll` method to invalidate all
     * of its binding expressions and request a new rebind to refresh the UI, set the visibility
     * of the `nicknameEdit` `EditText` to GONE, the visibility of the `doneButton` `Button` to
     * GONE, and the visibility of the `nicknameText` `TextView` to `VISIBLE`.
     *
     * We then initialize our [InputMethodManager] variable `val imm` with a handle to an instance
     * to use to access input methods, then call its `hideSoftInputFromWindow` method with a token
     * of our [View] parameter [view] to hide the soft input window (the keyboard that popped up
     * when the user clicked the `nicknameEdit` `EditText` to begin entering text into it).
     *
     * @param view [View] that was clicked (always the `doneButton` `Button` in our case).
     */
    private fun addNickname(view: View) {
        binding.apply {
            myName?.nickname = nicknameEdit.text.toString()
            invalidateAll()
            nicknameEdit.visibility = View.GONE
            doneButton.visibility = View.GONE
            nicknameText.visibility = View.VISIBLE
        }

        // Hide the keyboard.
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
