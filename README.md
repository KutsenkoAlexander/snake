# Snake game
This is the famous Snake game with some additional.

One important feature that is multiplayer game for two person. Thous for implement of this I made server side and client side. 

# Client side
Clien side is Java Swing, which role just to renders elements of the game. Here is no any logic of calculation position snakes or apple. 
Client just gets neccessary JSON and according of these renders game elements on the game field. 
For implementing the feature to receive JSON data in real time I used websockets.

# Server side
Server was created for next tasks:
  - to get connection pool for managment of connection;
  - to get some object which will create games;
  - to operate of players;
  -  to create, operate and calculate of the positions for snakes and apple in the game;
  - to managment of games (create, delete, continue the game etc.);
  - to send actual data to particulr player in real time;
In another words the aim of server side to provides whol life cikel of the game without rendering and into additionl of this to managments of connections.
All of these feature were implemented with the help Spring Boot and WebSockets.
