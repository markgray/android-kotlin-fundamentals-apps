/*
 * Copyright 2018, The Android Open Source Project
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

package com.example.android.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.android.navigation.databinding.FragmentGameBinding

/**
 * This [Fragment] handles the UI for playing the AndroidTrivia game.
 */
class GameFragment : Fragment() {
    /**
     * This Data class holds a question to ask the user in its [text] field, and a list of possible
     * answers in its [answers] field.
     */
    data class Question(
            /**
             * The question we are asking the user.
             */
            val text: String,
            /**
             * The list of possible answers to the question in our [text] field. The first answer
             * in the list is the correct one.
             */
            val answers: List<String>
    )

    /**
     * Our list of [Question] questions. The first answer in the `answers` field of every [Question]
     * is the correct one. We randomize the answers before showing the text. All questions must have
     * four answers. We'd want these to contain references to string resources so we could
     * internationalize. (Or better yet, don't define the questions in code...)
     */
    private val questions: MutableList<Question> = mutableListOf(
            Question(text = "What is Android Jetpack?",
                    answers = listOf("All of these", "Tools", "Documentation", "Libraries")),
            Question(text = "What is the base class for layouts?",
                    answers = listOf("ViewGroup", "ViewSet", "ViewCollection", "ViewRoot")),
            Question(text = "What layout do you use for complex screens?",
                    answers = listOf("ConstraintLayout", "GridLayout", "LinearLayout", "FrameLayout")),
            Question(text = "What do you use to push structured data into a layout?",
                    answers = listOf("Data binding", "Data pushing", "Set text", "An OnClick method")),
            Question(text = "What method do you use to inflate layouts in fragments?",
                    answers = listOf("onCreateView()", "onActivityCreated()", "onCreateLayout()", "onInflateLayout()")),
            Question(text = "What's the build system for Android?",
                    answers = listOf("Gradle", "Graddle", "Grodle", "Groyle")),
            Question(text = "Which class do you use to create a vector drawable?",
                    answers = listOf("VectorDrawable", "AndroidVectorDrawable", "DrawableVector", "AndroidVector")),
            Question(text = "Which one of these is an Android navigation component?",
                    answers = listOf("NavController", "NavCentral", "NavMaster", "NavSwitcher")),
            Question(text = "Which XML element lets you register an activity with the launcher activity?",
                    answers = listOf("intent-filter", "app-registry", "launcher-registry", "app-launcher")),
            Question(text = "What do you use to mark a layout for data binding?",
                    answers = listOf("<layout>", "<binding>", "<data-binding>", "<dbinding>"))
    )

    /**
     * The current question that we are asking. It is used to set the text of the `questionText`
     * TextView in our layout file using a android:text="@{game.currentQuestion.text}" Layout
     * expression as well as in our fragment's code when we determine if the user answered the
     * question correctly (which occurs in the `OnClickListener` lamda of the `submitButton`
     * Button in our layout).
     */
    lateinit var currentQuestion: Question

    /**
     * The shuffled `answers` list field of the [Question] field [currentQuestion]. They are each
     * displayed using android:text="@{game.answers}" Layout expressions in the attributes of
     * their respective `RadioButton` widgets in our layout file as in our fragment's code when we
     * determine if the user answered the question correctly.
     */
    lateinit var answers: MutableList<String>

    /**
     * Index into our [questions] field which we use to choose the next [currentQuestion]
     */
    private var questionIndex = 0

    /**
     * The number of questions we ask in each round of the game (at most 3).
     */
    private val numQuestions = ((questions.size + 1) / 2).coerceAtMost(3)

    /**
     * Called to have the fragment instantiate its user interface view. This will be called between
     * [onCreate] and [onActivityCreated]. It is recommended to only inflate the layout in this
     * method and move logic that operates on the returned View to [onViewCreated].
     *
     * We initialize our [FragmentGameBinding] binding variable `val binding` by having the method
     * [DataBindingUtil.inflate] use our [LayoutInflater] parameter [inflater] to inflate our binding
     * layout file (resource ID [R.layout.fragment_game]), with its LayoutParams generated from our
     * [ViewGroup] parameter [container] without attaching to it. Then we call our method
     * [randomizeQuestions] to have it shuffle the questions and set the question index to the first
     * question. We set the `game` variable in our `binding` to our layout to `this`. We set the
     * `OnClickListener` of the `submitButton` in `binding` to a lambda which retrieves the ID of
     * the checked radio button in the `questionRadioGroup` radio group of `binding` to set the
     * variable `val checkedId` and if it is not equal to -1 uses a `when` block to set the
     * variable `var answerIndex` to 0, 1, 2, or 3 depending on which radio button was checked, then
     * branches on whether the `answerIndex` entry in [answers] is equal to the 0'th entry in
     * the `answers` field of the [currentQuestion] (the first answer in the original question is
     * always the correct one):
     *
     *  - Answer is correct: it increments [questionIndex] and if [questionIndex] is less than the
     *  number of questions in [numQuestions] sets [currentQuestion] to the [questionIndex] entry
     *  in [questions], calls our [setQuestion] method to set the question and randomize the answers
     *  then calls the `invalidateAll` method of [FragmentGameBinding] `binding` to our layout to
     *  have it invalidate all binding expressions and request a new rebind to refresh UI with the
     *  updated [currentQuestion] values. If [questionIndex] is not less than [numQuestions] the
     *  user has won the game so we Navigate to the GameWonFragment passing [numQuestions], and
     *  [questionIndex] as its safe arguments.
     *
     *  - Answer is incorrect: it navigates to the GameOverFragment.
     *
     * Finally we return the `root` property of `binding` (the outermost [View] in the layout file
     * associated with the Binding) to the caller.
     *
     * @param inflater The [LayoutInflater] object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI will be attached to. The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here. We do not call [onSaveInstanceState]
     * so do not use.
     *
     * @return Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentGameBinding>(
                inflater,
                R.layout.fragment_game,
                container,
                false
        )

        // Shuffles the questions and sets the question index to the first question.
        randomizeQuestions()

        // Bind this fragment class to the layout
        binding.game = this

        // Set the onClickListener for the submitButton
        binding.submitButton.setOnClickListener { view: View ->
            val checkedId = binding.questionRadioGroup.checkedRadioButtonId
            // Do nothing if nothing is checked (id == -1)
            if (-1 != checkedId) {
                var answerIndex = 0
                when (checkedId) {
                    R.id.secondAnswerRadioButton -> answerIndex = 1
                    R.id.thirdAnswerRadioButton -> answerIndex = 2
                    R.id.fourthAnswerRadioButton -> answerIndex = 3
                }
                // The first answer in the original question is always the correct one, so if our
                // answer matches, we have the correct answer.
                if (answers[answerIndex] == currentQuestion.answers[0]) {
                    questionIndex++
                    // Advance to the next question
                    if (questionIndex < numQuestions) {
                        currentQuestion = questions[questionIndex]
                        setQuestion()
                        binding.invalidateAll()
                    } else {
                        // We've won!  Navigate to the gameWonFragment.
                        view.findNavController()
                                .navigate(GameFragmentDirections
                                        .actionGameFragmentToGameWonFragment(
                                                numQuestions,
                                                questionIndex
                                        )
                                )
                    }
                } else {
                    // Game over! A wrong answer sends us to the gameOverFragment.
                    view.findNavController()
                            .navigate(GameFragmentDirections.actionGameFragmentToGameOverFragment())
                }
            }
        }
        return binding.root
    }

    /**
     * Randomize the questions and set the first question. It does this by shuffling the list of
     * [Question] objects in [questions], setting the index into that list ([questionIndex]) to 0
     * and then calling our [setQuestion] method.
     */
    private fun randomizeQuestions() {
        questions.shuffle()
        questionIndex = 0
        setQuestion()
    }

    /**
     * Sets the question and randomizes the answers. This only changes the data, not the UI.
     * Calling invalidateAll on the [FragmentGameBinding] updates the display of the data in
     * the UI. We set the current question [currentQuestion] to the [questionIndex] entry in
     * [questions], set [answers] to a copy of the [answers] field of [currentQuestion], and
     * then shuffle [answers]. Finally we set the title of our action bar to a formatted string
     * displaying the current question number and the total number of questions to be asked.
     */
    private fun setQuestion() {
        currentQuestion = questions[questionIndex]
        // randomize the answers into a copy of the array
        answers = currentQuestion.answers.toMutableList()
        // and shuffle them
        answers.shuffle()
        (activity as AppCompatActivity).supportActionBar?.title =
                getString(
                        R.string.title_android_trivia_question,
                        questionIndex + 1,
                        numQuestions
                )
    }
}
