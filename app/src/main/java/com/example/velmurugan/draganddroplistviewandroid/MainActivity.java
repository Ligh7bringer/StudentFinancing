package com.example.velmurugan.draganddroplistviewandroid;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    String[] head_list = {"Food", "Transport", "Entertainment"};
    ArrayList items = new ArrayList();
    private ListAdapter adapter=null;
    private ArrayList<String> array=new ArrayList<String>(Arrays.asList(head_list));
    Button btnADDTRANS;
    TextView textDISPLAYBAL;
    public static int balance = 30;

    Button btnAdd;
    private String input_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnADDTRANS = findViewById(R.id.btnADDTRANS);


        textDISPLAYBAL = findViewById(R.id.textBAL);
        textDISPLAYBAL.setText(String.valueOf(balance));

        btnADDTRANS.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //custom dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("add transaction: ");
                //set the custom dialog components
                final EditText editText = new EditText(MainActivity.this);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(editText);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        balance -= Integer.valueOf(editText.getText().toString());
                        textDISPLAYBAL.setText(String.valueOf(balance));
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
        TouchListView tlv=(TouchListView) findViewById(R.id.touch_listview);
        adapter=new ListAdapter();
        tlv.setAdapter(adapter);

        tlv.setDropListener(onDrop);
        tlv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                   long arg3) {
            }
        });

        btnAdd = findViewById(R.id.btnPlus);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Enter new category: ");
                final EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        input_text = input.getText().toString();
                        adapter.insert(input_text, head_list.length);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    private TouchListView.DropListener onDrop=new TouchListView.DropListener() {
        @Override
        public void drop(int from, int to) {
            String item=adapter.getItem(from);
            adapter.remove(item);
            adapter.insert(item, to);
        }
    };

    class ListAdapter extends ArrayAdapter<String> {
        ListAdapter() {
            super(MainActivity.this, R.layout.adapter_layout, array);
        }
        public View getView(int position, View convertView,
                            ViewGroup parent) {
            View row=convertView;
            if (row==null) {
                LayoutInflater inflater=getLayoutInflater();
                row=inflater.inflate(R.layout.adapter_layout, parent, false);
            }
            TextView label=(TextView)row.findViewById(R.id.label);
            label.setText(array.get(position));
            return(row);
        }
    }
}
