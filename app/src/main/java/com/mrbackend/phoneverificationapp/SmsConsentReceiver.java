package com.mrbackend.phoneverificationapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

public class SmsConsentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {

            Bundle extras = intent.getExtras();
            if (extras == null) return;

            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

            switch (status.getStatusCode()) {

                case CommonStatusCodes.SUCCESS:
                    // پیامک رسید → گوگل Allow/Deny را باز می‌کند
                    Intent consentIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                    if (consentIntent != null) {
                        Intent i = new Intent(context, VerifyActivity.class);
                        i.putExtra("consent_intent", consentIntent);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                    break;

                case CommonStatusCodes.TIMEOUT:
                    Log.e("SMS", "Timeout دریافت پیامک");
                    break;
            }
        }
    }
}
