package aki.codedepirates;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by User on 25-Dec-14.
 */
public class Login extends Activity{

    static Spinner spinner;
    static Button button;
    static EditText editText;
    static Singleton singleton = Singleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

        editText = (EditText) findViewById(R.id.password);

        button = (Button) findViewById(R.id.login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = (int)spinner.getSelectedItemId()+1;
                String password = editText.getText().toString();
                if(password.equals("pirate"+String.valueOf(id))){
                    Singleton.getInstance().setId(id);
                    Singleton.getInstance().setPassword(password);

                    SharedPreferences.Editor editor = singleton.sharedPreferences.edit();
                    editor.putInt("ID",id);
                    editor.putString("PASS",password);
                    editor.commit();

                    Intent intent = new Intent(getBaseContext(),Screen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(Login.this,"Wrong Password!!!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        spinner = (Spinner) findViewById(R.id.team_select);
        Integer[] items = new Integer[80];
        for (int i = 0; i < items.length; i++) {
            items[i] = i+1;
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,R.layout.spinner, items);
        spinner.setAdapter(adapter);
    }
}
