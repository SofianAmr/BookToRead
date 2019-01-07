package com.example.victorhinaux.booktoread;


import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
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

public class MainActivity extends AppCompatActivity {

    DbHelper dbHelper;
    ArrayAdapter<String> mAdapter;
    ListView lstBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(this);

        lstBook = findViewById(R.id.lstBook);

        loadBookList();
    }

    private void loadBookList() {
        ArrayList<String> bookList = dbHelper.getBookList();
        if(mAdapter==null) {
            mAdapter = new ArrayAdapter<String>(this,R.layout.row,R.id.book_title,bookList);
            lstBook.setAdapter(mAdapter);
        }
        else {
            mAdapter.clear();
            mAdapter.addAll(bookList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
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
                                loadBookList();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;
            case R.id.action_see_read_book:
                Intent i = new Intent(this,ActivityRead.class);
                this.startActivity(i);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteBook(View view) {
        View parent = (View)view.getParent();
        TextView bookTextView = (TextView)parent.findViewById(R.id.book_title);
        String book = String.valueOf(bookTextView.getText());
        dbHelper.deleteBook(book);
        loadBookList();
    }

    public void readBook(View view) {
        View parent = (View)view.getParent();
        TextView bookTextView = (TextView)parent.findViewById(R.id.book_title);
        String book = String.valueOf(bookTextView.getText());
        dbHelper.insertNewReadBook(book);
        dbHelper.deleteBook(book);
        loadBookList();
    }
}
