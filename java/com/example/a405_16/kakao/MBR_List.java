package com.example.a405_16.kakao;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MBR_List extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mbr_list);
        final Context ctx = MBR_List.this;
        final ListView mbrList = findViewById(R.id.mbrList);
        final ItemList query = new ItemList(ctx);

        final ArrayList<Member> mlist = (ArrayList<Member>) new Main.ListService() {
            @Override
            public List<?> perfome() {
                return query.execute();
            }
        }.perfome();
        mbrList.setAdapter(new MemberAdapter(ctx,mlist));
        findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx, MBR_Detail.class));
            }
        });
        // detail 처리
        mbrList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> p, View v, int i, long l) {
                Member m = (Member)mbrList.getItemAtPosition(i);
                Log.d("선택한 ID : ", m.seq+"");
                Intent intent = new Intent(ctx, MBR_Detail.class);
                intent.putExtra("seq",m.seq+"");
                startActivity(intent);
            }
        });
        // delete 처리
        mbrList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> p, View v, int i, long l) {
                Member m = (Member) mbrList.getItemAtPosition(i);
                new AlertDialog.Builder(ctx)
                        .setTitle("삭 제")
                        .setMessage("정말 삭제하시겠습니까?")
                        .setPositiveButton(
                                android.R.string.yes,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 삭제 쿼리 실행
                                        Toast.makeText(ctx, "삭제완료",Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(ctx,MBR_List.class));
                                    }
                                }
                        )
                        .setNegativeButton(
                                android.R.string.no,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(ctx,"삭제 취소",Toast.LENGTH_LONG).show();
                                    }
                                }
                        ).show();

                return false;
            }
        });

        findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx,MBR_Add.class));
            }
        });

        // onCreate End
    }

    private class ListQuery extends Main.QueryFactory {
        SQLiteOpenHelper helper;

        public ListQuery(Context ctx) {
            super(ctx);
            helper = new Main.SqliteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }

    private class ItemList extends ListQuery {

        public ItemList(Context ctx) {
            super(ctx);
        }

        public ArrayList<Member> execute() {
            ArrayList<Member> ls = new ArrayList<>();
            Cursor c = this.getDatabase().rawQuery(
                    "SELECT * FROM MEMBER", null
            );
            Member m = null;
            if (c != null) {
                while (c.moveToNext()) {
                    m = new Member();
                    m.setSeq(Integer.parseInt(c.getString(c.getColumnIndex(DBInfo.MBR_SEQ))));
                    m.setName(c.getString(c.getColumnIndex(DBInfo.MBR_NAME)));
                    m.setAddr(c.getString(c.getColumnIndex(DBInfo.MBR_ADDR)));
                    m.setEmail(c.getString(c.getColumnIndex(DBInfo.MBR_EMAIL)));
                    m.setPass(c.getString(c.getColumnIndex(DBInfo.MBR_PASS)));
                    m.setPhone(c.getString(c.getColumnIndex(DBInfo.MBR_PHONE)));
                    m.setPhoto(c.getString(c.getColumnIndex(DBInfo.MBR_PHOTO)));
                    ls.add(m);
                    Log.d("등록된 회원수는", ls.size() + "");
                }

            } else {
                Log.d("등록된 회원은", "없음");
            }
            return ls;
        }
    }

    // Item 관련 파트
    private class MemberAdapter extends BaseAdapter {
        ArrayList<Member> ls;
        Context ctx;
        LayoutInflater inflater;

        public MemberAdapter(Context ctx, ArrayList<Member> ls) {
            this.ctx = ctx;
            this.ls = ls;
            this.inflater = LayoutInflater.from(ctx);
        }

        @Override
        public int getCount() {
            return ls.size();
        }

        @Override
        public Object getItem(int i) {
            return ls.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View v, ViewGroup g) {
            ViewHolder holder;
            if (v == null) {
                v = inflater.inflate(R.layout.mbr_item, null);
                holder = new ViewHolder();
                holder.photo = v.findViewById(R.id.photo);
                holder.name = v.findViewById(R.id.name);
                holder.phone = v.findViewById(R.id.phone);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }
            holder.name.setText(ls.get(i).getName());
            holder.phone.setText(ls.get(i).getPhone());
            // photo 불러오는 코드
            final ItemPhoto query = new ItemPhoto(ctx);
            query.seq = ls.get(i).seq + "";
            String s = ((String) new Main.ObjectService() {
                @Override
                public Object perfome() {
                    return query.execute();
                }
            }.perfome()).toLowerCase();
            Log.d("파일명 : ", s);
            holder.photo
                    .setImageDrawable(getResources().getDrawable(
                            getResources().getIdentifier(ctx.getPackageName()+":drawable/"+s.toLowerCase()
                                    ,null,null),ctx.getTheme()));
            return v;
        }
    }

    static class ViewHolder {
        ImageView photo;
        TextView name, phone;
    }

    private class PhotoQuery extends Main.QueryFactory {
        Main.SqliteHelper helper;

        public PhotoQuery(Context ctx) {
            super(ctx);
            helper = new Main.SqliteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }
    private class ItemPhoto extends PhotoQuery{
        String seq;
        public ItemPhoto(Context ctx) {
            super(ctx);
        }
        public String execute(){
            Cursor c= getDatabase()
                    .rawQuery(String.format(
                            " SELECT %s FROM %s WHERE %s LIKE '%s' ",
                            DBInfo.MBR_PHOTO,
                            DBInfo.MBR_TABLE,
                            DBInfo.MBR_SEQ,
                            seq
                    ),null);
            String result = "";
            if(c!= null){
                if(c.moveToNext()){
                    result = c.getString(c.getColumnIndex(DBInfo.MBR_PHOTO));
                }
            }
            return result;
        }
    }

}
