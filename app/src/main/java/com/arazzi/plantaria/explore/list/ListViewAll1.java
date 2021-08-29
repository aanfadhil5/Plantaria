package com.arazzi.plantaria.explore.list;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.arazzi.plantaria.R;
import com.arazzi.plantaria.explore.penjelasan.PenjeAnyelir;
import com.arazzi.plantaria.explore.penjelasan.PenjeBougenville;
import com.arazzi.plantaria.explore.penjelasan.PenjeDahlia;
import com.arazzi.plantaria.explore.penjelasan.PenjeKamboja;
import com.arazzi.plantaria.explore.penjelasan.PenjeTasbih;

import java.util.ArrayList;

public class ListViewAll1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_all1);
        getSupportActionBar().setTitle("Indoor Plants");

        final ListView listViewAll1 = findViewById(R.id.list_viewall1);
        String[] values = new String[]{
                "Bougenvil", "Bunga Anyelir",
                "Bunga Dahlia", "Kamboja Jepang",
                "Bunga Tasbih"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listViewAll1.setAdapter(adapter);

        listViewAll1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    Intent intent = new Intent(view.getContext(), PenjeBougenville.class);
                    startActivity(intent); }
                if (position == 1){
                    Intent intent = new Intent(view.getContext(), PenjeAnyelir.class);
                    startActivity(intent);}
                if (position == 2){
                    Intent intent = new Intent(view.getContext(), PenjeDahlia.class);
                    startActivity(intent); }
                if (position == 3){
                    Intent intent = new Intent(view.getContext(), PenjeKamboja.class);
                    startActivity(intent); }
                if (position == 4){
                    Intent intent = new Intent(view.getContext(), PenjeTasbih.class);
                    startActivity(intent); }
            }
        });
    }
}