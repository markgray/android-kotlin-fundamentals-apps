package com.example.aboutmecompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
    var nickNameEntry by rememberSaveable {
        mutableStateOf("")
    }
    var nickNameSaved by rememberSaveable {
        mutableStateOf("")
    }
    var showDoneButton by rememberSaveable {
        mutableStateOf(true)
    }
    var showEnterNickNameTextField by rememberSaveable {
        mutableStateOf(true)
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.name),
            fontSize = 20.sp
        )
        HideOrShow(showEnterNickNameTextField) {
            OutlinedTextField(
                value = nickNameEntry,
                onValueChange = {
                    nickNameEntry = it
                },
                label = {
                    Text(stringResource(id = R.string.what_is_your_nickname))
                }
            )
        }
        HideOrShow(showDoneButton) {
            DoneButton(onClick = { nickNameSaved = nickNameEntry })
            if (nickNameSaved != "") {
                showDoneButton = false
                showEnterNickNameTextField = false
            }
        }
        Text(
            text = nickNameSaved,
            fontSize = 20.sp,
            modifier = Modifier.clickable {
                showDoneButton = true
                showEnterNickNameTextField = true
                nickNameSaved = ""
            }
        )
        Box(modifier = Modifier
            .height(40.dp)
            .width(40.dp)) {
            Image(
                modifier =Modifier.fillMaxSize(1f),
                painter = painterResource(id = android.R.drawable.btn_star_big_on),
                contentDescription = stringResource(id = R.string.yellow_star)
            )
        }
        Column(
            modifier = modifier.verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.bio),
                fontSize = 20.sp,
                modifier = modifier.padding(start = 8.dp, end = 8.dp)
            )
            Text(
                text = stringResource(id = R.string.more_bio1),
                fontSize = 20.sp,
                modifier = modifier.padding(start = 8.dp, end = 8.dp)
            )
            Text(
                text = stringResource(id = R.string.more_bio2),
                fontSize = 20.sp,
                modifier = modifier.padding(start = 8.dp, end = 8.dp)
            )
        }
    }
}

@Composable
private fun DoneButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = stringResource(id = R.string.done))
    }
}

@Composable
private fun HideOrShow(
    show: Boolean = true,
    content: @Composable () -> Unit
) {
    if (show) {
        content()
    }

}