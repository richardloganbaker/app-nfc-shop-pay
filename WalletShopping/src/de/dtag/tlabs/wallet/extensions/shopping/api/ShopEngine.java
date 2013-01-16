/*
 * Shop functions engine
 */
package de.dtag.tlabs.wallet.extensions.shopping.api;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.Handler.Callback;
import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class ShopEngine; Shop functions engine.
 */
public class ShopEngine {
  private final static String TAG = ShopEngine.class.getName();
  private static final String ACTION_WALLETSHOPPINGSERVICE_START = "de.dtag.tlabs.wallet.extensions.shopping.shop.WalletShoppingService.Start";
  public static final int CODE_SHOP_OPEN = 9101;
  public static final int CODE_SHOP_ADDITEM = 9201;
  public static final int CODE_SHOP_REMOVEITEM = 9202;
  public static final int CODE_SHOP_CHECKOUT = 9901;
  public static final int CODE_SHOP_RELEASE = 9199;

  private Context context = null;
  private Callback resultHandler = null;
  private Messenger messenger = null;

  private ShopEngine () {
    Log.d(TAG, "ShopEngine()");
  }

  /**
   * Instantiates a new shop engine.
   *
   * @param activity : the context of the caller, who uses the shop
   * @param resultHandler : the callback handler implemented by the caller
   */
  public ShopEngine (Context context, Handler.Callback resultHandler) {
    Log.d(TAG, "ShopEngine()");

    this.context = context;
    this.resultHandler = resultHandler;

    Log.d(TAG, "ShopEngine() : startet=" + start());
  }

  private boolean start() {
    Log.d(TAG, "start()");

    Intent intent = new Intent(ACTION_WALLETSHOPPINGSERVICE_START);
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

  /**
   * Open shop.
   *
   * @param merchant : the shop's merchant
   */
  public void openShop(Merchant merchant) {
    Log.d(TAG, "openShop()");
    sendMessage(CODE_SHOP_OPEN, "Merchant", merchant.toBundle());
  }

  /**
   * Adds the item.
   *
   * @param item : the shopping item
   */
  public void addItem(Item item) {
    Log.d(TAG, "addItem()");
    sendMessage(CODE_SHOP_ADDITEM, "Item", item.toBundle());
  }

  /**
   * Removes the item.
   *
   * @param productID : the shopping item product id; item.productID
   */
  public void removeItem(String productID) {
    Log.d(TAG, "removeItem()");
    sendMessage(CODE_SHOP_REMOVEITEM, "ProductID", productID);
  }

  /**
   * Checkout. Ask for payment.
   */
  public void checkout() {
    Log.d(TAG, "checkout()");
    sendMessage(CODE_SHOP_CHECKOUT);
  }

  /**
   * Release shop.
   */
  public void release() {
    Log.d(TAG, "release()");
    sendMessage(CODE_SHOP_RELEASE);
  }

  private void sendMessage(int code) {
    Message message = new Message();
    message.replyTo = new Messenger(new MessageHandler());
    message.what = code;

    try {
      messenger.send(message);
    } catch (RemoteException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void sendMessage(int code, String key, String value) {
    Bundle msgBundle = new Bundle();
    msgBundle.putString(key, value);

    Message message = new Message();
    message.replyTo = new Messenger(new MessageHandler());
    message.what = code;
    message.setData(msgBundle);

    try {
      messenger.send(message);
    } catch (RemoteException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void sendMessage(int code, String key, Bundle bundle) {
    Bundle msgBundle = new Bundle();
    msgBundle.putBundle(key, bundle);

    Message message = new Message();
    message.replyTo = new Messenger(new MessageHandler());
    message.what = code;
    message.setData(msgBundle);

    try {
      messenger.send(message);
    } catch (RemoteException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private class MessageHandler extends Handler {
    private final String TAG = MessageHandler.class.getName();

    @Override
    public void handleMessage(Message msg) {
      Log.d(TAG, "handleMessage() 1");

      Answer answer = null;
      int what = msg.what;
      Log.d(TAG, "handleMessage() what=" + what);
      if(Activity.RESULT_OK == what) {
        Log.d(TAG, "handleMessage() what=RESULT_OK");
        Bundle bundle = msg.getData().getBundle("Answer");
        if (bundle != null) {
          answer = new Answer(bundle);
          Log.d(TAG, "handleMessage() answer=" + answer.state + " " + answer.message);
        } else {
          Log.d(TAG, "handleMessage() Got no answer!");
        }
      }
      if(resultHandler != null) {
        resultHandler.handleMessage(msg);
      }
      Log.d(TAG, "handleMessage() 2");
    }
  }
}
