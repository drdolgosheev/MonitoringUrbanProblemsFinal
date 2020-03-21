package com.example.monitoringurbanproblems;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.monitoringurbanproblems.ui.dashboard.MapFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class add_problem extends Activity implements View.OnClickListener{
    Bundle arguments;
    Double longitude;
    Double latitude;
    EditText prob_desc, prob_name;
    Button confirm_but;
    FloatingActionButton photo_button;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser fb_user = FirebaseAuth.getInstance().getCurrentUser();
    User cur_user;
    Problem problem;
    List<Problem> problemList;
    String problem_description, problem_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        setContentView(R.layout.activity_add_problem);

        View v = new View(add_problem.this);

        v.setClipToOutline(true);

        prob_desc = (EditText) findViewById(R.id.text_problem_description);
        prob_name = (EditText) findViewById(R.id.prob_name);
        confirm_but = (Button) findViewById(R.id.button_add_problem);
        photo_button = (FloatingActionButton) findViewById(R.id.batton_camera);
        arguments = getIntent().getExtras();
        longitude = arguments.getDouble("longitude");
        latitude = arguments.getDouble("latitude");
        problem = new Problem(0,0, fb_user.getEmail(),"testurl","url",0,"name");
        problemList = new List<Problem>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(@Nullable Object o) {
                return false;
            }

            @NonNull
            @Override
            public Iterator<Problem> iterator() {
                return null;
            }

            @NonNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NonNull
            @Override
            public <T> T[] toArray(@NonNull T[] a) {
                return null;
            }

            @Override
            public boolean add(Problem problem) {
                return false;
            }

            @Override
            public boolean remove(@Nullable Object o) {
                return false;
            }

            @Override
            public boolean containsAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(@NonNull Collection<? extends Problem> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, @NonNull Collection<? extends Problem> c) {
                return false;
            }

            @Override
            public boolean removeAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public Problem get(int index) {
                return null;
            }

            @Override
            public Problem set(int index, Problem element) {
                return null;
            }

            @Override
            public void add(int index, Problem element) {

            }

            @Override
            public Problem remove(int index) {
                return null;
            }

            @Override
            public int indexOf(@Nullable Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(@Nullable Object o) {
                return 0;
            }

            @NonNull
            @Override
            public ListIterator<Problem> listIterator() {
                return null;
            }

            @NonNull
            @Override
            public ListIterator<Problem> listIterator(int index) {
                return null;
            }

            @NonNull
            @Override
            public List<Problem> subList(int fromIndex, int toIndex) {
                return null;
            }
        };
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
                 problem.setDescription(problem_description);
                 problem.setName(problem_name);
                 problem.setLongitude(longitude);
                 problem.setLatitude(latitude);
                 final String mail = fb_user.getEmail();
                 db.collection("users").document(mail).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                     @Override
                     public void onSuccess(DocumentSnapshot documentSnapshot) {
                         Log.e("TAG", mail);
                         cur_user = documentSnapshot.toObject(User.class);
                         problemList = cur_user.getProblems();
                         problem.setId(cur_user.getProblemCount());
                         cur_user.setProblemCount(cur_user.getProblemCount() + 1);
                         problem.setUserId(cur_user.getMail());
                         problemList.add(problem);
                         cur_user.setProblems(problemList);
                         db.collection("users").document(documentSnapshot.getId()).set(cur_user);
                         Toast.makeText(add_problem.this, documentSnapshot.getId(),
                                 Toast.LENGTH_SHORT).show();
                     }
                 });

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
        }
    }
}
