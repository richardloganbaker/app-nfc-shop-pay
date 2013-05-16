app-nfc-shop-pay
================

This is the NFC Shop & Pay application as developed by AmbieSense on behalf of Webinos.

This is a prototype application that is meant for showcasing Webinos' abilities to 
use it's NFC API's and demonstrate them in real world use cases.

Currently the platform only allows the application to be run in a browser whilst 
pointing to your NFC Enabled mobile's NFC Reader via forwarding. Setup is as follows.

1) Download the application here.

2) Connect your NFC Enabled phone to the PC/Laptop via USB Cable

3) Install the latest Android distro of Webinos on your phone

4) From a command prompt, type in the following lines:

adb forward tcp:8080 tcp:8080

adb forward tcp:8081 tcp:8081

5) Run the 'index.html' file in a browser window.

6) Click on the central button in the middle of the screen.

7) Pass an NFC tag in front of the scanner.

8) Watch the URL embedded in the tag redirect the page in your browser!

9) To scan another tag, simply press the back button and refresh the screen.

We plan on making significant improvements to this model. Keep checking back for updates.
