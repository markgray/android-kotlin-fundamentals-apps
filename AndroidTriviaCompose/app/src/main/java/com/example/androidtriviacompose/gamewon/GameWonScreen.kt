package com.example.androidtriviacompose.gamewon

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
import com.example.androidtriviacompose.R

@Preview
@Composable
fun GameWonScreen(
    modifier: Modifier = Modifier
) {
    GameWonScreenContent(modifier = modifier)
}

@Composable
fun GameWonScreenContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = modifier.height(100.dp))
        Image(
            painter = painterResource(id = R.drawable.you_win),
            contentDescription = null
        )
        Spacer(modifier = modifier.height(100.dp))
        Button(onClick = { /*TODO*/ }) {
            Text(
                text = stringResource(id = R.string.next_match),
                fontSize = 18.sp
            )
        }
    }
}