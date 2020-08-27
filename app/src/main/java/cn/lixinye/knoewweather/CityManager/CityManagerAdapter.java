package cn.lixinye.knoewweather.CityManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import cn.lixinye.knoewweather.R;
import cn.lixinye.knoewweather.bean.WeatherBean;
import cn.lixinye.knoewweather.db.DatabaseBean;

public class CityManagerAdapter extends BaseAdapter {
    Context context;
    List<DatabaseBean> mDatas;

    public CityManagerAdapter(Context context, List<DatabaseBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_city_manager,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        DatabaseBean databaseBean = mDatas.get(position);
        holder.cityTv.setText(databaseBean.getCity());
        WeatherBean weatherBean = new Gson().fromJson(databaseBean.getContent(), WeatherBean.class);
        List<WeatherBean.DataBean> data = weatherBean.getData();
        holder.currentTempTv.setText(data.get(0).getTem()+"°C");
        holder.conTV.setText("天气:"+data.get(0).getWea());
        holder.windTv.setText(data.get(0).getWin().get(0)+data.get(0).getWin_speed());
        holder.tempRangeTv.setText(data.get(0).getTem2()+"~"+data.get(0).getTem2()+"°C");
        return convertView;

    }

    class ViewHolder{
        TextView cityTv,conTV,currentTempTv,windTv,tempRangeTv;

        public ViewHolder(View itemview) {
            cityTv = itemview.findViewById(R.id.item_city_tv_city);
            conTV = itemview.findViewById(R.id.item_city_tv_condition);
            currentTempTv = itemview.findViewById(R.id.item_city_tv_temp);
            windTv = itemview.findViewById(R.id.item_city_wind);
            tempRangeTv = itemview.findViewById(R.id.item_city_temprange);
        }
    }
}
