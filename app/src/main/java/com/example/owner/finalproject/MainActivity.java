package com.example.owner.finalproject;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.pm.PackageInfo;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView pictureView;

    String f2_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton cameraButton = (ImageButton) findViewById(R.id.cameraButton);

        pictureView = (ImageView) findViewById(R.id.pictureView);

        //disable the button if the user has no camera

        if (!hasCamera())
            cameraButton.setEnabled(false);
    }

    //Check if the user has a camera
    private boolean hasCamera(){
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    //Launching the camera
    public void launchCamera(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Take a picture and pass results along to onActivityResult
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    //If you want to return the image taken
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            //Get the photo
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");     //photo taken by the user gets saved as a bitmap as a photo
            pictureView.setImageBitmap(photo);

            extractColor(photo, 1);
            //complementary(0xFF0000);
        }

    }

    public void onNextClicked(View view)
    {
        Intent i = new Intent(getApplicationContext(), Activity2.class);
        i.putExtra("f2_color", f2_color);
        startActivity(i);
    }

    public void analyze(View view)
    {

    }

    public int extractColor(Bitmap bitmap, int radius){
        // Gets center coordinates
        int cx = bitmap.getWidth() / 2;
        int cy = bitmap.getHeight() / 2;

        // if the radius is 1, return the color int of the center pixel

        if (radius <= 1) {
            int bitmapcolor = bitmap.getPixel(cx, cy);
            f2_color = Integer.toHexString(bitmapcolor).toUpperCase();
            f2_color = f2_color.replace("FF", "#");


            TextView color = findViewById(R.id.color);
            color.setText(f2_color);
            Log.d("Michael", "Color: " + f2_color);
            return bitmap.getPixel(cx, cy);
        }

        // Get the area of the reticle, no. of pixels to count is the area


        double area = Math.PI * radius * radius;
        int[][] rgb = new int[(int)Math.ceil(area)][3];

        // Loop through a square and add these specific pixels to rgb array
        int i = 0;
        for (int x = cx - radius; x < cx + radius && i < rgb.length; x++){
            for (int y = cy - radius; y < cy + radius && i < rgb.length; y++){
                if (Math.hypot(cx - x, cy - y) >= radius)
                    continue;
                int pixel = bitmap.getPixel(x,y);
                rgb[i][0] = Color.red(pixel);
                rgb[i][1] = Color.blue(pixel);
                rgb[i][2] = Color.green(pixel);
                i++;
            }
        }

        // Take the average of the colors in the array and return it
        int redAvg = 0;
        int blueAvg = 0;
        int greenAvg = 0;

        for (i = 0; i < rgb.length; i++){
            redAvg += rgb[i][0];
            blueAvg += rgb[i][1];
            greenAvg += rgb[i][2];
        }

        redAvg /= rgb.length;
        blueAvg /= rgb.length;
        greenAvg /= rgb.length;


        String f_color = String.format("#%02X%02X%02X", redAvg, blueAvg, greenAvg);

        TextView color = findViewById(R.id.color);
        color.setText (f_color);
        Log.d("Michael", "Color: " + f_color);

        return Color.rgb(redAvg, greenAvg, blueAvg);
    }


    private double[] RGBtoHSL(int rgb){

        double h = 0;
        double s = 0;
        double l = 0;
        double r = Color.red(rgb) / 255;
        double g = Color.green(rgb) / 255;
        double b = Color.blue(rgb) / 255;

        double min = Math.min(r, Math.min(g, b));
        double max = Math.max(r, Math.max(g, b));
        double delta = max - min;

        l = (max + min) / 2;

        if (delta == 0)
            h = s = 0;

        else {
            s = delta / (1 - Math.abs(2 * l - 1));

            if (max == r)
                h = ((g - b) / delta) % 6;
            else if (max == g)
                h = (b - r) / delta + 2;
            else if (max == b)
                h = (r - g) / delta + 4;
        }
        double[] hsl = {h * 60, s * 100, l * 100};
        return hsl;
    }

    private int HSLtoRGB(double[] hsl){
        double r = 0;
        double g = 0;
        double b = 0;
        double h = hsl[0];
        double s = hsl[1];
        double l = hsl[2];

        double delta = (1 - Math.abs(2 * l - 1)) * s;
        double x = delta * (1 - Math.abs((h / 60.0) % 2 - 1));
        double m = l - delta / 2.0;

        if (h < 60){
            r = delta;
            g = x;
            b = 0;
        }

        else if (h < 120){
            r = x;
            g = delta;
            b = 0;
        }

        else if (h < 180){
            r = 0;
            g = delta;
            b = x;
        }

        else if (h < 240){
            r = 0;
            g = x;
            b = delta;
        }

        else if (h < 300){
            r = x;
            g = 0;
            b = delta;
        }
        else {
            r = delta;
            g = 0;
            b = x;
        }

        r = Math.round((r + m) * 255);
        g = Math.round((g + m) * 255);
        b = Math.round((b + m) * 255);

       return Color.rgb((int)r,(int) g,(int)b);
}

    public int complementary(int rgb){
        double[] hsl = RGBtoHSL(rgb);
        hsl[0] = (hsl[0] + 180) % 360;

        TextView color = findViewById(R.id.color);
        color.setText (HSLtoRGB(hsl));

        return HSLtoRGB(hsl);
    }

}

/*
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private ImageButton takePictureButton;
    private ImageView imageView;
    private Uri file;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        takePictureButton = (ImageButton) findViewById(R.id.button_image);
        imageView = (ImageView) findViewById(R.id.imageview);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            takePictureButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                takePictureButton.setEnabled(true);
            }
        }
    }

    public void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

        startActivityForResult(intent, 100);
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                Log.d("CameraDemo", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }


    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                imageView.setImageURI(file);
            }
        }
    }

    public void onNextClicked(View view)
    {
        Intent i = new Intent(getApplicationContext(), Activity2.class);
        startActivity(i);
    }

}
*/