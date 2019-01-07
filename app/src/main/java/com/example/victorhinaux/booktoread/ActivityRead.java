package com.example.victorhinaux.booktoread;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class ActivityRead extends AppCompatActivity {

    DbHelper dbHelper;
    ArrayAdapter<String> mAdapter;
    ListView lstBookRead;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_activity);

        dbHelper = new DbHelper(this);

        lstBookRead = findViewById(R.id.lstBookRead);
        loadReadBookList();
        final Button back =  findViewById(R.id.btnGoback);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityRead.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        //Change menu icon color
        Drawable icon = menu.getItem(0).getIcon();
        icon.mutate();
        icon.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_book:
                final EditText bookEditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add New Book")
                        .setMessage("What do you want to read next ?")
                        .setView(bookEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String book = String.valueOf(bookEditText.getText());
                                dbHelper.insertNewBook(book);
                                loadReadBookList();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;
            case R.id.action_see_read_book:
                setContentView(R.layout.activity_main);

        }
        return super.onOptionsItemSelected(item);
    }


    private void loadReadBookList() {
        ArrayList<String> bookList = dbHelper.getReadBookList();
        if(mAdapter==null) {
            mAdapter = new ArrayAdapter<String>(this,R.layout.row2,R.id.bookRead_title,bookList);
            lstBookRead.setAdapter(mAdapter);
        }
        else {
            mAdapter.clear();
            mAdapter.addAll(bookList);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void deleteReadBook(View view) {
        View parent = (View)view.getParent();
        TextView bookTextView = (TextView)parent.findViewById(R.id.bookRead_title);
        String book = String.valueOf(bookTextView.getText());
        dbHelper.deleteReadBook(book);
        loadReadBookList();
    }


}
