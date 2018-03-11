package comon.example.administrator.cniao.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;

import comon.example.administrator.cniao.CniaoApplication;
import comon.example.administrator.cniao.LoginActivity;
import comon.example.administrator.cniao.bean.User;

public abstract class BaseFragment extends Fragment {





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = createView(inflater,container,savedInstanceState);
        ViewUtils.inject(this, view);

        initToolBar();

        init();

        return view;

    }

    public void  initToolBar(){

    }


    public abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract void init();


    //重载了startActivity的方法
    public void startActivity(Intent intent, boolean isNeedLogin){


        if(isNeedLogin){

            User user = CniaoApplication.getInstance().getUser();
            if(user !=null){
                super.startActivity(intent);//已经登录了的话就跳转到目标就可以了
            }
            else{

                CniaoApplication.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                super.startActivity(loginIntent);

            }

        }
        else{
            super.startActivity(intent);
        }

    }

}
