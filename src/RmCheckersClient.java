// RmCheckersClient.java is a client that interacts with Sam, a checkers
// server. It is designed to illustrate how to communicate with the server
// in a minimal way.  It is not meant to be beautiful java code.
// Given the correct machine name and port for the server, a user id, and a
// password (_machine, _port, _user, and _password in the code), running
// this program will initiate connection and start a game with the default
// player. (the _machine and _port values used should be correct, but check
// the protocol document.)
//
// the program has been tested and used under Java 5.0, and 6.0, but probably
// would work under older or newer versions. (also works under Java 8).
//
// Copyright (C) 2008 Robert McCartney

// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License as
// published by the Free Software Foundation; either version 2 of the
// License, or (at your option) any later version.

// This program is distributed in the hope that it will be useful, but
// WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
// USA

import java.io.*;
import java.net.*;

public class RmCheckersClient {

  public final static String _user1 = "9";
  public final static String _password1 = "919747";
  public final static String _user2 = "10";
  public final static String _password2 = "183220";
  public String user, password,opponent;
  public final static String _opponent = "0";
  protected final String _machine  = "icarus.engr.uconn.edu";
  protected int _port = 3499;
  protected Socket _socket = null;
  protected PrintWriter _out = null;
  protected BufferedReader _in = null;

  protected String _gameID;
  protected String _myColor;

  public Evaluator e;
    public Evaluator endEval;
  public CheckersAI ai;
  public CheckersGameState3 currentState;

  public RmCheckersClient(){
    _socket = openSocket();
    //e = new Evaluator00();
    e = new BaseEvaluator("weights/beta.csv");
    endEval = new EndEvaluator("../src/weights/endbeta.csv");
    currentState = new CheckersGameState3();
    user = _user1;
    password = _password1;
    opponent = _opponent;
  }

  public RmCheckersClient(int player, String opponent){
    _socket = openSocket();
    e = new BaseEvaluator("../src/weights/beta.csv");
    endEval = new EndEvaluator("../src/weights/endbeta.csv");
    currentState = new CheckersGameState3();
    user = player==1 ? _user1 : _user2;
    password = player==1 ? _password1 : _password2;
    this.opponent = opponent;
  }

  public Socket getSocket(){
    return _socket;
  }

  public PrintWriter getOut(){
    return _out;
  }

  public BufferedReader getIn(){
    return _in;
  }

  public void setGameID(String id){
    _gameID = id;
  }

  public String getGameID() {
    return _gameID;
  }

  public void setColor(String color){
    _myColor = color;
  }

  public String getColor() {
    return _myColor;
  }

  public int connectGetPlayer() {
    String readMessage;

    try{
      this.readAndEcho(); // start message
      this.readAndEcho(); // ID query
      this.writeMessageAndEcho(this.user); // user ID

      this.readAndEcho(); // password query
      this.writeMessage(this.password);  // password

      this.readAndEcho(); // opponent query
      this.writeMessageAndEcho(this.opponent);  // opponent

      this.setGameID(this.readAndEcho()); // game
      this.setColor(this.readAndEcho().substring(6,11));  // color
      System.out.println("I am playing as "+this.getColor()+ " in "+ this.getGameID());
      if (this.getColor().equals("White")) {
        this.ai = new CheckersAI(this.e, 2);
        return 2;
      }
      else {
        this.ai = new CheckersAI(this.e, 1);
        return 1;
      }
    } catch  (IOException e) {
      System.out.println("Failed in read/close");
      return 0;
    }
  }

  public static void main(String[] argv){
    String readMessage;
    if(argv.length != 2) {
      System.out.println("error! please specify your player(1 or 2) and the opponent");
      return;
    }
    int myUser = Integer.parseInt(argv[0]);
    RmCheckersClient myClient = new RmCheckersClient(myUser, argv[1]);

    try{
      int player = myClient.connectGetPlayer();
      myClient.playGame(player);
      myClient.getSocket().close();
    } catch  (IOException e) {
      System.out.println("Failed in read/close");
      System.exit(1);
    }
  }

  public void playGame(int player) {
    int minPly = 8;
    int maxPly = 8;
    boolean switched = false;
    int time = 150;
    try {
      String msg = readAndEcho(); // initial message
      if(player == 1) { // black
        // dont do anything cause it was a move query
      }
      else if(player == 2) { // white
        // first apply blacks move then read move query
        applyMove(parseMove(msg));
        readAndEcho(); // move query
      }
      while(currentState.actions().size()>0){
        if(currentState.isEndGame() && !switched){
          minPly = maxPly;
	  switched = true;
	  if(currentState.pieceRatio(player) < 0.5){
	      ai.eval = endEval;
	  }
        }
	if(time < 30) {
	    minPly = 8;
	}
        currentState.printState();
        Move myMove = ai.minimax(currentState, minPly);
        writeMessageAndEcho(myMove.toString());
        if(!applyMove(myMove.toString())) {
          System.out.println("couldn't apply my move");
          break;
        }
        msg = readAndEcho(); // my move
        msg = readAndEcho(); // their move
        if(msg.contains("Result")) {
          System.out.println("Done.");
          break;
        }
        boolean success = applyMove(parseMove(msg)); // apply their move
        if(!success){
          System.out.println("couldn't apply their move");
          break;
        }
        msg = readAndEcho(); // move query
        if(msg.contains("Result")) {
          System.out.println("Done.");
          break;
        }
	time = parseTime(msg);
      }
    } catch  (IOException e) {
      System.out.println("Failed in read/close");
      System.exit(1);
    }
  }

  public int parseTime(String msg) {
      String time = msg.substring(msg.indexOf("(")+1,msg.indexOf(")"));
      return Integer.parseInt(time);
  }
    

  public String parseMove(String msg) {
    return msg.substring(11,msg.length());
  }

  public boolean applyMove(String move) {
    for(Move m : currentState.actions()) {
      if(move.equals(m.toString())) {
        currentState = currentState.result(m);
        return true;
      }
    }
    return false;
  }

  public String readAndEcho() throws IOException
  {
    String readMessage = _in.readLine();
    System.out.println("read: "+readMessage);
    return readMessage;
  }

  public void writeMessage(String message) throws IOException
  {
    _out.print(message+"\r\n");
    _out.flush();
  }

  public void writeMessageAndEcho(String message) throws IOException
  {
    _out.print(message+"\r\n");
    _out.flush();
    System.out.println("sent: "+ message);
  }

  public  Socket openSocket(){
    //Create socket connection, adapted from Sun example
    try{
      _socket = new Socket(_machine, _port);
      _out = new PrintWriter(_socket.getOutputStream(), true);
      _in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
    } catch (UnknownHostException e) {
      System.out.println("Unknown host: " + _machine);
      System.exit(1);
    } catch  (IOException e) {
      System.out.println("No I/O");
      System.exit(1);
    }
    return _socket;
  }
}
