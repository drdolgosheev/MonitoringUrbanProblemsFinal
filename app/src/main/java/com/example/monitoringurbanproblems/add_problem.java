package com.example.monitoringurbanproblems;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class add_problem extends Activity implements View.OnClickListener{
    Bundle arguments;
    Double longitude;
    Double latitude;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText prob_desc, prob_name;
    Button confirm_but;
    FloatingActionButton photo_button;
    FirebaseUser fb_user = FirebaseAuth.getInstance().getCurrentUser();
    User cur_user;
    Problem problem;
    String problem_description, problem_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        setContentView(R.layout.activity_add_problem);

        prob_desc = (EditText) findViewById(R.id.text_problem_description);
        prob_name = (EditText) findViewById(R.id.prob_name);
        confirm_but = (Button) findViewById(R.id.button_add_problem);
        photo_button = (FloatingActionButton) findViewById(R.id.batton_camera);
        arguments = getIntent().getExtras();
        longitude = arguments.getDouble("longitude");
        latitude = arguments.getDouble("latitude");
        problem = new Problem(0,0, fb_user.getEmail(),"testurl","url",0,"name");
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == confirm_but.getId()) {
            if(prob_name.getText().toString().isEmpty()){
                Toast.makeText(add_problem.this, "Укажите название проблемы",
                        Toast.LENGTH_SHORT).show();
            }else
                if(prob_desc.getText().toString().isEmpty()){
                Toast.makeText(add_problem.this, "Укажите описание проблемы",
                        Toast.LENGTH_SHORT).show();
            }else {
                 problem_description = prob_desc.getText().toString();
                 problem_name = prob_name.getText().toString();
                 Log.e("GASGH", problem_description);
                 problem.setDescription("sdkkjdskjsd");
                 Log.e("GACHIMUCHI", problem.getDescription());
                 problem.setName(problem_name);
                 problem.setLongitude(longitude);
                 problem.setLatitude(latitude);
                 db.collection("users").document(fb_user.getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                     @Override
                     public void onSuccess(DocumentSnapshot documentSnapshot) {
                         Toast.makeText(add_problem.this, documentSnapshot.getId(),
                                 Toast.LENGTH_SHORT).show();
                     }
                 });
                }
        }
    }
}
