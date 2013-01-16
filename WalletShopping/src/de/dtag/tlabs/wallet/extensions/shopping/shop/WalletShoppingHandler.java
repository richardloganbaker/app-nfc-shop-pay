package de.dtag.tlabs.wallet.extensions.shopping.shop;

import de.dtag.tlabs.wallet.extensions.shopping.api.Answer;
import de.dtag.tlabs.wallet.extensions.shopping.api.ShopEngine;
import de.dtag.tlabs.wallet.extensions.shopping.api.Item;
import de.dtag.tlabs.wallet.extensions.shopping.api.Merchant;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

public class WalletShoppingHandler extends Handler {
  private final static String TAG = WalletShoppingHandler.class.getName();
  private static Shop shop = null;
  private Context context = null;

  private WalletShoppingHandler() {
    Log.d(TAG, "WalletShoppingHandler()");
  }

  public WalletShoppingHandler(Context context) {
    Log.d(TAG, "WalletShoppingHandler()");

    this.context = context;
  }

  @Override
  public void handleMessage(Message msg) {
    Log.d(TAG, "handleMessage() 1");

    Answer answer = null;
    int what = msg.what;
    Log.d(TAG, "handleMessage() what=" + what);
    if(ShopEngine.CODE_SHOP_OPEN == what) {
      Log.d(TAG, "handleMessage() what=CODE_SHOP_OPEN");
      Bundle bundle = msg.getData().getBundle("Merchant");
      if (bundle != null) {
        shop = new Shop(context, new Merchant(bundle));
        if(shop != null) {
          answer = shop.createBasket();
        }
      }
    } else if(ShopEngine.CODE_SHOP_ADDITEM == what) {
      Log.d(TAG, "handleMessage() what=CODE_SHOP_ADDITEM");
      Bundle bundle = msg.getData().getBundle("Item");
      if (bundle != null) {
        if(shop != null) {
          answer = shop.addItem(new Item(bundle));
        }
      }
    } else if(ShopEngine.CODE_SHOP_REMOVEITEM == what) {
      Log.d(TAG, "handleMessage() what=CODE_SHOP_REMOVEITEM");
      String productID = msg.getData().getString("ProductID");
      if (productID != null) {
        if(shop != null) {
          answer = shop.removeItem(productID);
        }
      }
    } else if(ShopEngine.CODE_SHOP_CHECKOUT == what) {
      Log.d(TAG, "handleMessage() what=CODE_SHOP_CHECKOUT");
      if(shop != null) {
        answer = shop.checkout();
      }
    } else if(ShopEngine.CODE_SHOP_RELEASE == what) {
      Log.d(TAG, "handleMessage() what=CODE_SHOP_RELEASE");
      if(shop != null) {
        answer = shop.release();
      }
    } else {
      answer = new Answer(Answer.STATE_ERROR, "Unknown action!");
    }

    if (answer != null) {
      Log.d(TAG, "handleMessage() send result");
      Message answ = new Message();
      Bundle abdl = new Bundle();
      abdl.putBundle("Answer", answer.toBundle());
      answ.setData(abdl);
      answ.what = Activity.RESULT_OK;
      try {
        msg.replyTo.send(answ);
      } catch (RemoteException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    Log.d(TAG, "handleMessage() 2");
  }
}
