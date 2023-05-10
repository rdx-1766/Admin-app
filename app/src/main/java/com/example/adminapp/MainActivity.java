package com.example.adminapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements onClick {

    CardView uploadNotice,addGalleryImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addGalleryImage = findViewById(R.id.addGalleryImage);
        uploadNotice = findViewById(R.id.addNotice);

        uploadNotice.setOnClickListener((View.OnClickListener) this);
        addGalleryImage.setOnClickListener((View.OnClickListener) this);
    }
    @Override
    public void onClick(View view) {
        Intent intent;
        if (view.getId() == R.id.addNotice) {
            intent = new Intent(MainActivity.this, uploadNotice.class);
            startActivity(intent);
        }
        else if (view.getId() == R.id.addGalleryImage) {
            intent = new Intent(MainActivity.this, UploadImage.class);
            startActivity(intent);
        }
    }



}