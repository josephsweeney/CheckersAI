import java.util.List;
import java.util.ArrayList;
import java.util.Random;
/*Sparse representation: The set of pieces is represented as a list of pieces and locations: each piece is
represented by its token (one-character string) plus its board location (row, col).*/

public class CheckersGameState4 implements CheckersGameState{
  /**
   * Savanna Smith
   * Created: 4/2/17
   * Modified: 4/4/17
   * Sparse Board representation
   */
  private ArrayList<Piece> _pieces;
  private int _player; //1 = black, 0 = white

  public CheckersGameState4(){
    _pieces = new ArrayList<Piece>();
    _player = 1;
    initPieces();
  }
  public CheckersGameState4(int player, ArrayList<Piece> pieces){
    _pieces = pieces;
    _player = player;
  }
  public static void main(String[] args){ //for testing
   Random r = new Random();
   CheckersGameState4 state = new CheckersGameState4();
   System.out.println("This is the initial board followed by an entire game played.");
   System.out.println("We do this by always choosing a random move.");
   state.printState();
   List<Move> actions = state.actions();
   while(actions.size() > 0){
     System.out.println("Possible actions: ");
     for(Move move : actions) {
       System.out.println(move);
     }

   Move action = actions.get(r.nextInt(actions.size()));
    System.out.println("Chosen action: " + action);
   state = (CheckersGameState4)state.result(action);
   state.printState();
   actions = state.actions();
  }
    System.out.println("Diamond Test Case");
    ArrayList<Piece> p = new ArrayList<Piece>();
    p.add(new Piece('B', 5, 2));
    p.add(new Piece('w', 4, 1));
    p.add(new Piece('w', 4, 3));
    p.add(new Piece('w', 2, 3));
    p.add(new Piece('w', 2, 1));
    CheckersGameState4 cg = new CheckersGameState4(1, p);
    cg.printState();
    for(Move m: cg.actions()){
        System.out.println(m);
    }
    //cg.printState();

  }

  void initPieces(){
    int  col    = 0;
    char color  = 'b';
    for(int row = 7; row>=0; row--){
      if(row    == 4) {
        row--;
        color   = 'w';
      }
      else{
        col = (col+1) %2; //toggle starting column
        while(col <= 7){
          _pieces.add(new Piece(color, row, col));
          col+=2;
        }
      }
    }
  }
  //resulting row & col of piece moving in a direction
  int[] dest(Piece p, int direction){
    return dest(p._row, p._col, direction);
  }

  //resulting row & col if moving in a direction
  int[] dest(int row, int col, int direction){
    int[] dest = null;
    switch(direction){
      case 0: dest = new int[] {row +1, col -1};
              break;
      case 1: dest = new int[] {row+1, col+1};
              break;
      case 2: dest = new int[] {row-1, col-1};
              break;
      case 3: dest = new int[] {row-1, col+1};
              break;
    }
    return dest;
  }

  @Override
  public String player () {
    if(_player == 1) return "black";
    else             return "white";
  }
  boolean samePlayer(int a, char b){
    if((a==1 && b == 'b') || (a==1 && b == 'B')) return true;
    if((a==0 && b == 'w') || (a==0 && b == 'W')) return true;
    return false;
  }
  boolean oppPlayer(int a, char b){
    if((a==1 && b == 'w') || (a==1 && b == 'W')) return true;
    if((a==0 && b == 'b') || (a==0 && b == 'B')) return true;
    return false;
  }
  boolean isEnemy(Piece p){
    if(p== null) return false;
    return oppPlayer(_player, p._token);
  }
  public List<Move> actions (){
    ArrayList<Move> jumps = new ArrayList<Move>();
    ArrayList<Move> avail = new ArrayList<Move>();
    char[] neigh;

    for(Piece p: _pieces){
      if(samePlayer(_player, p._token)){
        neigh = neighbors(p);
        for(int i = 0; i<4; i++){
          if(neigh[i] == 'x') avail.add(new Move4(p, i));
            if(isJump(p._row, p._col, i, neigh)){
              jumps.addAll(computeJumps(p, i, null));
            }
        }
      }
    }
    if(jumps.isEmpty()) return avail;
    return jumps;
  }
  boolean isJump(int row, int col, int index, char[] neigh){
      int[] jumpto;
      if(oppPlayer(_player, neigh[index])){
        jumpto = dest(row, col, index);
        jumpto = dest(jumpto[0], jumpto[1], index);
        Piece item = spotContains(jumpto[0], jumpto[1]);
        if(item!=null){
          if(item._empty) {
            return true;
          }
        }
      }
      return false;
  }
  ArrayList<Move> computeJumps(Piece p, int neighborIndex, Move4 lastMove){
    ArrayList<Move> jumps = new ArrayList<Move>();
    int[] nextSpot = dest(p, neighborIndex);
    Piece captured = spotContains(nextSpot[0], nextSpot[1]);
    Move4 last = lastMove;
    int saverow = p._row;
    int savecol = p._col;
    if(last == null) {
      // This is the first jump
      // Make our move
      last = new Move4(p, dest(captured, neighborIndex), captured);
    }
    p._row = -1;
    p._col = -1;
    // Check to see if we can keep going
    // if we can, jumps.addAll(computeJumps(p, newgoodindex, nextMove))
    // else, add(nextMove)
    int lastRow = last._path[last._path.length-2];
    int lastCol = last._path[last._path.length-1];
    Piece[] newneighbors = neighborPieces(lastRow, lastCol, p);
    for(int i = 0; i<newneighbors.length; i++) {
      Piece neigh = newneighbors[i];
      if (neigh != null){
        char[] nchars = neighbors(lastRow, lastCol, p);
        if(isEnemy(neigh) && last.notCaptured(neigh)) {
          if(isJump(lastRow, lastCol, i, nchars)) {
            Move4 newMove = new Move4(last, neigh, dest(neigh, i));
            jumps.addAll(computeJumps(p, i, newMove));
          }
        }
      }
    }
    if(jumps.isEmpty()){
      jumps.add(last);
    }
    p._row = saverow;
    p._col = savecol;
    return jumps;
  }
  //return piece's 4 neighbor tokens
  char[] neighbors(Piece p){
    return neighbors(p._row, p._col, p);
  }

  char[] neighbors(int row, int col, Piece p){
    char[] neigh = new char[4];
    Piece[] neighpieces = neighborPieces(row, col, p);
    for(int i= 0; i<4; i++){
      if(neighpieces[i] != null){
        if(neighpieces[i]._empty) neigh[i] = 'x';
        else neigh[i] = neighpieces[i]._token;
      }
    }
    return neigh;
  }

  //find neighbors of the spot this piece is in.
  Piece[] neighborPieces(Piece p){
    return neighborPieces(p._row, p._col, p);
  }

  //find neighbors of a spot I'm not necessarily in
  Piece[] neighborPieces(int row, int col, Piece p){
    Piece[] neigh = new Piece[4];
    if(p._token!='b'){ //all things b cannot do
      if(row < 7 && col > 0)
        neigh[0] = spotContains(row+1, col-1);
      if(row < 7 && col < 7)
        neigh[1] = spotContains(row+1, col+1);
    }
    if(p._token!='w'){ //all things w cannot do
      if(row > 0 && col > 0)
        neigh[2] = spotContains(row-1, col-1);
      if(row > 0 && col < 7)
        neigh[3] = spotContains(row-1, col+1);
    }
    return neigh;
  }
  //go through all pieces to see what this spot contains
  Piece spotContains(int row, int col){
    for(Piece p : _pieces){
      if(p._row == row && p._col == col){
        return p;
      }
    }
    if(row >=0 && row<8 && col>=0 && col<8){
      return new Piece(true);
    }
    return null;
  }
  //return resulting state from taking move x
  public CheckersGameState result (Move x){
    Move4 m = (Move4) x;
    Piece[] captures = m.capturedPieces();
    //Piece[] pieces;
  //  if(m._path.length == 4){ //only src and dest
      ArrayList<Piece> newPieces = new ArrayList<Piece>();
      for(Piece p : _pieces){
        boolean addPiece = true;
        if(p == m._piece) {
          Piece clone = p.clone();
          clone._row = m._path[m._path.length-2];
          clone._col = m._path[m._path.length-1];
          if(m._path[m._path.length-2] == 7 && m._piece._token == 'w'){
            clone._token = 'W'; //king me
          }
          if(m._path[m._path.length-2] == 0 && m._piece._token == 'b'){
            clone._token = 'B'; //king me
          }
          newPieces.add(clone);
          addPiece = false;
        }
        if(captures!=null){
          for(Piece c : captures) {
            if(c==p){
              addPiece = false;
              break;
            }
          }
        }

        if(addPiece){
          newPieces.add(p.clone());
        }
      }
      return new CheckersGameState4((_player+1)%2, newPieces);
    //}
//    return null;
  }
  public void printState (){
    char[][] board = new char[8][8];
    for(Piece p: _pieces){
      board[p._row][p._col] = p._token;
    }
    System.out.println("player: " + player());
    for(int i = 7; i>=0; i--){
      for(int j= 0; j<8; j++){
        if(board[i][j]== '\u0000') System.out.print("-");
        else System.out.print("" + board[i][j]);
      }
      System.out.println("");
    }
      System.out.println("");
  }
}
