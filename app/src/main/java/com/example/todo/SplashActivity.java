    package com.example.todo;


    import androidx.appcompat.app.AppCompatActivity;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;
    import android.content.Intent;
    import android.os.Bundle;
    import android.view.View;
    import android.os.Handler;
    import java.util.ArrayList;
    import java.util.List;
    public class SplashActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash);
            getSupportActionBar().hide();



            final Intent i = new Intent(SplashActivity.this, MainActivity.class);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(i);
                    finish();
                }
            }, 2000);
        }
    }