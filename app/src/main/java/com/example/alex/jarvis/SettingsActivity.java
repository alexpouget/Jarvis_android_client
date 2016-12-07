package com.example.alex.jarvis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by alex on 07/12/2016.
 */

public class SettingsActivity extends Activity {

    EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pop_up);
        Intent intent = getIntent();
        String ip = intent.getStringExtra("ip");

        text = (EditText) findViewById(R.id.editText);
        Button close = (Button) findViewById(R.id.valider);
        text.setText(ip);
        close.setOnClickListener(click);
    }

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            intent.putExtra("ip",text.getText().toString());

            startActivity(intent);

            //add transmission ip
        }
    };

}
