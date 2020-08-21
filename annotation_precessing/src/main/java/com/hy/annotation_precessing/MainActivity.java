package com.hy.annotation_precessing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.hy.annotation.BindView;
import com.hy.annotation_lib.Binding;


public class MainActivity extends AppCompatActivity {
    @BindView(R.id.text)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Binding.bind(this);

        textView.setText("成功");
    }
}
