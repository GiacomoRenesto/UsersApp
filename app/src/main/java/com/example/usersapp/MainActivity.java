package com.example.usersapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;

import com.example.usersapp.Model.Result;
import com.example.usersapp.Model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import at.markushi.ui.CircleButton;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity {

    //ListView listView;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    SwipeRefreshLayout refreshLayout;
    CircleButton deleteSingleBtn;
    //ArrayList<Result> startupResults = new ArrayList<>();
    OrderedRealmCollection<Result> startupResults;
    FloatingActionButton fab;
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
        startupResults = new RealmList<>();
        startupResults = realm.where(Result.class).findAll();
        recyclerView.setAdapter(new RealmAdapter(startupResults));
        final RealmResults<Result> resultsToBeDeleted = realm.where(Result.class).findAll();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.deleteAll();
                                //resultsToBeDeleted.deleteAllFromRealm();
                            }
                        });
                        dialog.dismiss();
                        /*Intent intent = getIntent();
                        finish();
                        startActivity(intent);*/
                        recreate();

                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });
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

    }
}
