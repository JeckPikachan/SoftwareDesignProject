package com.example.eugene_kachanouski.softwaredesignproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ProfileFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

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

    private void setProfileData(View view) {
        Context context = getActivity();
        ProfileData profileData = ProfileService.getProfileData(context, this);

        ((TextView) view.findViewById(R.id.fullNameTextView)).setText(profileData.firstName + ' ' + profileData.lastName);
        ((TextView) view.findViewById(R.id.emailTextView)).setText(profileData.email);
        ((TextView) view.findViewById(R.id.phoneTextView)).setText(profileData.phone);
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

                viewSwitcher.showNext();
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

        EditText firstNameEditText = view.findViewById(R.id.first_name_edit_text);
        EditText lastNameEditText = view.findViewById(R.id.last_name_edit_text);
        EditText emailEditText = view.findViewById(R.id.email_edit_text);
        EditText phoneEditText = view.findViewById(R.id.phone_edit_text);

        View profileDataEditLayout = view.findViewById(R.id.profile_data_edit_layout);
        ViewGroup.LayoutParams params = profileDataEditLayout.getLayoutParams();

        if (isOn) {
            ProfileData profileData = ProfileService.getProfileData(getContext(), this);

            firstNameEditText.setText(profileData.firstName);
            lastNameEditText.setText(profileData.lastName);
            emailEditText.setText(profileData.email);
            phoneEditText.setText(profileData.phone);

            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {
            ProfileData newProfileData = new ProfileData(
                    firstNameEditText.getText().toString(),
                    lastNameEditText.getText().toString(),
                    emailEditText.getText().toString(),
                    phoneEditText.getText().toString()
            );

            ProfileService.setProfileData(getContext(), this, newProfileData);
            setProfileData(view);

            params.height = 0;
        }

        profileDataEditLayout.setLayoutParams(params);
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
