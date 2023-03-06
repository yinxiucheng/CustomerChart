package com.yxc.chartlib.render;

import static com.yxc.chartlib.entrys.model.EnergyEntry.ENERGY_TYPE_PRESS;
import static com.yxc.chartlib.entrys.model.EnergyEntry.ENERGY_TYPE_SPORT;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yxc.fitness.chart.entrys.RecyclerBarEntry;
import com.yxc.chartlib.attrs.BarChartAttrs;
import com.yxc.chartlib.component.YAxis;
import com.yxc.chartlib.entrys.model.EnergyEntry;
import com.yxc.chartlib.util.CanvasUtil;
import com.yxc.chartlib.util.ChartComputeUtil;
import com.yxc.chartlib.util.RoundRectType;
import com.yxc.chartlib.utils.DecimalUtil;

/**
 * @author yxc
 * @since  2019/4/14
 */
public class EnergyBarChartRender extends BaseChartRender<RecyclerBarEntry, BarChartAttrs> {

    protected  Context mContext;

    public EnergyBarChartRender(Context context, BarChartAttrs barChartAttrs) {
        super(barChartAttrs);
        this.mContext = context;
    }

    public EnergyBarChartRender(BarChartAttrs barChartAttrs) {
        super(barChartAttrs);
    }

    @Override
    protected int getChartColor(RecyclerBarEntry entry) {
        EnergyEntry energyEntry = (EnergyEntry) entry;
        return EnergyEntry.getEnergyTypeColor(mContext, energyEntry.energyType);
    }

    //绘制柱状图, mYAxis这个坐标会实时变动，所以通过 BarChartItemDecoration 传过来的精确值。
    final public void drawBarChart(final Canvas canvas, @NonNull final RecyclerView parent, final YAxis mYAxis) {
        final float parentRight = parent.getWidth() - parent.getPaddingRight();
        final float parentLeft = parent.getPaddingLeft();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            EnergyEntry barChart = (EnergyEntry) child.getTag();
            RectF rectF = ChartComputeUtil.getBarChartRectF(child, parent, mYAxis, mBarChartAttrs, barChart);
            drawChart(parent.getContext(), canvas, rectF, parentLeft, parentRight, barChart);
        }
    }

    private void drawChart(Context context, Canvas canvas, RectF rectF, float parentLeft, float parentRight, EnergyEntry entry) {
//        float radius = (rectF.right - rectF.left) * mBarChartAttrs.barChartRoundRectRadiusRatio;
        float radius = 36;
        // 浮点数的 == 比较需要注意
        if (DecimalUtil.smallOrEquals(rectF.right, parentLeft)) {
            //continue 会闪，原因是end == parentLeft 没有过滤掉，显示出来柱状图了。
        } else if (rectF.left < parentLeft && rectF.right > parentLeft) {
            //左边部分滑入的时候，处理柱状图的显示
            rectF.left = parentLeft;
            Path path = CanvasUtil.createRectRoundPath(rectF, radius, RoundRectType.TYPE_RIGHT_TOP);
            mBarChartPaint.setColor(mBarChartAttrs.chartEdgeColor);
            canvas.drawPath(path, mBarChartPaint);
        } else if (DecimalUtil.bigOrEquals(rectF.left, parentLeft) && DecimalUtil.smallOrEquals(rectF.right, parentRight)) {
            //中间的; 浮点数的 == 比较需要注意
            int chartColor = EnergyEntry.getEnergyTypeColor(context, entry.energyType);
            mBarChartPaint.setColor(chartColor);
            Path path;
            if (entry.energyType == ENERGY_TYPE_PRESS || entry.energyType == ENERGY_TYPE_SPORT) {
                path = CanvasUtil.createRectRoundPath(rectF, radius, RoundRectType.TYPE_BOTTOM);
            } else {
                path = CanvasUtil.createRectRoundPath(rectF, radius, RoundRectType.TYPE_TOP);
            }
            canvas.drawPath(path, mBarChartPaint);
        } else if (DecimalUtil.smallOrEquals(rectF.left, parentRight) && rectF.right > parentRight) {
            //右边部分滑出的时候，处理柱状图，文字的显示
            float distance = (parentRight - rectF.left);
            rectF.right = rectF.left + distance;
            Path path = CanvasUtil.createRectRoundPath(rectF, radius, RoundRectType.TYPE_LEFT_TOP);
            mBarChartPaint.setColor(mBarChartAttrs.chartEdgeColor);
            canvas.drawPath(path, mBarChartPaint);
        }
    }

}
