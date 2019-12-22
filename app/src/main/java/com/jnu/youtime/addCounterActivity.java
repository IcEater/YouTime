package com.jnu.youtime;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.pickerview.TimePickerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jnu.youtime.data.YouTimeCounter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class addCounterActivity extends AppCompatActivity {

    FloatingActionButton backButton;
    FloatingActionButton confirmButton;
    EditText editTitle;
    EditText editNote;
    ListView listView;
    ImageView imageView;

    int picNum;
    long time;
    Bitmap bitmap;
    String title;
    String note;
    long repeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_counter);
        init();
    }

    private void init(){
        Random random=new Random();
        picNum=new Integer(random.nextInt(4));

        picNum+=1;
        Resources res = getResources();
        switch (picNum)
        {
            case 1:
                bitmap= BitmapFactory.decodeResource(res, R.mipmap.nature_1);
                break;
            case 2:
                bitmap=BitmapFactory.decodeResource(res, R.mipmap.nature_2);
                break;
            case 3:
                bitmap=BitmapFactory.decodeResource(res, R.mipmap.nature_3);
                break;
            case 4:
                bitmap=BitmapFactory.decodeResource(res, R.mipmap.nature_4);
                break;
        }
        editTitle=findViewById(R.id.addCounterTitleEditText);
        editNote=findViewById(R.id.addCounterNoteEditText);
        imageView=findViewById(R.id.addLayoutImageView);
        backButton=findViewById(R.id.addLayoutBackFAB);
        confirmButton=findViewById(R.id.addLayoutConfirmFAB);
        listView=findViewById(R.id.addLayoutListView);

        imageView.setImageBitmap(bitmap);
        ColorMatrix cMatrix = new ColorMatrix();
        int brightness=-60;
        cMatrix.set(new float[] { 1, 0, 0, 0, brightness, 0, 1, 0, 0, brightness, // 改变亮度
                0, 0, 1, 0, brightness, 0, 0, 0, 1, 0 });
        imageView.setColorFilter(new ColorMatrixColorFilter(cMatrix));



        List<Map<String,Object>> lists;
        lists = new ArrayList<>();
        Map<String,Object> map =new HashMap<>();

        map.put("image", R.mipmap.iceater_icon_clock);
        map.put("text", "设置时间");
        lists.add(map);
        map=new HashMap<>();

        map.put("image", R.mipmap.iceater_icon_cycle);
        map.put("text", "重复");
        lists.add(map);
        map=new HashMap<>();

        map.put("image", R.mipmap.iceater_icon_picture);
        map.put("text", "更换图片");
        lists.add(map);



        String []from={"image", "text"};
        int []to=new int[]{R.id.addCounterListImageView, R.id.addCounterListTextView};

        listView.setAdapter(new SimpleAdapter(addCounterActivity.this, lists, R.layout.add_layout_sublayout, from, to));

        final int index=getIntent().getIntExtra("numOfCounter", -1);

        final TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                time=date.getTime();
                time=time/1000;
            }
        })
                .build();
        pvTime.setDate(Calendar.getInstance());
        //根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i)
                {
                    case 0:
                        pvTime.show();
                        break;
                    case 1:
                        PopupMenu popupMenu = new PopupMenu(addCounterActivity.this, listView.getChildAt(i));
                        popupMenu.getMenuInflater().inflate(R.menu.repect_popup_menu,popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                // TODO Auto-generated method stub
                                int id = item.getItemId();
                                switch (id) {
                                    case R.id.none:
                                        repeat=-1;
                                        break;
                                    case R.id.everyDay:
                                        repeat=60*60*24;
                                        break;
                                    case R.id.everyWeek:
                                        repeat=60*60*24*7;
                                    default:
                                        break;
                                }
                                return false;
                            }
                        });
                        popupMenu.show();
                        break;
                    case 2:
                        Random random=new Random();
                        int picNew=new Integer(random.nextInt(4))+1;
                        while(picNew==picNum)
                        {
                            picNew=new Integer(random.nextInt(4))+1;
                        }
                        picNum=picNew;
                        Resources res = getResources();
                        switch (picNew)
                        {
                            case 1:
                                bitmap= BitmapFactory.decodeResource(res, R.mipmap.nature_1);
                                break;
                            case 2:
                                bitmap=BitmapFactory.decodeResource(res, R.mipmap.nature_2);
                                break;
                            case 3:
                                bitmap=BitmapFactory.decodeResource(res, R.mipmap.nature_3);
                                break;
                            case 4:
                                bitmap=BitmapFactory.decodeResource(res, R.mipmap.nature_4);
                                break;
                        }
                        imageView.setImageBitmap(bitmap);
                        break;
                    default:
                        break;
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(addCounterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title=editTitle.getText().toString();
                if(title.isEmpty())
                {

                    Toast.makeText(addCounterActivity.this, "标题未设置！", Toast.LENGTH_SHORT).show();
                    return ;
                }
                else
                {
                    if(time*1000<System.currentTimeMillis())
                    {
                        Toast.makeText(addCounterActivity.this, "时间设置错误！", Toast.LENGTH_SHORT).show();
                        return ;
                    }
                    note=editNote.getText().toString();
                    YouTimeCounter counter=new YouTimeCounter(time, title, note, bitmap, repeat);
                    MySQLiteOpenHelper dbHelper1 = new MySQLiteOpenHelper(addCounterActivity.this,"youtime_db",2);
                    SQLiteDatabase sqliteDatabase = dbHelper1.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("Id", index);
                    values.put("Time", counter.getTime());
                    values.put("Tittle", counter.getTitle());
                    values.put("Note", counter.getNote());
                    values.put("Image", MainActivity.bitmapToByte(counter.getImage()));
                    values.put("Repeat", counter.getRepeat());
                    sqliteDatabase.insert("MainList", null, values);
                    sqliteDatabase.close();
                    Intent intent=new Intent(addCounterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
}
