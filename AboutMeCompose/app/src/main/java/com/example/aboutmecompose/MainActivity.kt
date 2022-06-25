package com.example.aboutmecompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aboutmecompose.ui.theme.AboutMeComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AboutMeComposeTheme {
                AboutMeApp()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutMeApp() {
    NameNicknameButtonAndFishtail(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    )
}

@Composable
fun NameNicknameButtonAndFishtail(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.name), fontSize = 20.sp)
        Image(
            painter = painterResource(id = android.R.drawable.btn_star_big_on),
            contentDescription = stringResource(id = R.string.yellow_star)
        )
        Text(
            text = stringResource(id = R.string.bio),
            fontSize = 20.sp,
            modifier = modifier.padding(all = 8.dp)
        )
    }
}