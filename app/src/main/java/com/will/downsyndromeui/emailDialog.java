package com.will.downsyndromeui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class emailDialog extends AppCompatDialogFragment {


    private emailDialog.emailListner listner ;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            listner = (emailDialog.emailListner) context ;

        }catch(ClassCastException e){
            throw new ClassCastException(context.toString() + "Must be implemented");


        }
    }

    EditText edtUsername ;
    TextView tvHeader , tvInstruction ;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());

        LayoutInflater inflater =  getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_change_username , null);

        edtUsername = view.findViewById(R.id.edtNewUsername);

        tvHeader = view.findViewById(R.id.txtHeader);
        tvInstruction = view.findViewById(R.id.txtInstruction);

        tvHeader.setText("Email");
        tvInstruction.setText("Enter your email");



        builder.setView(view).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        })
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String newUsername = edtUsername.getText().toString().trim();

                        listner.ApplyEmail(newUsername);
                    }
                });



        return builder.create();
    }

    public interface emailListner{

        void ApplyEmail(String email) ;

    }


}
