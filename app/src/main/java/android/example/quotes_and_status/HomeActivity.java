package android.example.quotes_and_status;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.example.quotes_and_status.QoutesModel.CategoryImageMode;
import android.example.quotes_and_status.QoutesModel.CategoryModel;
import android.example.quotes_and_status.adapters.CategoryAdapter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ShimmerFrameLayout shimmerFrameLayout;

    public boolean sound = false;

    private RecyclerView categoryRecycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.dark_black));
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.home_toolbar);
        navigationView = findViewById(R.id.navigation_view);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Quotes and Status");
        // enabling action bar app icon and behaving it as toggle button




        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));













        ArrayList<CategoryModel> categoryList = new ArrayList<CategoryModel>();

        // store image id in array list
        ArrayList<CategoryImageMode> categoryImageList = new ArrayList<>();
        categoryImageList.add(new CategoryImageMode("Alone", R.drawable.ic_icons8_alone__1_));
        categoryImageList.add(new CategoryImageMode("Attitude", R.drawable.ic_icons8_attitude__1_));
        categoryImageList.add(new CategoryImageMode("Awesome", R.drawable.ic_icons8_smiling__1_));
        categoryImageList.add(new CategoryImageMode("Action", R.drawable.ic_icons8_action__1_));
        categoryImageList.add(new CategoryImageMode("Angry", R.drawable.ic_icons8_angry__1_));
        categoryImageList.add(new CategoryImageMode("Love", R.drawable.ic_icons8_love__1_));
        categoryImageList.add(new CategoryImageMode("Anniversary", R.drawable.ic_icons8_wedding_day__2_));
        categoryImageList.add(new CategoryImageMode("Family", R.drawable.ic_icons8_family__2_));
        categoryImageList.add(new CategoryImageMode("Friend", R.drawable.ic_icons8_friend));
        categoryImageList.add(new CategoryImageMode("Relationship", R.drawable.ic_icons8_relationship));
        categoryImageList.add(new CategoryImageMode("Success", R.drawable.ic_icons8_success));
        categoryImageList.add(new CategoryImageMode("Cool", R.drawable.ic_icons8_cool));
        categoryImageList.add(new CategoryImageMode("Life", R.drawable.ic_icons8_life));
        categoryImageList.add(new CategoryImageMode("Funny", R.drawable.ic_icons8_steven_universe));
        categoryImageList.add(new CategoryImageMode("Beauty", R.drawable.ic_icons8_lipstick));
        categoryImageList.add(new CategoryImageMode("Fitness", R.drawable.ic_icons8_fitness_1));
        categoryImageList.add(new CategoryImageMode("Sad", R.drawable.ic_icons8_sad));
        categoryImageList.add(new CategoryImageMode("Motivational", R.drawable.ic_icons8_motivated));
        categoryImageList.add(new CategoryImageMode("Nature", R.drawable.ic_icons8_afternoon));
        categoryImageList.add(new CategoryImageMode("Inspirational", R.drawable.ic_icons8_inspiration));

        // shimmer frame layout
        shimmerFrameLayout = (ShimmerFrameLayout) findViewById(R.id.shimmer_effect);
        shimmerFrameLayout.startShimmer();

        categoryRecycleView = (RecyclerView) findViewById(R.id.categoryRecycleView);
        //categoryRecycleView.setLayoutManager(new LinearLayoutManager(this));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        categoryRecycleView.setLayoutManager(gridLayoutManager);

        CategoryAdapter categoryAdapter = new CategoryAdapter(this,categoryList, categoryImageList);
        categoryRecycleView.setAdapter(categoryAdapter);


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // The default cache size threshold is 100 MB. Configure "setCacheSizeBytes"
        // for a different threshold (minimum 1 MB) or set to "CACHE_SIZE_UNLIMITED"
        // to disable clean-up.
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .build();

        db.setFirestoreSettings(settings);
        db.collection("quotes")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot document : list) {
                            String id = document.getId();
                            String name = String.valueOf(Objects.requireNonNull(document.getData()).get("name"));
                            categoryList.add(new CategoryModel(id,name));
                            Log.d(TAG, "Document ==> " + id + " ==> " + name);
                        }

                        Collections.sort(categoryList, new Comparator<CategoryModel>(){
                            @Override
                            public int compare(CategoryModel c1, CategoryModel c2){
                                return c1.getName().compareToIgnoreCase(c2.getName());
                            }
                        });

                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        categoryAdapter.notifyDataSetChanged();
                        categoryRecycleView.setVisibility(View.VISIBLE);
                    }
                });





        // navigate through all menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                switch (id) {

                    case R.id.home_action:

                        break;

                    case R.id.rate_us_action:
                        Uri uri = Uri.parse("\"https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName());
                        Intent rateUsIntent = new Intent(Intent.ACTION_VIEW, uri);

                        try{
                            startActivity(rateUsIntent);
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }

                        break;

                    case R.id.share_us_action:
                        break;

                    case R.id.privacy_action:
                        Intent privacyIntent = new Intent(HomeActivity.this, PrivacyPolicy.class);
                        startActivity(privacyIntent);
                }


                // close drawer when user click on any menu item
                drawerLayout.closeDrawer(GravityCompat.START);


                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        // Associate searchable configuration with the SearchView

        Switch switchCompat = (Switch) menu.findItem(R.id.toolbar_switch_action)
                .getActionView();

        switchCompat.setChecked(false);
        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 sound = (switchCompat.isChecked()) ?  true :  false;
            }
            
        });

        return true;
    }

}

