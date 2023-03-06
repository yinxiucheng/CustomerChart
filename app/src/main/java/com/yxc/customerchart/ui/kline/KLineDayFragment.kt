package com.yxc.customerchart.ui.kline

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yxc.chartlib.attrs.StockChartAttrs
import com.yxc.chartlib.barchart.BarChartAdapter
import com.yxc.chartlib.barchart.SpeedRatioLayoutManager
import com.yxc.chartlib.barchart.itemdecoration.StockChartItemDecoration
import com.yxc.chartlib.component.StockYAxis
import com.yxc.chartlib.component.StockYAxis.Companion.createYAxisWithLabelCount
import com.yxc.chartlib.component.StockYAxis.Companion.resetStockYAxis
import com.yxc.chartlib.component.XAxis
import com.yxc.chartlib.entrys.StockEntry
import com.yxc.chartlib.entrys.StockEntry.Companion.getTheMaxMinModel
import com.yxc.chartlib.entrys.StockEntry.Companion.getTheMaxMinModelVolume
import com.yxc.chartlib.formatter.ValueFormatter
import com.yxc.chartlib.listener.RecyclerItemGestureListener
import com.yxc.chartlib.listener.RecyclerStockItemGestureListener
import com.yxc.chartlib.listener.SimpleItemGestureListener
import com.yxc.chartlib.listener.SimpleStockItemGestureListener
import com.yxc.chartlib.util.ChartComputeUtil
import com.yxc.chartlib.utils.AppUtil
import com.yxc.chartlib.utils.DecimalUtil
import com.yxc.chartlib.view.StockChartRecyclerView
import com.yxc.customerchart.R
import com.yxc.customerchart.mock.DataMock
import com.yxc.customerchart.ui.ecg.TestData
import com.yxc.customerchart.ui.line.BaseLineFragment
import com.yxc.customerchart.ui.utils.DataHelper.createStockEntryList
import com.yxc.customerchart.ui.valueformatter.StockKLineXAxisFormatter
import com.yxc.fitness.chart.entrys.RecyclerBarEntry

class KLineDayFragment : BaseLineFragment() {
    val TAG = "KLineDayFragment"
    lateinit var recyclerView: StockChartRecyclerView
    lateinit var mBarChartAdapter: BarChartAdapter<StockEntry>
    val mEntries: MutableList<StockEntry> = mutableListOf()
    lateinit var mItemDecoration: StockChartItemDecoration
    lateinit var mYAxis: StockYAxis
    lateinit var mAttacheYAxis: StockYAxis
    lateinit var mXAxis: XAxis
    lateinit var valueFormatter: ValueFormatter
    var mType = 0
    private var displayNumber = 0
    lateinit private var mBarChartAttrs: StockChartAttrs
    lateinit var mContext: Context
    private var currentPage:Int = 0
    private val stockItemGestureListener:SimpleStockItemGestureListener by lazy { MySimpleStockItemGestureListener() }

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
        recyclerView = view.findViewById(R.id.kline_day_chart)
        mBarChartAttrs = recyclerView.mAttrs
    }

    private fun initData() {
        displayNumber = mBarChartAttrs.displayNumbers
        mType = TestData.VIEW_DAY
        valueFormatter = StockKLineXAxisFormatter()
        val layoutManager = SpeedRatioLayoutManager(activity, mBarChartAttrs)
        mYAxis = StockYAxis(mBarChartAttrs)
        mAttacheYAxis = StockYAxis(mBarChartAttrs)
        mXAxis = XAxis(mBarChartAttrs, displayNumber, valueFormatter)
        mItemDecoration = StockChartItemDecoration(mYAxis, mXAxis, mBarChartAttrs, mAttacheYAxis)
        recyclerView.addItemDecoration(mItemDecoration)
        mBarChartAdapter = BarChartAdapter(activity, mEntries, recyclerView, mXAxis, mBarChartAttrs)
        recyclerView.adapter = mBarChartAdapter
        recyclerView.layoutManager = layoutManager

        DataMock.loadDayData(mContext, currentPage) { entityList ->
            val windowCountManager = createStockEntryList(entityList, mEntries.size)
            bindBarChartList(windowCountManager.stockEntryList, true)
            setXAxis(displayNumber)
            reSizeYAxis()
        }

        recyclerView.resetDisplayNumber = { displayNumber ->
            this@KLineDayFragment.displayNumber = displayNumber
            mXAxis.resetDisplayNumber(this@KLineDayFragment.displayNumber)
            mBarChartAdapter.updateXAxis(mXAxis)
        }
    }

    private fun reSizeYAxis() {
        if (mEntries.size == 0) {
            return
        }
        val visibleSize = displayNumber.coerceAtMost(mEntries.size)
        recyclerView.scrollToPosition(0)
        val visibleEntries: List<StockEntry> = mEntries.subList(0, visibleSize)
        val maxMinModel = getTheMaxMinModel(visibleEntries)
        mYAxis = createYAxisWithLabelCount(mBarChartAttrs, maxMinModel.max, maxMinModel.min, 4)
        val maxMinModelAttache = getTheMaxMinModelVolume(visibleEntries)
        mAttacheYAxis = createYAxisWithLabelCount(mBarChartAttrs, maxMinModelAttache.max, maxMinModelAttache.min, 4)
        mItemDecoration.setYAxis(mYAxis, mAttacheYAxis)
        mBarChartAdapter.setYAxis(mYAxis)
        setVisibleEntries(visibleEntries)
    }

    private fun resetYAxis(recyclerView: RecyclerView) {
        val visibleEntries: List<StockEntry> = ChartComputeUtil.getVisibleEntriesJust(recyclerView, displayNumber)
        val maxMinModel = getTheMaxMinModel(visibleEntries)
        setVisibleEntries(visibleEntries)
        val maxMinModelAttache = getTheMaxMinModelVolume(visibleEntries)
        mAttacheYAxis = resetStockYAxis(mAttacheYAxis, maxMinModelAttache.max, maxMinModelAttache.min, 2)
        mYAxis = resetStockYAxis(mYAxis, maxMinModel.max, maxMinModel.min, 4)
        mItemDecoration.setYAxis(mYAxis, mAttacheYAxis)
    }

    //滑动监听
    private fun setListener() {
        mItemGestureListener = RecyclerStockItemGestureListener<RecyclerBarEntry>(activity, recyclerView, stockItemGestureListener)
        recyclerView.addOnItemTouchListener(mItemGestureListener)
    }

    private fun bindBarChartList(entries: List<StockEntry>, isFirst: Boolean,
                                 windowCount5:WindowCount? = null,
                                 windowCount10: WindowCount? = null,
    windowCount20:WindowCount? = null) {
        if (isFirst){
            mEntries.clear()
            mEntries.addAll(entries)
        }else{
            val size = mEntries.size
            mEntries.addAll(entries)
            if (size == 0) return
            for (i in size -1 downTo 0){
                val entry = mEntries[i]
                if (!DecimalUtil.equals(entry.ma20, -1f)){
                    break
                }
                windowCount5?.apply { val avg = getAvg(5, entry.mClose)
                if (!DecimalUtil.equals(avg, -1f)){
                    entry.ma5 = avg
                }}
                windowCount10?.apply { val avg = getAvg(10, entry.mClose)
                    if (!DecimalUtil.equals(avg, -1f)){
                        entry.ma10 = avg
                    }}
                windowCount20?.apply { val avg = getAvg(20, entry.mClose)
                    if (!DecimalUtil.equals(avg, -1f)){
                        entry.ma20 = avg
                    }}
            }
        }
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
    private fun showPopupWindow(){}

    inner class MySimpleStockItemGestureListener: SimpleStockItemGestureListener(){
        var isRightScrollInner = false
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            // 当不滚动时
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                if (!recyclerView.canScrollHorizontally(-1) && isRightScrollInner) {
                    Log.d(TAG, " can't Scroll left ! entry size:" + mEntries.size)
                    DataMock.loadDayData(mContext, currentPage++){ entityList ->
                        val windowCountManager = createStockEntryList(entityList, mEntries.size)
                        bindBarChartList(windowCountManager.stockEntryList, false,
                            windowCountManager.windowCount5,
                            windowCountManager.windowCount10,
                            windowCountManager.windowCount20)
                        mBarChartAdapter.notifyDataSetChanged()
                    }
                } else if (!recyclerView.canScrollHorizontally(1)) {
                }
            }
            resetYAxis(recyclerView)
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            isRightScrollInner = dx < 0
            resetYAxis(recyclerView)
        }

        override fun onStockItemBottomClick(view: View) {
            Log.d(TAG, "show hello ketiy")
        }

        override fun showBottomPopWindow() {
            Log.d(TAG, "show hello ketiy2.")
            showPopupWindow()
        }
    }
}