package comon.example.administrator.cniao.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import comon.example.administrator.cniao.Contants;
import comon.example.administrator.cniao.R;
import comon.example.administrator.cniao.WareListActivity;
import comon.example.administrator.cniao.adatper.CardViewtemDecortion;
import comon.example.administrator.cniao.adatper.HomeCatgoryAdapter;
import comon.example.administrator.cniao.bean.Banner;
import comon.example.administrator.cniao.bean.Campaign;
import comon.example.administrator.cniao.bean.HomeCampaign;
import comon.example.administrator.cniao.http.BaseCallback;
import comon.example.administrator.cniao.http.OkHttpHelper;
import comon.example.administrator.cniao.http.SpotsCallBack;


/**
 * Created by Administrator on 2017/12/17.
 */

public class HomeFragment extends Fragment {

    private SliderLayout mSliderLayout;

    //RecyclerView的代码
    private RecyclerView mRecyclerView;
    private List<String> datas = new ArrayList<>();
    private HomeCatgoryAdapter mAdatper;

    private static final String TAG="HomeFragment";

    //声明一个Gson用来解析Gson数据
    private Gson mGson = new Gson();
    //用一个List来接收Gson数据
    private List<Banner> mBanner;

    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view= inflater.inflate(R.layout.fragment_home,container,false);

        //广告轮播的代码
        mSliderLayout= (SliderLayout) view.findViewById(R.id.slider);
        requestImages();


        //RecyclerView的代码//首页商品分页的数据加载
        initRecyclerView(view);
        return view;
    }

    private void requestImages(){
        String url = "http://112.124.22.238:8081/course_api/banner/query?type=1";

        httpHelper.get(url, new SpotsCallBack<List<Banner>>(getContext()) {


            @Override
            public void onBeforeRequest(Request request) {

            }

            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onSuccess(Response response, List<Banner> banners) {

                mBanner = banners;
                initSlider();

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });



    }


    private void initRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
//
//        List<HomeCategory> datas = new ArrayList<>(15);
//
//        HomeCategory category = new HomeCategory("热门活动",R.drawable.img_big_1,R.drawable.img_1_small1,R.drawable.img_1_small2);
//        datas.add(category);
//
//        category = new HomeCategory("有利可图",R.drawable.img_big_4,R.drawable.img_4_small1,R.drawable.img_4_small2);
//        datas.add(category);
//        category = new HomeCategory("品牌街",R.drawable.img_big_2,R.drawable.img_2_small1,R.drawable.img_2_small2);
//        datas.add(category);
//
//        category = new HomeCategory("金融街 包赚翻",R.drawable.img_big_1,R.drawable.img_3_small1,R.drawable.imag_3_small2);
//        datas.add(category);
//
//        category = new HomeCategory("超值购",R.drawable.img_big_0,R.drawable.img_0_small1,R.drawable.img_0_small2);
//        datas.add(category);
//
//        mAdatper = new HomeCatgoryAdapter(datas);
//
//        mRecyclerView.setAdapter(mAdatper);
//
//        mRecyclerView.addItemDecoration(new DividerItemDecortion());
//
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        httpHelper.get(Contants.API.CAMPAIGN_HOME, new BaseCallback<List<HomeCampaign>>() {
            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onBeforeRequest(Request request) {

            }

            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, List<HomeCampaign> homeCampaigns) {
                initData(homeCampaigns);
            }


            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });
    }

    private void initData(List<HomeCampaign> homeCampaigns)
    {
        mAdatper = new HomeCatgoryAdapter(homeCampaigns,getActivity());

        mAdatper.setOnCampaignClickListener(new HomeCatgoryAdapter.OnCampaignClickListener() {
            @Override
            public void onClick(View view, Campaign campaign) {

                Intent intent = new Intent(getActivity(), WareListActivity.class);
                intent.putExtra(Contants.COMPAINGAIN_ID,campaign.getId());

                startActivity(intent);
            }
        });
        
        mRecyclerView.setAdapter(mAdatper);

        mRecyclerView.addItemDecoration(new CardViewtemDecortion());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    }

    //广告轮播的代码//通过TextSliderView去调用一些方法，相当于操作UI的控件
    private void initSlider(){

        if(mBanner != null){
            for (Banner banner : mBanner){

                TextSliderView textSliderView = new TextSliderView(this.getActivity());
                textSliderView.image(banner.getImgUrl());
                textSliderView.description(banner.getName());
                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                mSliderLayout.addSlider(textSliderView);
            }
        }




        //设置轮播风格
        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);//默认指示器indicator的风格
        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.RotateUp);//轮播动画
        mSliderLayout.setDuration(3000);

        //轮播广告的监听器（即发生点击事件时调用的方法）
        mSliderLayout.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int il) {

                Log.d(TAG,"oPageScrolled");
            }

            @Override
            public void onPageSelected(int i) {

                Log.d(TAG,"onPageSelected");
            }

            @Override
            public void onPageScrollStateChanged(int i) {

                Log.d(TAG,"onPageScrollStateChanged");
            }
        });
    }



}
