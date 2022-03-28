package com.example.mybrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class history extends AppCompatActivity {

    MydbHandler dbHandler=new MydbHandler(this, null, null, 1);
    WebView mywebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        final List<String> sites=dbHandler.databaseToString ();
        if (sites.size()>0) {
            ArrayAdapter myadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,sites);
            ListView mylist = (ListView) findViewById(R.id.listviewHistory);
            mylist.setAdapter(myadapter);
        }
    }
}