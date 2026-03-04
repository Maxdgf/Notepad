package com.example.notepad.utils

import android.app.Activity
import android.content.Context
import kotlin.system.exitProcess

class AppManager(private val activity: Activity?) {
    /**Exits app.*/
    fun breakApp() {
        activity?.finish() // finish activity
        exitProcess(0) // exit process
    }
}