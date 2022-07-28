package com.example.androidtriviacompose.title

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
import com.example.androidtriviacompose.R
import com.example.androidtriviacompose.Routes
import com.example.androidtriviacompose.game.GameScreen

/**
 * This is the start screen of the game, and it consists of a [Column] holding an [Image] and a
 * "Play" [Button] that the user can click to navigate to the [GameScreen] to play a game. Note that
 * the [Column] has a [Modifier.verticalScroll] modifier so it can be scrolled if the [Button] does
 * not fit on the screen, but just in case the user fails to notice this the [Image] also has a
 * [Modifier.clickable] that navigates to the [GameScreen] as well.
 */
@Preview
@Composable
fun TitleScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    TitleScreenContent(
        modifier = modifier,
        navController = navController
    )
}

/**
 * This is the content displayed by the [TitleScreen], a level of indirection added for flexibility
 * when writing the code but not necessary it turned out. Our `content` consists of a [Column] whose
 * `modifier` adds 8 dp to the padding of the `modifier` parameter passed to [TitleScreenContent]
 * and modifies the [Column] to allow to scroll it to scroll vertically. Its `horizontalAlignment`
 * parameter (horizontal alignment of the layout's children) is [Alignment.CenterHorizontally] (the
 * children are centered horizontally). The `content` of the [Column] is an [Spacer] of 100dp,
 * followed by an  [Image], followed by a [Spacer] of 100dp, and a [Button] labeled "Play" whose
 * `onClick` parameter uses our [navController] parameter to navigate to the [Routes.Game.route]
 * (the [GameScreen]) to allow the user to play a game.
 *
 * @param modifier a [Modifier] instance that our caller could use to modify our Composables (but
 * they don't do so, so the default [Modifier] is used instead).
 * @param navController the [NavHostController] we use to navigate to the [GameScreen].
 */
@Composable
fun TitleScreenContent(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    Column(
        modifier = modifier.padding(8.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = modifier.height(100.dp))
        Image(
            painter = painterResource(id = R.drawable.android_trivia),
            contentDescription = null,
            modifier = Modifier.clickable { navController.navigate(Routes.Game.route) }
        )
        Spacer(modifier = modifier.height(100.dp))
        Button(onClick = { navController.navigate(Routes.Game.route) }) {
            Text(
                text = stringResource(id = R.string.play),
                fontSize = 18.sp
            )
        }
    }
}