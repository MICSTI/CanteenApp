# CanteenApp
This is a project for the Mobile App Development course of ITM13

# Features
- Check out the weekly Mensa meal plan
- You can add your favourite dish to the 'Favourites'
- Isn't it great to know, when it's time for your favourite dish? The built-in notifications will never let you miss it!

# Structure
- the app consists of a MainActivity that holds three fragments:
  - **LocationFragment** - fetches all available Mensa locations and displays them for selection
  - **CanteenDetailFragment** - displays the current meal schedule for a Mensa location, all favourite meals are highlighted
  - **FavouriteMealFragment** - a list containing all favourite meals. Meals can be added or removed from this list
- on starting the MainActivity, **FavouriteMealService** is started automatically that runs indefinitely. Once a day it checks if a favourite meal is served on this day and issues a notification with information about the favourite meal. Thanks to this, the users don't have to check the app every day. The time when the notification will be issued can be set in Config.java.
- all files that perform supporting tasks which are used in multiple locations are located in the **handler** or **util** package.

# Dependencies
- for parsing HTML web pages, the app uses [jsoup](http://jsoup.org/), version 1.8.3. The according .jar file is located in the **vendor** package.

# Compilation
- the app was compiled with *Android SDK version 23*, the build tools version was 21.1.2.
- the app's minimal SDK version is 17, its target version is 21
- all further compilation details can be found in the **build.gradle** file in the project's root directory

# Permissions
- to run the app with its full potential, it requires the following permissions:
  - *ACCESS_NETWORK_STATE*, for determining if it is possible to update the meal schedule and the list of Mensa locations
  - *INTERNET*, for updating the meal schedule and the list of Mensa locations through HTTP requests
