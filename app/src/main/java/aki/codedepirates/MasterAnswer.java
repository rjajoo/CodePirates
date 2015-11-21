package aki.codedepirates;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by User on 19-Jan-15.
 */
public class MasterAnswer extends Activity {

    TextView textView;
    EditText editText;
    Button button;
    Singleton singleton = Singleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.master_answer);

        textView = (TextView) findViewById(R.id.code);
        editText = (EditText) findViewById(R.id.master_answer);
        button = (Button) findViewById(R.id.submit);

        if(singleton.count < 15){
            editText.setVisibility(View.INVISIBLE);
        } else {
            editText.setVisibility(View.VISIBLE);
        }

        textView.setText("");

        Cursor cursor = Singleton.getInstance().database.rawQuery("SELECT * from " + Singleton.TABLE_Master +" ORDER BY Sn ASC;",null);
        cursor.moveToFirst();
        for(int i=0;i<cursor.getCount();i++){
            textView.append(singleton.M[cursor.getInt(cursor.getColumnIndexOrThrow("Station")) - 1].getQues());
            cursor.moveToNext();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().equals("")) {
                    Toast.makeText(MasterAnswer.this, "Enter Answer!!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editText.getText().toString().equals("NUTECH")){
                    Singleton.popup = -1;
                    SharedPreferences.Editor editor = singleton.sharedPreferences.edit();
                    editor.putInt("timer", Singleton.timer);
                    editor.putInt("pop",Singleton.popup);
                    editor.commit();
                    Screen.context.stopService(Screen.intent);
                    finish();
                }
            }
        });
    }
}
