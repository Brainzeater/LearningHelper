package com.kushnarev.learnproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kushnarev.learnproject.utilities.NotificationUtils;

public class Timer extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Do something every 30 seconds
        System.out.println("Notification!");
        NotificationUtils.remindUser(context);
    }
}
