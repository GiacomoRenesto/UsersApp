package com.example.usersapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.usersapp.Model.Picture;
import com.example.usersapp.Model.Result;
import com.example.usersapp.Model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//public class usersAdapter extends ArrayAdapter<Result> {

   // public usersAdapter(Context context, int resource, ArrayList<Result> results) {
   //    super(context, resource, results);
   // }
public class usersAdapter extends RecyclerView.Adapter<usersAdapter.MyViewHolder>{

    private ArrayList<Result> results;

    public usersAdapter(ArrayList<Result> results){
        this.results = results;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView firstName,lastName,email,phone;
        public ImageView imageView;
        public MyViewHolder(View view){
            super (view);
            firstName = (TextView) view.findViewById(R.id.tvName);
            lastName = (TextView) view.findViewById(R.id.tvLastName);
            email = (TextView) view.findViewById(R.id.tvEmail);
            imageView = (ImageView) view.findViewById(R.id.ivUserIcon);
            phone = (TextView) view.findViewById(R.id.tvPhoneNumber);
        }
    }

    public usersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    public void onBindViewHolder(MyViewHolder holder, int position){
        Result result = results.get(position);
        holder.firstName.setText(result.getName().getFirst());
        holder.lastName.setText(result.getName().getLast());
        holder.email.setText(result.getEmail());
        holder.phone.setText(result.getPhone());

        Picasso.get().load(result.getPicture().getThumbnail()).resize(250,250).into(holder.imageView);
    }

    public int getItemCount(){
        return results.size();
    }

    /*@Override
    public  View getView(int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
        }

        // Get the data item for this position
        Result result = getItem(position);

        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvHome = (TextView) convertView.findViewById(R.id.tvHometown);
        // Populate the data into the template view using the data object
        tvName.setText(result.getName().getFirst());
        tvHome.setText(result.getLocation().getCity());
        // Return the completed view to render on screen
        return convertView;
    }*/
}

