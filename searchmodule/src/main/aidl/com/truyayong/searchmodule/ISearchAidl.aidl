// ISearchAidl.aidl
package com.truyayong.searchmodule;

import com.truyayong.searchmodule.IOnSearchResultArrivedListener;

// Declare any non-default types here with import statements

interface ISearchAidl {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
     void saveSearchLucene();
     List<String> searchKeyword(String keyword);
     void registerListener(IOnSearchResultArrivedListener listener);
     void unregisterListener(IOnSearchResultArrivedListener listener);
}
