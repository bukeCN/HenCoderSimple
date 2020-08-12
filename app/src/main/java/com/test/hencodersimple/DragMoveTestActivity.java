package com.test.hencodersimple;

import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

/**
 * Time: 2020/7/20
 * Author: liupang
 * Description:
 */
public class DragMoveTestActivity extends AppCompatActivity {
    private ImageView dragView;
    private LinearLayout layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        dragView = findViewById(R.id.dragView);
        layout = findViewById(R.id.layout);

        dragView.setOnLongClickListener( v -> {
            v.setElevation(v.getElevation()+1);
            return ViewCompat.startDragAndDrop(v,null,new View.DragShadowBuilder(v),"Nice",0);
        });

        layout.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()){
                    case DragEvent.ACTION_DRAG_ENDED:
                        Log.e("sun","结束");
                        break;
                    case DragEvent.ACTION_DROP:
                        Log.e("sun","目标");
                        String msg = (String) event.getLocalState();
                        LinearLayout linearLayout = (LinearLayout) v;
                        TextView textView = new TextView(DragMoveTestActivity.this);
                        textView.setText(msg);
                        textView.setTextSize(18);
                        linearLayout.addView(textView);
                }
                return true;
            }


        });

    }
}
