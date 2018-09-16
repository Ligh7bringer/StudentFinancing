package com.example.velmurugan.draganddroplistviewandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    String[] head_list = {};
    String[] list_bad = {};

    private ListAdapter adapter=null, adapterBad = null;
    private ArrayList<String> array=new ArrayList<String>(Arrays.asList(head_list));
    private ArrayList<String> arrayBad=new ArrayList<String>(Arrays.asList(list_bad));
    private ArrayList<String> currentList;
    Button btnADDTRANS;
    TextView textDISPLAYBAL;

    Button btnAdd;
    private String input_text;
    private float budget = 100;
    private String lastCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TouchListView tlv = findViewById(R.id.touch_listview);
        adapter=new ListAdapter(array);
        btnADDTRANS = findViewById(R.id.btnADDTRANS);


        textDISPLAYBAL = findViewById(R.id.textBAL);
        textDISPLAYBAL.setText(String.valueOf(budget));

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
                        budget -= Float.valueOf(editText.getText().toString());
                        textDISPLAYBAL.setText(String.valueOf(budget));
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
        adapter=new ListAdapter(array);

        tlv.setAdapter(adapter);

        TouchListView tlvBad = findViewById(R.id.touch_listview_bad);
        adapterBad = new ListAdapter(arrayBad);
        tlvBad.setAdapter(adapterBad);

        tlv.setDropListener(onDrop);
        tlv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                   long arg3) {

            }
        });


        tlvBad.setDropListener(onDrop2);

        btnAdd = findViewById(R.id.btnPlus);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout layout = new LinearLayout(MainActivity.this);
                layout.setOrientation(LinearLayout.HORIZONTAL);

                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Add new category");

                final EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setHint("Name");

                layout.addView(input);

                final EditText price = new EditText(MainActivity.this);
                price.setHint("Price");
                layout.addView(price);

                builder.setView(layout);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        input_text = input.getText().toString();
                        String costStr = price.getText().toString();
                        float cost = Integer.valueOf(costStr);
                        lastCost = costStr;

                        if(cost > budget) {
                            arrayBad.add(input_text);
                            currentList = arrayBad;
                        } else {
                            array.add(input_text);
                            currentList = array;
                            budget -= cost;
                            Toast.makeText(getBaseContext(),"Remaining budget: " + budget, Toast.LENGTH_LONG).show();
                            textDISPLAYBAL.setText(String.valueOf(budget));
                        }
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

    private TouchListView.DropListener onDrop = new TouchListView.DropListener() {
        @Override
        public void drop(int from, int to) {
            String item=adapter.getItem(from);
            adapter.remove(item);
            adapter.insert(item, to);
        }
    };

    private TouchListView.DropListener onDrop2 = new TouchListView.DropListener() {
        @Override
        public void drop(int from, int to) {
            String item=adapterBad.getItem(from);
            adapterBad.remove(item);
            adapterBad.insert(item, to);
        }
    };

    class ListAdapter extends ArrayAdapter<String> {

        ListAdapter(ArrayList<String> itemList) {
            super(MainActivity.this, R.layout.adapter_layout, itemList);
        }

        @NonNull
        public View getView(int position, View convertView,
                            ViewGroup parent) {
            View row=convertView;
            if (row==null) {
                LayoutInflater inflater=getLayoutInflater();
                row=inflater.inflate(R.layout.adapter_layout, parent, false);
            }
            TextView label = row.findViewById(R.id.label);

            label.setText(currentList.get(position));

            TextView labelCost = row.findViewById(R.id.label_cost);
            labelCost.setText(lastCost);

            return(row);
        }
    }
}
