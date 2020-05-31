package com.harper.carnet.ui.support

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.harper.carnet.ext.cast

/**
 * Created by HarperJr on 15:48
 **/
object Keyboard {

    fun hide(view: View) = view.context.getSystemService(Context.INPUT_METHOD_SERVICE).cast<InputMethodManager>()
        .hideSoftInputFromWindow(view.windowToken, 0)

    fun show(view: View) = view.context.getSystemService(Context.INPUT_METHOD_SERVICE).cast<InputMethodManager>()
        .showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}