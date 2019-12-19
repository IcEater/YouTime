package com.jnu.youtime;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class addCounterActivity extends AppCompatActivity {

    FloatingActionButton backButton;
    FloatingActionButton confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_counter);
        init();
    }
    private void init(){
        backButton=findViewById(R.id.addLayoutBackFAB);
        confirmButton=findViewById(R.id.addLayoutConfirmFAB);

        int index=getIntent().getIntExtra("numOfCounter", -1);
        String a=new String();

        Toast.makeText(addCounterActivity.this, new Integer(index) .toString(), Toast.LENGTH_LONG).show();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(addCounterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}
