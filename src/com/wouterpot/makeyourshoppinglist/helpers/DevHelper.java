package com.wouterpot.makeyourshoppinglist.helpers;

import com.google.appengine.api.utils.SystemProperty;

public class DevHelper {
	
    public static boolean isDevelopment() {
        return SystemProperty.environment.value() == SystemProperty.Environment.Value.Development;
    }

    public static String getAppUrl() {
        if (isDevelopment()) {
            return "http://127.0.0.1:8888/com_wouterpot_makeyourshoppinglist.html?gwt.codesvr=127.0.0.1:9997";
        } else {
            return "http://makeyourshoppinglist.appspot.com/";
        }
    }
}
