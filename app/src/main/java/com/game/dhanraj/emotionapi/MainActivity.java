package com.game.dhanraj.emotionapi;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.microsoft.projectoxford.emotion.EmotionServiceClient;
import com.microsoft.projectoxford.emotion.EmotionServiceRestClient;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public EmotionServiceClient emotionServiceClient = new EmotionServiceRestClient("fb771b3cda724259b6a0b208a0a2c191");
    public static final int GET_FROM_GALLERY = 3;
    public static final int TAKE_A_PHOTO=1;
    private Button getfromthegallery,takeaphoto;
    //private Button takeaphoto;
    public  static final int GET_FROM_GALLERY1=9;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       getfromthegallery = (Button) findViewById(R.id.getfromgallery);
        takeaphoto = (Button) findViewById(R.id.takeaphoto1);
        
        
        takeaphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPermission();
            }
        });

        getfromthegallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getpermission();
            }
        });

    }

    private void getPermission() {
        if ( ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA  )
                != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    TAKE_A_PHOTO);
        }
        else
        {
            takeAPhoto();
        }
    }

    private void takeAPhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,5);
    }

    private void getpermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    GET_FROM_GALLERY);
        }
        // Should we show an explanation?
        else {
            getFromGallery();
        }

    }

   private void getFromGallery() {
        // startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI),GET_FROM_GALLERY);
        Intent gallIntent = new Intent(Intent.ACTION_GET_CONTENT);
        gallIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(gallIntent, "Select Picture"), GET_FROM_GALLERY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case GET_FROM_GALLERY: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getFromGallery();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {

                        new AlertDialog.Builder(this).
                                setTitle("External Storage Permission").
                                setMessage("you need to grant this permission in order to select images from the gallery");
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    }else
                    {
                        new AlertDialog.Builder(this)
                                .setTitle("External Storage Permission")
                                .setMessage("you have denied this permission.Now you have to go to settings to enable it");
                    }
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case TAKE_A_PHOTO:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takeAPhoto();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.CAMERA)) {

                        new AlertDialog.Builder(this).
                                setTitle("Camera Permission").
                                setMessage("you need to grant this permission in order to select images from the gallery");
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    }else
                    {
                        new AlertDialog.Builder(this)
                                .setTitle("External Storage Permission")
                                .setMessage("you have denied this permission.Now you have to go to settings to enable it");
                    }
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap b=null;
        Intent n = new Intent(MainActivity.this,DisplayResultActivity.class);
        if (requestCode == 5) {
            if (resultCode == Activity.RESULT_OK ) {

                 b = data.getParcelableExtra("data");
                n.putExtra("imageformcamera",b);
                startActivity(n);
            }
        }

        else if(requestCode == GET_FROM_GALLERY && data != null && data.getData() != null && resultCode == Activity.RESULT_OK)
            {
                Uri selectedimage=data.getData();
                try{
                    b=android.provider.MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedimage);
                   // detectAndFrame(b);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                n.putExtra("imageformcamera",b);
                startActivity(n);

        }



    }

}