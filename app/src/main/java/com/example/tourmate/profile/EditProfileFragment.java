package com.example.tourmate.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourmate.R;
import com.example.tourmate.databinding.FragmentEditProfileBinding;
import com.example.tourmate.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class EditProfileFragment extends Fragment {

    private String[] items = {"", "Laki-laki", "Perempuan"};
    FragmentEditProfileBinding binding;
    ArrayAdapter<String> adapterItems;
    View view;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private User user;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        if (((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setCustomView(R.layout.action_bar_custom);
            TextView tvTitle = getActivity().findViewById(R.id.tvTitle);
            tvTitle.setText("Edit Profile");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, items);
        binding.spGender.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance("https://tourmate-a3731-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference();

        databaseReference.child("users").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);

                String gender = user.getGender();
                if (gender.equalsIgnoreCase("Laki-laki")) {
                    binding.spGender.setSelection(1);
                } else {
                    binding.spGender.setSelection(2);
                }

                binding.TIETFirstname.setText(user.getFirstName());
                binding.TIETLastname.setText(user.getLastName());
                binding.TIETTelephone.setText(user.getPhone());
                binding.TIETBirthdate.setText(user.getBirthDate());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ERROR", "onCancelled: "+error);
            }
        });

        binding.btUpload.setOnClickListener(view1 -> {
            String firstName = binding.TIETFirstname.getText().toString();
            String lastName = binding.TIETLastname.getText().toString();
            String telephone = binding.TIETTelephone.getText().toString();
            String birthdate = binding.TIETBirthdate.getText().toString();
            String gender = binding.spGender.getSelectedItem().toString();

            User newUser = new User(firstName, lastName, user.getEmail(), telephone, gender, birthdate);

            databaseReference.child("users").child(mAuth.getUid()).setValue(newUser)
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getActivity(), "success to update data", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(getActivity(), new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Failed to update data", Toast.LENGTH_SHORT).show();
                        }
                    });

            getActivity().finish();

        });

        return this.view;
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId()== android.R.id.home) {
//            finish();
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}