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

public class passwordDialog extends AppCompatDialogFragment {

    private passwordDialog.passwordInterface listner ;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            listner = (passwordDialog.passwordInterface) context ;

        }catch(ClassCastException e){
            throw new ClassCastException(context.toString() + "Must be implemented");


        }
    }

    EditText edtOldPass , edtPassword ;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());

        LayoutInflater inflater =  getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_change_password , null);

        edtOldPass = view.findViewById(R.id.edtOldPass);
        edtPassword = view.findViewById(R.id.edtNewPass);


        builder.setView(view).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        })
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String newPass = edtPassword.getText().toString().trim();
                        String oldPass = edtOldPass.getText().toString().trim();
                        listner.ApplyUsername(newPass , oldPass);
                    }
                });



        return builder.create();
    }

    public interface passwordInterface{

        void ApplyUsername(String oldPass , String newPassword) ;

    }






}
