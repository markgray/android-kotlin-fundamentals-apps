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

package com.example.android.guesstheword.screens.game

import android.util.Log
import androidx.lifecycle.ViewModel

/**
 * ViewModel containing all the logic needed to run the game displayed by `GameFragment`
 */
class GameViewModel : ViewModel() {

    /**
     * The current word
     */
    var word: String = ""

    /**
     * The current score
     */
    var score: Int = 0

    /**
     * The list of words - the front of the list is the next word to guess
     */
    private lateinit var wordList: MutableList<String>


    /**
     * Resets the list of words in [wordList] and randomizes the order by shuffling
     */
    private fun resetList() {
        wordList = mutableListOf(
            "queen",
            "hospital",
            "basketball",
            "cat",
            "change",
            "snail",
            "soup",
            "calendar",
            "sad",
            "desk",
            "guitar",
            "home",
            "railway",
            "zebra",
            "jelly",
            "car",
            "crow",
            "trade",
            "bag",
            "roll",
            "bubble"
        )
        wordList.shuffle()
    }

    init {
        Log.i("GameViewModel", "GameViewModel created!")
        resetList()
        nextWord()
    }

    /**
     * Callback called when the [ViewModel] is destroyed. First we call our super's implementation
     * of `onCleared`, then we log the fact that the [GameViewModel] has been destroyed.
     */
    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed!")
    }

    /** Methods for updating the UI **/

    /**
     * Called from the `onSkip` method of `GameFragment` when the user clicks the "Skip" button.
     * We decrement the current score in [score], then call our [nextWord] method to move to the
     * next word in our [wordList] list.
     */
    fun onSkip() {
        score--
        nextWord()
    }

    /**
     * Called from the `onCorrect` method of `GameFragment` when the user clicks the "Got it" button.
     * We increment the current score in [score], then call our [nextWord] method to move to the
     * next word in our [wordList] list.
     */
    fun onCorrect() {
        score++
        nextWord()
    }

    /**
     * Moves to the next word in the list. If our [wordList] list of words field is not empty we
     * remove its zeroth entry and set our [word] field to it.
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isNotEmpty()) {
            word = wordList.removeAt(0)
        }
    }
}
