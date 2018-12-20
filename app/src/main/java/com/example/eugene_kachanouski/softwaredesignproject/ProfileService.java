package com.example.eugene_kachanouski.softwaredesignproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;

public class ProfileService {

    public static ProfileData getProfileData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.profile_preferences_key), Context.MODE_PRIVATE);
        String firstName = sharedPreferences.getString(context.getString(R.string.profile_first_name_key), context.getString(R.string.default_first_name));
        String lastName = sharedPreferences.getString(context.getString(R.string.profile_last_name_key), context.getString(R.string.default_last_name));
        String email = sharedPreferences.getString(context.getString(R.string.profile_email_key), context.getString(R.string.default_email));
        String phone = sharedPreferences.getString(context.getString(R.string.profile_phone_key), context.getString(R.string.default_phone));
        String fullName = firstName + ' ' + lastName;

        return new ProfileData(firstName, lastName, email, phone);
    }

    public static void setProfileData(Context context, ProfileData profileData) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.profile_preferences_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(context.getString(R.string.profile_first_name_key), profileData.firstName);
        editor.putString(context.getString(R.string.profile_last_name_key), profileData.lastName);
        editor.putString(context.getString(R.string.profile_email_key), profileData.email);
        editor.putString(context.getString(R.string.profile_phone_key), profileData.phone);

        editor.apply();
    }

    public static void saveAvatarImageUri(Context context, Uri uri) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.profile_preferences_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(context.getString(R.string.profile_avatar_uri_key), uri.toString());

        editor.apply();
    }

    public static Uri getAvatarImageUri(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.profile_preferences_key), Context.MODE_PRIVATE);

        return Uri.parse(sharedPreferences.getString(context.getString(R.string.profile_avatar_uri_key), ""));
    }
}
