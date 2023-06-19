package com.example.tourmate.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tourmate.R;
import com.example.tourmate.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Register extends AppCompatActivity {

    FirebaseAuth auth;

    EditText FirstName, LastName, Email, Password, ConfirmPassword, Phone;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        FirstName = findViewById(R.id.inputFirstname);
        LastName = findViewById(R.id.inputLastName);
        Email = findViewById(R.id.inputEmail);
        Password = findViewById(R.id.inputPassword);
        ConfirmPassword = findViewById(R.id.inputConfirmPassword);
        Phone = findViewById(R.id.inputPhone);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v ->{
            if(FirstName.getText().length() > 0 && Email.getText().length()>0 && Password.getText().length()>0){
                register(Email.getText().toString(), Password.getText().toString());
            }else{
                Toast.makeText(getApplicationContext(), "Data harus diisi", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void register(String email, String password){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful() && task.getResult()!=null){
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    if(firebaseUser!=null) {
                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                .setDisplayName(FirstName.getText().toString())
                                .build();
                        firebaseUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                reload();
                            }
                        });
                    }else{
                        Toast.makeText(getApplicationContext(), "Gagal Registrasi", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void reload(){
        startActivity(new Intent(getApplicationContext(), HomeFragment.class));
    }

    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }
}