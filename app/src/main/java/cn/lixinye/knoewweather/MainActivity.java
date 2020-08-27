package cn.lixinye.knoewweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import cn.lixinye.knoewweather.CityManager.CityManagerActivity;
import cn.lixinye.knoewweather.db.DBManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView addCityIv,moreIv;
    LinearLayout pointLayout;
    RelativeLayout outLayout;
    ViewPager mainVp;
    //viewpage的数据源
    List<Fragment> fragmentList;
    //表示需要显示的城市的集合
    List<String> cityList;
    //表示viewpage的个数指示器显示的内容
    List<ImageView> imgList;
    int pageShow = -1;
    private CityFragmentPageAdapter adapter;
    private SharedPreferences pref;
    private int bgNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addCityIv = findViewById(R.id.main_iv_add);
        moreIv = findViewById(R.id.main_iv_more);
        pointLayout = findViewById(R.id.main_layout_point);
        outLayout = findViewById(R.id.main_out_layout);
        exchangeBg();
        mainVp = findViewById(R.id.main_vp);
        //添加点击事件
        addCityIv.setOnClickListener(this);
        moreIv.setOnClickListener(this);
        fragmentList = new ArrayList<>();
        //获取数据库中的城市列表
        cityList = DBManager.queryFiveCity();
        imgList = new ArrayList<>();
        if(cityList.size() == 0){
            cityList.add("南昌");
        }

        /*搜索见面点击可以跳转到此界面，需要获取city来进行天气的显示*/
        Intent intent = getIntent();
        String city = intent.getStringExtra("city");
        if (!cityList.contains(city) && !TextUtils.isEmpty(city)) {
            if(cityList.size() == 5){
                cityList.remove(0);
            }
            cityList.add(city);
        }else if(cityList.contains(city)){
            pageShow = cityList.indexOf(city);
        }

        //初始化viewpage页面的方法
        initPager();
        adapter = new CityFragmentPageAdapter(getSupportFragmentManager(), fragmentList);
        mainVp.setAdapter(adapter);
        //创建小圆点指示器
        initPoint();
        //设置显示的城市的信息
        if(pageShow != -1){
            mainVp.setCurrentItem(pageShow);
            for(int i=0;i<imgList.size();i++){
                imgList.get(i).setImageResource(R.mipmap.a1);
            }
            imgList.get(pageShow).setImageResource(R.mipmap.a2);
        }else{
            mainVp.setCurrentItem(fragmentList.size()-1);
        }
        //设置viewpage页面监听
        setPagerListener();
    }

    /*更换壁纸*/
    public void exchangeBg(){
        pref = getSharedPreferences("bg_pref", MODE_PRIVATE);
        bgNum = pref.getInt("bg", 0);
        switch (bgNum) {
            case 0:
                outLayout.setBackgroundResource(R.drawable.bg_gradient);
                break;
            case 1:
                outLayout.setBackgroundResource(R.mipmap.bg2);
                break;
            case 2:
                outLayout.setBackgroundResource(R.mipmap.bg3);
                break;
        }
    }

    private void setPagerListener() {
        //设置监听事件
        mainVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                for(int i=0;i<imgList.size();i++){
                    imgList.get(i).setImageResource(R.mipmap.a1);
                }
                imgList.get(position).setImageResource(R.mipmap.a2);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void initPoint() {
        //创建小圆点
        for(int i=0;i<fragmentList.size();i++){
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.mipmap.a1);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) imageView.getLayoutParams();
            lp.setMargins(0,0,20,0);
            imgList.add(imageView);
            pointLayout.addView(imageView);
        }
        imgList.get(imgList.size()-1).setImageResource(R.mipmap.a2);
    }

    private void initPager() {
        //创建Fragment对象添加到Viewpage数据源当中
        for(int i=0;i<cityList.size();i++){
            CityWeatherFragment cwFrag = new CityWeatherFragment();
            Bundle bundle = new Bundle();
            bundle.putString("city",cityList.get(i));
            cwFrag.setArguments(bundle);
            fragmentList.add(cwFrag);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.main_iv_add:
                intent.setClass(this, CityManagerActivity.class);
                break;
            case R.id.main_iv_more:
                intent.setClass(this, MoreActivity.class);
                break;
        }
        startActivity(intent);
    }

    /*在页面重写加载时会调用的函数，这个函数在页面获取焦点之前进行调用，此处完成viewpage的更新*/
    @Override
    protected void onRestart() {
        super.onRestart();
        //获取数据库中还剩下的列表
        List<String> list = DBManager.queryFiveCity();
        if (list.size()==0) {
            cityList.add("南昌");
        }
        cityList.clear();
        cityList.addAll(list);
        //剩下的城市也要创建对饮的fragment页面
        fragmentList.clear();
        initPager();
        adapter.notifyDataSetChanged();
        //页面数量发生改变指示器的数量也会发生变化
        imgList.clear();
        pointLayout.removeAllViews();
        initPoint();
        mainVp.setCurrentItem(fragmentList.size()-1);
    }
}
