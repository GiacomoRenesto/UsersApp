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
import com.google.gson.GsonBuilder;
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

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

public class Connection extends AsyncTask<Void, Void, User> {

    private Context context;
    User jsonUser;
    private UsersAdapter adapter;
    private RealmAdapter realmAdapter;
    private ListView listView;
    private RecyclerView recyclerView;
    OrderedRealmCollection <Result> dbResults;

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

                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

                Type resultsType = new TypeToken<User>() {
                }.getType();

                jsonUser = gson.fromJson(bufferedReader, resultsType);

            } finally {
                httpsURLConnection.disconnect();
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        for (Result result : jsonUser.getResults()) {
                            Number currentIdNum = realm.where(Result.class).max("dbId");
                            int nextId;
                            if(currentIdNum == null) {
                                nextId = 1;
                            } else {
                                nextId = currentIdNum.intValue() + 1;
                            }
                            result.setDbId(nextId);
                            realm.insertOrUpdate(result);
                        }
                        //realm.insert(jsonUser.getResults());
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
        //dbResults = new ArrayList<>();
        dbResults = new RealmList<>();
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Result> queryResults = realm.where(Result.class).findAll().sort("dbId", Sort.DESCENDING);
                realmAdapter = new RealmAdapter(queryResults);
            }
        });
        //adapter = new usersAdapter(context,R.id.listViewUsers, userResult.getResults());
        //listView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
        //Collections.reverse(dbResults);
        //realmAdapter = new RealmAdapter(dbResults);
        recyclerView.setAdapter(realmAdapter);
        realmAdapter.notifyDataSetChanged();
    }

}

