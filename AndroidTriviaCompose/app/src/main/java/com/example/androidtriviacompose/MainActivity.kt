package com.example.androidtriviacompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.androidtriviacompose.about.AboutScreen
import com.example.androidtriviacompose.game.GameScreen
import com.example.androidtriviacompose.gameover.GameOverScreen
import com.example.androidtriviacompose.gamewon.GameWonScreen
import com.example.androidtriviacompose.rules.RulesScreen
import com.example.androidtriviacompose.title.TitleScreen
import com.example.androidtriviacompose.ui.theme.AndroidTriviaComposeTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * This is the launch activity of the Android Trivia game.
 */
class MainActivity : ComponentActivity() {
    /**
     * Called when the activity is starting. First we call our super's implementation of `onCreate`,
     * then we call the [setContent] method to have it compose the composable passed in its `content`
     * lambda into the our activity. The content will become the root view of the activity. That
     * composable is our [AndroidTriviaApp] composable which is wrapped in the custom [MaterialTheme]
     * defined by [AndroidTriviaComposeTheme].
     *
     * @param savedInstanceState we do not override [onSaveInstanceState] so we ignore it.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidTriviaComposeTheme {
                AndroidTriviaApp()
            }
        }
    }
}

/**
 * This class defines the navigation routes that can be requested of the [NavHost] in our [NavGraph]
 * composable.
 *
 * @param route the [String] that is used as the route argument of the [NavGraphBuilder.composable]
 * method in the [NavHost] composable of [NavGraph] for the screen which is the composable for the
 * destination given in the [NavGraphBuilder.composable] `content` lambda argument.
 */
sealed class Routes(val route: String) {
    /**
     * Used to navigate to the [AboutScreen] composable.
     */
    object About : Routes("about")

    /**
     * Used to navigate to the [GameScreen] composable.
     */
    object Game : Routes("game")

    /**
     * Used to navigate to the [GameOverScreen] composable.
     */
    object GameOver : Routes("gameover")

    /**
     * Used to navigate to the [GameWonScreen] composable.
     */
    object GameWon : Routes("gamewon")

    /**
     * Used to navigate to the [RulesScreen] composable.
     */
    object Rules : Routes("rules")

    /**
     * Used to navigate to the [TitleScreen] composable.
     */
    object Title : Routes("title")
}

@Suppress("UNUSED_PARAMETER")
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
            AboutScreen(navController = navController)
        }
        composable(Routes.Game.route) {
            GameScreen(navController = navController)
        }
        composable(Routes.GameOver.route) {
            GameOverScreen(navController = navController)
        }
        composable(Routes.GameWon.route) {
            GameWonScreen(navController = navController)
        }
        composable(Routes.Rules.route) {
            RulesScreen(navController = navController)
        }
        composable(Routes.Title.route) {
            TitleScreen(navController = navController)
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
fun DrawerContent(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    scope: CoroutineScope
) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            scope.launch { scaffoldState.drawerState.close() }
            navController.navigate(Routes.Rules.route)
        }) {
        Image(
            painter = painterResource(id = R.drawable.rules),
            contentDescription = stringResource(id = R.string.rules)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = stringResource(id = R.string.rules))
    }
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            scope.launch { scaffoldState.drawerState.close() }
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
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val scope: CoroutineScope = rememberCoroutineScope()
    val navController: NavHostController = rememberNavController()
    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {
            DrawerContent(
                navController = navController,
                scaffoldState = scaffoldState,
                scope = scope
            )
        },
        drawerShape = MaterialTheme.shapes.small,
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