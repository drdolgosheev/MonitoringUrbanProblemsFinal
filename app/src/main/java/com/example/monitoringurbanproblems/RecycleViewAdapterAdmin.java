package com.example.monitoringurbanproblems;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.monitoringurbanproblems.ui.home.RecycleViewAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecycleViewAdapterAdmin extends RecyclerView.Adapter<RecycleViewAdapterAdmin.ViewHolder >{
    private static final String TAG = "RecycleViewAdapterAdmin";

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    FirebaseUser fb_user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private List<User> usersList = new ArrayList<>();
    private ArrayList<Boolean> mIsModer = new ArrayList<>();
    private ArrayList<String> mUserLogin = new ArrayList<>();
    private ArrayList<Integer> mUserStrikeCount = new ArrayList<>();
    private Context mContext;

    public RecycleViewAdapterAdmin(List<User> usersList, ArrayList<Boolean> mIsModer, ArrayList<String> mUsersLogin,
                                   ArrayList<Integer> mUsersStrikeCount, Context mContext) {
        this.usersList = usersList;
        this.mIsModer = mIsModer;
        this.mContext = mContext;
        this.mUserStrikeCount = mUsersStrikeCount;
        this.mUserLogin = mUsersLogin;
    }


    @NonNull
    @Override
    public RecycleViewAdapterAdmin.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_wiev_admin, parent, false);
        RecycleViewAdapterAdmin.ViewHolder holder = new RecycleViewAdapterAdmin.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecycleViewAdapterAdmin.ViewHolder holder, final int position) {
        Log.d(TAG, "Is called.");

        User cur_user = usersList.get(position);

        int real_pos = position + 1;

        Log.e("POS", position + " ");
        if (!cur_user.isHaveAva().equals("")) {
            StorageReference riversRef = storageRef.child("Profile_image/" + usersList.get(position).getMail());
            Log.e(TAG, riversRef.getPath());
            riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(mContext)
                            .load(uri)
                            .into(holder.image);
                }
            });
        }

        holder.user_mail_textV.setText(cur_user.getMail());

        if (cur_user.isModerator()) {
            holder.moder_switch.setChecked(true);
            holder.moder_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!isChecked) {
                        usersList.get(position).setModerator(false);
                        db.collection("users").document(usersList.get(position).getMail())
                                .set(usersList.get(position))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.e(TAG, "User is now moderator");
                                        Toast.makeText(mContext, usersList.get(position).getMail() + " теперь не является модератором",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        usersList.get(position).setModerator(true);
                        db.collection("users").document(usersList.get(position).getMail())
                                .set(usersList.get(position))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.e(TAG, "User is now moderator");
                                        Toast.makeText(mContext, usersList.get(position).getMail() + " теперь модератор",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            });
        } else {
            holder.moder_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        usersList.get(position).setModerator(true);
                        db.collection("users").document(usersList.get(position).getMail())
                                .set(usersList.get(position))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.e(TAG, "User is now moderator");
                                        Toast.makeText(mContext, usersList.get(position).getMail() + " теперь модератор",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        usersList.get(position).setModerator(false);
                        db.collection("users").document(usersList.get(position).getMail())
                                .set(usersList.get(position))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.e(TAG, "User is now moderator");
                                        Toast.makeText(mContext, usersList.get(position).getMail() + " теперь не является модератором",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            });
        }



        if (cur_user.getStrikeCount() >= 2) {
            holder.ban_switch.setChecked(true);
            holder.ban_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!isChecked) {
                        usersList.get(position).setStrikeCount(0);
                        db.collection("users").document(usersList.get(position).getMail())
                                .set(usersList.get(position))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.e(TAG, "User is now unbanned");
                                        Toast.makeText(mContext, usersList.get(position).getMail() + " разблокирован",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        usersList.get(position).setStrikeCount(3);
                        db.collection("users").document(usersList.get(position).getMail())
                                .set(usersList.get(position))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.e(TAG, "User is banned");
                                        Toast.makeText(mContext, usersList.get(position).getMail() + " заблокирован",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            });
        } else {
            holder.ban_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        usersList.get(position).setStrikeCount(3);
                        db.collection("users").document(usersList.get(position).getMail())
                                .set(usersList.get(position))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.e(TAG, "User is now moderator");
                                        Toast.makeText(mContext, usersList.get(position).getMail() + " заблокирован",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        usersList.get(position).setStrikeCount(0);
                        db.collection("users").document(usersList.get(position).getMail())
                                .set(usersList.get(position))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.e(TAG, "User is now moderator");
                                        Toast.makeText(mContext, usersList.get(position).getMail() + " разблокирован",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView image;
        TextView user_mail_textV;
        Switch moder_switch, ban_switch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.profile_image_admin);
            user_mail_textV = itemView.findViewById(R.id.user_login_admin);
            moder_switch = (Switch) itemView.findViewById(R.id.make_moderator);
            ban_switch = (Switch) itemView.findViewById(R.id.give_ban);
        }
    }
}
