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
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

/**
 * Main Activity of the AboutMe app. This app demonstrates:
 *  * Getting user input with an EditText.
 *  * Click handler for a Button to retrieve text from an EditText and set it in a TextView.
 *  * Setting a click handler on a TextView.
 *  * Setting the visibility status of a view.
 */
class MainActivity : AppCompatActivity() {

    /**
     * Called when the activity is starting. First we call our super's implementation of [onCreate],
     * then we set our content view to our layout file R.layout.activity_main. We find the [Button]
     * with ID R.id.done_button (labeled "Done") and set its `OnClickListener` to a lambda which calls
     * our method [addNickname] with the view that was clicked, and we find the [TextView] with ID
     * R.id.nickname_text and set its `OnClickListener` to a lambda which calls our method
     * [updateNickname] with the view that was clicked.
     *
     * @param savedInstanceState we do not override [onSaveInstanceState] so do not use
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.done_button).setOnClickListener {
            addNickname(it)
        }

        findViewById<TextView>(R.id.nickname_text).setOnClickListener {
            updateNickname(it)
        }
    }

    /**
     * Click handler for the "DONE" [Button]. Hides the [EditText] and the "DONE" [Button]. Sets the
     * text of the [TextView] to the contents of the [EditText] displays the [TextView].
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
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)

    }

    /**
     * Click handler for the nickname TextView.
     * Displays the EditText and the DONE button.
     * Hides the nickname TextView.
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
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, 0)
    }
}