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

  protected final static String _user1 = "9";
  protected final static String _password1 = "919747";
  protected final static String _user2 = "10";
  protected final static String _password2 = "183220";
  protected final static String _opponent = "0";
  protected final String _machine  = "icarus.engr.uconn.edu";
  protected int _port = 3499;
  protected Socket _socket = null;
  protected PrintWriter _out = null;
  protected BufferedReader _in = null;

  protected String _gameID;
  protected String _myColor;

  public Evaluator e;
  public CheckersAI ai;
  public CheckersGameState currentState;

  public RmCheckersClient(){
    _socket = openSocket();
    e = new Evaluator00();
    currentState = new CheckersGameState3();
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

  public static void main(String[] argv){
    String readMessage;
    RmCheckersClient myClient = new RmCheckersClient();

    try{
      myClient.readAndEcho(); // start message
      myClient.readAndEcho(); // ID query
      myClient.writeMessageAndEcho(_user1); // user ID

      myClient.readAndEcho(); // password query
      myClient.writeMessage(_password1);  // password

      myClient.readAndEcho(); // opponent query
      myClient.writeMessageAndEcho(_opponent);  // opponent

      myClient.setGameID(myClient.readAndEcho()); // game
      myClient.setColor(myClient.readAndEcho().substring(6,11));  // color
      System.out.println("I am playing as "+myClient.getColor()+ " in "+ myClient.getGameID());
      // readMessage = myClient.readAndEcho();
      // depends on color--a black move if i am white, Move:Black:i:j
      // otherwise a query to move, ?Move(time):
      if (myClient.getColor().equals("White")) {
        myClient.ai = new CheckersAI(myClient.e, 2);
        myClient.playGame(2);
        // readMessage = myClient.readAndEcho();  // move query
        // myClient.writeMessageAndEcho("(2:4):(3:5)");
        // readMessage = myClient.readAndEcho();  // white move
        // readMessage = myClient.readAndEcho();  // black move
        // readMessage = myClient.readAndEcho();  // move query
        // here you would need to move again
      }
      else {
        myClient.ai = new CheckersAI(myClient.e, 1);
        myClient.playGame(1);
        // myClient.writeMessageAndEcho("(5:3):(4:4)");
        // readMessage = myClient.readAndEcho();  // move query
        // readMessage = myClient.readAndEcho();  // black move
        // readMessage = myClient.readAndEcho();  // white move
        // here you would need to move again
      }

      myClient.getSocket().close();
    } catch  (IOException e) {
      System.out.println("Failed in read/close");
      System.exit(1);
    }
  }

  public void playGame(int player) {
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
        currentState.printState();
        Move myMove = ai.minimax(currentState);
        writeMessageAndEcho(myMove.toString());
        if(!applyMove(myMove.toString())) {
          System.out.println("couldn't apply my move");
          break;
        }
        msg = readAndEcho(); // my move
        msg = readAndEcho(); // their move
        boolean success = applyMove(parseMove(msg)); // apply their move
        if(!success){
          System.out.println("couldn't apply their move");
          break;
        }
        msg = readAndEcho(); // move query
      }
    } catch  (IOException e) {
      System.out.println("Failed in read/close");
      System.exit(1);
    }
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
