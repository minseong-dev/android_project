package com.example.rentalfarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

        Intent intent = getIntent();
        String ID = intent.getExtras().getString("ID");
        String PW = intent.getExtras().getString("PW");

        // 배열을 이용해 구역 ID 등록
        for(i=0;i<farm.length;i++){
            farm[i] = (Button)findViewById(farmIDs[i]);
            registerForContextMenu(farm[i]);

            /**
             * 농장 구역 화면 > 채팅창 화면 전환
             **/
            farm[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SectionActivity.this, MainActivity.class);
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
        switch (item.getItemId()){
            case R.id.update:
            case R.id.change:
                Intent intent = new Intent(SectionActivity.this, InformationActivity.class);
                startActivity(intent);
                return true;
            case R.id.delete:
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