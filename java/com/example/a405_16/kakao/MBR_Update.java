package com.example.a405_16.kakao;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MBR_Update extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mbr_update);
        final Context ctx = MBR_Update.this;
        final String[] arr = getIntent().getStringExtra("spec").split("/");
        // m.seq+"/"+m.addr+"/"+m.email+"/"+m.name+"/"+m.pass+"/"+m.photo+"/"+m.phone
        // update 전
        final EditText name = findViewById(R.id.name);
        name.setHint(arr[3]);
        final EditText email = findViewById(R.id.email);
        email.setHint(arr[2]);
        final EditText phone = findViewById(R.id.phone);
        phone.setHint(arr[6]);
        final EditText addr = findViewById(R.id.addr);
        addr.setHint(arr[1]);
        final EditText pass = findViewById(R.id.pass);
        pass.setHint(arr[4]);
        ImageView photo = findViewById(R.id.photo);
        Log.d("프로필사진 :: ",arr[5].toLowerCase());
        photo.setImageDrawable(
                getResources()
                        .getDrawable(
                                getResources()
                                        .getIdentifier(this.getPackageName()+":drawable/"+arr[5].toLowerCase(),null,null), ctx.getTheme()
                        )
        );
        // 수정 이후
        findViewById(R.id.confirmBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Member member = new Member();
                final ItemUpdate query = new ItemUpdate(ctx);
                // member에 값을 넣는데, 만약 EditText 가 NULL이라면
                // 배열에 있는 값이라도 member에 할당해야한다.
                member.setName((name.getText().equals(""))? arr[3] : name.getText().toString());
                query.member = member;
                new Main.ExecuteService() {
                    @Override
                    public void perfome() {
                        query.execute();
                    }
                }.perfome();
            }
        });
        findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    } // onCreat
    private class UpdateQuery extends  Main.QueryFactory{
        SQLiteOpenHelper helper;
        public UpdateQuery(Context ctx) {
            super(ctx);
            this.helper = new Main.SqliteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getWritableDatabase();
        }
    }
    private class ItemUpdate extends UpdateQuery{
        Member member;
        public ItemUpdate(Context ctx) {
            super(ctx);
            member = new Member();
            // 인스턴스 변수는 반드시 생성자 내부에서 초기화한다.
            // 로직은 반드시 에어리어 내부에서 이뤄진다.
            // 에어리어 내부는 CPU를 뜻한다.
            // 필드는 RAM 영역을 뜻한다.
        }
        public void execute(){
            String sql = String.format(
                    "UPDATE %s SET "+
                    " %s = '%s' ," +
                    " %s = '%s' ," +
                    " %s = '%s' ," +
                    " %s = '%s' ," +
                    " %s = '%s' ," +
                    " %s = '%s' " +
                    "WHERE %s LIKE '%s'",
                    DBInfo.MBR_TABLE,
                    DBInfo.MBR_ADDR, member.addr,
                    DBInfo.MBR_EMAIL, member.email,
                    DBInfo.MBR_NAME, member.name,
                    DBInfo.MBR_PASS, member.pass,
                    DBInfo.MBR_PHOTO, member.photo,
                    DBInfo.MBR_PHONE, member.phone,
                    DBInfo.MBR_SEQ, member.seq
                    // m.seq+"/"+m.addr+"/"+m.email+"/"+m.name+"/"+m.pass+"/"+m.photo+"/"+m.phone
            );
            Log.d("SQL ::: ",sql);
            getDatabase().execSQL("");
        }
    }
}