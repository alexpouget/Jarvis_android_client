package com.example.alex.jarvis;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    private Button button;
    //private Command command;
    private Sender sender;
    private static final int REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(speak);
        //command = new Command();
        String ip = "192.0.0.0";
        sender = new Sender(3434,ip);
        checkVoiceRecognition();
    }

    View.OnClickListener speak = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getClass().getPackage().getName());
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            startActivityForResult(intent,REQUEST_CODE);
        }
    };

    private void checkVoiceRecognition() {
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH),0);
        if(activities.size() == 0){
            button.setEnabled(false);
            Toast.makeText(this,"recognizer not available",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE){
            if (resultCode == RESULT_OK){
                ArrayList<String> arrayList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if(arrayList.isEmpty()){
                    Toast.makeText(this,"string empty",Toast.LENGTH_LONG).show();
                }
                Toast.makeText(this,arrayList.get(0),Toast.LENGTH_LONG).show();
                Command command = new Command(arrayList.get(0));
                sender.sendCommand(command);
            }
        }else{
            Toast.makeText(this,resultCode,Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
