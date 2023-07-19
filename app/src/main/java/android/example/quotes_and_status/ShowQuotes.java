package android.example.quotes_and_status;


import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.example.quotes_and_status.QoutesModel.QuotesModel;
import android.example.quotes_and_status.adapters.QuotesAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ShowQuotes extends AppCompatActivity {


    FirebaseFirestore  db = FirebaseFirestore.getInstance();
    ShimmerFrameLayout shimmerFrameLayout;
    ArrayList<QuotesModel> quotesList = new ArrayList<>();

    RecyclerView quotesRecycle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_quotes);


        // change toolbar according to category
        Intent intent = getIntent();
        String strID = intent.getStringExtra("id");
        String strName = intent.getStringExtra("name");

        String first = strName.substring(0,1);
        String remaning = strName.substring(1);
        first = first.toUpperCase();
        strName = first + remaning;

        // array of card background images
        ArrayList<Integer> cardImagesList = new ArrayList<>();

        cardImagesList.add(R.drawable.quote_bg_1);
        cardImagesList.add(R.drawable.quote_bg_2);
        cardImagesList.add(R.drawable.quote_bg_3);
        cardImagesList.add(R.drawable.quote_bg_4);

        // change the toolbar title
        Objects.requireNonNull(getSupportActionBar()).setTitle(strName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        shimmerFrameLayout = findViewById(R.id.post_shimmer_effect);
        shimmerFrameLayout.startShimmer();

        quotesRecycle = (RecyclerView) findViewById(R.id.quotesRecycleView);
        quotesRecycle.setLayoutManager(new LinearLayoutManager(this));
        quotesList = new ArrayList<>();
        QuotesAdapter quotesAdapter = new QuotesAdapter(this,getApplicationContext(), quotesList, cardImagesList);
        quotesRecycle.setAdapter(quotesAdapter);


        // The default cache size threshold is 100 MB. Configure "setCacheSizeBytes"
        // for a different threshold (minimum 1 MB) or set to "CACHE_SIZE_UNLIMITED"
        // to disable clean-up.
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .build();
        db.setFirestoreSettings(settings);

        db.collection("quotes")
                .document(strID)
                .collection("all")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot document : list) {
                            Log.d(TAG, "document => " + document.getData().get("data"));
                            String id = document.getId();
                            String data = String.valueOf(Objects.requireNonNull(document.getData()).get("data"));
                            //boolean isLike = (boolean) document.getData().get("like");
                            Log.d(TAG, "Document ==> " + document.getData());
                            quotesList.add(new QuotesModel(id, data, false));
                        }


                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        // update adapter
                        quotesAdapter.notifyDataSetChanged();
                        quotesRecycle.setVisibility(View.VISIBLE);
                    }
                });

    }

}