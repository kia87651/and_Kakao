package com.example.a405_16.kakao.util;

import android.content.Intent;
import android.net.Uri;

public class email {
    public void sendEmail(String email){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto : " + email));
        intent.putExtra(Intent.EXTRA_EMAIL,email);
        intent.putExtra(Intent.EXTRA_SUBJECT,"Hello");
        intent.putExtra(intent.EXTRA_TEXT,"안녕 !!!");
        // ctx.startActivity(intent.createChooser(intent."example"));
    }
}
