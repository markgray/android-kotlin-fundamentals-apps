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

package com.android.example.colormyviews

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.android.example.colormyviews.databinding.ActivityMainBinding

/**
 * The ColorMyViews app demonstrates how to use a `ConstraintLayout` using the Layout Editor.
 * Therefore all of the interesting information is in the instructions themselves.
 */
class MainActivity : AppCompatActivity() {

    /**
     * Binding to our layout file [R.layout.activity_main], initialized in our [onCreate] override.
     */
    private lateinit var binding: ActivityMainBinding

    /**
     * Called when the activity is starting. First we call our super's implementation of `onCreate`.
     * Then we use the [ActivityMainBinding.inflate] method with the LayoutInflater instance that
     * this Window retrieved from its Context to inflate our layout file [R.layout.activity_main]
     * into an [ActivityMainBinding] instance which we use to initialize our field [binding], and
     * set our content view to the outermost View in the associated layout file associated with
     * [ActivityMainBinding]. Finally we call our [setListeners] method to have it set the
     * `OnClickListener` of the [View]'s in our layout file to a lambda which calls our [makeColored]
     * method with the [View] clicked to have it set the background color of the [View] to different
     * colors depending on the ID of the [View].
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being
     * shut down then this Bundle contains the data it most recently supplied in [onSaveInstanceState]
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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
        setContentView(binding.root)

        setListeners()
    }

    /**
     * Attaches an `OnClickListener` to all the views. The listener is a lambda which calls our
     * [makeColored] method with the [View] clicked as the parameter to have it set the background
     * color to a different color depending on the ID of the [View].
     */
    private fun setListeners() {

        val boxOneText = binding.boxOneText
        val boxTwoText = binding.boxTwoText
        val boxThreeText = binding.boxThreeText
        val boxFourText = binding.boxFourText
        val boxFiveText = binding.boxFiveText

        val rootConstraintLayout = binding.constraintLayout

        val redButton = binding.redButton
        val greenButton = binding.greenButton
        val yellowButton = binding.yellowButton

        val clickableViews: List<View> =
            listOf(
                boxOneText, boxTwoText, boxThreeText,
                boxFourText, boxFiveText, rootConstraintLayout,
                redButton, greenButton, yellowButton
            )

        for (item in clickableViews) {
            item.setOnClickListener { makeColored(it) }
        }
    }

    /**
     * Sets the background color of a view depending on it's resource id.
     * This is a way of using one click handler to do similar operations on a
     * group of views.
     */
    private fun makeColored(view: View) {
        when (view.id) {

            // Boxes using Color class colors for background
            R.id.box_one_text -> view.setBackgroundColor(Color.DKGRAY)
            R.id.box_two_text -> view.setBackgroundColor(Color.GRAY)

            R.id.box_three_text -> view.setBackgroundColor(Color.BLUE)
            R.id.box_four_text -> view.setBackgroundColor(Color.MAGENTA)
            R.id.box_five_text -> view.setBackgroundColor(Color.BLUE)

            // Boxes using custom colors for background
            R.id.red_button -> binding.boxThreeText.setBackgroundResource(R.color.my_red)

            R.id.yellow_button -> binding.boxFourText.setBackgroundResource(R.color.my_yellow)
            R.id.green_button -> binding.boxFiveText.setBackgroundResource(R.color.my_green)

            else -> view.setBackgroundColor(Color.LTGRAY)
        }
    }
}
