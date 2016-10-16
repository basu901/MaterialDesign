package com.example.xyzreader.ui;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.data.ItemsContract;
import com.example.xyzreader.data.ItemsProvider;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by shaunak basu on 11-10-2016.
 */
public class DetailActivity extends AppCompatActivity {

    ViewPager viewPager;
    Cursor mCursor;
    ArrayList<String> addElements=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detail_activity);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        Long mItemId=getIntent().getLongExtra("item_id",-1);
        //DetailActivityFragment detailActivityFragment = new DetailActivityFragment();
        //detailActivityFragment.setArguments(args);

        //getSupportFragmentManager().beginTransaction().add(R.id.pager_reader, detailActivityFragment).commit();

        viewPager = (ViewPager) findViewById(R.id.pager_reader);
        CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(customPagerAdapter);
        mCursor = getContentResolver().query(ItemsContract.Items.buildDirUri(), new String[]{ItemsContract.Items._ID},
                null, null, null);

        customPagerAdapter.notifyDataSetChanged();

        if (mItemId > 0) {
            mCursor.moveToFirst();
            while (!mCursor.isAfterLast()) {
                if (mCursor.getLong(mCursor.getColumnIndex(ItemsContract.Items._ID)) == mItemId) {
                    final int position = mCursor.getPosition();
                    viewPager.setCurrentItem(position, false);
                    break;
                }
                mCursor.moveToNext();
            }
            mItemId = 0L;
        }
    }



    class CustomPagerAdapter extends FragmentStatePagerAdapter{

        public CustomPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position){
            mCursor.moveToPosition(position);
            String id=mCursor.getString(mCursor.getColumnIndex(ItemsContract.Items._ID));
            Log.v("IN DETAIL ACTVITY!!!!:",id);
            return DetailActivityFragment.newInstance(Long.parseLong(mCursor.getString(mCursor.getColumnIndex(ItemsContract.Items._ID))));

        }

        @Override
        public int getCount(){
            return (mCursor != null) ? mCursor.getCount() : 0;
        }
    }

}

