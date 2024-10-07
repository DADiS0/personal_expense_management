package com.example.personal_expense_management;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private TextView titleTextView;
    private CheckBox rememberPasswordCheckBox;

    // SharedPreferences لتخزين بيانات المستخدم
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private final String DEFAULT_USERNAME = "mySavedUsername";
    private final String DEFAULT_PASSWORD = "mySavedPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        titleTextView = findViewById(R.id.titleTextView); // افترض أن هناك TextView بعنوان الواجهة
        rememberPasswordCheckBox = findViewById(R.id.rememberPasswordCheckBox);

        // إعداد SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // التحقق إذا كان هناك مستخدم مسجل بالفعل
        checkIfUserExists();

        // التعامل مع الضغط على زر تسجيل الدخول
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });
    }

    private void checkIfUserExists() {
        String savedUsername = sharedPreferences.getString("username", null);
        String savedPassword = sharedPreferences.getString("password", null);

        // إذا تم تفعيل remember password
        boolean rememberPassword = sharedPreferences.getBoolean("rememberPassword", false);

        if (savedUsername != null && savedPassword != null) {
            // المستخدم مسجل بالفعل
            titleTextView.setText("Enter your username and password");
            // إذا تم تفعيل rememberPassword ، نقله مباشرة إلى الشاشة الرئيسية
            if (rememberPassword) {
                Toast.makeText(this, "Logging in automatically...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, list.class);
                startActivity(intent);
                finish();
            }


        } else {
            // لا يوجد مستخدم
            titleTextView.setText("Create your user");
        }
    }


    private void handleLogin() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        String savedUsername = sharedPreferences.getString("username", null);
        String savedPassword = sharedPreferences.getString("password", null);

        // في أول مرة يتم فيها حفظ بيانات المستخدم
        if (savedUsername == null && savedPassword == null) {
            editor.putString("username", username);
            editor.putString("password", password);
            editor.putBoolean("rememberPassword", rememberPasswordCheckBox.isChecked());
            editor.apply();

            Toast.makeText(this, "User created successfully!", Toast.LENGTH_SHORT).show();

            // إذا تم تفعيل خيار تذكر كلمة المرور، الانتقال إلى الشاشة الرئيسية
            if (rememberPasswordCheckBox.isChecked()) {
                Intent intent = new Intent(MainActivity.this, list.class);
                startActivity(intent);
                finish();
            }

        } else {
            // التحقق من صحة بيانات المستخدم المدخلة
            if (username.equals(savedUsername) && password.equals(savedPassword)) {
                // التحقق من remember password
                if (rememberPasswordCheckBox.isChecked()) {
                    editor.putBoolean("rememberPassword", true);
                    editor.apply();
                }

                // الانتقال إلى الشاشة الرئيسية
                Intent intent = new Intent(MainActivity.this, list.class);
                startActivity(intent);
                finish();
            } else {
                // في حالة إدخال كلمة مرور أو اسم مستخدم خاطئ
                Toast.makeText(this, "Username or password is incorrect!", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
