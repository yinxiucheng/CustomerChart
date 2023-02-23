package com.yxc.chartlib.entrys;

import com.xiaomi.fitness.chart.entrys.RecyclerBarEntry;

import java.util.List;

public class MultiBarEntry extends RecyclerBarEntry {

    private List<MultiItem> multiItems;

    public MultiBarEntry(float x, float y, long timestamp, int type) {
        super(x, y, timestamp, type);
    }

    public void setMultiItems(List<MultiItem> multiItems) {
        this.multiItems = multiItems;
    }

    public List<MultiItem> getMultiItems() {
        return multiItems;
    }

    public static class MultiItem {
        public int color;
        public int value;
        public boolean topStatus;

        public MultiItem(int color, int value, boolean topStatus) {
            this.color = color;
            this.value = value;
            this.topStatus = topStatus;
        }
    }
}
