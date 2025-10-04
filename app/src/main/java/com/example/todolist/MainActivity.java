package com.example.todolist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText editTextName;
    Button buttonAdd, buttonSendIntent, buttonSaveSharedPref, buttonSaveSQLite, buttonClearList;
    ListView listViewNames;

    ArrayList<String> textList;
    ArrayAdapter<String> adapter;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.editTextName);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonSendIntent = findViewById(R.id.buttonSendIntent);
        buttonSaveSharedPref = findViewById(R.id.buttonSaveSharedPref);
        buttonSaveSQLite = findViewById(R.id.buttonSaveSQLite);
        buttonClearList = findViewById(R.id.buttonClearList);
        listViewNames = findViewById(R.id.listViewNames);

        textList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, textList);
        listViewNames.setAdapter(adapter);

        dbHelper = new DBHelper(this);

        buttonAdd.setOnClickListener(view -> {
            String name = editTextName.getText().toString().trim();
            if (!name.isEmpty()) {
                textList.add(name);
                adapter.notifyDataSetChanged();
                editTextName.setText("");
            }
        });

        buttonSendIntent.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putStringArrayListExtra("textList", textList);
            startActivity(intent);
        });

        buttonSaveSharedPref.setOnClickListener(view -> saveListToSharedPreferences(textList));

        buttonSaveSQLite.setOnClickListener(view -> {
            for (String name : textList) {
                dbHelper.insertName(name);
            }
        });

        buttonClearList.setOnClickListener(view -> {
            textList.clear();
            adapter.notifyDataSetChanged();
        });
    }

    private void saveListToSharedPreferences(ArrayList<String> newList) {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String jsonString = prefs.getString("textList", "[]");
        ArrayList<String> existingList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                existingList.add(jsonArray.getString(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String item : newList) {
            if (!existingList.contains(item)) {
                existingList.add(item);
            }
        }

        JSONArray jsonArray = new JSONArray(existingList);
        prefs.edit().putString("textList", jsonArray.toString()).apply();
    }
}
