package com.arazzi.plantaria.explore.list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.arazzi.plantaria.R;
import com.arazzi.plantaria.explore.penjelasan.PenjeAnyelir;
import com.arazzi.plantaria.explore.penjelasan.PenjeBlanceng;
import com.arazzi.plantaria.explore.penjelasan.PenjeBougenville;
import com.arazzi.plantaria.explore.penjelasan.PenjeDahlia;
import com.arazzi.plantaria.explore.penjelasan.PenjeEkor;
import com.arazzi.plantaria.explore.penjelasan.PenjeKamboja;
import com.arazzi.plantaria.explore.penjelasan.PenjeLidah;
import com.arazzi.plantaria.explore.penjelasan.PenjePalem;
import com.arazzi.plantaria.explore.penjelasan.PenjePuring;
import com.arazzi.plantaria.explore.penjelasan.PenjeTasbih;

public class ListViewAll2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_all2);
        getSupportActionBar().setTitle("Outdoor Plants");

        final ListView listViewAll1 = findViewById(R.id.list_viewall2);
        String[] values = new String[]{
                "Blanceng", "Lidah Buaya",
                "Palem Kuning", "Ekor Kucing",
                "Puring"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listViewAll1.setAdapter(adapter);

        listViewAll1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    Intent intent = new Intent(view.getContext(), PenjeBlanceng.class);
                    startActivity(intent); }
                if (position == 1){
                    Intent intent = new Intent(view.getContext(), PenjeLidah.class);
                    startActivity(intent);}
                if (position == 2){
                    Intent intent = new Intent(view.getContext(), PenjePalem.class);
                    startActivity(intent); }
                if (position == 3){
                    Intent intent = new Intent(view.getContext(), PenjeEkor.class);
                    startActivity(intent); }
                if (position == 4){
                    Intent intent = new Intent(view.getContext(), PenjePuring.class);
                    startActivity(intent); }
            }
        });
    }
}