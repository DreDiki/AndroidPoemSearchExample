package com.drediki.poems.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Created by DreDiki on 2017/2/22.
 */

public class Converter {

    public static final String DB_NAME = "/POEM.db";
    public static final String TABLE = "Poems";
    private SQLiteDatabase sqLiteDatabase;
    private Context context;

    public Converter(Context context) {
        this.context = context;
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(context.getFilesDir() + DB_NAME, null);
    }
    public Converter(Context context,SQLiteDatabase sqLiteDatabase) {
        this.context = context;
        this.sqLiteDatabase = sqLiteDatabase;
    }

    public void run() {
        initSQL();
        try {
            read(context.getAssets().open("p (1).txt"));
            read(context.getAssets().open("p (2).txt"));
            read(context.getAssets().open("p (3).txt"));
            read(context.getAssets().open("p (4).txt"));
            read(context.getAssets().open("p (5).txt"));
            read(context.getAssets().open("p (6).txt"));
            read(context.getAssets().open("p (7).txt"));
            read(context.getAssets().open("p (8).txt"));
            read(context.getAssets().open("p (9).txt"));
            read(context.getAssets().open("p (10).txt"));
            read(context.getAssets().open("p (11).txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //sqLiteDatabase.close();//right position?
    }

    public void initSQL() {
        try {
            sqLiteDatabase.execSQL("create table " + TABLE + "(_id integer primary key autoincrement,name text,author text,content text,annotation text,translate text,comment text)");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void read(InputStream inputStream) {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        String name = "", author = "", comment = "", translate = "", annotation = "", content = "";
        ContentValues contentValues;
        int state = -1;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                switch (state) {
                    case 1://name
                        name = line.substring(1, line.length() - 1);
                        state++;
                        break;
                    case 2://author
                        author = line.substring(3);
                        state++;
                        break;
                    case 3://content
                        if (line.length() > 1) {
                            if (line.contains("【注解】")) {
                                state++;
                                break;
                            }
                            content = content + line;
                        }
                        break;
                    case 4://annotation
                        if (line.length() > 1) {
                            if (line.contains("【韵译】")) {
                                state++;
                                break;
                            }
                            annotation = annotation + line;
                        }
                        break;
                    case 5://trans
                        if (line.length() > 1) {
                            if (line.contains("【评析】")) {
                                state++;
                                break;
                            }
                            translate = translate + line;
                        }
                        break;
                    case 6://comment
                        if (line.length() > 1) {
                            if (line.contains("=============================")) {
                                //finish , add to db
                                Log.i(TABLE, name);
                                contentValues = new ContentValues();
                                contentValues.put("name", name);
                                contentValues.put("author", author);
                                contentValues.put("content", content);
                                contentValues.put("annotation", annotation);
                                contentValues.put("translate", translate);
                                contentValues.put("comment", comment);
                                sqLiteDatabase.insert(TABLE, null, contentValues);

                                //zz
                                name = "";
                                author = "";
                                comment = "";
                                translate = "";
                                annotation = "";
                                content = "";
                                state = 1;
                                break;
                            }
                            comment = comment + line;
                        }
                        break;
                    default:
                        if (line.contains("=============================")) {
                            state = 1;
                            name = "";
                            author = "";
                            comment = "";
                            translate = "";
                            annotation = "";
                            content = "";
                            break;
                        }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
