package pl.droidsonroids.hodor.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import pl.droidsonroids.hodor.Constants;
import pl.droidsonroids.hodor.R;
import pl.droidsonroids.hodor.model.FCMMessage;
import pl.droidsonroids.hodor.ui.MainActivity;

public class HodorMessagingService extends FirebaseMessagingService {

    private static final String URI_ANDROID_RESOURCE = "android.resource://";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String username = remoteMessage.getData().get(FCMMessage.USERNAME);
        showNotification(username);
    }

    private void showNotification(final String username) {
        Intent intentStart = new Intent(this, MainActivity.class);
        intentStart.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntentStart = PendingIntent.getActivity(this, Constants.PENDING_START_ID, intentStart, 0);

        Intent startBackService = new Intent(this, SendBackService.class);
        startBackService.putExtra(Constants.USERNAME_KEY, username);
        PendingIntent pendingIntentSendBack = PendingIntent.getService(this, Constants.PENDING_BACK_ID, startBackService, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = buildNotification(username, pendingIntentStart, pendingIntentSendBack);
        NotificationManager notifManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notifManager.notify(Constants.NOTIFICATION_ID, notification);
    }

    private Notification buildNotification(String username, PendingIntent pendingIntentStart, PendingIntent pendingIntentSendBack) {
        return new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(String.format(getString(R.string.notification_msg), username))
                .setContentIntent(pendingIntentStart)
                .addAction(0, getString(R.string.send_hodor_back), pendingIntentSendBack)
                .setAutoCancel(true)
                .setVibrate(Constants.VIBRATION_PATTERN)
                .setLights(Color.CYAN, Constants.LIGHT_ENABLED, Constants.LIGHT_DISABLED)
                .setSound(Uri.parse(URI_ANDROID_RESOURCE + getPackageName() + "/" + R.raw.hodor))
                .setPriority(Notification.PRIORITY_HIGH)
                .build();
    }
}
