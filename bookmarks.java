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

public class bookmarks extends AppCompatActivity {

    myDbHandlerBook dbHandlerBook=new myDbHandlerBook(this, null, null, 1);
    WebView mywebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        final List<String>  books=dbHandlerBook.databaseToString();
        if (books.size()>0)
        {
            ArrayAdapter myadapter =new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,books);
            ListView mylist =(ListView) findViewById(R.id.listviewBookmarks);
            mylist.setAdapter(myadapter);

            mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long ld) {

                    String url=books.get (position);
                    Intent intent = new Intent(view.getContext(),MainActivity.class);

                    intent.putExtra("urls", url);

                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}