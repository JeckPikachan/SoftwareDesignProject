package com.example.eugene_kachanouski.softwaredesignproject;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    private String ALLOW_READ_PHONE_STATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing permission const
        ALLOW_READ_PHONE_STATE = getResources().getString(R.string.allowReadPhoneState);

        // Setting version name to layout
        TextView versionTextView = findViewById(R.id.versionTextView);
        String versionName = BuildConfig.VERSION_NAME;
        versionTextView.setText(versionName);

        // Checking READ_PHONE_STATE permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // Asking for permission
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE
            );
        } else {
            String IMEI = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
            setTextToIMEITextView(IMEI);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            // Processing permission results
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                String IMEI = ALLOW_READ_PHONE_STATE;

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        IMEI = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                    } catch (SecurityException e) {

                    }
                }
                setTextToIMEITextView(IMEI);
            }
        }
    }

    /**
     * Sets passed text to imeiTextView on layout
     * @param text - text to be set
     * @throws SecurityException
     */
    private void setTextToIMEITextView(String text) throws SecurityException {
        TextView IMEITextView = findViewById(R.id.imeiTextView);
        IMEITextView.setText(text);
    }
}
