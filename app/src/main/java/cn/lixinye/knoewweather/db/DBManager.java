package cn.lixinye.knoewweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    public static SQLiteDatabase dataBase;
    /*初始化数据库信息*/
    public static void initDB(Context context){
        DBHelper dbHelper = new DBHelper(context);
        dataBase = dbHelper.getWritableDatabase();
    }

    /*查找数据库中的后添加的五个城市*/
    public static List<String> queryFiveCity(){
        int count = getCityCount();
        if(count-5 > 0){
            count = count -5;
        }else{
            count = 0;
        }
        Cursor cursor = dataBase.query("info", null, null, null, null, null, null,count+",5");
        List<String> cityList = new ArrayList<>();
        while(cursor.moveToNext()){
            String city = cursor.getString(cursor.getColumnIndex("city"));
            cityList.add(city);
        }
        return cityList;
    }

    /*查询数据中所有的城市名*/
    public static List<String> queryAllCityName(){
        Cursor cursor = dataBase.query("info", null, null, null, null, null, null);
        List<String> cityList = new ArrayList<>();
        while(cursor.moveToNext()){
            String city = cursor.getString(cursor.getColumnIndex("city"));
            cityList.add(city);
        }
        return cityList;
    }

    /*根据城市名称替换信息内容*/
    public static int updateInfoByCity(String city,String content){
        ContentValues contentValues = new ContentValues();
        contentValues.put("content",content);
        return dataBase.update("info",contentValues,"city=?",new String[]{city});
    }

    /*新增一条城市记录*/
    public static long addCityInfo(String city,String content){
        ContentValues contentValues = new ContentValues();
        contentValues.put("city",city);
        contentValues.put("content",content);
        return dataBase.insert("info",null,contentValues);
    }

    /*更具城市名查询数据库中的内容*/
    public static String queryInfoByCity(String city){
        Cursor cursor = dataBase.query("info", null, "city=?", new String[]{city}, null, null, null);
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            String content = cursor.getString(cursor.getColumnIndex("content"));
            return content;
        }
        return null;
    }

    /*存储城市天气要求不能超过5个，一旦超过五个就不能在进行存储了，获取目前已经存储的数量*/
    public static int getCityCount(){
        Cursor cursor = dataBase.query("info", null, null, null, null, null, null);
        int count = cursor.getCount();
        return count;
    }

    /*查询数据库中的全部信息*/
    public static List<DatabaseBean> queryAllInfo(){
        List<DatabaseBean> list = new ArrayList<>();
        Cursor cursor = dataBase.query("info", null, null, null, null, null, null);
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String city = cursor.getString(cursor.getColumnIndex("city"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            DatabaseBean databaseBean = new DatabaseBean(id,city,content);
            list.add(databaseBean);
        }
        return list;
    }

    /*根据城市名删除数据库中的城市信息*/
    public static int deleteCityByName(String city){
        int info = dataBase.delete("info", "city=?", new String[]{city});
        return info;
    }

    public static void clearAll() {
        String sql = "delete from info";
        dataBase.execSQL(sql);
    }
}
