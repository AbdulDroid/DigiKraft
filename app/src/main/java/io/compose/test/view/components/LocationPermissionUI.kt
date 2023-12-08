package io.compose.test.view.components

import android.Manifest
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import io.compose.test.utils.checkLocationPermission
import io.compose.test.utils.shouldShowPermissionRationale


@Composable
fun LocationPermissionUI(
    context: Context,
    permissionRationale: String,
    hostState: SnackbarHostState,
    permissionAction: (Boolean) -> Unit
) {


    val permissionGranted =
        context.checkLocationPermission()

    //Need both permission to better handle location restrictions in Android 12 see here: https://developer.android.com/training/location/permissions#approximate-request
    val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    if (permissionGranted) {
        permissionAction(true)
        return
    }


    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        permissionAction(results.all { it.value })
    }


    val showPermissionRationale = context.shouldShowPermissionRationale()


    if (showPermissionRationale) {

        LaunchedEffect(showPermissionRationale) {

            val snackBarResult = hostState.showSnackbar(
                message = permissionRationale,
                actionLabel = "Grant Access",
                duration = SnackbarDuration.Long

            )
            when (snackBarResult) {
                SnackbarResult.Dismissed -> {
                    //User denied the permission, do nothing
                    permissionAction(false)
                }

                SnackbarResult.ActionPerformed -> {
                    launcher.launch(permissions)
                }
            }
        }
    } else {
        //Request permissions again
        SideEffect {
            launcher.launch(permissions)
        }

    }


}
