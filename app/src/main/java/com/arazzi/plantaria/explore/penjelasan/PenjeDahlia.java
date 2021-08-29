package com.arazzi.plantaria.explore.penjelasan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.arazzi.plantaria.R;

public class PenjeDahlia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penje_dahlia);
        getSupportActionBar().setTitle("Dahlia");
    }
}