package cn.lixinye.knoewweather;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import cn.lixinye.knoewweather.db.DBManager;

public class MoreActivity extends AppCompatActivity implements View.OnClickListener {

    TextView bgTv,cacheTv,versionTv,shareTv;
    RadioGroup bgRg;
    ImageView backIv;
    private SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        bgTv = findViewById(R.id.more_tv_exchangebg);
        cacheTv = findViewById(R.id.more_tv_cache);
        versionTv = findViewById(R.id.more_tv_version);
        shareTv = findViewById(R.id.more_tv_share);
        backIv = findViewById(R.id.more_iv_back);
        bgRg = findViewById(R.id.more_rg);

        bgTv.setOnClickListener(this);
        cacheTv.setOnClickListener(this);
        shareTv.setOnClickListener(this);
        backIv.setOnClickListener(this);
        pref = getSharedPreferences("bg_pref", MODE_PRIVATE);
        String versionName = getVersionName();
        versionTv.setText("当前版本: v"+versionName);
        setRGListener();
    }

    private void setRGListener() {
        /* 设置改变背景图片的单选按钮的监听*/
        bgRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                获取目前的默认壁纸
                int bg = pref.getInt("bg", 0);
                SharedPreferences.Editor editor = pref.edit();
                Intent intent = new Intent(MoreActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                switch (checkedId) {
                    case R.id.more_rb_green:
                        if (bg==0) {
                            Toast.makeText(MoreActivity.this,"您选择的为当前背景，无需改变！",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        editor.putInt("bg",0);
                        editor.commit();
                        break;
                    case R.id.more_rb_pink:
                        if (bg==1) {
                            Toast.makeText(MoreActivity.this,"您选择的为当前背景，无需改变！",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        editor.putInt("bg",1);
                        editor.commit();
                        break;
                    case R.id.more_rb_blue:
                        if (bg==2) {
                            Toast.makeText(MoreActivity.this,"您选择的为当前背景，无需改变！",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        editor.putInt("bg",2);
                        editor.commit();
                        break;
                }
                startActivity(intent);
            }
        });

    }

    private String getVersionName() {

        PackageManager packageManager = getPackageManager();
        String versionName = null;
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.more_iv_back:
                finish();
                break;
            case R.id.more_tv_cache:
                clearCache();
                break;
            case R.id.more_tv_share:
                shareSoft("沃迪天气app是一款天气软件");
                break;
            case R.id.more_tv_exchangebg:
                if (bgRg.getVisibility() == View.VISIBLE) {
                    bgRg.setVisibility(View.GONE);
                }else{
                    bgRg.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void shareSoft(String message) {
        /*分享软件的函数*/
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,message);
        startActivity(Intent.createChooser(intent,"沃迪天气"));
    }

    private void clearCache() {
        /* 清除缓存*/
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息").setMessage("确定要删除所有的缓存吗?");
        builder.setPositiveButton("确定删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DBManager.clearAll();
                Toast.makeText(MoreActivity.this,"已清除所有的缓存数据!",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MoreActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }).setNegativeButton("取消删除",null);
        builder.create().show();
    }
}
