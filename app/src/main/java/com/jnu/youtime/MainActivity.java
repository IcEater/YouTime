package com.jnu.youtime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jnu.youtime.data.YouTimeCounter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView mainListView;
    ListView navigationListview;
    ImageButton imageButton;
    FloatingActionButton FAB;
    ArrayList<YouTimeCounter> Counters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        init();
    }
    private void init()
    {
        deleteDatabase("youtime_db");

        mainListView=findViewById(R.id.mainListView);
        navigationListview=findViewById(R.id.navigationListView);
        imageButton=findViewById(R.id.imageButton);
        FAB=findViewById(R.id.floatingActionButton);
        Counters=new ArrayList<>();

        addTestTimeCounter();

        MySQLiteOpenHelper dbHelper = new MySQLiteOpenHelper(MainActivity.this,"youtime_db",2);

        SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();

        // 调用SQLiteDatabase对象的query方法进行查询
        // 返回一个Cursor对象：由数据库查询返回的结果集对象

//        Cursor cursor = sqliteDatabase.query("user", new String[] { "id",
//                "name" }, "id=?", new String[] { "1" }, null, null, null);

        Cursor cursor = sqliteDatabase.query("user", new String[] {"id"},
                null, new String[] {}, null, null, null);
        // 参数1：（String）表名
        // 参数2：（String[]）要查询的列名
        // 参数3：（String）查询条件
        // 参数4：（String[]）查询条件的参数
        // 参数5：（String）对查询的结果进行分组
        // 参数6：（String）对分组的结果进行限制
        // 参数7：（String）对查询的结果进行排序
        if(cursor.moveToNext())
        {
            byte[] msg = cursor.getBlob(cursor.getColumnIndex("id"));
            System.out.println(msg);
            Counters=getInfoAListFromBytes(msg);
        }

    }

    private void addTestTimeCounter()
    {
        Resources res = getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, R.mipmap.testimage);
        YouTimeCounter test=new YouTimeCounter(1579881600, "NEW YEAR", "It is a test", bmp);

        MySQLiteOpenHelper dbHelper1 = new MySQLiteOpenHelper(MainActivity.this,"youtime_db",2);
        SQLiteDatabase  sqliteDatabase1 = dbHelper1.getWritableDatabase();

        ContentValues values = new ContentValues();


        Counters.add(test);
        values.put("id", getInfoBytesFromAList(Counters));
        Counters.clear();

        sqliteDatabase1.insert("user", null, values);
        sqliteDatabase1.close();
    }
    public byte[] getInfoBytesFromAList(ArrayList<YouTimeCounter> list){
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(arrayOutputStream);
            objectOutputStream.writeObject(list);
            objectOutputStream.flush();
            byte[] data = arrayOutputStream.toByteArray();
            objectOutputStream.close();
            arrayOutputStream.close();
            System.out.println("data is here:");
            System.out.println(data);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<YouTimeCounter> getInfoAListFromBytes(byte[] bytes)
    {
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(bytes);
        try {
            ObjectInputStream inputStream = new ObjectInputStream(arrayInputStream);
            ArrayList<YouTimeCounter> list = (ArrayList<YouTimeCounter>) inputStream.readObject();
            inputStream.close();
            arrayInputStream.close();
            return list;
        }catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
