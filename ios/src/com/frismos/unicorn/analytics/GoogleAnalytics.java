package com.frismos.unicorn.analytics;

import com.frismos.unicorn.patterns.GoogleAnalyticsController;

import org.robovm.apple.foundation.NSNumber;
import org.robovm.pods.google.analytics.GAI;
import org.robovm.pods.google.analytics.GAIDictionaryBuilder;
import org.robovm.pods.google.analytics.GAILogLevel;
import org.robovm.pods.google.analytics.GAITracker;

/**
 * Created by eavanyan on 3/22/16.
 */
public class GoogleAnalytics implements GoogleAnalyticsController {

    private GAI sharedInstance;
    private GAITracker tracker;

    public GoogleAnalytics() {
        sharedInstance = GAI.getSharedInstance();
        sharedInstance.getLogger().setLogLevel(GAILogLevel.Verbose);
        tracker = sharedInstance.getTracker("UA-72850710-1");

        System.out.println("google analytics");
    }

    @Override
    public void sendEvent(String category, String action, String label, int value) {
        tracker.send(GAIDictionaryBuilder.createEvent(category, action, label, NSNumber.valueOf(value)).build());
        System.out.println(GAIDictionaryBuilder.createEvent(category, action, label, NSNumber.valueOf(value)).build());
    }
}
