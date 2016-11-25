package com.jzd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jzd.Utils.NetWorkUitls;
import com.squareup.okhttp.FormEncodingBuilder;

import com.jzd.Utils.HttpUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG ="TestAPI" ;
    //用户名
    private EditText mEtUsername;
    //密码
    private EditText mEtPwd;
    //登录按键
    private Button mBtnLogin;
    private TextView mTvResult;

    private String url ="http://192.168.2.20/wish/index.php/Api/Api/appLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
    }

    /**
     * 初始化组件
     */
    private void initView() {
        mEtUsername = (EditText) findViewById(R.id.login_et_name);
        mEtPwd = (EditText) findViewById(R.id.login_et_pwd);
        mBtnLogin = (Button) findViewById(R.id.login_btn_login);
        mTvResult = (TextView) findViewById(R.id.login_tv_result);

    }

    /**
     * 设置监听器
     */
    private void initListener() {
        mBtnLogin.setOnClickListener(this);
    }

    /*
    单击事件监听
     */
    @Override
    public void onClick(View v) {
        if(v==mBtnLogin){
            Log.d(TAG,"CLICK--"+v.getId());
            login();
        }
    }

    /*
    登录
     */
    private void login() {
        final String username = mEtUsername.getText().toString().trim();
        final String password = mEtPwd.getText().toString().trim();

        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
            Toast.makeText(MainActivity.this, "用户名或者密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(){
            @Override
            public void run() {
                HttpUtils httpUtils = new HttpUtils();
                //转换为JSON
                String user = httpUtils.bolwingJson(username, password);
                //String user ="{'username':" + username + ","+"'password':"+password+"}";
//                Log.d(TAG, "user:" + user);
                try {
                    final String result = httpUtils.login(url, user);
                    HashMap<String,String> params = new HashMap<String,String>();
                    params.put("username",username);
                    params.put("password",password);

                    NetWorkUitls.requestNet(url, params, new NetWorkUitls.CallBack() {
                        @Override
                        public void onSuccess(String message) {
                            //更新UI,在UI线程中
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if("SUCCESS".equals(result)){
                                        mTvResult.setText("登录成功");
                                    }else{
                                        mTvResult.setText("登录失败");
                                    }
                                }
                            });
                            Log.d(TAG, "结果:" + result);
                        }

                        @Override
                        public void onError(com.squareup.okhttp.Request request, Exception e) {
                        }
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
