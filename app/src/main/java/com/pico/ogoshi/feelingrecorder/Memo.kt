package com.pico.ogoshi.feelingrecorder

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Memo (
    @PrimaryKey    open var id: String=UUID.randomUUID().toString(),
    open var date :Int=0,
    open var event:String = "",
    open var barometer:Int=0,
    open var good:Boolean=true,
    open var quoteOrNot:Boolean=true,
    open var personName:String="",
    open var quote:String="",
    open var createdAt:Date=Date(System.currentTimeMillis()),
    open var year:Int=0,
    open var month:Int=0,
    open var diaryOrNot:Boolean=false,
    open var diary:String=""

): RealmObject()





