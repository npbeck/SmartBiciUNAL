package com.example.smartbiciunal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements OnCompleteListener<QuerySnapshot> {

    EditText usernameEditText;
    EditText passwordEditText;
    Button loginButton;
    ProgressBar progressBar;
    FirebaseFirestore db;

    // this string stores the user ID, so that other activities can use it to query the database for info
    protected static String userIdInDatabase = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        progressBar = findViewById(R.id.loading);

        // connect to database
        db = FirebaseFirestore.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                // TODO check how query must be re-written for it to work (connection works)
                db.collection("users")
                        //.whereEqualTo("username", usernameEditText.getText().toString())
                        //.whereEqualTo("password", passwordEditText.getText().toString())
                        .get()
                        .addOnCompleteListener(LoginActivity.this);
            }
        });
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        progressBar.setVisibility(View.INVISIBLE);
        if (task.isSuccessful()){
            List<DocumentSnapshot> docs = Objects.requireNonNull(task.getResult()).getDocuments();
            if (docs.size() > 0){
                userIdInDatabase = docs.get(0).getId();
                Toast.makeText(getApplicationContext(), "Bienvenid@, " + userIdInDatabase + " !", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, IdleActivity.class));
                return;
            }
        }

        Toast.makeText(getApplicationContext(), "No pudimos verificar sus credenciales", Toast.LENGTH_LONG).show();
    }
}
