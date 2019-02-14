package com.example.usersapp;

import android.content.Context;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.usersapp.Model.Result;
import com.example.usersapp.Model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import io.realm.Realm;
import io.realm.RealmResults;

public class Connection extends AsyncTask<Void, Void, User> {

    private Context context;
    User jsonUser;
    private UsersAdapter adapter;
    private ListView listView;
    private RecyclerView recyclerView;
    List<Result> dbResults;

    public Connection(SwipeRefreshLayout.OnRefreshListener onRefreshListener, RecyclerView recyclerView){}

    //public Connection (Context context, ListView listView) {
        //this.context = context;
        //this.listView = listView;
    //}

    public Connection (Context context,RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
    }


        @Override
    protected User doInBackground(Void... voids) {

        jsonUser = new User();

        try {
            URL url = new URL("https://randomuser.me/api/?results=10");
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            try {
                InputStream inputStream = httpsURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                Gson gson = new Gson();

                Type resultsType = new TypeToken<User>() {
                }.getType();

                jsonUser = gson.fromJson(bufferedReader, resultsType);

            } finally {
                httpsURLConnection.disconnect();
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.insert(jsonUser.getResults());
                    }
                });
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return jsonUser;
    }

    @Override
    protected void onPostExecute(User result) {
        super.onPostExecute(result);
        //User userResult = jsonUser;
        dbResults = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Result> queryResults = realm.where(Result.class).findAll();
                dbResults.addAll(queryResults);
            }
        });
        //adapter = new usersAdapter(context,R.id.listViewUsers, userResult.getResults());
        //listView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
        Collections.reverse(dbResults);
        adapter = new UsersAdapter((ArrayList<Result>) dbResults);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}

