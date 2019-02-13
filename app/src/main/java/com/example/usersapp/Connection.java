package com.example.usersapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class Connection extends AsyncTask<Void, Void, User> {

    private Context context;
    User jsonUser;
    private usersAdapter adapter;
    private ListView listView;
    private RecyclerView recyclerView;

    public Connection (){}

    //public Connection (Context context, ListView listView) {
        //this.context = context;
        //this.listView = listView;
    //}

    public Connection (Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
    }


        @Override
    protected User doInBackground(Void... voids) {

        jsonUser = new User();

        try {
            URL url = new URL("https://randomuser.me/api/?results=100");
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
        User userResult = jsonUser;
        //adapter = new usersAdapter(context,R.id.listViewUsers, userResult.getResults());
        //listView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
        adapter = new usersAdapter(result.getResults());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}

