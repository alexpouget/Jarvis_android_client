package com.example.alex.jarvis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.BitmapDrawable;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {


    private Button button;
    private Button onBedRoom;
    private Button offBedRoom;
    private Button onLivingRoom;
    private Button offLivingRoom;
    private Sender sender;

    private static final int REQUEST_CODE = 1001;
    private static final int PORT = 5555;
    private String ip= "192.168.0.126";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.settings){
            //open popUp settings
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            intent.putExtra("ip",ip);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    /*@Override
    protected void onResume() {
        super.onResume();

    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if (intent.getStringExtra("ip")!= null) {

            ip = intent.getStringExtra("ip");
        }

        Toast.makeText(this,ip,Toast.LENGTH_LONG).show();

        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(speak);

        onBedRoom = (Button)findViewById(R.id.OnBedRoom);
        onBedRoom.setOnClickListener(clickOnBedRoom);

        offBedRoom = (Button)findViewById(R.id.OffBedRoom);
        offBedRoom.setOnClickListener(clickOffBedRoom);

        onLivingRoom = (Button)findViewById(R.id.OnLivingRoom);
        onLivingRoom.setOnClickListener(clickOnLivingRoom);

        offLivingRoom = (Button)findViewById(R.id.OffLivingRoom);
        offLivingRoom.setOnClickListener(clickOffLivingRoom);

        onLivingRoom.setVisibility(View.INVISIBLE);
        offLivingRoom.setVisibility(View.INVISIBLE);
        onBedRoom.setVisibility(View.INVISIBLE);
        offBedRoom.setVisibility(View.INVISIBLE);
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
    View.OnClickListener clickOnBedRoom = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Command command = new Command("switch on bedroom");
            sender = new Sender(PORT,ip);
            sender.sendCommand(command);
            onLivingRoom.setVisibility(View.INVISIBLE);
            offLivingRoom.setVisibility(View.INVISIBLE);
            onBedRoom.setVisibility(View.INVISIBLE);
            offBedRoom.setVisibility(View.INVISIBLE);
        }
    };
    View.OnClickListener clickOffBedRoom = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Command command = new Command("switch off bedroom");
            sender = new Sender(PORT,ip);
            sender.sendCommand(command);
            onLivingRoom.setVisibility(View.INVISIBLE);
            offLivingRoom.setVisibility(View.INVISIBLE);
            onBedRoom.setVisibility(View.INVISIBLE);
            offBedRoom.setVisibility(View.INVISIBLE);
        }
    };
    View.OnClickListener clickOnLivingRoom = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Command command = new Command("switch on living room");
            sender = new Sender(PORT,ip);
            sender.sendCommand(command);
            onLivingRoom.setVisibility(View.INVISIBLE);
            offLivingRoom.setVisibility(View.INVISIBLE);
            onBedRoom.setVisibility(View.INVISIBLE);
            offBedRoom.setVisibility(View.INVISIBLE);
        }
    };
    View.OnClickListener clickOffLivingRoom = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Command command = new Command("switch off living room");
            sender = new Sender(PORT,ip);
            sender.sendCommand(command);
            onLivingRoom.setVisibility(View.INVISIBLE);
            offLivingRoom.setVisibility(View.INVISIBLE);
            onBedRoom.setVisibility(View.INVISIBLE);
            offBedRoom.setVisibility(View.INVISIBLE);

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
                if(!command.getAction().equalsIgnoreCase("default")){
                    sender = new Sender(PORT,ip);
                    sender.sendCommand(command);
                }else{
                    onBedRoom.setVisibility(View.VISIBLE);
                    offBedRoom.setVisibility(View.VISIBLE);
                    onLivingRoom.setVisibility(View.VISIBLE);
                    offLivingRoom.setVisibility(View.VISIBLE);
                }
            }
        }else{
            Toast.makeText(this,resultCode,Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*private void showPopup(final Activity context) {
        int popupWidth = 200;
        int popupHeight = 300;

        // Inflate the popup_layout.xml
        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.pop_up, viewGroup);

        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);

        // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
        int OFFSET_X = 30;
        int OFFSET_Y = 30;

        // Clear the default translucent background
        popup.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        popup.showAtLocation(layout, Gravity.NO_GRAVITY, 0 + OFFSET_X, 0 + OFFSET_Y);

        // Getting a reference to Close button, and close the popup when clicked.
        final EditText text = (EditText) layout.findViewById(R.id.editText);
        Button close = (Button) layout.findViewById(R.id.valider);
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.putExtra("ip",text.getText());
                startActivity(intent);
                popup.dismiss();
                //add transmission ip
            }
        });
    }*/

}
