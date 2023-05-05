package com.vpn.client.utils

import android.app.Application
import android.widget.Toast
import javax.inject.Inject

class ToastShower @Inject constructor(
    private val context: Application
) {
    fun show(textId: Int) = Toast.makeText(context, textId, Toast.LENGTH_SHORT).show()
}