package aki.codedepirates;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by User on 25-Dec-14.
 */
public class Screen extends FragmentActivity implements View.OnClickListener{

    static Button button1, button2, button3, button4, button5, button6;
    static FrameLayout frameLayout;
    static TextView textView;
    static Singleton singleton = Singleton.getInstance();
    static Context context;

    static Intent intent;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int count = Singleton.timer;
            textView.setText(count/3600 + " : " + (count%3600)/60 + " : " + ((count%3600)%60));
            if(count < 10800) {
                handler.postDelayed(this, 1000);
            }
            else {
                //getApplicationContext().unbindService(conn);
                stopService(intent);
                onClick(button2);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.screen);

        context = getBaseContext();

        frameLayout = (FrameLayout) findViewById(R.id.frame);

        textView = (TextView) findViewById(R.id.timer);

        button1 = (Button) findViewById(R.id.rules);
        button2 = (Button) findViewById(R.id.ques);
        button3 = (Button) findViewById(R.id.track);
        button4 = (Button) findViewById(R.id.map);
        button5 = (Button) findViewById(R.id.master);
        button6 = (Button) findViewById(R.id.aboutus);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);

        SQLiteDatabase sqLiteDatabase = getApplicationContext().openOrCreateDatabase(Singleton.DATABASE, MODE_PRIVATE, null);
        singleton.Set(sqLiteDatabase);

        if(singleton.sharedPreferences.getBoolean("there",false)){
            singleton.values();
        } else {
            singleton.generate();
        }

        intent = new Intent(Screen.this, BindService.class);
        if(Singleton.popup != -1 && !Singleton.status)
            startService(intent);

        //getApplicationContext().bindService(intent, conn, Service.BIND_AUTO_CREATE);
        handler.post(runnable);
        onClick(button2);
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment = null;
        if(v == button1){
            fragment = new Rules();
        }else if (v == button2){
            fragment = new Ques();
        }else if (v == button3){
            fragment = new Track();
        }else if (v == button4){
            fragment = new Map();
        }else if (v == button5){
            fragment = new Master();
        }else if (v == button6){
            fragment = new About();
        }
        if(fragment!=null) {
            android.support.v4.app.FragmentTransaction ft=fragmentManager.beginTransaction();
            ft.replace(R.id.frame, fragment);
            //ft.addToBackStack(null);
            ft.commit();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        singleton.values_save();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        singleton.database.close();
    }
}