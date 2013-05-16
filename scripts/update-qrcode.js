
  // Auto generate qr code for the current web page
  // This happens at when the page is being loaded.

  //window.onload = update_qrcode;


var draw_qrcode = function(text, typeNumber, errorCorrectLevel) {
	document.write(create_qrcode(text, typeNumber, errorCorrectLevel) );
};

var create_qrcode = function(text, typeNumber, errorCorrectLevel, table) {

	var qr = qrcode(typeNumber || 4, errorCorrectLevel || 'M');
	qr.addData(text);
	qr.make();
	return qr.createImgTag();
};

var update_qrcode = function() {

	var text = location.href;
	document.getElementById('qr').innerHTML = create_qrcode(text);
};
