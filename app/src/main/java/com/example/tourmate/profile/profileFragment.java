package com.example.tourmate.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tourmate.R;
import com.example.tourmate.auth.Login;
import com.example.tourmate.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;

public class profileFragment extends Fragment {

    FragmentProfileBinding binding;
    View view;
    private FirebaseAuth mAuth;

    public profileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        if (((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setCustomView(R.layout.action_bar_custom);
            TextView tvTitle = getActivity().findViewById(R.id.tvTitle);
            tvTitle.setText("Profile");
        }

        mAuth = FirebaseAuth.getInstance();

        setHasOptionsMenu(true);

        return this.view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logOut) {
            LogOut();
        } else if (item.getItemId()==R.id.editProfile) {
            
        }

        return super.onOptionsItemSelected(item);
    }

    private void LogOut() {
        mAuth.signOut();
        Intent intent = new Intent(view.getContext(), Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}