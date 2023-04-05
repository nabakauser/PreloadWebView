package com.example.webviewdemo2

import android.Manifest
import android.annotation.SuppressLint
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.webviewdemo2.WebViewUrl.WebURL
import com.example.webviewdemo2.WebViewUrl.WebUrlNew
import com.example.webviewdemo2.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val DPM_REQUEST_CODE = 100
    private lateinit var adminComponent: ComponentName
    private var devicePolicyManager: DevicePolicyManager? = null

    private var binding: ActivityMainBinding? = null
    private val requestCameraPermissionLauncher by lazy {
        registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
            ::onPermissionResult
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        requestCameraPermissionLauncher
        checkAndRequestCameraPermission()

        setupWebView()
        setUpListeners()

        deviceAdmin()
        cameraSwitch()
    }


    private fun setUpListeners() {
        binding?.uiBtnWebView?.setOnClickListener {
            val intent = Intent(this, WebViewActivity::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private fun cameraSwitch() {
        val cameraSwitch = binding?.uiSwitch

        cameraSwitch?.setOnCheckedChangeListener { _, isEnabled ->
            try {
                if (isEnabled) {
                    devicePolicyManager?.setCameraDisabled(
                        adminComponent,
                        false)
                    Toast.makeText(this,"Camera is enabled" , Toast.LENGTH_SHORT).show()
                } else {
                    devicePolicyManager?.setCameraDisabled(
                        adminComponent,
                        true)
                    Toast.makeText(this,"Camera is disabled " , Toast.LENGTH_SHORT).show()
                }
            }
            catch (securityException: SecurityException) {
                Log.e(
                    "Device Administrator",
                    "Error occurred while disabling/enabling camera - " + securityException.message
                )
            }
        }
        cameraSwitch?.isChecked = !devicePolicyManager?.getCameraDisabled(adminComponent)!!
    }

    private fun onPermissionResult(isSuccess: Boolean) {
        if (isSuccess)
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        else {
            Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT)
                .show()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        Constants.cachedView = WebView(this)
       // Handler(Looper.getMainLooper()).postDelayed({
            Constants.cachedView?.apply {
                requestFocus()
                webChromeClient = OnlineWebChromeClient()
                settings.mediaPlaybackRequiresUserGesture = false
                settings.javaScriptEnabled = true
                settings.setSupportMultipleWindows(true)
                settings.domStorageEnabled = true
                settings.javaScriptCanOpenWindowsAutomatically = true
                settings.setSupportZoom(true)
                addJavascriptInterface(WebAppInterface(), "Mobile")
                loadUrl(WebUrlNew)
            }
       // },5000)
    }

    inner class WebAppInterface() {
        @JavascriptInterface
        fun showMessageFromWeb(message: String) {
            Log.d("WebAppInterface", "showMessageFromWeb: $message")
            runOnUiThread {
                // viewModel.onMessageFromWeb(message)
            }
        }
    }

    inner class OnlineWebChromeClient : WebChromeClient() {
        override fun onPermissionRequest(request: PermissionRequest) {
            request.grant(
                arrayOf(PermissionRequest.RESOURCE_VIDEO_CAPTURE)
            )
        }
    }

    private fun checkAndRequestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun deviceAdmin() {
        devicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        adminComponent = ComponentName(packageName, "$packageName.DeviceAdministrator")

        if (!devicePolicyManager?.isAdminActive(adminComponent)!!) {
            Log.e("D_Admin", "onCreate: device policy manager is working", )
            val activateDeviceAdmin = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
            activateDeviceAdmin.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent)
            startActivityForResult(activateDeviceAdmin, DPM_REQUEST_CODE)
        }
    }

}

