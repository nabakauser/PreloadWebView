package com.example.webviewdemo2

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast


class DeviceAdmin : DeviceAdminReceiver() {
    override fun onDisabled(context: Context, intent: Intent) {
        super.onDisabled(context, intent)
        Toast.makeText(context, "Device Admin has been disabled.", Toast.LENGTH_SHORT).show()
    }

    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
        Toast.makeText(context, "Device Admin has been enabled.", Toast.LENGTH_SHORT).show()
    }
}
