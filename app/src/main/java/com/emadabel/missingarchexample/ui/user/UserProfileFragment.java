package com.emadabel.missingarchexample.ui.user;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emadabel.missingarchexample.R;
import com.emadabel.missingarchexample.utilities.InjectorUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * A placeholder fragment containing a simple view.
 */
public class UserProfileFragment extends Fragment {

    private static final String ARGUMENT_USER_ID = "ARGUMENT_USER_ID";
    private UserProfileViewModel mViewModel;

    @BindView(R.id.text_display_info)
    TextView mDisplayInfoTextView;

    public UserProfileFragment() {
    }

    public static UserProfileFragment newInstance(String userId) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARGUMENT_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String userId = getArguments().getString(ARGUMENT_USER_ID);
        UserProfileViewModelFactory factory = InjectorUtils.provideUserProfileViewModelFactory();
        mViewModel = ViewModelProviders.of(this, factory).get(UserProfileViewModel.class);
        mViewModel.init(userId);
        mViewModel.getUser().observe(this, user -> {
            Timber.d("displaying data");
            mDisplayInfoTextView.setText(user.getName());
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_profile, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
