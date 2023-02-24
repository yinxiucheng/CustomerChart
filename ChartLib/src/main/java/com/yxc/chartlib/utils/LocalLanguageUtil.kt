package com.yxc.chartlib.utils

import android.content.Context
import java.util.*

object LocalLanguageUtil {

    fun languageFrench():Boolean{
        val context: Context = com.yxc.chartlib.utils.AppUtil.app
        val locale = context.resources.configuration.locale
        val language = locale.language.lowercase(Locale.getDefault())
        return language.contains("fr")
    }

    fun languageGreek(): Boolean{
        val context: Context = com.yxc.chartlib.utils.AppUtil.app
        val locale = context.resources.configuration.locale
        val language = locale.language.lowercase(Locale.getDefault())
        return language.contains("el")
    }

    fun languageGerman():Boolean{
        val context: Context = com.yxc.chartlib.utils.AppUtil.app
        val locale = context.resources.configuration.locale
        val language = locale.language.lowercase(Locale.getDefault())
        return language.contains("de")
    }


    fun sportBehaviorSpecial():Boolean{
        val context: Context = com.yxc.chartlib.utils.AppUtil.app
        val locale = context.resources.configuration.locale
        val language = locale.language.lowercase(Locale.getDefault())
        return language.contains("uk") || language.contains("ro") || language.contains("es")
                || language.contains("ru") || language.contains("de") || language.contains("el")
    }

}