package pl.droidsonroids.hodor.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;

import pl.droidsonroids.hodor.Constants;
import pl.droidsonroids.hodor.HodorApplication;
import pl.droidsonroids.hodor.model.User;

public class SendBackService extends IntentService {

    public SendBackService() {
        super("SendBackService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.getStringExtra(Constants.USERNAME_KEY) != null) {
            final String username = intent.getStringExtra(Constants.USERNAME_KEY);
            HodorApplication.getInstance().getDatabaseHelper().getUserFromDatabase(username, this::send);
            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(Constants.NOTIFICATION_ID);
        }
    }

    private void send(final User user) {
        final String usernameFromPrefs = HodorApplication.getInstance().getPreferences().getUsername();
        HodorApplication.getInstance().getRestAdapter().sendPush(user.getToken(), usernameFromPrefs);
    }
}
