package com.example.rentalfarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView chat_view;
    ArrayAdapter adapter;
    ArrayList<String> datas = new ArrayList<String>();
    EditText input_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView main_label = (TextView) findViewById(R.id.main_label);
        Button button = (Button) findViewById(R.id.setText);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog customDialog = new CustomDialog(MainActivity.this);
                customDialog.callFunction(main_label);
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

        chat_view = findViewById(R.id.chat_view);
        input_msg = findViewById(R.id.input_msg);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, datas);
        chat_view.setAdapter(adapter);
    }

    public void clickBtn(View view) {

        String str = input_msg.getText().toString();

        datas.add(str);

        adapter.notifyDataSetChanged();

        chat_view.setSelection(datas.size()-1);

        input_msg.setText("");
    }
}
