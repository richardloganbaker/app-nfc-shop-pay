package de.dtag.tlabs.wallet.tests.wallet.api;

import android.os.Bundle;

public class CardResponse {
  public static final int STATE_UNKNOWN = 0;
  public static final int STATE_OK = 1;
  public static final int STATE_ERROR = -1;

  public int state = STATE_UNKNOWN;
  public String message = "";
  public String token = "";

  private CardResponse() {
    init(STATE_UNKNOWN, "", "");
  }

  public CardResponse(Bundle bundle) {
    if(bundle != null) {
      init(bundle.getInt("state"),
          bundle.getString("message"),
          bundle.getString("token"));
    }
    else
    {
      init(STATE_UNKNOWN, "", "");
    }
  }

  public CardResponse(int state, String message, String token) {
    init(state, message, token);
  }

  private void init(int state, String message, String token) {
    this.state = state;
    this.message = message;
    this.token = token;
  }

  public Bundle toBundle() {
    Bundle bundle = new Bundle();
    bundle.putInt("state", state);
    bundle.putString("message", message);
    bundle.putString("token", token);
    return bundle;
  }
}
