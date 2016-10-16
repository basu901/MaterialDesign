package com.example.xyzreader.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.RippleDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xyzreader.R;
import com.example.xyzreader.data.ItemsContract;
import com.squareup.picasso.Picasso;

/**
 * Created by shaunak basu on 15-10-2016.
 */
public class DetailActivityFragment extends Fragment {

    String url,published_date,author,title,body,aspect_ratio;
    Long itemId;
    View rootView;

    public DetailActivityFragment(){}

    public static Fragment newInstance(long item_Id) {
        Bundle arguments = new Bundle();
        arguments.putLong("item_id", item_Id);
        DetailActivityFragment fragment = new DetailActivityFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle extras=getArguments();
        itemId=extras.getLong("item_id");
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.detail_activity_fragment, container, false);
        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Uri uri= ItemsContract.Items.buildItemUri(itemId);
        Cursor cursor=getActivity().getContentResolver().query(uri,new String[]{ItemsContract.Items._ID,
                ItemsContract.Items.TITLE,
                ItemsContract.Items.PUBLISHED_DATE,
                ItemsContract.Items.AUTHOR,
                ItemsContract.Items.THUMB_URL,
                ItemsContract.Items.PHOTO_URL,
                ItemsContract.Items.ASPECT_RATIO,
                ItemsContract.Items.BODY},null,null,null);

        TextView article_details=(TextView)rootView.findViewById(R.id.detail_subtitle);
        final TextView article_body=(TextView)rootView.findViewById(R.id.detail_article_text);
        ImageView imageView=(ImageView)rootView.findViewById(R.id.detail_toolbar_image);
        ImageView back_image=(ImageView)rootView.findViewById(R.id.arrow_back);
        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });


        Toolbar toolbar=(Toolbar)rootView.findViewById(R.id.detail_toolbar);

        final CollapsingToolbarLayout collapsingToolbarLayout=(CollapsingToolbarLayout)rootView.findViewById(R.id.detail_collapsing);
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

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout.setTitle(title);

        //imageView.setAspectRatio(Float.parseFloat(aspect_ratio));
        Picasso.with(getActivity().getApplicationContext()).load(url).into(imageView);

        final String body_details="by "+author+", "+ Html.fromHtml(DateUtils.getRelativeTimeSpanString(
                Long.parseLong(published_date),
                System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_ALL).toString());
        String body_text=Html.fromHtml(body).toString();


        //Bitmap bitmap = Bitmap.createBitmap(imageView.getWidth(),imageView.getHeight(),Bitmap.Config.ARGB_8888);

        article_details.setText(body_details);
        article_body.setText(body_text);

        FloatingActionButton share_button=(FloatingActionButton) rootView.findViewById(R.id.share_fab);
        share_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent sharing_intent=new Intent(Intent.ACTION_SEND);
                sharing_intent.setType("text");
                sharing_intent.putExtra(android.content.Intent.EXTRA_TEXT,body_details);
                startActivity(Intent.createChooser(sharing_intent, "Share using"));
            }
        });


        /*Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {

            @Override
            public void onGenerated(Palette palette) {
                collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(getResources().getColor(R.color.primary)));

            }
        });*/
    }


}
