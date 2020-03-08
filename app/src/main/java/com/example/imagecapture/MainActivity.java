package com.example.imagecapture;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class MainActivity extends AppCompatActivity {

    Button btnImage;
    ImageView viewVideo;
    String pathToFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnImage = findViewById(R.id.btnRecord);
        viewVideo = findViewById(R.id.imageView);



        if(Build.VERSION.SDK_INT >= 23){
            requestPermissions( new String[ ]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},2 );
        }
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   clickImage();

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){

            if(requestCode == 1){
                Bitmap bitmap = BitmapFactory.decodeFile(pathToFile);
                viewVideo.setImageBitmap(bitmap);

            }
        }
    }

    public void clickImage(){

        Intent takeImage = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if(takeImage.resolveActivity(getPackageManager()) != null){
            File imageFile = null;
            imageFile = createImageFile();


            if( imageFile != null )
            {
                pathToFile = imageFile.getAbsolutePath();

                Uri imageURI = FileProvider.getUriForFile(MainActivity.this,"com.example.imagecapture.fileprovider" , imageFile);
                takeImage.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
                takeImage.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 8);
                startActivityForResult(takeImage,1);

            }
        }

    }

    public File createImageFile()
    {

        //String name = new SimpleDateFormat("yearMMDD_HHmmss").format(new Date());
        String name = "Test" + new SimpleDateFormat("yyyy.MM.dd EEEE hh.mm.ss").format(new Date());
        File storeDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(name,".mp4",storeDir);
        } catch (IOException e) {
            Log.d("myLog","Exception" + e.toString());

        }

        return image;
    }
}
