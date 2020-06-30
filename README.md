# Pocket friend Appliaction
Our app has been designed with the purpose of providing quick information to the user.
It has been named “Pocket Friend” and is able to give information about actuality through real-time news, weather, and important places around.
The functionalities that we developed are described below.
This application done as a project assignment for the course (Application development for android "DA401A") at Malmö University.

<img src="https://user-images.githubusercontent.com/26624976/86140414-20e53b80-baf1-11ea-8544-2da9ef436c05.png" width="400" height="800" />


### Latest News
The Latest News part of the app shows a list of the breaking news around the world to stay up to date.

<img src="https://user-images.githubusercontent.com/26624976/86140522-3b1f1980-baf1-11ea-91b3-b8b02961f995.png" width="400" height="800" />

The app also has additional features to filter the news list to a specific country (Sweden, USA, UK, etc..) or specify the news source among the biggest news publishers world wide ( BBC, Aljazeera, Washington Post, Reuters, etc..).
You can at any time turn off the filter to show the latest news around the world.

The latest news part of the app using three different fragments. The main news fragment that contains a list of the articles, news filter fragment that let users select the filter, and the fragment of the article that shows the articles briefly and provides a link to the publisher website.
The news controller handles the functionality of this application part and holds the function logic.
It switches between the fragments and additionally, it’s responsible for the connection to the API to fetch articles asynchronously using a link and API-key as the API specification and post them to the (SharedViewModel) that I used to hold the data while using the application.
The articles list observes the ViewModel and updates every time changes occur in the ViewModel.
I used the ViewModel also to manage and provide the data to the UI elements and to handle the configuration changes such as the device rotation.

<img src="https://user-images.githubusercontent.com/26624976/86140623-568a2480-baf1-11ea-92a4-45fe9c2e093e.png" width="400" height="800" /> <img src="https://user-images.githubusercontent.com/26624976/86140637-5b4ed880-baf1-11ea-9f43-3780d6cca7f7.png" width="400" height="800" />

### Weather
The weather system provides a way to get information about weather very quickly. When using the feature for the first time it will ask you to give the permission of using your location.

<img src="https://user-images.githubusercontent.com/26624976/86141130-fcd62a00-baf1-11ea-961d-edfaa415bc62.png" width="400" height="800" />

The weather page is set by default with the current location of the user, but offers the user the option to look for any city he wants, by typing the name in the field and clicking the button “Search”.
Also, there is a button to allow you to find back the weather for your location.
The weather part takes the form of a fragment, with one layout for portrait, and another one for landscape.
It is handled by a controller, and uses a model class to store all the data of the weather information provided by the used API.
The controller is in charge of showing the fragment to the screen, gathering information from the API in an asynchronous way, and updating the view according to the received data.
A loader is displayed during the process, to show the user something is running, until the information is received and displayed.

### Map
The Map displays the nearby location such as hospital, restaurant and school.

<img src="https://user-images.githubusercontent.com/26624976/86141147-0069b100-baf2-11ea-94e3-129d3d523021.png" width="400" height="800" />

For the first time, the user needs to allow the google map to access their device location, then map shows users current Location.
By clicking on the hospital button map shows the user nearby hospitals. Similarly on clicking restaurant or school buttons the map shows nearby restaurants or schools accordingly.
If the user is looking for a specific city, the user can type the place name in the search input field and click the Search button. The map camera moves to the location searched.
Clicking on the restaurant button shows the nearby restaurant according to the location searched.
The MapActivity displays the google map. DownloadUrl is a class which is used to retrieve data from URL using HttpURLConnection and File handling methods. After retrieving data it passes to the OnPostExecute and the data is in the form of Json and needs to be parsed. After parse, the result (nearby places)is displayed on the google map.
<img src="https://user-images.githubusercontent.com/26624976/86141168-0495ce80-baf2-11ea-8895-5048ceac08c5.png" width="400" height="800" /> <img src="https://user-images.githubusercontent.com/26624976/86141175-0790bf00-baf2-11ea-8ec0-3c7a0f45ae21.png" width="400" height="800" />

#### Used API’s :
- [NewsAPI](https://newsapi.org/)
- [OpenWeatherMap](https://openweathermap.org/api)
- [Google Maps API](https://developers.google.com/maps/documentation/android-sdk/intro)
