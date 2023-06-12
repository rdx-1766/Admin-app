package com.example.adminapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;

public class UploadProject extends AppCompatActivity {

    private EditText projectTitle,projectDesc,projectDeveloper,projectLink,projectFuture,projectFeatures,projectDemoLink,projectLang,problemStm;
    private Button uploadProjectBtn;
    private DatabaseReference databaseReference;
    private ImageView pdfImg;
    private TextView projectTextView;
    private StorageReference storageReference;

    private Spinner projectCategory;
    private String title,desc,link,developer,pdfUrl,pdfName,category,features,future,lang,demoLink,ps;
    private ProgressDialog pd;

    private final int REQ = 1;
    private Uri pdfData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_project);
        getWindow().setStatusBarColor(ContextCompat.getColor(UploadProject.this,R.color.darkblue));

        projectTitle = findViewById(R.id.projectTitle);
        projectDesc = findViewById(R.id.projectDesc);
        projectDeveloper = findViewById(R.id.projectDeveloper);
        projectLink = findViewById(R.id.projectLink);
        projectCategory = findViewById(R.id.projectCategory);
        projectFeatures = findViewById(R.id.projectFeatures);
        projectFuture = findViewById(R.id.projectFuture);
        projectDemoLink = findViewById(R.id.projectDemoLink);
        problemStm = findViewById(R.id.problemStm);
        projectLang = findViewById(R.id.projectLang);

        uploadProjectBtn = findViewById(R.id.uploadProjectBtn);
        pdfImg = findViewById(R.id.addPdf);
        projectTextView = findViewById(R.id.pdfTextView);

        pd = new ProgressDialog(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        String [] items = new String[]{"Android Development","Full-Stack Development","Data Science","Machine Learning","IOT","AR-VR","Artificial Intelligence"};
        projectCategory.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,items));
        projectCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = projectCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        pdfImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        uploadProjectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = projectTitle.getText().toString();
                desc = projectDesc.getText().toString();
                link = projectLink.getText().toString();
                developer = projectDeveloper.getText().toString();
                demoLink = projectDemoLink.getText().toString();
                features = projectFeatures.getText().toString();
                future = projectFuture.getText().toString();
                lang = projectLang.getText().toString();
                ps = problemStm.getText().toString();

                if(title.isEmpty()){
                    projectTitle.setError("Empty");
                    projectTitle.requestFocus();
                }
                else if(desc.isEmpty()){
                    projectDesc.setError("Empty");
                    projectDesc.requestFocus();
                }
                else if(developer.isEmpty()){
                    projectDeveloper.setError("Empty");
                    projectDeveloper.requestFocus();
                }
                else if(link.isEmpty()){
                    projectLink.setError("Empty");
                    projectLink.requestFocus();
                }
                else if(pdfData == null){
                    Toast.makeText(UploadProject.this, "Please Upload Pdf", Toast.LENGTH_SHORT).show();
                }
                else if(ps.isEmpty()){
                    problemStm.setError("Empty");
                    problemStm.requestFocus();
                }
                else if(features.isEmpty()){
                    projectFeatures.setError("Empty");
                    projectFeatures.requestFocus();
                }
                else if(lang.isEmpty()){
                    projectLang.setError("Empty");
                    projectLang.requestFocus();
                }
                else{
                    uploadPdf();
                }
            }
        });


    }

    private void uploadPdf() {
        pd.setTitle("Please wait...");
        pd.setMessage("Uploading...");
        pd.show();
        StorageReference reference = storageReference.child("reports/"+pdfName+"-"+System.currentTimeMillis()+".pdf");
        reference.putFile(pdfData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isComplete());
                Uri uri = uriTask.getResult();
                uploadData(String.valueOf(uri));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UploadProject.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadData(String valueOf) {
        String uniqueKey = databaseReference.child("Projects").push().getKey();
        HashMap data = new HashMap();

        data.put("ptitle",title);
        data.put("pdesc",desc);
        data.put("pdeveloper",developer);
        data.put("plink",link);
        data.put("pdfUrl",valueOf);
        data.put("ps",ps);
        data.put("demo",demoLink);
        data.put("features",features);
        data.put("future",future);
        data.put("lang",lang);

        databaseReference.child("Projects").child(category).child(title).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                Toast.makeText(UploadProject.this, "Project uploaded successfully", Toast.LENGTH_SHORT).show();
                projectTitle.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UploadProject.this, "Failed to upload pdf", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery(){
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Pdf File"),REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK) {
            pdfData = data.getData();

            if(pdfData.toString().startsWith("content://")){
                Cursor cursor = null;
                try {
                    cursor = UploadProject.this.getContentResolver().query(pdfData,null,null,null,null);

                    if(cursor != null && cursor.moveToFirst()){
                        pdfName = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            else if (pdfData.toString().startsWith("file://")) {
                pdfName = new File(pdfData.toString()).getName();
            }
            projectTextView.setText(pdfName);
        }
    }
}