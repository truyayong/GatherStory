// IOnSearchResultArrivedListener.aidl
package com.truyayong.searchmodule;

// Declare any non-default types here with import statements

interface IOnSearchResultArrivedListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onSearchResultArrived(in List<String> result);
}
