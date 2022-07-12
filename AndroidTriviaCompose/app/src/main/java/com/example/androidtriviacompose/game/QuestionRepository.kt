@file:Suppress("unused")

package com.example.androidtriviacompose.game

class QuestionRepository {
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
            answers = listOf("onCreateView", "onActivityCreated", "onCreateLayout", "onInflateLayout")),
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
     * The current question that we are asking.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    lateinit var currentQuestion: Question

    /**
     * The shuffled `answers` list field of the [Question] field [currentQuestion].
     */
    @Suppress("MemberVisibilityCanBePrivate")
    lateinit var answers: MutableList<String>

    @Suppress("MemberVisibilityCanBePrivate")
    var gameWon: Boolean = false

    /**
     * Index into our [questions] field which we use to choose the next [currentQuestion]
     */
    private var questionIndex = 0

    /**
     * The number of questions we ask in each round of the game (at most 3).
     */
    private val numQuestions = ((questions.size + 1) / 2).coerceAtMost(3)

    fun initialize() {
        questions.shuffle()
        questionIndex = 0
        currentQuestion = questions[questionIndex]
        answers = currentQuestion.answers.toMutableList()
        answers.shuffle()
    }

    fun nextQuestion(): Question {
        return Question(text = currentQuestion.text, answers = answers)
    }

    fun checkAnswer(answer: String): Boolean {
        val okay: Boolean = answer == currentQuestion.answers[0]
        questionIndex++
        if (questionIndex < numQuestions) {
            currentQuestion = questions[questionIndex]
            answers = currentQuestion.answers.toMutableList()
            answers.shuffle()
        } else {
            gameWon = true
        }
        return okay
    }

}