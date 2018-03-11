package comon.example.administrator.cniao.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.picasso.Picasso;

import comon.example.administrator.cniao.CniaoApplication;
import comon.example.administrator.cniao.Contants;
import comon.example.administrator.cniao.LoginActivity;
import comon.example.administrator.cniao.R;
import comon.example.administrator.cniao.bean.User;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Administrator on 2017/12/17.
 */

public class MineFragment extends BaseFragment {


    @ViewInject(R.id.img_head)
    private CircleImageView mImageHead;//把头像定义出来

    @ViewInject(R.id.txt_username)
    private TextView mTxtUserName;




    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_mine,container,false);
    }

    @Override
    public void init() {

//        //拿到user对象
//        User user = CniaoApplication.getInstance().getUser();
//        showUser(user);



    }

    @OnClick(value = {R.id.img_head,R.id.txt_username})
    public void toLogin(View view){

        //点击头像或点击“点击登录”textView的时候跳转到登录界面

        //通信代码
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivityForResult(intent, Contants.REQUEST_CODE);

    }

    @OnClick(R.id.btn_logout)
    public void logout(View view){

        CniaoApplication.getInstance().clearUser();
        showUser(null);
    }


    //用onActivityResult方法进行接收
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivityForResult(intent, Contants.REQUEST_CODE);


    }

    private void showUser(User user){

        if(user != null){
            //使用者的名字换一下
            mTxtUserName.setText(user.getUsername());
            //头像也要换一下
            Picasso.with(getActivity()).load(user.getLogo_url()).into(mImageHead);
        }
        else{
            mTxtUserName.setText(R.string.to_login);
        }


    }
}
