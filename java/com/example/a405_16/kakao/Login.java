package com.example.a405_16.kakao;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        final Context ctx = Login.this;
        final EditText etID = findViewById(R.id.etID);
        final EditText etPass = findViewById(R.id.etPass);
        final Button btLogin = findViewById(R.id.btLogin);
        final Button btCancel = findViewById(R.id.btCancel);
        findViewById(R.id.btLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etID.getText().length() !=0 &&
                    etPass.getText().length() !=0){
                    String ID = etID.getText().toString();
                    String Pass = etPass.getText().toString();
                    final ItemExist query = new ItemExist(ctx);
                    query.id = ID;
                    query.pw = Pass;
                    new Main.ExecuteService() {
                        @Override
                        public void perfome() {
                            if(query.execute()){
                                startActivity(new Intent(ctx,MBR_List.class));
                            }else{
                                startActivity(new Intent(ctx,MBR_List.class));
                            }
                        }
                    }.perfome();

                }else{
                    startActivity(new Intent(ctx,MBR_List.class));
                }
            }
        });
        findViewById(R.id.btCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    } // onCreate End
    private class LoginQuery extends Main.QueryFactory{
        SQLiteOpenHelper helper;
        public LoginQuery(Context ctx) {
            super(ctx);
            helper = new Main.SqliteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }

    } // LoginQuery End
    private class ItemExist extends LoginQuery{
        String id, pw;
        public ItemExist(Context ctx) {
            super(ctx);
        }
        public boolean execute() {
            String s = String.format(
                    "SELECT * FROM %s "+
                            "WHERE %s LIKE '%s' AND %s LIKE '%s'",
                    DBInfo.MBR_TABLE,DBInfo.MBR_SEQ, id,
                    DBInfo.MBR_PASS, pw);
            return super
                    .getDatabase()
                    .rawQuery(s,null)
                    .moveToNext()
                    ;
        }
    }
}
