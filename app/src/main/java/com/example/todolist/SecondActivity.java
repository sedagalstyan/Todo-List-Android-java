package com.example.todolist;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    ListView listView;
    Spinner spinnerDataSource;
    Button buttonClearIntent, buttonClearSharedPref, buttonClearSQLite;
    ArrayList<String> textList;
    DBHelper dbHelper;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        listView = findViewById(R.id.listView);
        spinnerDataSource = findViewById(R.id.spinnerDataSource);
        buttonClearIntent = findViewById(R.id.buttonClearIntent);
        buttonClearSharedPref = findViewById(R.id.buttonClearSharedPref);
        buttonClearSQLite = findViewById(R.id.buttonClearSQLite);
        dbHelper = new DBHelper(this);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,
                new String[]{"Intent", "SharedPreferences", "SQLite"});
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDataSource.setAdapter(spinnerAdapter);

        textList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, textList);
        listView.setAdapter(adapter);

        spinnerDataSource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadData(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        buttonClearIntent.setOnClickListener(v -> {
            textList.clear();
            adapter.notifyDataSetChanged();
        });

        buttonClearSharedPref.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            prefs.edit().clear().apply();
            if (spinnerDataSource.getSelectedItemPosition() == 1) {
                textList.clear();
                adapter.notifyDataSetChanged();
            }
        });

        buttonClearSQLite.setOnClickListener(v -> {
            dbHelper.deleteAllNames();
            if (spinnerDataSource.getSelectedItemPosition() == 2) {
                textList.clear();
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void loadData(int source) {
        textList.clear();
        switch (source) {
            case 0:
                ArrayList<String> intentList = getIntent().getStringArrayListExtra("textList");
                if (intentList != null) textList.addAll(intentList);
                break;
            case 1:
                textList.addAll(getListFromSharedPreferences());
                break;
            case 2:
                textList.addAll(dbHelper.getAllNames());
                break;
        }
        adapter.notifyDataSetChanged();
    }

    private ArrayList<String> getListFromSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String jsonString = prefs.getString("textList", "[]");
        ArrayList<String> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
