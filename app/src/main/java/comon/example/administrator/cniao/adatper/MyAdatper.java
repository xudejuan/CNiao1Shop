package comon.example.administrator.cniao.adatper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.List;

import comon.example.administrator.cniao.R;

/**
 * Created by Administrator on 2017/12/22.
 */

public class MyAdatper extends RecyclerView.Adapter<MyAdatper.ViewHolder> {

    private List<String> mDatas;

    private LayoutInflater inflater;

    private OnItemClickListener listener;

    public MyAdatper(List<String> datas){
        mDatas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = listener;
    }

    public void addData(int position, String city) {
        mDatas.add(position,city);

//        notifyItemChanged(position);

        notifyItemInserted(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.text);
        }
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener){

        this.listener = (OnItemClickListener) listener;
    }

    public interface  OnItemClickListener{


        void onClick(View v, int position, String city);


    }


}
