package com.example.monitoringurbanproblems.ui.notifications;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.monitoringurbanproblems.LoginActivity;
import com.example.monitoringurbanproblems.MainActivity;
import com.example.monitoringurbanproblems.R;
import com.example.monitoringurbanproblems.RegisterActivity;
import com.example.monitoringurbanproblems.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class NotificationsFragment extends Fragment {


    static final int GALLERY_REQUEST = 1;
    private NotificationsViewModel notificationsViewModel;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser fb_user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    StorageReference riversRef;
    Uri path;

    TextView email, prob_count, reg_text, login_text, logout_text;
    Button upload_ava_but;
    CircleImageView avatar;
    ImageView upload_ava_iv, login_but, register_but, logout_but;

    User cur_user;
    String mail, email_message, problem_message;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_profile, container, false);

        if(fb_user == null || fb_user.getEmail().equals("anon@mail.ru")) {
            mAuth.signInWithEmailAndPassword("anon@mail.ru", "123456").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.e("LogInGuest", task.toString());
                        email = root.findViewById(R.id.user_email_profile);
                        prob_count = root.findViewById(R.id.problems_count_profile);
                        logout_but = root.findViewById(R.id.logout_account_iv);
                        reg_text = root.findViewById(R.id.reg_text);
                        login_text = root.findViewById(R.id.login_text);
                        logout_text = root.findViewById(R.id.logout_text);

                        login_text.setVisibility(View.INVISIBLE);
                        logout_but.setVisibility(View.INVISIBLE);
                        logout_but.setClickable(false);
                        logout_text.setVisibility(View.INVISIBLE);
                        reg_text.setVisibility(View.VISIBLE);
                        login_text.setVisibility(View.VISIBLE);
                        login_but = root.findViewById(R.id.LoginIV);
                        register_but = root.findViewById(R.id.RegisterIV);
                        upload_ava_iv = root.findViewById(R.id.new_upload_but);
                        avatar = root.findViewById(R.id.avatar_pic);
                        mail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                        login_but.setImageResource(R.drawable.log);
                        register_but.setImageResource(R.drawable.reg);
                        avatar.setImageResource(R.drawable.ic_user_blue);

                        login_but.setVisibility(View.VISIBLE);
                        register_but.setVisibility(View.VISIBLE);
                        login_but.setClickable(true);
                        register_but.setClickable(true);
                        logout_but.setVisibility(View.INVISIBLE);
                        logout_but.setClickable(false);

                        logout_but.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                mAuth.signOut();
                                login_but.setVisibility(View.GONE);
                                login_text.setVisibility(View.GONE);
                                reg_text.setVisibility(View.GONE);
                                register_but.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "Вы успешно покинули вашу учетную запись",
                                        Toast.LENGTH_SHORT).show();
                                logoutIntent();
                            }
                        });

                        upload_ava_iv.setImageResource(R.drawable.download);
                        upload_ava_iv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getContext(), "Что бы загрузить аватар зарегестрируйтесь или войдите",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });


                        login_but.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loginIntent();
                            }
                        });

                        register_but.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                registerIntent();
                            }
                        });


                        riversRef = storageRef.child("Profile_image/" + mail);

                        db.collection("users").document(mail).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                cur_user = documentSnapshot.toObject(User.class);

                                email_message = "Почта: " + cur_user.getMail();
                                problem_message = "Проблем загружено: " + cur_user.getProblemCount();

                                email.setText(email_message);
                                prob_count.setText(problem_message);
                                avatar = null;
                                Log.e("Ava is null", " ");
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Failure", e.toString());
                }
            });


//        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//            }
//        });
        } else {
            email = root.findViewById(R.id.user_email_profile);
            prob_count = root.findViewById(R.id.problems_count_profile);
            logout_but = root.findViewById(R.id.logout_account_iv);
            upload_ava_iv = root.findViewById(R.id.new_upload_but);
            avatar = root.findViewById(R.id.avatar_pic);
            login_but = root.findViewById(R.id.LoginIV);
            register_but = root.findViewById(R.id.RegisterIV);
            mail = fb_user.getEmail();
            reg_text = root.findViewById(R.id.reg_text);
            login_text = root.findViewById(R.id.login_text);
            logout_text = root.findViewById(R.id.logout_text);

//            logout_text.setVisibility(View.VISIBLE);
            logout_but.setVisibility(View.VISIBLE);
            logout_but.setClickable(true);
            reg_text.setVisibility(View.INVISIBLE);
            login_text.setVisibility(View.INVISIBLE);
            login_but.setVisibility(View.INVISIBLE);
            register_but.setVisibility(View.INVISIBLE);
            login_but.setClickable(false);
            register_but.setClickable(false);
            logout_but.setVisibility(View.VISIBLE);
            logout_but.setClickable(true);

            logout_but.setImageResource(R.drawable.logout);
            avatar.setImageResource(R.drawable.ic_user_blue);
            upload_ava_iv.setImageResource(R.drawable.download);

            logout_but.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mAuth.signOut();
                    Toast.makeText(getContext(), "Вы успешно покинули вашу учетную запись",
                            Toast.LENGTH_SHORT).show();
                    logoutIntent();
                }
            });

            upload_ava_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
                }
            });

            riversRef = storageRef.child("Profile_image/" + fb_user.getEmail());

            db.collection("users").document(mail).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    cur_user = documentSnapshot.toObject(User.class);

                    email_message = "Почта: " + cur_user.getMail();
                    problem_message = "Проблем загружено: " + cur_user.getProblemCount();

                    email.setText(email_message);
                    prob_count.setText(problem_message);
                    if (!cur_user.isHaveAva().equals("")) {
                        Log.e("isHaveAva: ", "|" + cur_user.isHaveAva() + "|");
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(getContext())
                                        .load(uri)
                                        .into(avatar);
                                Log.e("Ava is notnull", riversRef.getDownloadUrl().toString());
                            }
                        });
                    } else {
                        avatar = null;
                        Log.e("Ava is null", " ");
                    }
                }
            });
        }
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void logoutIntent() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }

    public void registerIntent(){
        Intent intent = new Intent(getContext(), RegisterActivity.class);
        startActivity(intent);
    }

    public void loginIntent(){
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = avatar.getWidth();
        int targetH = avatar.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path.toString(), bmOptions);
        avatar.setImageBitmap(bitmap);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {

                    riversRef = storageRef.child("Profile_image/" + fb_user.getEmail());

                    path = imageReturnedIntent.getData();

                    UploadTask uploadTask = riversRef.putFile(path);

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getContext(), "При попытке добавить изображение произошла ошибка",
                                    Toast.LENGTH_SHORT).show();
                            Log.e("FAILURE:", "Can not Upload an image");
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            db.collection("users").document(mail).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    cur_user = documentSnapshot.toObject(User.class);
                                    cur_user.setHaveAva(path.toString());
                                    db.collection("users").document(mail).set(cur_user);
                                }
                            });
                            Log.e("Success:", "You Upload an image");
                            Toast.makeText(getContext(), "Изображение успешно добавлено",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
        }
    }
}