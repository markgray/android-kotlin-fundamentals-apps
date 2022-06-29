package com.example.colormyviewscompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.colormyviewscompose.ui.theme.ColorMyViewsComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ColorMyViewsComposeTheme {
                ColorMyViewApp()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ColorMyViewApp() {
    ColumnContent(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopCenter)
    )
}

@Composable
fun ColumnContent(modifier: Modifier = Modifier) {
    var boxOneColor by remember {
        mutableStateOf(Color.White)
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.box_one),
            fontSize = 24.sp,
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp)
                .background(boxOneColor)
                .padding(16.dp)
                .clickable {
                    boxOneColor = Color.DarkGray
                }
        )
    }
}