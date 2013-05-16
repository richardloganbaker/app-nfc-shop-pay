/* shop.js */

var shop = {
  purchases: [],

  on_start: function ()
  {
    var button, name, price;

    name = this.element("name");

    if (name)
    {
      this.name = (name.innerHTML).replace(/^\s+|\s+$/g, '');
      price = this.element("price");
      this.price = parseFloat(price.innerHTML);

      button = this.element("add");
      button.addEventListener("click", function() { shop.on_add(); }, false);

      button = this.element("remove");
      button.addEventListener("click", function() { shop.on_remove(); }, false);
    }

    button = this.element("clear");

    if (button)
      button.addEventListener("click", function() { shop.on_clear(); }, false);

    button = this.element("buy");

    if (button)
      button.addEventListener("click", function() { shop.on_buy(); }, false);

    var basket = this.get_cookie("basket");

    if (basket)
      this.purchases = JSON.parse(basket);

    this.show_basket();
  },

  define_product: function (name, price)
  {
    this.products[this.products.length] = { "name" : name, "price" : price};
  },

  show_basket: function ()
  {
    var basket = this.element("basket");
    var content = "", i, item, total = 0;

    for (i = 0; i < this.purchases.length; ++i)
    {
      item = this.purchases[i];
      content += item.name + " &mdash; " + item.price + "€<br />\n";
      total += item.price;
    }

    basket.innerHTML = content;

    var heading = this.element("total");

    if (heading)
      heading.innerHTML = "Total: " + (total > 0 ? total + "€" : "");
  },

  get_item: function (name)
  {
    for (i = 0; i < this.purchases.length; ++i)
    {
      item = this.purchases[i];

      if (item.name == name)
        return item;
    }

    return null;
    for (i = 0; i < this.purchases.length; ++i)
    {
      item = this.purchases[i];

      if (item.name == name)
        return item;
    }

    return null;
  },

  on_add: function (e)
  {
    var added = document.getElementById("added");
    added.innerHTML = "Added";
    added.setAttribute("class", "product-added-on");

    var item = this.get_item(this.name);

    if (!item)
    {
      this.purchases[this.purchases.length] = { "name" : this.name, "price" : this.price};
      this.set_cookie("basket", JSON.stringify(this.purchases), 5);
      this.show_basket();
    }

  },

  on_remove: function (e)
  {
    var added = document.getElementById("added");
    added.innerHTML = "Removed";
    added.setAttribute("class", "product-added-on");


    var i;

    for (var i = j = 0; i < this.purchases.length; ++i)
    {
      item = this.purchases[i];

      if (item.name == this.name)
      {
        this.purchases.splice(i, 1);
        this.set_cookie("basket", JSON.stringify(this.purchases), 5);
        this.show_basket();
        break;
      }
    }

  },

  on_clear: function ()
  {
    this.index = 0;
    this.purchases = [];
    this.delete_cookie("basket");
    this.show_basket();
  },

  on_buy: function (e)
  {
    shop.get_pin();
  },

  on_bought: function (e)
  {
    var total = 0, i, item;

    for (i = 0; i < this.purchases.length; ++i)
    {
      item = this.purchases[i];
      total += item.price;
    }

    //alert("Total cost: " + total + "€");
    var button = this.element("buy");
    //var div = button.parentNode.parentNode;

    //div.innerHTML = "<h2>Transaction complete: " +
    // total + "€</h2>";

    var div1 = document.getElementById("checkout-title");
    div1.innerHTML = "Shopping completed";

    var div3 = document.getElementById("total");
    div3.setAttribute("class", "shopping-total-hidden");

    var div2 = document.getElementById("shopping-items");
    div2.innerHTML = "Payment successful. Please take your mobile and item(s) with you.";

    var button = document.getElementById("home-button");
    button.setAttribute("class", "home");

    button = document.getElementById("clear");
    button.setAttribute("class", "empty_cart_hidden");

    button = document.getElementById("buy");
    button.setAttribute("class", "check_out_hidden");

    button = document.getElementById("restart");
    button.setAttribute("class", "restart_shopping");


    button = document.getElementById("disclaimer");
    button.setAttribute("class", "disclaimer");

    this.on_clear();
  },

  get_pin: function (e)
  {

    var div = document.createElement("div");

    var keypad = this.create_keypad();
    div.appendChild(keypad);


    div.setAttribute("id", "pin-dialog");

    document.body.appendChild(div);

    var button = document.getElementById("home-button");
    button.setAttribute("class", "home-hidden");

  },

  create_keypad: function ()
  {
    var div = document.createElement("div");
    div.setAttribute("id", "keypad");
    div.setAttribute("class", "pincode");

    var keys = "1234567890";

    for (i = 0; i < keys.length; ++i)
    {
      key = document.createElement("button");
      key.setAttribute("onclick", "shop.pin_key('" + keys[i] + "')");

      key.className = "pin" + keys[i];

      key.textContent = keys[i];
      div.appendChild(key);

    }

    var button = document.createElement("button");
    button.innerHTML = "Okay";
    button.setAttribute("onclick", "shop.accept_pin()");
    button.setAttribute("class", "ok");

    div.appendChild(button);

    button = document.createElement("button");
    button.innerHTML = "Clear";
    button.setAttribute("onclick", "shop.pin_key_clear()");
    button.setAttribute("class", "pin-clear");

    div.appendChild(button);

    button = document.createElement("button");
    button.innerHTML = "Cancel";
    button.setAttribute("onclick", "shop.cancel_pin()");
    button.setAttribute("class", "cancel");

    div.appendChild(button);


    var notice = document.createElement("div");
    notice.innerHTML = "Enter pin";
    notice.setAttribute("class", "pin-notice");

    div.appendChild(notice);

    var pinDisplay = document.createElement("input");
    pinDisplay.setAttribute("class", "pin-display");
    pinDisplay.setAttribute("id", "pin");
    pinDisplay.setAttribute("name", "pin");
    pinDisplay.setAttribute("type", "password");

    div.appendChild(pinDisplay);

    return div;
  },

  pin_key: function (string)
  {
    var pin = document.getElementById('pin');
    pin.value += string;
  },

  pin_key_clear: function ()
  {
    var pin = document.getElementById('pin');
    pin.value = "";
  },


  accept_pin: function ()
  {

    var dialog = document.getElementById("pin-dialog");
    document.body.removeChild(dialog);
    shop.on_bought();

  },

  cancel_pin: function ()
  {
    var dialog = document.getElementById("pin-dialog");
    document.body.removeChild(dialog);

    var button = document.getElementById("home-button");
    button.setAttribute("class", "home");

  },

  // set persistent cookie for 'expires' minutes from now
  set_cookie: function(name, value, expires, path, domain, secure)
  {
    var now = new Date();

    // map expires value from minutes to milliseconds
    if (expires)
      expires = expires * 60 *1000;

    var expires_date = new Date(now.getTime() + expires);

    document.cookie = name + "=" + escape(value) +
      (expires ? ";expires=" + expires_date.toGMTString() : "") +
      (path ? ";path=" + path : "") +
      (domain ? ";domain=" + domain : "") +
      (secure ? ";secure" : "");
  },

  get_cookie: function (name)
  {
    // get ';' separated sequence of name=value pairs
    // since several cookies have apply to this origin
    var cookies = document.cookie.split(';');
    var cookie, cookie_name;

    for ( i = 0; i < cookies.length; i++ )
    {
      // split name=value pair
      cookie = cookies[i].split( '=' );

      // trim leading/trailing whitespace
      cookie_name = cookie[0].replace(/^\s+|\s+$/g, '');

      // check for desired cookie name
      if (cookie_name == name)
      {
        // unescape and trim leading/trailing whitespace
        if (cookie.length > 1)
          return unescape(cookie[1].replace(/^\s+|\s+$/g, ''));

        return null;  // in case value is missing
      }
    }

    return null;
  },

  delete_cookie: function( name, path, domain )
  {
    if (this.get_cookie(name))
      document.cookie = name + "=" +
        (path ? ";path=" + path : "") +
        (domain ? ";domain=" + domain : "") +
        ";expires=Thu, 01-Jan-1970 00:00:01 GMT";
  },

  element: function (id)
  {
    return document.getElementById(id);
  },
};

window.addEventListener("load", function() { shop.on_start(); }, false); 








function openPage(url)
{
    window.open(url,'_self',false);
}


function openHome()
{
    window.open('index.html','_self',false);
}