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

public class Create_AccountActivity extends AppCompatActivity {

    EditText email_edit, password_edit, config_password_edit;
    Button create_Account_btn;
    ProgressBar ProgressBar;
    TextView Login_text_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        email_edit = findViewById(R.id.email_edit);
        password_edit = findViewById(R.id.password_edit);
        config_password_edit = findViewById(R.id.config_password_edit);
        create_Account_btn = findViewById(R.id.create_Account_btn);
        ProgressBar = findViewById(R.id.ProgressBar);
        Login_text_btn = findViewById(R.id.Login_text_btn);

        // Khi nút Create được nhấn
        create_Account_btn.setOnClickListener(v -> CreateAccount());
        Login_text_btn.setOnClickListener(v -> startActivities(new Intent[]{new Intent(Create_AccountActivity.this, LoginActivity.class)}));
    }

    void CreateAccount(){
        String email = email_edit.getText().toString();
        String password = password_edit.getText().toString();
        String config_password = config_password_edit.getText().toString();

        boolean check = check_info(email, password, config_password);
        if (!check) return;

        // Tạo một tài khoản bằng Firebase
        createAccount_Firebase(email, password);
    }

    void createAccount_Firebase(String email, String password){
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(); // Tạo đối tượng FirebaseAuth từ thư viện được dùng để xác thực người dùng
        // Tạo một tài khoản người dùng mới bằng cách cung cấp thông tin về email và pass sau đó lắng nghe thông tin mà FireBase trả về
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Create_AccountActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if(task.isSuccessful()){
                    firebaseAuth.getCurrentUser().sendEmailVerification(); // Gửi một Email xác thực đến địa chỉ email mà người dùng đăng ký
                    firebaseAuth.signOut(); // Đăng xuất người dùng khỏi FireBase sau khi đã hoàn thành việc tạo tài khoản và gửi email xác thực
                    finish(); // Kết thúc hoạt động hiện tại
                }
                else {
                    // Thất bại thì sẽ gửi thông báo và cho biết lý do tại sao lại thất bại
                    Toast.makeText(Create_AccountActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Tạo hiệu ứng với thanh ProgressBar
    void changeInProgress(boolean inProgressBar){
        // Thay đổi trạng thái của ProgressBar
        if (inProgressBar){
            ProgressBar.setVisibility(View.VISIBLE); // HIện ra ProgressBar
            create_Account_btn.setVisibility(View.GONE); // Ẩn nút tạo tài khoản đi
        }
        else {
            ProgressBar.setVisibility(View.GONE); // Ẩn ra ProgressBar
            create_Account_btn.setVisibility(View.VISIBLE); // Hiện nút tạo tài khoản đi
        }
    }

    boolean check_info(String email, String password, String config_password){
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email_edit.setError("Email không hợp lệ");
            return false;
        }
        if(password.length() < 5){
            password_edit.setError("Độ dài của mật khẩu quá ngắn");
            return false;
        }
        if (!password.equals(config_password)){
            config_password_edit.setError("Mật khẩu không khớp");
            return false;
        }
        return true;
    }
}