package com.example.monitoringurbanproblems;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Button reg_but;
    private TextInputEditText login;
    private EditText pass, conf_pass;
    private FirebaseAuth mAuth;
    private TextView allReg;
    private User user;
    private TextInputEditText user_name_text, user_surname_text;
    private String user_name, user_surname;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Problem problem;
    private List<Problem> prob_list;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();

        reg_but = (Button) findViewById(R.id.registerButton);
        login = (TextInputEditText) findViewById(R.id.login);
        pass = (EditText) findViewById(R.id.password);
        conf_pass = (EditText) findViewById(R.id.passwordConfiration);
        allReg = (TextView) findViewById(R.id.alreadyReg);

        prob_list = new List<Problem>() {
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
        problem = new Problem(0,0, "testid", "testdesc",
                "testurl", 0, "testname");
        prob_list.add(0,problem);
        user = new User("John","Doe","test@mail.com",
                0,0,false,false, prob_list, "");
        user_name_text = (TextInputEditText) findViewById(R.id.user_name);
        user_surname_text = (TextInputEditText) findViewById(R.id.user_surname);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // для ролей
//        updateUI(currentUser);
            allReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == reg_but.getId()) {
            user_name = user_name_text.getText().toString();
            user_surname = user_surname_text.getText().toString();
            if(!user_surname_text.getText().toString().isEmpty() ||
                    !user_surname_text.getText().toString().isEmpty()) {
                if (pass.getText().toString().isEmpty() || login.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Вы не указали логин или пароль",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (pass.getText().toString().equals(conf_pass.getText().toString())) {
                        reg_but.setEnabled(false);
                        createAccount(login.getText().toString(), pass.getText().toString());
                    } else {
                        Toast.makeText(RegisterActivity.this, "Пароли не совпадают",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }else
                Toast.makeText(RegisterActivity.this, "Укажите имя и фамилию",
                        Toast.LENGTH_SHORT).show();
        }
    }

    public void createAccount(final String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            reg_but.setEnabled(false);
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser fUser = mAuth.getCurrentUser();
                            user.setName(user_name);
                            user.setSurname(user_surname);
                            user.setMail(email);
                            db.collection("users").document(user.getMail()).set(user);
                            Toast.makeText(RegisterActivity.this, "Регистрация успешна",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Такой пользователь уже сушествует",
                                    Toast.LENGTH_SHORT).show();
                            reg_but.setEnabled(true);
//                            updateUI(null);
                        }
                    }
                });
    }

//    private void addUser(){
//        myRef.child("user").child(user.getMail().replaceAll("\\.","@"))
//                .setValue(user, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//            }
//        });
    }


