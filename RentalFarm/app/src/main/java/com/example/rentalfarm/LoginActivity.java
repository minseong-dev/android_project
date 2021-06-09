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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    // 와이파이 변경 시 ip주소 바꿔줄 것.
    static String ip = "192.168.0.73";

    // loginInfo
    private String str, receiveMsg;
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
                String user_id = EditID.getText().toString();
                String user_password = EditPW.getText().toString();

                Intent intent = new Intent(LoginActivity.this, SectionActivity.class);
                Intent intent2 = new Intent(LoginActivity.this, MainActivity.class);
                // 서버와 통신하는 부분 시작, 통신이 필요한 클래스 안에 작성.
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        // 로그인 URL
                        URL loginEndpoint = null;
                        try {
                            loginEndpoint = new URL("http://" + ip + ":3000/user/login");
                            // catch 예외처리
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        HttpURLConnection loginConnection = null;
                        try {
                            loginConnection = (HttpURLConnection) loginEndpoint.openConnection();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            loginConnection.setRequestMethod("POST");
                            loginConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                            loginConnection.setRequestProperty("Accept", "application/json");
                            loginConnection.setDoOutput(true);
                            loginConnection.setDoInput(true);
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        }

                        JSONObject jsonParam = new JSONObject();
                        try {
                            jsonParam.put("user_id", user_id);
                            jsonParam.put("user_password", user_password);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("JSON", jsonParam.toString());

                        DataOutputStream os = null;
                        try {
                            os = new DataOutputStream(loginConnection.getOutputStream());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            os.write(jsonParam.toString().getBytes("UTF-8"));
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
                            if(loginConnection.getResponseCode() == 200) {
                                // 다음화면으로 넘겨줄 값.
                                intent.putExtra("ID",user_id);
                                intent.putExtra("PW",user_password);
                            } else {
                                Toast.makeText(getApplicationContext(), "다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
                            }
                            Log.i("STATUS", String.valueOf(loginConnection.getResponseCode()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // 잘 연결됐는지 log출력
                        try {
                            Log.i("MSG" , loginConnection.getResponseMessage());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            if(loginConnection.getResponseCode() == 200) {
                                InputStream responseBody = loginConnection.getInputStream();
                                InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                                BufferedReader bufferedReader = new BufferedReader(responseBodyReader);
                                StringBuilder buffer = new StringBuilder();
                                while ((str = bufferedReader.readLine()) != null) {
                                    buffer.append(str);
                                }

                                receiveMsg = buffer.toString();
                                Log.i("receiveMsg : ", receiveMsg);
                                bufferedReader.close();

                                try {
                                    JSONObject jObject = new JSONObject(receiveMsg);
                                    String userType = jObject.getString("userType");
                                    Log.i("userType : ", userType);

                                    intent.putExtra("userType", userType);

                                    if(userType.equals("사용자")) {
                                        startActivity(intent2);
                                    } else {
                                        startActivity(intent);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                Log.i("결과", loginConnection.getResponseCode() + "Error");
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Toast.makeText(getApplicationContext(), "로그인이 되었습니다.", Toast.LENGTH_SHORT).show();
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