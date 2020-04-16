package com.example.monitoringurbanproblems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.monitoringurbanproblems.ui.home.HomeViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class adminPage extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private ImageView logout_button;
    private TextView email;

    private ArrayList<Boolean> mIsModer = new ArrayList<>();
    private ArrayList<String> mUserlogin = new ArrayList<>();
    private ArrayList<Integer> mUserStrikeCount = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        recyclerView = (RecyclerView) findViewById(R.id.ResycleWievAdmin);
        logout_button = (ImageView) findViewById(R.id.logoutAdmin_iv);
        email = (TextView) findViewById(R.id.admin_email_profile);

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(adminPage.this,
                        "Успешно", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(adminPage.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        email.setText("Email: " + mAuth.getCurrentUser().getEmail());

        InitBitMap();
    }

    private void InitBitMap(){

        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<User> list = new ArrayList<>();
                    for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                    {
                        User cur_user = queryDocumentSnapshot.toObject(User.class);
                        if(!cur_user.getMail().equals("anon@mail.ru") && !(cur_user.isAdmin())) {
                            list.add(cur_user);
                            mIsModer.add(cur_user.isModerator());
                            mUserlogin.add(cur_user.getMail());
                            mUserStrikeCount.add(cur_user.getStrikeCount());
                        }
                    }
                    initRecyclerView(list);
                }

            }

        });
    }

    private void initRecyclerView(List<User> list){
        RecycleViewAdapterAdmin adapter = new RecycleViewAdapterAdmin(list, mIsModer, mUserlogin, mUserStrikeCount, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
