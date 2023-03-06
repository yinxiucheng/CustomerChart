package com.yxc.customerchart.ui.kline;

/**
 * @author yxc
 * @since 2019-07-16
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.yxc.customerchart.R;


public class SharePopupWindow extends PopupWindow{

    private View mView;
    public RelativeLayout mRlWXSession, mRlWXTimeLine, mRlDownload;


    public SharePopupWindow(Context context, OnClickListener itemsOnClick) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.share_path_popup_window, null);

        mRlWXSession = mView.findViewById(R.id.rl_share_wx_session);
        mRlWXTimeLine = mView.findViewById(R.id.rl_share_timeline);
        mRlDownload = mView.findViewById(R.id.rl_download);

        mRlWXSession.setOnClickListener(itemsOnClick);
        mRlWXTimeLine.setOnClickListener(itemsOnClick);
        mRlDownload.setOnClickListener(itemsOnClick);

        //设置PopupWindow的View
        this.setContentView(mView);
        //设置PopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置PopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置PopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupAnimation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }
}
