package com.test.hencodersimple.view10;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.test.hencodersimple.view06.Utils;

import java.util.Random;

/**
 * Time: 2020/7/9
 * Author: liupang
 * Description:
 */
public class ColorsTextView extends androidx.appcompat.widget.AppCompatTextView {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Random colorRadom;
    private Random textSizeRadom;

    public static final int[] colors = new int[]{
            Color.parseColor("#1abc9a"),
            Color.RED,
            Color.GREEN,
            Color.parseColor("#8e44ad"),
            Color.parseColor("#3498db")
    };
    private static final int[] textSizes = new int[]{
            12,
            16,
            18,
            26
    };

    public ColorsTextView(Context context) {
        super(context);
    }

    public ColorsTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        Random random = new Random();
//
//        ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(context,attrs);
//        marginLayoutParams.leftMargin = (int) Utils.dp2px(random.nextInt(6));
//        marginLayoutParams.topMargin = (int) Utils.dp2px(random.nextInt(4));
//        marginLayoutParams.rightMargin = (int) Utils.dp2px(random.nextInt(10));
//        marginLayoutParams.bottomMargin = (int) Utils.dp2px(random.nextInt(16));
//
//        setLayoutParams(marginLayoutParams);


        setTextColor(Color.WHITE);
        setTextSize(textSizes[new Random().nextInt(textSizes.length)]);

        setBackgroundColor(colors[new Random().nextInt(colors.length)]);
    }
}
