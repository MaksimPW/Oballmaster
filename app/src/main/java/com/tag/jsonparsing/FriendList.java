package com.tag.jsonparsing;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FriendList extends ListActivity {
    private static final String TAG_RESP ="response";
    JSONArray resp= null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        String UserId = intent.getStringExtra("func");

        String URL = UserId;

        JSONParser jParser = new JSONParser();

        JSONObject json = jParser.getJSONFrObj(URL);

        try {
            resp = json.getJSONArray(TAG_RESP);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < resp.length(); i++) {
                int S = resp.getInt(i);

                if (i<resp.length()-1){
                sb.append(S+",");
                            }
                else{
                    sb.append(S);
                }
            }

            String result ="http://oball.ru/mobile/get_friend_games?ids="+ sb.toString();
            Intent in = new Intent(getApplicationContext(),VkGamesParsing.class);
            in.putExtra("func",result);
            startActivity(in);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}