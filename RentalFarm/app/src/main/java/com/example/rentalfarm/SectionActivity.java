package com.example.rentalfarm;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import static com.example.rentalfarm.LoginActivity.ip;

public class SectionActivity extends AppCompatActivity {

    // farm1~9 버튼 배열로 처리
    private Button[] farm = new Button[9];
    Integer[] farmIDs = {R.id.farm1, R.id.farm2, R.id.farm3, R.id.farm4, R.id.farm5, R.id.farm6, R.id.farm7, R.id.farm8, R.id.farm9};
    int i;

    /**
     * 회원가입&로그인 화면 > 농장 구역 화면 전환
     * getExtras(): 로그인 화면 ID, PW 값 전달 받음
     * @param savedInstanceState */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.farmsection);

//        Intent intent = getIntent();
//        String ID = intent.getExtras().getString("ID");
//        String PW = intent.getExtras().getString("PW");

        // 배열을 이용해 구역 ID 등록
        for(i=0;i<farm.length;i++){
            farm[i] = (Button)findViewById(farmIDs[i]);
            registerForContextMenu(farm[i]);
            Button zone_id = farm[i];
            /**
             * 농장 구역 화면 > 채팅창 화면 전환
             **/
            farm[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SectionActivity.this, MainActivity.class);
                    intent.putExtra("zone_id", String.valueOf(zone_id));
                    startActivity(intent);
                }
            });
        }
    }

    // 농장 구역 꾹 누르면 Context Menu 뜨게 설정
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater mInflater = getMenuInflater();

        for(i=0;i<farm.length;i++){
            if(v == farm[i]){
                menu.setHeaderTitle("선택하세요.");
                mInflater.inflate(R.menu.context_menu, menu);
            }
        }
    }

    // Context Menu 선택 시 화면 작동
    public boolean onContextItemSelected(MenuItem item){
        Intent intent = new Intent(SectionActivity.this, InformationActivity.class);
        switch (item.getItemId()){
            case R.id.update:
                // InformationActivity에 구역 ID 넘겨주기
                for(i=0;i<farm.length;i++) {
                    farm[i] = (Button) findViewById(farmIDs[i]);
                    //registerForContextMenu(farm[i]);
                    Button zone_id = farm[i];
                    intent.putExtra("zone_id", String.valueOf(zone_id));
                }
                startActivity(intent);
                return true;
            case R.id.change:
                Intent intent2 = new Intent(SectionActivity.this, InformationChangeActivity.class);
                // InformationActivity 에 구역 ID 넘겨주기
                for(i=0;i<farm.length;i++) {
                    farm[i] = (Button) findViewById(farmIDs[i]);
                    //registerForContextMenu(farm[i]);
                    Button zone_id = farm[i];
                    intent.putExtra("zone_id", String.valueOf(zone_id));
                }
                startActivity(intent2);
                return true;
            case R.id.delete:
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        // URL 원하는 변수명.
                        URL deleteEndpoint = null;
                        try {
                            deleteEndpoint = new URL("http://" + ip + ":3000/user/farm/");
                            // catch 예외처리
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        HttpURLConnection deleteConnection = null;
                        try {
                            deleteConnection = (HttpURLConnection) deleteEndpoint.openConnection();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            deleteConnection.setRequestMethod("DELETE");
                            deleteConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                            deleteConnection.setRequestProperty("Accept", "application/json");
                            deleteConnection.setDoOutput(true);
                            deleteConnection.setDoInput(true);
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        }
                        JSONObject jsonParam = new JSONObject();
                        try {
                            jsonParam.put("zone_id[i]", farmIDs);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("JSON", jsonParam.toString());
                        DataOutputStream os = null;
                        try {
                            os = new DataOutputStream(deleteConnection.getOutputStream());
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
                            intent.putExtra("zone_id", farmIDs);
                            Log.i("STATUS", String.valueOf(deleteConnection.getResponseCode()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // 잘 삭제됐는지 log 출력
                        try {
                            Log.i("MSG", deleteConnection.getResponseMessage());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}