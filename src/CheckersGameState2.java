import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class CheckersGameState2 implements CheckersGameState {

  // 0 = empty
  // 1 = black piece
  // 2 = white piece
  // 3 = black king
  // 4 = white king
  private int[] board;

  // 1 = black, 2 = white
  private int player;

  public static void main(String args[]) {
    CheckersGameState2 state = new CheckersGameState2();
    System.out.println("This is the initial board followed by an entire game played.");
    System.out.println("We do this by always choosing the first move.");
    state.printState();
    List<Move> actions = state.actions();
    while(actions.size() > 0){
      for(Move move : actions) {
        System.out.println((Move2)move+" ~~ "+((Move2)move).destination());
      }
      state = (CheckersGameState2)state.result(actions.get(0));
      state.printState();
      actions = state.actions();
    }
    System.out.println("\n\nNow this tests a diamond movement edge case:");
    int[] b = new int[32];
    b[5] = 3;
    b[8] = 2;
    b[16] = 2;
    b[17] = 2;
    b[9] = 2;
    CheckersGameState s = new CheckersGameState2(1, b);
    s.printState();
    for(Move m: s.actions()){
        System.out.println(m);
    }
    s.result(s.actions().get(0)).printState();

  }

  public CheckersGameState2(int player, int[] board) {
    this.board = board;
    this.player = player;
  }
  public CheckersGameState2(int player) {
    board = new int[32];
    for(int i = 0; i<32; i++) {
      if(i<12) {
        board[i] = 1;
      }
      else if(i>=20) {
        board[i] = 2;
      }
      else {
        board[i] = 0;
      }
    }
    if(player == 1 || player == 2) {
      this.player = player;
    }
    else {
      this.player = 1;
    }
  }
  public CheckersGameState2() {
    this(1);
  }

  public String player () {
    if(this.player == 1) {
      return "black";
    }
    else if(this.player == 2) {
      return "white";
    }
    else {
      System.out.println("Player data is wrong.");
      return "Error";
    }
  }

  private int otherPlayer() {
    return (player%2) + 1;
  }

  private boolean correctColor(int space) {
    // returns true if piece at a space is your color
    return space != 0 && (player == space || (player + 2) == space);
  }

  private boolean isEnemy(int piece) {
    int other = otherPlayer();
    return piece == other || (other + 2) == piece;
  }

  private int[] neighbors(int index) {
    int[] neighbors = new int[4];
    int row = (index - index%4)/4;
    int col = index%4;
    if (row % 2 == 0) {
      neighbors[0] = index + 4;
      neighbors[1] = index + 5;
      neighbors[2] = index - 4;
      neighbors[3] = index - 3;

      if(col == 3) {
        neighbors[1] = -1;
        neighbors[3] = -1;
      }
    }
    else {
      neighbors[0] = index + 3;
      neighbors[1] = index + 4;
      neighbors[2] = index - 5;
      neighbors[3] = index - 4;

      if(col == 0) {
        neighbors[0] = -1;
        neighbors[2] = -1;
      }
    }

    for(int i = 0; i<4; i++) {
      if(neighbors[i] < 0 || neighbors[i] >=32) {
        neighbors[i] = -1;
      }
    }

    return neighbors;
  }

  private ArrayList<Move> findJumpMoves(int starting, int current, int[] path) {
    ArrayList<Move> moves = new ArrayList<Move>();
    int piece = board[starting];
    board[starting] = 0;
    int[] neighbors = neighbors(current);
    int beg, end;
    if(piece == 1) {
      beg = 0;
      end = 2;
    }
    else if(piece == 2) {
      beg = 2;
      end = 4;
    }
    else {
      beg = 0;
      end = 4;
    }
    for(int i = beg; i<end; i++) {
      boolean valid = neighbors[i] != -1 && isEnemy(board[neighbors[i]]);
      if(valid && !isCaptured(neighbors[i], path)) {
        int next = neighbors(neighbors[i])[i];
        if(next != -1 && board[next] == 0) {
          int [] newPath = Arrays.copyOf(path, path.length+1);
          newPath[path.length] = next;
          ArrayList<Move> jumpMoves = findJumpMoves(starting, next, newPath);
          if(jumpMoves.size() == 0){
            moves.add(new Move2(newPath));
          } else {
            moves.addAll(jumpMoves);
          }
        }
      }
    }
    board[starting] = piece;
    return moves;
  }

  private boolean isCaptured(int space, int[] path) {
    Move temp = new Move2(path);
    int[] captured = temp.captures();
    for(int i = 0; i<captured.length; i++) {
      if(captured[i]==space) {
        return true;
      }
    }
    return false;
  }

  private ArrayList<Move> captures() {
    ArrayList<Move> captures = new ArrayList<Move>();
    for(int i = 0; i<32; i++) {
      int piece = board[i];
      if(piece != 0 && correctColor(piece)) {
        captures.addAll(findJumpMoves(i, i, new int[]{i}));
      }
    }
    return captures;
  }

  private ArrayList<Move> noncaptures() {
    ArrayList<Move> moves = new ArrayList<Move>();
    for(int i = 0; i<32; i++) {
      int space = board[i];
      if(space != 0 && correctColor(space)) {
        int[] neighbors = neighbors(i);
        if(space == 1) {
          // black piece
          if(neighbors[0] != -1 && board[neighbors[0]] == 0) {
            moves.add(new Move2(i, neighbors[0]));
          }
          if(neighbors[1] != -1 && board[neighbors[1]] == 0) {
            moves.add(new Move2(i, neighbors[1]));
          }
        }
        else if(space == 2) {
          // white piece
          if(neighbors[2] != -1 && board[neighbors[2]] == 0) {
            moves.add(new Move2(i, neighbors[2]));
          }
          if(neighbors[3] != -1 && board[neighbors[3]] == 0) {
            moves.add(new Move2(i, neighbors[3]));
          }
        }
        else {
          // king
          for(int j = 0; j<4; j++) {
            if(neighbors[j] != -1 && board[neighbors[j]] == 0) {
              moves.add(new Move2(i, neighbors[j]));
            }
          }
        }
      }
    }
    return moves;
  }

  public List <Move> actions() {
    ArrayList<Move> actions = this.captures();
    if(actions.size() != 0) {
      return actions;
    }
    else {
      actions = this.noncaptures();
    }
    return actions;
  }

  public CheckersGameState result ( Move x ) {
    Move2 move = (Move2) x;
    int[] newBoard = Arrays.copyOf(board, board.length);
    int newPlayer = otherPlayer();
    int[] captures = move.captures();
    if(captures[0] != -1) {
      for(int i = 0; i<captures.length; i++) {
        newBoard[captures[i]] = 0;
      }
    }
    int s = newBoard[move.source()];
    newBoard[move.source()] = 0;
    newBoard[move.destination()] = s;

    if(promote(move.source(), move.destination())) {
      newBoard[move.destination()] += 2;
    }

    return new CheckersGameState2(newPlayer, newBoard);
  }

  private boolean promote(int src, int dest) {
    switch(board[src]) {
      case 0:
        return false;
      case 1:
        return dest>=28;
      case 2:
        return dest<=3;
      case 3: case 4:
        return false;
    }
    return false;
  }

  public void printState () {
    String output = "";
    for(int i = 0; i<32; i++) {
      int space = board[i];
      int row = (i - i%4)/4;
      if(i%4 == 0 && i != 0){
        output += "\n";
      }
      if(row % 2 == 0) {
        output += " - ";
      }
      switch (space) {
        case 0: output += " - ";
          break;
        case 1: output += " b ";
          break;
        case 2: output += " w ";
          break;
        case 3: output += " B ";
          break;
        case 4: output += " W ";
          break;
      }

      if(row % 2 == 1) {
        output += " - ";
      }
    }
    System.out.println(output);
  }
}
