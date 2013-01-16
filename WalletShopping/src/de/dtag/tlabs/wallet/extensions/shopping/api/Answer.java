/*
 * Shop functions answer object
 */
package de.dtag.tlabs.wallet.extensions.shopping.api;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

// TODO: Auto-generated Javadoc
/**
 * The Class Answer; Shop function answer object.
 */
public class Answer {

  /** The Constant STATE_UNKNOWN. */
  public static final int STATE_UNKNOWN = 0;
  /** The Constant STATE_OK. */
  public static final int STATE_OK = 1;
  /** The Constant STATE_CHECKOUT_SUCCESSFUL. */
  public static final int STATE_CHECKOUT_SUCCESSFUL = 901;
  /** The Constant STATE_CHECKOUT_FAILED. */
  public static final int STATE_CHECKOUT_FAILED = -901;
  /** The Constant STATE_CHECKOUT_NO_ITEMS. */
  public static final int STATE_CHECKOUT_NO_ITEMS = -902;
  /** The Constant STATE_ERROR. */
  public static final int STATE_ERROR = -1;
  /** The Constant STATE_ERROR_NO_SHOP. */
  public static final int STATE_ERROR_NO_SHOP = -101;
  /** The Constant STATE_ERROR_UNKNOWN_SHOP. */
  public static final int STATE_ERROR_UNKNOWN_SHOP = -102;
  /** The Constant STATE_ERROR_SHOP_ALLREADY_OPENED. */
  public static final int STATE_ERROR_SHOP_ALLREADY_OPENED = -103;
  /** The Constant STATE_ERROR_UNKNOWN_ITEM. */
  public static final int STATE_ERROR_NO_ITEMS = -201;
  /** The Constant STATE_ERROR_NO_ITEMS. */
  public static final int STATE_ERROR_UNKNOWN_ITEM = -202;

  /** The state; see Answer.STATE_... */
  public int state = STATE_UNKNOWN;
  /** The message. */
  public String message = "";

  private Answer() {
    init(STATE_UNKNOWN, "");
  }

  /**
   * Instantiates a new answer.
   *
   * @param message : the message for unmarshalling to an answer
   */
  public Answer(Message message) {
    if(Activity.RESULT_OK == message.what) {
      Bundle bundle = message.getData().getBundle("Answer");
      if (bundle != null) {
        init(bundle.getInt("state"),
            bundle.getString("message"));
      }
    }
  }

  /**
   * Instantiates a new answer.
   *
   * @param bundle : the bundle for unmarshalling to an answer
   */
  public Answer(Bundle bundle) {
    if(bundle != null) {
      init(bundle.getInt("state"),
          bundle.getString("message"));
    }
    else
    {
      init(STATE_UNKNOWN, "");
    }
  }

  /**
   * Instantiates a new answer.
   *
   * @param state : the answer state; see Answer.STATE_...
   * @param message : the answer message
   */
  public Answer(int state, String message) {
    init(state, message);
  }

  private void init(int state, String message) {
    this.state = state;
    this.message = message;
  }

  /**
   * To bundle.
   *
   * @return the answer marshalled to a bundle
   */
  public Bundle toBundle() {
    Bundle bundle = new Bundle();
    bundle.putInt("state", state);
    bundle.putString("message", message);
    return bundle;
  }
}
