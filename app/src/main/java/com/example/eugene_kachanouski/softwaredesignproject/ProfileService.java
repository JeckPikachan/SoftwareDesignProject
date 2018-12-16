package com.example.eugene_kachanouski.softwaredesignproject;

import android.net.Uri;

import com.google.firebase.database.DatabaseReference;

public class ProfileService {

    private static Profile currentProfile = new Profile("", "", "", "");
    private static DatabaseReference profileRef;
    private static String userId;

    public static void setCurrentProfile(Profile profile) {
        if (profile != null) {
            ProfileService.currentProfile = profile;
        }
    }

    public static void resetCurrentProfile() {
        ProfileService.currentProfile = new Profile("", "", "", "");
        ProfileService.userId = null;
        ProfileService.profileRef = null;
    }

    public static void setUserId(String userId) {
        ProfileService.userId = userId;
    }

    public static void setProfileRef(DatabaseReference ref) {
        ProfileService.profileRef = ref;
    }

    public static ProfileData getProfileData() {
        String firstName = ProfileService.currentProfile.getFirstName();
        String lastName = ProfileService.currentProfile.getLastName();
        String phone = ProfileService.currentProfile.getPhone();
        String email = ProfileService.currentProfile.getEmail();

        return new ProfileData(firstName, lastName, email, phone);
    }

    public static void setProfileData(ProfileData profileData) {

        ProfileService.currentProfile.setFirstName(profileData.firstName);
        ProfileService.currentProfile.setLastName(profileData.lastName);
        ProfileService.currentProfile.setEmail(profileData.email);
        ProfileService.currentProfile.setPhone(profileData.phone);

        ProfileService.profileRef.child(ProfileService.userId == null ? "" : ProfileService.userId).setValue(ProfileService.currentProfile);
    }

    public static void saveAvatarImageUri(Uri uri) {
        ProfileService.currentProfile.setPhoto(uri.toString());
        ProfileService.profileRef.child(ProfileService.userId == null ? "" : ProfileService.userId).setValue(ProfileService.currentProfile);
    }

    public static Uri getAvatarImageUri() {
        return Uri.parse(ProfileService.currentProfile.getPhoto());
    }
}
