package com.amjrd.novalauncher.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class PrivateSpaceReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context,intent: Intent) {
        // Profile availability is consumed by LauncherViewModel on the next resume.
    }
}