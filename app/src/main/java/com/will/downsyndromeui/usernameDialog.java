package com.will.downsyndromeui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class usernameDialog extends AppCompatDialogFragment {


   private usernameListner listner ;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            listner = (usernameListner) context ;

        }catch(ClassCastException e){
            throw new ClassCastException(context.toString() + "Must be implemented");


        }
    }

    EditText edtUsername ;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());

        LayoutInflater inflater =  getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_change_username , null);

        edtUsername = view.findViewById(R.id.edtNewUsername);


        builder.setView(view).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        })
        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String newUsername = edtUsername.getText().toString().trim();

                listner.ApplyUsername(newUsername);
            }
        });



        return builder.create();
    }

    public interface usernameListner{

        void ApplyUsername(String uName) ;

    }


}
