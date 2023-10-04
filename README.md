# DiscordSpammer
Android application to spam in discord servers.

Minimal android version for this app to launch is `10`

# Features
1. Choose any channel in any server.
2. Length of spam messages is 2000 characters.
3. Specify how much messages you want to send.
4. Save token so you don't have to type it over and over again.
5. Runs in background.
6. Progress in notification.

# How to get my token?
1. Open discord in web browser.
2. Open dev tools (depends on your browser)
3. Open any channel in any server.
4. Find something like `messages?limit=50` request.
5. Click it and find `Authorization` in `Request Headers`
6. Copy this token and use it in app.

This token will revoke if you logout in browser you copied it from.<br>
So if you ever feel unsafe about this app using your token logout from this browser.
