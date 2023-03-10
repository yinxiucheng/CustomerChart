package com.yxc.customerchart

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import butterknife.Bind
import butterknife.ButterKnife
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.BarLineChartTouchListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.Utils
import com.yxc.customerchart.api.ConstantTest
import com.yxc.customerchart.bean.DataParse
import com.yxc.customerchart.bean.KLineBean
import com.yxc.customerchart.mychart.CoupleChartGestureListener
import com.yxc.customerchart.mychart.MyYAxis
import com.yxc.customerchart.rxutils.MyUtils
import com.yxc.customerchart.rxutils.VolFormatter
import org.json.JSONException
import org.json.JSONObject

class KLineActivity : BaseActivity() {
    @Bind(R.id.combinedchart)
    var combinedchart: CombinedChart? = null

    @Bind(R.id.barchart)
    var barChart: BarChart? = null
    private var mData: DataParse? = null
    private var kLineDatas: ArrayList<KLineBean>? = null
    var xAxisBar: XAxis? = null
    var xAxisK: XAxis? = null
    var axisLeftBar: YAxis? = null
    var axisLeftK: YAxis? = null
    var axisRightBar: YAxis? = null
    var axisRightK: YAxis? = null
    var barDataSet: BarDataSet? = null
    private val mChartTouchListener: BarLineChartTouchListener? = null
    private val coupleChartGestureListener: CoupleChartGestureListener? = null
    var sum = 0f
    private val handler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            barChart!!.isAutoScaleMinMaxEnabled = true
            combinedchart!!.isAutoScaleMinMaxEnabled = true
            combinedchart!!.notifyDataSetChanged()
            barChart!!.notifyDataSetChanged()
            combinedchart!!.invalidate()
            barChart!!.invalidate()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kline)
        ButterKnife.bind(this)
        combinedchart = findViewById(R.id.combinedchart)
        barChart = findViewById(R.id.barchart)
        initChart()
        offLineData
    }

    /*??????????????????????????????*/
    private val offLineData: Unit
        private get() {
            /*??????????????????????????????*/
            mData = DataParse()
            var `object`: JSONObject? = null
            try {
                `object` = JSONObject(ConstantTest.KLINEURL)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            mData!!.parseKLine(`object`)
            mData!!.kLineDatas
            setData(mData!!)
        }

    private fun initChart() {
        barChart!!.setDrawBorders(true)
        barChart!!.setBorderWidth(1f)
        barChart!!.setBorderColor(resources.getColor(R.color.minute_grayLine))
        val description = Description()
        description.text = ""
        barChart!!.description = description
        barChart!!.isDragEnabled = true
        barChart!!.isScaleYEnabled = false
        val barChartLegend = barChart!!.legend
        barChartLegend.isEnabled = false

        //BarYAxisFormatter  barYAxisFormatter=new BarYAxisFormatter();
        //bar x y???
        xAxisBar = barChart!!.xAxis
        xAxisBar?.apply {
            setDrawLabels(true)
            setDrawGridLines(false)
            setDrawAxisLine(false)
            setTextColor(resources.getColor(R.color.minute_zhoutv))
            setPosition(XAxis.XAxisPosition.BOTTOM)
            setGridColor(resources.getColor(R.color.minute_grayLine))
        }
        axisLeftBar = barChart!!.axisLeft
        axisLeftBar!!.setAxisMinValue(0f)
        axisLeftBar!!.setDrawGridLines(false)
        axisLeftBar!!.setDrawAxisLine(false)
        axisLeftBar!!.textColor = resources.getColor(R.color.minute_zhoutv)
        axisLeftBar!!.setDrawLabels(true)
        axisLeftBar!!.spaceTop = 0f
//        axisLeftBar!!.setShowOnlyMinMax(true)
        axisRightBar = barChart!!.axisRight
        axisRightBar!!.setDrawLabels(false)
        axisRightBar!!.setDrawGridLines(false)
        axisRightBar!!.setDrawAxisLine(false)
        /** */
        combinedchart!!.setDrawBorders(true)
        combinedchart!!.setBorderWidth(1f)
        combinedchart!!.setBorderColor(resources.getColor(R.color.minute_grayLine))
        combinedchart!!.description = Description().apply { text = "" }
        combinedchart!!.isDragEnabled = true
        combinedchart!!.isScaleYEnabled = false
        val combinedchartLegend = combinedchart!!.legend
        combinedchartLegend.isEnabled = false
        //bar x y???
        xAxisK = combinedchart!!.xAxis
        xAxisK?.apply {
            setDrawLabels(true)
            setDrawGridLines(false)
            setDrawAxisLine(false)
            setTextColor(resources.getColor(R.color.minute_zhoutv))
            setPosition(XAxis.XAxisPosition.BOTTOM)
            setGridColor(resources.getColor(R.color.minute_grayLine))
        }
        axisLeftK = combinedchart!!.axisLeft
        axisLeftK!!.setDrawGridLines(true)
        axisLeftK!!.setDrawAxisLine(false)
        axisLeftK!!.setDrawLabels(true)
        axisLeftK!!.textColor = resources.getColor(R.color.minute_zhoutv)
        axisLeftK!!.gridColor = resources.getColor(R.color.minute_grayLine)
        axisLeftK!!.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        axisRightK = combinedchart!!.axisRight
        axisRightK!!.setDrawLabels(false)
        axisRightK!!.setDrawGridLines(true)
        axisRightK!!.setDrawAxisLine(false)
        axisRightK!!.gridColor = resources.getColor(R.color.minute_grayLine)
        combinedchart!!.isDragDecelerationEnabled = true
        barChart!!.isDragDecelerationEnabled = true
        combinedchart!!.dragDecelerationFrictionCoef = 0.2f
        barChart!!.dragDecelerationFrictionCoef = 0.2f


        // ???K?????????????????????????????????????????????
        combinedchart!!.onChartGestureListener =
            CoupleChartGestureListener(combinedchart, arrayOf<Chart<*>?>(barChart))
        // ??????????????????????????????????????????K?????????
        barChart!!.onChartGestureListener =
            CoupleChartGestureListener(barChart, arrayOf<Chart<*>?>(combinedchart))
        barChart!!.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(entry: Entry, h: Highlight) {
                Log.e("MPChart", "${h.x}" )
                combinedchart!!.highlightValues(arrayOf(h))
            }

            override fun onNothingSelected() {
                combinedchart!!.highlightValue(null)
            }
        })
        combinedchart!!.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(entry: Entry, h: Highlight) {
                barChart!!.highlightValues(arrayOf(h))
            }

            override fun onNothingSelected() {
                barChart!!.highlightValue(null)
            }
        })
    }

    private fun getSum(a: Int, b: Int): Float {
        for (i in a..b) {
            sum += mData!!.kLineDatas[i].close
        }
        return sum
    }

    private fun culcMaxscale(count: Float): Float {
        var max = 1f
        max = count / 127 * 5
        return max
    }

    private fun setData(mData: DataParse) {
        kLineDatas = mData.kLineDatas
        val size = kLineDatas?.size?:0 //????????????
        // axisLeftBar.setAxisMaxValue(mData.getVolmax());
        val unit = MyUtils.getVolUnit(mData.volmax)
        var u = 1
        if ("??????" == unit) {
            u = 4
        } else if ("??????" == unit) {
            u = 8
        }
        axisLeftBar!!.valueFormatter = VolFormatter(Math.pow(10.0, u.toDouble()).toInt())
        // axisRightBar.setAxisMaxValue(mData.getVolmax());
        Log.e("@@@", mData.volmax.toString() + "da")
        val xVals = ArrayList<String>()
        val barEntries = ArrayList<BarEntry>()
        val candleEntries = ArrayList<CandleEntry>()
        val line5Entries = ArrayList<Entry>()
        val line10Entries = ArrayList<Entry>()
        val line30Entries = ArrayList<Entry>()
        var i = 0
        var j = 0
        while (i < mData.kLineDatas.size) {
            xVals.add(mData.kLineDatas[i].date + "")
            barEntries.add(BarEntry(mData.kLineDatas[i].vol, i.toFloat()))
            candleEntries.add(
                CandleEntry(
                    i.toFloat(),
                    mData.kLineDatas[i].high,
                    mData.kLineDatas[i].low,
                    mData.kLineDatas[i].open,
                    mData.kLineDatas[i].close
                )
            )
            if (i >= 4) {
                sum = 0f
                line5Entries.add(Entry(getSum(i - 4, i) / 5, i.toFloat()))
            }
            if (i >= 9) {
                sum = 0f
                line10Entries.add(Entry(getSum(i - 9, i) / 10, i.toFloat()))
            }
            if (i >= 29) {
                sum = 0f
                line30Entries.add(Entry(getSum(i - 29, i) / 30, i.toFloat()))
            }
            i++
            j++
        }
        barDataSet = BarDataSet(barEntries, "?????????")
        barDataSet?.apply {
            barBorderWidth = 0.5f //bar??????
            isHighlightEnabled = true
            highLightAlpha = 255
            highLightColor = Color.WHITE
            setDrawValues(false)
            color = Color.RED
        }
        val barData = BarData(barDataSet)
        barChart!!.data = barData
        val viewPortHandlerBar = barChart!!.viewPortHandler
        viewPortHandlerBar.setMaximumScaleX(culcMaxscale(xVals.size.toFloat()))
        val touchmatrix = viewPortHandlerBar.matrixTouch
        val xscale = 3f
        touchmatrix.postScale(xscale, 1f)
        val candleDataSet = CandleDataSet(candleEntries, "KLine")
        candleDataSet.setDrawHorizontalHighlightIndicator(false)
        candleDataSet.isHighlightEnabled = true
        candleDataSet.highLightColor = Color.WHITE
        candleDataSet.valueTextSize = 10f
        candleDataSet.setDrawValues(false)
        candleDataSet.color = Color.RED
        candleDataSet.shadowWidth = 1f
        candleDataSet.axisDependency = YAxis.AxisDependency.LEFT
        val candleData = CandleData(candleDataSet)
        val sets = ArrayList<ILineDataSet>()
        /******????????????????????????????????????????????????MA?????????????????????????????????0?????????????????????????????? */
        if (size >= 30) {
            sets.add(setMaLine(5, xVals, line5Entries))
            sets.add(setMaLine(10, xVals, line10Entries))
            sets.add(setMaLine(30, xVals, line30Entries))
        } else if (size >= 10 && size < 30) {
            sets.add(setMaLine(5, xVals, line5Entries))
            sets.add(setMaLine(10, xVals, line10Entries))
        } else if (size >= 5 && size < 10) {
            sets.add(setMaLine(5, xVals, line5Entries))
        }
        val combinedData = CombinedData()
        val lineData = LineData(sets)
        combinedData.setData(candleData)
        combinedData.setData(lineData)
        combinedchart!!.data = combinedData
        combinedchart!!.moveViewToX((mData.kLineDatas.size - 1).toFloat())
        val viewPortHandlerCombin = combinedchart!!.viewPortHandler
        viewPortHandlerCombin.setMaximumScaleX(culcMaxscale(xVals.size.toFloat()))
        val matrixCombin = viewPortHandlerCombin.matrixTouch
        val xscaleCombin = 3f
        matrixCombin.postScale(xscaleCombin, 1f)
        combinedchart!!.moveViewToX((mData.kLineDatas.size - 1).toFloat())
        barChart!!.moveViewToX((mData.kLineDatas.size - 1).toFloat())
        setOffset()
        /****************************************************************************************
         * ???????????????????????????CombinedChartDemo???k??????y????????????????????????????????????????????????bug?????????????????????????????????
         * (?????????bug????????????????????????chenguang79????????????)
         */
        handler.sendEmptyMessageDelayed(0, 300)
    }

    private fun setMaLine(
        ma: Int,
        xVals: ArrayList<String>,
        lineEntries: ArrayList<Entry>
    ): LineDataSet {
        val lineDataSetMa = LineDataSet(lineEntries, "ma$ma")
        if (ma == 5) {
            lineDataSetMa.isHighlightEnabled = true
            lineDataSetMa.setDrawHorizontalHighlightIndicator(false)
            lineDataSetMa.highLightColor = Color.WHITE
        } else { /*??????????????????*/
            lineDataSetMa.isHighlightEnabled = false
        }
        lineDataSetMa.setDrawValues(false)
        if (ma == 5) {
            lineDataSetMa.color = Color.GREEN
        } else if (ma == 10) {
            lineDataSetMa.color = Color.GRAY
        } else {
            lineDataSetMa.color = Color.YELLOW
        }
        lineDataSetMa.lineWidth = 1f
        lineDataSetMa.setDrawCircles(false)
        lineDataSetMa.axisDependency = YAxis.AxisDependency.LEFT
        return lineDataSetMa
    }

    /*??????????????????*/
    private fun setOffset() {
        val lineLeft = combinedchart!!.viewPortHandler.offsetLeft()
        val barLeft = barChart!!.viewPortHandler.offsetLeft()
        val lineRight = combinedchart!!.viewPortHandler.offsetRight()
        val barRight = barChart!!.viewPortHandler.offsetRight()
        val barBottom = barChart!!.viewPortHandler.offsetBottom()
        val offsetLeft: Float
        val offsetRight: Float
        var transLeft = 0f
        var transRight = 0f
        /*??????setExtraLeft...????????????????????????????????????????????????A???offLeftA=20dp,B???offLeftB=30dp,???A.setExtraLeftOffset(10),?????????30???????????????????????????*/if (barLeft < lineLeft) {
            /* offsetLeft = Utils.convertPixelsToDp(lineLeft - barLeft);
            barChart.setExtraLeftOffset(offsetLeft);*/
            transLeft = lineLeft
        } else {
            offsetLeft = Utils.convertPixelsToDp(barLeft - lineLeft)
            combinedchart!!.extraLeftOffset = offsetLeft
            transLeft = barLeft
        }
        /*??????setExtraRight...????????????????????????????????????????????????A???offRightA=20dp,B???offRightB=30dp,???A.setExtraLeftOffset(30),?????????10???????????????????????????*/if (barRight < lineRight) {
            /*  offsetRight = Utils.convertPixelsToDp(lineRight);
            barChart.setExtraRightOffset(offsetRight);*/
            transRight = lineRight
        } else {
            offsetRight = Utils.convertPixelsToDp(barRight)
            combinedchart!!.extraRightOffset = offsetRight
            transRight = barRight
        }
        barChart!!.setViewPortOffsets(transLeft, 15f, transRight, barBottom)
    }
}