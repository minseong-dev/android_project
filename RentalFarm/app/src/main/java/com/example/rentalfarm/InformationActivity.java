package com.example.rentalfarm;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.Calendar;

public class InformationActivity extends AppCompatActivity {

    DatePickerDialog datePickerDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.farmchange);

        // SectionActivity의 구역 id가져오기
        Intent intents = getIntent();
        String zone_id = intents.getExtras().getString("zone_id");

        Button endChange = (Button) findViewById(R.id.endChange);
        EditText Edit_farmName = (EditText) findViewById(R.id.Edit_farmName);
        EditText Edit_userID = (EditText) findViewById(R.id.Edit_userID);
        Button changeDate1 = (Button) findViewById(R.id.changeDate1);
        Button changeDate2 = (Button) findViewById(R.id.changeDate2);
        Button back = (Button) findViewById(R.id.back);

        // 계약 일자 수정 버튼 클릭 시 날짜 설정 다이얼로그 띄우기 1
        changeDate1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (view == changeDate1) {
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(changeDate1.getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            changeDate1.setText(year + " / " + (monthOfYear + 1) + " / " + dayOfMonth);
                        }
                    },mYear,mMonth,mDay);
                    datePickerDialog.show();
                }
            }
        });
        changeDate2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (view == changeDate2) {
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(changeDate2.getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            changeDate2.setText(year + " / " + (monthOfYear + 1) + " / " + dayOfMonth);
                        }
                    },mYear,mMonth,mDay);
                    datePickerDialog.show();
                }
            }
        });

        /**
         * 농장 구역 정보 화면 > 농장 구역 화면 전환, 토스트 메시지
         * EditText 값 저장 > 농장 구역 정보 값 넘겨받을 곳은 모르겠어서 그냥 뒀음!
         */
        endChange.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                Intent intent = new Intent(InformationActivity.this, SectionActivity.class);
                Toast.makeText(getApplicationContext(), "완료되었습니다.", Toast.LENGTH_SHORT).show();

//                intent.putExtra("NewFarmName", Edit_farmName.getText().toString());
//                intent.putExtra("NewAppointment1", changeDate1.getText().toString());
//                intent.putExtra("NewAppointment1", changeDate2.getText().toString());
//                intent.putExtra("NewUserID", Edit_userID.getText().toString());

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        // URL 원하는 변수명.
                        URL httpEndpoint = null;
                        try {
                            httpEndpoint = new URL("http://" + LoginActivity.ip + ":3000/farm/");
                            // catch 예외처리
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        HttpURLConnection myConnection = null;
                        try {
                            myConnection = (HttpURLConnection) httpEndpoint.openConnection();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            myConnection.setRequestMethod("POST");
                            myConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                            myConnection.setRequestProperty("Accept", "application/json");
                            myConnection.setDoOutput(true);
                            myConnection.setDoInput(true);
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        }

                        JSONObject jsonParam = new JSONObject();
                        try {
                            jsonParam.put("zone_name", Edit_farmName.getText().toString());
                            jsonParam.put("zone_contract_date", changeDate1.getText().toString() + '~' +changeDate2.getText().toString());
                            jsonParam.put("zone_user_id", Edit_userID.getText().toString());
                            jsonParam.put("farm_id", 1);
                            jsonParam.put("zone_id", zone_id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("JSON", jsonParam.toString());

                        DataOutputStream os = null;
                        try {
                            os = new DataOutputStream(myConnection.getOutputStream());
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
                            if(myConnection.getResponseCode() == 200) {

                                // 화면이동.
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
                            }
                            Log.i("STATUS", String.valueOf(myConnection.getResponseCode()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // 잘 연결됐는지 log출력
                        try {
                            Log.i("MSG" , myConnection.getResponseMessage());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InformationActivity.this, SectionActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}