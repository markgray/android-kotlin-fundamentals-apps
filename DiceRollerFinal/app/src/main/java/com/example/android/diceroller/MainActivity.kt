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

@file:Suppress("MemberVisibilityCanBePrivate")

package com.example.android.diceroller

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

/**
 * DiceRoller demonstrates simple interactivity in an Android app.
 * It contains one button that updates an image view with a dice
 * vector image with a random value between 1 and 6.
 */
class MainActivity : AppCompatActivity() {

    /**
     * The [ImageView] in our layout file with ID [R.id.dice_image]
     */
    lateinit var diceImage : ImageView

    /**
     * Called when the activity is starting. First we call our super's implementation of `onCreate`,
     * then we set our content view to our layout file [R.layout.activity_main]. We initialize our
     * [Button] variable `val rollButton` by finding the view with ID [R.id.roll_button] and set its
     * `OnClickListener` to a lambda which calls our [rollDice] method. We initialize our [ImageView]
     * field `var diceImage` by finding the view with ID [R.id.dice_image]
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
        diceImage = findViewById(R.id.dice_image)
    }

    /**
     * Click listener for the Roll button. We initialize our variable `val randomInt` to an [Int]
     * from the [IntRange] 1 to 6. We use a `when` statement to initialize our [Int] variable
     * `val drawableResource` based on the value of `randomInt`:
     *
     *  1. The resource ID [R.drawable.dice_1]
     *  2. The resource ID [R.drawable.dice_2]
     *  3. The resource ID [R.drawable.dice_3]
     *  4. The resource ID [R.drawable.dice_4]
     *  5. The resource ID [R.drawable.dice_5]
     *  6. (or anything else) The resource ID [R.drawable.dice_6]
     *
     * Then we set the content of our [ImageView] field [diceImage] to the drawable with resourse ID
     * `drawableResource`.
     */
    private fun rollDice() {
        @Suppress("MoveVariableDeclarationIntoWhen")
        val randomInt = (1..6).random()

        val drawableResource = when (randomInt) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }

        diceImage.setImageResource(drawableResource)
    }
}
