package com.example.androidtriviacompose.gameover

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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

@Preview
@Composable
fun GameOverScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    GameOverScreenContent(
        modifier = modifier,
        navController = navController
    )
}
@Composable
fun GameOverScreenContent(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = modifier.height(100.dp))
        Image(
            painter = painterResource(id = R.drawable.try_again),
            contentDescription = null
        )
        Spacer(modifier = modifier.height(100.dp))
        Button(onClick = { navController.navigate(Routes.Game.route) }) {
            Text(
                text = stringResource(id = R.string.try_again),
                fontSize = 18.sp
            )
        }
    }
}