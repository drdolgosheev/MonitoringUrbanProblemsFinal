package com.example.monitoringurbanproblems.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.monitoringurbanproblems.LoginActivity;
import com.example.monitoringurbanproblems.MainActivity;
import com.example.monitoringurbanproblems.R;
import com.example.monitoringurbanproblems.RegisterActivity;
import com.example.monitoringurbanproblems.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class NotificationsFragment extends Fragment{

    private NotificationsViewModel notificationsViewModel;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser fb_user = FirebaseAuth.getInstance().getCurrentUser();

    TextView email, prob_count;
    Button logout_but;

    User cur_user;
    String mail, email_message, problem_message;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        email = root.findViewById(R.id.user_email_profile);
        prob_count = root.findViewById(R.id.problems_count_profile);

        logout_but = root.findViewById(R.id.logout_account);

        logout_but.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mAuth.signOut();
                logoutIntent();
            }
        });

        mail = fb_user.getEmail();

        db.collection("users").document(mail).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                cur_user = documentSnapshot.toObject(User.class);

                email_message = "Email: " + cur_user.getMail();
                problem_message = "Problems total uploaded: " + cur_user.getProblemCount();

                email.setText(email_message);
                prob_count.setText(problem_message);

            }
        });


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void logoutIntent() {
        Intent intent = new Intent(getContext(), RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}