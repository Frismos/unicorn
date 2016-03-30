package com.frismos.unicorn;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.badlogic.gdx.utils.ObjectMap;
import com.frismos.unicorn.analytics.GoogleAnalytics;
import com.frismos.unicorn.gamecenter.GameCenterListener;
import com.frismos.unicorn.gamecenter.GameCenterManager;
import com.frismos.unicorn.patterns.GameCenterController;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSError;
import org.robovm.apple.foundation.NSErrorException;
import org.robovm.apple.gamekit.GKAchievement;
import org.robovm.apple.gamekit.GKLeaderboard;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIApplicationLaunchOptions;
import org.robovm.apple.uikit.UIScreen;
import org.robovm.apple.uikit.UIView;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.apple.uikit.UIWindow;
import org.robovm.pods.flurry.analytics.Flurry;
import org.robovm.pods.flurry.analytics.FlurryLogLevel;
import org.robovm.pods.google.GGLContextAnalytics;

import java.util.ArrayList;

public class IOSLauncher extends IOSApplication.Delegate {

    private GoogleAnalytics googleAnalytics;

    @Override
    public boolean didFinishLaunching(UIApplication application, UIApplicationLaunchOptions options) {
        try {
            GGLContextAnalytics.getSharedInstance().configure();
        } catch (NSErrorException e) {
            e.printStackTrace();
        }
        googleAnalytics = new GoogleAnalytics();

        Flurry.enableCrashReporting();
        Flurry.setDebugLogEnabled(true);
        Flurry.setLogLevel(FlurryLogLevel.All);
        Flurry.startSession("CG69RNZHRCRXY9P3CJTY");

        return super.didFinishLaunching(application, options);
    }

    @Override
    protected IOSApplication createApplication() {
        UIWindow window = new UIWindow(UIScreen.getMainScreen().getBounds());
        UIViewController viewController = new UIViewController();

        UIView view = new UIView(UIScreen.getMainScreen().getBounds());
        viewController.setView(view);

        window.setRootViewController(viewController);
        window.makeKeyAndVisible();
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

        ObjectMap<String, Object> controllers = new ObjectMap<>();
        controllers.put("game_center", gameCenterController);
        controllers.put("google_analytics", googleAnalytics);

        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        config.orientationLandscape = true;
        config.orientationPortrait = false;

        return new IOSApplication(new UnicornGame(controllers), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }
}