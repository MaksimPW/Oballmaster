package com.tag.jsonparsing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SingleVk extends Activity {

    private static final String TAG_NAME = "t_name";
    private static final String TAG_ID ="t_id";
    private static final String TAG_TEAMS = "teams";
    private static final String TAG_GAMES ="games";
    private static final String TAG_DATE = "date_time";
    private static final String TAG_STATUS = "status";
    public String URLSTR;
    private String TAG_TEAMNAME1 = "t1name";
    private String TAG_TEAMNAME2 = "t2name";
    private String TAG_S1="s1";
    private String TAG_S2="s2";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_list_item);

        Intent intent = getIntent();

        String sName = intent.getStringExtra(TAG_NAME);

        TextView lblName = (TextView) findViewById(R.id.TextViewForName);

        String idName = intent.getStringExtra(TAG_ID);

        URLSTR = "http://oball.ru/mobile/get_tournament?trn_id="+idName;

        lblName.setText(sName);

        ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();

        JSONParser jParser = new JSONParser();

        JSONArray json = jParser.getJSONFromUrl(URLSTR);

        try {

            for(int i = 0; i < json.length(); i++) {
                JSONObject c = json.getJSONObject(i);

                String TeamName1 =c.getString(TAG_TEAMNAME1);
                String TeamName2 =c.getString(TAG_TEAMNAME2);
                String date = c.getString(TAG_DATE);
                String s1 = c.getString(TAG_S1);
                String s2 = c.getString(TAG_S2);
                String status =c.getString(TAG_STATUS);

                HashMap<String, String> map = new HashMap<String, String>();

                map.put(TAG_TEAMNAME1, TeamName1 + "  vs  " +TeamName2);
                if (!date.equals("0000-00-00 00:00:00")) {
                    map.put(TAG_DATE, " " + date);
                }else{
                    map.put(TAG_DATE,"Unknown date");
                }

                if (status.equals("2")) {
                    map.put(TAG_S1, " " + s1 + " -- " + s2);
                }else {
                    map.put(TAG_S1,"The game is not played");
                }

                contactList.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListView gamesview = (ListView) findViewById(R.id.GamesView);

        ListAdapter adapter = new SimpleAdapter(this, contactList,
                R.layout.listname_item,
                new String[] { TAG_TEAMNAME1, TAG_DATE ,TAG_S1}, new int[] {
                R.id.TeamName1, R.id.date,R.id.Score});
        gamesview.setAdapter(adapter);
    };

}
