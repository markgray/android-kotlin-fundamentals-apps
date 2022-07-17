package com.example.androidtriviacompose.gamewon

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.androidtriviacompose.game.GameScreen
import com.example.androidtriviacompose.game.QuestionRepository.Question
import com.example.androidtriviacompose.R
import com.example.androidtriviacompose.Routes

/**
 * This is the screen that is navigated to if [GameScreen] determines that the user has that the
 * user has won by answering 3 [Question]'s correctly. It consists of a [Column] holding an [Image]
 * and a "Next Match" [Button] that the user can click to navigate to the [GameScreen] to play
 * another game. Note that the [Column] has a [Modifier.verticalScroll] modifier so it can be
 * scrolled if the [Button] does not fit on the screen, but just in case the user fails to notice
 * this the [Image] also has a [Modifier.clickable] that navigates to the [GameScreen] as well.
 */
@Preview
@Composable
fun GameWonScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    GameWonScreenContent(
        modifier = modifier,
        navController = navController
    )
}

@Composable
fun GameWonScreenContent(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    Column(
        modifier = modifier.padding(8.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = modifier.height(100.dp))
        Image(
            painter = painterResource(id = R.drawable.you_win),
            contentDescription = null,
            modifier = Modifier.clickable { navController.navigate(Routes.Game.route) }
        )
        Spacer(modifier = modifier.height(100.dp))
        Button(onClick = { navController.navigate(Routes.Game.route) }) {
            Text(
                text = stringResource(id = R.string.next_match),
                fontSize = 18.sp
            )
        }
    }
}