package com.pico.ogoshi.feelingrecorder

import android.app.PendingIntent
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

class NewAppWidget : AppWidgetProvider() {


    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            val realm: Realm = Realm.getDefaultInstance()

            val views = RemoteViews(context.packageName, R.layout.new_app_widget)

            val datum = realm.where(Memo::class.java).equalTo("good", true).findAll()
            val goodData = datum[Random.nextInt(datum.size)]
            val date = goodData?.date
            val year = goodData?.year
            val month = goodData?.month
            val content = goodData?.event
            val quote = goodData?.quote
            val person = goodData?.personName
            val widgetText = "${month}月${date}日にあったイイコト"

            if (goodData?.quoteOrNot == true) {
                views.setTextViewText(R.id.widgetContent, quote)
                views.setTextViewText(R.id.widgetPerson, person)
            } else {
                views.setTextViewText(R.id.widgetContent, content)
                views.setTextViewText(R.id.widgetPerson,"")
            }
            views.setTextViewText(R.id.appwidget_text, widgetText)
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent =
                PendingIntent.getActivity(context, 0,intent, PendingIntent.FLAG_UPDATE_CURRENT)
            views.setOnClickPendingIntent(R.id.widgetWhole, pendingIntent)




            appWidgetManager.updateAppWidget(appWidgetId, views)


        }

    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created

    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}


