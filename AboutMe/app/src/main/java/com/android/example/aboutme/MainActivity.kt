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

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/**
 * Main Activity of the AboutMe app. This app demonstrates:
 *  * LinearLayout with TextViews, ImageView, and ScrollView
 *  * ScrollView to display scrollable text
 */
class MainActivity : AppCompatActivity() {

    /**
     * Called when the activity is starting. We just set our content view to our layout file
     * [R.layout.activity_main].
     *
     * @param savedInstanceState we do not implement [onSaveInstanceState], so do not use
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
