package com.jzd.Utils;

/**
 * Created by WALES7 on 2016/11/25.
 */

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class NetWorkUitls {
    public static void requestNet(String url,Map<String ,String> map,final CallBack callBack){
        if (url==null || callBack==null) {
            return;
        }
        //创建okHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //参数传递
        FormEncodingBuilder builder = new FormEncodingBuilder();
        Set<String> keySet=map.keySet();
        for (String i:keySet) {
            //从集合中一一取到对应的key和value
            String str=map.get(i);
            builder.add(i, str);

        }
        //创建一个Request
        Request request = new Request.Builder().url(url).post(builder.build()).build();
        //请求加入调度
        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Response response) throws IOException {
                String res=response.body().string();
                callBack.onSuccess(res);
            }

            @Override
            public void onFailure(Request request, IOException e) {
                callBack.onError(request, e);
            }
        });
    }
    /**
     * 网络请求回调接口
     * @author Administrator
     *
     */
    public interface CallBack{
        /**
         * 请求成功的回调
         * @param message
         */
        public void onSuccess(String message);
        /**
         * 请求失败的回调
         * @param e
         * @param errorCode
         */
        public void onError(Request request,Exception e);
    }

}