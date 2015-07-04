package com.tag.jsonparsing;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class VkGamesParsing extends ListActivity {

    private static final String TAG_NAME = "t_name";
    private static final String TAG_ID = "trn_id";

    DBHelper dbHelper;
    final String LOG_TAG = "myLogs";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DBHelper(this);

        Intent intent = getIntent();

        String sName = intent.getStringExtra("func");

        String url = sName;

        ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();

        JSONParser jParser = new JSONParser();

        JSONArray json = jParser.getJSONFromUrl(url);

        ContentValues cv = new ContentValues();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {

            for(int i = 0; i < json.length(); i++) {
                JSONObject c = json.getJSONObject(i);

                HashMap<String, String> map = new HashMap<String, String>();

                String name = c.getString(TAG_NAME);
                String id = c.getString(TAG_ID);

                cv.put(TAG_NAME, name);
                cv.put(TAG_ID, id);

                long rowID = db.insert("table_tour_list",null,cv);
                Log.d(LOG_TAG, "row inserted, ID = " + rowID);

                map.put(TAG_NAME, name);
                map.put(TAG_ID, id);

                contactList.add(map);

                //map.size();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(this, contactList,
                R.layout.list_item,
                new String[] { TAG_NAME }, new int[] {
                R.id.name});

        setListAdapter(adapter);

        ListView lv = getListView();

        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                HashMap<String, String> map = (HashMap<String, String>)getListAdapter().getItem(position);
                Intent in = new Intent(getApplicationContext(),SingleVk.class);
                in.putExtra(TAG_NAME, map.get(TAG_NAME));
                in.putExtra(TAG_ID, map.get(TAG_ID));
                startActivity(in);

            }
        });


    }

    public void pushDBSingleTour(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("table_tour_list", null, null, null, null, null, null);
        if(c!=null&&c. moveToFirst()){
            do{
                long id = c.getLong(c.getColumnIndexOrThrow ("_id"));
                String name  = c.getString(c.getColumnIndexOrThrow ("t_name"));
                // вытаскиваем список id в обработку добавления singletour
                Intent in = new Intent(getApplicationContext(),SingleVk.class);
                in.putExtra(TAG_NAME, id);
                in.putExtra(TAG_ID, name);
                startActivity(in);

            }while(c.moveToNext());
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


