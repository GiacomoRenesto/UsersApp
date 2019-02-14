package com.example.usersapp;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;

import com.example.usersapp.Model.Result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import at.markushi.ui.CircleButton;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity {

    //ListView listView;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    SwipeRefreshLayout refreshLayout;
    CircleButton deleteSingleBtn;
    ArrayList<Result> startupResults = new ArrayList<>();
    //private RecyclerView.Adapter adapter;
    //private List<Result> results;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
        //RealmConfiguration realmConfiguration = new RealmConfiguration();
        //listView = findViewById(R.id.listViewUsers);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new  LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);
        startupResults.addAll(realm.where(Result.class).findAll());
        Collections.reverse(startupResults);
        recyclerView.setAdapter(new UsersAdapter(startupResults));
        /*deleteSingleBtn = (CircleButton)findViewById(R.id.deleteCircle);
        deleteSingleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                    }
                });
            }
        }); */
        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Connection connection = new Connection(getBaseContext(), recyclerView);
                connection.execute();
                refreshLayout.setRefreshing(false);
            }

        });



        //populateUsersList();
    }


    //private void populateUsersList() {
        // Construct the data source
        //ArrayList<User> listOfUsers =  User.getUsers();
        // Create the adapter to convert the array to views
        //usersAdapter adapter = new usersAdapter(this, );
        // Attach the adapter to a ListView
        //ListView listView = (ListView) findViewById(R.id.listViewUsers);
        //listView.setAdapter(adapter);
   // }
}
