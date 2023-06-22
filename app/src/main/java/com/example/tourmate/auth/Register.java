package com.example.tourmate.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tourmate.R;
import com.example.tourmate.model.User;
import com.example.tourmate.navbar.NavbarActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    FirebaseAuth auth;

    EditText FirstName, LastName, Email, Password, ConfirmPassword, Phone, BirthDate;

    Spinner Gender;

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
        Gender = findViewById(R.id.spinnerGender);
        BirthDate = findViewById(R.id.inputBirthDate);
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
                                if (task.isSuccessful()) {
                                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
                                    String userId = firebaseUser.getUid();

                                    User user = new User();
                                    user.setFirstName(FirstName.getText().toString());
                                    user.setLastName(LastName.getText().toString());
                                    user.setEmail(Email.getText().toString());
                                    user.setPhone(Phone.getText().toString());
                                    user.setGender(Gender.getSelectedItem().toString());
                                    user.setBirthDate(BirthDate.getText().toString());

                                    Log.d("RegisterActivity", "Gender: " + Gender.getSelectedItem().toString());
                                    Log.d("RegisterActivity", "Birth Date: " + BirthDate.getText().toString());

                                    userRef.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                reload();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Gagal menyimpan data pengguna ke database", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(getApplicationContext(), "Gagal memperbarui profil pengguna", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Gagal Registrasi", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void reload(){
        startActivity(new Intent(getApplicationContext(), NavbarActivity.class));
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