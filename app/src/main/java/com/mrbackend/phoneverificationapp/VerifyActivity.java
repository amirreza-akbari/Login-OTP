package com.mrbackend.phoneverificationapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerifyActivity extends AppCompatActivity {

    private static final int SMS_CONSENT_REQUEST = 200;
    EditText edtOtp;
    String phone;
    Intent consentIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        edtOtp = findViewById(R.id.edtOtp);

        phone = getIntent().getStringExtra("phone");
        consentIntent = getIntent().getParcelableExtra("consent_intent");

        sendOtpToServer(phone);

        SmsRetrieverClient client = SmsRetriever.getClient(this);
        client.startSmsUserConsent(null);

        if (consentIntent != null) {
            startActivityForResult(consentIntent, SMS_CONSENT_REQUEST);
        }
    }

    private void sendOtpToServer(String phone) {
        new Thread(() -> {
            try {
                URL url = new URL("https://b.mrbackend.ir/otp/send_otp.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                os.write(("phone=" + phone).getBytes());
                os.flush();
                os.close();

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                StringBuilder res = new StringBuilder();
                while ((line = br.readLine()) != null) res.append(line);

                Log.d("SERVER", res.toString());

            } catch (Exception e) {
                Log.e("ERR", e.getMessage());
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SMS_CONSENT_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                String code = extractOtp(message);
                edtOtp.setText(code);
                Toast.makeText(this, "OTP دریافت شد: "+code, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "رد شد (Deny)", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String extractOtp(String msg) {
        Matcher m = Pattern.compile("\\b\\d{5}\\b").matcher(msg);
        return m.find() ? m.group(0) : "";
    }
}
