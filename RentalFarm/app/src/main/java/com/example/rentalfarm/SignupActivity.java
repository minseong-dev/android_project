package com.example.rentalfarm;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    /**
     * 로그인 화면 > 회원가입 화면 전환
     * @param savedInstanceState */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        Button BtnSignup2 = (Button) findViewById(R.id.BtnSignup2);
        Button X = (Button) findViewById(R.id.X);
        EditText Signup_EditName = (EditText) findViewById(R.id.Signup_EditName);
        EditText Signup_EditID = (EditText) findViewById(R.id.Signup_EditID);
        EditText Signup_EditPW = (EditText) findViewById(R.id.Signup_EditPW);
        EditText Signup_EditPN = (EditText) findViewById(R.id.Signup_EditPN);
        CheckBox manager = (CheckBox) findViewById(R.id.check1);
        CheckBox user = (CheckBox) findViewById(R.id.check2);
        CheckBox eye = (CheckBox) findViewById(R.id.eye);

        /**
         * 회원가입 화면 > (회원가입 완료 후) 농장 구역 화면 전환
         * putExtra: 회원가입 정보 값 전달 */
        BtnSignup2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, SectionActivity.class);

                intent.putExtra("NewName",Signup_EditName.getText().toString());
                intent.putExtra("NewID",Signup_EditID.getText().toString());
                intent.putExtra("NewPW",Signup_EditPW.getText().toString());
                intent.putExtra("NewPN",Signup_EditPN.getText().toString());
                if(manager.isChecked()){
                    intent.putExtra("manager", manager.isChecked());
                }if(user.isChecked()){
                    intent.putExtra("user",user.isChecked());
                }

                startActivity(intent);
            }
        });

        X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        eye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    Signup_EditPW.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else{
                    Signup_EditPW.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
    }
}
