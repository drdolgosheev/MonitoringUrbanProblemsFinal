package com.example.monitoringurbanproblems;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.example.monitoringurbanproblems.ui.home.RecycleViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecycleViewAdapterModerator extends RecyclerView.Adapter<RecycleViewAdapterModerator.ViewHolder> {

    private static final String TAG = "RecycleViewAdapter";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    FirebaseUser fb_user = FirebaseAuth.getInstance().getCurrentUser();
    Problem cur_problem;
    public int cur_pos;

    private ArrayList<String> mImagesName = new ArrayList<>();
    private ArrayList<String> mDescription = new ArrayList<>();
    private List<Problem> probList;
    private Context mContext;

    public RecycleViewAdapterModerator(List<Problem> probList, ArrayList<String> mDescription, ArrayList<String> mImagesName,
                               Context mContext) {
        this.probList = probList;
        this.mImagesName = mImagesName;
        this.mContext = mContext;
        this.mDescription = mDescription;
    }

    @NonNull
    @Override
    public RecycleViewAdapterModerator.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_wiev_moderator, parent, false);
        RecycleViewAdapterModerator.ViewHolder holder = new RecycleViewAdapterModerator.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecycleViewAdapterModerator.ViewHolder holder, final int position) {

        cur_pos = position;
        Log.e("cur_pos: ", cur_pos + " ");
        cur_problem = probList.get(position);
        StorageReference riversRef = storageRef.child("prob_for_moder/" + cur_problem.getUserId() + "_" + cur_problem.getId() + ".jpeg");

        Log.e("URI", riversRef.getPath());
        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(mContext)
                        .load(uri)
                        .into(holder.image);
            }
        });

        holder.problem_name.setText(mImagesName.get(position));

        holder.prob_desc.setText(mDescription.get(position));

        if (cur_problem.getStatus() >= 2){
            holder.checked.setVisibility(View.INVISIBLE);
            holder.checked.setClickable(false);
        }

        if (cur_problem.getStatus() >= 4){
            holder.solved.setVisibility(View.INVISIBLE);
            holder.solved.setClickable(false);
        }

        if(cur_problem.getStatus() == 0){
            holder.checked.setVisibility(View.INVISIBLE);
            holder.checked.setClickable(false);
            holder.solved.setVisibility(View.INVISIBLE);
            holder.solved.setClickable(false);
            holder.giveStrike.setChecked(true);
            holder.giveStrike.setClickable(false);
        }

        holder.checked.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cur_problem.setStatus(2);
                    cur_problem.setModer_mail(fb_user.getEmail());
                    db.collection("problems").document(cur_problem.getUserId() + "_" + cur_problem.getId() + ".jpeg").set(cur_problem).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(mContext, "Статус проблемы успешно изменен", Toast.LENGTH_LONG).show();
                        }
                    });

                    db.collection("users").document(cur_problem.getUserId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            User cur_user = documentSnapshot.toObject(User.class);
                            probList.set(cur_pos, cur_problem);
                            cur_user.setProblems(probList);
                            db.collection("users").document(cur_problem.getUserId()).set(cur_user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.e("DABASE: ", "Problem seted");
                                }
                            });
                        }
                    });

                    holder.checked.setVisibility(View.INVISIBLE);
                    holder.checked.setClickable(false);
                }
            }
        });

        holder.solved.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cur_problem.setModer_mail(fb_user.getEmail());
                    cur_problem.setStatus(4);
                    db.collection("problems").document(cur_problem.getUserId() + "_" + cur_problem.getId() + ".jpeg").set(cur_problem).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(mContext, "Статус проблемы успешно изменен", Toast.LENGTH_LONG).show();
                        }
                    });

                    db.collection("users").document(cur_problem.getUserId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            User cur_user = documentSnapshot.toObject(User.class);
                            probList.set(cur_pos, cur_problem);
                            cur_user.setProblems(probList);
                            db.collection("users").document(cur_problem.getUserId()).set(cur_user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.e("DABASE: ", "Problem seted");
                                }
                            });
                        }
                    });

                    holder.solved.setVisibility(View.INVISIBLE);
                    holder.solved.setClickable(false);
                }
            }
        });


        holder.giveStrike.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cur_problem.setModer_mail(fb_user.getEmail());
                    cur_problem.setStatus(0);
                    db.collection("problems").document(cur_problem.getUserId() + "_" + cur_problem.getId() + ".jpeg").set(cur_problem).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(mContext, "Статус проблемы успешно изменен", Toast.LENGTH_LONG).show();
                        }
                    });

                    db.collection("users").document(cur_problem.getUserId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            User cur_user = documentSnapshot.toObject(User.class);
                            probList.set(cur_pos, cur_problem);
                            cur_user.setProblems(probList);
                            int cur_strike_count = cur_user.getStrikeCount() + 1;
                            cur_user.setStrikeCount(cur_strike_count);
                            db.collection("users").document(cur_problem.getUserId()).set(cur_user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.e("DABASE: ", "Problem seted");
                                }
                            });
                        }
                    });

                    holder.checked.setVisibility(View.INVISIBLE);
                    holder.checked.setClickable(false);
                    holder.solved.setVisibility(View.INVISIBLE);
                    holder.solved.setClickable(false);
                    holder.giveStrike.setClickable(false);
                }
            }
        });

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ModeratorCommentFrame.class);
                Problem c_prob = probList.get(position);
                intent.putExtra("position", position);
                intent.putExtra("state", c_prob.getStatus());
                try {
                    intent.putExtra("adress", c_prob.getAddressForLocation(mContext,
                            c_prob.getLatitude(), c_prob.getLongitude()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                intent.putExtra("name", c_prob.getName());
                intent.putExtra("desc", c_prob.getDescription());
                intent.putExtra("moder_com", c_prob.getModer_coment());
                intent.putExtra("probId", c_prob.getId());
                intent.putExtra("userMail", c_prob.getUserId());
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mImagesName.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView image;
        TextView problem_name, prob_desc;
        RelativeLayout layout;
        RadioButton solved, checked, giveStrike;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.problem_image_moder);
            problem_name = itemView.findViewById(R.id.problem_description_moder);
            prob_desc = itemView.findViewById(R.id.prob_desc_moder);
            layout = itemView.findViewById(R.id.parent_layout_moder);
            solved = itemView.findViewById(R.id.radioButtonStatusSolved);
            checked = itemView.findViewById(R.id.radioButtonStatusChecked);
            giveStrike = itemView.findViewById(R.id.radioButtonStatusStrike);

        }
    }
}