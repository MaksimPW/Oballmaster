package com.tag.jsonparsing;

/**
 * Created by maksimpw on 03.07.15.
 */

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
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.HashMap;

public class SavedDBActivity extends Activity {
    final String LOG_TAG = "myLogs";

    final Uri CONTACT_URI = Uri
            .parse("content://com.tag.jsonparsing.TableTourList/contacts");

    final String CONTACT_NAME = "t_name";
    //final String CONTACT_EMAIL = "email";

    DBHelper dbHelper;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_db_activity);



        Cursor cursor = getContentResolver().query(CONTACT_URI, null, null,
                null, null);
        startManagingCursor(cursor);

        String from[] = { "t_name","_id" };
        int to[] = { R.id.name};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.list_item, cursor, from, to);

        ListView lvContact = (ListView) findViewById(R.id.lvContact);
        lvContact.setAdapter(adapter);


        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.d(LOG_TAG, "itemClick: position = " + position + ", id = "
                        + id);

                Intent in = new Intent(getApplicationContext(),SingleVk.class);
                in.putExtra("_id", id);
                //in.putExtra("t_name", name);
                startActivity(in);

            }
        });

        lvContact.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.d(LOG_TAG, "itemSelect: position = " + position + ", id = "
                        + id);

            }

            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(LOG_TAG, "itemSelect: nothing");
            }
        });


        // События нажатия на элемент
        /*
        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                HashMap<String, String> map = (HashMap<String, String>) getListAdapter().getItem(position);
                Intent in = new Intent(getApplicationContext(), SingleVk.class);
                in.putExtra(TAG_NAME, map.get(TAG_NAME));
                in.putExtra(TAG_ID, map.get(TAG_ID));
                startActivity(in);

            }
        });
        */
    }

    /*
    public void onClickInsert(View v) {
        ContentValues cv = new ContentValues();
        cv.put(CONTACT_NAME, "name 4");
        //cv.put(CONTACT_EMAIL, "email 4");
        Uri newUri = getContentResolver().insert(CONTACT_URI, cv);
        Log.d(LOG_TAG, "insert, result Uri : " + newUri.toString());
    }

    public void onClickUpdate(View v) {
        ContentValues cv = new ContentValues();
        cv.put(CONTACT_NAME, "name 5");
        //cv.put(CONTACT_EMAIL, "email 5");
        Uri uri = ContentUris.withAppendedId(CONTACT_URI, 2);
        int cnt = getContentResolver().update(uri, cv, null, null);
        Log.d(LOG_TAG, "update, count = " + cnt);
    }
*/
    public void onClickDelete(View v) {
        //Uri uri = ContentUris.withAppendedId(CONTACT_URI, 3);
        //int cnt = getContentResolver().delete(uri, null, null);
        //Log.d(LOG_TAG, "delete, count = " + cnt);

        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM table_tour_list;");

        Intent in = new Intent(getApplicationContext(),SavedDBActivity.class);
        startActivity(in);
        setContentView(R.layout.saved_db_activity);

    }

    public void onClickError(View v) {
        Uri uri = Uri.parse("content://com.tag.jsonparsing.TableTourList/phones");
        try {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        } catch (Exception ex) {
            Log.d(LOG_TAG, "Error: " + ex.getClass() + ", " + ex.getMessage());
        }


    }

    class DBHelper extends SQLiteOpenHelper {
        final String LOG_TAG = "myLogs";

        public DBHelper(Context context) {
            //
            super(context, "myDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");
            // create table_tour_list
            db.execSQL("create table table_tour_list ("
                    + "t_name text,"
                    + "_id integer primary key" + ");");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
