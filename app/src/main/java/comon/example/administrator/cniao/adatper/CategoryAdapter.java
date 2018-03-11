package comon.example.administrator.cniao.adatper;

import android.content.Context;

import java.util.List;

import comon.example.administrator.cniao.R;
import comon.example.administrator.cniao.bean.Category;

/**
 * Created by Administrator on 2018/1/10.
 */

//创建Adapter绑定数据
public class CategoryAdapter extends SimpleAdapter<Category>{
    public CategoryAdapter(Context context,List<Category> datas) {
        super(context, R.layout.template_single_text, datas);
    }

    @Override
    protected void convert(BaseViewHolder viewHoder, Category item) {

        viewHoder.getTextView(R.id.textView).setText(item.getName());

    }
}
