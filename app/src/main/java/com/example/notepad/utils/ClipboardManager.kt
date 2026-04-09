package com.example.notepad.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

class ClipBoardManager(context: Context) {
    private val clipboard =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager // get clipboard manager

    /**
     * Sets text to clipboard.
     * @param text text string.
     * @param label clipboard item label.
     */
    fun setTextToClipboard(
        text: String,
        label: String = "Notepad note",
    ) {
        val clipData = ClipData.newPlainText(label, text) // new clip data
        clipboard.setPrimaryClip(clipData) // set clip data
    }
}