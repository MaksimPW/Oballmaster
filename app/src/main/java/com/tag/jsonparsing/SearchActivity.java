package com.tag.jsonparsing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.perm.kate.api.Api;

public class SearchActivity extends Activity {

    private final int REQUEST_LOGIN=1;

    ImageButton authorizeButton;
    ImageButton logoutButton;
    Button MyGamesButton;
    Button FriendGamesButton;
    Button ChangeUserButton;

    Account account=new Account();
    Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupUI();

        account.restore(this);

        if(account.access_token!=null)
            api=new Api(account.access_token, Constants.API_ID);

        showButtons();
    }

    private View.OnClickListener authorizeClick=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            startLoginActivity();
        }
    };


    private void setupUI() {
        authorizeButton= (ImageButton) findViewById(R.id.AutBut);
        authorizeButton.setOnClickListener(authorizeClick);

        logoutButton=(ImageButton)findViewById(R.id.LogoutButton);
        logoutButton.setOnClickListener(logoutClick);

        MyGamesButton= (Button) findViewById(R.id.MyGamesButton);

        FriendGamesButton= (Button) findViewById(R.id.FriendGamesButton);

        ChangeUserButton= (Button) findViewById(R.id.ChangeUserButton);
        ChangeUserButton.setOnClickListener(authorizeClick);
    }

    private void startLoginActivity() {
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);
    }

    private View.OnClickListener logoutClick=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            logOut();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LOGIN) {
            if (resultCode == RESULT_OK) {
                account.access_token = data.getStringExtra("token");
                account.user_id = data.getLongExtra("user_id", 0);
                account.save(SearchActivity.this);
                api = new Api(account.access_token, Constants.API_ID);
                showButtons();
            }
        }
    }

    private void logOut() {
        api=null;
        account.access_token=null;
        account.user_id=0;
        account.save(SearchActivity.this);
        showButtons();
    }

    void showButtons(){
        if(api!=null){
            authorizeButton.setVisibility(View.GONE);
            logoutButton.setVisibility(View.VISIBLE);
            MyGamesButton.setVisibility(View.VISIBLE);
            FriendGamesButton.setVisibility(View.VISIBLE);
            ChangeUserButton.setVisibility(View.VISIBLE);
        }else{
            authorizeButton.setVisibility(View.VISIBLE);
           logoutButton.setVisibility(View.GONE);
            MyGamesButton.setVisibility(View.GONE);
            FriendGamesButton.setVisibility(View.GONE);
            ChangeUserButton.setVisibility(View.GONE);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void OnClick(View view) {
        final EditText myText = (EditText)findViewById(R.id.editText);
        Intent intent = new Intent(this, TourList.class);
        intent.putExtra("func", "http://oball.ru/mobile/get_tournaments?q="+myText.getText().toString().replaceAll("\\s+", "%20"));
        startActivity(intent);
    }

    public void myGamesClick(View view) {
        Intent intent = new Intent(this, VkGamesParsing.class);
        String myGames = "http://oball.ru/mobile/get_friend_games?ids=" + Long.toString(account.user_id);
        intent.putExtra("func",myGames);
        startActivity(intent);
    }

    public void FriendGamesClick(View view) {
        Intent intent = new Intent(this, FriendList.class);
        String myGames = "https://api.vk.com/method/friends.get?user_id=" + Long.toString(account.user_id);
        intent.putExtra("func",myGames);
        startActivity(intent);
    }
}
