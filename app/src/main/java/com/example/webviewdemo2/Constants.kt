package com.example.webviewdemo2

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView


@SuppressLint("StaticFieldLeak")
object Constants{
    var cachedView : WebView? = null
}

fun View?.removeSelf() {
    this ?: return
    val parentView = parent as? ViewGroup ?: return
    parentView.removeView(this)
}

object WebViewUrl {
    const val WebURL = "https://digiclass.digivalsolutions.com/liveness_mobile/mobile.html?appname=DC&type=student&employeeOrAcademicId=555555585&platform=android&debug=false&faceurl=https://ecs-auth-staging.digi-val.com/api/v0/auth/facial"
    const val WebUrlNew = "https://ecs-dcweb-staging.digivalitsolutions.com/face_verification_mobile/mobile.html?appname=DC&type=student&employeeOrAcademicId=8521245567&platform=android&debug=false&faceurl=https://ecs-auth-staging.digivalitsolutions.com/api/v0/auth/facial-labeled-descriptors?employeeOrAcademicId="
}
