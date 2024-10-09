package com.example.srodenas.simulacioncrud.Views;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.srodenas.simulacioncrud.Logic.ClaseA;


public class SecondActivity extends AppCompatActivity {
    private static final String TAG = "SecondActivity";
    private String variable1 = "Inicial";
    private String variable2 = "Inicial";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        variable1 = "onCreate";
        variable2 = "onCreate";
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

        ClaseA objetoA = new ClaseA(1, "Valor 2", true);
        Toast.makeText(this, objetoA.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        variable1 = "onResume";
        variable2 = "onResume";
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        variable1 = "onPause";
        variable2 = "onPause";
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        variable1 = "onStop";
        variable2 = "onStop";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        variable1 = "onDestroy";
        variable2 = "onDestroy";
    }
}
