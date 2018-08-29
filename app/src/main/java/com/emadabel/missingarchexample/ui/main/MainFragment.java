package com.emadabel.missingarchexample.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.emadabel.missingarchexample.R;
import com.emadabel.missingarchexample.ui.user.UserProfileFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFragment extends Fragment {

    @BindView(R.id.text_user_id)
    EditText mUserIdEditText;
    @BindView(R.id.button_get_user)
    Button mGetUserButton;

    public MainFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUserIdEditText.setOnEditorActionListener(this::onEditorAction);
        mGetUserButton.setOnClickListener(this::onClick);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    private boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            performSearch();
            return true;
        }
        return false;
    }

    private void onClick(View v) {
        performSearch();
    }

    private void performSearch() {
        if (!TextUtils.isEmpty(mUserIdEditText.getText())) {
            UserProfileFragment userProfileFragment;
            userProfileFragment = UserProfileFragment.newInstance(mUserIdEditText.getText().toString());

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.fragment, userProfileFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            Toast.makeText(getContext(), "You did not provide a user id", Toast.LENGTH_SHORT).show();
        }
    }
}
