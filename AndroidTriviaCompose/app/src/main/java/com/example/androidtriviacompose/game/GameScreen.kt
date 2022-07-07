@file:Suppress("unused")

package com.example.androidtriviacompose.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.Button
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidtriviacompose.R

@Preview
@Composable
fun GameScreen(
    modifier: Modifier = Modifier
) {
    GameScreenContent(modifier = modifier)
}

@Composable
fun GameScreenContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.android_category_simple),
            contentDescription = null
        )
        QuestionContent(modifier = modifier)
        Button(onClick = { /*TODO*/ }) {
            Text(
                text = stringResource(id = R.string.submit_button),
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun QuestionContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.selectableGroup()
    ) {
        Text(
            text = "What color is the Android mascot?",
            fontSize = 30.sp
        )
        Row {
            RadioButton(selected = false, onClick = { /*TODO*/ })
            Text(
                modifier = modifier.padding(top = 12.dp),
                text = "Blue"
            )
        }
        Row {
            RadioButton(selected = false, onClick = { /*TODO*/ })
            Text(
                modifier = modifier.padding(top = 12.dp),
                text = "Green"
            )
        }
        Row {
            RadioButton(selected = false, onClick = { /*TODO*/ })
            Text(
                modifier = modifier.padding(top = 12.dp),
                text = "Yellow"
            )
        }
        Row {
            RadioButton(selected = false, onClick = { /*TODO*/ })
            Text(
                modifier = modifier.padding(top = 12.dp),
                text = "Red"
            )
        }
    }
}

