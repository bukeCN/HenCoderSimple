package com.hy.hot_fix_simple;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button showTextBtn;
    Button hotFixBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text);

        showTextBtn = findViewById(R.id.showBtn);

        hotFixBtn = findViewById(R.id.hotFixBtn);

        showTextBtn.setOnClickListener(view -> {
            TextContent textContent = new TextContent();
            textView.setText(textContent.getContent());
        });

        hotFixBtn.setOnClickListener(view -> {
            File hotFixFile = new File(getCacheDir() + "/hotfix_2.dex");
            try {
                InputStream inputStream = getAssets().open("hotfix_2.dex");
                FileOutputStream fileOutputStream = new FileOutputStream(hotFixFile);
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                fileOutputStream.write(buffer);
                fileOutputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                String hotFixFilePath = hotFixFile.getPath();
                ClassLoader classLoader = getClassLoader();
                Class loaderClass = BaseDexClassLoader.class;
                Field pathListField = null;
                pathListField = loaderClass.getDeclaredField("pathList");

                pathListField.setAccessible(true);
                Object pathListObject = pathListField.get(classLoader);
                Class pathListClass = pathListObject.getClass();
                Field dexElementsField = pathListClass.getDeclaredField("dexElements");
                dexElementsField.setAccessible(true);
                Object dexElementsObject = dexElementsField.get(pathListObject);

                // classLoader.pathList.dexElements = ???;
                PathClassLoader newClassLoader = new PathClassLoader(hotFixFile.getPath(), null);
                Object newPathListObject = pathListField.get(newClassLoader);
                Object newDexElementsObject = dexElementsField.get(newPathListObject);

                int oldLength = Array.getLength(dexElementsObject);
                int newLength = Array.getLength(newDexElementsObject);
                Object concatDexElementsObject = Array.newInstance(dexElementsObject.getClass().getComponentType(), oldLength + newLength);
                for (int i = 0; i < newLength; i++) {
                    Array.set(concatDexElementsObject, i, Array.get(newDexElementsObject, i));
                }
                for (int i = 0; i < oldLength; i++) {
                    Array.set(concatDexElementsObject, newLength + i, Array.get(dexElementsObject, i));
                }

                dexElementsField.set(pathListObject, concatDexElementsObject);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("失败"+e.getMessage());
            }
//
//            try {
//                // 通过反射获取 BaseDexCLassLoader 中的 pathList，然后从其中获取 DexElements 对象
//                ClassLoader classLoader = getClassLoader();
//                Class baseDexClass = BaseDexClassLoader.class;
//                // 获取其私有属性 pathList
//                Field pathListField = baseDexClass.getDeclaredField("pathList");
//                pathListField.setAccessible(true);
//                Object pathListObj = pathListField.get(classLoader);
//                // 通过 pathList 获取 DexElement
//                Class pathListClass = pathListObj.getClass();
//                Field dexElementsFields = pathListClass.getDeclaredField("dexElements");
//                dexElementsFields.setAccessible(true);
//                Object dexElementsObject = dexElementsFields.get(pathListObj);
//
//                // 构造自己的 DexElement，然后将设置进入 dexElementsFields
//                PathClassLoader newClassLoader = new PathClassLoader(hotFixFilePath,null);
//                Object newPathList = pathListField.get(newClassLoader);
//                Object newDexElementsObject = dexElementsFields.get(newPathList);
//
//                // 进行合并
//                int oldLength = Array.getLength(dexElementsObject);
//                int newLength = Array.getLength(newDexElementsObject);
//                Object concatDexElementsObject = Array.newInstance(dexElementsObject.getClass().getComponentType(), oldLength + newLength);
//                for (int i = 0; i < newLength; i++) {
//                    Array.set(concatDexElementsObject, i, Array.get(newDexElementsObject, i));
//                }
//                for (int i = 0; i < oldLength; i++) {
//                    Array.set(concatDexElementsObject, newLength + i, Array.get(dexElementsObject, i));
//                }
//
//                // 相当于 ClassLoader.pathList.dexElements = newDexelmentObj
//                dexElementsFields.set(newPathList,concatDexElementsObject);
//            } catch (NoSuchFieldException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }

            TextContent textContent = new TextContent();
            textView.setText(textContent.getContent());

        });


    }
}
