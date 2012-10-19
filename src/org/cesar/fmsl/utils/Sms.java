package org.cesar.fmsl.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

public class Sms {
	
	public boolean sendSms(Context context, String to, String message) {
		
		try {
			SmsManager smsManager = SmsManager.getDefault();
			PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, new Intent(), 0);
			smsManager.sendTextMessage(to, null, message, pIntent, null);
			return true;
		} catch (Exception e) {
			Log.e("FMSL", "Error sending message: " + e.getMessage(), e);
			return false;
		}
		
		
	}

}
