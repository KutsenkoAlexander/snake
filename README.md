# Snake game
This is the famous Snake game with some additional features. One important feature that this is multiplayer game for two person. Thous for implement of this I made server side and client side of the game.

### Client side
Client side is Java Swing, which role just to renders elements of the game. Here is no any logic of calculation position, movements snakes and apple. Client gets necessary JSON  data (this is replica of whole the game) and according of these data, client renders game elements on the game field. Also client sends the chosen direction of the player's snake to the server. For this I use still the same JSON where set special topic of the message for this. For implementing the feature to receive JSON data in real time I used WebSockets.

### Server side
Server was created for next tasks:
 - to get connection pool for management of connections;
 - to get some object which would be create game sessions;
 - to operate of players;
 - to create, operate and calculate of the positions for game instance (snakes and apple) in the game;
 - to management of games (create, delete, continue particular the game session etc.);
 - to send actual data of the game session to particular player in real time; 
  
In another words the aim of server side to provides whole life cycle of the game without rendering and into additional of this to provide a pool of multiplayer games. All of these feature were implemented with the help Spring Boot.
