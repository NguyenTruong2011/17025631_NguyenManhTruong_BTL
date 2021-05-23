package com.example.loginfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class FaceComment extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private ImageView face_smile_touch, face_bored_touch, face_angry_touch;

    private int face_smile_count, face_bored_count, face_angry_count = 0;

    private Button btn_finish;
    private String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_comment);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        face_smile_touch = findViewById(R.id.face_smile);
        face_bored_touch = findViewById(R.id.face_bored);
        face_angry_touch = findViewById(R.id.face_angry);

        btn_finish = findViewById(R.id.finish);

        DocumentReference docRef = db.collection("users").document(mAuth.getCurrentUser().getUid());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        face_angry_count = document.getLong("angry").intValue();
                        face_bored_count = document.getLong("bored").intValue();
                        face_smile_count = document.getLong("smile").intValue();
                        name = document.getString("name");
                    } else {
                        Log.d("LOGGER", "No such document");
                    }
                } else {
                    Log.d("LOGGER", "get failed with ", task.getException());
                }
            }
        });


        face_bored_touch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                face_bored_count = face_bored_count + 1;
                Toast.makeText(getApplicationContext(), "Bored: " + face_bored_count, Toast.LENGTH_LONG).show();
            }
        });

        face_angry_touch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                face_angry_count = face_angry_count + 1;
                Toast.makeText(getApplicationContext(), "Angry: " + face_angry_count, Toast.LENGTH_LONG).show();
            }
        });

        face_smile_touch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                face_smile_count = face_smile_count + 1;
                Toast.makeText(getApplicationContext(), "Smile: " + face_smile_count, Toast.LENGTH_LONG).show();
            }
        });

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                docRef.update("smile", new Long(face_smile_count), "angry", new Long(face_angry_count), "bored", new Long(face_bored_count)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Success!" + new Long(face_smile_count) + "::" + face_bored_count + "::" + face_smile_count + "::" + mAuth.getCurrentUser().getUid(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Err: " + e, Toast.LENGTH_LONG).show();

                    }
                });
            }
        });

    }

}