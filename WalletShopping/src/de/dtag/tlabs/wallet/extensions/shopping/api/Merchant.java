/*
 * Shops merchant description object
 */
package de.dtag.tlabs.wallet.extensions.shopping.api;

import android.os.Bundle;

// TODO: Auto-generated Javadoc
/**
 * The Class Merchant;  Shop merchant description object.
 */
public class Merchant {

  /** The merchant id. */
  public String merchantID = "";
  /** The merchant authentication token. */
  public String merchantToken = "";

  private Merchant() {
  }

  /**
   * Instantiates a new merchant.
   *
   * @param bundle : the bundle for unmarshalling to an merchant
   */
  public Merchant(Bundle bundle) {
    if(bundle != null) {
      init(bundle.getString("merchantID"),
          bundle.getString("merchantToken"));
    }
    else
    {
      init("", "");
    }
  }

  /**
   * Instantiates a new merchant.
   *
   * @param merchantID : the merchant id
   * @param merchantToken : the merchant authentication token
   */
  public Merchant(String merchantID, String merchantToken) {
    init(merchantID, merchantToken);
  }

  private void init(String merchantID, String merchantToken) {
    this.merchantID = merchantID;
    this.merchantToken = merchantToken;
  }

  /**
   * To bundle.
   *
   * @return the merchant marshalled to a bundle
   */
  public Bundle toBundle() {
    Bundle bundle = new Bundle();
    bundle.putString("merchantID", merchantID);
    bundle.putString("merchantToken", merchantToken);
    return bundle;
  }
}
