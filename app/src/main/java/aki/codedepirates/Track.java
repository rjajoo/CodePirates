package aki.codedepirates;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.zip.Inflater;

/**
 * Created by User on 25-Dec-14.
 */
public class Track extends android.support.v4.app.Fragment {

    static TableLayout tableLayout;
    static TextView textView;
    static Singleton singleton = Singleton.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.track, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textView = (TextView) view.findViewById(R.id.id);
        textView.append(String.valueOf(singleton.getId()));

        tableLayout = (TableLayout) view.findViewById(R.id.track_table);

        Cursor cursor = singleton.database.rawQuery("SELECT * FROM " + Singleton.TABLE + ";", null);

        int rows = cursor.getCount();
        cursor.moveToFirst();

        for(int i=0;i<rows;i++){
            int j=0;
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.row, tableLayout, false);

            TextView tv = (TextView) row.findViewById(R.id.sn);
            tv.setText(cursor.getString(j));
            j++;

            ImageView iv = (ImageView) row.findViewById(R.id.status);
            if(cursor.getString(j).equals("1")){
                iv.setImageResource(R.drawable.correct);
            } else {
                iv.setImageResource(R.drawable.wrong);
            }
            j++;

            tv = (TextView) row.findViewById(R.id.station);
            tv.setText(cursor.getString(j));
            j++;

            tv = (TextView) row.findViewById(R.id.time);
            tv.setText(cursor.getString(j));
            j++;
            /*TableRow row = new TableRow(getActivity());
            row.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            row.setWeightSum(100);

            for(int j=0;j<4;j++){
                TextView tv = new TextView(getActivity());
                tv.setBackgroundResource(R.drawable.cell_shape);
                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(20);
                tv.setTextColor(Color.parseColor("#00ff00"));
                //tv.setPadding(0, 5, 0, 5);

                String str = cursor.getString(j);
                tv.setText(str);

                row.addView(tv);
            }*/
            cursor.moveToNext();

            tableLayout.addView(row);
        }
    }
}