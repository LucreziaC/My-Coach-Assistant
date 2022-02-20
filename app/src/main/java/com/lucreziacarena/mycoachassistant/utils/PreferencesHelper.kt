package com.lucreziacarena.mycoachassistant.utils

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferencesHelper @Inject constructor(@ApplicationContext context: Context) {

    companion object{
        const val LAST_NETWORK_LOOKUP: String = "last_network_lookup"
        const val ATHLETS_CACHE_TIME: String = "athlete_cahce_time"
    }

    private val mContext = context.applicationContext
    private val  mSharedPreferences : SharedPreferences = context.getSharedPreferences("ESCARGO_OFFICINE", Context.MODE_PRIVATE)
    private val  mEditor: SharedPreferences.Editor = mSharedPreferences.edit();


    fun  getStringPreference( key:String): String? {
        return mSharedPreferences.getString(key , null);
    }

    fun  setStringPreference( key: String ,  value:String){
        mEditor.putString(key , value);
        mEditor.commit();
    }
    fun  getLongPreference( key:String): Long {
        return mSharedPreferences.getLong(key , -1);
    }

    fun  setLongPreference( key: String ,  value:Long){
        mEditor.putLong(key , value);
        mEditor.commit();
    }
}
