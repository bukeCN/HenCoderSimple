package com.hy.hiltsimple

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UserView(context: Context, attrs: AttributeSet?) : AppCompatTextView(context, attrs) {
    @Inject lateinit var user: User

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        user.mood = "搞定了共享"
        text = "${user.name}初次使用Hilt的心情是：${user.mood}"
    }
}