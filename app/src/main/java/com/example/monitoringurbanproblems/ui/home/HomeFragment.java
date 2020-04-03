package com.example.monitoringurbanproblems.ui.home;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monitoringurbanproblems.LoginActivity;
import com.example.monitoringurbanproblems.MainActivity;
import com.example.monitoringurbanproblems.Problem;
import com.example.monitoringurbanproblems.R;
import com.example.monitoringurbanproblems.User;
import com.example.monitoringurbanproblems.add_problem;
import com.example.monitoringurbanproblems.callbackListener;
import com.example.monitoringurbanproblems.moderatorPage;
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

import java.util.ArrayList;
import java.util.List;

import static androidx.core.app.ActivityCompat.finishAffinity;

public class HomeFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser fb_user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    StorageReference storageRef;
    User cur_user;
    Problem problem;
    List<Problem> problemList;
    String problem_description, problem_name;
    private RecyclerView recyclerView;
    private HomeViewModel homeViewModel;
    private ArrayList<Integer> mStatus = new ArrayList<>();
    private ArrayList<String> mImagesName = new ArrayList<>();
    private ArrayList<String> mImagesUrls = new ArrayList<>();
    private ArrayList<String> mDescr = new ArrayList<>();
    public String url = " ";
    public String mail;
    public int counter = 1;
    public int counter2 = 1;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_problems, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.ResycleWiev);
        counter = 1;
        storageRef = storage.getReference();
        if (fb_user == null) {
            mAuth.signInWithEmailAndPassword("anon@mail.ru", "123456").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.e("LogInGuest", task.toString());
                        chekGPS();
                        initImageBitmaps();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Failure", e.toString());
                }
            });
        } else {
            chekGPS();
            initImageBitmaps();
        }
        return root;
    }

    private void initImageBitmaps(){
        mail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        cur_user = null;
            db.collection("users").document(mail).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Log.e("TAG", mail);
                    cur_user = documentSnapshot.toObject(User.class);
                    if (cur_user.getProblemCount() != 0) {
                        problemList = cur_user.getProblems();

                        for (counter = 1; counter <= cur_user.getProblemCount(); counter++) {
                            Log.e("COUNTER1", " " + counter);
                            Log.e("PROB1", " " + cur_user.getProblemCount());
                            mImagesName.add(problemList.get(counter - 1).getName());
                            Log.e("mName: ", mImagesName.toString());
                            mDescr.add(problemList.get(counter - 1).getDescription());
                            // Тут возможно что то не так
                            mStatus.add(problemList.get(counter - 1).getStatus());
                        }

//                        for(counter2 = 1; counter2<= cur_user.getProblemCount();counter2++){
//                            url = " ";
//                            Log.e("counter: ", counter2 + " ");
//                            StorageReference riversRef = storageRef.child("images/" + fb_user.getEmail() + "/" + counter2);
//                            riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    url = uri.toString();
//                                    mImagesUrls.add(url);
//                                    Log.e("mImagesUrls: ", mImagesUrls.toString());
//                                    Log.e("counter: ", counter2 + " ");
//                                    if (counter == cur_user.getProblemCount()+1) {
                                        initRecyclerView();
//                                    }
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception exception) {
//                                    // Handle any errors
//                                }
//                            });
//                            try {
//                                Thread.sleep(   500);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
                    }
                }
            });

    }

    private void initRecyclerView(){
        RecycleViewAdapter adapter = new RecycleViewAdapter(mStatus, mDescr, mImagesName, mImagesUrls, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void chekGPS()
    {
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(getContext(), "Включите доступ к GPS", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder
                    .setTitle("Необходимо предоставить доступ к GPS")
                    .setMessage("Если доступ не будет предоставлен, приложение будет закрыто")
                    .setIcon(android.R.drawable.stat_sys_warning)
                    .setPositiveButton("Предоставить", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(android.provider.Settings
                                    .ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("Не предоставлять", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishAffinity((Activity) getContext());
                        }
                    })
                    .setCancelable(false).show();
        }
    }

    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.e("Успешный гостевой вход", "Успешно");
                        }
                    }
                });
    }

    public void getUri(int i, final callbackListener myListener) {
    }

}