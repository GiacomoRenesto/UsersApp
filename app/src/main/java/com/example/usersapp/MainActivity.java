package com.example.usersapp;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.usersapp.Model.Result;
import com.example.usersapp.Presenter.Connection;
import com.example.usersapp.Presenter.Presenter;

import at.markushi.ui.CircleButton;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity implements Contract.IView {

    //ListView listView;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    SwipeRefreshLayout refreshLayout;
    CircleButton deleteSingleBtn;
    OrderedRealmCollection<Result> startupResults;
    FloatingActionButton fab;
    Presenter presenter;
    SwipeRefreshLayout setRefreshing;
    //ArrayList<Result> startupResults = new ArrayList<>();
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
        layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);
        startupResults = new RealmList<>();
        startupResults = realm.where(Result.class).findAll();
        recyclerView.setAdapter(new RealmAdapter(startupResults));
        final RealmResults<Result> resultsToBeDeleted = realm.where(Result.class).findAll();
        deleteSingleBtn = (CircleButton)findViewById(R.id.deleteCircle);
        fab = findViewById(R.id.fab);
        setRefreshing = findViewById(R.id.refreshLayout);
        fabClick(getApplicationContext());
        refreshLayout(getApplicationContext());
    }


    @Override
    public void fabClick(Context context) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        presenter = new Presenter();
                        presenter.deleteAllRealm();

                        dialog.dismiss();
                        /*Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                        recreate();*/

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

    }

    @Override
    public void refreshLayout(Context context) {
        setRefreshing.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            //private RecyclerView recyclerView;

            @Override
            public void onRefresh() {
                ConnectivityManager cm =
                        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if (!isConnected) {
                    Snackbar mySnackbar = Snackbar.make(getWindow().getDecorView(),
                            "You are offline, check your connection", Snackbar.LENGTH_LONG);
                    mySnackbar.show();
                } else {
                    Connection connection = new Connection(context, recyclerView);
                    connection.execute();

                }
                setRefreshing.setRefreshing(false);
            }

        });
    }
}
