package com.waheed.reypostre.recipes.projects.android.reypostre;

import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.ads.MobileAds;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

interface ItemClickListener {
    void onItemClick(View view, int position);
}

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener, ItemClickListener {

    Toolbar toolbar;

    //GridView gridView;
    RecyclerView recyclerView;

    //CustomAdapter customAdapter;
    MyRecyclerViewAdapter customAdapter;
    CartRecyclerViewAdapter customCartAdapter;

    ArrayList<recipe> recipeArrayList = new ArrayList<>();

    ArrayList<recipe> homeRecipeList = new ArrayList<>();
    ArrayList<recipe> newRecipeList = new ArrayList<>();
    ArrayList<recipe> cakesRecipeList = new ArrayList<>();
    ArrayList<recipe> nobakeRecipeList = new ArrayList<>();
    ArrayList<recipe> cupcakeRecipeList = new ArrayList<>();
    ArrayList<recipe> pastryRecipeList = new ArrayList<>();
    ArrayList<recipe> pieRecipeList = new ArrayList<>();
    ArrayList<recipe> icecreamRecipeList = new ArrayList<>();
    ArrayList<recipe> kidsRecipeList = new ArrayList<>();
    ArrayList<recipe> arabRecipeList = new ArrayList<>();
    ArrayList<recipe> puddingRecipeList = new ArrayList<>();
    ArrayList<recipe> asiaRecipeList = new ArrayList<>();
    ArrayList<recipe> breakfastRecipeList = new ArrayList<>();
    ArrayList<recipe> otherRecipeList = new ArrayList<>();

    Context context = this;
    DatabaseReference ref;
    DatabaseReference cakeRef;
    DatabaseReference nobakeRef;
    DatabaseReference cupcakeRef;
    DatabaseReference pastryRef;
    DatabaseReference pieRef;
    DatabaseReference iceCreamRef;
    DatabaseReference kidsRef;
    DatabaseReference arabicRef;
    DatabaseReference puddingRef;
    DatabaseReference asianRef;
    DatabaseReference breakfastRef;
    DatabaseReference otherRef;
    DatabaseReference newRef;
   //FloatingActionButton fab;
    int lastP;
    LinearLayout noItemText;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar spinner;
    public static final String PREFS_NAME = "MyPrefsFile";
    ArrayList<recipe>[] retRec;
    ArrayList<cart>[] cartList;

    SQLiteDatabase database;
    BottomNavigationView navigation;
    NavigationView navigationView;
    MenuItem itemm;
    MenuItem deletItem;
    MenuItem addToCartItem;
    Menu menuToDelete;
    DrawerLayout drawer;

    LinearLayout noInCart;
    int numberOfColumns = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean dialogShown = settings.getBoolean("dialogShown", false);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);


        recyclerView = (RecyclerView) findViewById(R.id.gridview);
        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));


        noInCart = (LinearLayout) findViewById(R.id.noInCart);

        //fab = (FloatingActionButton) findViewById(R.id.fab);

       /* fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailentent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "ahmed.waheed.elanwerr@gmail.com", null));
                emailentent.putExtra(Intent.EXTRA_SUBJECT, "Your dessert name..");
                emailentent.putExtra(Intent.EXTRA_TEXT, "Your dessert data..");
                startActivity(Intent.createChooser(emailentent, "Send recipe.."));
            }
        });*/

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_home);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                itemm = menuToDelete.findItem(R.id.action_search);
                deletItem = menuToDelete.findItem(R.id.action_delete);
                addToCartItem = menuToDelete.findItem(R.id.action_add_to_cart);

                switch (item.getItemId()) {
                    case R.id.navigation_home:

                        toolbar.setTitle("Home");
                        noItemText.setVisibility(View.GONE);
                        itemm.setVisible(true);
                        deletItem.setVisible(false);
                        addToCartItem.setVisible(false);
                        navigationView.setCheckedItem(R.id.nav_home);

                        if(!toolbar.getTitle().equals("Shopping cart")){
                            numberOfColumns = 2;
                            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
                        }else{
                            numberOfColumns = 1;
                            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
                        }

                        if(noInCart.getVisibility() == View.VISIBLE){
                            noInCart.setVisibility(View.GONE);
                        }

                        if (homeRecipeList.size() > 0) {
                            customAdapter = new MyRecyclerViewAdapter(getBaseContext(), homeRecipeList);
                            customAdapter.setClickListener(MainActivity.this);
                            recyclerView.setAdapter(customAdapter);
                            spinner.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
                        }

                        //fab.show();

                        return true;
                    case R.id.navigation_fav:

                        toolbar.setTitle("Favourites");
                        itemm.setVisible(false);
                        deletItem.setVisible(true);
                        addToCartItem.setVisible(false);

                        navigationView.setCheckedItem(R.id.nav_favorites);
                        SQLiteDatabase sqLiteDatabase = new SQLiteDatabase(getBaseContext());
                        retRec = new ArrayList[]{sqLiteDatabase.ReadData()};

                        if(!toolbar.getTitle().equals("Shopping cart")){
                            numberOfColumns = 2;
                            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
                        }else{
                            numberOfColumns = 1;
                            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
                        }

                        if(noInCart.getVisibility() == View.VISIBLE){
                            noInCart.setVisibility(View.GONE);
                        }

                        noItemText.setVisibility(View.VISIBLE);

                        if (!retRec[0].isEmpty()) {
                            noItemText.setVisibility(View.GONE);
                        }

                        customAdapter = new MyRecyclerViewAdapter(getBaseContext(), retRec[0]);
                        customAdapter.setClickListener(MainActivity.this);
                        recyclerView.setAdapter(customAdapter);


                        database = new SQLiteDatabase(context);


                        //fab.show();

                        return true;
                    case R.id.navigation_new:

                        noItemText.setVisibility(View.GONE);
                        toolbar.setTitle("New recipes");
                        itemm.setVisible(false);
                        deletItem.setVisible(false);
                        addToCartItem.setVisible(false);
                        navigationView.setCheckedItem(R.id.nav_new);
                        if(!toolbar.getTitle().equals("Shopping cart")){
                            numberOfColumns = 2;
                            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
                        }else{
                            numberOfColumns = 1;
                            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
                        }

                        if(noInCart.getVisibility() == View.VISIBLE){
                            noInCart.setVisibility(View.GONE);
                        }


                        if (newRecipeList.size() > 0) {
                            customAdapter = new MyRecyclerViewAdapter(getBaseContext(), newRecipeList);
                            customAdapter.setClickListener(MainActivity.this);
                            recyclerView.setAdapter(customAdapter);
                            spinner.setVisibility(View.GONE);

                        } else {
                            Toast.makeText(context, "No new data", Toast.LENGTH_SHORT).show();
                        }

                        //fab.show();

                        return true;
                    case R.id.navigation_category:
                        drawer.openDrawer(Gravity.START);


                        return true;
                    case R.id.navigation_cart:
                        toolbar.setTitle("Shopping cart");
                        noItemText.setVisibility(View.GONE);
                        itemm.setVisible(false);
                        deletItem.setVisible(true);
                        addToCartItem.setVisible(true);

                        navigationView.setCheckedItem(R.id.nav_cart);

                        if(!toolbar.getTitle().equals("Shopping cart")){
                            numberOfColumns = 2;
                            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
                        }else{
                            numberOfColumns = 1;
                            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));

                            SQLiteDatabase cartdb = new SQLiteDatabase(getBaseContext());
                            cartList = new ArrayList[]{cartdb.ReadDataFromCart()};

                            if (cartList[0].isEmpty()) {
                                noInCart.setVisibility(View.VISIBLE);
                            }else{
                                noInCart.setVisibility(View.GONE);
                            }

                            customCartAdapter = new CartRecyclerViewAdapter(getBaseContext(), cartList[0]);
                            customCartAdapter.setClickListener(MainActivity.this);
                            recyclerView.setAdapter(customCartAdapter);


                        }

                        return true;
                }

                return false;
            }
        });

        if (!dialogShown) {
            // AlertDialog code here
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setCancelable(false);
            alertDialog.setTitle("User Guide");
            alertDialog.setMessage("this is important to use our app.. \n \nYou can go to any category from the side menu \n \nYou can send us your recipes by clicking on the send button in home window \n \nDon't forget to rate our app :) \n" +
                    "Hello Dessert Team.. ");
            alertDialog.setNegativeButton("Got it", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("dialogShown", true);
            editor.apply();
        }


        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        noItemText = (LinearLayout) findViewById(R.id.text_No);
        noItemText.setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            findViewById(R.id.gradientShadow).setVisibility(View.GONE);
        }




        //layoutManager = ((GridLayoutManager) recyclerView.getLayoutManager());


        //lastP = layoutManager.findFirstVisibleItemPosition();

        //coordinatorLayout = (LinearLayout) findViewById(R.id.layoutt);

       /* recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (!toolbar.getTitle().equals("Fuck El Ex")) {
                    GridLayoutManager layoutManager = ((GridLayoutManager) recyclerView.getLayoutManager());

                    int currentP = layoutManager.findFirstVisibleItemPosition();
                    if (currentP > lastP) {
                        // scroll down
                        fab.hide();

                    }
                    if (currentP < lastP) {
                        // scroll up
                        fab.show();
                    }

                    lastP = currentP;
                } else {
                    fab.hide();
                }
            }
        });*/


        ref = FirebaseDatabase.getInstance().getReference().child("HomeDesserts");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                homeRecipeList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    recipe rec = new recipe();
                    rec.setName(ds.getValue(recipe.class).getName());
                    rec.setUrl(ds.getValue(recipe.class).getUrl());
                    rec.setTime(ds.getValue(recipe.class).getTime());
                    rec.setLevel(ds.getValue(recipe.class).getLevel());
                    rec.setServing(ds.getValue(recipe.class).getServing());
                    rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                    rec.setStep(ds.getValue(recipe.class).getStep());

                    homeRecipeList.add(rec);
                }


                if(toolbar.getTitle().equals("Home")) {
                    if (homeRecipeList.size() > 0) {
                        customAdapter = new MyRecyclerViewAdapter(getBaseContext(), homeRecipeList);
                        customAdapter.setClickListener(MainActivity.this);
                        recyclerView.setAdapter(customAdapter);
                        spinner.setVisibility(View.GONE);

                    } else {
                        Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        newRef = FirebaseDatabase.getInstance().getReference().child("New Recipes");
        newRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                newRecipeList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    recipe rec = new recipe();
                    rec.setName(ds.getValue(recipe.class).getName());
                    rec.setUrl(ds.getValue(recipe.class).getUrl());
                    rec.setTime(ds.getValue(recipe.class).getTime());
                    rec.setLevel(ds.getValue(recipe.class).getLevel());
                    rec.setServing(ds.getValue(recipe.class).getServing());
                    rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                    rec.setStep(ds.getValue(recipe.class).getStep());

                    newRecipeList.add(rec);
                }


                if(toolbar.getTitle().equals("New recipes")) {
                    if (newRecipeList.size() > 0) {
                        customAdapter = new MyRecyclerViewAdapter(getBaseContext(), newRecipeList);
                        customAdapter.setClickListener(MainActivity.this);
                        recyclerView.setAdapter(customAdapter);
                        spinner.setVisibility(View.GONE);

                    } else {
                        Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        cakeRef = FirebaseDatabase.getInstance().getReference().child("cakes");
        cakeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                cakesRecipeList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    recipe rec = new recipe();
                    rec.setName(ds.getValue(recipe.class).getName());
                    rec.setUrl(ds.getValue(recipe.class).getUrl());
                    rec.setTime(ds.getValue(recipe.class).getTime());
                    rec.setLevel(ds.getValue(recipe.class).getLevel());
                    rec.setServing(ds.getValue(recipe.class).getServing());
                    rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                    rec.setStep(ds.getValue(recipe.class).getStep());

                    cakesRecipeList.add(rec);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        nobakeRef = FirebaseDatabase.getInstance().getReference().child("NoBake");
        nobakeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                nobakeRecipeList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    recipe rec = new recipe();
                    rec.setName(ds.getValue(recipe.class).getName());
                    rec.setUrl(ds.getValue(recipe.class).getUrl());
                    rec.setTime(ds.getValue(recipe.class).getTime());
                    rec.setLevel(ds.getValue(recipe.class).getLevel());
                    rec.setServing(ds.getValue(recipe.class).getServing());
                    rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                    rec.setStep(ds.getValue(recipe.class).getStep());

                    nobakeRecipeList.add(rec);
                }

                if(toolbar.getTitle().equals("No bake")) {

                    if (nobakeRecipeList.size() > 0) {
                        customAdapter = new MyRecyclerViewAdapter(getBaseContext(), nobakeRecipeList);
                        customAdapter.setClickListener(MainActivity.this);
                        recyclerView.setAdapter(customAdapter);
                        spinner.setVisibility(View.GONE);

                    } else {
                        Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        cupcakeRef = FirebaseDatabase.getInstance().getReference().child("cupcake");
        cupcakeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                cupcakeRecipeList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    recipe rec = new recipe();
                    rec.setName(ds.getValue(recipe.class).getName());
                    rec.setUrl(ds.getValue(recipe.class).getUrl());
                    rec.setTime(ds.getValue(recipe.class).getTime());
                    rec.setLevel(ds.getValue(recipe.class).getLevel());
                    rec.setServing(ds.getValue(recipe.class).getServing());
                    rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                    rec.setStep(ds.getValue(recipe.class).getStep());

                    cupcakeRecipeList.add(rec);
                }

                if(toolbar.getTitle().equals("Cupcakes")) {

                    if (cupcakeRecipeList.size() > 0) {
                        customAdapter = new MyRecyclerViewAdapter(getBaseContext(), cupcakeRecipeList);
                        customAdapter.setClickListener(MainActivity.this);
                        recyclerView.setAdapter(customAdapter);
                        spinner.setVisibility(View.GONE);

                    } else {
                        Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        pastryRef = FirebaseDatabase.getInstance().getReference().child("pastry");
        pastryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                pastryRecipeList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    recipe rec = new recipe();
                    rec.setName(ds.getValue(recipe.class).getName());
                    rec.setUrl(ds.getValue(recipe.class).getUrl());
                    rec.setTime(ds.getValue(recipe.class).getTime());
                    rec.setLevel(ds.getValue(recipe.class).getLevel());
                    rec.setServing(ds.getValue(recipe.class).getServing());
                    rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                    rec.setStep(ds.getValue(recipe.class).getStep());

                    pastryRecipeList.add(rec);
                }

                if(toolbar.getTitle().equals("Pastry")) {

                    if (pastryRecipeList.size() > 0) {
                        customAdapter = new MyRecyclerViewAdapter(getBaseContext(), pastryRecipeList);
                        customAdapter.setClickListener(MainActivity.this);
                        recyclerView.setAdapter(customAdapter);
                        spinner.setVisibility(View.GONE);

                    } else {
                        Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        iceCreamRef = FirebaseDatabase.getInstance().getReference().child("iceCream");
        iceCreamRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                icecreamRecipeList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    recipe rec = new recipe();
                    rec.setName(ds.getValue(recipe.class).getName());
                    rec.setUrl(ds.getValue(recipe.class).getUrl());
                    rec.setTime(ds.getValue(recipe.class).getTime());
                    rec.setLevel(ds.getValue(recipe.class).getLevel());
                    rec.setServing(ds.getValue(recipe.class).getServing());
                    rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                    rec.setStep(ds.getValue(recipe.class).getStep());

                    icecreamRecipeList.add(rec);
                }

                if(toolbar.getTitle().equals("Ice cream")) {

                    if (icecreamRecipeList.size() > 0) {
                        customAdapter = new MyRecyclerViewAdapter(getBaseContext(), icecreamRecipeList);
                        customAdapter.setClickListener(MainActivity.this);
                        recyclerView.setAdapter(customAdapter);
                        spinner.setVisibility(View.GONE);

                    } else {
                        Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        pieRef = FirebaseDatabase.getInstance().getReference().child("pies");
        pieRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                pieRecipeList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    recipe rec = new recipe();
                    rec.setName(ds.getValue(recipe.class).getName());
                    rec.setUrl(ds.getValue(recipe.class).getUrl());
                    rec.setTime(ds.getValue(recipe.class).getTime());
                    rec.setLevel(ds.getValue(recipe.class).getLevel());
                    rec.setServing(ds.getValue(recipe.class).getServing());
                    rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                    rec.setStep(ds.getValue(recipe.class).getStep());

                    pieRecipeList.add(rec);
                }

                if(toolbar.getTitle().equals("Pies")) {

                    if (pieRecipeList.size() > 0) {
                        customAdapter = new MyRecyclerViewAdapter(getBaseContext(), pieRecipeList);
                        customAdapter.setClickListener(MainActivity.this);
                        recyclerView.setAdapter(customAdapter);
                        spinner.setVisibility(View.GONE);

                    } else {
                        Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        kidsRef = FirebaseDatabase.getInstance().getReference().child("forKids");
        kidsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                kidsRecipeList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    recipe rec = new recipe();
                    rec.setName(ds.getValue(recipe.class).getName());
                    rec.setUrl(ds.getValue(recipe.class).getUrl());
                    rec.setTime(ds.getValue(recipe.class).getTime());
                    rec.setLevel(ds.getValue(recipe.class).getLevel());
                    rec.setServing(ds.getValue(recipe.class).getServing());
                    rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                    rec.setStep(ds.getValue(recipe.class).getStep());

                    kidsRecipeList.add(rec);
                }

                if(toolbar.getTitle().equals("For kids")) {

                    if (kidsRecipeList.size() > 0) {
                        customAdapter = new MyRecyclerViewAdapter(getBaseContext(), kidsRecipeList);
                        customAdapter.setClickListener(MainActivity.this);
                        recyclerView.setAdapter(customAdapter);
                        spinner.setVisibility(View.GONE);

                    } else {
                        Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        arabicRef = FirebaseDatabase.getInstance().getReference().child("Arabic Desserts");
        arabicRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                arabRecipeList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    recipe rec = new recipe();
                    rec.setName(ds.getValue(recipe.class).getName());
                    rec.setUrl(ds.getValue(recipe.class).getUrl());
                    rec.setTime(ds.getValue(recipe.class).getTime());
                    rec.setLevel(ds.getValue(recipe.class).getLevel());
                    rec.setServing(ds.getValue(recipe.class).getServing());
                    rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                    rec.setStep(ds.getValue(recipe.class).getStep());

                    arabRecipeList.add(rec);
                }

                if(toolbar.getTitle().equals("Arabic desserts")) {

                    if (arabRecipeList.size() > 0) {
                        customAdapter = new MyRecyclerViewAdapter(getBaseContext(), arabRecipeList);
                        customAdapter.setClickListener(MainActivity.this);
                        recyclerView.setAdapter(customAdapter);
                        spinner.setVisibility(View.GONE);

                    } else {
                        Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        puddingRef = FirebaseDatabase.getInstance().getReference().child("pudding");
        puddingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                puddingRecipeList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    recipe rec = new recipe();
                    rec.setName(ds.getValue(recipe.class).getName());
                    rec.setUrl(ds.getValue(recipe.class).getUrl());
                    rec.setTime(ds.getValue(recipe.class).getTime());
                    rec.setLevel(ds.getValue(recipe.class).getLevel());
                    rec.setServing(ds.getValue(recipe.class).getServing());
                    rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                    rec.setStep(ds.getValue(recipe.class).getStep());

                    puddingRecipeList.add(rec);
                }

                if(toolbar.getTitle().equals("Pudding")) {

                    if (puddingRecipeList.size() > 0) {
                        customAdapter = new MyRecyclerViewAdapter(getBaseContext(), puddingRecipeList);
                        customAdapter.setClickListener(MainActivity.this);
                        recyclerView.setAdapter(customAdapter);
                        spinner.setVisibility(View.GONE);

                    } else {
                        Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        asianRef = FirebaseDatabase.getInstance().getReference().child("Asian Desserts");
        asianRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                asiaRecipeList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    recipe rec = new recipe();
                    rec.setName(ds.getValue(recipe.class).getName());
                    rec.setUrl(ds.getValue(recipe.class).getUrl());
                    rec.setTime(ds.getValue(recipe.class).getTime());
                    rec.setLevel(ds.getValue(recipe.class).getLevel());
                    rec.setServing(ds.getValue(recipe.class).getServing());
                    rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                    rec.setStep(ds.getValue(recipe.class).getStep());

                    asiaRecipeList.add(rec);
                }

                if(toolbar.getTitle().equals("Asian desserts")) {

                    if (asiaRecipeList.size() > 0) {
                        customAdapter = new MyRecyclerViewAdapter(getBaseContext(), asiaRecipeList);
                        customAdapter.setClickListener(MainActivity.this);
                        recyclerView.setAdapter(customAdapter);
                        spinner.setVisibility(View.GONE);

                    } else {
                        Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        breakfastRef = FirebaseDatabase.getInstance().getReference().child("breakfast");
        breakfastRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                breakfastRecipeList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    recipe rec = new recipe();
                    rec.setName(ds.getValue(recipe.class).getName());
                    rec.setUrl(ds.getValue(recipe.class).getUrl());
                    rec.setTime(ds.getValue(recipe.class).getTime());
                    rec.setLevel(ds.getValue(recipe.class).getLevel());
                    rec.setServing(ds.getValue(recipe.class).getServing());
                    rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                    rec.setStep(ds.getValue(recipe.class).getStep());

                    breakfastRecipeList.add(rec);
                }

                if(toolbar.getTitle().equals("Breakfast")) {

                    if (breakfastRecipeList.size() > 0) {
                        customAdapter = new MyRecyclerViewAdapter(getBaseContext(), breakfastRecipeList);
                        customAdapter.setClickListener(MainActivity.this);
                        recyclerView.setAdapter(customAdapter);
                        spinner.setVisibility(View.GONE);

                    } else {
                        Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        otherRef = FirebaseDatabase.getInstance().getReference().child("Others");
        otherRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                otherRecipeList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    recipe rec = new recipe();
                    rec.setName(ds.getValue(recipe.class).getName());
                    rec.setUrl(ds.getValue(recipe.class).getUrl());
                    rec.setTime(ds.getValue(recipe.class).getTime());
                    rec.setLevel(ds.getValue(recipe.class).getLevel());
                    rec.setServing(ds.getValue(recipe.class).getServing());
                    rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                    rec.setStep(ds.getValue(recipe.class).getStep());

                    otherRecipeList.add(rec);
                }

                if(toolbar.getTitle().equals("Other recipes")) {

                    if (otherRecipeList.size() > 0) {
                        customAdapter = new MyRecyclerViewAdapter(getBaseContext(), otherRecipeList);
                        customAdapter.setClickListener(MainActivity.this);
                        recyclerView.setAdapter(customAdapter);
                        spinner.setVisibility(View.GONE);

                    } else {
                        Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);


    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        menuToDelete = menu;
        menu.findItem(R.id.action_delete).setVisible(false);
        menu.findItem(R.id.action_add_to_cart).setVisible(false);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) menu.findItem(R.id.action_search).getActionView();

        if (null != searchView) {
            if (searchManager != null) {
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            }
        }

        android.support.v7.widget.SearchView.OnQueryTextListener queryTextListener = new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {

                String newText = text.toLowerCase();

          if (toolbar.getTitle().equals("Home")) {
                    ref.orderByChild("name").startAt(newText).endAt(newText + "\uf8ff")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    homeRecipeList.clear();

                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        recipe rec = new recipe();
                                        rec.setName(ds.getValue(recipe.class).getName());
                                        rec.setUrl(ds.getValue(recipe.class).getUrl());
                                        rec.setTime(ds.getValue(recipe.class).getTime());
                                        rec.setLevel(ds.getValue(recipe.class).getLevel());
                                        rec.setServing(ds.getValue(recipe.class).getServing());
                                        rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                                        rec.setStep(ds.getValue(recipe.class).getStep());

                                        homeRecipeList.add(rec);
                                    }


                                    if (homeRecipeList.size() > 0) {
                                        customAdapter = new MyRecyclerViewAdapter(getBaseContext(), homeRecipeList);
                                        customAdapter.setClickListener(MainActivity.this);
                                        recyclerView.setAdapter(customAdapter);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                } else if (toolbar.getTitle().equals("Cakes")) {
                    cakeRef.orderByChild("name").startAt(newText).endAt(newText + "\uf8ff")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    cakesRecipeList.clear();

                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        recipe rec = new recipe();
                                        rec.setName(ds.getValue(recipe.class).getName());
                                        rec.setUrl(ds.getValue(recipe.class).getUrl());
                                        rec.setTime(ds.getValue(recipe.class).getTime());
                                        rec.setLevel(ds.getValue(recipe.class).getLevel());
                                        rec.setServing(ds.getValue(recipe.class).getServing());
                                        rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                                        rec.setStep(ds.getValue(recipe.class).getStep());

                                        cakesRecipeList.add(rec);
                                    }


                                    if (cakesRecipeList.size() > 0) {
                                        customAdapter = new MyRecyclerViewAdapter(getBaseContext(), cakesRecipeList);
                                        customAdapter.setClickListener(MainActivity.this);
                                        recyclerView.setAdapter(customAdapter);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                } else if (toolbar.getTitle().equals("No bake")) {
                    nobakeRef.orderByChild("name").startAt(newText).endAt(newText + "\uf8ff")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    nobakeRecipeList.clear();

                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        recipe rec = new recipe();
                                        rec.setName(ds.getValue(recipe.class).getName());
                                        rec.setUrl(ds.getValue(recipe.class).getUrl());
                                        rec.setTime(ds.getValue(recipe.class).getTime());
                                        rec.setLevel(ds.getValue(recipe.class).getLevel());
                                        rec.setServing(ds.getValue(recipe.class).getServing());
                                        rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                                        rec.setStep(ds.getValue(recipe.class).getStep());

                                        nobakeRecipeList.add(rec);
                                    }


                                    if (nobakeRecipeList.size() > 0) {
                                        customAdapter = new MyRecyclerViewAdapter(getBaseContext(), nobakeRecipeList);
                                        customAdapter.setClickListener(MainActivity.this);
                                        recyclerView.setAdapter(customAdapter);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                } else if (toolbar.getTitle().equals("Cupcakes")) {
                    cupcakeRef.orderByChild("name").startAt(newText).endAt(newText + "\uf8ff")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    cupcakeRecipeList.clear();

                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        recipe rec = new recipe();
                                        rec.setName(ds.getValue(recipe.class).getName());
                                        rec.setUrl(ds.getValue(recipe.class).getUrl());
                                        rec.setTime(ds.getValue(recipe.class).getTime());
                                        rec.setLevel(ds.getValue(recipe.class).getLevel());
                                        rec.setServing(ds.getValue(recipe.class).getServing());
                                        rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                                        rec.setStep(ds.getValue(recipe.class).getStep());

                                        cupcakeRecipeList.add(rec);
                                    }


                                    if (cupcakeRecipeList.size() > 0) {
                                        customAdapter = new MyRecyclerViewAdapter(getBaseContext(), cupcakeRecipeList);
                                        customAdapter.setClickListener(MainActivity.this);
                                        recyclerView.setAdapter(customAdapter);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                } else if (toolbar.getTitle().equals("Pastry")) {
                    pastryRef.orderByChild("name").startAt(newText).endAt(newText + "\uf8ff")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    pastryRecipeList.clear();

                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        recipe rec = new recipe();
                                        rec.setName(ds.getValue(recipe.class).getName());
                                        rec.setUrl(ds.getValue(recipe.class).getUrl());
                                        rec.setTime(ds.getValue(recipe.class).getTime());
                                        rec.setLevel(ds.getValue(recipe.class).getLevel());
                                        rec.setServing(ds.getValue(recipe.class).getServing());
                                        rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                                        rec.setStep(ds.getValue(recipe.class).getStep());

                                        pastryRecipeList.add(rec);
                                    }


                                    if (pastryRecipeList.size() > 0) {
                                        customAdapter = new MyRecyclerViewAdapter(getBaseContext(), pastryRecipeList);
                                        customAdapter.setClickListener(MainActivity.this);
                                        recyclerView.setAdapter(customAdapter);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                } else if (toolbar.getTitle().equals("Pies")) {
                    pieRef.orderByChild("name").startAt(newText).endAt(newText + "\uf8ff")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    pieRecipeList.clear();

                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        recipe rec = new recipe();
                                        rec.setName(ds.getValue(recipe.class).getName());
                                        rec.setUrl(ds.getValue(recipe.class).getUrl());
                                        rec.setTime(ds.getValue(recipe.class).getTime());
                                        rec.setLevel(ds.getValue(recipe.class).getLevel());
                                        rec.setServing(ds.getValue(recipe.class).getServing());
                                        rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                                        rec.setStep(ds.getValue(recipe.class).getStep());

                                        pieRecipeList.add(rec);
                                    }


                                    if (pieRecipeList.size() > 0) {
                                        customAdapter = new MyRecyclerViewAdapter(getBaseContext(), pieRecipeList);
                                        customAdapter.setClickListener(MainActivity.this);
                                        recyclerView.setAdapter(customAdapter);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                } else if (toolbar.getTitle().equals("Ice cream")) {
                    iceCreamRef.orderByChild("name").startAt(newText).endAt(newText + "\uf8ff")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    icecreamRecipeList.clear();

                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        recipe rec = new recipe();
                                        rec.setName(ds.getValue(recipe.class).getName());
                                        rec.setUrl(ds.getValue(recipe.class).getUrl());
                                        rec.setTime(ds.getValue(recipe.class).getTime());
                                        rec.setLevel(ds.getValue(recipe.class).getLevel());
                                        rec.setServing(ds.getValue(recipe.class).getServing());
                                        rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                                        rec.setStep(ds.getValue(recipe.class).getStep());

                                        icecreamRecipeList.add(rec);
                                    }


                                    if (icecreamRecipeList.size() > 0) {
                                        customAdapter = new MyRecyclerViewAdapter(getBaseContext(), icecreamRecipeList);
                                        customAdapter.setClickListener(MainActivity.this);
                                        recyclerView.setAdapter(customAdapter);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                } else if (toolbar.getTitle().equals("For kids")) {
                    kidsRef.orderByChild("name").startAt(newText).endAt(newText + "\uf8ff")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    kidsRecipeList.clear();

                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        recipe rec = new recipe();
                                        rec.setName(ds.getValue(recipe.class).getName());
                                        rec.setUrl(ds.getValue(recipe.class).getUrl());
                                        rec.setTime(ds.getValue(recipe.class).getTime());
                                        rec.setLevel(ds.getValue(recipe.class).getLevel());
                                        rec.setServing(ds.getValue(recipe.class).getServing());
                                        rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                                        rec.setStep(ds.getValue(recipe.class).getStep());

                                        kidsRecipeList.add(rec);
                                    }


                                    if (kidsRecipeList.size() > 0) {
                                        customAdapter = new MyRecyclerViewAdapter(getBaseContext(), kidsRecipeList);
                                        customAdapter.setClickListener(MainActivity.this);
                                        recyclerView.setAdapter(customAdapter);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                } else if (toolbar.getTitle().equals("Arabic desserts")) {
                    arabicRef.orderByChild("name").startAt(newText).endAt(newText + "\uf8ff")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    arabRecipeList.clear();

                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        recipe rec = new recipe();
                                        rec.setName(ds.getValue(recipe.class).getName());
                                        rec.setUrl(ds.getValue(recipe.class).getUrl());
                                        rec.setTime(ds.getValue(recipe.class).getTime());
                                        rec.setLevel(ds.getValue(recipe.class).getLevel());
                                        rec.setServing(ds.getValue(recipe.class).getServing());
                                        rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                                        rec.setStep(ds.getValue(recipe.class).getStep());

                                        arabRecipeList.add(rec);
                                    }


                                    if (arabRecipeList.size() > 0) {
                                        customAdapter = new MyRecyclerViewAdapter(getBaseContext(), arabRecipeList);
                                        customAdapter.setClickListener(MainActivity.this);
                                        recyclerView.setAdapter(customAdapter);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                } else if (toolbar.getTitle().equals("Pudding")) {
                    puddingRef.orderByChild("name").startAt(newText).endAt(newText + "\uf8ff")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    puddingRecipeList.clear();

                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        recipe rec = new recipe();
                                        rec.setName(ds.getValue(recipe.class).getName());
                                        rec.setUrl(ds.getValue(recipe.class).getUrl());
                                        rec.setTime(ds.getValue(recipe.class).getTime());
                                        rec.setLevel(ds.getValue(recipe.class).getLevel());
                                        rec.setServing(ds.getValue(recipe.class).getServing());
                                        rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                                        rec.setStep(ds.getValue(recipe.class).getStep());

                                        puddingRecipeList.add(rec);
                                    }


                                    if (puddingRecipeList.size() > 0) {
                                        customAdapter = new MyRecyclerViewAdapter(getBaseContext(), puddingRecipeList);
                                        customAdapter.setClickListener(MainActivity.this);
                                        recyclerView.setAdapter(customAdapter);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                } else if (toolbar.getTitle().equals("Asian desserts")) {
                    asianRef.orderByChild("name").startAt(newText).endAt(newText + "\uf8ff")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    asiaRecipeList.clear();

                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        recipe rec = new recipe();
                                        rec.setName(ds.getValue(recipe.class).getName());
                                        rec.setUrl(ds.getValue(recipe.class).getUrl());
                                        rec.setTime(ds.getValue(recipe.class).getTime());
                                        rec.setLevel(ds.getValue(recipe.class).getLevel());
                                        rec.setServing(ds.getValue(recipe.class).getServing());
                                        rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                                        rec.setStep(ds.getValue(recipe.class).getStep());

                                        asiaRecipeList.add(rec);
                                    }


                                    if (asiaRecipeList.size() > 0) {
                                        customAdapter = new MyRecyclerViewAdapter(getBaseContext(), asiaRecipeList);
                                        customAdapter.setClickListener(MainActivity.this);
                                        recyclerView.setAdapter(customAdapter);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                } else if (toolbar.getTitle().equals("Breakfast")) {
                    breakfastRef.orderByChild("name").startAt(newText).endAt(newText + "\uf8ff")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    breakfastRecipeList.clear();

                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        recipe rec = new recipe();
                                        rec.setName(ds.getValue(recipe.class).getName());
                                        rec.setUrl(ds.getValue(recipe.class).getUrl());
                                        rec.setTime(ds.getValue(recipe.class).getTime());
                                        rec.setLevel(ds.getValue(recipe.class).getLevel());
                                        rec.setServing(ds.getValue(recipe.class).getServing());
                                        rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                                        rec.setStep(ds.getValue(recipe.class).getStep());

                                        breakfastRecipeList.add(rec);
                                    }


                                    if (breakfastRecipeList.size() > 0) {
                                        customAdapter = new MyRecyclerViewAdapter(getBaseContext(), breakfastRecipeList);
                                        customAdapter.setClickListener(MainActivity.this);
                                        recyclerView.setAdapter(customAdapter);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                } else if (toolbar.getTitle().equals("Other desserts")) {
                    otherRef.orderByChild("name").startAt(newText).endAt(newText + "\uf8ff")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    otherRecipeList.clear();

                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        recipe rec = new recipe();
                                        rec.setName(ds.getValue(recipe.class).getName());
                                        rec.setUrl(ds.getValue(recipe.class).getUrl());
                                        rec.setTime(ds.getValue(recipe.class).getTime());
                                        rec.setLevel(ds.getValue(recipe.class).getLevel());
                                        rec.setServing(ds.getValue(recipe.class).getServing());
                                        rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                                        rec.setStep(ds.getValue(recipe.class).getStep());

                                        otherRecipeList.add(rec);
                                    }


                                    if (otherRecipeList.size() > 0) {
                                        customAdapter = new MyRecyclerViewAdapter(getBaseContext(), otherRecipeList);
                                        customAdapter.setClickListener(MainActivity.this);
                                        recyclerView.setAdapter(customAdapter);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                }
                return false;
            }
        };


        if (searchView != null) {
            searchView.setOnQueryTextListener(queryTextListener);
        }

        return super.onCreateOptionsMenu(menu);
    }


    AlertDialog.Builder alertDialog;
    long result;
    EditText textUserInput;
    EditText textMarketName;

    String UserInputValue;
    String MarketNameValue;

    SQLiteDatabase sqLiteDatabase;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        sqLiteDatabase = new SQLiteDatabase(getBaseContext());


        if (id == R.id.action_add_to_cart){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add to cart");
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.add_to_cart_dialog, null);
            builder.setView(dialogView);

            textUserInput = (EditText) dialogView.findViewById(R.id.input_to_buy);
            textMarketName = (EditText) dialogView.findViewById(R.id.input_market_name);

            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    if(!textUserInput.getText().toString().matches("")) {
                        UserInputValue = textUserInput.getText().toString();

                        if(!textMarketName.getText().toString().matches("")) {
                            MarketNameValue = textMarketName.getText().toString();
                        }else{
                            MarketNameValue = "Any Market";
                        }

                        long result = sqLiteDatabase.AddToCart(UserInputValue,MarketNameValue);

                        if (result != -1) {
                            //done
                            Toast.makeText(getBaseContext(), "Item saved successfully", Toast.LENGTH_SHORT).show();

                            if (customCartAdapter != null) {
                                if (toolbar.getTitle().equals("Shopping cart")) {
                                    cartList[0] = sqLiteDatabase.ReadDataFromCart();
                                    refreshCart();
                                    customCartAdapter.notifyDataSetChanged();
                                }
                            }

                        } else {
                            //failed
                            Toast.makeText(getBaseContext(), "Failed to save item!", Toast.LENGTH_SHORT).show();
                        }
                    }else{

                        Toast.makeText(getBaseContext(), "You can't leave To Buy field empty!", Toast.LENGTH_SHORT).show();

                    }

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();

        }

        if (id == R.id.action_delete) {

            final SQLiteDatabase database = new SQLiteDatabase(getBaseContext());

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setCancelable(false);
            alertDialog.setTitle("Delete confirmation");
            if(!toolbar.getTitle().equals("Shopping cart")) {
                alertDialog.setMessage("Are you sure that you want to delete all the saved recipes?");
            }else{
                alertDialog.setMessage("Are you sure that you want to delete all the To Buy list items?");
            }
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    if(!toolbar.getTitle().equals("Shopping cart")) {
                        result = database.removeAllArticle("recipe");
                    }else{
                        result = database.removeAllArticle("cart");
                    }

                    if (result != -1) {
                        //done
                        if(!toolbar.getTitle().equals("Shopping cart")) {
                            Toast.makeText(getBaseContext(), "Recipes deleted successfully", Toast.LENGTH_SHORT).show();
                            if (customAdapter != null) {
                                if (toolbar.getTitle().equals("Favourites")) {
                                    retRec[0] = database.ReadData();
                                    refreshFav();
                                    customAdapter.notifyDataSetChanged();
                                }
                            }
                        }else{
                            Toast.makeText(getBaseContext(), "items deleted successfully", Toast.LENGTH_SHORT).show();
                            if (customCartAdapter != null) {
                                if (toolbar.getTitle().equals("Shopping cart")) {
                                    cartList[0] = database.ReadDataFromCart();
                                    refreshCart();
                                    customCartAdapter.notifyDataSetChanged();
                                }
                            }
                        }

                    } else {
                        //failed
                        if(!toolbar.getTitle().equals("Shopping cart")) {
                            Toast.makeText(getBaseContext(), "Failed to delete recipes!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getBaseContext(), "Failed to delete items!", Toast.LENGTH_SHORT).show();
                        }
                    }


                }
            });
            alertDialog.show();


        }

        if (id == R.id.action_about) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setCancelable(false);
            alertDialog.setTitle("Credits");
            alertDialog.setMessage("Hello dessert app is one of the biggest apps in cooking zone, we have more than 300 recipe of desserts from all over the world. Here you can find any fantastic desserts to share with your friends. " + "\n \n" + "All recipes in this app from:" + "\n" + "http://www.taste.com.au/ & http://www.nestle-family.com/" + "\n \n" + "All icons in this app from:" + "\n" + "https://icons8.com/" + "\n \n" + "This application developed by Ahmed Waheed. \n \nCopy Rights reserved ©");
            alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();


        } else if (id == R.id.action_guide) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setCancelable(false);
            alertDialog.setTitle("User Guide");
            alertDialog.setMessage("this is important to use our app.. \n \nYou can go to any category from the side menu \n \nYou can send us your recipes by clicking on the send button in home window \n \nDon't forget to rate our app :) \n" +
                    "Hello Dessert Team.. ");
            alertDialog.setNegativeButton("Got it", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        } else if (id == R.id.action_license) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setCancelable(false);
                alertDialog.setMessage(Html.fromHtml("<b>Picasso library for images</b>" + "<br/> <br/>" + "Copyright 2013 Square, Inc.<br/>" +
                        "Licensed under the Apache License, Version 2.0 (the \"License\");<br/>" +
                        "you may not use this file except in compliance with the License.<br/>" +
                        "You may obtain a copy of the License at<br/>" +
                        "<br/>" +
                        "   http://www.apache.org/licenses/LICENSE-2.0<br/>" +
                        "<br/>" +
                        "Unless required by applicable law or agreed to in writing, software<br/>" +
                        "distributed under the License is distributed on an \"AS IS\" BASIS,<br/>" +
                        "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.<br/>" +
                        "See the License for the specific language governing permissions and<br/>" +
                        "limitations under the License.", Html.FROM_HTML_MODE_LEGACY));
                alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            } else {
                alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setCancelable(false);
                alertDialog.setMessage(Html.fromHtml("<b>Picasso library for images</b>" + "<br/> <br/>" + "Copyright 2013 Square, Inc.<br/>" +
                        "Licensed under the Apache License, Version 2.0 (the \"License\");<br/>" +
                        "you may not use this file except in compliance with the License.<br/>" +
                        "You may obtain a copy of the License at<br/>" +
                        "<br/>" +
                        "   http://www.apache.org/licenses/LICENSE-2.0<br/>" +
                        "<br/>" +
                        "Unless required by applicable law or agreed to in writing, software<br/>" +
                        "distributed under the License is distributed on an \"AS IS\" BASIS,<br/>" +
                        "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.<br/>" +
                        "See the License for the specific language governing permissions and<br/>" +
                        "limitations under the License."));
                alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        itemm = menuToDelete.findItem(R.id.action_search);
        deletItem = menuToDelete.findItem(R.id.action_delete);
        addToCartItem = menuToDelete.findItem(R.id.action_add_to_cart);


        if (id == R.id.nav_cake) {
            toolbar.setTitle("Cakes");
            noItemText.setVisibility(View.GONE);
            itemm.setVisible(true);
            deletItem.setVisible(false);
            addToCartItem.setVisible(false);

            navigation.setSelectedItemId(R.id.navigation_category);

            if(!toolbar.getTitle().equals("Shopping cart")){
                numberOfColumns = 2;
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
            }else{
                numberOfColumns = 1;
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
            }

            if(noInCart.getVisibility() == View.VISIBLE){
                noInCart.setVisibility(View.GONE);
            }


            if (cakesRecipeList.size() > 0) {
                customAdapter = new MyRecyclerViewAdapter(getBaseContext(), cakesRecipeList);
                customAdapter.setClickListener(MainActivity.this);
                recyclerView.setAdapter(customAdapter);
                spinner.setVisibility(View.GONE);
            } else {
                Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_cart){
            toolbar.setTitle("Shopping cart");
            noItemText.setVisibility(View.GONE);
            itemm.setVisible(false);
            deletItem.setVisible(true);
            addToCartItem.setVisible(true);

            navigation.setSelectedItemId(R.id.navigation_cart);


            if(!toolbar.getTitle().equals("Shopping cart")){
                numberOfColumns = 2;
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
            }else{
                numberOfColumns = 1;
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));

                SQLiteDatabase cartdb = new SQLiteDatabase(getBaseContext());
                cartList = new ArrayList[]{cartdb.ReadDataFromCart()};

                if (cartList[0].isEmpty()) {
                    noInCart.setVisibility(View.VISIBLE);
                }else{
                    noInCart.setVisibility(View.GONE);
                }

                customCartAdapter = new CartRecyclerViewAdapter(getBaseContext(), cartList[0]);
                customCartAdapter.setClickListener(MainActivity.this);
                recyclerView.setAdapter(customCartAdapter);


            }

        }

        else if (id == R.id.nav_feedback) {

            String body = null;
            try {
                body = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
                body = "\n\n-----------------------------\nPlease don't remove this information\n Device OS: Android \n Device OS version: " +
                        Build.VERSION.RELEASE + "\n App Version: " + body + "\n Device Brand: " + Build.BRAND +
                        "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER;
            } catch (PackageManager.NameNotFoundException e) {
            }
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ahmed.waheed.elanwerr@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Hello Dessert feedback");
            intent.putExtra(Intent.EXTRA_TEXT, body);
            context.startActivity(Intent.createChooser(intent, "Choose email client"));

        }

        else if (id == R.id.nav_ic_cream) {
            toolbar.setTitle("Ice cream");
            noItemText.setVisibility(View.GONE);
            itemm.setVisible(true);
            deletItem.setVisible(false);
            navigation.setSelectedItemId(R.id.navigation_category);
            addToCartItem.setVisible(false);

            if(!toolbar.getTitle().equals("Shopping cart")){
                numberOfColumns = 2;
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
            }else{
                numberOfColumns = 1;
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
            }

            if(noInCart.getVisibility() == View.VISIBLE){
                noInCart.setVisibility(View.GONE);
            }



            if (icecreamRecipeList.size() > 0) {
                customAdapter = new MyRecyclerViewAdapter(getBaseContext(), icecreamRecipeList);
                customAdapter.setClickListener(MainActivity.this);
                recyclerView.setAdapter(customAdapter);
                spinner.setVisibility(View.GONE);
            } else {
                Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
            }


        } else if (id == R.id.nav_others) {
            toolbar.setTitle("Other desserts");
            noItemText.setVisibility(View.GONE);
            itemm.setVisible(true);
            deletItem.setVisible(false);
            navigation.setSelectedItemId(R.id.navigation_category);
            addToCartItem.setVisible(false);

            if(!toolbar.getTitle().equals("Shopping cart")){
                numberOfColumns = 2;
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
            }else{
                numberOfColumns = 1;
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
            }

            if(noInCart.getVisibility() == View.VISIBLE){
                noInCart.setVisibility(View.GONE);
            }


            if (otherRecipeList.size() > 0) {
                customAdapter = new MyRecyclerViewAdapter(getBaseContext(), otherRecipeList);
                customAdapter.setClickListener(MainActivity.this);
                recyclerView.setAdapter(customAdapter);
                spinner.setVisibility(View.GONE);
            } else {
                Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_pastry) {
            toolbar.setTitle("Pastry");
            noItemText.setVisibility(View.GONE);
            itemm.setVisible(true);
            deletItem.setVisible(false);
            navigation.setSelectedItemId(R.id.navigation_category);
            addToCartItem.setVisible(false);

            if(!toolbar.getTitle().equals("Shopping cart")){
                numberOfColumns = 2;
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
            }else{
                numberOfColumns = 1;
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
            }

            if(noInCart.getVisibility() == View.VISIBLE){
                noInCart.setVisibility(View.GONE);
            }



            if (pastryRecipeList.size() > 0) {
                customAdapter = new MyRecyclerViewAdapter(getBaseContext(), pastryRecipeList);
                customAdapter.setClickListener(MainActivity.this);
                recyclerView.setAdapter(customAdapter);
                spinner.setVisibility(View.GONE);
            } else {
                Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_cupcakes) {
            toolbar.setTitle("Cupcakes");
            noItemText.setVisibility(View.GONE);
            itemm.setVisible(true);
            deletItem.setVisible(false);
            navigation.setSelectedItemId(R.id.navigation_category);
            addToCartItem.setVisible(false);

            if(!toolbar.getTitle().equals("Shopping cart")){
                numberOfColumns = 2;
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
            }else{
                numberOfColumns = 1;
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
            }

            if(noInCart.getVisibility() == View.VISIBLE){
                noInCart.setVisibility(View.GONE);
            }

            if (cupcakeRecipeList.size() > 0) {
                customAdapter = new MyRecyclerViewAdapter(getBaseContext(), cupcakeRecipeList);
                customAdapter.setClickListener(MainActivity.this);
                recyclerView.setAdapter(customAdapter);
                spinner.setVisibility(View.GONE);
            } else {
                Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
            }


        } else if (id == R.id.nav_pie) {
            toolbar.setTitle("Pies");
            noItemText.setVisibility(View.GONE);
            itemm.setVisible(true);
            deletItem.setVisible(false);
            navigation.setSelectedItemId(R.id.navigation_category);
            addToCartItem.setVisible(false);

            if(!toolbar.getTitle().equals("Shopping cart")){
                numberOfColumns = 2;
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
            }else{
                numberOfColumns = 1;
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
            }

            if(noInCart.getVisibility() == View.VISIBLE){
                noInCart.setVisibility(View.GONE);
            }


            if (pieRecipeList.size() > 0) {
                customAdapter = new MyRecyclerViewAdapter(getBaseContext(), pieRecipeList);
                customAdapter.setClickListener(MainActivity.this);
                recyclerView.setAdapter(customAdapter);
                spinner.setVisibility(View.GONE);
            } else {
                Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_rate) {
            Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
            }
        } else if (id == R.id.nav_share) {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String sAux = "\nLet me recommend you this application\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=" + context.getPackageName() + "\n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            } catch (Exception e) {
                //e.toString();
            }
        } else if (id == R.id.nav_pudding) {
            toolbar.setTitle("Pudding");
            noItemText.setVisibility(View.GONE);
            itemm.setVisible(true);
            deletItem.setVisible(false);
            navigation.setSelectedItemId(R.id.navigation_category);
            addToCartItem.setVisible(false);

            if(!toolbar.getTitle().equals("Shopping cart")){
                numberOfColumns = 2;
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
            }else{
                numberOfColumns = 1;
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
            }

            if(noInCart.getVisibility() == View.VISIBLE){
                noInCart.setVisibility(View.GONE);
            }

            if (puddingRecipeList.size() > 0) {
                customAdapter = new MyRecyclerViewAdapter(getBaseContext(), puddingRecipeList);
                customAdapter.setClickListener(MainActivity.this);
                recyclerView.setAdapter(customAdapter);
                spinner.setVisibility(View.GONE);
            } else {
                Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
            }


        } else if (id == R.id.nav_baby) {
            toolbar.setTitle("For kids");
            noItemText.setVisibility(View.GONE);
            itemm.setVisible(true);
            deletItem.setVisible(false);
            navigation.setSelectedItemId(R.id.navigation_category);
            addToCartItem.setVisible(false);

            if(!toolbar.getTitle().equals("Shopping cart")){
                numberOfColumns = 2;
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
            }else{
                numberOfColumns = 1;
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
            }

            if(noInCart.getVisibility() == View.VISIBLE){
                noInCart.setVisibility(View.GONE);
            }

            if (kidsRecipeList.size() > 0) {
                customAdapter = new MyRecyclerViewAdapter(getBaseContext(), kidsRecipeList);
                customAdapter.setClickListener(MainActivity.this);
                recyclerView.setAdapter(customAdapter);
                spinner.setVisibility(View.GONE);
            } else {
                Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
            }


        } else if (id == R.id.nav_nobake) {
            toolbar.setTitle("No bake");
            noItemText.setVisibility(View.GONE);
            itemm.setVisible(true);
            deletItem.setVisible(false);
            navigation.setSelectedItemId(R.id.navigation_category);
            addToCartItem.setVisible(false);

            if(!toolbar.getTitle().equals("Shopping cart")){
                numberOfColumns = 2;
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
            }else{
                numberOfColumns = 1;
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
            }

            if(noInCart.getVisibility() == View.VISIBLE){
                noInCart.setVisibility(View.GONE);
            }


            if (nobakeRecipeList.size() > 0) {
                customAdapter = new MyRecyclerViewAdapter(getBaseContext(), nobakeRecipeList);
                customAdapter.setClickListener(MainActivity.this);
                recyclerView.setAdapter(customAdapter);
                spinner.setVisibility(View.GONE);
            } else {
                Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_new) {

            noItemText.setVisibility(View.GONE);
            toolbar.setTitle("New recipes");
            itemm.setVisible(false);
            deletItem.setVisible(false);
            navigation.setSelectedItemId(R.id.navigation_new);
            addToCartItem.setVisible(false);

            if(!toolbar.getTitle().equals("Shopping cart")){
                numberOfColumns = 2;
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
            }else{
                numberOfColumns = 1;
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
            }

            if(noInCart.getVisibility() == View.VISIBLE){
                noInCart.setVisibility(View.GONE);
            }

            if (newRecipeList.size() > 0) {
                customAdapter = new MyRecyclerViewAdapter(getBaseContext(), newRecipeList);
                customAdapter.setClickListener(MainActivity.this);
                recyclerView.setAdapter(customAdapter);
                spinner.setVisibility(View.GONE);

            } else {
                Toast.makeText(context, "No new data", Toast.LENGTH_SHORT).show();
            }

            //fab.show();

        } else if (id == R.id.nav_arabic) {
            toolbar.setTitle("Arabic desserts");
            noItemText.setVisibility(View.GONE);
            itemm.setVisible(true);
            deletItem.setVisible(false);
            navigation.setSelectedItemId(R.id.navigation_category);
            addToCartItem.setVisible(false);

            if(!toolbar.getTitle().equals("Shopping cart")){
                numberOfColumns = 2;
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
            }else{
                numberOfColumns = 1;
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
            }

            if(noInCart.getVisibility() == View.VISIBLE){
                noInCart.setVisibility(View.GONE);
            }

            if (arabRecipeList.size() > 0) {
                customAdapter = new MyRecyclerViewAdapter(getBaseContext(), arabRecipeList);
                customAdapter.setClickListener(MainActivity.this);
                recyclerView.setAdapter(customAdapter);
                spinner.setVisibility(View.GONE);
            } else {
                Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
            }


        } else if (id == R.id.nav_JapAndCh) {
            toolbar.setTitle("Asian desserts");
            noItemText.setVisibility(View.GONE);
            itemm.setVisible(true);
            deletItem.setVisible(false);
            navigation.setSelectedItemId(R.id.navigation_category);
            addToCartItem.setVisible(false);

            if(!toolbar.getTitle().equals("Shopping cart")){
                numberOfColumns = 2;
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
            }else{
                numberOfColumns = 1;
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
            }

            if(noInCart.getVisibility() == View.VISIBLE){
                noInCart.setVisibility(View.GONE);
            }


            if (asiaRecipeList.size() > 0) {
                customAdapter = new MyRecyclerViewAdapter(getBaseContext(), asiaRecipeList);
                customAdapter.setClickListener(MainActivity.this);
                recyclerView.setAdapter(customAdapter);
                spinner.setVisibility(View.GONE);
            } else {
                Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_breakfast) {
            toolbar.setTitle("Breakfast");
            noItemText.setVisibility(View.GONE);
            itemm.setVisible(true);
            deletItem.setVisible(false);
            navigation.setSelectedItemId(R.id.navigation_category);
            addToCartItem.setVisible(false);

            if(!toolbar.getTitle().equals("Shopping cart")){
                numberOfColumns = 2;
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
            }else{
                numberOfColumns = 1;
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
            }

            if(noInCart.getVisibility() == View.VISIBLE){
                noInCart.setVisibility(View.GONE);
            }


            if (breakfastRecipeList.size() > 0) {
                customAdapter = new MyRecyclerViewAdapter(getBaseContext(), breakfastRecipeList);
                customAdapter.setClickListener(MainActivity.this);
                recyclerView.setAdapter(customAdapter);
                spinner.setVisibility(View.GONE);
            } else {
                Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_home) {
            toolbar.setTitle("Home");
            noItemText.setVisibility(View.GONE);
            itemm.setVisible(true);
            deletItem.setVisible(false);
            navigation.setSelectedItemId(R.id.navigation_home);
            addToCartItem.setVisible(false);
            if(!toolbar.getTitle().equals("Shopping cart")){
                numberOfColumns = 2;
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
            }else{
                numberOfColumns = 1;
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
            }

            if(noInCart.getVisibility() == View.VISIBLE){
                noInCart.setVisibility(View.GONE);
            }


            if (homeRecipeList.size() > 0) {
                customAdapter = new MyRecyclerViewAdapter(getBaseContext(), homeRecipeList);
                customAdapter.setClickListener(MainActivity.this);
                recyclerView.setAdapter(customAdapter);
                spinner.setVisibility(View.GONE);
            } else {
                Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
            }

            //fab.show();
        } else if (id == R.id.nav_favorites) {
            toolbar.setTitle("Favourites");
            itemm.setVisible(false);
            deletItem.setVisible(true);
            navigation.setSelectedItemId(R.id.navigation_fav);
            addToCartItem.setVisible(false);

            if(!toolbar.getTitle().equals("Shopping cart")){
                numberOfColumns = 2;
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
            }else{
                numberOfColumns = 1;
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns));
            }

            if(noInCart.getVisibility() == View.VISIBLE){
                noInCart.setVisibility(View.GONE);
            }

            SQLiteDatabase sqLiteDatabase = new SQLiteDatabase(getBaseContext());
            retRec = new ArrayList[]{sqLiteDatabase.ReadData()};


            noItemText.setVisibility(View.VISIBLE);

            if (!retRec[0].isEmpty()) {
                noItemText.setVisibility(View.GONE);
            }

            customAdapter = new MyRecyclerViewAdapter(getBaseContext(), retRec[0]);
            customAdapter.setClickListener(MainActivity.this);
            recyclerView.setAdapter(customAdapter);


            database = new SQLiteDatabase(context);


            //fab.show();


        } else if (id == R.id.nav_other) {

            String url = "https://play.google.com/store/apps/dev?id=6877401326939455947";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onRefresh() {

        swipeRefreshLayout.setRefreshing(true);
        if (toolbar.getTitle().equals("Home")) {
            refreshList();
        } else if (toolbar.getTitle().equals("Favourites")) {
            refreshFav();
        } else if (toolbar.getTitle().equals("Cakes")) {
            refreshCake();
        } else if (toolbar.getTitle().equals("No bake")) {
            refreshNoBake();
        } else if (toolbar.getTitle().equals("Cupcakes")) {
            refreshCupcake();
        } else if (toolbar.getTitle().equals("Pastry")) {
            refreshPastry();
        } else if (toolbar.getTitle().equals("Pies")) {
            refreshPies();
        } else if (toolbar.getTitle().equals("Ice cream")) {
            refreshIce();
        } else if (toolbar.getTitle().equals("For kids")) {
            refreshKids();
        } else if (toolbar.getTitle().equals("Arabic desserts")) {
            refreshArabic();
        } else if (toolbar.getTitle().equals("Pudding")) {
            refreshPud();
        } else if (toolbar.getTitle().equals("Asian desserts")) {
            refreshAsian();
        } else if (toolbar.getTitle().equals("Breakfast")) {
            refreshBreakfast();
        } else if (toolbar.getTitle().equals("Other desserts")) {
            refreshOthers();
        } else if (toolbar.getTitle().equals("New recipes")) {
            refreshNew();
        } else if (toolbar.getTitle().equals("Shopping cart")){
            refreshCart();
        }
    }

    public void refreshCupcake() {
        cupcakeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cupcakeRecipeList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    recipe rec = new recipe();
                    rec.setName(ds.getValue(recipe.class).getName());
                    rec.setUrl(ds.getValue(recipe.class).getUrl());
                    rec.setTime(ds.getValue(recipe.class).getTime());
                    rec.setLevel(ds.getValue(recipe.class).getLevel());
                    rec.setServing(ds.getValue(recipe.class).getServing());
                    rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                    rec.setStep(ds.getValue(recipe.class).getStep());

                    cupcakeRecipeList.add(rec);
                }


                if (cupcakeRecipeList.size() > 0) {
                    customAdapter = new MyRecyclerViewAdapter(getBaseContext(), cupcakeRecipeList);
                    customAdapter.setClickListener(MainActivity.this);
                    recyclerView.setAdapter(customAdapter);
                } else {
                    Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        swipeRefreshLayout.setRefreshing(false);
    }

    public void refreshNew() {
        newRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                newRecipeList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    recipe rec = new recipe();
                    rec.setName(ds.getValue(recipe.class).getName());
                    rec.setUrl(ds.getValue(recipe.class).getUrl());
                    rec.setTime(ds.getValue(recipe.class).getTime());
                    rec.setLevel(ds.getValue(recipe.class).getLevel());
                    rec.setServing(ds.getValue(recipe.class).getServing());
                    rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                    rec.setStep(ds.getValue(recipe.class).getStep());

                    newRecipeList.add(rec);
                }


                if (newRecipeList.size() > 0) {
                    customAdapter = new MyRecyclerViewAdapter(getBaseContext(), newRecipeList);
                    customAdapter.setClickListener(MainActivity.this);
                    recyclerView.setAdapter(customAdapter);
                } else {
                    Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        swipeRefreshLayout.setRefreshing(false);
    }

    public void refreshOthers() {
        otherRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                otherRecipeList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    recipe rec = new recipe();
                    rec.setName(ds.getValue(recipe.class).getName());
                    rec.setUrl(ds.getValue(recipe.class).getUrl());
                    rec.setTime(ds.getValue(recipe.class).getTime());
                    rec.setLevel(ds.getValue(recipe.class).getLevel());
                    rec.setServing(ds.getValue(recipe.class).getServing());
                    rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                    rec.setStep(ds.getValue(recipe.class).getStep());

                    otherRecipeList.add(rec);
                }


                if (otherRecipeList.size() > 0) {
                    customAdapter = new MyRecyclerViewAdapter(getBaseContext(), otherRecipeList);
                    customAdapter.setClickListener(MainActivity.this);
                    recyclerView.setAdapter(customAdapter);
                } else {
                    Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        swipeRefreshLayout.setRefreshing(false);
    }

    public void refreshBreakfast() {
        breakfastRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                breakfastRecipeList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    recipe rec = new recipe();
                    rec.setName(ds.getValue(recipe.class).getName());
                    rec.setUrl(ds.getValue(recipe.class).getUrl());
                    rec.setTime(ds.getValue(recipe.class).getTime());
                    rec.setLevel(ds.getValue(recipe.class).getLevel());
                    rec.setServing(ds.getValue(recipe.class).getServing());
                    rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                    rec.setStep(ds.getValue(recipe.class).getStep());

                    breakfastRecipeList.add(rec);
                }


                if (breakfastRecipeList.size() > 0) {
                    customAdapter = new MyRecyclerViewAdapter(getBaseContext(), breakfastRecipeList);
                    customAdapter.setClickListener(MainActivity.this);
                    recyclerView.setAdapter(customAdapter);
                } else {
                    Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        swipeRefreshLayout.setRefreshing(false);
    }

    public void refreshAsian() {
        asianRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                asiaRecipeList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    recipe rec = new recipe();
                    rec.setName(ds.getValue(recipe.class).getName());
                    rec.setUrl(ds.getValue(recipe.class).getUrl());
                    rec.setTime(ds.getValue(recipe.class).getTime());
                    rec.setLevel(ds.getValue(recipe.class).getLevel());
                    rec.setServing(ds.getValue(recipe.class).getServing());
                    rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                    rec.setStep(ds.getValue(recipe.class).getStep());

                    asiaRecipeList.add(rec);
                }


                if (asiaRecipeList.size() > 0) {
                    customAdapter = new MyRecyclerViewAdapter(getBaseContext(), asiaRecipeList);
                    customAdapter.setClickListener(MainActivity.this);
                    recyclerView.setAdapter(customAdapter);
                } else {
                    Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        swipeRefreshLayout.setRefreshing(false);
    }

    public void refreshPud() {
        puddingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                puddingRecipeList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    recipe rec = new recipe();
                    rec.setName(ds.getValue(recipe.class).getName());
                    rec.setUrl(ds.getValue(recipe.class).getUrl());
                    rec.setTime(ds.getValue(recipe.class).getTime());
                    rec.setLevel(ds.getValue(recipe.class).getLevel());
                    rec.setServing(ds.getValue(recipe.class).getServing());
                    rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                    rec.setStep(ds.getValue(recipe.class).getStep());

                    puddingRecipeList.add(rec);
                }


                if (puddingRecipeList.size() > 0) {
                    customAdapter = new MyRecyclerViewAdapter(getBaseContext(), puddingRecipeList);
                    customAdapter.setClickListener(MainActivity.this);
                    recyclerView.setAdapter(customAdapter);
                } else {
                    Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        swipeRefreshLayout.setRefreshing(false);
    }

    public void refreshPies() {
        pieRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pieRecipeList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    recipe rec = new recipe();
                    rec.setName(ds.getValue(recipe.class).getName());
                    rec.setUrl(ds.getValue(recipe.class).getUrl());
                    rec.setTime(ds.getValue(recipe.class).getTime());
                    rec.setLevel(ds.getValue(recipe.class).getLevel());
                    rec.setServing(ds.getValue(recipe.class).getServing());
                    rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                    rec.setStep(ds.getValue(recipe.class).getStep());

                    pieRecipeList.add(rec);
                }


                if (pieRecipeList.size() > 0) {
                    customAdapter = new MyRecyclerViewAdapter(getBaseContext(), pieRecipeList);
                    customAdapter.setClickListener(MainActivity.this);
                    recyclerView.setAdapter(customAdapter);
                } else {
                    Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        swipeRefreshLayout.setRefreshing(false);
    }

    public void refreshArabic() {
        arabicRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arabRecipeList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    recipe rec = new recipe();
                    rec.setName(ds.getValue(recipe.class).getName());
                    rec.setUrl(ds.getValue(recipe.class).getUrl());
                    rec.setTime(ds.getValue(recipe.class).getTime());
                    rec.setLevel(ds.getValue(recipe.class).getLevel());
                    rec.setServing(ds.getValue(recipe.class).getServing());
                    rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                    rec.setStep(ds.getValue(recipe.class).getStep());

                    arabRecipeList.add(rec);
                }


                if (arabRecipeList.size() > 0) {
                    customAdapter = new MyRecyclerViewAdapter(getBaseContext(), arabRecipeList);
                    customAdapter.setClickListener(MainActivity.this);
                    recyclerView.setAdapter(customAdapter);
                } else {
                    Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        swipeRefreshLayout.setRefreshing(false);
    }

    public void refreshIce() {
        iceCreamRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                icecreamRecipeList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    recipe rec = new recipe();
                    rec.setName(ds.getValue(recipe.class).getName());
                    rec.setUrl(ds.getValue(recipe.class).getUrl());
                    rec.setTime(ds.getValue(recipe.class).getTime());
                    rec.setLevel(ds.getValue(recipe.class).getLevel());
                    rec.setServing(ds.getValue(recipe.class).getServing());
                    rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                    rec.setStep(ds.getValue(recipe.class).getStep());

                    icecreamRecipeList.add(rec);
                }


                if (icecreamRecipeList.size() > 0) {
                    customAdapter = new MyRecyclerViewAdapter(getBaseContext(), icecreamRecipeList);
                    customAdapter.setClickListener(MainActivity.this);
                    recyclerView.setAdapter(customAdapter);
                } else {
                    Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        swipeRefreshLayout.setRefreshing(false);
    }

    public void refreshPastry() {
        pastryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pastryRecipeList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    recipe rec = new recipe();
                    rec.setName(ds.getValue(recipe.class).getName());
                    rec.setUrl(ds.getValue(recipe.class).getUrl());
                    rec.setTime(ds.getValue(recipe.class).getTime());
                    rec.setLevel(ds.getValue(recipe.class).getLevel());
                    rec.setServing(ds.getValue(recipe.class).getServing());
                    rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                    rec.setStep(ds.getValue(recipe.class).getStep());

                    pastryRecipeList.add(rec);
                }


                if (pastryRecipeList.size() > 0) {
                    customAdapter = new MyRecyclerViewAdapter(getBaseContext(), pastryRecipeList);
                    customAdapter.setClickListener(MainActivity.this);
                    recyclerView.setAdapter(customAdapter);
                } else {
                    Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        swipeRefreshLayout.setRefreshing(false);
    }

    public void refreshNoBake() {
        nobakeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nobakeRecipeList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    recipe rec = new recipe();
                    rec.setName(ds.getValue(recipe.class).getName());
                    rec.setUrl(ds.getValue(recipe.class).getUrl());
                    rec.setTime(ds.getValue(recipe.class).getTime());
                    rec.setLevel(ds.getValue(recipe.class).getLevel());
                    rec.setServing(ds.getValue(recipe.class).getServing());
                    rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                    rec.setStep(ds.getValue(recipe.class).getStep());

                    nobakeRecipeList.add(rec);
                }


                if (nobakeRecipeList.size() > 0) {
                    customAdapter = new MyRecyclerViewAdapter(getBaseContext(), nobakeRecipeList);
                    customAdapter.setClickListener(MainActivity.this);
                    recyclerView.setAdapter(customAdapter);
                } else {
                    Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        swipeRefreshLayout.setRefreshing(false);
    }

    public void refreshKids() {

        kidsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                kidsRecipeList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    recipe rec = new recipe();
                    rec.setName(ds.getValue(recipe.class).getName());
                    rec.setUrl(ds.getValue(recipe.class).getUrl());
                    rec.setTime(ds.getValue(recipe.class).getTime());
                    rec.setLevel(ds.getValue(recipe.class).getLevel());
                    rec.setServing(ds.getValue(recipe.class).getServing());
                    rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                    rec.setStep(ds.getValue(recipe.class).getStep());

                    kidsRecipeList.add(rec);
                }


                if (kidsRecipeList.size() > 0) {
                    customAdapter = new MyRecyclerViewAdapter(getBaseContext(), kidsRecipeList);
                    customAdapter.setClickListener(MainActivity.this);
                    recyclerView.setAdapter(customAdapter);
                } else {
                    Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        swipeRefreshLayout.setRefreshing(false);
    }

    public void refreshCake() {

        cakeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cakesRecipeList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    recipe rec = new recipe();
                    rec.setName(ds.getValue(recipe.class).getName());
                    rec.setUrl(ds.getValue(recipe.class).getUrl());
                    rec.setTime(ds.getValue(recipe.class).getTime());
                    rec.setLevel(ds.getValue(recipe.class).getLevel());
                    rec.setServing(ds.getValue(recipe.class).getServing());
                    rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                    rec.setStep(ds.getValue(recipe.class).getStep());

                    cakesRecipeList.add(rec);
                }


                if (cakesRecipeList.size() > 0) {
                    customAdapter = new MyRecyclerViewAdapter(getBaseContext(), cakesRecipeList);
                    customAdapter.setClickListener(MainActivity.this);
                    recyclerView.setAdapter(customAdapter);
                } else {
                    Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        swipeRefreshLayout.setRefreshing(false);
    }

    public void refreshFav() {
        SQLiteDatabase sqLiteDatabase = new SQLiteDatabase(getBaseContext());
        final ArrayList<recipe>[] retRec = new ArrayList[]{sqLiteDatabase.ReadData()};
        if (retRec[0].isEmpty()) {
            noItemText.setVisibility(View.VISIBLE);
        }
        recipeArrayList.clear();
        customAdapter = new MyRecyclerViewAdapter(getBaseContext(), retRec[0]);
        customAdapter.setClickListener(MainActivity.this);
        recyclerView.setAdapter(customAdapter);

        swipeRefreshLayout.setRefreshing(false);

    }

    public void refreshCart() {
        SQLiteDatabase sqLiteDatabase = new SQLiteDatabase(getBaseContext());
        cartList = new ArrayList[]{sqLiteDatabase.ReadDataFromCart()};
        if (cartList[0].isEmpty()) {
            noInCart.setVisibility(View.VISIBLE);
        }else{
            noInCart.setVisibility(View.GONE);
        }
        recipeArrayList.clear();
        customCartAdapter = new CartRecyclerViewAdapter(getBaseContext(), cartList[0]);
        customCartAdapter.setClickListener(MainActivity.this);
        recyclerView.setAdapter(customCartAdapter);

        swipeRefreshLayout.setRefreshing(false);

    }

    public void refreshList() {
        //add your code to get the new data and add it in your adaptr
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                homeRecipeList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    recipe rec = new recipe();
                    rec.setName(ds.getValue(recipe.class).getName());
                    rec.setUrl(ds.getValue(recipe.class).getUrl());
                    rec.setTime(ds.getValue(recipe.class).getTime());
                    rec.setLevel(ds.getValue(recipe.class).getLevel());
                    rec.setServing(ds.getValue(recipe.class).getServing());
                    rec.setMaterial(ds.getValue(recipe.class).getMaterial());
                    rec.setStep(ds.getValue(recipe.class).getStep());

                    homeRecipeList.add(rec);
                }


                if (homeRecipeList.size() > 0) {
                    customAdapter = new MyRecyclerViewAdapter(getBaseContext(), homeRecipeList);
                    customAdapter.setClickListener(MainActivity.this);
                    recyclerView.setAdapter(customAdapter);
                } else {
                    Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (customAdapter != null) {
            if (toolbar.getTitle().equals("Favourites")) {
                retRec[0] = database.ReadData();
                refreshFav();
                customAdapter.notifyDataSetChanged();
            }
            customAdapter.notifyDataSetChanged();
        }
        if(customCartAdapter != null){
            customCartAdapter.notifyDataSetChanged();
        }
    }


    class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

        ArrayList<recipe> mRecipes;
        LayoutInflater mInflater;
        Context context2;
        boolean isExx;
        SQLiteDatabase sqLiteDatabasee;
        public ItemClickListener mClickListener;

        // data is passed into the constructor
        MyRecyclerViewAdapter(Context context, ArrayList<recipe> recipes) {
            this.mInflater = LayoutInflater.from(context);
            this.mRecipes = recipes;
            this.context2 = context;

        }

        // inflates the cell layout from xml when needed
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.card, parent, false);
            return new ViewHolder(view);
        }

        // binds the data to the textview in each cell
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int i) {

            sqLiteDatabasee = new SQLiteDatabase(context2);

            holder.Rname.setText(mRecipes.get(i).getName());
            holder.Rtime.setText(mRecipes.get(i).getTime());
            holder.Rlevel.setText(mRecipes.get(i).getLevel());
            holder.Rserving.setText(mRecipes.get(i).getServing());

            isExx = sqLiteDatabasee.isExist(mRecipes.get(i).getUrl(), mRecipes.get(i).getName());

            if (!isExx) {
                holder.Rsave.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            } else {
                holder.Rsave.setImageResource(R.drawable.ic_favorite_black_24dp);
            }

            PicassoClient picassoClient = new PicassoClient();
            picassoClient.downloadImage(context2, mRecipes.get(i).getUrl(), holder.Rimage);

            holder.Rsave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    isExx = sqLiteDatabasee.isExist(mRecipes.get(i).getUrl(), mRecipes.get(i).getName());

                    if (!isExx) {
                        // save article
                        long result = sqLiteDatabasee.WriteData(mRecipes.get(i).getName()
                                , mRecipes.get(i).getUrl(), mRecipes.get(i).getTime(), mRecipes.get(i).getServing()
                                , mRecipes.get(i).getLevel(), mRecipes.get(i).getMaterial(), mRecipes.get(i).getStep());

                        if (result != -1) {
                            //done
                            holder.Rsave.setImageResource(R.drawable.ic_favorite_black_24dp);
                            isExx = true;
                            Toast.makeText(context2, "Recipe saved successfully", Toast.LENGTH_SHORT).show();

                        } else {
                            //failed
                            Toast.makeText(context2, "Failed to save recipe!", Toast.LENGTH_SHORT).show();
                        }


                    } else {

                        // remove article
                        long result = sqLiteDatabasee.removeArticle(mRecipes.get(i).getUrl());

                        if (result != -1) {
                            //done
                            holder.Rsave.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            isExx = false;
                            Toast.makeText(context2, "Recipe removed successfully", Toast.LENGTH_SHORT).show();
                            if (customAdapter != null) {
                                if (toolbar.getTitle().equals("Favourites")) {
                                    retRec[0] = database.ReadData();
                                    refreshFav();
                                    customAdapter.notifyDataSetChanged();
                                }
                            }

                        } else {
                            //failed
                            Toast.makeText(context2, "Failed to remove recipe!", Toast.LENGTH_SHORT).show();
                        }

                    }

                    view.startAnimation(AnimationUtils.loadAnimation(context2, R.anim.click));

                }


            });
        }

        // total number of cells
        @Override
        public int getItemCount() {
            return mRecipes.size();
        }


        // stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView Rname;
            ImageView Rimage;
            TextView Rtime;
            TextView Rlevel;
            TextView Rserving;
            ImageView Rsave;

            ViewHolder(View itemView) {
                super(itemView);
                Rname = (TextView) itemView.findViewById(R.id.name);
                Rtime = (TextView) itemView.findViewById(R.id.time);
                Rlevel = (TextView) itemView.findViewById(R.id.level);
                Rserving = (TextView) itemView.findViewById(R.id.serve);
                Rimage = (ImageView) itemView.findViewById(R.id.recipeImagee);
                Rsave = (ImageView) itemView.findViewById(R.id.save_bu);
                itemView.setOnClickListener(this);

            }

            @Override
            public void onClick(View view) {
                if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
            }
        }

        // allows clicks events to be caught
        void setClickListener(ItemClickListener itemClickListener) {
            this.mClickListener = itemClickListener;
        }

        // parent activity will implement this method to respond to click events

    }

    class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder> {

        ArrayList<cart> mCart;
        LayoutInflater mInflater;
        Context context2;
        SQLiteDatabase sqLiteDatabasee;
        public ItemClickListener mClickListener;

        // data is passed into the constructor
        CartRecyclerViewAdapter(Context context, ArrayList<cart> mCart) {
            this.mInflater = LayoutInflater.from(context);
            this.mCart = mCart;
            this.context2 = context;

        }

        // inflates the cell layout from xml when needed
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.card_cart, parent, false);
            return new ViewHolder(view);
        }

        // binds the data to the textview in each cell
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int i) {

            sqLiteDatabasee = new SQLiteDatabase(context2);

            holder.Rmarketname.setText(mCart.get(i).getMarketName());
            holder.Ruserinput.setText(mCart.get(i).getUserInput());

            holder.Rdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    long result = sqLiteDatabasee.removeFromCart(mCart.get(i).getUserInput());

                    if (result != -1) {
                        //done
                        Toast.makeText(context2, "item removed successfully", Toast.LENGTH_SHORT).show();
                        if (customCartAdapter != null) {
                            if (toolbar.getTitle().equals("Shopping cart")) {
                                cartList[0] = sqLiteDatabasee.ReadDataFromCart();
                                refreshCart();
                                customAdapter.notifyDataSetChanged();
                            }
                        }

                    } else {
                        //failed
                        Toast.makeText(context2, "Failed to remove item!", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }

        // total number of cells
        @Override
        public int getItemCount() {
            return mCart.size();
        }


        // stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView Ruserinput;
            ImageView Rdelete;
            TextView Rmarketname;


            ViewHolder(View itemView) {
                super(itemView);

                Ruserinput = (TextView) itemView.findViewById(R.id.userInput);
                Rdelete = (ImageView) itemView.findViewById(R.id.delete_from_cart);
                Rmarketname = (TextView) itemView.findViewById(R.id.market_name);
                itemView.setOnClickListener(this);

            }

            @Override
            public void onClick(View view) {
                if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
            }
        }

        void setClickListener(ItemClickListener itemClickListener) {
            this.mClickListener = itemClickListener;
        }

    }


    String userInput ;
    String marketName;
    int id;

    EditText textUserInputEdit;
    EditText textMarketNameEdit;

    String marketNameEdited;
    String userInputEdited;
    @Override
    public void onItemClick(View view, int i) {


        if (toolbar.getTitle().equals("Favourites")) {

            String name = retRec[0].get(i).getName();
            String url = retRec[0].get(i).getUrl();
            String level = retRec[0].get(i).getLevel();
            String time = retRec[0].get(i).getTime();
            String serving = retRec[0].get(i).getServing();
            String material = retRec[0].get(i).getMaterial();
            String step = retRec[0].get(i).getStep();

            Intent g = new Intent(MainActivity.this, DessertActivity.class);
            g.putExtra("name", name);
            g.putExtra("url", url);
            g.putExtra("level", level);
            g.putExtra("time", time);
            g.putExtra("serving", serving);
            g.putExtra("material", material);
            g.putExtra("step", step);
            startActivity(g);

        } else if(toolbar.getTitle().equals("Shopping cart")){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Edit this..");
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.add_to_cart_dialog, null);
            builder.setView(dialogView);

            userInput = cartList[0].get(i).getUserInput();
            marketName = cartList[0].get(i).getMarketName();
            id = cartList[0].get(i).getId();

            textUserInputEdit = (EditText) dialogView.findViewById(R.id.input_to_buy);
            textMarketNameEdit = (EditText) dialogView.findViewById(R.id.input_market_name);

            textUserInputEdit.setText(userInput);
            textMarketNameEdit.setText(marketName);

            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    userInputEdited = textUserInputEdit.getText().toString();
                    marketNameEdited = textMarketNameEdit.getText().toString();

                    if(!userInputEdited.matches("")){

                        if(marketNameEdited.matches("")){
                            marketNameEdited = "Any Market";
                        }

                        SQLiteDatabase db = new SQLiteDatabase(getBaseContext());

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("userTextInput",userInputEdited);
                        contentValues.put("marketName",marketNameEdited);

                        db.updateItemInList(contentValues,id);

                        refreshCart();
                        customCartAdapter.notifyDataSetChanged();

                        Toast.makeText(getBaseContext(), "Item edited successfully", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(getBaseContext(), "You can't leave To Buy field empty!", Toast.LENGTH_SHORT).show();
                    }



                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();

        }

        else if (toolbar.getTitle().equals("Home")){
            openRecipe(homeRecipeList,i);
        }

        else if (toolbar.getTitle().equals("New recipes")){
            openRecipe(newRecipeList,i);
        }

        else if (toolbar.getTitle().equals("Cakes")){
            openRecipe(cakesRecipeList,i);
        }

        else if (toolbar.getTitle().equals("No bake")){
            openRecipe(nobakeRecipeList,i);
        }

        else if (toolbar.getTitle().equals("Cupcakes")){
            openRecipe(cupcakeRecipeList,i);
        }

        else if (toolbar.getTitle().equals("Pastry")){
            openRecipe(pastryRecipeList,i);
        }

        else if (toolbar.getTitle().equals("Pies")){
            openRecipe(pieRecipeList,i);
        }

        else if (toolbar.getTitle().equals("Ice cream")){
            openRecipe(icecreamRecipeList,i);
        }

        else if (toolbar.getTitle().equals("For kids")){
            openRecipe(kidsRecipeList,i);
        }

        else if (toolbar.getTitle().equals("Arabic desserts")){
            openRecipe(arabRecipeList,i);
        }

        else if (toolbar.getTitle().equals("Pudding")){
            openRecipe(puddingRecipeList,i);
        }

        else if (toolbar.getTitle().equals("Asian desserts")){
            openRecipe(asiaRecipeList,i);
        }

        else if (toolbar.getTitle().equals("Breakfast")){
            openRecipe(breakfastRecipeList,i);
        }

        else if (toolbar.getTitle().equals("Other desserts")) {
            openRecipe(otherRecipeList,i);
        }
    }


    public void openRecipe(ArrayList<recipe> recipes, int position){

        String name = recipes.get(position).getName();
        String url = recipes.get(position).getUrl();
        String level = recipes.get(position).getLevel();
        String time = recipes.get(position).getTime();
        String serving = recipes.get(position).getServing();
        String material = recipes.get(position).getMaterial();
        String step = recipes.get(position).getStep();

        Intent g = new Intent(MainActivity.this, DessertActivity.class);
        g.putExtra("name", name);
        g.putExtra("url", url);
        g.putExtra("level", level);
        g.putExtra("time", time);
        g.putExtra("serving", serving);
        g.putExtra("material", material);
        g.putExtra("step", step);
        startActivity(g);

    }



}




