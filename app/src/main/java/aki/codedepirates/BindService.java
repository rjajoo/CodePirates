package aki.codedepirates;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class BindService extends Service {
    private boolean quit;

    static Singleton singleton = Singleton.getInstance();
    //static int COUNT = 14400;
    private MyBinder binder = new MyBinder();
    // My Binder
    public class MyBinder extends Binder
    {
        public int getCount()
        {
            // get the counting status：count
            return Singleton.timer;
        }
    }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Notifier("Timer Started");
            return START_STICKY;
        }

    private void Notifier(String text) {
        Intent Nintent = new Intent(this, Screen.class);
        Nintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , Nintent, 0);
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Code De Pirates")
                .setTicker("Timer")
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();
        startForeground(1,notification);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        System.out.println("Service is Binded");
        // return the binder instance
        return binder;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        System.out.println("Service is Created");
        //Singleton.timer = 0;
        // counting work
        new Thread()
        {
            @Override
            public void run()
            {
                while (!quit)
                {
                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e)
                    {
                    }
                    Singleton.timer++;
                    Notifier(Screen.textView.getText().toString());
                    SharedPreferences.Editor editor = singleton.sharedPreferences.edit();
                    editor.putInt("timer", Singleton.timer);
                    if(Singleton.timer >= 10800) {
                        Singleton.status = true;
                        editor.putBoolean("status", Singleton.status);
                    }
                    editor.commit();
                }
            }
        }.start();
    }
    // invoke when the service unbind
    @Override
    public boolean onUnbind(Intent intent)
    {
        System.out.println("Service is Unbinded");
        return true;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Notifier("Time froze at the World's End! Tap to return to home port.");
        this.quit = true;
        stopForeground(true);
        SharedPreferences.Editor editor = singleton.sharedPreferences.edit();
        editor.putInt("timer", Singleton.timer);
        editor.commit();
        System.out.println("Service is Destroyed");
    }

    @Override
    public void onRebind(Intent intent)
    {
        super.onRebind(intent);
        this.quit = true;
        System.out.println("Service is ReBinded");
    }
}