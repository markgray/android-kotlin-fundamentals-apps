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

@file:Suppress("MemberVisibilityCanBePrivate") // I like to reference methods and fields in kdoc

package com.example.android.diceroller

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams

/**
 * DiceRoller demonstrates simple interactivity in an Android app.
 * It contains one button that updates an image view with a dice
 * vector image with a random value between 1 and 6.
 */
class MainActivity : AppCompatActivity() {

    /**
     * The [ImageView] in our layout file with ID `R.id.dice_image`
     */
    lateinit var diceImage: ImageView

    /**
     * The [ImageView] in our layout file with ID `R.id.dice_image2`
     */
    lateinit var diceImage2: ImageView

    /**
     *
     * Called when the activity is starting. First we call [enableEdgeToEdge]
     * to enable edge to edge display, then we call our super's
     * implementation of onCreate, and set our content view to our layout
     * file `R.layout.activity_main`. We initialize our [LinearLayout]
     * variable `rootView` to the view with ID `R.id.root_view` then call
     * [ViewCompat.setOnApplyWindowInsetsListener] to take over over the policy
     * for applying window insets to `rootView`, with the `listener` argument
     * a lambda that accepts the [View] passed the lambda in variable `v` and
     * the [WindowInsetsCompat] passed the lambda in variable `windowInsets`. It
     * initializes its [Insets] variable `insets` to the [WindowInsetsCompat.getInsets]
     * of `windowInsets` with [WindowInsetsCompat.Type.systemBars] as the
     * argument, then it updates the layout parameters of `v` to be a
     * [ViewGroup.MarginLayoutParams] with the left margin set to `insets.left`,
     * the right margin set to `insets.right`, the top margin set to `insets.top`,
     * and the bottom margin set to `insets.bottom`. Finally it returns
     * [WindowInsetsCompat.CONSUMED] to the caller (so that the window insets
     * will not keep passing down to descendant views).
     *
     * We initialize our [Button] variable `val rollButton` by finding the view with ID
     * `R.id.roll_button` and set its `OnClickListener` to a lambda that calls our [rollDice]
     * method. We initialize our [ImageView] field `var diceImage` by finding the view with ID
     * `R.id.dice_image`. We initialize our [ImageView] field `var diceImage2` by finding the
     * view with ID `R.id.dice_image2`.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being
     * shut down then this Bundle contains the data it most recently supplied in an override of
     * [onSaveInstanceState] Otherwise it is null. We do not override [onSaveInstanceState] so
     * do not use.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rootView = findViewById<LinearLayout>(R.id.root_view)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v: View, windowInsets: WindowInsetsCompat ->
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

        // Get the Button view from the layout and assign a click
        // listener to it.
        val rollButton: Button = findViewById(R.id.roll_button)
        rollButton.setOnClickListener { rollDice() }
        diceImage = findViewById(R.id.dice_image)
        diceImage2 = findViewById(R.id.dice_image2)
    }

    /**
     * Click listener for the Roll button. We set the content of our [ImageView] field [diceImage]
     * to the drawable with the resourse ID returned by our method [getRandomDiceImage], and then
     * set the content of our [ImageView] field [diceImage2] to the drawable with the resourse ID
     * returned by a second call to our method [getRandomDiceImage].
     */
    private fun rollDice() {
        diceImage.setImageResource(getRandomDiceImage())
        diceImage2.setImageResource(getRandomDiceImage())
    }

    /**
     * Returns a randomly chosen drawable resourse ID from our 6 dice images. We initialize our
     * variable `val randomInt` to an [Int] from the [IntRange] 1 to 6, then use a `when` statement
     * to return a drawable resource ID depending on the value of `randomInt`:
     *
     *  1. The resource ID `R.drawable.dice_1`
     *  2. The resource ID `R.drawable.dice_2`
     *  3. The resource ID `R.drawable.dice_3`
     *  4. The resource ID `R.drawable.dice_4`
     *  5. The resource ID `R.drawable.dice_5`
     *  6. (or anything else) The resource ID `R.drawable.dice_6`
     *
     * @return a randomly chosen drawable resourse ID from our 6 dice images.
     */
    private fun getRandomDiceImage(): Int {

        return when ((1..6).random()) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }
    }
}
