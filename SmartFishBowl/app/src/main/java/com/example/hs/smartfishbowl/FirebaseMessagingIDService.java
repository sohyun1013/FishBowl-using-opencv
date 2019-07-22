package com.example.hs.smartfishbowl;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingIDService extends FirebaseMessagingService {
    private static final String TAG = "Service";
/*
    @Override
    public void onTokenRefresh(){
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: "+ refreshedToken);

        sendRegistrationToServer(refreshedToken);
    }*/

    @Override
    public void onNewToken(String s){
        super.onNewToken(s);
        Log.e("Firebase",s);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        Log.d(TAG,"From: "+remoteMessage);
        Log.d(TAG,remoteMessage.getData().toString());
        sendNotification(remoteMessage);
        /*if(remoteMessage != null && remoteMessage.getData().size()>0){
            sendNotification(remoteMessage);
        }*/
    }/*
    private void sendNotification(RemoteMessage remoteMessage){
        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");

        sendNotification(remoteMessage);
    }
    private void sendRegistrationToServer(String token){

    }

    private void scheduleJob() {
        //이건 아직 나중에 알아 볼것.
        Log.d(TAG, "이것에 대해서는 나중에 알아 보자.");
    }

    private void handleNow() {
        Log.d(TAG, "10초이내 처리됨");
    }
*/
    private void sendNotification(RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        //NotificationChannel mChannel = new NotificationChannel("and","and", NotificationManager.IMPORTANCE_DEFAULT);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder notificationBuilder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentText(remoteMessage.getData().toString())
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{1000, 1000})
                .setLights(Color.BLUE, 1,1)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
