package com.emadabel.missingarchexample.ui.main;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.emadabel.missingarchexample.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            MainFragment mainFragment = new MainFragment();

            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.fragment, mainFragment)
                    .commit();
        }
    }
}
