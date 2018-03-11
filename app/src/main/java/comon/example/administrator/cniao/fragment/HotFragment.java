package comon.example.administrator.cniao.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

import comon.example.administrator.cniao.Contants;
import comon.example.administrator.cniao.R;
import comon.example.administrator.cniao.WareListActivity;
import comon.example.administrator.cniao.adatper.BaseAdapter;
import comon.example.administrator.cniao.adatper.HWAdatper;
import comon.example.administrator.cniao.bean.Page;
import comon.example.administrator.cniao.bean.Wares;
import comon.example.administrator.cniao.utils.Pager;

/**
 * Created by Administrator on 2017/12/17.
 */

public class HotFragment extends BaseFragment implements Pager.OnPageListener<Wares>{




    private HWAdatper mAdatper;

    @ViewInject(R.id.recyclerview)//实例化
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.refresh_view)//实例化
    private MaterialRefreshLayout mRefreshLayout;


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hot,container,false);
    }

    @Override
    public void init() {

        Pager pager = Pager.newBuilder()
                .setUrl(Contants.API.WARES_HOT)
                .setLoadMore(true)
                .setOnPageListener(this)
                .setPageSize(20)
                .setRefreshLayout(mRefreshLayout)
                .build(getContext(), new TypeToken<Page<Wares>>() {}.getType());


        pager.request();

    }


    @Override
    public void load(List<Wares> datas, int totalPage, int totalCount) {

        mAdatper = new HWAdatper(getContext(),datas);

        mAdatper.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Wares wares = mAdatper.getItem(position);

                Intent intent = new Intent(getActivity(), WareListActivity.class);

                intent.putExtra(Contants.WARE,wares);
                startActivity(intent);//starActivity传过去到WareDetailActivity
            }
        });


        mRecyclerView.setAdapter(mAdatper);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public void refresh(List<Wares> datas, int totalPage, int totalCount) {
        mAdatper.refreshData(datas);

        mRecyclerView.scrollToPosition(0);
    }

    @Override
    public void loadMore(List<Wares> datas, int totalPage, int totalCount) {

        mAdatper.loadMoreData(datas);
        mRecyclerView.scrollToPosition(mAdatper.getDatas().size());
    }


//
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_hot, container, false);
//
//
//        ViewUtils.inject(this,view);
//
//
//
//        Pager pager = Pager.newBuilder().setUrl(Contants.API.WARES_HOT)
//                .setLoadMore(true)
//                .setOnPageListener(new Pager.OnPageListener() {
//            @Override
//            public void load(List datas, int totalPage, int totalCount) {
//
//                mAdatper = new HWAdatper(getContext(),datas);
//
//                mRecyclerView.setAdapter(mAdatper);
//
//                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                //设置动画
//                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//                mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
//            }
//
//            @Override
//            public void refresh(List datas, int totalPage, int totalCount) {
//
//                mAdatper.clear();
//                mAdatper.addData(datas);
//
//                mRecyclerView.scrollToPosition(0);//滚动到第一个位置
//
//            }
//
//            @Override
//            public void loadMore(List datas, int totalPage, int totalCount) {
//
//                mAdatper.addData(mAdatper.getDatas().size(),datas);
//                mRecyclerView.scrollToPosition(mAdatper.getDatas().size());
//
//            }
//        }).setPageSize(20)
//                .setRefreshLayout(mRefreshLayout).build(getContext(), new TypeToken<Page<Wares>>(){}.getType());
//
//        pager.request();
//        return view;
//    }
//
//
//    @Override
//    public void load(List<Wares> datas, int totalPage, int totalCount) {
//
//    }
//
//    @Override
//    public void refresh(List<Wares> datas, int totalPage, int totalCount) {
//
//    }
//
//    @Override
//    public void loadMore(List<Wares> datas, int totalPage, int totalCount) {
//
//    }
}
