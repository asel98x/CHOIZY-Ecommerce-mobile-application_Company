package lk.choizy.company;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

public class PasswordChangeDialog extends DialogFragment {

    TextInputLayout newPassword,re_password;
    Button changeBtn;
    ImageButton closeBtn;
    onPasswordChange listener;

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.password_change_dialog,null);
        builder.setView(view);

        newPassword = view.findViewById(R.id.PassC_NewPass);
        re_password = view.findViewById(R.id.PassC_ReTypePass);
        changeBtn = view.findViewById(R.id.PassC_ChangeBtn);
        closeBtn = view.findViewById(R.id.PassC_CloseBtn);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newPassword.getEditText().getText().toString().trim().length()<=7){
                    newPassword.setError("Password should at least contain 8 characters");
                    newPassword.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            newPassword.setError(null);
                            newPassword.requestFocus();
                        }
                    },2000);
                    return;
                }

                if(newPassword.getEditText().getText().toString().trim().equals(re_password.getEditText().getText().toString().trim())){
                    listener.NewPassword(newPassword.getEditText().getText().toString().trim());
                    dismiss();
                }else {
                    re_password.setError("Password Dose not Match");
                    re_password.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            re_password.setError(null);
                            re_password.getEditText().setText("");
                            newPassword.getEditText().setText("");
                            re_password.requestFocus();

                        }
                    },4000);
                }

            }
        });






        return builder.create();

    }

    @Override
    public void onResume() {
        super.onResume();

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public interface onPasswordChange{
        void NewPassword(String pass);
    }

    public void setListener(onPasswordChange listener) {
        this.listener = listener;
    }
}
