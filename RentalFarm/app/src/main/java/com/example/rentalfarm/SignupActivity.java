package com.example.rentalfarm;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

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
         * 회원가입 화면 > (회원가입 완료 후) 로그인 화면 전환
         * putExtra: 회원가입 정보 값 전달 */
        BtnSignup2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);

                String user_name = Signup_EditName.getText().toString();
                String user_id = Signup_EditID.getText().toString();
                String user_password = Signup_EditPW.getText().toString();
                String user_phone_number = Signup_EditPN.getText().toString();

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        // URL 원하는 변수명.
                        URL singupEndpoint = null;
                        try {
                            singupEndpoint = new URL("http://" + LoginActivity.ip + ":3000/user/signup");
                            // catch 예외처리
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        HttpURLConnection singupConnection = null;
                        try {
                            singupConnection = (HttpURLConnection) singupEndpoint.openConnection();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            singupConnection.setRequestMethod("POST");
                            singupConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                            singupConnection.setRequestProperty("Accept", "application/json");
                            singupConnection.setDoOutput(true);
                            singupConnection.setDoInput(true);
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        }

                        JSONObject jsonParam = new JSONObject();
                        try {
                            jsonParam.put("user_name", user_name);
                            jsonParam.put("user_id", user_id);
                            jsonParam.put("user_password", user_password);
                            jsonParam.put("user_phone_number", user_phone_number);

                            //manager user 구분해서 값 불러오기(수정해야함)
                            if(manager.isChecked()){
                                jsonParam.put("user_type", manager.getText().toString());
                            }if(user.isChecked()){
                                jsonParam.put("user_type", user.getText().toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("JSON", jsonParam.toString());

                        DataOutputStream os = null;
                        try {
                            os = new DataOutputStream(singupConnection.getOutputStream());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            os.writeBytes(jsonParam.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            os.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            os.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            if(singupConnection.getResponseCode() == 200) {
                                // 화면이동.
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
                            }
                            Log.i("STATUS", String.valueOf(singupConnection.getResponseCode()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // 잘 연결됐는지 log출력
                        try {
                            Log.i("MSG" , singupConnection.getResponseMessage());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
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
