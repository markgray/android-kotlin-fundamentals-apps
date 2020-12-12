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

    /**
     * Play the game again event private so only this class can modify, public read-only access is
     * available by the [eventPlayAgain] property. When `true` an `Observer` of the [eventPlayAgain]
     * public version added in the `onCreateView` override of `ScoreFragment` will navigate to the
     * `GameFragment`. Set to `true` by our [onPlayAgain] method (which is called when the "Play
     * Again" `Button` with resource ID R.id.play_again_button is clicked), and reset to `false` by
     * our [onPlayAgainComplete] method (which needs to be called to prevent repeating the action
     * after navigating to the `GameFragment`).
     */
    private val _eventPlayAgain = MutableLiveData<Boolean>()

    /**
     * Public read-only access to our [_eventPlayAgain] property. An `Observer` added to it in the
     * `onCreateView` override of `ScoreFragment` navigates to the `GameFragment` when it toggles
     * to `true`.
     */
    val eventPlayAgain: LiveData<Boolean>
        get() = _eventPlayAgain


    init {
        _score.value = finalScore
    }

    /**
     * Called to set the value of our [_eventPlayAgain] property to `true`. This is called when the
     * "Play Again" `Button` with resource ID R.id.play_again_button is clicked.
     */
    fun onPlayAgain() {
        _eventPlayAgain.value = true
    }

    /**
     * Called to set the value of our [_eventPlayAgain] property to `false`. This is called when the
     * `Observer` of [eventPlayAgain] has navigated to the `GameFragment` in order to prevent the
     * repetition of the action.
     */
    fun onPlayAgainComplete() {
        _eventPlayAgain.value = false
    }


}
