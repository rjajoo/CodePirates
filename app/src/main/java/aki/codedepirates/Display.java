package aki.codedepirates;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by User on 20-Jan-15.
 */
public class Display extends Activity {

    TextView textView;
    Button button;
    Singleton singleton = Singleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.code);

        textView = (TextView) findViewById(R.id.viewer);
        textView.setText(singleton.M[Singleton.popup].getQues());

        button = (Button) findViewById(R.id.close);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
