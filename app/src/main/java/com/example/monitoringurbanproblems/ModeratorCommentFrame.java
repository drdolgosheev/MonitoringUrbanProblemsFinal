package com.example.monitoringurbanproblems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.util.List;

public class ModeratorCommentFrame extends Activity {

    Bundle arguments;
    EditText desc, comments;
    TextView status, header_prob_name, adress_tv;
    ImageView save_but;
    Integer state, counter, probId;
    String adress, description, moder_coment, prob_name, adress_parsed, user_mail;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Dialog);
        setContentView(R.layout.activity_moderator_comment_frame);


        adress_tv = (TextView) findViewById(R.id.adress_commentFrame);
        desc = (EditText) findViewById(R.id.prob_desc_card_commentFrame);
        comments = (EditText) findViewById(R.id.coment_card_commentFrame);
        status = (TextView) findViewById(R.id.status_card_text_commentFrame);
        header_prob_name = (TextView) findViewById(R.id.header_text_card_commentFrame);
        save_but = (ImageView) findViewById(R.id.save_commentFrame);

        save_but.setImageResource(R.drawable.save);

        arguments = getIntent().getExtras();
        counter = arguments.getInt("position");
        state = arguments.getInt("state");
        adress = arguments.getString("adress");
        prob_name = arguments.getString("name");
        description = arguments.getString("desc");
        moder_coment = arguments.getString("moder_com");
        probId = arguments.getInt("probId");
        user_mail = arguments.getString("userMail");

        desc.setTag(comments.getKeyListener());
        desc.setKeyListener(null);

        adress_tv.setText("Адресс: " + adress);
        desc.setText(description);

        if (!moder_coment.equals(" "))
            comments.setText(moder_coment);

        status.setText("Статус: " + Problem.getStatus(state));
        header_prob_name.setText(prob_name);

        save_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("problems").
                        document(user_mail + "_" + probId + ".jpeg").
                        get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Problem cur_prob = documentSnapshot.toObject(Problem.class);
                        cur_prob.setModer_coment(comments.getText().toString());
                        db.collection("problems").
                                document(user_mail + "_" + probId + ".jpeg").
                                set(cur_prob).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(ModeratorCommentFrame.this, "Коментарий успешно сохранен",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });


                db.collection("users").
                        document(user_mail).
                        get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User cur_user = documentSnapshot.toObject(User.class);
                        List<Problem> probList = cur_user.getProblems();
                        Problem cur_prob = probList.get(probId-1);
                        cur_prob.setModer_coment(comments.getText().toString());
                        db.collection("users").
                                document(user_mail).
                                set(cur_user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                    }
                });
            }
        });
    }
}
