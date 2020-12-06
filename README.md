# BioGames: A Behavioral Biometrics Collection Tool for Mobile Devices

If you simply want to try the app then download the BioGamesApp.apk and install it on your mobile device. If you want to create your database to collect your research data follow the instructions below. 

# 1	Introduction
This document contains and defines the requirements, specifications and how the Android application called "BioGames" has been developed. The purpose of this document is to present the use of the application, so that it can be studied and extended by others.
BioGames is an Android application for controlling sensor values by users, including accelerometer measurements, gyroscope, touch gestures and keystroke dynamics.
The two main objectives of this application are:

• The recording of sensor values, touch gestures and keystroke dynamics.
• The online storage of values in a database.

# 1.1 Scope
Initially, the application collects the values of the accelerometer and gyroscope sensors, as well as the touch gestures and keystrokes. 
The online database is designed in such a way that multiple users can store their sensor data at any time. Thus, there is no concern about data separation and synchronization. The application covers all the possibilities of malfunction of the multi-communication with the base.
Also, the application is completely secure as it does not receive any personal information from users while it automatically informs them about the access to the available resources of each device during installation and execution.
The API is built to upload the given data from the application and to store it in the online MySQL database. The API data transmission is built on key elements and therefore is not encrypted.

# 1.2	Assumptions
It is assumed that the user has an internet connection and he/she has given the required access permission to the application which is requested after the first boot. It is also assumed that there is no need to encrypt transfers between the application and the API.

# 1.3	Application environment
The "BioGames" application communicates directly with an onlin database system. The online database uses MySQL system. The application sends the data to the API which then takes over the storage of the data in the database.

# 1.4	Database and API installation
We create the database on mysql server (e.g. with phpmyadmin).In the folder “For Data Base” you will find the DataBase creation.sql file.
For reasons of convenience you can also do this by importing the creation.sql file which has the appropriate sql commands to create the table.
Following, you create a user.

# 1.5	API in the folder public_html 

In the folder “For Data Base” you will find the folder public_html where there is the code of the API. We put the files of the folder public_html in the folder public_html of the server. The connection file connect.php, has the connection commands with the database. You must insert your credentials in the following fields, as shown below: $db_name="Your db name";
$mysql_user="Your user name";
$mysql_pass="Your password";



# 1.6	Application code
You can download the code or clone it. Run the code in Android Studio and create an APK with which you can install the application on a mobile device.

# 1.7	Contributions
A software system must be sustainable and scalable. Knowing the types of expected changes, the structure of the application is such that it significantly enhances the creation of an architecture that could accommodate all possible types of expected changes.
The project was developed in Android Studio. To use the source code of the program, as well as for compiling and redistributing, developers who may be extending the code should use the latest version of Android Studio with the latest version of the Android SDK installed.


# Cite

Ioannis Stylios, Spyros Kokolakis, Andreas Skalkos, Sotirios Chatzis. BioGames: A Behavioral Biometrics Collection Tool for Mobile Devices. 
