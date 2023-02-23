package com.yxc.chartlib.render;

import android.graphics.Canvas;
import android.graphics.Path;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xiaomi.fitness.chart.attrs.BarChartAttrs;
import com.xiaomi.fitness.chart.component.BaseYAxis;
import com.xiaomi.fitness.chart.entrys.MultiBarEntry;
import com.xiaomi.fitness.chart.entrys.RecyclerBarEntry;
import com.xiaomi.fitness.chart.formatter.ValueFormatter;
import com.xiaomi.fitness.chart.render.BaseChartRender;
import com.xiaomi.fitness.chart.util.CanvasUtil;
import com.xiaomi.fitness.chart.util.ChartComputeUtil;
import com.xiaomi.fitness.chart.util.RectFWrapper;
import com.xiaomi.fitness.chart.util.RoundRectType;
import com.xiaomi.fitness.common.utils.DecimalUtil;

import java.util.List;

/**
 * 用于听音 以及睡眠鼾声梦话图
 */
public class MultiBarChartDayRender extends BaseChartRender<MultiBarEntry, BarChartAttrs> {

    @Override
    protected <T extends RecyclerBarEntry> int getChartColor(T entry) {
        return mBarChartAttrs.chartColor;
    }

    public MultiBarChartDayRender(BarChartAttrs barChartAttrs, ValueFormatter highLightValueFormatter) {
        super(barChartAttrs, highLightValueFormatter);
    }

    //绘制柱状图, mYAxis这个坐标会实时变动，所以通过 BarChartItemDecoration 传过来的精确值。
    public <T extends BaseYAxis> void drawChart(final Canvas canvas, @NonNull final RecyclerView parent, final T mYAxis) {
        final float parentRight = parent.getWidth() - parent.getPaddingRight();
        final float parentLeft = parent.getPaddingLeft();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            MultiBarEntry barChart = (MultiBarEntry) child.getTag();
            List<MultiBarEntry.MultiItem> multiItems = barChart.getMultiItems();
            List<RectFWrapper> rectFList = ChartComputeUtil.getMultiChartRectFList(child, parent, mYAxis, mBarChartAttrs, multiItems);
            for (int position = 0; position < rectFList.size(); position++) {
                RectFWrapper rectF = rectFList.get(position);
                drawColorChart(canvas, rectF, parentLeft, parentRight, rectF.color);
            }
        }
    }

    protected void drawColorChart(Canvas canvas, RectFWrapper rectF, float parentLeft, float parentRight, int color) {
        float radius = 36;
//        float radius = (rectF.right - rectF.left) * mBarChartAttrs.barChartRoundRectRadiusRatio;
        // 浮点数的 == 比较需要注意
        if (DecimalUtil.smallOrEquals(rectF.right, parentLeft)) {
            //continue 会闪，原因是end == parentLeft 没有过滤掉，显示出来柱状图了。
        } else if (rectF.left < parentLeft && rectF.right > parentLeft) {
            //左边部分滑入的时候，处理柱状图的显示
            rectF.left = parentLeft;
            Path path = CanvasUtil.createRectRoundPath(rectF, radius, RoundRectType.TYPE_RIGHT_TOP);
//            if (rectF.isTop) {
//                path = CanvasUtil.createRectRoundPath(rectF, radius, RoundRectType.TYPE_RIGHT_TOP);
//            } else {
//                path = CanvasUtil.createRectPath(rectF);
//            }
            mBarChartPaint.setColor(mBarChartAttrs.chartEdgeColor);
            canvas.drawPath(path, mBarChartPaint);
        } else if (DecimalUtil.bigOrEquals(rectF.left, parentLeft) && DecimalUtil.smallOrEquals(rectF.right, parentRight)) {
            //中间的; 浮点数的 == 比较需要注意
            mBarChartPaint.setColor(color);
            Path path = CanvasUtil.createRectRoundPath(rectF, radius);
//            if (rectF.isTop) {
//                path = CanvasUtil.createRectRoundPath(rectF, radius);
//            } else {
//                path = CanvasUtil.createRectPath(rectF);
//            }
            canvas.drawPath(path, mBarChartPaint);
        } else if (DecimalUtil.smallOrEquals(rectF.left, parentRight) && rectF.right > parentRight) {
            //右边部分滑出的时候，处理柱状图，文字的显示
            float distance = (parentRight - rectF.left);
            rectF.right = rectF.left + distance;
            Path path = CanvasUtil.createRectRoundPath(rectF, radius, RoundRectType.TYPE_LEFT_TOP);
//            if (rectF.isTop) {
//                path = CanvasUtil.createRectRoundPath(rectF, radius, RoundRectType.TYPE_LEFT_TOP);
//            } else {
//                path = CanvasUtil.createRectPath(rectF);
//            }
            mBarChartPaint.setColor(mBarChartAttrs.chartEdgeColor);
            canvas.drawPath(path, mBarChartPaint);
        }
    }
}
