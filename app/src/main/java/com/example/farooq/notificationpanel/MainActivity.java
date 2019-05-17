package com.example.farooq.notificationpanel;

import android.content.Intent;
import android.graphics.Color;
import android.os.TestLooperManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView profileLabel;
    private TextView usersLabel;
    private TextView notificationLabel;
    private ViewPager pager;
    private PagerViewAdapter adapter;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser mCurrentUser = mAuth.getCurrentUser();
        if(mCurrentUser==null){
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        profileLabel=(TextView) findViewById(R.id.profileLabel);
        usersLabel=(TextView) findViewById(R.id.usersLabel);
        notificationLabel=(TextView) findViewById(R.id.notificationLabel);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(2);
        adapter= new PagerViewAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            @Override public void onPageSelected(int position) { changeTabs(position); }
            @Override public void onPageScrollStateChanged(int state) { }
        });
        profileLabel.setOnClickListener(this);
        usersLabel.setOnClickListener(this);
        notificationLabel.setOnClickListener(this);
    }

    private void changeTabs(int postion){
        switch (postion){
            case 0:
                profileLabel.setTextSize(22);profileLabel.setTextColor(Color.parseColor("#FFFFFF"));
                usersLabel.setTextSize(16);usersLabel.setTextColor(Color.parseColor("#cccccc"));
                notificationLabel.setTextSize(16);notificationLabel.setTextColor(Color.parseColor("#cccccc"));
                break;
            case 1:
                profileLabel.setTextSize(16);profileLabel.setTextColor(Color.parseColor("#cccccc"));
                usersLabel.setTextSize(22);usersLabel.setTextColor(Color.parseColor("#FFFFFF"));
                notificationLabel.setTextSize(16);notificationLabel.setTextColor(Color.parseColor("#cccccc"));
                break;
            case 2:
                profileLabel.setTextSize(16);profileLabel.setTextColor(Color.parseColor("#cccccc"));
                usersLabel.setTextSize(16);usersLabel.setTextColor(Color.parseColor("#cccccc"));
                notificationLabel.setTextSize(22);notificationLabel.setTextColor(Color.parseColor("#FFFFFF"));
                break;
        }
    }

    @Override public void onClick(View v) {
        if (v==profileLabel){pager.setCurrentItem(0);}
        if (v==usersLabel){pager.setCurrentItem(1);}
        if (v==notificationLabel){pager.setCurrentItem(2);}
    }
}
