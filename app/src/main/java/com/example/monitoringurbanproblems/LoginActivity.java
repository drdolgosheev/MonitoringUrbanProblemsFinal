package com.example.monitoringurbanproblems;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.WindowId;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends Activity implements View.OnClickListener {

    private Button auth_but;
    private EditText pass, login;
    private FirebaseAuth mAuth;
    ImageView ico;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(android.R.style.Theme_Dialog);

        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        ico = (ImageView) findViewById(R.id.login_ico);
        ico.setImageResource(R.drawable.loginico);
        auth_but = (Button) findViewById(R.id.signInBut);
        login = (EditText) findViewById(R.id.log);
        pass = (EditText) findViewById(R.id.pass);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == auth_but.getId()) {
            if (pass.getText().toString().isEmpty() || login.getText().toString().isEmpty()) {
                System.out.println(pass.toString());
                Toast.makeText(LoginActivity.this, "Пустой логин или пароль",
                        Toast.LENGTH_SHORT).show();
            } else {
                auth_but.setEnabled(false);
                signIn(login.getText().toString(), pass.getText().toString());
            }
        }
    }

    public void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Успешно",
                                    Toast.LENGTH_SHORT).show();

                            db.collection("users").document(mAuth.getCurrentUser().getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    User cur_user = documentSnapshot.toObject(User.class);

                                    if(cur_user.isModerator()){
                                        Intent intent = new Intent(getApplicationContext(), moderatorPage.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }

                                }
                            });
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this,
                                    "Что бы войти зарегестрируйтесь", Toast.LENGTH_SHORT).show();
                            auth_but.setEnabled(true);
//                            updateUI(null);
                        }

                    }
                });
    }
}
