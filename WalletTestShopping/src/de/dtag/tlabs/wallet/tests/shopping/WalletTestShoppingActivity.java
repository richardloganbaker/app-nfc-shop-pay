package de.dtag.tlabs.wallet.tests.shopping;

import de.dtag.tlabs.wallet.extensions.shopping.api.Answer;
import de.dtag.tlabs.wallet.extensions.shopping.api.Item;
import de.dtag.tlabs.wallet.extensions.shopping.api.Merchant;
import de.dtag.tlabs.wallet.extensions.shopping.api.ShopEngine;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler.Callback;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class WalletTestShoppingActivity extends Activity {
  private final static String TAG = WalletTestShoppingActivity.class.getName();
  private static Activity me = null;
  private static ShopEngine engine = null;
  private static String button = "";
  private static TextView textView1 = null;

  private Callback resultHandler = new Callback() {

    @Override
    public boolean handleMessage(Message msg) {
      Log.d(TAG, "handleMessage() 1");

      Answer answer = null;
      int what = msg.what;
      Log.d(TAG, "handleMessage() what=" + what);
      if(Activity.RESULT_OK == what) {
        Log.d(TAG, "handleMessage() what=RESULT_OK");
        answer = new Answer(msg);
        String text = "Button: " + button + "\nState: " + answer.state + "\nMessage: " + answer.message;
        Log.d(TAG, "handleMessage() " + text);
        TextView textView1 = (TextView)findViewById(R.id.textView1);
        textView1.setText(text);
      }
      Log.d(TAG, "handleMessage() 2");
      return false;
    }
  };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_test_shopping);

        Log.d(TAG, "onCreate()");

        me = this;
        textView1 = (TextView)findViewById(R.id.textView1);

       Button button1 = (Button)findViewById(R.id.button1);
       button1.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
           button = (String) ((Button)v).getText();
           textView1.setText(button + " : ???");
           engine.openShop(new Merchant("knownMerchantID", "knownMerchantToken"));
         }
       });

      Button button11 = (Button)findViewById(R.id.button11);
      button11.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          button = (String) ((Button)v).getText();
          textView1.setText(button + " : ???");
          engine.openShop(new Merchant("unknownMerchantID", "unknownMerchantToken"));
        }
      });

      Button button2 = (Button)findViewById(R.id.button2);
      button2.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          button = (String) ((Button)v).getText();
          textView1.setText(button + " : ???");
          engine.addItem(new Item("myProductID", "myProductName", "myProductDescription", Item.CURRENCY_USD, 12345, 99));
        }
      });

     Button button3 = (Button)findViewById(R.id.button3);
     button3.setOnClickListener(new OnClickListener() {
       @Override
       public void onClick(View v) {
         button = (String) ((Button)v).getText();
         textView1.setText(button + " : ???");
         engine.removeItem("myProductID");
       }
     });

    Button button33 = (Button)findViewById(R.id.button33);
    button33.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        button = (String) ((Button)v).getText();
        textView1.setText(button + " : ???");
        engine.removeItem("myProductID123");
      }
    });

    Button button4 = (Button)findViewById(R.id.button4);
    button4.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        button = (String) ((Button)v).getText();
        textView1.setText(button + " : ???");
        engine.checkout();
      }
    });

   Button button5 = (Button)findViewById(R.id.button5);
   button5.setOnClickListener(new OnClickListener() {
     @Override
     public void onClick(View v) {
       button = (String) ((Button)v).getText();
       textView1.setText(button + " : ???");
       engine.release();
     }
   });
    }

    @Override
    public void onResume() {
      super.onResume();
      Log.d(TAG, "onResume()");
      if(engine == null) {
        engine = new ShopEngine(me, resultHandler);
        Log.d(TAG, "onResume() : ShopEngine bound");
      }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      menu.add(0, 0, Menu.FIRST, "Exit");
      return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
      case 0:
        killMe();
        return true;
      default:
        return false;
      }
    }

    protected static void killMe() {
      me.finish();
      android.os.Process.killProcess(android.os.Process.myPid());
    }
}
