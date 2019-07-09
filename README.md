# Snake game
This is the famous Snake game with some additional.

One important feature that is multiplayer game for two person. Thous for implement of this I made server side and client side. 

# Client side
Client side is Java Swing, which role just to renders elements of the game. Here is no any logic of calculation position snakes or apple. 
Client just gets necessary JSON and according of these renders game elements on the game field. 
For implementing the feature to receive JSON data in real time I used WebSockets.

# Server side
Server was created for next tasks:
  - to get connection pool for management of connection;
  - to get some object which will create games;
  - to operate of players;
  -  to create, operate and calculate of the positions for snakes and apple in the game;
  - to management of games (create, delete, continue the game etc.);
  - to send actual data to particular player in real time;
In another words the aim of server side to provides whole life cycle of the game without rendering and into additional of this to managements of connections.

All of these feature were implemented with the help Spring Boot and WebSockets.
