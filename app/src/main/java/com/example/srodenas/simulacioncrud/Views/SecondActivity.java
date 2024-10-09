package com.example.srodenas.simulacioncrud.Views;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.srodenas.simulacioncrud.Logic.ClaseA;
import com.example.srodenas.simulacioncrud.R;


public class SecondActivity extends AppCompatActivity {
    private static final String TAG = "SecondActivity";
    private String variable1 = "Inicial";
    private String variable2 = "Inicial";
    private Button btnSaludar;
    private Button btnNoSaludar;
    private Button btnSaludarClase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        Button btnSaludar = findViewById(R.id.btn_saludar);
        Button btnNoSaludar = findViewById(R.id.btn_no_saludar);
        Button btnSaludarClase = findViewById(R.id.btn_saludar_clase);
        btnSaludar.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Has pulsado el botón Saludar");
            builder.show();
        });
        btnNoSaludar.setOnClickListener(view -> {
            AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
            builder2.setMessage("Has pulsado el botón No Saludar");
            builder2.show();
        });
        btnSaludarClase.setOnClickListener(view -> {
            AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
            builder3.setMessage("Has pulsado el botón Saludar a Clase");
            builder3.show();
        });
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
