package com.example.usersapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import com.example.usersapp.Model.Result;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    ListView listView;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private List<Result> results;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //listView = findViewById(R.id.listViewUsers);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new  LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);
        Connection connection = new Connection(this, recyclerView);
        connection.execute();


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
