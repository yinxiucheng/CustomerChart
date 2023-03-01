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
import com.yxc.chartlib.component.StockYAxis
import com.yxc.chartlib.component.XAxis
import com.yxc.chartlib.entrys.StockEntry
import com.yxc.chartlib.formatter.StockValueFormatter
import com.yxc.chartlib.formatter.ValueFormatter
import com.yxc.chartlib.listener.RecyclerItemGestureListener
import com.yxc.chartlib.listener.SimpleItemGestureListener
import com.yxc.chartlib.util.ChartComputeUtil
import com.yxc.chartlib.utils.AppUtil
import com.yxc.chartlib.view.StockChartRecyclerView
import com.yxc.customerchart.R
import com.yxc.customerchart.mock.DataMock
import com.yxc.customerchart.ui.ecg.TestData
import com.yxc.customerchart.ui.line.BaseLineFragment
import com.yxc.customerchart.ui.valueformatter.StockKLineXAxisFormatter
import com.yxc.fitness.chart.entrys.RecyclerBarEntry
import com.yxc.mylibrary.TimeDateUtil

class KLineDayFragment : BaseLineFragment() {
    val TAG = "KLineDayFragment"
    lateinit var recyclerView: StockChartRecyclerView
    lateinit var mBarChartAdapter: BarChartAdapter<StockEntry>
    val mEntries: MutableList<StockEntry> = mutableListOf()
    lateinit var mItemDecoration: StockChartItemDecoration
    lateinit var mItemGestureListener: RecyclerItemGestureListener<StockEntry>
    lateinit var mYAxis: StockYAxis
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
        recyclerView = view.findViewById(R.id.kline_day_chart)
        mBarChartAttrs = recyclerView.mAttrs
    }

    private fun initData() {
        displayNumber = mBarChartAttrs.displayNumbers
        mType = TestData.VIEW_DAY
        valueFormatter = StockKLineXAxisFormatter()
        val layoutManager = SpeedRatioLayoutManager(activity, mBarChartAttrs)
        mYAxis = StockYAxis(mBarChartAttrs)
        mXAxis = XAxis(mBarChartAttrs, displayNumber, valueFormatter)
        mItemDecoration = StockChartItemDecoration(mYAxis, mXAxis, mBarChartAttrs)
        recyclerView.addItemDecoration(mItemDecoration)
        mBarChartAdapter = BarChartAdapter(activity, mEntries, recyclerView, mXAxis, mBarChartAttrs)
        recyclerView.adapter = mBarChartAdapter
        recyclerView.layoutManager = layoutManager

        DataMock.loadDayData(mContext, currentPage) { entityList ->
            bindBarChartList(createStockEntryList(entityList))
            setXAxis(displayNumber)
            reSizeYAxis()
        }
    }

    protected fun reSizeYAxis() {
        if (mEntries.size == 0) {
            return
        }
        val visibleSize = Math.min(displayNumber, mEntries.size)
        recyclerView.scrollToPosition(0)
        val visibleEntries: List<StockEntry> = mEntries.subList(0, visibleSize)

        val maxMinModel = StockEntry.getTheMaxMinModel(visibleEntries)
        mYAxis = StockYAxis.createYAxisWithLabelCount(mBarChartAttrs, maxMinModel.max, maxMinModel.min, 4)
        mItemDecoration.setYAxis(mYAxis)
        mBarChartAdapter.setYAxis(mYAxis)
        mBarChartAdapter.notifyDataSetChanged()
        setVisibleEntries(visibleEntries)
    }

    protected fun resetYAxis(recyclerView: RecyclerView) {
        val visibleEntries: List<StockEntry> = ChartComputeUtil.getVisibleEntriesJust(recyclerView, displayNumber)
        val maxMinModel = StockEntry.getTheMaxMinModel(visibleEntries)
        setVisibleEntries(visibleEntries)
        mYAxis = StockYAxis.resetStockYAxis(mYAxis, maxMinModel.max, maxMinModel.min, 4)
        mItemDecoration.setYAxis(mYAxis)
    }

    private fun createStockEntryList(kEntityList:List<IKEntity>): List<StockEntry>{
        val stockEntryList = mutableListOf<StockEntry>()
        var index = mEntries.size
        var preEntry = if (mEntries.isNotEmpty()) mEntries[mEntries.size - 1] else null
        kEntityList.map { entity ->
//          (x:Float, time:Long, shadowH: Float, shadowL:Float, open:Float, close:Float)
            val stockEntry = StockEntry((index++).toFloat(), entity.getTime()/1000, entity.getHighPrice(),
                entity.getLowPrice(), entity.getOpenPrice(), entity.getClosePrice())
            preEntry?.let {
                stockEntry.isRise = it.mClose < stockEntry.mClose
                val lastDate = TimeDateUtil.timestampToLocalDate(it.timestamp)
                val thisDate = TimeDateUtil.timestampToLocalDate(stockEntry.timestamp)
                if (!TimeDateUtil.isSameMonth(lastDate, thisDate)){
                    stockEntry.type = RecyclerBarEntry.TYPE_XAXIS_FIRST
                }
            }
            preEntry = stockEntry
            stockEntryList.add(stockEntry)
        }
        return stockEntryList
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
                        }
                    }
                    resetYAxis(recyclerView)
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    isRightScrollInner = dx < 0
                    resetYAxis(recyclerView)
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