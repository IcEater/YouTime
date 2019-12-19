package com.jnu.youtime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.service.autofill.OnClickAction;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jnu.youtime.data.YouTimeCounter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ListView mainListView;
    ListView navigationListView;
    FloatingActionButton fab;
    ArrayList<YouTimeCounter> Counters;
    DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    private void init() {
        deleteDatabase("youtime_db");

        drawerLayout=findViewById(R.id.mainDrawerLayout);
        mainListView=findViewById(R.id.mainListView);
        navigationListView=findViewById(R.id.navigationListView);
        fab=findViewById(R.id.floatingActionButton);
        Counters=new ArrayList<>();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, addCounterActivity.class);
                intent.putExtra("numOfCounter", Counters.size());
                startActivity(intent);
            }
        });

        addTestTimeCounter();

        MySQLiteOpenHelper dbHelper = new MySQLiteOpenHelper(MainActivity.this,"youtime_db",2);
        SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();

        // 调用SQLiteDatabase对象的query方法进行查询
        // 返回一个Cursor对象：由数据库查询返回的结果集对象

//        Cursor cursor = sqliteDatabase.query("user", new String[] { "id",
//                "name" }, "id=?", new String[] { "1" }, null, null, null);

        Cursor cursor = sqliteDatabase.query("MainList", new String[] {"id", "Time", "Tittle", "Note", "Image"},
                null, new String[] {}, null, null, null);
        // 参数1：（String）表名
        // 参数2：（String[]）要查询的列名
        // 参数3：（String）查询条件
        // 参数4：（String[]）查询条件的参数
        // 参数5：（String）对查询的结果进行分组
        // 参数6：（String）对分组的结果进行限制
        // 参数7：（String）对查询的结果进行排序
        while(cursor.moveToNext()) {
            Counters.add(new YouTimeCounter(
                    cursor.getLong(cursor.getColumnIndex("Time")),
                    cursor.getString(cursor.getColumnIndex("Tittle")),
                    cursor.getString(cursor.getColumnIndex("Note")),
                    bytetoBitmap(cursor.getBlob(cursor.getColumnIndex("Image")))
                    ));
        }
        cursor.close();

        /* set a adapter for the mainListView */
        SimpleAdapter adapterMain=setAdapter(Counters);
        mainListView.setAdapter(adapterMain);
        adapterMain.notifyDataSetChanged();
        /*  */

        /*set a adapter for the navigationListView */
        List<Map<String,Object>> navigationLists;
        navigationLists = new ArrayList<>();
        Map<String,Object> map =new HashMap<>();
        map.put("image", R.mipmap.iceater_icon_home);
        map.put("text", "首页 ");
        navigationLists.add(map);
        map=new HashMap<>();

        map.put("image", R.mipmap.iceater_icon_color);
        map.put("text", "主题色 ");
        navigationLists.add(map);
        map=new HashMap<>();

        map.put("image", R.mipmap.iceater_icon_option);
        map.put("text", "设置 ");
        navigationLists.add(map);

        String []navigationListViewFrom=new String []{"image", "text"};
        int []navigationListViewTo=new int []{R.id.navigationImage, R.id.navigationText};

        SimpleAdapter adapterNavigation=new SimpleAdapter(
                MainActivity.this,
                navigationLists,
                R.layout.navigation_sublayout,
                navigationListViewFrom,
                navigationListViewTo);
        navigationListView.setAdapter(adapterNavigation);
        /*  */

    }

    private SimpleAdapter setAdapter(ArrayList<YouTimeCounter> Counters) {
        List<Map<String,Object>> lists;
        lists = new ArrayList<>();
        for(int i=0; i<Counters.size(); i++)
        {
            Map<String,Object> map =new HashMap<>();
            map.put("image", Counters.get(i).getImage());
            map.put("title", Counters.get(i).getTitle());
            map.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date(Counters.get(i).getTime() * 1000)));
            map.put("note", Counters.get(i).getNote());
            lists.add(map);
        }
        String [] from=new String[]{"image", "title", "time", "note"};
        int[] ids=new int[]{R.id.mainListImage, R.id.mainListTitle, R.id.mainListTime, R.id.mainListNote};
        SimpleAdapter adapter= new SimpleAdapter(MainActivity.this, lists, R.layout.main_sublayout, from, ids);
        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                // TODO Auto-generated method stub
                if(view instanceof ImageView && data instanceof Bitmap){
                    ImageView i = (ImageView)view;
                    i.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });
        return adapter;
    }

    private void addTestTimeCounter() {
        Resources res = getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, R.mipmap.testimage);

        YouTimeCounter test=new YouTimeCounter(1579881600+3600*8, "NEW YEAR", "It is a test", bmp);

        Counters.add(test);
        Counters.add(test);
        Counters.add(test);
        Counters.add(test);
        Counters.add(test);
        Counters.add(test);
        Counters.add(test);
        Counters.add(test);
        Counters.add(test);
        Counters.add(test);
        Counters.add(test);
        Counters.add(test);
        insertToDB(Counters);
        Counters.clear();
    }
    public void insertToDB(ArrayList<YouTimeCounter> list) {
        MySQLiteOpenHelper dbHelper1 = new MySQLiteOpenHelper(MainActivity.this,"youtime_db",2);
        SQLiteDatabase  sqliteDatabase1 = dbHelper1.getWritableDatabase();

        for(int i=0;i <list.size();i ++)
        {
            ContentValues values = new ContentValues();
            values.put("Id", i);
            values.put("Time", Counters.get(i).getTime());
            values.put("Tittle", Counters.get(i).getTitle());
            values.put("Note", Counters.get(i).getNote());
            values.put("Image", bitmapToByte(Counters.get(i).getImage()));
            sqliteDatabase1.insert("MainList", null, values);
        }
        sqliteDatabase1.close();
    }
    public byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
//        int bytes = b.getByteCount();
//        ByteBuffer buffer = ByteBuffer.allocate(bytes);
//        b.copyPixelsToBuffer(buffer); //Move the byte data to the buffer
//        byte[] data = buffer.array(); //Get the bytes array of the bitmap
//        return data;
    }

    public Bitmap bytetoBitmap(byte[] b) {
        Bitmap pic;
        if (b.length != 0) {
            pic=BitmapFactory.decodeByteArray(b, 0, b.length);
            return pic;
        }else return null;
    }
}
