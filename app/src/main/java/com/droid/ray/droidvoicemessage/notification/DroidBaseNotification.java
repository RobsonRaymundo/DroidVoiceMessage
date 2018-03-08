package com.droid.ray.droidvoicemessage.notification;

import android.content.Intent;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import static com.droid.ray.droidvoicemessage.common.DroidCommon.TAG;

/**
 * Created by Robson on 16/02/2016.
 */

public class DroidBaseNotification extends NotificationListenerService {


    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        cancelAllNotifications();
        Log.d(TAG, "onNotificationPosted");
        super.onNotificationPosted(sbn);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn, RankingMap rankingMap) {
        cancelAllNotifications();
        Log.d(TAG, "onNotificationPosted");
        super.onNotificationPosted(sbn, rankingMap);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.d(TAG, "onNotificationRemoved");
        super.onNotificationRemoved(sbn);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn, RankingMap rankingMap) {
        Log.d(TAG, "onNotificationRemoved");
        super.onNotificationRemoved(sbn, rankingMap);
    }

    @Override
    public void onListenerConnected() {
        Log.d(TAG, "onListenerConnected");
        super.onListenerConnected();
    }


    @Override
    public void onNotificationRankingUpdate(RankingMap rankingMap) {
        Log.d(TAG, "onNotificationRankingUpdate");
        super.onNotificationRankingUpdate(rankingMap);
    }

    @Override
    public void onListenerHintsChanged(int hints) {
        Log.d(TAG, "onListenerHintsChanged");
        super.onListenerHintsChanged(hints);
    }

    @Override
    public void onInterruptionFilterChanged(int interruptionFilter) {
        Log.d(TAG, "onInterruptionFilterChanged");
        super.onInterruptionFilterChanged(interruptionFilter);
    }

    @Override
    public StatusBarNotification[] getActiveNotifications() {
        Log.d(TAG, "getActiveNotifications");
        return super.getActiveNotifications();
    }

    @Override
    public StatusBarNotification[] getActiveNotifications(String[] keys) {
        Log.d(TAG, "getActiveNotifications");
        return super.getActiveNotifications(keys);
    }

    @Override
    public RankingMap getCurrentRanking() {
        return super.getCurrentRanking();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onListenerHintsChanged");
        return super.onBind(intent);
    }
}
