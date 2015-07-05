package com.tag.jsonparsing;


import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import java.util.HashMap;

/**
 * Created by maksimpw on 05.07.15.
 */
public class SavedDBActivitySingle extends Activity {
    final String LOG_TAG = "myLogs";

    final Uri CONTACT_URI = Uri
            .parse("content://com.tag.jsonparsing.TableTourSingle/contacts");

    final String CONTACT_NAME = "t_name";
    final String CONTACT_ID = "_id";

    DBHelper dbHelper;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_list_item);



        Cursor cursor = getContentResolver().query(CONTACT_URI, null, null,
                null, null);
        startManagingCursor(cursor);



        String from[] = { "t_team1", "t_team2", "t_date"};
        int to[] = {R.id.TeamName1, R.id.date,R.id.Score};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.listname_item, cursor, from, to);
        ListView lvContact = (ListView) findViewById(R.id.lvContact);
        //lvContact.setAdapter(adapter);
/*
        String from[] = { "t_name","_id" };
        int to[] = { R.id.name};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.list_item, cursor, from, to);

        ListView lvContact = (ListView) findViewById(R.id.lvContact);
        lvContact.setAdapter(adapter);
 */
    };



    public void onClickUpdate(View v) {
        ContentValues cv = new ContentValues();
        cv.put(CONTACT_NAME, "name 5");
        //cv.put(CONTACT_EMAIL, "email 5");
        Uri uri = ContentUris.withAppendedId(CONTACT_URI, 2);
        int cnt = getContentResolver().update(uri, cv, null, null);
        Log.d(LOG_TAG, "update, count = " + cnt);
    }

    public void onClickDelete(View v) {

        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM table_tour_list;");
        db.execSQL("DELETE FROM table_tour_single;");

        Intent in = new Intent(getApplicationContext(),SavedDBActivity.class);
        startActivity(in);
        setContentView(R.layout.saved_db_activity);



    }

    class DBHelper extends SQLiteOpenHelper {
        final String LOG_TAG = "myLogs";

        public DBHelper(Context context) {
            //
            super(context, "myDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database SavedDBActivitySingle ---");
            // create table_tour_single
            db.execSQL("CREATE TABLE `table_tour_single` (`_id`	INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,`t_team1`	TEXT,`t_team2`	TEXT,`t_s1`	TEXT,`t_s2`	TEXT,`t_date`	TEXT,`t_status`	TEXT,`trn_id`	INTEGER );");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}