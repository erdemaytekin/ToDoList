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

public class CreateUser extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        editTextUsername = findViewById(R.id.etKullaniciAdi);
        editTextPassword = findViewById(R.id.etSifre);

        Button btnCreateUser = findViewById(R.id.btnCreateUser);
        btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (validateInputs(username, password)) {
                    // Kullanıcıyı veritabanına kaydetme işlemi
                    ToDoModel user = new ToDoModel();
                    user.setUsername(username);
                    user.setPassword(password);

                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                    db.openDatabase();
                    db.insertUser(user);
                    db.closeDatabase();

                    // Giriş yap ekranına yönlendirme yapılıyor
                    Intent intent = new Intent(CreateUser.this, LoginActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("password", password);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(CreateUser.this, "Geçersiz kullanıcı adı veya şifre", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateInputs(String username, String password) {
        // Girilen kullanıcı adı ve şifre için geçerlilik kontrolü yapabilirsiniz
        return !username.isEmpty() && !password.isEmpty();
    }
}
