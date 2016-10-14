package com.example.xyzreader.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.data.ItemsContract;
import com.example.xyzreader.data.ItemsProvider;
import com.squareup.picasso.Picasso;

/**
 * Created by shaunak basu on 11-10-2016.
 */
public class DetailActivity extends AppCompatActivity {
    String url,published_date,author,title,body,aspect_ratio;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detail_activity_fragment);
        //int itemId=getIntent().getIntExtra("Id",-1);
        Uri uri= Uri.parse(getIntent().getStringExtra("URI"));
        Cursor cursor=getContentResolver().query(uri,new String[]{ItemsContract.Items._ID,
                ItemsContract.Items.TITLE,
                ItemsContract.Items.PUBLISHED_DATE,
                ItemsContract.Items.AUTHOR,
                ItemsContract.Items.THUMB_URL,
                ItemsContract.Items.PHOTO_URL,
                ItemsContract.Items.ASPECT_RATIO,
                ItemsContract.Items.BODY},null,null,null);

        TextView article_details=(TextView)findViewById(R.id.detail_subtitle);
        TextView article_body=(TextView)findViewById(R.id.detail_article_text);

        ImageView imageView=(ImageView)findViewById(R.id.detail_toolbar_image);
        Toolbar toolbar=(Toolbar)findViewById(R.id.detail_toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.detail_collapsing);
        try{
            if(cursor.moveToFirst()) {
                url = cursor.getString(cursor.getColumnIndex(ItemsContract.Items.PHOTO_URL));
                Log.v("In detail activity:",url);
                published_date = cursor.getString(cursor.getColumnIndex(ItemsContract.Items.PUBLISHED_DATE));

                Log.v("In detail activity:",published_date);
                author = cursor.getString(cursor.getColumnIndex(ItemsContract.Items.AUTHOR));
                Log.v("In detail activity:",author);
                title = cursor.getString(cursor.getColumnIndex(ItemsContract.Items.TITLE));
                Log.v("In detail activity:",title);
                aspect_ratio = cursor.getString(cursor.getColumnIndex(ItemsContract.Items.ASPECT_RATIO));
                Log.v("In detail activity:",aspect_ratio);
                body=cursor.getString(cursor.getColumnIndex(ItemsContract.Items.BODY));
                Log.v("In detail activity:",body);
            }
            }catch(NullPointerException e){
            Log.v("In DETAIL ACTIVITY","NO cursor result");
        }
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);

        collapsingToolbarLayout.setTitle(title);

        //imageView.setAspectRatio(Float.parseFloat(aspect_ratio));
        Picasso.with(getApplicationContext()).load(url).into(imageView);

        String body_details="by "+author+", "+Html.fromHtml(DateUtils.getRelativeTimeSpanString(
                Long.parseLong(published_date),
                System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_ALL).toString());
        String body_text=Html.fromHtml(body).toString();

        article_details.setText(body_details);
        article_body.setText(body_text);

    }

}

