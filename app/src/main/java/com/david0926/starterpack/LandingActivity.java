package com.david0926.starterpack;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;

import com.david0926.starterpack.fragment.LandingFragment;
import com.github.paolorotolo.appintro.AppIntro;
import com.google.firebase.auth.FirebaseAuth;

public class LandingActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        addSlide(LandingFragment.newInstance(R.layout.activity_landing1));
        addSlide(LandingFragment.newInstance(R.layout.activity_landing2));
        addSlide(LandingFragment.newInstance(R.layout.activity_landing3));
        addSlide(LandingFragment.newInstance(R.layout.activity_landing4));

        showSkipButton(true);
        setProgressButtonEnabled(true);

        showSeparator(false);

        setIndicatorColor(getColor(R.color.colorPrimary), getColor(R.color.materialGray5));
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
        finishLanding();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        finishLanding();
    }

    private void finishLanding() {

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(LandingActivity.this, MainActivity.class));
        } else startActivity(new Intent(LandingActivity.this, LoginActivity.class));

        finish();
    }
}
