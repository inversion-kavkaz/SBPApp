package ru.inversion.sbpapplication.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.FragmentActivity



/**Для расширений*/
class Extension {

    companion object{

        fun EditText.openKeyboard(fragment: FragmentActivity) {
            val imm = fragment.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        }

        fun EditText.closeKeyboard(fragment: FragmentActivity) {
            val imm = fragment.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.windowToken, 0)
        }

        fun Button.setEnabled(isEnabled: Boolean) {
            this.isEnabled = isEnabled
        }
        fun EditText.hideKeyboard() {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as    InputMethodManager
            imm.hideSoftInputFromWindow(this.windowToken, 0)
        }


    }

}