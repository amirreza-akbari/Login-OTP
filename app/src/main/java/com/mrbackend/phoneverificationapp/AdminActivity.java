package com.mrbackend.phoneverificationapp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        TextView txtWelcome = findViewById(R.id.txtWelcome);
        txtWelcome.setText("به پنل ادمین خوش آمدید!");
    }
}
