package de.dtag.tlabs.wallet.tests.wallet.api;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.Handler.Callback;
import android.util.Log;

public class WalletEngine {
  private final static String TAG = WalletEngine.class.getName();
  private static final String ACTION_WALLETSERVICE_START = "de.dtag.tlabs.wallet.tests.wallet.WalletService.Start";
  public static final int CODE_WALLET_GETTOKEN = 99101;

  private Context context = null;
  private Callback resultHandler = null;
  private Messenger messenger = null;
  private Synchronizer synchronizer = new Synchronizer();
  private Thread synchronizerThread = new Thread(synchronizer);

  private WalletEngine() {
    Log.d(TAG, "WalletEngine()");
  }

  public WalletEngine(Context context, Handler.Callback resultHandler) {
    Log.d(TAG, "WalletEngine()");

    this.context = context;
    this.resultHandler = resultHandler;
    synchronizerThread.start();

    Log.d(TAG, "WalletEngine() : startet=" + start());
  }

  private boolean start() {
    Log.d(TAG, "start()");

    Intent intent = new Intent(ACTION_WALLETSERVICE_START);
    ServiceConnection conn = new ServiceConnection() {

      @Override
      public void onServiceDisconnected(ComponentName name) {
        Log.d(TAG, "ServiceConnection.onServiceDisconnected()");
        messenger = null;
      }

      @Override
      public void onServiceConnected(ComponentName name, IBinder service) {
        Log.d(TAG, "ServiceConnection.onServiceConnected()");
        messenger = new Messenger(service);
      }
    };
    return context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
  }

  public void release() {
    synchronizer.release();
  }

  public void requestCard(CardRequest request) {
    Log.d(TAG, "requestCard()");

    sendMessage(CODE_WALLET_GETTOKEN, "CardRequest", request.toBundle());

    synchronized (synchronizerThread) {
      try {
        synchronizerThread.wait();
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    forewardMessage(synchronizer.getMessage());
  }

  private void sendMessage(int code, String key, Bundle bundle) {
    Log.d(TAG, "sendMessage()");
    Bundle msgBundle = new Bundle();
    msgBundle.putBundle(key, bundle);

    Message message = new Message();
    message.replyTo = new Messenger(synchronizer.getMessageHandler());
    message.what = code;
    message.setData(msgBundle);

    try {
      messenger.send(message);
    } catch (RemoteException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void forewardMessage(Message msg) {
    if(resultHandler != null) {
      Log.d(TAG, "forewardMessage() "  + msg.what + "/" + msg.arg1);
      resultHandler.handleMessage(msg);
    }
  }

  public class Synchronizer implements Runnable {
    private final String TAG = Synchronizer.class.getName();
    private MessageHandler messageHandler = null;
    private Message message = null;
    private Looper myLooper = null;

    public class MessageHandler extends Handler {
      private final String TAG = MessageHandler.class.getName();

      @Override
      public void handleMessage(Message msg) {
        Log.d(TAG, "handleMessage() " + msg.what + "/" + msg.arg1);
        message = new Message();
        message.copyFrom(msg);

        synchronized (synchronizerThread) {
          synchronizerThread.notify();
        }
      }
    }

    @Override
    public void run() {
      Log.d(TAG, "run() ... start");
      if(Looper.myLooper() == null) {
        Looper.prepare();
        myLooper = Looper.myLooper();
      }
      if(messageHandler == null) {
        messageHandler = new MessageHandler();
      }
      Looper.loop();
      Log.d(TAG, "run() ... end");
    }

    protected void release() {
      myLooper.quit();
    }

    protected MessageHandler getMessageHandler() {
      return messageHandler;
    }

    protected Message getMessage() {
      Message msg = new Message();
      if(message != null) {
        msg.copyFrom(message);
        message = null;
      }
      return msg;
    }
  }
}
