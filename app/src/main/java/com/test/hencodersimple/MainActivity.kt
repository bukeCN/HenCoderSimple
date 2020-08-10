package com.test.hencodersimple

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.RotateAnimation
import android.widget.Toast
import com.test.hencodersimple.view08.RotateAnimatorImageView
import com.test.hencodersimple.view09.MeterialEditText
import com.test.hencodersimple.view11.TouchView
import java.util.concurrent.ExecutorService
import java.util.concurrent.ThreadPoolExecutor

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}