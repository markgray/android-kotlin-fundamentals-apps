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

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding

/**
 * Fragment where the game is played
 */
class GameFragment : Fragment() {

    /**
     * [GameFragmentBinding] inflated from our layout file `R.layout.game_fragment`.
     */
    private lateinit var binding: GameFragmentBinding

    /**
     * Handle to our singleton [GameViewModel] view model
     */
    private lateinit var viewModel: GameViewModel

    /**
     * Called to have the fragment instantiate its user interface view. This will be called between
     * [onCreate] and [onActivityCreated]. It is recommended to only inflate the layout in this
     * method and move logic that operates on the returned View to [onViewCreated]. We have the
     * [DataBindingUtil.inflate] method use our [LayoutInflater] parameter [inflater] to inflate
     * our layout file `R.layout.game_fragment` using our [ViewGroup] parameter [container] for the
     * LayoutParams without attaching to it and use the [GameFragmentBinding] binding to the result
     * that it returns to initialize our field [binding]. We create `ViewModelProvider` with `this`
     * as the `ViewModelStoreOwner` and call its `get` method to retrieve an existing ViewModel or
     * create a new one and initialize our [GameViewModel] field [viewModel] to the view model it
     * returns.
     *
     * We add an [Observer] to the `LiveData` wrapped `word` property of [viewModel] whose lambda
     * sets the text of the `wordText` property of [binding] (the `TextView` in our layout file
     * with resource ID `R.id.word_text`) to the new value of `word` whenever it changes.
     *
     * We add an [Observer] to the `LiveData` wrapped `score` property of [viewModel] whose lambda
     * sets the text of the `scoreText` property of [binding] (the `TextView` in our layout file
     * with resource ID `R.id.score_text`) to the new [String] value of `score` whenever it changes.
     *
     * We add an [Observer] to the `LiveData` wrapped `eventGameFinish` property of [viewModel] whose
     * lambda calls our [gameFinished] method whenever the value of `eventGameFinish` toggles to
     * `true`.
     *
     * We set the `OnClickListener` of the `correctButton` property of [binding] (the button with
     * ID `R.id.correct_button` in our layout file labeled "Got it") to a lambda that calls our
     * method [onCorrect], we set the `OnClickListener` of the `skipButton` property of [binding]
     * (the button with ID `R.id.skip_button` in our layout file labeled "Skip") to a lambda that
     * calls our method [onSkip], and we set the `OnClickListener` of the `endGameButton` property
     * of [binding] (the button with ID `R.id.end_game_button` in our layout file labeled "End game")
     * to a lambda that calls our method [onEndGame].
     *
     * Finally we return the outermost View in the layout file associated with [binding] to our
     * caller.
     *
     * @param inflater The [LayoutInflater] object that can be used to inflate XML layout files.
     * @param container If non-null, this is the parent view that the fragment's UI will be
     * attached to.  The fragment should not add the view itself, but this can be used to
     * generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     * saved state as given here.
     * @return Return the [View] for the fragment's UI, or `null`.
     */
    @Suppress("RedundantNullableReturnType") // The method we are overriding returns nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.game_fragment,
            container,
            false
        )
        Log.i("GameFragment", "Called ViewModelProvider")

        viewModel = ViewModelProvider(this)[GameViewModel::class.java]

        /** Setting up LiveData observation relationship **/
        viewModel.word.observe(viewLifecycleOwner) { newWord: String ->
            binding.wordText.text = newWord
        }

        viewModel.score.observe(viewLifecycleOwner) { newScore: Int ->
            binding.scoreText.text = newScore.toString()

        }

        // Observer for the Game finished event
        viewModel.eventGameFinish.observe(viewLifecycleOwner) { hasFinished: Boolean ->
            if (hasFinished) gameFinished()
        }

        binding.correctButton.setOnClickListener { onCorrect() }
        binding.skipButton.setOnClickListener { onSkip() }
        binding.endGameButton.setOnClickListener { onEndGame() }
        return binding.root
    }


    /** Methods for buttons presses **/

    /**
     * This is called when the "Skip" button is clicked (`skipButton` property of [binding], aka
     * resource ID `R.id.skip_button` in our layout file). We just call the `onSkip` method of
     * [GameViewModel] field [viewModel] which decrements our score by 1 and advances to the next
     * word.
     */
    private fun onSkip() {
        viewModel.onSkip()
    }

    /**
     * This is called when the "Got it" button is clicked (`correctButton` property of [binding],
     * aka resource ID `R.id.correct_button` in our layout file). We just call the `onCorrect`
     * method of [GameViewModel] field [viewModel] which increments our score by 1 and advances
     * to the next word.
     */
    private fun onCorrect() {
        viewModel.onCorrect()
    }

    /**
     * This is called when the "End game" button is clicked (`endGameButton` property of [binding],
     * aka resource ID `R.id.end_game_button` in our layout file). We just call our [gameFinished]
     * method which toasts the message "Game has just finished", creates an `ActionGameToScore`
     * "action", sets the `score` property [viewModel] as the `score` argument of the action and
     * then uses that action to navigate to the `ScoreFragment`.
     */
    private fun onEndGame() {
        gameFinished()
    }

    /**
     * Called when the game is finished. First we toast the message "Game has just finished", then
     * we initialize our variable `val action` with a new `GameFragmentDirections.ActionGameToScore`
     * instance, set the `score` property of `action` to the `score` property of our [GameViewModel]
     * field [viewModel] (the final score) or to 0 if it is `null`, then locate the `NavController`
     * associated with this [Fragment], and use it to navigate to `action` (the `ScoreFragment` with
     * the safe argument score set to the final score). Finally we call the `onGameFinishComplete`
     * method of [viewModel] to have it set its `_eventGameFinish` private `MutableLiveData` wrapped
     * [Boolean] to `false` (we observe the public read only version `eventGameFinish`).
     */
    private fun gameFinished() {
        Toast.makeText(activity, "Game has just finished", Toast.LENGTH_SHORT).show()
        val action = GameFragmentDirections.actionGameToScore()
        action.score = viewModel.score.value ?: 0
        findNavController().navigate(action)
        viewModel.onGameFinishComplete()
    }
}
