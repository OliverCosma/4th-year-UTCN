package com.library.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookDetails extends AppCompatActivity {
    //creating variables for strings,text view, image views and button.
    String title, subtitle, publisher, publishedDate, description, thumbnail, previewLink, infoLink, buyLink;
    int pageCount;
    private ArrayList<String> authors;

    TextView titleTV, subtitleTV, publisherTV, descTV, pageTV, publishDateTV;
    Button previewBtn, buyBtn, sendDataBtn;
    private ImageView bookIV;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    BookToSave bookToSave;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        //initializing our views..
        titleTV = findViewById(R.id.idTVTitle);
        subtitleTV = findViewById(R.id.idTVSubTitle);
        publisherTV = findViewById(R.id.idTVpublisher);
        descTV = findViewById(R.id.idTVDescription);
        pageTV = findViewById(R.id.idTVNoOfPages);
        publishDateTV = findViewById(R.id.idTVPublishDate);
        previewBtn = findViewById(R.id.idBtnPreview);
        buyBtn = findViewById(R.id.idBtnBuy);
        bookIV = findViewById(R.id.idIVbook);

        //getting the data which we have passed from our adapter class.
        title = getIntent().getStringExtra("title");
        subtitle = getIntent().getStringExtra("subtitle");
        publisher = getIntent().getStringExtra("publisher");
        publishedDate = getIntent().getStringExtra("publishedDate");
        description = getIntent().getStringExtra("description");
        pageCount = getIntent().getIntExtra("pageCount", 0);
        thumbnail = getIntent().getStringExtra("thumbnail");
        previewLink = getIntent().getStringExtra("previewLink");
        infoLink = getIntent().getStringExtra("infoLink");
        buyLink = getIntent().getStringExtra("buyLink");

        //after getting the data we are setting that data to our text views and image view.
        titleTV.setText(title);
        subtitleTV.setText(subtitle);
        publisherTV.setText(publisher);
        publishDateTV.setText("Published On : " + publishedDate);
        descTV.setText(description);
        pageTV.setText("No Of Pages : " + pageCount);

        String url_modified = thumbnail.replace("http", "https");

        Glide.with(this).load(url_modified).into(bookIV);

        //adding on click listener for our preview button.howho
        previewBtn.setOnClickListener(v -> {
            if (previewLink.isEmpty()) {
                //below toast message is displayed when preview link is not present.
                Toast.makeText(BookDetails.this, "No preview Link present", Toast.LENGTH_SHORT).show();
                return;
            }
            //if the link is present we are opening that link via an intent.
            Uri uri = Uri.parse(previewLink);
            Intent i = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(i);
        });

        //initializing on click listener for buy button.
        buyBtn.setOnClickListener(v -> {
            if (buyLink.isEmpty()) {
                //below toast message is displaying when buy link is empty.
                Toast.makeText(BookDetails.this, "No buy page present for this book", Toast.LENGTH_SHORT).show();
                return;
            }
            //if the link is present we are opening the link via an intent.
            Uri uri = Uri.parse(buyLink);
            Intent i = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(i);
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("BookToSave");

        bookToSave = new BookToSave();
        sendDataBtn = findViewById(R.id.idBtnSave);

        sendDataBtn.setOnClickListener(view -> {
            addDatatoFirebase(title, subtitle, publisher, publishedDate, description, pageCount,
                    thumbnail, previewLink, infoLink, buyLink);
        });


    }

    private void addDatatoFirebase(String title, String subtitle, String publisher,
                                   String publishedDate, String description, int pageCount, String thumbnail,
                                   String previewLink, String infoLink, String buyLink) {
                    bookToSave.setBuyLink(buyLink);
                    bookToSave.setDescription(description);
                    bookToSave.setTitle(title);
                    bookToSave.setSubtitle(subtitle);
                    bookToSave.setPublisher(publisher);
                    bookToSave.setPublishedDate(publishedDate);
                    bookToSave.setPageCount(pageCount);
                    bookToSave.setThumbnail(thumbnail);
                    bookToSave.setPreviewLink(previewLink);
                    bookToSave.setInfoLink(infoLink);

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            databaseReference.child(title).setValue(bookToSave);
                            Toast.makeText(BookDetails.this,"data added", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(BookDetails.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
    }
}