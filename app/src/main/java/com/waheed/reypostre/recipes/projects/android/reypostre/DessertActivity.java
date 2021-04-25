package com.waheed.reypostre.recipes.projects.android.reypostre;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by waheed on 9/26/2017.
 */

public class DessertActivity extends AppCompatActivity {

    Context context2 = this;
    Toolbar toolbar;
    FloatingActionButton fab;
    String name;
    String level;
    String time;
    String serving;
    String material;
    String step;
    SQLiteDatabase sqLiteDatabase;
    boolean isEx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dessert_activity);

        sqLiteDatabase = new SQLiteDatabase(getBaseContext());
        fab = (FloatingActionButton) findViewById(R.id.fab_share);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = getIntent().getStringExtra("name");
        final String url = getIntent().getStringExtra("url");
        level = getIntent().getStringExtra("level");
        time = getIntent().getStringExtra("time");
        serving = getIntent().getStringExtra("serving");
        material = getIntent().getStringExtra("material");
        step = getIntent().getStringExtra("step");

        TextView materialText = (TextView) findViewById(R.id.integ_text);
        materialText.setText(material);

        TextView stepText = (TextView) findViewById(R.id.step_text);
        stepText.setText(step);

        TextView levelT = (TextView) findViewById(R.id.dess_level);
        levelT.setText(level);

        TextView timeT = (TextView) findViewById(R.id.dess_time);
        timeT.setText(time);

        TextView servT = (TextView) findViewById(R.id.people_dess);
        servT.setText(serving);

        ImageView imageView = (ImageView) findViewById(R.id.dess_image);

        PicassoClient picassoClient = new PicassoClient();
        picassoClient.downloadImage(context2, url, imageView);

        this.setTitle(name);

        isEx = sqLiteDatabase.isExist(url,name);
        if(!isEx){
            fab.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }else{
            fab.setImageResource(R.drawable.ic_favorite_black_24dp);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isEx) {
                    // save article
                    long result = sqLiteDatabase.WriteData(name,url,time,serving,level,material,step);

                    if(result != -1){
                        //done
                        fab.setImageResource(R.drawable.ic_favorite_black_24dp);
                        isEx = true;
                        Toast.makeText(getBaseContext(), "Recipe saved successfully", Toast.LENGTH_SHORT).show();
                    }else{
                        //failed
                        Toast.makeText(getBaseContext(), "Failed to save recipe!", Toast.LENGTH_SHORT).show();
                    }


                }else{

                    // remove article
                    long result = sqLiteDatabase.removeArticle(url);

                    if(result != -1){
                        //done
                        fab.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        isEx = false;
                        Toast.makeText(getBaseContext(), "Recipe removed successfully", Toast.LENGTH_SHORT).show();
                    }else{
                        //failed
                        Toast.makeText(getBaseContext(), "Failed to remove recipe!", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dess, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == android.R.id.home) {
            finish();
        }
        if(id == R.id.action_share){
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Recipe of " + name + " by Hello Dessert team" + "\n \n" + "Time:" + time + "\n" + "Level:" + level + "\n" + "Serving:" + serving + "\n \n" + "Integrates:" + "\n \n" + material + "\n \n \n" + "Steps:" + "\n \n" + step + "\n \n \n" + "Download our application to get more recipes..");
            shareIntent.setType("text/plain");
            startActivity(shareIntent);
        }


        return true;
    }
}