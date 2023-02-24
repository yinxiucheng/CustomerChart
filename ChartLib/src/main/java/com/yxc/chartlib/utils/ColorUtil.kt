package com.yxc.chartlib.utils

import android.content.Context
import android.os.Build

object ColorUtil {

    fun getColor(context: Context, colorResourceId:Int):Int{
       return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.resources.getColor(colorResourceId, null)
        }else{
            context.resources.getColor(colorResourceId)
        }

    }

    fun getResourcesColor(context: Context, color: Int): Int {
        var ret = 0x00ffffff
        try {
            ret = context.resources.getColor(color)
        } catch (e: Exception) {
        }
        return ret
    }

    @JvmStatic
    fun getResourcesColor(color: Int): Int {
        return getResourcesColor(AppUtil.app, color)
    }
}