package com.arazzi.plantaria.water.reminder;


import android.annotation.TargetApi;
import android.app.Activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;

import android.os.Build;
import android.os.CountDownTimer;


import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.arazzi.plantaria.R;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.lang.Math.ceil;

public class PlantsAdapter extends RecyclerView.Adapter<PlantsAdapter.ViewHolder> {



    private OnItemClickListener listener;



    public interface OnItemClickListener {
        void OnItemClick(View itemView, int position);
    }

    private void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }



    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    class ViewHolder extends RecyclerView.ViewHolder{
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        TextView nameTextView;
        TextView speciesTextView;
        Button messageButton;
        CountDownTimer myCounter;
        CircleImageView picView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        ViewHolder(final View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = itemView.findViewById(R.id.plant_name);
            speciesTextView = itemView.findViewById(R.id.plant_species);
            messageButton = itemView.findViewById(R.id.message_button);
            picView = itemView.findViewById(R.id.plant_pic);


            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                        listener.OnItemClick(itemView, position);
                }
            });
        }
    }

    private final ArrayList<Plant> mPlants;
    private final Context context;

    PlantsAdapter(ArrayList<Plant> plants, Context c){
        mPlants = plants;
        context = c;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate the custom layout
        View itemView = inflater.inflate(R.layout.item_plant, parent, false);

        // Return a new holder instance
        return new ViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(@NotNull final PlantsAdapter.ViewHolder viewHolder, final int position) {
        if (getItemCount() > 0) {
            // Get the data model based on position
            final Plant plant = mPlants.get(position);

            // Set item views based on your views and data model
            TextView nameTextView = viewHolder.nameTextView;
            nameTextView.setText(plant.getName());
            TextView speciesTextView = viewHolder.speciesTextView;
            speciesTextView.setText(plant.getSpecies());
            final Button button = viewHolder.messageButton;
            button.getBackground().clearColorFilter();
            CircleImageView picView = viewHolder.picView;

            if (plant.getPic() != null) {
                Glide.with(context).load(Uri.parse(plant.getPic())).placeholder(R.drawable.ic_launcher_background).into(picView);
            } else {
                picView.setImageBitmap(null);
            }

            String daysUntilWater = Integer.toString(plant.getDaysUntilWater());
            button.setText(daysUntilWater);
            CountDownTimer cdt = viewHolder.myCounter;
            if (cdt == null) {
                cdt = new CountDownTimer(plant.getDaysUntilWater()*30000, 30000L)

                {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        plant.setDaysUntilWater((int) ceil(millisUntilFinished/30000.0));
                        button.setText(Integer.toString((int) ceil(millisUntilFinished/30000.0)));

                    }

                    @Override
                    public void onFinish() {
                        button.setText(R.string.needwater);
                        button.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        tampilNotifikasi(context);



                    }
                };
                cdt.start();

                final CountDownTimer finalCdt = cdt;

                setOnItemClickListener((itemView, position1) -> {
                    finalCdt.cancel();
                    Plant p = mPlants.get(position1);
                    Intent intent = new Intent(context, ChangePlantActivity.class);
                    intent.putExtra(WaterReminder.EXTRA_PLANT, p);
                    intent.putExtra(WaterReminder.POSITION, position1);

                    ((Activity) context).startActivityForResult(intent, 2);
                });

                button.setOnClickListener(v -> {
                    finalCdt.cancel();
                    viewHolder.myCounter = null;
                    plant.setDaysUntilWater(plant.getWaterSchedule());
                    button.getBackground().clearColorFilter();
                    notifyItemChanged(position);
                });
            }
        }

    }
@TargetApi(Build.VERSION_CODES.O)
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void tampilNotifikasi(Context context) {
        Intent intent = new Intent(context.getApplicationContext(),WaterReminder.class);
        String CHANNEL_ID="Plantaria";
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,"name",NotificationManager.IMPORTANCE_LOW);
        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(),1,intent,0);

    Notification notification = new Notification.Builder(context.getApplicationContext(),CHANNEL_ID)
                .setContentTitle("Plant Water Reminder")
                .setContentText("Some of your plants need water")
                .setContentIntent(pendingIntent)
                .setChannelId(CHANNEL_ID)
                .setSmallIcon(R.drawable.a)
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
        notificationManager.notify(1,notification);
    }


    @Override
    public int getItemCount() {
        return mPlants == null ? 0 : mPlants.size();
    }

}
