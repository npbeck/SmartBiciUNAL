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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements OnCompleteListener<QuerySnapshot> {

    EditText usernameEditText;
    EditText passwordEditText;
    Button loginButton;
    ProgressBar progressBar;
    FirebaseFirestore db;

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
                // query for the user's data in the database
                db.collection("users")
                        .whereEqualTo("name", usernameEditText.getText().toString())
                        .get()
                        .addOnCompleteListener(LoginActivity.this);
            }
        });

        // fetch static bike park data
        SmartBiciConstants.fetchStaticLocationsData();
    }

    /*
    perform this method when presented with the user's data
     */
    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        progressBar.setVisibility(View.INVISIBLE);
        if (task.isSuccessful()){

            List<DocumentSnapshot> docs = Objects.requireNonNull(task.getResult()).getDocuments();
            if (docs.size() > 0){
                DocumentSnapshot user = docs.get(0);

                // check that password is correct
                if (! user.getString("password").equals(passwordEditText.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Credenciales erróneos", Toast.LENGTH_LONG).show();
                    return;
                }

                // set user's bike ID reference
                SmartBiciConstants.userBikeReferenceInDatabase = (DocumentReference) user.get("bike");

                Toast.makeText(getApplicationContext(), "Bienvenid@, " + user.getString("name") + " !", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, IdleActivity.class));
                return;
            }
        }

        Toast.makeText(getApplicationContext(), "No pudimos verificar sus credenciales", Toast.LENGTH_LONG).show();
    }
}
