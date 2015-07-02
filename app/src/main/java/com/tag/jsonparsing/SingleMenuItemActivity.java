package com.tag.jsonparsing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SingleMenuItemActivity  extends Activity {

	private static final String TAG_NAME = "t_name";
    private static final String TAG_ID ="t_id";
    private static final String TAG_TEAMS = "teams";
    private static final String TAG_GAMES ="games";
    private static final String TAG_DATE = "date_time";
    private static final String TAG_STATUS = "status";
    public String URLSTR;
    private String TAG_TEAM1="team1";
    private String TAG_TEAM2="team2";
    private String TAG_S1="s1";
    private String TAG_S2="s2";
    JSONArray games = null;

    String ReturnName (String t_id){
        JSONArray teams = null;
        final String TAG_NAME2 = "name";
        final String TAG_ID2 ="id";

        ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();

        JSONParser jParser = new JSONParser();

        JSONObject json = jParser.getJSONFrObj(URLSTR);

        HashMap<String, String> map = new HashMap<String, String>();

        try {
            teams = json.getJSONArray(TAG_TEAMS);

            for(int i = 0; i < teams.length(); i++){
                JSONObject c = teams.getJSONObject(i);

                String id = c.getString(TAG_ID2);
                String name = c.getString(TAG_NAME2);

                map.put(id, name);

                contactList.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String result = "";
        for (HashMap.Entry entry : map.entrySet()) {
            String key = String.valueOf(entry.getKey());
            String value = String.valueOf(entry.getValue());
            if (t_id.equals(key)){
                result = value;
                return result;
            }
        }
        return result;
    }

	@Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_list_item);

        Toast.makeText(this, "Please wait", Toast.LENGTH_LONG).show();

        Intent intent = getIntent();

        String sName = intent.getStringExtra(TAG_NAME);

        TextView lblName = (TextView) findViewById(R.id.TextViewForName);

        String idName = intent.getStringExtra(TAG_ID);

        URLSTR = "http://oball.ru/mobile/get_tournament?trn_id="+idName;

        lblName.setText(sName);

        ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();

        JSONParser jParser = new JSONParser();

        JSONObject json = jParser.getJSONFrObj(URLSTR);

        try {
            games = json.getJSONArray(TAG_GAMES);

            for(int i = 0; i < games.length(); i++) {
                JSONObject c = games.getJSONObject(i);

                String team1 = c.getString(TAG_TEAM1);
                String TeamName1 =ReturnName(team1);
                String team2 = c.getString(TAG_TEAM2);
                String TeamName2 =ReturnName(team2);
                String date = c.getString(TAG_DATE);
                String s1 = c.getString(TAG_S1);
                String s2 = c.getString(TAG_S2);
                String status =c.getString(TAG_STATUS);

                HashMap<String, String> map = new HashMap<String, String>();

                map.put(TAG_TEAM1, TeamName1 + "  vs  " +TeamName2);
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
                new String[] { TAG_TEAM1, TAG_DATE ,TAG_S1}, new int[] {
                R.id.TeamName1, R.id.date,R.id.Score});
        gamesview.setAdapter(adapter);
        };

    }
