package com.example.usersapp;

import android.content.Context;

public interface Contract {

    interface IView{
       void fabClick(Context context);
       void refreshLayout(Context context);
    }

    interface IPresenter{
         void deleteAllRealm();

    }
}
