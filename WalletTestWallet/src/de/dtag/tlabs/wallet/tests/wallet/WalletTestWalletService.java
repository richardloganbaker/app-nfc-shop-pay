package de.dtag.tlabs.wallet.tests.wallet;

import de.dtag.tlabs.wallet.tests.wallet.api.WalletEngine;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class WalletTestWalletService extends Service {
  private final static String TAG = WalletTestWalletService.class.getName();
  private final WalletTestWalletService me = this;
  private final MessageHandler messageHandler = new MessageHandler();
  private final Messenger messenger = new Messenger(messageHandler);
  private Messenger shopMessenger = null;

  public class MessageHandler extends Handler {
    private final String TAG = MessageHandler.class.getName();

    public MessageHandler() {
      Log.d(TAG, "MessageHandler()");
    }

    @Override
    public void handleMessage(Message msg) {
      Log.d(TAG, "handleMessage() 1");
      int what = msg.what;
      Log.d(TAG, "handleMessage() what=" + what);
      if(WalletEngine.CODE_WALLET_GETTOKEN == what) {
        Log.d(TAG, "handleMessage() what=CODE_WALLET_GETTOKEN");
        shopMessenger = msg.replyTo;
        Bundle bundle = msg.getData().getBundle("CardRequest");
        if (bundle != null) {

          Intent intent = new Intent(WalletTestWalletActivity.INTENT_GET_TOKEN);
           intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           intent.putExtra("CardRequest", bundle);
          me.startActivity(intent);
          WalletTestWalletActivity.setCallback(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
              Log.d(TAG, "Handler.Callback.handleMessage() " + msg.what + "/" + msg.arg1);
              if(shopMessenger != null)
              {
                Log.d(TAG, "Handler.Callback.handleMessage() : reply to");
                  try {
                    shopMessenger.send(msg);
                } catch (RemoteException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
                }
                shopMessenger = null;
              }
              return false;
            }
          });
        }
      }
      Log.d(TAG, "handleMessage() 2");
    }
  }

  @Override
  public void onCreate() {
    Log.d(TAG, "onCreate()");
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Log.d(TAG, "onStartCommand() " + startId + " : " + intent);
//    Toast.makeText(this, "Service started.", Toast.LENGTH_SHORT).show();
    // We want this service to continue running until it is explicitly
    // stopped, so return sticky.
    return START_STICKY;
  }

  @Override
  public void onDestroy() {
    Log.d(TAG, "onDestroy()");
//    Toast.makeText(this, "Service stopped.", Toast.LENGTH_SHORT).show();
  }

  @Override
  public IBinder onBind(Intent intent) {
    Log.d(TAG, "onBind()");
//    Toast.makeText(this, "Service bound.", Toast.LENGTH_SHORT).show();
    return messenger.getBinder();
  }

}
