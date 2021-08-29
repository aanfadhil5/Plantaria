package com.arazzi.plantaria.water.reminder;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.arazzi.plantaria.R;
import com.arazzi.plantaria.water.reminder.PlantsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WaterReminder extends AppCompatActivity {
    public static final String EXTRA_PLANT = "com.arazzi.plantaria.PLANT";
    public static final String POSITION = "com.arazzi.plantaria.POSITION";
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;


    ArrayList<Plant> plants;
    PlantsAdapter adapter;
    RecyclerView rvPlants;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_reminder);
        getSupportActionBar().setTitle("Water Reminder");

        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        File newdir = new File(dir);
        newdir.mkdirs();

        getPrefs();
        if (plants == null) plants = new ArrayList<>();

        FloatingActionButton myFab = findViewById(R.id.fab);
        myFab.setOnClickListener(v -> {
            Intent intent = new Intent(WaterReminder.this, AddPlantActivity.class);
            startActivityForResult(intent, 1);
        });

        // Lookup the recyclerview in activity layout
        rvPlants = findViewById(R.id.rvPlants);
        // Create adapter passing in the sample user data
        adapter = new PlantsAdapter(plants, this);
        // Attach the adapter to the recyclerview to populate items
        rvPlants.setAdapter(adapter);
        // Set layout manager to position the items
        rvPlants.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvPlants.addItemDecoration(itemDecoration);

//        createNotificationChannel();



    }

//    public void createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            CharSequence name = "plantariaChannel";
//            String description = "Channel for Plantaria notification";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel("CHANNELID", name, importance);
//            channel.setDescription(description);
//
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannels(Collections.singletonList(channel));
//        }
//
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Plant plant = data.getParcelableExtra(EXTRA_PLANT);

            plants.add(0, plant);

            Gson gson = new Gson();
            String jsonPlants = gson.toJson(plants);
            setDefaults("Plants", jsonPlants, this);

            adapter.notifyDataSetChanged();
            rvPlants.scrollToPosition(0);
        } else if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            Plant p = data.getParcelableExtra(EXTRA_PLANT);
            int position = data.getIntExtra(POSITION, 0);
            if (p != null) {
                plants.set(position, p);
                adapter.notifyItemChanged(position);
            } else {
                if (plants.get(position).getPic() != null) {
                    File file = new File(plants.get(position).getPic());
                    file.delete();
                }
                plants.remove(position);
                adapter.notifyDataSetChanged();
            }
        }
    }

    // Saves the current state of the app and the current date before closing
    @Override
    protected void onStop() {
        Gson gson = new Gson();
        Gson dateGson = Converters.registerLocalDate(new GsonBuilder()).create();

        String jsonPlants = gson.toJson(plants);

        LocalDate date = LocalDate.now();
        String jsonDate = dateGson.toJson(date);

        setDefaults("Plants", jsonPlants,this);
        setDefaults("Date", jsonDate, this);
        super.onStop();
    }

    // Upon launching the app, loads the saved state of the app and
    // subtracts the number of days passed from each plant watering schedule
    private void getPrefs() {
        Gson gson = new Gson();
        Gson dateGson = Converters.registerLocalDate(new GsonBuilder()).create();

        String jsonPlants = getDefaults("Plants", this);
        plants = gson.fromJson(jsonPlants, new TypeToken<List<Plant>>(){}.getType());

        String jsonDate = getDefaults("Date", this);
        LocalDate lastDate = dateGson.fromJson(jsonDate, LocalDate.class);

        LocalDate currDate = LocalDate.now();

        if (lastDate != null) {
            int daysPast = Days.daysBetween(lastDate, currDate).getDays();

            for (Plant p:plants) {
                p.setDaysUntilWater(p.getDaysUntilWater()-daysPast);
            }
        }
    }

    // Stores a string into shared preferences
    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    // Gets a string from shared preferences
    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }
}
    /*public void sendNotification(PlantsAdapter plantsAdapter){
        Intent resultIntent = new Intent(WaterReminder.this, PlantsAdapter.class);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNELID")
                .setSmallIcon(android.R.drawable.stat_notify_chat)
                .setContentTitle("abc")
                .setContentText("I need water")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
     */