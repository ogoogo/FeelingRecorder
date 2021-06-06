package com.pico.ogoshi.feelingrecorder

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import io.realm.Realm
import kotlin.random.Random

/**
 * Implementation of App Widget functionality.
 */
val realm:Realm= Realm.getDefaultInstance()
class NewAppWidget : AppWidgetProvider() {



    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {

            updateAppWidget(context, appWidgetManager, appWidgetId)
        }

    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created

    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    }


internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {

    val views = RemoteViews(context.packageName, R.layout.new_app_widget)
    val widgetText = "今日もおつかれさまです"
    val datum = realm.where(Memo::class.java).equalTo("good",true).findAll()
    val goodData = datum[Random.nextInt(datum.size)]
    val date=goodData?.date
    val year=goodData?.year
    val month=goodData?.month
    val content=goodData?.event
    val quote=goodData?.quote
    val person=goodData?.personName

    if (goodData?.quoteOrNot==true){
        views.setTextViewText(R.id.widgetContent,quote)
        views.setTextViewText(R.id.widgetPerson,person+"より")
    }else{
        views.setTextViewText(R.id.widgetContent,content)
    }
    views.setTextViewText(R.id.widgetDate,"${year.toString()}.\n${month.toString()}.${date.toString()}")
    views.setTextViewText(R.id.appwidget_text, widgetText)



    appWidgetManager.updateAppWidget(appWidgetId, views)
}