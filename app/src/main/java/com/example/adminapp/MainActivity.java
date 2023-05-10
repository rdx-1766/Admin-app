package com.example.adminapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.adminapp.faculty.UpdateFaculty;

public class MainActivity extends AppCompatActivity {

    CardView uploadNotice,addGalleryImage,addEbook,addFaculty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addGalleryImage = findViewById(R.id.addGalleryImage);
        uploadNotice = findViewById(R.id.addNotice);
        addEbook = findViewById(R.id.addEbook);
        addFaculty = findViewById(R.id.addFaculty);

        uploadNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, uploadNotice.class);
                startActivity(intent);
            }
        });
        addGalleryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UploadImage.class);
                startActivity(intent);
            }
        });

        addEbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UploadPdf.class);
                startActivity(intent);
            }
        });

        addFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UpdateFaculty.class);
                startActivity(intent);
            }
        });
    }


}