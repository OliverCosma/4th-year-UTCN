package com.library.app;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class BookAdapter2 extends RecyclerView.Adapter<BookAdapter2.MyViewHolder> {

    Context context;

    ArrayList<Book> list;


    public BookAdapter2(Context context, ArrayList<Book> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Book user = list.get(position);


        holder.nameTV.setText(user.getTitle());
        holder.publisherTV.setText(user.getPublisher());
        holder.pageCountTV.setText("No of Pages : " + user.getPageCount());
        holder.dateTV.setText(user.getPublishedDate());
        //below line is use to set image from URL in our image view.
        String url = user.getThumbnail();
        String url_modified = url.replace("http","https");

        Picasso.get().load(url_modified).into(holder.bookIV);
        holder.itemView.setOnClickListener(v -> {
            //inside on click listner method we are calling a new activity and passing all the data of that item in next intent.
            Intent i = new Intent(context, BookDetails.class);
            i.putExtra("title", user.getTitle());
            i.putExtra("subtitle", user.getSubtitle());
            i.putExtra("authors", user.getAuthors());
            i.putExtra("publisher", user.getPublisher());
            i.putExtra("publishedDate", user.getPublishedDate());
            i.putExtra("description", user.getDescription());
            i.putExtra("pageCount", user.getPageCount());
            i.putExtra("thumbnail", user.getThumbnail());
            i.putExtra("previewLink", user.getPreviewLink());
            i.putExtra("infoLink", user.getInfoLink());
            i.putExtra("buyLink", user.getBuyLink());
            //after passing that data we are starting our new  intent.
            context.startActivity(i);
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nameTV, publisherTV, pageCountTV, dateTV;
        ImageView bookIV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTV = itemView.findViewById(R.id.idTVBookTitle);
            publisherTV = itemView.findViewById(R.id.idTVpublisher);
            pageCountTV = itemView.findViewById(R.id.idTVPageCount);
            dateTV = itemView.findViewById(R.id.idTVDate);
            bookIV = itemView.findViewById(R.id.idIVbook);

        }
    }

}