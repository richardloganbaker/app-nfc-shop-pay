package de.dtag.tlabs.wallet.tests.wallet.api;

import android.os.Bundle;

public class CardRequest {
  public static final String CURRENCY_EUR = "EUR";
  public static final String CURRENCY_USD = "USD";

  public String shopID = "";
  public String shopToken = "";
  public String requestDescription = "";
  public String tokenRequirements = "";

  private CardRequest() {
  }

  public CardRequest(Bundle bundle) {
    if(bundle != null) {
      init(bundle.getString("shopID"),
          bundle.getString("shopToken"),
          bundle.getString("requestDescription"),
          bundle.getString("tokenRequirements"));
    }
    else
    {
      init("", "", "", "");
    }
  }

  public CardRequest(String shopID, String shopToken, String requestDescription, String tokenRequirements) {
    init(shopID, shopToken, requestDescription, tokenRequirements);
  }

  private void init(String shopID, String shopToken, String requestDescription, String tokenRequirements) {
    this.shopID = shopID;
    this.shopToken = shopToken;
    this.requestDescription = requestDescription;
    this.tokenRequirements = tokenRequirements;
  }

  public Bundle toBundle() {
    Bundle bundle = new Bundle();
    bundle.putString("shopID", shopID);
    bundle.putString("shopToken", shopToken);
    bundle.putString("requestDescription", requestDescription);
    bundle.putString("tokenRequirements", tokenRequirements);
    return bundle;
  }
}
