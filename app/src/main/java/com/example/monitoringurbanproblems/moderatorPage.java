package com.example.monitoringurbanproblems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.monitoringurbanproblems.ui.home.HomeViewModel;
import com.example.monitoringurbanproblems.ui.home.RecycleViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class moderatorPage extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private Button logout_button;
    private TextView email;

    private ArrayList<Integer> mStatus = new ArrayList<>();
    private ArrayList<String> mImagesName = new ArrayList<>();
    private ArrayList<String> mDescr = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moderator_page);

        recyclerView = (RecyclerView) findViewById(R.id.ResycleWievModerator);
        logout_button = (Button) findViewById(R.id.logoutModerator);
        email = (TextView) findViewById(R.id.moderator_email_profile);

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(moderatorPage.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        email.setText("Email: " + mAuth.getCurrentUser().getEmail());

        InitBitMap();
    }

    private void InitBitMap(){

        db.collection("problems").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<Problem> list = new ArrayList<>();
                    for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                    {
                        Problem cur_prob = queryDocumentSnapshot.toObject(Problem.class);
                        list.add(cur_prob);
                        mDescr.add(cur_prob.getDescription());
                        mImagesName.add(cur_prob.getName());
                    }
                    initRecyclerView(list);
                }

            }

        });
    }

    private void initRecyclerView(List<Problem> list){
        RecycleViewAdapterModerator adapter = new RecycleViewAdapterModerator(list, mDescr, mImagesName, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
