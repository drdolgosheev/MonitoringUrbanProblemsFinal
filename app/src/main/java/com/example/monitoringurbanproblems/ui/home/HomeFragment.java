package com.example.monitoringurbanproblems.ui.home;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.example.monitoringurbanproblems.Problem;
import com.example.monitoringurbanproblems.R;
import com.example.monitoringurbanproblems.User;
import com.example.monitoringurbanproblems.add_problem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static androidx.core.app.ActivityCompat.finishAffinity;

public class HomeFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser fb_user = FirebaseAuth.getInstance().getCurrentUser();
    User cur_user;
    Problem problem;
    List<Problem> problemList;
    String problem_description, problem_name;
    private RecyclerView recyclerView;
    private HomeViewModel homeViewModel;
    private ArrayList<String> mImagesName = new ArrayList<>();
    private ArrayList<String> mImagesUrls = new ArrayList<>();
    private ArrayList<String> mDescr = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_problems, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.ResycleWiev);
        initImageBitmaps();
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        chekGPS();

        return root;
    }

    private void initImageBitmaps(){

        final String mail = fb_user.getEmail();
        cur_user = null;
            db.collection("users").document(mail).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Log.e("TAG", mail);
                    cur_user = documentSnapshot.toObject(User.class);
                    problemList = cur_user.getProblems();
                    Log.e("KAJHJKDSHJASKJHASJKDHJKSAHDJKHDSJKHSDAJKHSADJKH", " " + problemList.size());
                    for (int i = 0; i < problemList.size(); i++) {
                        mImagesUrls.add("https://i.redd.it/tpsnoz5bzo501.jpg");
                        mImagesName.add(problemList.get(i).getName());
                        mDescr.add(problemList.get(i).getDescription());
                    }
                    initRecyclerView();
                }
            });
    }

    private void initRecyclerView(){
        RecycleViewAdapter adapter = new RecycleViewAdapter(mDescr, mImagesName, mImagesUrls, getContext());
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

}