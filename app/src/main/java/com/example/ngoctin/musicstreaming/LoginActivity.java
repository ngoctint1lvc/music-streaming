package com.example.ngoctin.musicstreaming;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.yarolegovich.lovelydialog.LovelyInfoDialog;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private static String TAG = "LoginActivity";
    FloatingActionButton fab;
    private final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private EditText editTextUsername, editTextPassword;
    private LovelyProgressDialog waitingDialog;

    private AuthUtils authUtils;
//    private FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener mAuthListener;
//    private FirebaseUser user;
    private boolean firstTimeAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        editTextUsername = (EditText) findViewById(R.id.et_username);
        editTextPassword = (EditText) findViewById(R.id.et_password);
        firstTimeAccess = true;
        authUtils = new AuthUtils();
        waitingDialog = new LovelyProgressDialog(this).setCancelable(false);
    }

    public void clickLogin(View view) {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        if (validate(username, password)) {
            authUtils.signIn(username, password);
        } else {
            Toast.makeText(this, "Invalid email or empty password", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validate(String emailStr, String password) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return (password.length() > 0 || password.equals(";")) && matcher.find();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    class AuthUtils {
        /**
         * Action Login
         *
         * @param email
         * @param password
         */
        void signIn(String email, String password) {
            waitingDialog.setIcon(R.drawable.ic_person_low)
                    .setTitle("Login....")
                    .setTopColorRes(R.color.colorPrimary)
                    .show();
            waitingDialog.dismiss();
            if (email.equals("ngoctin@gmail.com") && password.equals("ngoctin")) {
                Toast.makeText(LoginActivity.this,"ok", Toast.LENGTH_LONG).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                LoginActivity.this.finish();
            } else {
                new LovelyInfoDialog(LoginActivity.this) {
                    @Override
                    public LovelyInfoDialog setConfirmButtonText(String text) {
                        findView(R.id.ld_btn_confirm).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dismiss();
                            }
                        });
                        return super.setConfirmButtonText(text);
                    }
                }
                .setTopColorRes(R.color.colorAccent)
                .setIcon(R.drawable.ic_person_low)
                .setTitle("Login false")
                .setMessage("Email not exist or wrong password!")
                .setCancelable(false)
                .setConfirmButtonText("Ok")
                .show();
            }
        }
    }
}
