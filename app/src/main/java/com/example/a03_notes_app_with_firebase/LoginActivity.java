package com.example.a03_notes_app_with_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText email_edit, password_edit;
    Button btn_Login;
    android.widget.ProgressBar ProgressBar;
    TextView Register_text_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_edit = findViewById(R.id.email_edit);
        password_edit = findViewById(R.id.password_edit);
        btn_Login = findViewById(R.id.btn_Login);
        ProgressBar = findViewById(R.id.ProgressBar);
        Register_text_btn = findViewById(R.id.Register_text_btn);

        btn_Login.setOnClickListener(v -> LoginUser());
        Register_text_btn.setOnClickListener(v -> startActivities(new Intent[]{new Intent(LoginActivity.this, Create_AccountActivity.class)}));
    }

    void LoginUser(){
        String email = email_edit.getText().toString();
        String password = password_edit.getText().toString();

        boolean check = check_info(email, password);
        if (!check) return;

        LoginFireBase(email, password);
    }

    void LoginFireBase(String email, String pass){
        changeInProgress(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if (task.isSuccessful()){
                    // Đăng nhập thành công và đã xác minh Email
                    if (firebaseAuth.getCurrentUser().isEmailVerified()){
                        // Chuyển đến màn hình chính
                        startActivities(new Intent[]{new Intent(LoginActivity.this, MainActivity.class)});
                        finish();
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Email chưa được xác nhận. \nHãy kiểm tra lại Email bạn đã đăng ký và xác nhận!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void changeInProgress(boolean inProgressBar){
        // Thay đổi trạng thái của ProgressBar
        if (inProgressBar){
            ProgressBar.setVisibility(View.VISIBLE); // HIện ra ProgressBar
            btn_Login.setVisibility(View.GONE); // Ẩn nút tạo tài khoản đi
        }
        else {
            ProgressBar.setVisibility(View.GONE); // Ẩn ra ProgressBar
            btn_Login.setVisibility(View.VISIBLE); // Hiện nút tạo tài khoản đi
        }
    }

    boolean check_info(String email, String password){
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email_edit.setError("Email không hợp lệ");
            return false;
        }
        if(password.length() < 5){
            password_edit.setError("Độ dài của mật khẩu quá ngắn");
            return false;
        }
        return true;
    }
}