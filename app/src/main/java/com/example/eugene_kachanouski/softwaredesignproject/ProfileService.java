package com.example.eugene_kachanouski.softwaredesignproject;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.fragment.app.Fragment;

public class ProfileService {

    public static ProfileData getProfileData(Context context, Fragment fragment) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fragment.getString(R.string.profile_preferences_key), Context.MODE_PRIVATE);
        String firstName = sharedPreferences.getString(fragment.getString(R.string.profile_first_name_key), fragment.getString(R.string.default_first_name));
        String lastName = sharedPreferences.getString(fragment.getString(R.string.profile_last_name_key), fragment.getString(R.string.default_last_name));
        String email = sharedPreferences.getString(fragment.getString(R.string.profile_email_key), fragment.getString(R.string.default_email));
        String phone = sharedPreferences.getString(fragment.getString(R.string.profile_phone_key), fragment.getString(R.string.default_phone));
        String fullName = firstName + ' ' + lastName;

        return new ProfileData(firstName, lastName, email, phone);
    }

    public static void setProfileData(Context context, Fragment fragment, ProfileData profileData) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                fragment.getString(R.string.profile_preferences_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(fragment.getString(R.string.profile_first_name_key), profileData.firstName);
        editor.putString(fragment.getString(R.string.profile_last_name_key), profileData.lastName);
        editor.putString(fragment.getString(R.string.profile_email_key), profileData.email);
        editor.putString(fragment.getString(R.string.profile_phone_key), profileData.phone);

        editor.apply();
    }
}
