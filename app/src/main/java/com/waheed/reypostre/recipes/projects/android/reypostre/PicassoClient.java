package com.waheed.reypostre.recipes.projects.android.reypostre;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by waheed on 9/9/2017.
 */

public class PicassoClient {

    public void downloadImage(Context c , String url , ImageView imageView){
        if(url != null && url.length()>0){
            Picasso.with(c).load(url).placeholder(R.drawable.intro_lgog).into(imageView);
        }else{
            Picasso.with(c).load(R.mipmap.ic_launcher).into(imageView);
        }
    }
}
