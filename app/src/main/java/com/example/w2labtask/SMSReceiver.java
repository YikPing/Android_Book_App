package com.example.w2labtask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Toast;


public class SMSReceiver extends BroadcastReceiver {

    /*
     * This method 'onReceive' will be invoked with each new incoming SMS
     * */
    @Override
    public void onReceive(Context context, Intent intent) {
        /*
         * Use the Telephony class to extract the incoming messages from the intent
         * */
        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for (int i = 0; i < messages.length; i++) {
            SmsMessage currentMessage = messages[i];
            String message = currentMessage.getDisplayMessageBody();
            String senderNum = currentMessage.getDisplayOriginatingAddress();
            Toast.makeText(context,"Sender: " + senderNum + ", message: "+ message , Toast.LENGTH_SHORT).show();

            /*
             * Now, for each new message, send a broadcast contains the new message to MainActivity
             * The MainActivity has to tokenize the new message and update the UI
             * */
            Intent msgIntent = new Intent();
            msgIntent.setAction("MySMS");
            msgIntent.putExtra("bookDetail", message);
            context.sendBroadcast(msgIntent);



        }


    }
}