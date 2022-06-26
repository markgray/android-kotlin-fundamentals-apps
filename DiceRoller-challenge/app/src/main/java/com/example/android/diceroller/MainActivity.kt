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

package com.example.android.diceroller

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * DiceRoller demonstrates simple interactivity in an Android app.
 * It contains one button that updates a text view with a random
 * value between 1 and 6.
 */
class MainActivity : AppCompatActivity() {

    /**
     * Called when the activity is starting. First we call our super's implementation of `onCreate`,
     * then we set our content view to our layout file [R.layout.activity_main]. We initialize our
     * [Button] variable `val rollButton` by finding the view with ID [R.id.roll_button] and set its
     * `OnClickListener` to a lambda which calls our [rollDice] method. We initialize our [Button]
     * variable `val countButton` by finding the view with ID [R.id.countup_button] and set its
     * `OnClickListener` to a lambda which calls our [countUp] method.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being
     * shut down then this Bundle contains the data it most recently supplied in an override of
     * [onSaveInstanceState] Otherwise it is null. We do not override [onSaveInstanceState] so
     * do not use.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get the Button view from the layout and assign a click
        // listener to it.
        val rollButton: Button = findViewById(R.id.roll_button)
        rollButton.setOnClickListener { rollDice() }

        val countButton: Button = findViewById(R.id.countup_button)
        countButton.setOnClickListener { countUp() }
    }

    /**
     * Click listener for the Roll button. We initialize our variable `val randomInt` to an [Int]
     * from the [IntRange] 1 to 6. We initialize our [TextView] variable `val resultText` by
     * finding the view with ID [R.id.result_text] and set its text to the [String] value of
     * `randomInt`.
     */
    private fun rollDice() {
        val randomInt = (1..6).random()

        val resultText: TextView = findViewById(R.id.result_text)
        resultText.text = randomInt.toString()
    }

    /**
     * Click listener for the countUp button. We initialize our [TextView] variable `val resultText`
     * by finding the view with ID [R.id.result_text] and if its text is the default "Hello World!"
     * we set its text to "1", otherwise we initialize our [Int] variable `var resultInt` to the
     * [Int] value of the `text` property of `resultText`. If `resultInt` is less than 6 we increment
     * `resultInt` and set the text of `resultText` to the [String] value of `resultInt`.
     */
    private fun countUp() {
        val resultText: TextView = findViewById(R.id.result_text)

        // If text is the default "Hello World!" set that text to 1.
        if (resultText.text == "Hello World!") {
            resultText.text = "1"
        } else {
            // Otherwise, increment the number up to 6.
            // The text value in resultText.text is an instance of the CharSequence class;
            // it needs to be converted to a String object before it can be converted to an int.
            var resultInt = resultText.text.toString().toInt()

            if (resultInt < 6) {
                resultInt++
                resultText.text = resultInt.toString()
            }
        }
    }
}
