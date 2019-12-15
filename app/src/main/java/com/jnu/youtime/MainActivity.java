package com.jnu.youtime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jnu.youtime.data.YouTimeCounter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
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
        init();
    }
    private void init()
    {
        deleteDatabase("youtime_db");

        mainListView=findViewById(R.id.mainListView);
        navigationListview=findViewById(R.id.navigationListView);
        imageButton=findViewById(R.id.imageButton);
        FAB=findViewById(R.id.floatingActionButton);
        Counters=new ArrayList<>();

        Log.v("orii", "herrrrrrrrre");
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
        while(cursor.moveToNext())
        {
            Counters.add(new YouTimeCounter(
                    cursor.getLong(cursor.getColumnIndex("Time")),
                    cursor.getString(cursor.getColumnIndex("Tittle")),
                    cursor.getString(cursor.getColumnIndex("Note")),
                    bytetoBitmap(cursor.getBlob(cursor.getColumnIndex("Image")))
                    ));
        }

    }

    private void addTestTimeCounter()
    {
        Resources res = getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, R.mipmap.testimage);

        Bitmap te=bytetoBitmap(bitmapToByte(R.mipmap.testimage));
        YouTimeCounter test=new YouTimeCounter(1579881600, "NEW YEAR", "It is a test", bmp);

        Counters.add(test);
        insertToDB(Counters);
        Counters.clear();
    }
    public void insertToDB(ArrayList<YouTimeCounter> list)
    {
        MySQLiteOpenHelper dbHelper1 = new MySQLiteOpenHelper(MainActivity.this,"youtime_db",2);
        SQLiteDatabase  sqliteDatabase1 = dbHelper1.getWritableDatabase();


        long b;
        for(int i=0;i <list.size();i ++)
        {
            ContentValues values = new ContentValues();
            values.put("Id", i);
            values.put("Time", Counters.get(i).getTime());
            values.put("Tittle", Counters.get(i).getTitle());
            values.put("Note", Counters.get(i).getNote());
//            values.put("Image", bitmapToByte(Counters.get(i).getImage()));
            b=sqliteDatabase1.insert("MainList", null, values);
        }
        sqliteDatabase1.close();
    }
    public byte[] bitmapToByte(int id)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(id)).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
//        int bytes = b.getByteCount();
//        ByteBuffer buffer = ByteBuffer.allocate(bytes);
//        b.copyPixelsToBuffer(buffer); //Move the byte data to the buffer
//        byte[] data = buffer.array(); //Get the bytes array of the bitmap
//        return data;
    }

    public Bitmap bytetoBitmap(byte[] b)
    {
        Bitmap pic;
        if (b.length != 0) {
            pic=BitmapFactory.decodeByteArray(b, 0, b.length);
            return pic;
        }else return null;
    }
}
