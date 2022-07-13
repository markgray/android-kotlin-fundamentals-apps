package com.example.androidtriviacompose.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
        modifier = modifier.padding(8.dp),
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