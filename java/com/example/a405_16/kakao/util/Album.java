package com.example.a405_16.kakao.util;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.a405_16.kakao.R;

public class Album extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album);
        final Context ctx = Album.this;
        GridView myAlbum = findViewById(R.id.myalbum);
        String[] arr = {"photo_1","photo_2","photo_3","photo_4","photo_5"};
        myAlbum.setAdapter(new  Picture(ctx,arr));
    }
    public class Picture extends BaseAdapter {
        private Context ctx;
        private String[] pictures;

        public Picture(Context ctx, String[] picture) {
            this.ctx = ctx;
            this.pictures = picture;
        }

        @Override
        public int getCount() {
            return pictures.length;
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View v, ViewGroup p) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService
                    (ctx.LAYOUT_INFLATER_SERVICE);
            View gridview;
            if(v==null){
                gridview = new GridView(ctx);
                gridview = inflater.inflate(R.layout.picture,null);
                ImageView iv = gridview.findViewById(R.id.photo);
                String ph = pictures[i];
                switch (ph){
                    case "photo_1" : iv.setImageResource(R.drawable.photo_1);break;
                    case "photo_2" : iv.setImageResource(R.drawable.photo_2);break;
                    case "photo_3" : iv.setImageResource(R.drawable.photo_3);break;
                    case "photo_4" : iv.setImageResource(R.drawable.photo_4);break;
                    case "photo_5" : iv.setImageResource(R.drawable.photo_5);break;
                }
            }
            return null;
        }
    }
}
