package app.cashadvisor.customView.multipleCircleProgressBar

import android.view.View

fun View.dp(dimensionDp: Int): Float {
    return dimensionDp * resources.displayMetrics.density
}