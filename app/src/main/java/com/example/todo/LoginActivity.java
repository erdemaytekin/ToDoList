package com.example.todo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todo.Model.ToDoModel;
import com.example.todo.Utils.DatabaseHandler;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = findViewById(R.id.etKullaniciAdi);
        editTextPassword = findViewById(R.id.etSifre);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Kullanıcı adı ve şifre doğrulaması burada yapılabilir
                if (validateInputs(username, password)) {
                    // Giriş başarılı olduğunda MainActivity'ye yönlendir
                    Toast.makeText(LoginActivity.this, "Kullanıcı adı ve şifre doğru", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class ));
                    finish(); // LoginActivity'yi kapat
                } else {
                    Toast.makeText(LoginActivity.this, "Geçersiz kullanıcı adı veya şifre", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateInputs(String username, String password) {
        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        dbHandler.openDatabase();

        ToDoModel user = dbHandler.getUser(username, password);

        if (user != null) {
            // Kullanıcı adı ve şifre doğru
            return true;
        } else {
            // Kullanıcı adı veya şifre yanlış
            return false;
        }
    }}


