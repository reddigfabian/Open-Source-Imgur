package com.fret.open_source_imgur.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fret.open_source_imgur.R

class OpenSourceImgurActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle(com.fret.shared_resources.R.string.app_name)
    }
}