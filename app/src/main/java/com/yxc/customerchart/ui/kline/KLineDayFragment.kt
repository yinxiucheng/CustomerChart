package com.yxc.customerchart.ui.kline

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.data.BarEntry
import com.github.wangyiqian.stockchart.entities.IKEntity
import com.yxc.chartlib.attrs.StockChartAttrs
import com.yxc.chartlib.barchart.BarChartAdapter
import com.yxc.chartlib.barchart.SpeedRatioLayoutManager
import com.yxc.chartlib.barchart.itemdecoration.StockChartItemDecoration
import com.yxc.chartlib.component.XAxis
import com.yxc.chartlib.component.YAxis
import com.yxc.chartlib.entrys.StockEntry
import com.yxc.chartlib.formatter.StockValueFormatter
import com.yxc.chartlib.formatter.ValueFormatter
import com.yxc.chartlib.listener.RecyclerItemGestureListener
import com.yxc.chartlib.listener.SimpleItemGestureListener
import com.yxc.chartlib.utils.AppUtil
import com.yxc.chartlib.view.StockChartRecyclerView
import com.yxc.customerchart.R
import com.yxc.customerchart.mock.DataMock
import com.yxc.customerchart.ui.ecg.TestData
import com.yxc.customerchart.ui.line.BaseLineFragment
import com.yxc.fitness.chart.entrys.RecyclerBarEntry

class KLineDayFragment : BaseLineFragment() {
    val TAG = "KLineDayFragment"
    lateinit var recyclerView: StockChartRecyclerView
    lateinit var mBarChartAdapter: BarChartAdapter<StockEntry>
    val mEntries: MutableList<StockEntry> = mutableListOf()
    var mItemDecoration: StockChartItemDecoration? = null
    lateinit var mItemGestureListener: RecyclerItemGestureListener<StockEntry>
    lateinit var mYAxis: YAxis
    lateinit var mXAxis: XAxis
    lateinit var valueFormatter: ValueFormatter
    var mType = 0
    private var displayNumber = 0
    lateinit private var mBarChartAttrs: StockChartAttrs
    lateinit var mContext: Context
    private var currentPage:Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = View.inflate(activity, R.layout.fragment_day_kline, null)
        mContext = activity?: AppUtil.app
        initView(view)
        initData()
        setListener()
        return view
    }

    private fun initView(view: View) {
        recyclerView = view.findViewById(R.id.line_chart)
        mBarChartAttrs = recyclerView.mAttrs
    }

    private fun initData() {
        displayNumber = mBarChartAttrs.displayNumbers
        mType = TestData.VIEW_DAY
        valueFormatter = StockValueFormatter()
        val layoutManager = SpeedRatioLayoutManager(activity, mBarChartAttrs)
        mYAxis = YAxis(mBarChartAttrs)
        mXAxis = XAxis(mBarChartAttrs, displayNumber, valueFormatter)
        mItemDecoration = StockChartItemDecoration(mYAxis, mXAxis, mBarChartAttrs)
        recyclerView.addItemDecoration(mItemDecoration!!)
        mBarChartAdapter = BarChartAdapter(activity, mEntries, recyclerView, mXAxis, mBarChartAttrs)
        recyclerView.adapter = mBarChartAdapter
        recyclerView.layoutManager = layoutManager

        DataMock.loadDayData(mContext, currentPage) { entityList ->
            bindBarChartList(createStockEntryList(entityList))
        }
        setXAxis(displayNumber)
        reSizeYAxis()
    }

    private fun createStockEntryList(kEntityList:List<IKEntity>): List<StockEntry>{
        val stockEntryList = mutableListOf<StockEntry>()
        var index = mEntries.size
        kEntityList.map { entity ->
//          (x:Float, time:Long, shadowH: Float, shadowL:Float, open:Float, close:Float)
            val stockEntry = StockEntry((index++).toFloat(), entity.getTime()/1000,
                entity.getHighPrice(), entity.getLowPrice(), entity.getOpenPrice(), entity.getClosePrice())
            stockEntryList.add(stockEntry)
        }
        return stockEntryList
    }

    private fun reSizeYAxis() {
        recyclerView.scrollToPosition(0)
        val yAxis = mYAxis.resetEcgYAxis(mYAxis)
        mBarChartAdapter.notifyDataSetChanged()
        if (yAxis != null) {
            mYAxis = yAxis
            mItemDecoration!!.setYAxis(mYAxis)
            mBarChartAdapter.setYAxis(mYAxis)
        }
        mBarChartAdapter.setYAxis(mYAxis)
    }

    //滑动监听
    private fun setListener() {
        mItemGestureListener = RecyclerItemGestureListener<RecyclerBarEntry>(
            activity, recyclerView,
            object : SimpleItemGestureListener() {
                var isRightScrollInner = false
                override fun onItemSelected(barEntry: BarEntry, position: Int) {}
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    // 当不滚动时
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (!recyclerView.canScrollHorizontally(-1) && isRightScrollInner) {
                            Log.d(TAG, " can't Scroll left ! entry size:" + mEntries.size)
                            DataMock.loadDayData(mContext, currentPage++){ entityList ->
                                mEntries.addAll(createStockEntryList(entityList))
                                mBarChartAdapter.notifyDataSetChanged()
                            }
                        } else if (!recyclerView.canScrollHorizontally(1)) {
//                                List<StockEntry> barEntries = EcgTestData.createEcgEntries(
//                                        currentTimestamp,
//                                        displayNumber,
//                                        mEntries.size(),
//                                        EcgTestData.testDoubleArray
//                                );
                            // mEntries.addAll(barEntries);

                        }
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    isRightScrollInner = dx < 0
                }
            })
        recyclerView.addOnItemTouchListener(mItemGestureListener)
    }

    private fun bindBarChartList(entries: List<StockEntry>) {
        mEntries.clear()
        mEntries.addAll(entries)
    }

    private fun setXAxis(displayNumber: Int) {
        mXAxis = XAxis(mBarChartAttrs, displayNumber)
        mBarChartAdapter.setXAxis(mXAxis)
    }

    override fun resetSelectedEntry() {
        if (mItemGestureListener != null) {
            Log.d("DayFragment", " visibleHint")
            mItemGestureListener.resetSelectedBarEntry()
        }
    }

    override fun displayDateAndRate() {}
    override fun scrollToCurrentCycle() {}
}