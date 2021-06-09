package com.example.rentalfarm;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InlineSuggestionsRequest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class CustomDialog extends AppCompatActivity {

    private Context context;

    public CustomDialog(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(final TextView main_label, String zone_id) {
//        Intent intents = getIntent();
//        zone_id = intents.getExtras().getString("zone_id");
        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.custom_dialog);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        //final 삭제
        EditText crop_name = (EditText) dlg.findViewById(R.id.crop_name);
        Button okButton = (Button) dlg.findViewById(R.id.okButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /** '확인' 버튼 클릭시 메인 액티비티에서 설정한 main_label에
                 커스텀 다이얼로그에서 입력한 메시지를 대입한다. */

                main_label.setText(crop_name.getText().toString());
                System.out.println(crop_name.getText().toString());
                Toast.makeText(context, "\"" +  crop_name.getText().toString() + "\" 을 입력하였습니다.", Toast.LENGTH_SHORT).show();

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {

                        // URL 원하는 변수명.
                        URL cropEndpoint = null;
                        try {
                            cropEndpoint = new URL("http://" + LoginActivity.ip + ":3000/farm/zone");
                            // catch 예외처리
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        HttpURLConnection cropConnection = null;
                        try {
                            cropConnection = (HttpURLConnection) cropEndpoint.openConnection();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            cropConnection.setRequestMethod("PATCH");
                            cropConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                            cropConnection.setRequestProperty("Accept", "application/json");
                            cropConnection.setDoOutput(true);
                            cropConnection.setDoInput(true);
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        }

                        JSONObject jsonParam = new JSONObject();
                        try {
                            jsonParam.put("zone_id", zone_id);
                            jsonParam.put("zone_crop_name", crop_name.getText().toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("JSON", jsonParam.toString());

                        DataOutputStream os = null;
                        try {
                            os = new DataOutputStream(cropConnection.getOutputStream());
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
                            if(cropConnection.getResponseCode() == 200) {
                                // 커스텀 다이얼로그를 종료한다.
                                dlg.dismiss();

                            } else {
                                Toast.makeText(context.getApplicationContext(), "다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
                            }
                            Log.i("STATUS", String.valueOf(cropConnection.getResponseCode()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // 잘 연결됐는지 log출력
                        try {
                            Log.i("MSG" , cropConnection.getResponseMessage());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                });

            }
        });
    }
}