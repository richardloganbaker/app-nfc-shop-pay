package de.dtag.tlabs.wallet.tests.wallet;

import de.dtag.tlabs.wallet.tests.wallet.api.CardRequest;
import de.dtag.tlabs.wallet.tests.wallet.api.CardResponse;
import de.dtag.tlabs.wallet.tests.wallet.api.WalletEngine;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.Vibrator;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WalletTestWalletActivity extends Activity {
  public final static String INTENT_GET_TOKEN = "de.dtag.tlabs.wallet.tests.wallet.Wallet.GetToken";
  private final static String TAG = WalletTestWalletActivity.class.getName();
  private static Activity me = null;
  private static Handler.Callback callback = null;
  private boolean pendingCardRequested = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_test_wallet);

        Log.d(TAG, "onCreate()");

        me = this;
        pendingCardRequested = true;

        Button button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            sendResponse(true);
            killMe();
          }
        });

        Button button2 = (Button)findViewById(R.id.button2);
        button2.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            sendResponse(false);
            killMe();
          }
        });
    }

    @Override
    public void onResume() {
      super.onResume();
      Log.d(TAG, "onResume()");
      Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
      v.vibrate(500);
    }

    @Override
    public void onPause() {
      super.onResume();
      Log.d(TAG, "onPause()");
      if(pendingCardRequested) {
        sendResponse(false);
        killMe();
      }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      menu.add(0, 0, Menu.FIRST, "Exit");
      return true;
    }

    private void sendResponse(boolean isOK) {
      String action = me.getIntent().getAction();
      Log.d(TAG, "onClick() isOK=" + isOK + " action=" + action);
      CardResponse response = new CardResponse(CardResponse.STATE_ERROR, "ERR", "No token");
      CardRequest request = null;
      Bundle bundle = me.getIntent().getBundleExtra("CardRequest");
      if (bundle != null) {
        request = new CardRequest(bundle);
      }

      if(INTENT_GET_TOKEN.equals(action) && request != null && isOK) {
        response = new CardResponse(CardResponse.STATE_OK, "OK", "Token");
      } else {
        response = new CardResponse(CardResponse.STATE_OK, "ERR", "No token");
      }

      if (response != null) {
        Log.d(TAG, "onClick() send result : ERR");
        sendMessage(WalletEngine.CODE_WALLET_GETTOKEN, "CardResponse", response.toBundle());
        pendingCardRequested = false;
      }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
      case 0:
        if(pendingCardRequested) {
          sendResponse(false);
        }
        killMe();
        return true;
      default:
        return false;
      }
    }


    private void sendMessage(int code, String key, Bundle bundle) {
      if(WalletTestWalletActivity.callback != null) {
        Bundle msgBundle = new Bundle();
        msgBundle.putBundle(key, bundle);

        Message message = new Message();
        message.what = Activity.RESULT_OK;
        message.arg1 = code;
        message.setData(msgBundle);

        WalletTestWalletActivity.callback.handleMessage(message);
      }
    }

    protected static void killMe() {
      me.finish();
//      android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static void setCallback (Handler.Callback callback) {
      WalletTestWalletActivity.callback = callback;
    }
}
