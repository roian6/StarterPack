package com.david0926.starterpack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import com.bumptech.glide.Glide;
import com.david0926.starterpack.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gun0912.tedimagepicker.builder.TedImagePicker;
import gun0912.tedkeyboardobserver.TedKeyboardObserver;


public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        binding.setActivity(this);

        //scroll to bottom when keyboard up
        new TedKeyboardObserver(this).listen(isShow -> {
            binding.scrollRegi.smoothScrollTo(0, binding.scrollRegi.getBottom());
        });

        //finish activity, when back button pressed
        binding.toolbarRegi.setNavigationOnClickListener(view -> finish());

        //profile edit button clicked
        binding.imgRegiEditprofile.setOnClickListener(view ->
                //start image picker
                TedImagePicker.with(this).showTitle(false).start(this::setProfileImage));

        //sign up button clicked
        binding.btnRegiSignup.setOnClickListener(view -> {

            //keyboard down
            View v = this.getCurrentFocus();
            if (v != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }

            if (TextUtils.isEmpty(binding.getName()) || TextUtils.isEmpty(binding.getEmail())
                    || TextUtils.isEmpty(binding.getPw()) || TextUtils.isEmpty(binding.getPwcheck())) //empty field
                showErrorMsg("Please fill all required fields.");

            else if (!isValidEmail(binding.getEmail())) //invalid email
                showErrorMsg("Please enter a valid email address.");

            else if (!isValidPw(binding.getPw())) //invalid password
                showErrorMsg("Please enter a valid password. (6~24 letters, 0-9 + A-z)");

            else if (!binding.getPw().equals(binding.getPwcheck())) //password confirm failed
                showErrorMsg("Please enter same password in both fields.");

            else if (binding.imgRegiProfile.getDrawable() == null) //profile image not uploaded
                showErrorMsg("Please upload your profile image.");

            else //confirm success
                createAccount(binding.imgRegiProfile.getDrawable(),
                        binding.getName(), binding.getEmail(), binding.getPw());

        });

    }

    private void createAccount(Drawable profile, String name, String email, String pw) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, pw)
                .addOnSuccessListener(this, task -> {

                    Bitmap bitmap = ((BitmapDrawable) profile).getBitmap();
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 10, outputStream);

                    byte[] data = outputStream.toByteArray();

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();
                    StorageReference profileRef = storageRef.child("profile/"+email+".png");

                    UploadTask uploadTask = profileRef.putBytes(data);
                    uploadTask.addOnSuccessListener(snapshot -> {
                        finishSignUp();
                    })
                    .addOnFailureListener(e -> showErrorMsg(e.getLocalizedMessage()));


                })
                .addOnFailureListener(this, e -> showErrorMsg(e.getLocalizedMessage()));

    }

    private void setProfileImage(Uri uri) {
        binding.lottieRegiProfile.setVisibility(View.GONE);
        Glide.with(this).load(uri).into(binding.imgRegiProfile);
    }

    private void finishSignUp() {
        binding.animatorRegi.showNext();
        binding.lottieRegiFinish.playAnimation();
        new Handler().postDelayed(() -> {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        }, binding.lottieRegiFinish.getDuration() + 1000);
    }

    private void showErrorMsg(String msg) {
        binding.txtRegiError.setVisibility(View.VISIBLE);
        binding.txtRegiError.setText(msg);
        binding.txtRegiError.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
    }

    private boolean isValidEmail(String target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private boolean isValidPw(String target) {
        //6~24 letters, 0~9 + A-z
        Pattern p = Pattern.compile("(^.*(?=.{6,24})(?=.*[0-9])(?=.*[A-z]).*$)");
        Matcher m = p.matcher(target);
        //except korean letters
        return m.find() && !target.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*");
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_down_before, R.anim.slide_down);
    }
}
