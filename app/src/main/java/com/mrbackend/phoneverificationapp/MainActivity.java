package com.mrbackend.phoneverificationapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText edtPhone;
    Button btnSendOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtPhone = findViewById(R.id.edtPhone);
        btnSendOtp = findViewById(R.id.btnSendOtp);

        btnSendOtp.setOnClickListener(v -> {
            String phone = edtPhone.getText().toString().trim();
            if (phone.isEmpty()) {
                Toast.makeText(this, "شماره موبایل را وارد کنید", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(MainActivity.this, VerifyActivity.class);
            intent.putExtra("phone", phone);
            startActivity(intent);
        });
    }
}
