package com.example.myapplication;

import android.graphics.Bitmap;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    TabLayout tabs;
    ViewPager pager;
    ImageView iv;
    private int ival=0;
    private Bitmap kalu=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        tabs=findViewById(R.id.tabs);
        pager=findViewById(R.id.pager);
        iv=findViewById(R.id.iv);

        tabs.addTab(tabs.newTab().setText("HOME"));
        tabs.addTab(tabs.newTab().setText("EDIT"));
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);

        final MyAdapter adapter=new MyAdapter(this,getSupportFragmentManager(),tabs.getTabCount());
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    public void setIval(int ival){
        this.ival=ival;
    }
    public int getIval(){
        return this.ival;
    }
    public void setKalu(Bitmap kalu){
        this.kalu=kalu;
    }
    public Bitmap getKalu(){
        return this.kalu;
    }
}
