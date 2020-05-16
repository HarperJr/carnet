package com.harper.carnet.ui.map.behaviour

import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior

class RoutingBottomSheetBehaviour<T : View> : BottomSheetBehavior<T>() {

    override fun onLayoutChild(parent: CoordinatorLayout, child: T, layoutDirection: Int): Boolean {
        return super.onLayoutChild(parent, child, layoutDirection)
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: T, dependency: View): Boolean {
        return super.onDependentViewChanged(parent, child, dependency)
    }
}