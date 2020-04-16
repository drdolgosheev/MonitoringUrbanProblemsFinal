package com.example.monitoringurbanproblems;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class problemCard extends Activity{

    FirebaseUser fb_user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    EditText desc, comments;
    TextView status, moderEmail, header_prob_name;
    ImageView pic;
    int counter;
    boolean isUser;
    User cur_user;
    Bundle arguments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(android.R.style.Theme_Dialog);

        setContentView(R.layout.activity_problem_card);

        desc = (EditText) findViewById(R.id.prob_desc_card);
        comments = (EditText) findViewById(R.id.coment_card);
        status = (TextView) findViewById(R.id.status_card_text);
        moderEmail = (TextView) findViewById(R.id.moder_email_text_card);
        header_prob_name = (TextView) findViewById(R.id.header_text_card);
        pic = (ImageView) findViewById(R.id.image_photo_card);

        arguments = getIntent().getExtras();
        counter = arguments.getInt("position");

    }

    @Override
    public void onStart() {
        super.onStart();

//        exit_but.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(problemCard.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });

        int real_pos = counter + 1;

        StorageReference riversRef = storageRef.child("images/" + fb_user.getEmail() + "/" + real_pos + ".jpeg");
        riversRef.getDownloadUrl().addOnSuccessListener( new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(problemCard.this)
                        .load(uri)
                        .into(pic);
            }
        });

        getUserProbList();

        desc.setTag(comments.getKeyListener());
        desc.setKeyListener(null);
    }

    public void getUserProbList(){
        db.collection("users").document(fb_user.getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                cur_user = documentSnapshot.toObject(User.class);
                List<Problem> problemList = cur_user.getProblems();
                Problem cur_prob = problemList.get(counter);
                desc.setText(cur_prob.getDescription());
                header_prob_name.setText(cur_prob.getName());
                status.setText("Статус: " + Problem.getStatus(cur_prob.getStatus()));
                moderEmail.setText("Над проблемой работает: " + cur_prob.getModer_mail());

                if (!cur_prob.getModer_coment().equals(" "))
                    comments.setText(cur_prob.getModer_coment());

                Log.e("COMENT", cur_prob.getModer_coment());
                comments.setTag(comments.getKeyListener());
                comments.setKeyListener(null);
            }
        });
    }
}
