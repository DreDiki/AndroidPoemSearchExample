package com.drediki.poems;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.drediki.poems.utils.Converter;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String DB_NAME = "/POEM.db";
    public static final String TABLE = "Poems";
    private EditText editText;
    private TextView textView;
    private ListView listView;
    private SimpleCursorAdapter adapter;
//    private ExpandableListView expandableListView;
    private Spinner spinner;
    private SQLiteDatabase sqLiteDatabase;

    //    private int cate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView = (TextView) findViewById(R.id.text);
        listView = (ListView)findViewById(R.id.listView);
        adapter = new SimpleCursorAdapter(this,R.layout.item,null,new String[]{"name","author","content","annotation","translate","comment"},new int[]{R.id.title,R.id.author,R.id.content,R.id.annotation,R.id.translate,R.id.comment},0);
        listView.setAdapter(adapter);
//        expandableListView = (ExpandableListView)findViewById(R.id.expandable);
        editText = (EditText) findViewById(R.id.editText);
        spinner = (Spinner) findViewById(R.id.spinner);
        File file = new File(getFilesDir() + DB_NAME);
        if (!file.exists()){
            sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(getFilesDir() + DB_NAME, null);
            new AlertDialog.Builder(this).setTitle("First Init").setMessage("Couldn't found database,please wait a minute to establish").show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Converter converter = new Converter(MainActivity.this,sqLiteDatabase);
                    converter.run();
                }
            }).run();
        }else {
            sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(getFilesDir() + DB_NAME, null);
        }
//        cursor = sqLiteDatabase.query(TABLE,);
//        cursor.getString()
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Cursor cursor = sqLiteDatabase.query(TABLE,null,null,null,null,null,null,null);
                new Alala().execute();
            }
        });

        /*
        new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Converter converter = new Converter(MainActivity.this);
                        converter.run();
                    }
                }).run();
         */
    }

    public void onClick(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Converter converter = new Converter(MainActivity.this);
                converter.run();
            }
        }).run();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sqLiteDatabase.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class Alala extends AsyncTask<Void,Void,Cursor>{

        private int i;
        private String text;
        @Override
        protected void onPostExecute(Cursor aVoid) {
            adapter.changeCursor(aVoid);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            i = spinner.getSelectedItemPosition();
            text = "%"+editText.getText().toString()+"%";
        }

        @Override
        protected Cursor doInBackground(Void... params) {
            Cursor cursor;
            switch (i) {
                case 0:
                    cursor = sqLiteDatabase.query(TABLE, null, "name like ?", new String[]{text}, null, null, null, null);
                    break;
                case 1:
                    cursor = sqLiteDatabase.query(TABLE, null, "author like ?", new String[]{text}, null, null, null, null);
                    break;
                case 3:
                    cursor = sqLiteDatabase.query(TABLE, null, "comment like ?",  new String[]{text}, null, null, null, null);
                    break;
                case 4:
                    cursor = sqLiteDatabase.query(TABLE, null, "translate like ?", new String[]{text}, null, null, null, null);
                    break;
                case 5:
                    cursor = sqLiteDatabase.query(TABLE, null, "annotation like ?", new String[]{text}, null, null, null, null);
                    break;
                case 2:
                default:
                    cursor = sqLiteDatabase.query(TABLE, null, "content like ?", new String[]{text}, null, null, null, null);
                    break;
            }
//            StringBuilder builder = new StringBuilder();
//            while (cursor.moveToNext()) {
//                builder.append(cursor.getString(1)).append("\n").append(cursor.getString(2)).append("\n").append(cursor.getString(3)).append("\n\n").append(cursor.getString(4)).append("\n\n").append(cursor.getString(5)).append("\n\n").append(cursor.getString(6)).append("\n====================\n\n\n");//获取第二列的值
//                strings.add(builder.toString());
//                builder = new StringBuilder();
//            }
//            cursor.close();
            return cursor;
        }
    }

}
