package com.example.androidtriviacompose.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.androidtriviacompose.MainScaffold
import com.example.androidtriviacompose.R
import com.example.androidtriviacompose.Routes

/**
 * This is the screen that displays the "About" text for the AndroidTriviaCompose app. It just
 * consists of an [Image] composable and a [Text] composable in a [Column], which uses the
 * [Modifier.verticalScroll] modifier to allow the user to scroll the [Column] if it is too large
 * to display. This screen is displayed when the user clicks the "About" button in the drawer of
 * the [MainScaffold] composable found in the MainActivity.kt file. The route to this screen is
 * defined by the [Routes.About] object and is the [String] "about".
 */
@Preview(showBackground = true)
@Composable
fun AboutScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    AboutScreenContent(modifier)
}

@Composable
fun AboutScreenContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.about_android_trivia),
            contentDescription = null
        )
        Spacer(modifier = modifier.height(20.dp))
        Text(
            text = stringResource(id = R.string.about_text),
            fontSize = 20.sp
        )
    }
}