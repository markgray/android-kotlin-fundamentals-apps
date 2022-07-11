package com.example.androidtriviacompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidtriviacompose.ui.theme.AndroidTriviaComposeTheme
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.androidtriviacompose.about.AboutScreen
import com.example.androidtriviacompose.game.GameScreen
import com.example.androidtriviacompose.gameover.GameOverScreen
import com.example.androidtriviacompose.gamewon.GameWonScreen
import com.example.androidtriviacompose.rules.RulesScreen
import com.example.androidtriviacompose.title.TitleScreen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidTriviaComposeTheme {
                AndroidTriviaApp()
            }
        }
    }
}

sealed class Routes(val route: String) {
    object About : Routes("about")
    object Game : Routes("game")
    object GameOver : Routes("gameover")
    object GameWon : Routes("gamewon")
    object Rules : Routes("rules")
    object Title : Routes("title")
}

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    finishActivity: () -> Unit = {},
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.Title.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.About.route) {
            AboutScreen()
        }
        composable(Routes.Game.route) {
            GameScreen()
        }
        composable(Routes.GameOver.route) {
            GameOverScreen()
        }
        composable(Routes.GameWon.route) {
            GameWonScreen()
        }
        composable(Routes.Rules.route) {
            RulesScreen()
        }
        composable(Routes.Title.route) {
            TitleScreen()
        }
    }
}

@Composable
fun AndroidTriviaApp() {
    MainScaffold(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    )
}

@Composable
fun DrawerContent(navController: NavHostController) {
    Button(onClick = {
        navController.navigate(Routes.Rules.route)
    }) {
        Image(
            painter = painterResource(id = R.drawable.rules),
            contentDescription = stringResource(id = R.string.rules)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = stringResource(id = R.string.rules))
    }
    Button(onClick = {
        navController.navigate(Routes.About.route)
    }) {
        Image(
            painter = painterResource(id = R.drawable.about_button),
            contentDescription = stringResource(id = R.string.about)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = stringResource(id = R.string.about))
    }
}

@Preview(showBackground = true)
@Composable
fun MainScaffold(
    modifier: Modifier = Modifier
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val navController: NavHostController = rememberNavController()
    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = { DrawerContent(navController) },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.android_trivia)) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            scope.launch { scaffoldState.drawerState.open() }
                        }
                    ) {
                        Icon(Icons.Filled.Menu, contentDescription = "Localized description")
                    }
                }
            )
        },
        content = { innerPadding ->
            NavGraph(
                modifier = Modifier.padding(innerPadding),
                navController = navController
            )
        }
    )
}