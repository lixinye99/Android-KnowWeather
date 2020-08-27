package cn.lixinye.knoewweather.CityManager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.lixinye.knoewweather.R;
import cn.lixinye.knoewweather.db.DBManager;

public class DeleteCityActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView errorIv,rightIv;
    ListView deleteLv;
    List<String> mDatas;   //存储listview的数据源
    List<String> deleteCitys;  //存储删除的城市信息
    DeleteCityAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_city);
        errorIv = findViewById(R.id.delete_iv_error);
        rightIv = findViewById(R.id.delete_iv_right);
        deleteLv = findViewById(R.id.delete_lv);
        mDatas = DBManager.queryAllCityName();
        deleteCitys = new ArrayList<>();
        errorIv.setOnClickListener(this);
        rightIv.setOnClickListener(this);
        adapter = new DeleteCityAdapter(this, mDatas, deleteCitys);
        deleteLv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.delete_iv_error:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示信息").setMessage("您确定要舍弃所有的删除更改吗？")
                        .setPositiveButton("确认舍弃", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                builder.setNegativeButton("取消舍弃",null);
                builder.create().show();
                break;
            case R.id.delete_iv_right:
                //遍历需要删除的城市名，进行删除
                for (int i=0;i<deleteCitys.size();i++){
                    String city = deleteCitys.get(i);
                    DBManager.deleteCityByName(city);
                }
                //删除完毕后返回上级页面
                finish();
                break;
        }
    }
}
