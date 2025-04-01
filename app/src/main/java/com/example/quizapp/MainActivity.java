package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText edit1;
    Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edit1=findViewById(R.id.editText3);
        btn1=findViewById(R.id.btn1);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startactivity2();
            }
        });
    }

    public void startactivity2(){
        // Create the Intent object of this class Context() to Second_activity class
        Intent intent = new Intent(MainActivity.this, activity2.class);
        // now by putExtra method put the value in key, value pair key is
        // message_key by this key we will receive the value, and put the string
        // start the Intent
        startActivity(intent);
    }
}