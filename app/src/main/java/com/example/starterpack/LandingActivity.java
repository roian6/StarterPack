package com.example.starterpack;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;

public class LandingActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        addSlide(LandingSlide.newInstance(R.layout.activity_landing1));
        addSlide(LandingSlide.newInstance(R.layout.activity_landing2));
        addSlide(LandingSlide.newInstance(R.layout.activity_landing3));

        showSkipButton(true);
        setProgressButtonEnabled(true);

        showSeparator(false);

        setIndicatorColor(getColor(R.color.colorPrimary), getColor(R.color.materialLightGray));
        setImageNextButton(getDrawable(R.drawable.ic_navigate_next_black_24dp));
        setSkipText("Skip");
        setColorSkipButton(getColor(R.color.colorPrimary));
        setSkipTextTypeface(R.font.productb);
        setDoneText("Get Started");
        setColorDoneText(getColor(R.color.colorPrimary));
        setDoneTextTypeface(R.font.productb);

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        Intent intent = new Intent(LandingActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        Intent intent = new Intent(LandingActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
