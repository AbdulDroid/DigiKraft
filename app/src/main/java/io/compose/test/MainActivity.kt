package io.compose.test

import android.location.Location
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import io.compose.test.models.fromJson
import io.compose.test.models.toJson
import io.compose.test.utils.getUserLocation
import io.compose.test.view.screens.DetailsScreen
import io.compose.test.view.screens.MainScreen
import io.compose.test.view.theme.ComposeTestTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            var userLocation by remember { mutableStateOf<Location?>(null) }
            getUserLocation {
                userLocation = it
            }
            ComposeTestTheme {
                // A surface container using the "background" color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController, startDestination = "home") {
                        composable("home", enterTransition = {
                            when (initialState.destination.route) {
                                "details/{data}" ->
                                    slideIntoContainer(
                                        AnimatedContentTransitionScope.SlideDirection.Left,
                                        animationSpec = tween(1000)
                                    )

                                else -> null
                            }
                        },
                            exitTransition = {
                                when (targetState.destination.route) {
                                    "details/{data}" ->
                                        slideOutOfContainer(
                                            AnimatedContentTransitionScope.SlideDirection.Left,
                                            animationSpec = tween(700)
                                        )

                                    else -> null
                                }
                            },
                            popEnterTransition = {
                                when (initialState.destination.route) {
                                    "details/{data}" ->
                                        slideIntoContainer(
                                            AnimatedContentTransitionScope.SlideDirection.Right,
                                            animationSpec = tween(1000)
                                        )

                                    else -> null
                                }
                            },
                            popExitTransition = {
                                when (targetState.destination.route) {
                                    "Blue" ->
                                        slideOutOfContainer(
                                            AnimatedContentTransitionScope.SlideDirection.Right,
                                            animationSpec = tween(700)
                                        )

                                    else -> null
                                }
                            }) {
                            MainScreen(location = userLocation) {
                                val encoded = Uri.encode(it.toJson())
                                navController.navigate("details/$encoded")
                            }
                        }
                        composable("details/{data}", arguments = listOf(navArgument("data") {
                            type = NavType.StringType
                        })) {
                            val dataString = it.arguments?.getString("data")
                            dataString?.let { s ->
                                val data = Uri.decode(s).fromJson()
                                DetailsScreen(userLocation = userLocation, data = data) {
                                    navController.navigateUp()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeTestTheme {
        MainScreen { }
    }
}