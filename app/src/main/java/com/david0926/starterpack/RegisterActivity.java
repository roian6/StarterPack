package com.david0926.starterpack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.david0926.starterpack.databinding.ActivityRegisterBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        binding.setActivity(this);

        //finish activity, when back button pressed
        binding.toolbarRegi.setNavigationOnClickListener(view -> finish());

        binding.btnRegiSignup.setOnClickListener(view -> {

            String fieldResult = confirmField();
            if(fieldResult.equals("success")){
                binding.animatorRegi.showNext();
            }
            else{
                binding.txtRegiError.setVisibility(View.VISIBLE);
                binding.txtRegiError.setText(fieldResult);
                binding.txtRegiError.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            }

        });

    }

    private String confirmField() {

        if (TextUtils.isEmpty(binding.getName()) || TextUtils.isEmpty(binding.getEmail())
                || TextUtils.isEmpty(binding.getPw()) || TextUtils.isEmpty(binding.getPwcheck())) //empty field
            return "Please fill in all required fields.";

        if (!isValidEmail(binding.getEmail())) //invalid email
            return "Please enter a valid email address.";

        if (!isValidPw(binding.getPw())) //invalid password
            return "Please enter a valid password. (6~24 letters, 0-9 + A-z)";

        if (!binding.getPw().equals(binding.getPwcheck())) //password confirm failed
            return "Please enter the same password in both fields.";

        return "success";

    }

    private boolean isValidEmail(String target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private boolean isValidPw(String target) {
        //6~24 letters, 0~9 + A-z
        Pattern p = Pattern.compile("(^.*(?=.{6,24})(?=.*[0-9])(?=.*[A-z]).*$)");
        Matcher m = p.matcher(target);
        //not including korean letters
        return m.find() && !target.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*");
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_down_before, R.anim.slide_down);
    }
}
