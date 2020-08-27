package cn.lixinye.knoewweather;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import cn.lixinye.knoewweather.base.BaseFragment;
import cn.lixinye.knoewweather.bean.WeatherBean;
import cn.lixinye.knoewweather.db.DBManager;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class CityWeatherFragment extends BaseFragment implements View.OnClickListener {
    TextView cityTv,dayTv,statusTv,tempTv,pmTv,airquilityTv,mintempTv,maxtempTv,sunriseTv,sunsetTv,windTv,winddirTv,humidityTv,dressTv,washcarTv,bloodsugarTv,reduceweightTv,raysTv,airpolluteTv;
    ImageView weathericon;
    LinearLayout futureLayout;
    ScrollView outLayout;
    String url1 = "https://www.tianqiapi.com/api/?appid=52386754&appsecret=eQ5SSdCR%20&version=v9&cityid=0&city=";
    String url2 = "&ip=0&callback=0";
    String city;
    private WeatherBean.DataBean dataBeanList;
    private SharedPreferences pref;
    private int bgNum;

    /*更换壁纸*/
    public void exchangeBg(){
        pref = getActivity().getSharedPreferences("bg_pref",MODE_PRIVATE);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_weather, container, false);
        initView(view);
        exchangeBg();
        //可以通过activity传值获取到当前fragment加载的是那个地方的天气情况
        Bundle bundle = getArguments();
        city = bundle.getString("city");
        String url = url1+city+url2;
        //调用父类的获取数据方法
        loadData(url);
        return view;
    }

    @Override
    public void onSuccess(String result) {
        //解析并展示数据
        parseShowData(result);
        //更新数据
        int i = DBManager.updateInfoByCity(city, result);
        if(i<=0){
            //更新数据库失败，没有这个城市,需要增加城市记录
            DBManager.addCityInfo(city,result);
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        //数据库中查找你上一次的城市信息进行显示
        String s = DBManager.queryInfoByCity(city);
        if (!TextUtils.isEmpty(s)) {
            parseShowData(s);
        }
    }

    private void parseShowData(String result) {
        //使用gson解析数据
        WeatherBean weatherBean = new Gson().fromJson(result, WeatherBean.class);
        //第一天的所有天气信息
        dataBeanList = weatherBean.getData().get(0);
        String status = dataBeanList.getWea_img();
        //设置textview
        tempTv.setText(dataBeanList.getTem()+"°C");
        statusTv.setText(dataBeanList.getWea());
        setweathericon(status,weathericon);
        dayTv.setText(dataBeanList.getDate());
        cityTv.setText(weatherBean.getCity());
        humidityTv.setText(dataBeanList.getHumidity());
        mintempTv.setText("最低温度:"+dataBeanList.getTem2()+"°C");
        maxtempTv.setText("最高温度"+dataBeanList.getTem1()+"°C");
        pmTv.setText("PM2.5："+weatherBean.getAqi().getPm25());
        airquilityTv.setText("空气质量:"+dataBeanList.getAir_level());
        sunriseTv.setText(dataBeanList.getSunrise()+"AM");
        sunsetTv.setText(dataBeanList.getSunset()+"PM");
        windTv.setText(dataBeanList.getWin_speed());
        winddirTv.setText(dataBeanList.getWin().get(0));
        //获取未来六天的天气情况加载到layout中
        List<WeatherBean.DataBean> futureList = weatherBean.getData();
        futureList.remove(0);
        for(int i=0;i<futureList.size();i++){
            if(getActivity()==null){
                return;
            }
            View itemview = LayoutInflater.from(getActivity()).inflate(R.layout.item_layout,null);
            itemview.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            futureLayout.addView(itemview);
            TextView idayTv = itemview.findViewById(R.id.tv_data_title);
            TextView imintempTv = itemview.findViewById(R.id.tv_low_temp);
            TextView imaxtempTv = itemview.findViewById(R.id.tv_high_temp);
            TextView idaytypeTv = itemview.findViewById(R.id.tv_day_type);
            TextView inighttypeTv = itemview.findViewById(R.id.tv_night_type);
            TextView idaywindTv = itemview.findViewById(R.id.tv_day_wind_force);
            TextView inightwindTv = itemview.findViewById(R.id.tv_night_wind_force);
            TextView idaywinddirTv = itemview.findViewById(R.id.tv_day_wind_dir);
            TextView inightwinddirTv = itemview.findViewById(R.id.tv_night_wind_dir);
            ImageView idayiconTv = itemview.findViewById(R.id.tv_day_icon);
            ImageView inighticonTv = itemview.findViewById(R.id.tv_night_icon);
            //获取josn中后六天的天气情况
            WeatherBean.DataBean dataBean = futureList.get(i);
            idayTv.setText(dataBean.getDay());
            imintempTv.setText("最低温度:"+dataBean.getTem2()+"°C");
            imaxtempTv.setText("最高温度:"+dataBean.getTem1()+"°C");
            idaytypeTv.setText(dataBean.getWea_day());
            inighttypeTv.setText(dataBean.getWea_night());
            idaywindTv.setText("风力:"+dataBean.getHours().get(0).getWin_speed());
            inightwindTv.setText("风力:"+dataBean.getHours().get(dataBean.getHours().size()-1).getWin_speed());
            idaywinddirTv.setText("风向:"+dataBean.getWin().get(0));
            inightwinddirTv.setText("风向:"+dataBean.getWin().get(1));
            setweathericon(dataBean.getWea_day_img(),idayiconTv);
            setweathericon(dataBean.getWea_night_img(),inighticonTv);
        }

    }

    private void setweathericon(String status,ImageView weathericon) {
        if(status.equals("qing")){
            weathericon.setImageResource(R.drawable.qing);
        }else if(status.equals("yun")){
            weathericon.setImageResource(R.drawable.yun);
        }else if(status.equals("yin")){
            weathericon.setImageResource(R.drawable.yin);
        }else if(status.equals("lei")){
            weathericon.setImageResource(R.drawable.lei);
        }else if(status.equals("yu")){
            weathericon.setImageResource(R.drawable.yu);
        }else if(status.equals("xue")){
            weathericon.setImageResource(R.drawable.xue);
        }
    }

    private void initView(View view) {
        weathericon = view.findViewById(R.id.frag_tv_weather_icon);
        tempTv = view.findViewById(R.id.frag_tv_temp);
        cityTv = view.findViewById(R.id.frag_tv_city);
        dayTv  = view.findViewById(R.id.frag_tv_day);
        windTv = view.findViewById(R.id.frag_tv_wind);
        statusTv = view.findViewById(R.id.frag_tv_status);
        pmTv = view.findViewById(R.id.frag_tv_pm25);
        airquilityTv  = view.findViewById(R.id.frag_tv_air_quality);
        mintempTv = view.findViewById(R.id.frag_tv_temp_min);
        maxtempTv = view.findViewById(R.id.frag_tv_temp_max);
        sunriseTv = view.findViewById(R.id.frag_tv_sunrise);
        sunsetTv = view.findViewById(R.id.frag_tv_sunset);
        winddirTv = view.findViewById(R.id.frag_tv_wind_dir);
        humidityTv = view.findViewById(R.id.frag_tv_humidity);
        windTv = view.findViewById(R.id.frag_tv_wind);
        dressTv = view.findViewById(R.id.frag_index_tv_dress);
        bloodsugarTv = view.findViewById(R.id.frag_index_tv_bloodsugar);
        washcarTv = view.findViewById(R.id.frag_index_tv_washcar);
        reduceweightTv = view.findViewById(R.id.frag_index_tv_reduceweight);
        raysTv = view.findViewById(R.id.frag_index_tv_rays);
        airpolluteTv = view.findViewById(R.id.frag_index_tv_airpollute);
        futureLayout = view.findViewById(R.id.frag_center_layout);
        outLayout = view.findViewById(R.id.out_layout);
        //设置点击事件的监听
        dressTv.setOnClickListener(this);
        bloodsugarTv.setOnClickListener(this);
        washcarTv.setOnClickListener(this);
        reduceweightTv.setOnClickListener(this);
        raysTv.setOnClickListener(this);
        airpolluteTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        switch (v.getId()) {
            case R.id.frag_index_tv_dress:
                builder.setTitle("穿衣指数");
                WeatherBean.DataBean.IndexBean indexBean = dataBeanList.getIndex().get(3);
                String msg = indexBean.getLevel()+"\n"+indexBean.getDesc();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
            case R.id.frag_index_tv_washcar:
                builder.setTitle("洗车指数");
                indexBean = dataBeanList.getIndex().get(4);
                msg = indexBean.getLevel()+"\n"+indexBean.getDesc();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
            case R.id.frag_index_tv_bloodsugar:
                builder.setTitle("血糖指数");
                indexBean = dataBeanList.getIndex().get(2);
                msg = indexBean.getLevel()+"\n"+indexBean.getDesc();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
            case R.id.frag_index_tv_reduceweight:
                builder.setTitle("减肥指数");
                indexBean = dataBeanList.getIndex().get(1);
                msg = indexBean.getLevel()+"\n"+indexBean.getDesc();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
            case R.id.frag_index_tv_rays:
                builder.setTitle("紫外线指数");
                indexBean = dataBeanList.getIndex().get(0);
                msg = indexBean.getLevel()+"\n"+indexBean.getDesc();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
            case R.id.frag_index_tv_airpollute:
                builder.setTitle("空气污染指数");
                indexBean = dataBeanList.getIndex().get(5);
                msg = indexBean.getLevel()+"\n"+indexBean.getDesc();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
        }
        builder.create().show();
    }
}
