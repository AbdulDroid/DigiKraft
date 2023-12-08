package io.digikraft

import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import io.digikraft.models.fromJson
import io.digikraft.models.toJson
import io.digikraft.utils.getUserLocation
import io.digikraft.view.screens.DetailsScreen
import io.digikraft.view.screens.MainScreen
import io.digikraft.view.theme.DigiKraftTheme

@AndroidEntryPoint
@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberAnimatedNavController()
            var userLocation by remember { mutableStateOf<Location?>(null)}
            getUserLocation {
                userLocation = it
            }
            DigiKraftTheme {
                // A surface container using the "background" color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AnimatedNavHost(navController, startDestination = "home") {
                        composable("home",enterTransition = {
                            when (initialState.destination.route) {
                                "details/{data}" ->
                                    slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(1000))
                                else -> null
                            }
                        },
                            exitTransition = {
                                when (targetState.destination.route) {
                                    "details/{data}" ->
                                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                                    else -> null
                                }
                            },
                            popEnterTransition = {
                                when (initialState.destination.route) {
                                    "details/{data}" ->
                                        slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(1000))
                                    else -> null
                                }
                            },
                            popExitTransition = {
                                when (targetState.destination.route) {
                                    "Blue" ->
                                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                                    else -> null
                                }
                            }) {
                            MainScreen(location = userLocation) {
                                navController.navigate("details/${it.toJson()}")
                            }
                        }
                        composable("details/{data}", arguments = listOf(navArgument("data") {
                            type = NavType.StringType
                        })) {
                            val dataString = it.arguments?.getString("data")
                            dataString?.let { s ->
                                val data = s.fromJson()
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
    DigiKraftTheme {
        MainScreen { }
    }
}