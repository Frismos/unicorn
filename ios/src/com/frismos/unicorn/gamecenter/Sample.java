
package com.frismos.unicorn.gamecenter;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.frismos.unicorn.UnicornGame;
import org.robovm.apple.foundation.NSError;
import org.robovm.apple.gamekit.GKAchievement;
import org.robovm.apple.gamekit.GKLeaderboard;
import org.robovm.apple.uikit.*;

import java.util.ArrayList;

/** Sample usage of GameKit framework */
public class Sample extends IOSApplication.Delegate {

    private GameCenterManager gcManager;
    private UIWindow window;
    private UnicornGame game;

    @Override
    public void didFinishLaunching(UIApplication application) {
        super.didFinishLaunching(application);
        System.out.println("did finish launching");
    }

    @Override
    protected IOSApplication createApplication() {

        window = new UIWindow(UIScreen.getMainScreen().getBounds());
        UIViewController viewController = new UIViewController();

        UIView view = new UIView(UIScreen.getMainScreen().getBounds());
        viewController.setView(view);

        window.setRootViewController(viewController);
        window.makeKeyAndVisible();

        gcManager = new GameCenterManager(UIApplication.getSharedApplication().getKeyWindow(), new GameCenterListener() {
            @Override
            public void playerLoginFailed (NSError error) {
                System.out.println("playerLoginFailed. error: " + error);
            }

            @Override
            public void playerLoginCompleted () {
                System.out.println("playerLoginCompleted");
                gcManager.loadLeaderboards();
                game.submitScore();
            }

            @Override
            public void achievementReportCompleted () {
                System.out.println("achievementReportCompleted");
            }

            @Override
            public void achievementReportFailed (NSError error) {
                System.out.println("achievementReportFailed. error: " + error);
            }

            @Override
            public void achievementsLoadCompleted (ArrayList<GKAchievement> achievements) {
                System.out.println("achievementsLoadCompleted: " + achievements.size());
            }

            @Override
            public void achievementsLoadFailed (NSError error) {
                System.out.println("achievementsLoadFailed. error: " + error);
            }

            @Override
            public void achievementsResetCompleted () {
                System.out.println("achievementsResetCompleted");
            }

            @Override
            public void achievementsResetFailed (NSError error) {
                System.out.println("achievementsResetFailed. error: " + error);
            }

            @Override
            public void scoreReportCompleted () {
                System.out.println("scoreReportCompleted");
            }

            @Override
            public void scoreReportFailed (NSError error) {
                System.out.println("scoreReportFailed. error: " + error);
            }

            @Override
            public void leaderboardsLoadCompleted (ArrayList<GKLeaderboard> scores) {
                System.out.println("scoresLoadCompleted: " + scores.size());
            }

            @Override
            public void leaderboardsLoadFailed (NSError error) {
                System.out.println("scoresLoadFailed. error: " + error);
            }

            @Override
            public void leaderboardViewDismissed () {
                System.out.println("leaderboardViewDismissed");
            }

            @Override
            public void achievementViewDismissed () {
                System.out.println("achievementViewDismissed");
            }
        });
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        config.orientationLandscape = true;
        config.orientationPortrait = false;
        game = new UnicornGame(gcManager);

        return new IOSApplication(game, config);
    }
}
