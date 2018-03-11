package comon.example.administrator.cniao.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import comon.example.administrator.cniao.Contants;
import comon.example.administrator.cniao.R;
import comon.example.administrator.cniao.adatper.BaseAdapter;
import comon.example.administrator.cniao.adatper.CategoryAdapter;
import comon.example.administrator.cniao.adatper.DividerItemDecoration;
import comon.example.administrator.cniao.adatper.WaresAdapter;
import comon.example.administrator.cniao.adatper.decoration.DividerGridItemDecoration;
import comon.example.administrator.cniao.bean.Banner;
import comon.example.administrator.cniao.bean.Category;
import comon.example.administrator.cniao.bean.Page;
import comon.example.administrator.cniao.bean.Wares;
import comon.example.administrator.cniao.http.BaseCallback;
import comon.example.administrator.cniao.http.OkHttpHelper;
import comon.example.administrator.cniao.http.SpotsCallBack;


/**
 * Created by Administrator on 2017/12/17.
 */

public class CategoryFragment extends Fragment {

    //把控件拿过来
    @ViewInject(R.id.recyclerview_category)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.recyclerview_wares)
    private RecyclerView mRecyclerviewWares;

    @ViewInject(R.id.refresh_layout)//注解
    private MaterialRefreshLayout mRefreshLayout;

    @ViewInject(R.id.slider)
    private SliderLayout mSliderLayout;

    private CategoryAdapter mCategoryAdapter;
    private WaresAdapter mWaresAdapter;

    //定义OkHttpHelper到服务端去拿数据
    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();

    private int currPage = 1;
    private int totalPage = 1;
    private int pageSize = 10;
    private long category_id = 0;

    //分页用到的
    private static final int STATE_NORMAL=0;
    private static final int STATE_REFREH=1;
    private static final int STATE_MORE=2;

    private int state=STATE_NORMAL;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_category, container, false);
        ViewUtils.inject(this,view);//requestCategoryData();之前必须要有这一步，要不然mRecyclerView全为空

        requestCategoryData();
        requestBannerData();
        initRefreshLayout();

        return view;
    }

    //初始化控件的方法
    private void initRefreshLayout(){
        //支持加载更多
        mRefreshLayout.setLoadMore(true);
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if(currPage <= totalPage)
                    loadMoreData();
                else{
//                        Toast.makeText(getContext(), "到底了", Toast.LENGTH_SHORT);
                    mRefreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }

    private void refreshData(){
        currPage =1;

        state=STATE_REFREH;
        requestWares(category_id);
    }

    private void loadMoreData(){
        currPage = ++currPage;
        state = STATE_MORE;

        requestWares(category_id);
    }

    private void requestCategoryData(){

        //拼命加载的CallBack
        mHttpHelper.get(Contants.API.CATEGORY_LIST, new SpotsCallBack<List<Category>>(getContext()) {


            @Override
            public void onBeforeRequest(Request request) {

            }

            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onSuccess(Response response, List<Category> categories) {
                showCategoryData(categories);

                if(categories != null && categories.size()>0)
                    category_id = categories.get(0).getId();
                    requestWares(category_id);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void showCategoryData(List<Category> categories){

        mCategoryAdapter = new CategoryAdapter(getContext(),categories);
        mCategoryAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Category category = mCategoryAdapter.getItem(position);

                category_id = category.getId();
                currPage=1;//状态重置
                state = STATE_NORMAL;//这样才不会出现bug

                requestWares(category_id);//把catrgory的Id传进来
            }
        });//设置点击事件

        mRecyclerView.setAdapter(mCategoryAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //设置动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));

    }

    private void requestBannerData( ) {

        String url = Contants.API.BANNER+"?type=1";

        mHttpHelper.get(url, new SpotsCallBack<List<Banner>>(getContext()) {

            @Override
            public void onBeforeRequest(Request request) {

            }

            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                showSliderViews(banners);

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });


    }

    //广告轮播的代码//通过TextSliderView去调用一些方法，相当于操作UI的控件
    private void showSliderViews(List<Banner> banners) {

        if (banners != null) {
            for (Banner banner : banners) {

                DefaultSliderView sliderView = new DefaultSliderView(this.getActivity());
                sliderView.image(banner.getImgUrl());
                sliderView.description(banner.getName());
                sliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                mSliderLayout.addSlider(sliderView);
            }
        }


        //设置轮播风格
        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);//默认指示器indicator的风格
        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);//轮播动画//转场效果
        mSliderLayout.setDuration(3000);
    }

    private void requestWares(long categoryId){

        String url = Contants.API.WARES_LIST+"?categoryId="+categoryId+"&curPage="+currPage+"&pageSize="+pageSize;

        mHttpHelper.get(url, new BaseCallback<Page<Wares>>() {


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
            public void onSuccess(Response response, Page<Wares> waresPage) {

                currPage = waresPage.getCurrentPage();
                totalPage = waresPage.getTotalPage();

                showWaresData(waresPage.getList());
            }


            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });

    }


    //展示我们的数据
    private void showWaresData(List<Wares> wares){

        switch (state) {

            case STATE_NORMAL:

                if (mWaresAdapter == null){
                mWaresAdapter = new WaresAdapter(getContext(),wares);

                mRecyclerviewWares.setAdapter(mWaresAdapter);

                mRecyclerviewWares.setLayoutManager(new GridLayoutManager(getContext(),2));//网格布局GridLayoutManager
                mRecyclerviewWares.setItemAnimator(new DefaultItemAnimator()); //设置动画
                mRecyclerviewWares.addItemDecoration(new DividerGridItemDecoration(getContext()));
                }
                else{
                    mWaresAdapter.clear();//清空数据
                    mWaresAdapter.addData(wares);

                }


                break;

            case STATE_REFREH:
                mWaresAdapter.clear();
                mWaresAdapter.addData(wares);

                mRecyclerviewWares.scrollToPosition(0);//滚动到第一个位置
                mRefreshLayout.finishRefresh();
                break;

            case STATE_MORE:
                mWaresAdapter.addData(mWaresAdapter.getDatas().size(),wares);
                mRecyclerView.scrollToPosition(mWaresAdapter.getDatas().size());
                mRefreshLayout.finishRefreshLoadMore();
                break;


        }

    }
}
