/*
 * Copyright (C) 2018 Google Inc.
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

package com.android.example.aboutme

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.graphics.Insets
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams

/**
 * Main Activity of the AboutMe app. This app demonstrates:
 *  * Getting user input with an EditText.
 *  * Click handler for a Button to retrieve text from an EditText and set it in a TextView.
 *  * Setting a click handler on a TextView.
 *  * Setting the visibility status of a view.
 */
class MainActivity : ComponentActivity() {

    /**
     * Called when the activity is starting. First we call [enableEdgeToEdge] to enable edge to edge
     * display, then we call our super's implementation of `onCreate`, then we set our content view
     * to our layout file `R.layout.activity_main`. We initialize our [LinearLayout] variable
     * `rootView` by finding the view with ID  `R.id.root_view`. We use the
     * [ViewCompat.setOnApplyWindowInsetsListener] method to set an [OnApplyWindowInsetsListener] to
     * take over over the policy for applying window insets to `rootView`, with the`listener` argument
     * a lambda that accepts the [View] passed the lambda in variable `v` and the [WindowInsetsCompat]
     * passed the lambda in variable `windowInsets`. It initializes its [Insets] variable `insets`
     * to the [WindowInsetsCompat.getInsets] of `windowInsets` with
     * [WindowInsetsCompat.Type.systemBars] as the argument, then it updates the layout parameters
     * of `v` to be a [ViewGroup.MarginLayoutParams] with the left margin set to `insets.left`, the
     * right margin set to `insets.right`, the top margin set to `insets.top`, and the bottom margin
     * set to `insets.bottom`. Finally it returns [WindowInsetsCompat.CONSUMED] to the caller (so
     * that the window insets will not keep passing down to descendant views).
     *
     * Next we find the [Button] with ID `R.id.done_button` (labeled "Done") and set its
     * `OnClickListener` to a lambda which calls our method [addNickname] with the view that was
     * clicked, and we find the [TextView] with ID `R.id.nickname_text` and set its `OnClickListener`
     * to a lambda which calls our method [updateNickname] with the view that was clicked.
     *
     * @param savedInstanceState we do not override [onSaveInstanceState] so do not use
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rootView: LinearLayout = findViewById(R.id.root_view)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, windowInsets ->
            val insets: Insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
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

        findViewById<Button>(R.id.done_button).setOnClickListener {
            addNickname(it)
        }

        findViewById<TextView>(R.id.nickname_text).setOnClickListener {
            updateNickname(it)
        }
    }

    /**
     * Click handler for the "DONE" [Button]. Hides the [EditText] and the "DONE" [Button]. Sets the
     * text of the [TextView] to the contents of the [EditText] and displays the [TextView]. First
     * we initialize our [EditText] variable `val editText` by finding the view with ID R.id.nickname_edit,
     * and we initialize our [TextView] variable `val nicknameTextView` by finding the view with ID
     * R.id.nickname_text. We set the text of `nicknameTextView` to the text of `editText`, set the
     * visibility of `editText` to [View.GONE], set the visibility of our [View] parameter [view] to
     * [View.GONE] (the "DONE" [Button]) and set the visibility of `nicknameTextView` to [View.VISIBLE].
     * We initialize our [InputMethodManager] variable `val imm` by fetching a handle to the system
     * level service [Context.INPUT_METHOD_SERVICE], then call the `hideSoftInputFromWindow` method
     * of `imm` to hide the soft input window from the context of the window that is currently accepting
     * input (using the unique token identifying the window that [view] is attached to).
     *
     * @param view the [View] that was clicked.
     */
    private fun addNickname(view: View) {
        val editText = findViewById<EditText>(R.id.nickname_edit)
        val nicknameTextView = findViewById<TextView>(R.id.nickname_text)

        nicknameTextView.text = editText.text
        editText.visibility = View.GONE
        view.visibility = View.GONE
        nicknameTextView.visibility = View.VISIBLE

        // Hide the keyboard.
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)

    }

    /**
     * Click handler for the nickname [TextView]. Displays the [EditText] and the DONE [Button], and
     * hides the nickname [TextView]. First we initialize our [EditText] variable `val editText` by
     * finding the view with ID R.id.nickname_edit, and we initialize our [Button] variable
     * `val doneButton` by finding the view with ID R.id.done_button. We set the visibility of
     * `editText` to [View.VISIBLE], set the visibility of `doneButton` to [View.VISIBLE], and
     * the visibility of our [View] parameter [view] (the nickname [TextView]) to [View.GONE].
     * We call the `requestFocus` method of `editText` to give focus to it. We initialize our
     * [InputMethodManager] variable `val imm` by fetching a handle to the system level service
     * [Context.INPUT_METHOD_SERVICE], then call the `showSoftInput` method of `imm` to request that
     * the current input method's soft input area be shown to the user.
     *
     * @param view the [View] that was clicked.
     */
    private fun updateNickname(view: View) {
        val editText = findViewById<EditText>(R.id.nickname_edit)
        val doneButton = findViewById<Button>(R.id.done_button)

        editText.visibility = View.VISIBLE
        doneButton.visibility = View.VISIBLE
        view.visibility = View.GONE

        // Set the focus to the edit text.
        editText.requestFocus()

        // Show the keyboard.
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, 0)
    }
}

