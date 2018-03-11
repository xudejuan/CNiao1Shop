package comon.example.administrator.cniao;

import android.content.Context;
import android.util.AttributeSet;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Administrator on 2018/1/9.
 */

public class MySimpleDraweeView extends SimpleDraweeView {
    public MySimpleDraweeView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public MySimpleDraweeView(Context context) {
        super(context);
    }

    public MySimpleDraweeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySimpleDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
