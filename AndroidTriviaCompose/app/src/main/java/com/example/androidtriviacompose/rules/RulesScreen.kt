@file:Suppress("unused")

package com.example.androidtriviacompose.rules

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
import com.example.androidtriviacompose.R

@Preview(showBackground = true)
@Composable
fun RulesScreen(
    modifier: Modifier = Modifier
) {
    RulesScreenContent(modifier)
}

@Composable
fun RulesScreenContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(8.dp),
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
