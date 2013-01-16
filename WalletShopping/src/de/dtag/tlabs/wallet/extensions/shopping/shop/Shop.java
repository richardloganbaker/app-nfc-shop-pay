package de.dtag.tlabs.wallet.extensions.shopping.shop;

import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import de.dtag.tlabs.wallet.extensions.shopping.api.Answer;
import de.dtag.tlabs.wallet.extensions.shopping.api.Item;
import de.dtag.tlabs.wallet.extensions.shopping.api.Merchant;
import de.dtag.tlabs.wallet.tests.wallet.api.CardRequest;
import de.dtag.tlabs.wallet.tests.wallet.api.CardResponse;
import de.dtag.tlabs.wallet.tests.wallet.api.WalletEngine;

public class Shop {
  private final static String TAG = Shop.class.getName();
  private static WalletEngine engine = null;
  private static HashMap<String, Item> items = null;
  private static Merchant merchant = null;
  private static boolean merchantOK = false;
  private CallbackHandler callbackHandler = null;

  protected Shop(Context context, Merchant merchant) {
    Log.d(TAG, "Shop()");
    Shop.merchant = merchant;
    if("knownMerchantID".equals(merchant.merchantID) && "knownMerchantToken".equals(merchant.merchantToken)) {
      merchantOK = true;
    } else {
      merchantOK = false;
    }
    if (context != null) {
      callbackHandler = new CallbackHandler();
      engine = new WalletEngine(context, callbackHandler.callback);
    }
  }

  public Answer createBasket() {
    Log.d(TAG, "createBasket()");
    if (merchantOK) {
      if(items != null) {
        return new Answer(Answer.STATE_ERROR_SHOP_ALLREADY_OPENED, "ERR; The shop is allready opened!");
      }
      items = new HashMap<String, Item>();
      return new Answer(Answer.STATE_OK, "OK; Open shop for merchant: " + merchant.merchantID);
    } else {
      items = null;
      return new Answer(Answer.STATE_ERROR_UNKNOWN_SHOP, "ERR; Could not open shop for merchant: " + merchant.merchantID);
    }
  }

  public Answer addItem(Item item) {
    Log.d(TAG, "addItem()");
    if(items == null) {
      Log.w(this.getClass().getSimpleName(), "ERR; Shop closed!");
      return new Answer(Answer.STATE_ERROR_NO_SHOP, "ERR; Shop closed!");
    }
    items.put(item.productID, item);
    return new Answer(Answer.STATE_OK, "OK; Added item: " + item.productID + ", " + item.productName);
  }

  public Answer removeItem(String productID) {
    Log.d(TAG, "removeItem()");
    if(items == null) {
      Log.w(this.getClass().getSimpleName(), "ERR; Shop closed!");
      return new Answer(Answer.STATE_ERROR_NO_SHOP, "ERR; Shop closed!");
    }
    if(items.size() < 1) {
      Log.w(this.getClass().getSimpleName(), "ERR; No items in the basket!");
      return new Answer(Answer.STATE_ERROR_NO_ITEMS, "ERR; No items in the basket!");
    }
    if(items.containsKey(productID)) {
      items.remove(productID);
      return new Answer(Answer.STATE_OK, "OK; Removed item: " + productID);
    } else {
      return new Answer(Answer.STATE_ERROR_UNKNOWN_ITEM, "ERR; Unknown item: " + productID);
    }
  }

  public Answer checkout() {
    Log.d(TAG, "checkout()");
    if(items == null) {
      Log.w(this.getClass().getSimpleName(), "ERR; Shop closed!");
      return new Answer(Answer.STATE_ERROR_NO_SHOP, "ERR; Shop closed!");
    } else {
      if(items.size() < 1) {
        Log.w(this.getClass().getSimpleName(), "ERR; No items in the basket!");
        return new Answer(Answer.STATE_CHECKOUT_NO_ITEMS, "ERR; No items in the basket!");
      }

      if(engine == null) {
        Log.w(this.getClass().getSimpleName(), "ERR; No wallet found!");
        return new Answer(Answer.STATE_CHECKOUT_FAILED, "ERR; No wallet found!");
      }

      Log.d(TAG, "Try checkout 1!");
      CardRequest request = new CardRequest("shopID", "shopToken", "requestDescription", "tokenRequirements");
      engine.requestCard(request);

      Log.d(TAG, "Try checkout 2!");
      return callbackHandler.remoteAnswer;
    }
  }

  public Answer  release() {
    Log.d(TAG, "release()");
    if(items == null) {
      Log.w(this.getClass().getSimpleName(), "ERR; Shop closed!");
      return new Answer(Answer.STATE_ERROR_NO_SHOP, "ERR; Shop closed!");
    }
    items.clear();
    items = null;

    engine.release();
    return new Answer(Answer.STATE_OK, "OK");
  }

  private class CallbackHandler {
    private final String TAG = CallbackHandler.class.getName();
    public Answer remoteAnswer = null;
    public Handler.Callback callback = new Handler.Callback() {

      @Override
      public boolean handleMessage(Message msg) {
        Log.d(TAG, "Handler.Callback.handleMessage() "  + msg.what + "/" + msg.arg1);
        if(msg.arg1 == WalletEngine.CODE_WALLET_GETTOKEN) {
          Log.d(TAG, "Handler.Callback.handleMessage() arg1=CODE_WALLET_GETTOKEN");
          Bundle bundle = msg.getData().getBundle("CardResponse");
          if (bundle != null) {
            CardResponse response = new CardResponse(bundle);
            Log.d(TAG, "Handler.Callback.handleMessage() got CardResponse=" + response.message);
            if(response.message.equals("OK")) {
              remoteAnswer = new Answer(Answer.STATE_CHECKOUT_SUCCESSFUL, "Payment confirmed.");
            } else {
              remoteAnswer = new Answer(Answer.STATE_CHECKOUT_FAILED, "Payment NOT confirmed!");
            }
          }
        }
        return false;
      }
    };
  }
}
