package aki.codedepirates;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by User on 25-Dec-14.
 */
public class Ques extends android.support.v4.app.Fragment {

    static Button button;
    static TextView textView;
    static EditText editText;
    static Singleton singleton=Singleton.getInstance();
    //static Random r1 = new Random();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ques, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textView = (TextView) view.findViewById(R.id.question);
        editText = (EditText) view.findViewById(R.id.answer);

        button = (Button) view.findViewById(R.id.submit_answer);

        if(Singleton.status || singleton.count > 14){
            editText.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
            textView.setGravity(Gravity.CENTER);
            if(Singleton.status){
                textView.setText(R.string.end);
            } else {
                textView.setText("Completed");
            }
        } else if (Singleton.spot){
            textView.setText("Solve spot question and then ask volunteer to enter code");
        }else {
            //Spanned spanned = Html.fromHtml(singleton.Q[singleton.current].getQues());
            textView.setText(singleton.Q[singleton.current].getQues());
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (Singleton.status){
                    Toast.makeText(getActivity(), "Time ended!!!", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                if(editText.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Enter Code!!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(editText.getText().toString().equals("DANGER")){
                    Singleton.timer+=300;
                    SharedPreferences.Editor editor = singleton.sharedPreferences.edit();
                    editor.putInt("timer", Singleton.timer);
                    editor.commit();
                    Toast.makeText(getActivity(), "Penalty added!!!", Toast.LENGTH_SHORT).show();
                }
                if(Singleton.spot){
                    if(singleton.Q[singleton.current].getCode().toLowerCase().equals(editText.getText().toString().toLowerCase())){
                        Singleton.spot = false;
                        SharedPreferences.Editor editor = singleton.sharedPreferences.edit();
                        editor.putBoolean("spot",false);
                        editor.commit();
                        singleton.generate();
                    }
                    Screen.button2.performClick();
                    return;
                }
                Singleton.sn++;
                Singleton.current_station = singleton.station(editText.getText().toString().toLowerCase());
                if(singleton.Q[singleton.current].getCode().toLowerCase().equals(editText.getText().toString().toLowerCase())){
                        singleton.Insert(1, Singleton.current_station, ((TextView) getActivity().findViewById(R.id.timer)).getText().toString());
                        singleton.Insert_Master(Singleton.current_station);
                    Singleton.spot = true;
                    SharedPreferences.Editor editor = singleton.sharedPreferences.edit();
                    editor.putBoolean("spot", true);
                    editor.commit();
                    singleton.values_save();
                    if(singleton.current != -1 )
                        textView.setText(singleton.Q[singleton.current].getQues());
                } else if (Singleton.current_station != -1){
                    singleton.Insert(0, Singleton.current_station, ((TextView)getActivity().findViewById(R.id.timer)).getText().toString());
                    Toast.makeText(getActivity(),"Looks like you anchored at wrong port!!!",Toast.LENGTH_LONG).show();
                }else {
                    Singleton.sn--;
                    Toast.makeText(getActivity(),"Invalid Code!!! Maybe you messed up there!",Toast.LENGTH_LONG).show();
                }
                Screen.button2.performClick();
                /*if(singleton.count > 14){
                    getActivity().stopService(Screen.intent);
                    textView.setGravity(Gravity.CENTER);
                    textView.setText("Completed");
                    editText.setVisibility(View.GONE);
                    button.setVisibility(View.GONE);
                }*/
            }
        });
    }
}