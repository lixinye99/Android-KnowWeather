package cn.lixinye.knoewweather.CityManager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.lixinye.knoewweather.R;
import cn.lixinye.knoewweather.db.DBManager;
import cn.lixinye.knoewweather.db.DatabaseBean;

public class CityManagerActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView addTv,backTv,deleteTv;
    ListView cityLv;
    List<DatabaseBean> mDatas;//显示列表数据源
    private CityManagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manager);
        addTv = findViewById(R.id.city_iv_add);
        backTv = findViewById(R.id.city_iv_back);
        deleteTv = findViewById(R.id.city_iv_delete);
        cityLv = findViewById(R.id.city_lv);
        mDatas = new ArrayList<>();
        //添加点击事件
        addTv.setOnClickListener(this);
        backTv.setOnClickListener(this);
        deleteTv.setOnClickListener(this);
        //设置适配器
        adapter = new CityManagerAdapter(this, mDatas);
        cityLv.setAdapter(adapter);
    }

    /*获取数据库中的真实数据源，添加到适配器中，提示适配器更新*/
    @Override
    protected void onResume() {
        super.onResume();
        List<DatabaseBean> list = DBManager.queryAllInfo();
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.city_iv_add:
                Intent intent = new Intent(this, SearchCityActivity.class);
                startActivity(intent);
                break;
            case R.id.city_iv_back:
                finish();
                break;
            case R.id.city_iv_delete:
                Intent intent1 = new Intent(this, DeleteCityActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
