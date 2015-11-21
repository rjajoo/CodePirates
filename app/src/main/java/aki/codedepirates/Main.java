package aki.codedepirates;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Main extends Activity {

    static Button button;
    Singleton singleton = Singleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash);
/*
        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.main);
        final ImageView iv = new ImageView(this);
        iv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        iv.setImageResource(R.drawable.logo);
        relativeLayout.addView(iv);
        */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                singleton.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Main.this);
                singleton.setId(singleton.sharedPreferences.getInt("ID",-1));
                if(singleton.getId() != -1){
                    Intent intent = new Intent(Main.this, Screen.class);
                    startActivity(intent);
                    finish();
                } else {

                    setContentView(R.layout.activity_main);

                    button = (Button) findViewById(R.id.main_next);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Main.this, Login.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        },2500);
    }
}
