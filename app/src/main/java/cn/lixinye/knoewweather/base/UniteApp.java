package cn.lixinye.knoewweather.base;

import android.app.Application;

import org.xutils.DbManager;
import org.xutils.x;

import cn.lixinye.knoewweather.db.DBManager;

public class UniteApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        DBManager.initDB(this);
    }
}
