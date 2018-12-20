package com.example.eugene_kachanouski.softwaredesignproject;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class InfoFragment extends Fragment {

    private final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    private String ALLOW_READ_PHONE_STATE;

    private TextView imeiTextView;

    public InfoFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        // Initializing permission const
        ALLOW_READ_PHONE_STATE = getResources().getString(R.string.allowReadPhoneState);

        // Setting version name to layout
        TextView versionTextView = view.findViewById(R.id.versionTextView);
        String versionName = BuildConfig.VERSION_NAME;
        versionTextView.setText(versionName);

        imeiTextView = view.findViewById(R.id.imeiTextView);

        getPhoneImei();

        return view;
    }

    private void getPhoneImei()
    {
        Context context = getContext();
        final Activity activity = getActivity();
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_PHONE_STATE)) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                dialogBuilder.setMessage(ALLOW_READ_PHONE_STATE);
                dialogBuilder.setTitle("Allow permissions!");
                dialogBuilder.setCancelable(false);
                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.READ_PHONE_STATE},
                                PERMISSIONS_REQUEST_READ_PHONE_STATE);
                    }
                });

                dialogBuilder.show();
            } else {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        PERMISSIONS_REQUEST_READ_PHONE_STATE);
            }

        } else {
            showImei();
        }

    }

    private void showImei()
    {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED)
            return;
        TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        imeiTextView.setText(imei);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showImei();
                } else {
                    imeiTextView.setText(ALLOW_READ_PHONE_STATE);
                }
                return;
            }
        }
    }
}
