package com.example.usersapp.Presenter;

import com.example.usersapp.Contract;

import io.realm.Realm;

public class Presenter implements Contract.IPresenter {

    Realm realm;

    @Override
    public void deleteAllRealm() {

        realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
                //resultsToBeDeleted.deleteAllFromRealm();
            }
        });
    }


}