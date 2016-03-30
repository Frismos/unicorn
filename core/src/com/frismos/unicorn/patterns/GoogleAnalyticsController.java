package com.frismos.unicorn.patterns;

/**
 * Created by eavanyan on 3/22/16.
 */
public interface GoogleAnalyticsController {
    public void sendEvent(String category, String action, String label, int value);
}
