package cn.lixinye.knoewweather.CityManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import cn.lixinye.knoewweather.MainActivity;
import cn.lixinye.knoewweather.R;
import cn.lixinye.knoewweather.base.BaseActivity;
import cn.lixinye.knoewweather.bean.WeatherBean;

public class SearchCityActivity extends BaseActivity implements View.OnClickListener {

    EditText searchEt;
    ImageView submitIv;
    GridView searchGv;
    String[] hotCitys = {"北京","上海","广州","深圳","珠海","佛山","南京","苏州","厦门","长沙","成都","福州",
            "杭州","武汉","青岛","西安","太原","沈阳","重庆","天津"};
    String url1 = "https://www.tianqiapi.com/api/?appid=52386754&appsecret=eQ5SSdCR%20&version=v9&cityid=0&city=";
    String url2 = "&ip=0&callback=0";
    String s;
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city);
        searchEt = findViewById(R.id.search_et);
        submitIv = findViewById(R.id.search_iv_submit);
        searchGv = findViewById(R.id.search_gv);
        submitIv.setOnClickListener(this);
        //设置适配器
        adapter = new ArrayAdapter<String>(this,R.layout.item_hotcity,hotCitys);
        searchGv.setAdapter(adapter);
        setListener();
    }

    /*设置监听事件*/
    private void setListener() {
        searchGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                s = hotCitys[position];
                String url = url1 + s + url2;
                loadData(url);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_iv_submit:
                s = searchEt.getText().toString();
                if (!TextUtils.isEmpty(s)) {
                    //判断是否能找到输入的城市
                    String url = url1 + s + url2;
                    loadData(url);

                }else{
                    Toast.makeText(this,"请输入需要查找的城市名!",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onSuccess(String result) {
        WeatherBean weatherBean = new Gson().fromJson(result, WeatherBean.class);
        String city = weatherBean.getCity();
        if( !s.equals("北京") && city.equals("北京")){
            Toast.makeText(this,"查找不到城市名!",Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("city",s);
            startActivity(intent);
        }
    }
}
