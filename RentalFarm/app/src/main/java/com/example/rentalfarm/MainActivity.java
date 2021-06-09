package com.example.rentalfarm;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    ListView chat_view;
    ArrayAdapter adapter;
    ArrayList<String> datas = new ArrayList<String>();
    EditText input_msg;
    Timer timer = new Timer();
    TextView contract_date;
    TextView crop_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intents = getIntent();
//        String zone_id = intents.getExtras().getString("zone_id");

        String zone_id = "10";

        final TextView main_label = (TextView) findViewById(R.id.main_label);
        Button button = (Button) findViewById(R.id.setText);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog customDialog = new CustomDialog(MainActivity.this);
//                Intent intent = new Intent(MainActivity.this, CustomDialog.class);
//                intent.putExtra("zone_id", String.valueOf(zone_id));
                customDialog.callFunction(main_label, zone_id);
            }
        });

        ImageButton imageButton = (ImageButton) findViewById(R.id.btn_photo);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Camera.class);
                startActivity(intent);
            }
        });

        status();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                chatting();
            }
        }, 0, 4000);

    }

    public void status() {
        String resultText = "[NULL]";

        try {
            resultText = new Task2().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        contract_date = findViewById(R.id.text2);
        crop_name = findViewById(R.id.value2);

        try {
            JSONObject jObject = new JSONObject(resultText);
            JSONArray jArray = jObject.getJSONArray("zoneInfo");

            JSONObject obj = jArray.getJSONObject(0);
            String cropName = obj.getString("crop_name");
            String contractDate = obj.getString("contract_date");

            crop_name.setText(cropName);
            contract_date.setText("  계약일자 : " + contractDate);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void chatting() {
        String resultText = "[NULL]";

        try {
            resultText = new Task().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        chat_view = findViewById(R.id.chat_view);
        input_msg = findViewById(R.id.input_msg);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, datas);
        datas.clear();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chat_view.setAdapter(adapter);
            }
        });

        try {
            JSONObject jObject = new JSONObject(resultText);
            JSONArray jArray = jObject.getJSONArray("chatInfo");

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject obj = jArray.getJSONObject(i);
                String chat_text = obj.getString("chat_text");
                String chat_img = obj.getString("chat_img");
                String type_sent = obj.getString("type_sent");
                if (chat_img == "null") {
                    String str = type_sent + ": " + chat_text;

                    datas.add(str);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            chat_view.setSelection(datas.size() - 1);
                        }
                    });

                } else if (chat_text == "null") {
                    //이미지출력
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void clickBtn(View view) {

        String zone_id = "10";
        String type_sent = "사용자";
        String chat_type = "text";
        String chat_text = input_msg.getText().toString();
        String date_sent = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());


        //post문 추가
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // URL 원하는 변수명.
                URL url = null;
                try {
                    url = new URL("http://" + LoginActivity.ip + ":3000/farm/zone/chat/");
                    // catch 예외처리
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                HttpURLConnection Connection = null;
                try {
                    Connection = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Connection.setRequestMethod("POST");
                    Connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    Connection.setRequestProperty("Accept", "application/json");
                    Connection.setDoOutput(true);

                } catch (ProtocolException e) {
                    e.printStackTrace();
                }

                JSONObject jsonParam = new JSONObject();
                try {
                    jsonParam.put("zone_id", zone_id);
                    jsonParam.put("type_sent", type_sent);
                    jsonParam.put("chat_type", chat_type);
                    jsonParam.put("chat_text", chat_text);
                    jsonParam.put("date_sent", date_sent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("JSON", jsonParam.toString());

                DataOutputStream os = null;
                try {
                    os = new DataOutputStream(Connection.getOutputStream());
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
                    Log.i("MSG" , Connection.getResponseMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                input_msg.setText("");

            }
        });

    }
}