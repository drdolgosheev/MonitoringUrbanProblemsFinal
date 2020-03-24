package com.example.monitoringurbanproblems.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.monitoringurbanproblems.Problem;
import com.example.monitoringurbanproblems.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder >{
    private static final String TAG = "RecycleViewAdapter";

    private ArrayList<String> mImagesName = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mDescription = new ArrayList<>();
    private ArrayList<Integer> mStatus = new ArrayList<>();
    private Context mContext;

    public RecycleViewAdapter(ArrayList<Integer> mStatus, ArrayList<String> mDescription, ArrayList<String> mImagesName,
                              ArrayList<String> mImages, Context mContext) {
        this.mImagesName = mImagesName;
        this.mImages = mImages;
        this.mContext = mContext;
        this.mDescription = mDescription;
        this.mStatus = mStatus;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_wiev_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG,"Is called.");

        Glide.with(mContext).asBitmap().load(mImages.get(position)).into(holder.image);

        holder.problem_name.setText(mImagesName.get(position));

        holder.prob_desc.setText(mDescription.get(position));

        holder.prob_status.setText("Статус: " + Problem.getStatus(mStatus.get(position)));
//        holder.prob_status.setTextColor();

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,mImagesName.get(position), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImagesName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView image;
        TextView problem_name, prob_desc, prob_status;
        RelativeLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.problem_image);
            problem_name = itemView.findViewById(R.id.problem_description);
            prob_desc = itemView.findViewById(R.id.prob_desc);
            prob_status = itemView.findViewById(R.id.problem_status);
            layout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
