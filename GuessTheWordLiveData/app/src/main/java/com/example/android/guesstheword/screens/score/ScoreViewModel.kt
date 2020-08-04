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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel for the final screen in `ScoreFragment` showing the score
 *
 * @param finalScore the safe args final score passed to us by the `GameFragment` via the factory
 * method `ScoreViewModelFactory` when it constructs us.
 */
class ScoreViewModel(finalScore: Int) : ViewModel() {

    /**
     * The final score, private so only this class can modify, public read-only access is available
     * by the [score] property. Set to our constructor argument `finalScore` in our init block.
     */
    private val _score = MutableLiveData<Int>()

    /**
     * Public read-only access to our [_score] property. An `Observer` added to it in the
     * `onCreateView` override of `ScoreFragment` sets the text of the `TextView` with resource
     * ID `R.id.score_text` to the [String] value of it when it changes value.
     */
    val score: LiveData<Int>
        get() = _score

    private val _eventPlayAgain = MutableLiveData<Boolean>()
    val eventPlayAgain: LiveData<Boolean>
        get() = _eventPlayAgain


    init {
        _score.value = finalScore
    }

    fun onPlayAgain() {
        _eventPlayAgain.value = true
    }

    fun onPlayAgainComplete() {
        _eventPlayAgain.value = false
    }


}
