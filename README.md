## JojosApp (Instant Messaging App for Android)
The JojosApp application has been designed with the Android Studio tool focused on Android mobile devices, also making use of MongoDB as a database. The design is based on offering instant messaging services. The application allows us to have private chats with another user, create group chats with several users at the same time, we can also share locations or even create our own contact list with the users of the application, among other things. The application initially supports two languages, English and Spanish.

It is necessary to make an integration between the application and the database. Because direct communication between these would be more complex, and also because the mobile device would need more resources, we are going to use an intermediate REST API that serves as a communicator between the two elements, implemented with Node.js and Express. Within this API we create the necessary methods, which we will call from the code executed on the mobile device. These functions are in charge of communicating with the database, obtaining the necessary resources and finally sending the information to the device, abstracting it from the queries.

### Services offered by the application.
Next, we are going to indicate in more detail the main services provided by the application, with special emphasis on the security used.

- Registration and login form from the application: Username, password and email for registration. Only username and password for login. 
- Having made the login, we can send messages to other users: Messages can be plain text, indicating that you have been introduced to a group or locations.
- Both private chats (only between two users) and group chats (more than two users) are allowed.
- The messages indicate the date and time of sending, together with the name of the sender if it is a group chat: The messages will appear in chronological order according to the moment they were sent.
- The application notifies when a new message is received, indicating the message received and the user who sent it.
- Apart from text messages, we can receive messages with a specific location or messages indicating that we have been entered into a group.
- You can open and observe the locations on a map.
- Each user can create her own contact agenda, adding the users she deems appropriate.
- The application also offers persistence, that is, the application can be closed and the information is kept within the application. The information is only deleted if the session is closed from the application itself or the account is deleted: There is persistence both in your address book and in all chats.
- Communications have been secured to exchange messages, both between device and API and between API and database.

In terms of security, an attempt has been made to secure as much as possible all aspects of the application. In the first place, to store the passwords, we have made use of a hash that generates a secure chain within the database, which achieves privacy even if someone had access to that chain, since they would not know the password. On the other hand, what is known in Android Studio as SharedPreferences has also been secured, in which important information about the user and the application is stored, using a password to encrypt the information. Regarding connections, we first protect the connection between the device and the API by making use of TLS and certificates (which must be created before). Regarding the connection between API and database we use SSL and certificates.

### Setting up the scenario

The project has three different folders. The first is called API and it has all the necessary functionality to communicate the application and the database. On the other hand, we have another folder called JojosApp that contains the Android Studio project of the application. And finally, we have an SSL folder that contains the necessary certificates for the application to work safely.

Within the API, we have a file called "bbdd.conf" in which we must put the link provided by the database itself within its web page, which will be different from the one in the example file.

The certificates that are in the "SSL" folder contain the certificates that are created for a specific user, which means that for a new user it is necessary to create the certificates again and replace them with whatever the new user thinks will use of the application. To create the certificates, we explain below a way to create them with a small list of steps to follow, once we are located in the folder that we want to use to save the certificates:

- A "newcerts" subfolder is created.
- An empty file called "index.txt" is created.
- A file called "serial" is created with content "01".
- A file named "crlnumber" is created with content "00".
- To generate the CA keys, use the command "openssl req -x509 -newkey rsa: 2048 -keyout cakey.pem -days 3650 -out cacert.pem". (Here it will be necessary to indicate the name, the organization, the department and the country)
- To generate the user's keys, use the command "keytool -genkey -keystore user.ks -keyalg rsa". (Here you will have to indicate the same information as in the previous case). At this point, you must sign the user's certificate and indicate that it is trusted with the remaining steps.
- "keytool -certreq -file user.csr -keystore user.ks".
- "openssl ca -keyfile cakey.pem -in user.csr".
- "keytool -import -trustcacerts -alias CA -file cacert.pem -keystore user.ks".
- "keytool -import -file user.pem -keystore user.ks".