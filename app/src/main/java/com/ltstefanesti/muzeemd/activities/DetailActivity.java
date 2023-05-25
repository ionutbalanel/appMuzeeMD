package com.ltstefanesti.muzeemd.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ltstefanesti.muzeemd.R;
import com.ltstefanesti.muzeemd.adapters.CommentAdapter;
import com.ltstefanesti.muzeemd.models.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DetailActivity extends AppCompatActivity {
    TextView tvNameMuseum;
    TextView tvAddress;
    TextView tvPhoneNumber;
    TextView tvWebsite;
    TextView tvEmail;
    TextView tvHoursOfOperation;
    TextView tvDescription;
    ImageSlider imageSlider;
    ImageButton buttonHome;
    ImageButton buttonSearch;
    ImageButton buttonMap;
    ImageButton buttonEdit;
    ImageButton buttonDelete;
    String key = "";
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String currentUserUID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
    String[] validUIDs = {"DDLr5dpMsybXMIPbrM16BL0iLml1", "lIO3OqazCQbIhilaz46RGcrL0PM2", "IrDYQlmu5sNSHsIPy8n6puspOXA3"};
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    static String COMMENT_KEY = "Comment";
    ImageView imgCurrentUser;
    EditText editTextComment;
    Button btnAddComment;
    RecyclerView rvComment;
    CommentAdapter commentAdapter;
    List<Comment> listComment;
    TextView likeButton;
    DatabaseReference likesReference;
    Boolean likeChecker = false;
    int likesCount;

    @SuppressLint({"SetTextI18n", "SourceLockedOrientationActivity"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setUpViews();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            key = bundle.getString("Key");
            tvNameMuseum.setText(bundle.getString("Title"));
            ArrayList<SlideModel> slideModels = new ArrayList<>();
            if (bundle.getString("Image") != null && !bundle.getString("Image").isEmpty()) {
                slideModels.add(new SlideModel(bundle.getString("Image"), ScaleTypes.CENTER_CROP));
            }
            if (bundle.getString("Image1") != null && !bundle.getString("Image1").isEmpty()) {
                slideModels.add(new SlideModel(bundle.getString("Image1"), ScaleTypes.CENTER_CROP));
            }
            if (bundle.getString("Image2") != null && !bundle.getString("Image2").isEmpty()) {
                slideModels.add(new SlideModel(bundle.getString("Image2"), ScaleTypes.CENTER_CROP));
            }
            imageSlider.setImageList(slideModels, ScaleTypes.CENTER_CROP);
            tvAddress.setText(bundle.getString("Address"));
            tvPhoneNumber.setText(bundle.getString("Phone"));
            tvWebsite.setText(bundle.getString("Web"));
            tvEmail.setText(bundle.getString("Email"));
            tvHoursOfOperation.setText(bundle.getString("Program"));
            tvDescription.setText(bundle.getString("Description"));
        }
        tvAddress.setOnClickListener(view -> {
            assert bundle != null;
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("http://maps.google.com/maps?saddr="
                    + "&daddr=" + bundle.getString("Latitude") + "," + bundle.getString("Longitude") + "&z=14&t=m"));
            startActivity(intent);
        });
        Glide.with(this).load(firebaseUser.getPhotoUrl()).into(imgCurrentUser);
        btnAddComment.setOnClickListener(view -> {
            btnAddComment.setVisibility(View.INVISIBLE);
            DatabaseReference commentReference = firebaseDatabase.getReference(COMMENT_KEY).child(key).push();
            String comment_content = editTextComment.getText().toString();
            String uid = firebaseUser.getUid();
            String uname = firebaseUser.getDisplayName();
            String uimg = Objects.requireNonNull(firebaseUser.getPhotoUrl()).toString();
            Comment comment = new Comment(comment_content, uid, uimg, uname);
            commentReference.setValue(comment).addOnSuccessListener(aVoid -> {
                showMessage("comentariu adaugat");
                editTextComment.setText("");
                btnAddComment.setVisibility(View.VISIBLE);
            }).addOnFailureListener(e -> showMessage("nu s-a putut adauga comentariul : " + e.getMessage()));
        });
        firebaseDatabase = FirebaseDatabase.getInstance();
        likesReference = firebaseDatabase.getReference("likes");
        likesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(key).hasChild(currentUserUID)) {
                    likesCount = (int) snapshot.child(key).getChildrenCount();
                    likeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);
                    likeButton.setText(likesCount + "");
                } else {
                    likesCount = (int) snapshot.child(key).getChildrenCount();
                    likeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dislike, 0, 0, 0);
                    likeButton.setText(likesCount + "");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        likeButton.setOnClickListener(view -> {
            likeChecker = true;
            likesReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (likeChecker.equals(true)) {
                        if (snapshot.child(key).hasChild(currentUserUID)) {
                            likesReference.child(key).child(currentUserUID).removeValue();
                        } else {
                            likesReference.child(key).child(currentUserUID).setValue(true);
                        }
                        likeChecker = false;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        });
        buttonHome.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            // Obținerea serviciului GoogleSignInClient
            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build());
            // Revocarea accesului pentru fiecare cont Google
            Task<Void> task = googleSignInClient.revokeAccess();
            task.addOnCompleteListener(this, task1 -> {
                // Eliminarea tuturor conturilor Google de pe dispozitiv
                try {
                    GoogleAuthUtil.clearToken(getApplicationContext(), GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
                    Toast.makeText(getApplicationContext(), "All Google accounts have been removed from the device.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        });
        buttonSearch.setOnClickListener(view -> {
            Intent intent = new Intent(this, MuseumsListActivity.class);
            startActivity(intent);
            finish();
        });
        buttonMap.setOnClickListener(view -> {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        });
        // Verificați dacă User UID-ul utilizatorului curent este valid și dacă are acces la buton
        for (String validUID : validUIDs) {
            if (currentUserUID.equals(validUID)) {
                buttonEdit.setVisibility(View.VISIBLE);
                buttonEdit.setOnClickListener(view -> {
                    ArrayList<SlideModel> slideModels = new ArrayList<>();
                    assert bundle != null;
                    if (bundle.getString("Image") != null && !bundle.getString("Image").isEmpty()) {
                        slideModels.add(new SlideModel(bundle.getString("Image"), ScaleTypes.CENTER_CROP));
                    }
                    if (bundle.getString("Image1") != null && !bundle.getString("Image1").isEmpty()) {
                        slideModels.add(new SlideModel(bundle.getString("Image1"), ScaleTypes.CENTER_CROP));
                    }
                    if (bundle.getString("Image2") != null && !bundle.getString("Image2").isEmpty()) {
                        slideModels.add(new SlideModel(bundle.getString("Image2"), ScaleTypes.CENTER_CROP));
                    }
                    if (slideModels.size() > 0) {
                        imageSlider.setImageList(slideModels, ScaleTypes.CENTER_CROP);
                    }
                    Intent intent = new Intent(DetailActivity.this, UpdateActivity.class)
                            .putExtra("Title", tvNameMuseum.getText().toString());
                    if (slideModels.size() > 0) {
                        SlideModel slideModel = slideModels.get(0);
                        intent.putExtra("Image", slideModel.getImageUrl());
                        if (slideModels.size() > 1) {
                            SlideModel slideModel1 = slideModels.get(1);
                            intent.putExtra("Image1", slideModel1.getImageUrl());
                        }
                        if (slideModels.size() > 2) {
                            SlideModel slideModel2 = slideModels.get(2);
                            intent.putExtra("Image2", slideModel2.getImageUrl());
                        }
                    }
                    intent.putExtra("Address", tvAddress.getText().toString())
                            .putExtra("Phone", tvPhoneNumber.getText().toString())
                            .putExtra("Web", tvWebsite.getText().toString())
                            .putExtra("Email", tvEmail.getText().toString())
                            .putExtra("Program", tvHoursOfOperation.getText().toString())
                            .putExtra("Description", tvDescription.getText().toString())
                            .putExtra("Latitude", bundle.getString("Latitude"))
                            .putExtra("Longitude", bundle.getString("Longitude"))
                            .putExtra("Key", key);
                    startActivity(intent);
                });
                buttonDelete.setVisibility(View.VISIBLE);
                buttonDelete.setOnClickListener(view -> {
                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("lista_muzee");
                    reference.child(key).removeValue();
                    Toast.makeText(DetailActivity.this, "Sa șters", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MuseumsListActivity.class));
                    finish();
                });
                break;
            } else {
                buttonEdit.setVisibility(View.GONE);
                buttonDelete.setVisibility(View.GONE);
            }
        }
        // ini Recyclerview Comment
        iniRvComment();
    }

    private void iniRvComment() {
        rvComment.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference commentRef = firebaseDatabase.getReference(COMMENT_KEY).child(key);
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listComment = new ArrayList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Comment comment = snap.getValue(Comment.class);
                    assert comment != null;
                    comment.setId(snap.getKey());
                    listComment.add(comment);
                }
                commentAdapter = new CommentAdapter(getApplicationContext(), listComment);
                rvComment.setAdapter(commentAdapter);
                int itemCount = commentAdapter.getItemCount();
                TextView tvItemCount = findViewById(R.id.tv_item_count);
                tvItemCount.setText(String.valueOf(itemCount));
                commentAdapter.setOnItemClickListener(position -> {
                    Comment comment = listComment.get(position);
                    // verificăm dacă utilizatorul curent este autorul comentariului
                    if (comment.getuId().equals(currentUserUID)) {
                        // ștergem comentariul
                        DatabaseReference commentRef = firebaseDatabase.getReference(COMMENT_KEY).child(key).child(comment.getId());
                        commentRef.removeValue().addOnSuccessListener(aVoid -> showMessage("Comentariu șters cu succes"))
                                .addOnFailureListener(e -> showMessage("Eroare la ștergerea comentariului: " + e.getMessage()));
                    } else {
                        showMessage("Nu aveți permisiunea de a șterge acest comentariu");
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void setUpViews() {
        tvNameMuseum = findViewById(R.id.name);
        tvAddress = findViewById(R.id.address);
        tvPhoneNumber = findViewById(R.id.phone_number);
        tvWebsite = findViewById(R.id.website);
        tvEmail = findViewById(R.id.email);
        tvHoursOfOperation = findViewById(R.id.hours_of_operation);
        tvDescription = findViewById(R.id.description);
        imageSlider = findViewById(R.id.slider);
        buttonHome = findViewById(R.id.menu_home);
        buttonSearch = findViewById(R.id.menu_search);
        buttonMap = findViewById(R.id.menu_map);
        buttonEdit = findViewById(R.id.menu_edit);
        buttonDelete = findViewById(R.id.menu_delete);
        imgCurrentUser = findViewById(R.id.post_detail_currentuser_img);
        editTextComment = findViewById(R.id.post_detail_comment);
        btnAddComment = findViewById(R.id.post_detail_add_comment_btn);
        rvComment = findViewById(R.id.rv_comment);
        likeButton = findViewById(R.id.like);
    }
}