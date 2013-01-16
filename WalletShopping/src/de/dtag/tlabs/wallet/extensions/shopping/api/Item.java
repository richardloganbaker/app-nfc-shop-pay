/*
 * Shops item description object
 */
package de.dtag.tlabs.wallet.extensions.shopping.api;

import android.os.Bundle;

// TODO: Auto-generated Javadoc
/**
 * The Class Item; Shop item description object.
 */
public class Item {

  /** The Constant CURRENCY_EUR. */
  public static final String CURRENCY_EUR = "EUR";
  /** The Constant CURRENCY_USD. */
  public static final String CURRENCY_USD = "USD";

  /** The product id. */
  public String productID = "";
  /** The product name. */
  public String productName = "";
  /** The product description. */
  public String productDescription = "";
  /** The currency; see Item.CURRENCY_... */
  public String currency = "";
  /** The price : the price; in e.g. euro cent */
  public long price = 0;
  /** The count. */
  public int count = 0;

  private Item() {
    init("", "", "", "", 0, 0);
  }

  /**
   * Instantiates a new item.
   *
   * @param bundle : the bundle for unmarshalling to an item
   */
  public Item(Bundle bundle) {
    if(bundle != null) {
      init(bundle.getString("productID"),
          bundle.getString("productName"),
          bundle.getString("productDescription"),
          bundle.getString("currency"),
          bundle.getLong("price"),
          bundle.getInt("count"));
    }
    else
    {
      init("", "", "", "", 0, 0);
    }
  }

  /**
   * Instantiates a new item.
   *
   * @param productID : the product id
   * @param productName : the product name
   * @param productDescription : the product description
   * @param currency : the currency; see Item.CURRENCY_...
   * @param price : the price; in e.g. euro cent
   * @param count : the count
   */
  public Item(String productID, String productName, String productDescription, String currency, long price, int count) {
    init(productID, productName, productDescription, currency, price, count);
  }

  private void init(String productID, String productName, String productDescription, String currency, long price, int count) {
    this.productID = productID;
    this.productName = productName;
    this.productDescription = productDescription;
    this.currency = currency;
    this.price = price;
    this.count = count;
  }

  /**
   * To bundle.
   *
   * @return the item marshalled to a bundle
   */
  public Bundle toBundle() {
    Bundle bundle = new Bundle();
    bundle.putString("productID", productID);
    bundle.putString("productName", productName);
    bundle.putString("productDescription", productDescription);
    bundle.putString("currency", currency);
    bundle.putLong("price", price);
    bundle.putInt("count", count);
    return bundle;
  }
}
