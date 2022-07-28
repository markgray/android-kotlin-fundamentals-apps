package com.example.dessertclickercompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.dessertclickercompose.ui.theme.DessertClickerComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DessertClickerComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreen() {
    ConstraintLayoutContent(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopCenter)
    )
}

@Composable
fun ConstraintLayoutContent(modifier: Modifier = Modifier) {
    ConstraintLayout(modifier = modifier) {
        val backgroundImage: ConstrainedLayoutReference = createRef()
        val whiteBackground: ConstrainedLayoutReference = createRef()
        val dessertButton: ConstrainedLayoutReference = createRef()
        val revenueText: ConstrainedLayoutReference = createRef()
        val dessertSoldText: ConstrainedLayoutReference = createRef()
        val amountSoldText: ConstrainedLayoutReference = createRef()

        var dessertId by remember {
            mutableStateOf(bakery.currentDessert.imageId)
        }
        var dessertsSold by remember {
            mutableStateOf(bakery.dessertsSold)
        }
        var revenue by remember {
            mutableStateOf(bakery.revenue)
        }

        Image(
            painter = painterResource(R.drawable.bakery_back),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .constrainAs(backgroundImage) {
                    top.linkTo(parent.top)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(parent.absoluteRight)
                }
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color.White)
                .constrainAs(whiteBackground) {
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(parent.absoluteRight)
                    bottom.linkTo(parent.bottom)
                }
        )
        Image(
            painter = painterResource(id = dessertId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(150.dp)
                .constrainAs(dessertButton) {
                    top.linkTo(parent.top)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(parent.absoluteRight)
                    bottom.linkTo(whiteBackground.top)
                }
                .clickable {
                    bakery.onDessertClicked()
                    dessertId = bakery.currentDessert.imageId
                    dessertsSold = bakery.dessertsSold
                    revenue = bakery.revenue
                }
        )
        Text(
            text = "$$revenue",
            fontSize = 33.sp,
            color = Color.Green,
            modifier = Modifier
                .constrainAs(revenueText) {
                    absoluteRight.linkTo(
                        parent.absoluteRight,
                        margin = 16.dp
                    )
                    bottom.linkTo(
                        parent.bottom,
                        margin = 16.dp
                    )
                }
        )
        Text(
            text = "Dessert Sold",
            fontSize = 20.sp,
            modifier = Modifier
                .constrainAs(dessertSoldText) {
                    absoluteLeft.linkTo(
                        parent.absoluteLeft,
                        margin = 16.dp
                    )
                    top.linkTo(
                        whiteBackground.top,
                        margin = 16.dp
                    )
                }
        )
        Text(
            text = "$dessertsSold",
            fontSize = 20.sp,
            modifier = Modifier
                .constrainAs(amountSoldText) {
                    absoluteRight.linkTo(
                        parent.absoluteRight,
                        margin = 16.dp
                    )
                    top.linkTo(
                        whiteBackground.top,
                        margin = 16.dp
                    )
                }
        )

    }
}
