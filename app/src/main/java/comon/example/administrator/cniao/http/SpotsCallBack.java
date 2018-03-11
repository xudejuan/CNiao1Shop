package comon.example.administrator.cniao.http;

import android.content.Context;
import android.content.Intent;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import comon.example.administrator.cniao.CniaoApplication;
import comon.example.administrator.cniao.LoginActivity;
import comon.example.administrator.cniao.R;
import comon.example.administrator.cniao.utils.ToastUtils;
import dmax.dialog.SpotsDialog;

/**
 * Created by Administrator on 2017/12/29.
 */

public abstract class SpotsCallBack<T>  extends SimpleCallback<T>{

    SpotsDialog dialog;


    //对话框申明出来
    public SpotsCallBack(Context context){
        super(context);

        initSpotsDialog();
    }


    private  void initSpotsDialog(){

        dialog = new SpotsDialog(mContext,"拼命加载中...");
    }



    public void showDialog()
    {
        dialog.show();
    }

    //关闭对话框
    public void dismissDialog()
    {
        if(dialog != null)
            dialog.dismiss();
    }

    public void setMessage(String message)
    {
        dialog.setMessage(message);
    }

    @Override
    public void onRequestBefore(Request request) {
        showDialog();
    }

    @Override
    public void onFailure(Request request, IOException e) {
        dismissDialog();//失败的时候需要关闭
    }

    @Override
    public void onResponse(Response response)
    {
        dismissDialog();
    }

    public abstract void onFailure(Request request, Exception e);

    @Override
    public void onTokenError(Response response, int code) {

        ToastUtils.show(mContext, R.string.token_error);//提示验证失败

        //跳转到登录界面
        Intent intent = new Intent(mContext, LoginActivity.class);
        mContext.startActivity(intent);

        //把保存到本地的用户的数据清空掉
        CniaoApplication.getInstance().clearUser();
    }
}
