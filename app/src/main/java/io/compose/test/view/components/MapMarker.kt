package io.compose.test.view.components

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.compose.runtime.Composable
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import io.compose.test.utils.createMapMarkerFromView

@Composable
fun MapMarker(
    context: Context,
    position: MarkerState,
    title: String,
    count: String,
    onMarkerClick: (Marker) -> Boolean,
    @LayoutRes layoutResId: Int
) {
    val icon = context.createMapMarkerFromView(
        layoutResId, count
    )
    Marker(
        state = position,
        title = title,
        icon = icon,
        onClick = onMarkerClick
    )
}