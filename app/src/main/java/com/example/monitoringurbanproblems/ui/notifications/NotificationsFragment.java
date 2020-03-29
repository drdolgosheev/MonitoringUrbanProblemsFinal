package com.example.monitoringurbanproblems.ui.notifications;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.monitoringurbanproblems.LoginActivity;
import com.example.monitoringurbanproblems.MainActivity;
import com.example.monitoringurbanproblems.R;
import com.example.monitoringurbanproblems.RegisterActivity;
import com.example.monitoringurbanproblems.User;
import com.example.monitoringurbanproblems.add_problem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

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
    StorageReference riversRef = storageRef.child("Profile_image/" + fb_user.getEmail());
    Uri path;

    TextView email, prob_count;
    Button logout_but, upload_ava_but;
    CircleImageView avatar;

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
        upload_ava_but = root.findViewById(R.id.upload_avatar);
        avatar = root.findViewById(R.id.avatar_pic);
        mail = fb_user.getEmail();

        avatar.setImageResource(R.drawable.ic_user_default);

        logout_but.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mAuth.signOut();
                logoutIntent();
            }
        });

        upload_ava_but.setOnClickListener(new View.OnClickListener() {
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

                email_message = "Email: " + cur_user.getMail();
                problem_message = "Problems total uploaded: " + cur_user.getProblemCount();

                email.setText(email_message);
                prob_count.setText(problem_message);
                if (!cur_user.isHaveAva().equals("")) {
                    riversRef.getDownloadUrl().addOnSuccessListener( new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(getContext())
                                    .load(uri)
                                    .into(avatar);
                            Log.e("Ava is notnull", riversRef.getDownloadUrl().toString());
                        }
                    });
                }
                else {
                    avatar = null;
                    Log.e("Ava is null", " ");
                }
            }
        });

        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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