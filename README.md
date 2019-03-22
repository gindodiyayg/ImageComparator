# ImageComparator
Android Application:ImageComparator

•	Problem Definition:- To design an Android application to snap a photograph and find similar set of images from the gallery/folder.

•	Problem Description:- The basic design of the Android application consists of the following features:
1.	Clicking an image by accessing the phone’s camera.
2.	[Alternative to pt. 1]  Selecting an image from phone gallery/folder.
3.	Comparing the clicked/selected image with all the images in the phone gallery/folder by accessing it.
4.	Display result (i.e. whether image is similar/same or not).

•	Technology Stack: -     Android (ver. 7.0).
-	IDE used: Android Studio.

•	Logic Used:- 
1.	For comparison of images:-  Longest Common Subsequence (LCS) was used.
2.	The images are converted to bitmap arrays, which are then compared.
3.	To ensure accuracy in comparison, the bitmap array of the image is divided into 9 regions and the each of these regions are compared with the corresponding regions of the other image.

•	Description of Role of Each Functionality:-
The application consists of one activity, i.e. MainActivity, which consists of an XML part as activity_main.xml and MainActivity.java.

1.	activity_main.xml:-
This contains the code for the XML part of the application, describing the user interface (UI) of the app.
It contains the code describing the structure and positioning of all the components in the app such as buttons, views etc.

2.	MainActivity.java:-
This contains the back-end part of the application, defining the logics behind each component.
It consists of the following functions:
a)	checkIfSame():- to check if the images to be compared are completely same or not (i.e. threshold=100%).
b)	checkIfSimilar():- to check if images to be compared are similar by a threshold value (70%).
•	The AndroidManifest.xml file includes seeking the permissions to access:
a)	Camera
b)	Gallery
c)	External Storage (read and write)
d)	Media content

