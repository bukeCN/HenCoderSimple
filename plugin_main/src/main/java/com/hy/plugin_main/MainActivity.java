package com.hy.plugin_main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File file = new File(getCacheDir() + "/da.apk");
        try {
            InputStream inputStream = getAssets().open("pluginable-debug.apk");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);

            fileOutputStream.write(buffer, 0, buffer.length);
            inputStream.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("文件是否存在"+file.exists());
        DexClassLoader dexClassLoader = new DexClassLoader(file.getPath(), getCacheDir().getPath(), null, null);
        try {
            Class cl = dexClassLoader.loadClass("com.hy.pluginable.Utils");
            Object object = cl.newInstance();
            Method method = cl.getMethod("shout");
            method.invoke(object);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }
}
