package com.arazzi.plantaria.explore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arazzi.plantaria.R;
import com.arazzi.plantaria.explore.list.ListViewAll1;
import com.arazzi.plantaria.explore.list.ListViewAll2;
import com.arazzi.plantaria.explore.penjelasan.PenjeAnyelir;
import com.arazzi.plantaria.explore.penjelasan.PenjeBlanceng;
import com.arazzi.plantaria.explore.penjelasan.PenjeBougenville;
import com.arazzi.plantaria.explore.penjelasan.PenjeDahlia;
import com.arazzi.plantaria.explore.penjelasan.PenjeLidah;
import com.arazzi.plantaria.explore.penjelasan.PenjePalem;
;

public class Explore extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        getSupportActionBar().hide();

    }

    public void onClickBougenville(View view) {Intent intent = new Intent(Explore.this, PenjeBougenville.class);Explore.this.startActivity(intent);}

    public void onClickAnyelir(View view) {Intent intent = new Intent(Explore.this, PenjeAnyelir.class);Explore.this.startActivity(intent);}

    public void onClickDahlia(View view) {Intent intent = new Intent(Explore.this, PenjeDahlia.class);Explore.this.startActivity(intent);}

    public void onClickBlanceng(View view) {Intent intent = new Intent(Explore.this, PenjeBlanceng.class);Explore.this.startActivity(intent);}

    public void onClickLidah(View view) {Intent intent = new Intent(Explore.this, PenjeLidah.class);Explore.this.startActivity(intent);}

    public void onClickPalem(View view) {Intent intent = new Intent(Explore.this, PenjePalem.class);Explore.this.startActivity(intent);}

    public void onClickViewAll1(View view) { Intent intent = new Intent(Explore.this, ListViewAll1.class);Explore.this.startActivity(intent); }

    public void onClickViewAll2(View view) { Intent intent = new Intent(Explore.this, ListViewAll2.class);Explore.this.startActivity(intent); }
}