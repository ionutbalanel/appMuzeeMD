package com.ltstefanesti.muzeemd.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ltstefanesti.muzeemd.R;
import com.ltstefanesti.muzeemd.models.MuseumDataClass;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UploadActivity extends AppCompatActivity {
    EditText name;
    EditText imageOne;
    EditText imageTwo;
    EditText imageThree;
    EditText address;
    EditText phoneNumber;
    EditText website;
    EditText email;
    EditText hoursOfOperation;
    EditText description;
    EditText latitude;
    EditText longitude;
    Button saveButton;

    @SuppressLint({"MissingInflatedId", "SourceLockedOrientationActivity"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setUpViews();
        saveButton.setOnClickListener(view -> saveData());
    }

    private void saveData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();
        uploadData();
        dialog.dismiss();
    }

    private void uploadData() {
        String name = this.name.getText().toString();
        String imageOne = this.imageOne.getText().toString();
        String imageTwo = this.imageTwo.getText().toString();
        String imageThree = this.imageThree.getText().toString();
        String address = this.address.getText().toString();
        String phoneNumber = this.phoneNumber.getText().toString();
        String website = this.website.getText().toString();
        String email = this.email.getText().toString();
        String hoursOfOperation = this.hoursOfOperation.getText().toString();
        String description = this.description.getText().toString();
        String latitude = this.latitude.getText().toString();
        String longitude = this.longitude.getText().toString();
        MuseumDataClass museumDataClass = new MuseumDataClass(name, imageOne, imageTwo, imageThree,
                address, phoneNumber, website, email, hoursOfOperation, description, latitude, longitude);
        // Formatul de data si ora compatibil cu Firebase Realtime Database
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        // Obține o referință la baza de date Firebase Realtime Database și stabilește nodul copil
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("lista_muzee").child(currentDate);
        // Adauga datele in baza de date si afiseaza un mesaj de confirmare in cazul in care incarcarea a fost efectuata cu succes
        databaseRef.setValue(museumDataClass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(UploadActivity.this, "Sa salvat!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(e -> Toast.makeText(UploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void setUpViews() {
        name = findViewById(R.id.name);
        imageOne = findViewById(R.id.image_one);
        imageTwo = findViewById(R.id.image_two);
        imageThree = findViewById(R.id.image_three);
        address = findViewById(R.id.address);
        phoneNumber = findViewById(R.id.phone_number);
        website = findViewById(R.id.website);
        email = findViewById(R.id.email);
        hoursOfOperation = findViewById(R.id.hours_of_operation);
        description = findViewById(R.id.description);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        saveButton = findViewById(R.id.save_button);
    }
}