package com.example.eugene_kachanouski.softwaredesignproject;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ProfileFragment extends Fragment {

    String mCurrentPhotoPath;

    private OnFragmentInteractionListener mListener;
    private final int RESULT_LOAD_IMG = 1;
    private final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private final int PERMISSIONS_REQUEST_CAMERA = 2;
    private final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 3;
    private final int GALLERY = 1, CAMERA = 2;
    private Uri photoURI;
    private boolean isEditableLayoutOn = false;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        setProfileData(view);
        setEditButtonOnClickListener(view);
        setChangePhotoLayoutOnClickListener(view);
        setAvatarImage(view);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outBundle) {
        super.onSaveInstanceState(outBundle);

        outBundle.putBoolean("isEditableLayoutOn", isEditableLayoutOn);
        if (isEditableLayoutOn) {
            View view = getView();
            ProfileData profileData = getProfileDataFromEditableLayout(view);

            outBundle.putString("firstName", profileData.firstName);
            outBundle.putString("lastName", profileData.lastName);
            outBundle.putString("email", profileData.email);
            outBundle.putString("phone", profileData.phone);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            isEditableLayoutOn = savedInstanceState.getBoolean("isEditableLayoutOn");

            if (isEditableLayoutOn) {
                ProfileData profileData = new ProfileData(
                        savedInstanceState.getString("firstName"),
                        savedInstanceState.getString("lastName"),
                        savedInstanceState.getString("email"),
                        savedInstanceState.getString("phone")
                );

                View view = getView();

                setEditableLayout(view, true);
                setEditableLayoutData(view, profileData);
            }
        }
    }

    private void setProfileData(View view) {
        ProfileData profileData = ProfileService.getProfileData();

        ((TextView) view.findViewById(R.id.fullNameTextView)).setText(profileData.firstName + ' ' + profileData.lastName);
        ((TextView) view.findViewById(R.id.emailTextView)).setText(profileData.email);
        ((TextView) view.findViewById(R.id.phoneTextView)).setText(profileData.phone);
    }

    private void setChangePhotoLayoutOnClickListener(View view) {
        view.findViewById(R.id.change_photo_layout).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                photoPickerIntent.setType("image/*");
//                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
                showPictureDialog();
            }
        });
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallery();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Context context = getContext();
        final Activity activity = getActivity();
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.CAMERA)) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                dialogBuilder.setMessage("Please allow to use your camera");
                dialogBuilder.setTitle("Allow permissions!");
                dialogBuilder.setCancelable(false);
                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.CAMERA},
                                PERMISSIONS_REQUEST_CAMERA);
                    }
                });

                dialogBuilder.show();
            } else {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.CAMERA},
                        PERMISSIONS_REQUEST_CAMERA);
            }

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                dialogBuilder.setMessage("Please allow to write to your storage");
                dialogBuilder.setTitle("Allow permissions!");
                dialogBuilder.setCancelable(false);
                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                    }
                });

                dialogBuilder.show();
            } else {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        } else {
            dispatchTakePictureIntent();
        }
    }

    private void setAvatarImage(View view) {
        setAvatarImageURI(view, ProfileService.getAvatarImageUri());
    }

    private void setEditButtonOnClickListener(View view) {
        view.findViewById(R.id.editButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                View fragmentView = getView();
                ViewSwitcher viewSwitcher = fragmentView.findViewById(R.id.profile_data_view_switcher);
                View nextView = viewSwitcher.getNextView();

                switch (nextView.getId()) {
                    case R.id.profile_data_view_layout:
                        setEditableLayout(fragmentView, false);
                        break;
                    case R.id.profile_data_edit_layout:
                        setEditableLayout(fragmentView, true);
                        break;
                }
            }
        });
    }

    private void setEditableLayout(View view, boolean isOn) {
        FloatingActionButton fab = view.findViewById(R.id.editButton);
        fab.setImageDrawable(
                isOn
                        ? ContextCompat.getDrawable(getContext(), R.drawable.ic_save_black_24dp)
                        : ContextCompat.getDrawable(getContext(), R.drawable.ic_edit_black_24dp)
        );

        View profileDataEditLayout = view.findViewById(R.id.profile_data_edit_layout);
        ViewGroup.LayoutParams params = profileDataEditLayout.getLayoutParams();

        View changePhotoLayout = view.findViewById(R.id.change_photo_layout);

        ViewSwitcher viewSwitcher = view.findViewById(R.id.profile_data_view_switcher);


        if (isOn) {
            ProfileData profileData = ProfileService.getProfileData();

            setEditableLayoutData(view, profileData);

            changePhotoLayout.setVisibility(View.VISIBLE);

            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            if (viewSwitcher.getNextView().getId() == R.id.profile_data_edit_layout) {
                viewSwitcher.showNext();
            }
        } else {
            ProfileData newProfileData = getProfileDataFromEditableLayout(view);

            ProfileService.setProfileData(newProfileData);

            setProfileData(view);

            changePhotoLayout.setVisibility(View.GONE);

            params.height = 0;

            if (viewSwitcher.getNextView().getId() == R.id.profile_data_view_layout) {
                viewSwitcher.showNext();
            }
        }

        isEditableLayoutOn = isOn;

        profileDataEditLayout.setLayoutParams(params);
    }

    private void setEditableLayoutData(View view, ProfileData profileData) {
        EditText firstNameEditText = view.findViewById(R.id.first_name_edit_text);
        EditText lastNameEditText = view.findViewById(R.id.last_name_edit_text);
        EditText emailEditText = view.findViewById(R.id.email_edit_text);
        EditText phoneEditText = view.findViewById(R.id.phone_edit_text);

        firstNameEditText.setText(profileData.firstName);
        lastNameEditText.setText(profileData.lastName);
        emailEditText.setText(profileData.email);
        phoneEditText.setText(profileData.phone);
    }

    private ProfileData getProfileDataFromEditableLayout(View view) {
        EditText firstNameEditText = view.findViewById(R.id.first_name_edit_text);
        EditText lastNameEditText = view.findViewById(R.id.last_name_edit_text);
        EditText emailEditText = view.findViewById(R.id.email_edit_text);
        EditText phoneEditText = view.findViewById(R.id.phone_edit_text);

        return new ProfileData(
                firstNameEditText.getText().toString(),
                lastNameEditText.getText().toString(),
                emailEditText.getText().toString(),
                phoneEditText.getText().toString()
        );
    }

    private void setAvatarImageURI(View view, Uri uri) {
        Context context = getContext();
        final Activity activity = getActivity();
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                dialogBuilder.setMessage("Please allow to read your images from gallery");
                dialogBuilder.setTitle("Allow permissions!");
                dialogBuilder.setCancelable(false);
                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });

                dialogBuilder.show();
            } else {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }

        } else {
            try {
                ImageView avatar = view.findViewById(R.id.avatar_image_view);
                InputStream imageStream = getActivity().getContentResolver().openInputStream(uri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                avatar.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == GALLERY) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                setAvatarImageURI(getView(), uri);
                ProfileService.saveAvatarImageUri(uri);
            }
        }

        if (requestCode == CAMERA) {
            if (resultCode == RESULT_OK) {
                Uri uri = photoURI;
                setAvatarImageURI(getView(), uri);
                ProfileService.saveAvatarImageUri(uri);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA);
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
