package com.mekpap.mekPap.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.mekpap.mekPap.R;


public class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextInputEditText rideid;
    TextView time;
    Button moreInformation;

    public HistoryViewHolder(@NonNull View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);

        rideid = itemView.findViewById(R.id.rideid);
        time = itemView.findViewById(R.id.time);
        moreInformation = itemView.findViewById(R.id.moreInfor);

        moreInformation.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), HistorySingleActivity.class);
            Bundle b = new Bundle();
            b.putString("requestId", rideid.getText().toString());
            intent.putExtras(b);

            v.getContext().startActivity(intent);

        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), HistorySingleActivity.class);
        Bundle b = new Bundle();
        b.putString("requestId", rideid.getText().toString());
        intent.putExtras(b);

        v.getContext().startActivity(intent);


    }
}
