package com.ltstefanesti.muzeemd.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ltstefanesti.muzeemd.R;
import com.ltstefanesti.muzeemd.adapters.MuseumAdapter;
import com.ltstefanesti.muzeemd.models.MuseumDataClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MuseumsListActivity extends AppCompatActivity implements MuseumAdapter.OnItemClickListener {
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    RecyclerView recyclerView;
    List<MuseumDataClass> dataList;
    MuseumAdapter adapter;
    SearchView searchView;
    ImageButton buttonHome;
    ImageButton buttonAdd;
    ImageButton buttonMap;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String currentUserUID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
    String[] validUIDs = {"DDLr5dpMsybXMIPbrM16BL0iLml1", "lIO3OqazCQbIhilaz46RGcrL0PM2", "IrDYQlmu5sNSHsIPy8n6puspOXA3"};

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museums_list);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setUpViews();
        searchView.clearFocus();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();
        dataList = new ArrayList<>();
        adapter = new MuseumAdapter(this, dataList);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("lista_muzee");
        dialog.show();
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    MuseumDataClass museumDataClass = itemSnapshot.getValue(MuseumDataClass.class);
                    assert museumDataClass != null;
                    museumDataClass.setKey(itemSnapshot.getKey());
                    dataList.add(museumDataClass);
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });
        buttonHome.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            // Obținerea serviciului GoogleSignInClient
            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, new GoogleSignInOptions.
                    Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build());
            // Revocarea accesului pentru fiecare cont Google
            Task<Void> task = googleSignInClient.revokeAccess();
            task.addOnCompleteListener(this, task1 -> {
                // Eliminarea tuturor conturilor Google de pe dispozitiv
                try {
                    GoogleAuthUtil.clearToken(getApplicationContext(), GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
                    Toast.makeText(getApplicationContext(), "All Google accounts have been removed from the device.",
                            Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        });
        buttonAdd.setOnClickListener(view -> {
            // Verificați dacă User UID-ul utilizatorului curent este valid și dacă are acces la buton
            for (String validUID : validUIDs) {
                if (currentUserUID.equals(validUID)) {
                    Intent intent = new Intent(this, UploadActivity.class);
                    startActivity(intent);
                    break;
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://forms.gle/8pTZKZ49AVSK9HjS9"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
        buttonMap.setOnClickListener(view -> {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        });
    }

    public void searchList(String text) {
        ArrayList<MuseumDataClass> searchList = new ArrayList<>();
        for (MuseumDataClass museumDataClass : dataList) {
            if (museumDataClass.getName().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(museumDataClass);
            }
        }
        adapter.searchDataList(searchList);
    }

    @Override
    public void onItemClick(int position) {
        finish();
    }

    private void setUpViews() {
        searchView = findViewById(R.id.search_view);
        recyclerView = findViewById(R.id.recycler_view);
        buttonHome = findViewById(R.id.menu_home);
        buttonAdd = findViewById(R.id.menu_add);
        buttonMap = findViewById(R.id.menu_map);
    }
}