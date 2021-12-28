package com.chus.clua.citybikes.presentation.base

import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chus.clua.citybikes.App
import com.google.android.material.snackbar.Snackbar


abstract class BaseActivity: AppCompatActivity() {

    fun getComponent() = (application as App).getComponent()

    fun showToast(message: String?) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    fun showSnackBar(
        messageText: String,
        actionText: String,
        listener: View.OnClickListener
    ) {
        Snackbar.make(
            findViewById(android.R.id.content),
            messageText,
            Snackbar.LENGTH_INDEFINITE
        ).setAction(actionText, listener).show()
    }

}