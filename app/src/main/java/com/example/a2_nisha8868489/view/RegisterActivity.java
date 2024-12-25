package com.example.a2_nisha8868489.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a2_nisha8868489.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private EditText registerEmail, registerPassword;
    private Button registerButton;
    private TextView loginRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Bind Views
        registerEmail = findViewById(R.id.register_userName); // Email field
        registerPassword = findViewById(R.id.register_password); // Password field
        registerButton = findViewById(R.id.register_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        // Register Button Click Listener
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        // Redirect to Login Activity
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void registerUser() {
        String email = registerEmail.getText().toString().trim();
        String password = registerPassword.getText().toString().trim();

        // Input Validation
        if (email.isEmpty()) {
            registerEmail.setError("Email cannot be empty");
            registerEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            registerPassword.setError("Password cannot be empty");
            registerPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            registerPassword.setError("Password must be at least 6 characters");
            registerPassword.requestFocus();
            return;
        }


        // Firebase Email/Password Registration
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Save additional user data to Firestore
                            saveUserToFirestore(email);
                        } else {
                            // Display error message
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Registration Failed";
                            Toast.makeText(RegisterActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveUserToFirestore(String email) {
        String userId = auth.getCurrentUser().getUid();

        // Create a user map to store data in Firestore
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("email", email);

        // Save to Firestore under a "users" collection
        db.collection("users").document(userId)
                .set(userMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            // Redirect to Login or another activity
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish(); // Prevent returning to this activity
                        } else {
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Failed to save user data";
                            Toast.makeText(RegisterActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
