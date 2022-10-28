/*
 * Copyright (C) 2019 Google Inc.
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

package com.example.android.guesstheword.screens.score

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * [ViewModelProvider.Factory] for [ViewModelProvider] to use to construct a [ScoreViewModel]
 *
 * @param finalScore the safe args `score` passed when the `GameFragment` navigates to `ScoreFragment`
 */
class ScoreViewModelFactory(private val finalScore: Int) : ViewModelProvider.Factory {

    /**
     * Creates a new instance of the given [Class]. After a sanity check to make sure we are only
     * being used to create a [ScoreViewModel] instance we return a [ScoreViewModel] constructed
     * to use our [finalScore] property as its `score` property.
     *
     * @param modelClass a [Class] whose instance is requested
     * @param T        The type parameter for the ViewModel.
     * @return a newly created [ScoreViewModel] view model.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScoreViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") // The above if statement checks the cast
            return ScoreViewModel(finalScore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
