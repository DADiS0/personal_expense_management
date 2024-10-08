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
    public SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        titleTextView = findViewById(R.id.titleTextView);
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
        boolean rememberPassword = sharedPreferences.getBoolean("rememberPassword", false);

        if (savedUsername != null && savedPassword != null) {
            titleTextView.setText("Enter your username and password");
            if (rememberPassword) {
                Toast.makeText(this, "Logging in automatically...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            titleTextView.setText("Create your user");
        }
    }

    private void handleLogin() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        String savedUsername = sharedPreferences.getString("username", null);
        String savedPassword = sharedPreferences.getString("password", null);

        if (savedUsername == null && savedPassword == null) {
            editor.putString("username", username);
            editor.putString("password", password);
            editor.putBoolean("rememberPassword", rememberPasswordCheckBox.isChecked());
            editor.apply();

            Toast.makeText(this, "User created successfully!", Toast.LENGTH_SHORT).show();

            if (rememberPasswordCheckBox.isChecked()) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            if (username.equals(savedUsername) && password.equals(savedPassword)) {
                if (rememberPasswordCheckBox.isChecked()) {
                    editor.putBoolean("rememberPassword", true);
                    editor.apply();
                }

                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Username or password is incorrect!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
