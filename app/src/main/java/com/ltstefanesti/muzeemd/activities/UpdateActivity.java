package com.ltstefanesti.muzeemd.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
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

public class UpdateActivity extends AppCompatActivity {
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
    Button updateButton;
    String key;
    DatabaseReference databaseReference;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setUpViews();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            key = bundle.getString("Key");
            name.setText(bundle.getString("Title"));
            imageOne.setText(bundle.getString("Image"));
            imageTwo.setText(bundle.getString("Image1"));
            imageThree.setText(bundle.getString("Image2"));
            address.setText(bundle.getString("Address"));
            phoneNumber.setText(bundle.getString("Phone"));
            website.setText(bundle.getString("Web"));
            email.setText(bundle.getString("Email"));
            hoursOfOperation.setText(bundle.getString("Program"));
            description.setText(bundle.getString("Description"));
            latitude.setText(bundle.getString("Latitude"));
            longitude.setText(bundle.getString("Longitude"));
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("lista_muzee").child(key);
        updateButton.setOnClickListener(view -> {
            saveData();
            Intent intent = new Intent(UpdateActivity.this, MuseumsListActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void saveData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();
        updateData();
        dialog.dismiss();
    }

    private void updateData() {
        String name = this.name.getText().toString().trim();
        String imageOne = this.imageOne.getText().toString().trim();
        String imageTwo = this.imageTwo.getText().toString().trim();
        String imageThree = this.imageThree.getText().toString().trim();
        String address = this.address.getText().toString().trim();
        String phoneNumber = this.phoneNumber.getText().toString().trim();
        String website = this.website.getText().toString().trim();
        String email = this.email.getText().toString().trim();
        String hoursOfOperation = this.hoursOfOperation.getText().toString().trim();
        String description = this.description.getText().toString().trim();
        String latitude = this.latitude.getText().toString().trim();
        String longitude = this.longitude.getText().toString().trim();
        MuseumDataClass museumDataClass = new MuseumDataClass(name,
                imageOne, imageTwo, imageThree,
                address, phoneNumber, website,
                email, hoursOfOperation, description,
                latitude, longitude
        );
        databaseReference.setValue(museumDataClass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                Toast.makeText(UpdateActivity.this, "Sa actualizat!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(e -> Toast.makeText(UpdateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
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
        updateButton = findViewById(R.id.update_button);
    }
}