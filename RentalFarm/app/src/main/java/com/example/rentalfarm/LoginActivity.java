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

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button BtnSignup1 = (Button) findViewById(R.id.BtnSignup1);
        Button BtnLogin = (Button) findViewById(R.id.BtnLogin);
        EditText EditID = (EditText) findViewById(R.id.EditID);
        EditText EditPW = (EditText) findViewById(R.id.EditPW);
        CheckBox eye = (CheckBox) findViewById(R.id.eye);

        /**
         * 로그인 화면 > 회원가입 화면 전환 */
        BtnSignup1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 로그인 화면 > 농장구역 화면 전환
         * putExtra: 로그인 창 ID, PW 입력한 값 전달 */
        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SectionActivity.class);

                intent.putExtra("ID",EditID.getText().toString());
                intent.putExtra("PW",EditPW.getText().toString());

                startActivity(intent);

            }
        });

        eye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    EditPW.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else{
                    EditPW.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}