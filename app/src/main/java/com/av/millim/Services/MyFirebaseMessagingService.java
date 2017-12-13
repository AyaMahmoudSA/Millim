package com.av.millim.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.av.millim.Activites.LoginActivity;
import com.av.millim.Activites.MainActivity;
import com.av.millim.Activites.SignUpActivity;
import com.av.millim.Data.CONSTANTS;
import com.av.millim.R;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.av.millim.Data.CONSTANTS.YES_ACTION;

/**
 * Created by Maiada on 12/5/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public String TAG = getClass().getSimpleName().toString();
    public static Context getContext;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        getContext = getApplicationContext();
        Log.e("dataChat",remoteMessage.getData().toString());
        try
        {

            JSONObject object = new JSONObject(remoteMessage.getData().toString());

            Log.e("body", object.getJSONObject("notification").getString("body"));

            String getBodyOfNotification =object.getJSONObject("notification").getString("body");
            sendNotification("MilliM",object.getJSONObject("notification").getString("body"));
        }
catch (JSONException ex){

}

    }

    private static void sendNotification(String title,String messageBody) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

     /*   Intent resultIntent = new Intent(getContext, MainActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getContext,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );*/

    /*    Intent dismissIntent = new Intent(getContext, LoginActivity.class);
        PendingIntent piDismiss = PendingIntent.getActivity(getContext, 0, dismissIntent,  PendingIntent.FLAG_CANCEL_CURRENT|PendingIntent.FLAG_UPDATE_CURRENT);

        Intent snoozeIntent = new Intent(getContext, SignUpActivity.class);
        PendingIntent piSnooze = PendingIntent.getActivity(getContext, 0, snoozeIntent, 0 );*/


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getContext)
                        .setSmallIcon(R.drawable.ic_millim_logo)
                        .setSound(alarmSound)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(messageBody))
                    /*    .addAction(R.drawable.ic_snooze,"LoginActivity",piDismiss)
                        .addAction(R.drawable.ic_dimiss,"SignUpActivity",piSnooze)*/
                        .setAutoCancel(true);


    //    mBuilder.setContentIntent(resultPendingIntent);

        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getContext.getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(001, mBuilder.build());

        //Get an instance of NotificationManager//
    /*    NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getContext)
                        .setSmallIcon(R.drawable.ic_millim_logo)
                        .setContentTitle(title)
                        .setSound(alarmSound)
                        .setContentText(messageBody);

        // Gets an instance of the NotificationManager service//
        NotificationManager mNotificationManager =
                (NotificationManager) getContext.getSystemService(Context.NOTIFICATION_SERVICE);

        // When you issue multiple notifications about the same type of event,
        // it’s best practice for your app to try to update an existing notification
        // with this new information, rather than immediately creating a new notification.
        // If you want to update this notification at a later date, you need to assign it an ID.
        // You can then use this ID whenever you issue a subsequent notification.
        // If the previous notification is still visible, the system will update this existing notification,
        // rather than create a new one. In this example, the notification’s ID is 001//
                mNotificationManager.notify(001, mBuilder.build());*/



    }
}
