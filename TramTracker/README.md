# README #

Tram Tracker Android app for REA coding challenge

### Build Instructions ###

The project should build as normal from within Android Studio.

### Command to Run unit/UI tests from CLI ###
./gradlew connectedAndroidTest

### Libraries and Architecture ###

I tried to use fairly a fairly standard set of libraries for this app:

* Hilt for the dependency injection
* Retrofit/OKHttp for API calls
* Moshi for the JSON de/serialisation
* Coroutines for the async calls
* Mockito for unit tests
* Espresso for UI tests
* ViewModel to store and manage UI-related data in a lifecycle conscious way
* LiveData for lifecycle-aware data holder

Architecture is also pretty standard, Activity -> ViewModel -> Repository -> API.

### Assumptions Made ###
My general assumption for how to implement the wireframe which has vague requirement for interview purpose. I have added Route No and Destination along with Direction in the header tile in recyclerview so that it is easy
for the user to understand the arrival times bounded for which destination. I assume that all entries in route details response has same destination (Tested api in postman and seen destination is same for all the
response objects). Since there is no requirement on CLEAR button click, I have hidden the recyclerview. Additionally, added progressbar to show user that API call is made behind the scene when LOAD button is clicked.
To make this app usable for all REA's office and to improve user experience, one more screen that takes route No and Stop Ids have to be designed (I have not designed user input screen due to time constraint).

### Final Thoughts ###

Overall I found this a pretty fun exercise. On the surface it seems like quite an easy and straight forward problem, but once I dived deeper into the proviced data I found that there were some tough decisions to make.
If I had more time, perhaps it would've been fun to develop additional screen for user input on stop ids and route No.