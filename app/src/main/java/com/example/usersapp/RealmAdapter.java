package com.example.usersapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.usersapp.Model.Result;
import com.squareup.picasso.Picasso;

import at.markushi.ui.CircleButton;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;

public class RealmAdapter extends RealmRecyclerViewAdapter<Result, RealmAdapter.MyViewHolder> {

    private OrderedRealmCollection<Result> results;
    Realm realm;

    public RealmAdapter(OrderedRealmCollection<Result> results) {
        super(results, true);
        this.results = results;
        setHasStableIds(true);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Result result = results.get(position);
        Realm realm = Realm.getDefaultInstance();
        holder.deleteSingleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        result.deleteFromRealm();
                        RealmAdapter.this.notifyDataSetChanged();
                    }
                });
            }
        });
        holder.firstName.setText(result.getName().getFirst());
        holder.lastName.setText(result.getName().getLast());
        holder.email.setText(result.getEmail());
        holder.phone.setText(result.getPhone());

        Picasso.get().load(result.getPicture().getThumbnail()).resize(270, 270).into(holder.imageView);


    }

    @Override
    public long getItemId(int position) {
        return position;
    }


        class MyViewHolder extends RecyclerView.ViewHolder {
            public Result result;
            public TextView firstName, lastName, email, phone;
            public ImageView imageView;
            CircleButton deleteSingleBtn;

            MyViewHolder(View view) {
                super(view);
                firstName = (TextView) view.findViewById(R.id.tvName);
                lastName = (TextView) view.findViewById(R.id.tvLastName);
                email = (TextView) view.findViewById(R.id.tvEmail);
                imageView = (ImageView) view.findViewById(R.id.ivUserIcon);
                phone = (TextView) view.findViewById(R.id.tvPhoneNumber);
                deleteSingleBtn = (CircleButton) view.findViewById(R.id.deleteCircle);
            }
        }
    }