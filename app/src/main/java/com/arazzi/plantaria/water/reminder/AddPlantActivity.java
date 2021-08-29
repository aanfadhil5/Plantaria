package com.arazzi.plantaria.water.reminder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.exifinterface.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.arazzi.plantaria.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.arazzi.plantaria.water.reminder.WaterReminder.REQUEST_ID_MULTIPLE_PERMISSIONS;

public class AddPlantActivity extends AppCompatActivity {
    final int TAKE_PHOTO_CODE = 1;
    final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File newDir = new File(dir);
        newDir.mkdirs();
        setContentView(R.layout.activity_add_plant);
        getSupportActionBar().hide();
    }

    public void onClick(View view){
        EditText waterScheduleEditText = findViewById(R.id.new_plant_water_schedule);
        String temp = waterScheduleEditText.getText().toString();
        if (temp.matches("")){
            Toast.makeText(this, "Please enter the plant's watering schedule", Toast.LENGTH_SHORT).show();
        } else {
            int waterSchedule = Integer.parseInt(temp);
            if (waterSchedule <= 0) {
                Toast.makeText(this, "The plant's watering schedule must be at least 1 day", Toast.LENGTH_SHORT).show();
            } else {
                EditText nameEditText = findViewById(R.id.new_plant_name);
                String name = nameEditText.getText().toString();

                EditText speciesEditText = findViewById(R.id.new_plant_species);
                String species = speciesEditText.getText().toString();

                Plant plant = new Plant(name, species, waterSchedule);

                File from = new File(dir, "tempd.jpg");

                if (from.canRead()) {
                    File to = new File(dir, name + species + ".jpg");
                    from.renameTo(to);

                    Uri picUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".com.arazzi.plantaria.provider", to);
                    plant.setPic(picUri.toString());
                    deleteTempPic();
                }

                Intent output = new Intent();
                output.putExtra(WaterReminder.EXTRA_PLANT, plant);
                setResult(RESULT_OK, output);
                finish();
            }
        }
    }

    public void capture(View view) {
        Uri outputFileUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".com.arazzi.plantaria.provider", createImageFile());

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        List<String> permissionList = PermissionsUtil.checkAndRequestPermissions(this);

        if (permissions(permissionList)) {
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            File file = new File(dir, "tempd.jpg");
            Uri outputFileUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".com.arazzi.plantaria.provider", file);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),outputFileUri);
                bitmap = rotateImageIfRequired(this, bitmap, outputFileUri);
                ImageView pic = findViewById(R.id.new_plant_pic);
                pic.setImageBitmap(bitmap);

                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } catch (IOException ignored) {

            }
        }
    }

    private File createImageFile(){
        File newFile = new File(dir, "tempd.jpg");
        try {
            newFile.createNewFile();
        } catch(IOException ignored){}

        return newFile;
    }

    private boolean permissions(List<String> listPermissionsNeeded) {
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[0]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {
        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private void deleteTempPic() {
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES + "/picFolder/");
        File file = new File(path, "tempd.jpg");
        file.delete();
    }
}
