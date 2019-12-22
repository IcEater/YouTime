package com.jnu.youtime;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jnu.youtime.data.YouTimeCounter;

import java.util.ArrayList;

public class editActivity extends AppCompatActivity {

    ArrayList<YouTimeCounter> Counters;

    ImageView imageView;
    TextView title;
    TextView time;
    TextView note;
    FloatingActionButton backFAB;
    FloatingActionButton dustbinFAB;
    int index;

    long countDown=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);
        init();
    }
    void init() {


        Counters=new ArrayList<>();
        MySQLiteOpenHelper dbHelper = new MySQLiteOpenHelper(editActivity.this,"youtime_db",2);
        SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();

        Cursor cursor = sqliteDatabase.query("MainList", new String[] {"id", "Time", "Tittle", "Note", "Image", "Repeat"},
                null, new String[] {}, null, null, null);

        while(cursor.moveToNext()) {
            Counters.add(new YouTimeCounter(
                    cursor.getLong(cursor.getColumnIndex("Time")),
                    cursor.getString(cursor.getColumnIndex("Tittle")),
                    cursor.getString(cursor.getColumnIndex("Note")),
                    MainActivity.byteToBitmap(cursor.getBlob(cursor.getColumnIndex("Image"))),
                    cursor.getLong(cursor.getColumnIndex("Repeat"))
            ));
        }
        cursor.close();

        index=getIntent().getIntExtra("counterIndex", -1);
//        long time, String title, String note, Bitmap image, long repeat

        backFAB=findViewById(R.id.editLayoutBackFAB);
        dustbinFAB=findViewById(R.id.editLayoutDustbin);
        imageView=findViewById(R.id.editLayoutImage);
        title=findViewById(R.id.editLayoutTitle);
        time=findViewById(R.id.editLayoutTime);
        note=findViewById(R.id.editLayoutNote);
        title.setText(Counters.get(index).getTitle());
        note.setText(Counters.get(index).getNote());

        countDown=Counters.get(index).getTime()*1000-System.currentTimeMillis();
        imageView.setImageBitmap(Counters.get(index).getImage());
        ColorMatrix cMatrix = new ColorMatrix();
        int brightness=-60;
        cMatrix.set(new float[] { 1, 0, 0, 0, brightness, 0, 1, 0, 0, brightness, // 改变亮度
                0, 0, 1, 0, brightness, 0, 0, 0, 1, 0 });
        imageView.setColorFilter(new ColorMatrixColorFilter(cMatrix));
        CountDownTimer cdTimer = new CountDownTimer(countDown, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time.setText((millisUntilFinished / 1000) + " s");
            }

            @Override
            public void onFinish() {
            }
        };
        cdTimer.start();

        backFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(editActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        dustbinFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDatabase("youtime_db");
                Counters.remove(index);
                insertToDB(Counters);
                Toast.makeText(editActivity.this, "删除成功", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(editActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public void insertToDB(ArrayList<YouTimeCounter> list) {
        MySQLiteOpenHelper dbHelper1 = new MySQLiteOpenHelper(editActivity.this,"youtime_db",2);
        SQLiteDatabase  sqliteDatabase1 = dbHelper1.getWritableDatabase();

        for(int i=0;i <list.size();i ++)
        {
            ContentValues values = new ContentValues();
            values.put("Id", i);
            values.put("Time", Counters.get(i).getTime());
            values.put("Tittle", Counters.get(i).getTitle());
            values.put("Note", Counters.get(i).getNote());
            values.put("Image", MainActivity.bitmapToByte(Counters.get(i).getImage()));
            values.put("Repeat", Counters.get(i).getRepeat());
            sqliteDatabase1.insert("MainList", null, values);
        }
        sqliteDatabase1.close();
    }
}
