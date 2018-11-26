package com.example.a405_16.kakao;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.a405_16.kakao.util.Album;
import com.example.a405_16.kakao.util.Phone;



public class MBR_Detail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mbr_detail);
        final Context ctx = MBR_Detail.this;
        Intent intent = this.getIntent();
        String seq = intent.getExtras().getString("seq");
        final ItemDetail query = new ItemDetail(ctx);
        query.seq = seq;
        Member m = (Member)new Main.ObjectService() {
            @Override
            public Object perfome() {
                return query.execute();
            }
        }.perfome();
        // 선택한 멤버 정보를 로그로 출력하기
        Log.d("선택한 멤버 정보",m.toString());
        final String spec = m.seq+"/"+m.addr+"/"+m.email+"/"+m.name+"/"+m.pass+"/"+m.photo+"/"+m.phone;
        findViewById(R.id.updateBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(ctx,MBR_Update.class);
               intent.putExtra("spec",spec);
               startActivity(intent);
            }
        });
        findViewById(R.id.callBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Phone phone = new Phone(ctx,MBR_Detail.this);
                phone.setPhoneNum(m.);
                phone.directCall();
            }
        });
        findViewById(R.id.dialBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Phone phone = new Phone(ctx,MBR_Detail.this);
                phone.setPhoneNum(m.phone);
                phone.dial();
            }
        });
        findViewById(R.id.smsBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.emailBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // email.sendEmail("kia87651@naver.com")
            }
        });
        findViewById(R.id.albumBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx,Album.class));
            }
        });
        findViewById(R.id.movieBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.mapBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.musicBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.listBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    } // onCreate End
    // database 영역
    public class DetailQuery extends Main.QueryFactory {
        Main.SqliteHelper helper;
        public DetailQuery(Context ctx) {
            super(ctx);
            helper = new Main.SqliteHelper(ctx);
        }
        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }

    }
    public class ItemDetail extends DetailQuery {
        String seq;
        public ItemDetail(Context ctx) {
            super(ctx);
        }
        public Member execute() {
            Member m = null;
            Cursor c = this.getDatabase()
                    .rawQuery(String.format(" SELECT * FROM %s " +
                            " WHERE %s LIKE '%s'",DBInfo.MBR_TABLE,
                            DBInfo.MBR_SEQ, seq),null);
            if (c != null) {
                if (c.moveToNext()) {
                    m = new Member();
                    m.setSeq(Integer.parseInt(c.getString(c.getColumnIndex(DBInfo.MBR_SEQ))));
                    m.setName(c.getString(c.getColumnIndex(DBInfo.MBR_NAME)));
                    m.setAddr(c.getString(c.getColumnIndex(DBInfo.MBR_ADDR)));
                    m.setEmail(c.getString(c.getColumnIndex(DBInfo.MBR_EMAIL)));
                    m.setPass(c.getString(c.getColumnIndex(DBInfo.MBR_PASS)));
                    m.setPhone(c.getString(c.getColumnIndex(DBInfo.MBR_PHONE)));
                    m.setPhoto(c.getString(c.getColumnIndex(DBInfo.MBR_PHOTO)));
                    Log.d("검색된 회원은", m.getName());
                }

            } else {
                Log.d("검색된 회원은", "없음");
            }
            return m;
        }
    }
}
