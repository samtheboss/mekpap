package com.mekpap.mekPap.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.mekpap.mekPap.R;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolder> {
    private List<HistoryObject> itemList;
    private Context context;

    public HistoryAdapter(List<HistoryObject> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        HistoryViewHolder rcv = new HistoryViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        holder.rideid.setText(itemList.get(position).getRideId());
        holder.time.setText(itemList.get(position).getRequestTime());
        holder.rideid.setEnabled(false);


    }

    @Override
    public int getItemCount() {

       return this.itemList.size();
    }
}
