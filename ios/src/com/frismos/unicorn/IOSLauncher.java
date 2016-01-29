package com.frismos.unicorn;

import com.badlogic.gdx.Gdx;
import com.frismos.unicorn.gamecenter.GameCenterListener;
import com.frismos.unicorn.gamecenter.GameCenterManager;
import com.frismos.unicorn.gamecenter.Sample;
import com.frismos.unicorn.manager.GameCenterController;
import com.frismos.unicorn.util.Constants;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSError;
import org.robovm.apple.foundation.NSNumber;
import org.robovm.apple.gamekit.GKAchievement;
import org.robovm.apple.gamekit.GKLeaderboard;
import org.robovm.apple.uikit.*;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.frismos.unicorn.UnicornGame;
import org.robovm.pods.flurry.analytics.Flurry;
import org.robovm.pods.google.analytics.GAI;
import org.robovm.pods.google.analytics.GAIDictionaryBuilder;
import org.robovm.pods.google.analytics.GAITracker;

import java.util.ArrayList;

public class IOSLauncher extends IOSApplication.Delegate {
    @Override
    protected IOSApplication createApplication() {

        UIWindow uiWindow = new UIWindow(UIScreen.getMainScreen().getBounds());
        uiWindow.makeKeyAndVisible();
        UIViewController viewController = new UIViewController();
        UIView view = new UIView(UIScreen.getMainScreen().getBounds());
        viewController.setView(view);
        uiWindow.setRootViewController(((IOSApplication) Gdx.app).getUIViewController());
        GameCenterController gameCenterController = new GameCenterManager(UIApplication.getSharedApplication().getKeyWindow(), new GameCenterListener() {
            @Override
            public void playerLoginCompleted() {

            }

            @Override
            public void playerLoginFailed(NSError error) {

            }

            @Override
            public void achievementReportCompleted() {

            }

            @Override
            public void achievementReportFailed(NSError error) {

            }

            @Override
            public void achievementsLoadCompleted(ArrayList<GKAchievement> achievements) {

            }

            @Override
            public void achievementsLoadFailed(NSError error) {

            }

            @Override
            public void achievementsResetCompleted() {

            }

            @Override
            public void achievementsResetFailed(NSError error) {

            }

            @Override
            public void scoreReportCompleted() {

            }

            @Override
            public void scoreReportFailed(NSError error) {

            }

            @Override
            public void leaderboardsLoadCompleted(ArrayList<GKLeaderboard> scores) {

            }

            @Override
            public void leaderboardsLoadFailed(NSError error) {

            }

            @Override
            public void leaderboardViewDismissed() {

            }

            @Override
            public void achievementViewDismissed() {

            }
        });

        Flurry.setAppVersion(Constants.APP_VERSION);
        Flurry.enableCrashReporting();
        Flurry.startSession("CG69RNZHRCRXY9P3CJTY");

        GAI gaiInstance = GAI.getSharedInstance();
        gaiInstance.setTracksUncaughtExceptions(true);
        GAITracker tracker = gaiInstance.getTracker("UA-72850710-1");
        tracker.send(GAIDictionaryBuilder.createEvent("Game Started", "", "", NSNumber.valueOf(0)).build());

        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        return new IOSApplication(new UnicornGame(gameCenterController), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, Sample.class);
        pool.close();
    }
}