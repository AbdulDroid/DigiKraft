package io.digikraft.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import io.digikraft.R

private fun View.createBitmapFromView(): Bitmap {
    measure(
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    )

    layout(0, 0, measuredWidth, measuredHeight)

    val canvas = Canvas()
    val bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)

    canvas.setBitmap(bitmap)
    draw(canvas)

    return bitmap
}

fun Context.createMapMarkerFromView(
    layoutResId: Int,
    count: String
): BitmapDescriptor {

    // retrieve the actual custom marker layout
    val view = LayoutInflater.from(this).inflate(layoutResId, null)
    view.findViewById<TextView>(R.id.tv_count).text = count
    val bm = view.createBitmapFromView()

    // draw it onto the bitmap
    return BitmapDescriptorFactory.fromBitmap(bm)
}