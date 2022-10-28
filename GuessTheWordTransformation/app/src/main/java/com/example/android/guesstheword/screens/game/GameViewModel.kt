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

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

/**
 * ViewModel containing all the logic needed to run the game displayed by `GameFragment`
 */
class GameViewModel : ViewModel() {
    /**
     * the [CountDownTimer] which triggers the end of the game when it finishes counting down.
     * Constructed in our init block where its `onTick` override sets the value of our [_currentTime]
     * to the time remaining, and its `onFinish` override sets [_currentTime] to [DONE] and calls
     * our [onGameFinish] method.
     */
    private val timer: CountDownTimer

    companion object {

        /**
         * Time when the game is over
         */
        private const val DONE = 0L

        /**
         * Countdown time interval
         */
        private const val ONE_SECOND = 1000L

        /**
         * Total time for the game
         */
        private const val COUNTDOWN_TIME = 60000L

    }

    /**
     * The current word to guess, private so only this class can modify, public read-only access is
     * available by the [word] property. Set by our [nextWord] method.
     */
    private val _word = MutableLiveData<String>()

    /**
     * Public read-only access to our [_word] property. A binding expression in the `GameFragment`
     * layout file layout/game_fragment.xml accesses it when it changes to set the text of the
     * `TextView` with resource ID R.id.word_text.
     */
    val word: LiveData<String>
        get() = _word

    /**
     * The current score, private so only this class can modify, public read-only access is
     * available by the [score] property. Incremented by our [onCorrect] method, and decremented
     * by our [onSkip] method.
     */
    private val _score = MutableLiveData<Int>()

    /**
     * Public read-only access to our [_score] property. Read by the `gameFinished` method of
     * `GameFragment` to supply the final score when navigating to the `ScoreFragment`, and a
     * binding expression in the `GameFragment` layout file layout/game_fragment.xml accesses it
     * when it changes to set the text of the `TextView` with resource ID R.id.score_text.
     */
    val score: LiveData<Int>
        get() = _score

    /**
     * Countdown time, private so only this class can modify, public read-only access is available
     * by the [currentTime] property. This is the time remaining in the game, it is updated by
     * the `onTick` override of our [CountDownTimer] field [timer] to the amount of time until
     * finished, and set to [DONE] by its `onFinish` override.
     */
    private val _currentTime = MutableLiveData<Long>()

    /**
     * Public read-only access to our [_currentTime] property. Our property [currentTimeString] uses
     * the [Transformations.map] method to format this as a [LiveData] wrapped [String] for display
     * in the `GameFragment` UI.
     */
    @Suppress("MemberVisibilityCanBePrivate") // I like to do kdoc [] references
    val currentTime: LiveData<Long>
        get() = _currentTime

    /**
     * The String version of the current time. A binding expression used for the value of the
     * "android:text" attribute of the `TextView` with ID R.id.timer_text updates the text displayed
     * whenever this changes value (which it does when our [currentTime] changes value.
     */
    val currentTimeString: LiveData<String> = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }

    /**
     * The game is over, time to navigate to the `ScoreFragment`, private so only this class can
     * modify, public read-only access is available by the [eventGameFinish] property. Set to
     * `true` by our [onGameFinish] method, reset to `false` by our [onGameFinishComplete] method.
     */
    private val _eventGameFinish = MutableLiveData<Boolean>()

    /**
     * Public read-only access to our [_eventGameFinish] property. An `Observer` with a lambda which
     * calls the `GameFragment.gameFinished` method when it transitions to `true` is added to it in
     * the `onCreateView` override of `GameFragment`.
     */
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish

    /**
     * The list of words - the front of the list is the next _word to guess
     */
    private lateinit var wordList: MutableList<String>

    /**
     * Resets the list of words and randomizes the order
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
        _word.value = ""
        _score.value = 0
        Log.i("GameViewModel", "GameViewModel created!")
        resetList()
        nextWord()

        /**
         * Creates a timer which triggers the end of the game when it finishes
         */
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            /**
             * Callback fired on regular interval. We just set the value of our [_currentTime]
             * property to our [millisUntilFinished] parameter divided by [ONE_SECOND]
             *
             * @param millisUntilFinished The amount of time until finished.
             */
            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = millisUntilFinished / ONE_SECOND
            }

            /**
             * Callback fired when the time is up. We set the value of our [_currentTime] property
             * to [DONE] then call our [onGameFinish] method to end the game.
             */
            override fun onFinish() {
                _currentTime.value = DONE
                onGameFinish()
            }
        }

        timer.start()
    }

    /**
     * Callback called when the ViewModel is destroyed. First we call our super's implementation of
     * `onCleared`, and log the fact that we were destroyed. Then we call the `cancel` method of our
     * [CountDownTimer] field [timer] to cancel the countdown.
     */
    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed!")
        timer.cancel()
    }

    /** Methods for updating the UI **/

    /**
     * Called by a binding expression that is the value of the "android:onClick" attribute of the
     * `Button` with resource ID R.id.skip_button in the `GameFragment` layout/game_fragment.xml
     * layout file. We decrement the current score in [_score], then call our [nextWord] method to
     * move to the next word in our [wordList] list.
     */
    fun onSkip() {
        _score.value = (_score.value)?.minus(1)
        nextWord()
    }

    /**
     * Called by a binding expression that is the value of the "android:onClick" attribute of the
     * `Button` with resource ID R.id.correct_button in the `GameFragment` layout/game_fragment.xml
     * layout file. We increment the current score in [_score], then call our [nextWord] method to
     * move to the next word in our [wordList] list.
     */
    fun onCorrect() {
        _score.value = (_score.value)?.plus(1)
        nextWord()
    }

    /**
     * Moves to the next word in the list. If our [wordList] list of words field is empty we call
     * our [resetList] method reset the list. Otherwise we remove the zeroth entry of [wordList]
     * and set our [_word] field to it.
     */
    private fun nextWord() {
        if (wordList.isEmpty()) {
            resetList()

        } else {
            //Select and remove a _word from the list
            _word.value = wordList.removeAt(0)
        }
    }

    /** Methods for the game completed event **/

    /**
     * Resets the value of our [_eventGameFinish] property to `false`. It is called from the
     * `gameFinished` method of `GameFragment` after it navigates to the `ScoreFragment` to
     * prevent repeating the action.
     */
    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }

    /**
     * Our game is over. Called by a binding expression that is the value of the "android:onClick"
     * attribute of the `Button` with resource ID R.id.end_game_button in the `GameFragment`
     * layout/game_fragment.xml layout file and by the `onFinish` override of our [CountDownTimer]
     * field [timer] when the count down is done. We set the value of our [_eventGameFinish] property
     * to `true` and an `Observer` of the public version [eventGameFinish] will navigate to the
     * `ScoreFragment` on this transition.
     */
    fun onGameFinish() {
        _eventGameFinish.value = true
    }

}
