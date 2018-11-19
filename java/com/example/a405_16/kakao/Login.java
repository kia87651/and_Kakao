package com.example.a405_16.kakao;

import android.content.Context;
import android.content.Intent;
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
                //String ID = etID.getText().toString();
               //String Pass = etPass.getText().toString();
                startActivity(new Intent(ctx,MBR_List.class));
            }
        });
    }
}
