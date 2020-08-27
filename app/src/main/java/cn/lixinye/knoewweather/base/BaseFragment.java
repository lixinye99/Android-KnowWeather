package cn.lixinye.knoewweather.base;


import androidx.fragment.app.Fragment;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/*
* 加载网络请求数据的步骤
* 声明整体模块
* 执行网络请求
* */
public class BaseFragment extends Fragment implements Callback.CommonCallback<String> {
    public void loadData(String path){
        RequestParams params = new RequestParams(path);
        x.http().get(params,this);
    }
    //获取成功回调的接口
    @Override
    public void onSuccess(String result) {

    }
    //获取失败回调的接口
    @Override
    public void onError(Throwable ex, boolean isOnCallback) {

    }
    //取消请求回调的接口
    @Override
    public void onCancelled(CancelledException cex) {

    }
    //请求完成回调的接口
    @Override
    public void onFinished() {

    }
}
