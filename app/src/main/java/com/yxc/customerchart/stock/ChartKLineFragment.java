package com.yxc.customerchart.stock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import com.yxc.chartlib.stockchart.KLineChart;
import com.yxc.chartlib.stockchart.charts.CoupleChartGestureListener;
import com.yxc.chartlib.stockchart.datamanage.KLineDataManage;
import com.yxc.customerchart.R;
import com.yxc.customerchart.base.BaseFragment;
import com.yxc.customerchart.constant.ChartData;
import com.yxc.customerchart.databinding.FragmentKlineBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class ChartKLineFragment extends BaseFragment {

    FragmentKlineBinding mBinding;
    KLineChart combinedchart;

    private int mType;//日K：1；周K：7；月K：30
    private boolean land;//是否横屏
    private KLineDataManage kLineData;
    private JSONObject object;
    private int indexType = 1;

    public static ChartKLineFragment newInstance(int type,boolean land){
        ChartKLineFragment fragment = new ChartKLineFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putBoolean("landscape",land);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_kline;
    }

    @Override
    protected void initBase(View view) {
        kLineData = new KLineDataManage(getActivity());
        combinedchart.initChart(land);
        try {
            if(mType == 1){
                object = new JSONObject(ChartData.KLINEDATA);
            }else if(mType == 7){
                object = new JSONObject(ChartData.KLINEWEEKDATA);
            }else if(mType == 30){
                object = new JSONObject(ChartData.KLINEMONTHDATA);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //上证指数代码000001.IDX.SH
        kLineData.parseKlineData(object,"000001.IDX.SH",land);
        combinedchart.setDataToChart(kLineData);

        combinedchart.getGestureListenerCandle().setCoupleClick(new CoupleChartGestureListener.CoupleClick() {
            @Override
            public void singleClickListener() {
                if(!land) {
//                    Intent intent = new Intent(getActivity(), StockDetailLandActivity.class);
//                    getActivity().startActivity(intent);
                }
            }
        });

        combinedchart.getGestureListenerBar().setCoupleClick(new CoupleChartGestureListener.CoupleClick() {
            @Override
            public void singleClickListener() {
                if(land) {
                    loadIndexData(indexType < 5 ? ++indexType : 1);
                }else {
//                    Intent intent = new Intent(getActivity(), StockDetailLandActivity.class);
//                    getActivity().startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getInt("type");
        land = getArguments().getBoolean("landscape");
    }

    private void loadIndexData(int type) {
        indexType = type;
        switch (indexType) {
            case 1://成交量
                combinedchart.doBarChartSwitch(indexType);
                break;
            case 2://请求MACD
                kLineData.initMACD();
                combinedchart.doBarChartSwitch(indexType);
                break;
            case 3://请求KDJ
                kLineData.initKDJ();
                combinedchart.doBarChartSwitch(indexType);
                break;
            case 4://请求BOLL
                kLineData.initBOLL();
                combinedchart.doBarChartSwitch(indexType);
                break;
            case 5://请求RSI
                kLineData.initRSI();
                combinedchart.doBarChartSwitch(indexType);
                break;
            default:
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentKlineBinding.inflate(getLayoutInflater());
        combinedchart = mBinding.combinedChart;
        return mBinding.getRoot();
    }
}
