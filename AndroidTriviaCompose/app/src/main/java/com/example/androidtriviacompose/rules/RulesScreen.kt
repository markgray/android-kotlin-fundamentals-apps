package com.example.androidtriviacompose.rules

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
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
import com.example.androidtriviacompose.MainActivity
import com.example.androidtriviacompose.R
import com.example.androidtriviacompose.Routes

/**
 * This is the screen which expalins the rules of the "Android Trivia" game. It just consists of an
 * [Image] composable and a [Text] composable in a [Column], which uses the [Modifier.verticalScroll]
 * modifier to allow the user to scroll the [Column] if it is too large to display. It is navigated
 * to when the user clicks the "Rules" [Button] in the drawer of the [Scaffold] of [MainActivity].
 * The route to this screen is defined by the [Routes.Rules] object and is the [String] "rules".
 */
@Preview(showBackground = true)
@Composable
fun RulesScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    RulesScreenContent(modifier)
}

@Composable
fun RulesScreenContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(8.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.trivia_rules),
            contentDescription = null)
        Spacer(modifier = modifier.height(20.dp))
        Text(
            text = stringResource(id = R.string.rules_text),
            fontSize = 20.sp
        )
    }
}
