package com.mekpap.mekPap.customer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.mekpap.mekPap.R;

public class appointmentType extends DialogFragment {
    int position = 0;

    public interface SingleChoiceListener {
        void onPositiveButtonClicked(String[] list, int position);

        void onNegativeButtonClicked();
    }

    SingleChoiceListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (SingleChoiceListener) context;
        } catch (Exception e) {
            throw new ClassCastException(getActivity().toString() + "error");

        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String[] list = getActivity().getResources().getStringArray(R.array.appontmentType);
        builder.setTitle("select Appontment type").setSingleChoiceItems(list, position, (dialog, i)
                -> position = i).setPositiveButton("Ok", (dialog, i)
                -> mListener.onPositiveButtonClicked(list, position)).setNegativeButton("Cancel", (dialog, i)
                -> mListener.onNegativeButtonClicked());
        return builder.create();
    }
}
