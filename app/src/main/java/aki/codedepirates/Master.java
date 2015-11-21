package aki.codedepirates;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by User on 30-Dec-14.
 */
public class Master extends android.support.v4.app.Fragment {

    ListView listView;
    Button button;
    RelativeLayout relativeLayout;
    static Singleton singleton = Singleton.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.master, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        relativeLayout = (RelativeLayout) view.findViewById(R.id.layout);

        listView = (ListView) view.findViewById(R.id.list);

        button = (Button) view.findViewById(R.id.execute);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MasterAnswer.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Singleton.popup == -1){
            listView.setVisibility(View.GONE);
            button.setVisibility(View.GONE);

            TextView textView = new TextView(getActivity());
            textView.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.parseColor("#00ff00"));
            textView.setText("Ahoy Pirate you found the hidden Treasure of Lost Island\nSet sail to Home Port to fly your flag!");

            relativeLayout.addView(textView);
        }
        Cursor c = Singleton.getInstance().database.rawQuery("SELECT * from " + Singleton.TABLE_Master + " ORDER BY Sn ASC;",null);
        ItemAdapter itemAdapter = new ItemAdapter(getActivity(),c);
        listView.setAdapter(itemAdapter);
    }

    public class ItemAdapter extends CursorAdapter {
        private LayoutInflater mLayoutInflater;
        private Context mContext;

        public ItemAdapter(Context context, Cursor c) {
            super(context, c);
            mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View v = mLayoutInflater.inflate(R.layout.list, parent, false);
            return v;
        }

        @Override
        public void bindView(View v, Context context, final Cursor c) {
            final int station = c.getInt(c.getColumnIndexOrThrow("Station"));
            final int serial = c.getInt(c.getColumnIndexOrThrow("Sn"));
            final int pos = c.getPosition();

            final TextView title_text = (TextView) v.findViewById(R.id.master_text);
            if (title_text != null) {
                title_text.setText("Master Code #" + String.valueOf(station));
            }

            title_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Singleton.popup = station - 1;
                    Intent intent = new Intent(getActivity(), Display.class);
                    startActivity(intent);
                }
            });

            Button button1, button2;

            button1 = (Button) v.findViewById(R.id.up);
            button2 = (Button) v.findViewById(R.id.down);

            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pos == 0)
                        return;
                    c.moveToPosition(pos);
                    c.moveToPrevious();
                    int st = c.getInt(c.getColumnIndexOrThrow("Station"));
                    int sr = c.getInt(c.getColumnIndexOrThrow("Sn"));
                    singleton.database.execSQL("UPDATE " + Singleton.TABLE_Master + " SET Sn = " + String.valueOf(serial) + " Where Sn = " + String.valueOf(sr));
                    singleton.database.execSQL("UPDATE " + Singleton.TABLE_Master + " SET Sn = " + String.valueOf(sr) + " Where Sn = " + String.valueOf(serial) + " AND Station = " + String.valueOf(station));
                    onResume();
                }
            });

            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //int x = c.getCount();
                    if(pos == c.getCount()-1)
                        return;
                    c.moveToPosition(pos);
                    c.moveToNext();
                    int st = c.getInt(c.getColumnIndexOrThrow("Station"));
                    int sr = c.getInt(c.getColumnIndexOrThrow("Sn"));
                    singleton.database.execSQL("UPDATE " + Singleton.TABLE_Master + " SET Sn = " + String.valueOf(serial) + " Where Sn = " + String.valueOf(sr));
                    singleton.database.execSQL("UPDATE " + Singleton.TABLE_Master + " SET Sn = " + String.valueOf(sr) + " Where Sn = " + String.valueOf(serial) + " AND Station = " + String.valueOf(station));
                    onResume();
                }
            });
        }
    }
}