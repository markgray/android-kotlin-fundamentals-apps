package com.example.androidtriviacompose.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.androidtriviacompose.game.QuestionRepository.Question
import com.example.androidtriviacompose.R
import com.example.androidtriviacompose.Routes

val questionRepository = QuestionRepository().also { it.initialize() }

@Preview
@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val initialQuestion = questionRepository.nextQuestion()
    var nextQuestionToAsk by remember {
        mutableStateOf(initialQuestion)
    }
    GameScreenContent(
        modifier = modifier,
        navController = navController,
        questionRepository = questionRepository,
        questionToAsk = nextQuestionToAsk,
        nextQuestion = { nextQuestionToAsk = questionRepository.nextQuestion() }
    )
}

@Composable
fun GameScreenContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    questionRepository: QuestionRepository,
    questionToAsk: Question = dummy,
    nextQuestion: () -> Unit
) {
    var selectedId by remember {
        mutableStateOf(-1)
    }
    Column(
        modifier = modifier.padding(8.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.android_category_simple),
            contentDescription = null
        )
        QuestionContent(
            modifier = modifier,
            selectedId = selectedId,
            question = questionToAsk,
            changeSelection = { selectedId = it }
        )
        Button(onClick = {
            if (questionRepository.checkAnswer(questionToAsk.answers[selectedId])) {
                selectedId = -1
                if (questionRepository.gameWon) {
                    questionRepository.initialize()
                    navController.navigate(Routes.GameWon.route)
                }
                nextQuestion()
            } else {
                selectedId = -1
                questionRepository.initialize()
                navController.navigate(Routes.GameOver.route)
            }
        }
        ) {
            Text(
                text = stringResource(id = R.string.submit_button),
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun QuestionContent(
    modifier: Modifier = Modifier,
    selectedId: Int = -1,
    question: Question = dummy,
    changeSelection: (Int) -> Unit = {}
) {
    Column(
        modifier = modifier.selectableGroup()
    ) {
        Text(
            text = question.text,
            fontSize = 30.sp
        )
        Row {
            val id = 0
            RadioButton(
                selected = selectedId == id,
                onClick = { changeSelection(id) }
            )
            Text(
                modifier = modifier
                    .padding(top = 12.dp)
                    .clickable { changeSelection(id) },
                text = question.answers[id]
            )
        }
        Row {
            val id = 1
            RadioButton(
                selected = selectedId == id,
                onClick = { changeSelection(id) }
            )
            Text(
                modifier = modifier
                    .padding(top = 12.dp)
                    .clickable { changeSelection(id) },
                text = question.answers[id]
            )
        }
        Row {
            val id = 2
            RadioButton(
                selected = selectedId == id,
                onClick = { changeSelection(id) }
            )
            Text(
                modifier = modifier
                    .padding(top = 12.dp)
                    .clickable { changeSelection(id) },
                text = question.answers[id]
            )
        }
        Row {
            val id = 3
            RadioButton(
                selected = selectedId == id,
                onClick = { changeSelection(id) }
            )
            Text(
                modifier = modifier
                    .padding(top = 12.dp)
                    .clickable { changeSelection(id) },
                text = question.answers[id]
            )
        }
    }
}

private val dummy = Question(
    text = "What color is the Android mascot?",
    answers = listOf("Blue", "Green", "Yellow", "Red")
)

