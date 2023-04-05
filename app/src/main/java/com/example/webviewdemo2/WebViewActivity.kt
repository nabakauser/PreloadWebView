package com.example.webviewdemo2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import com.example.webviewdemo2.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {
    private var binding: ActivityWebViewBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Constants.cachedView = WebView(this)
        setUpWebView()
        setUpListeners()

    }

    private fun setUpListeners() {
        binding?.uiBtnBack?.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            //Constants.cachedView?.removeAllViews()
            //Constants.cachedView?.clearHistory()
            //Constants.cachedView?.clearCache(true)
            //Constants.cachedView?.clearView()
            Constants.cachedView?.destroy()
        }
    }

    private fun setUpWebView() {
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        Constants.cachedView?.removeSelf()
        val layout = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        binding?.uiClWeb?.addView(Constants.cachedView, layout)
    }
}