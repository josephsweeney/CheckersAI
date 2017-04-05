import java.util.List;
import java.util.ArrayList;
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

  public CheckersGameState4(int player){
    _pieces = new ArrayList<Piece>();
    _player = player;
    initPieces();
  }
  public CheckersGameState4(int player, ArrayList<Piece> pieces){
    _pieces = pieces;
    _player = player;
  }

  public static void main(String[] args){ //for testing
    CheckersGameState4 cg = new CheckersGameState4(0);
    System.out.println(cg.player());
    cg.printState();
    System.out.println("");
    for(Move move : cg.actions()){
      System.out.println(move);
      cg.result(move).printState();
      System.out.println();
    }
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
  @Override
  public List<Move> actions (){
    ArrayList<Move> jumps = new ArrayList<Move>();
    ArrayList<Move> avail = new ArrayList<Move>();
    char[] neigh;
    int[] jumpPath;

    for(Piece p: _pieces){
      if(samePlayer(_player, p._token)){
        neigh = neighbors(p);
        for(int i = 0; i<4; i++){
          if(neigh[i] == 'x') avail.add(new Move4(p, i));
          if(!samePlayer(_player, neigh[i])) //check for jumps
            jumpPath = computeJumps(p, i);  //do something with this
        }
      }
    }
    if(jumps.isEmpty()) return avail;
    return jumps;
  }

  int[] computeJumps(Piece p, int i){
    return null;
  }

  //return piece's 4 neighbors
  char[] neighbors(Piece p){
    char[] neigh = new char[4];
    if(p._token!='b'){ //all things b cannot do
      if(p._row < 7 && p._col > 0)
        neigh[0] = spotContains(p._row+1, p._col-1);
      if(p._row < 7 && p._col < 7)
        neigh[1] = spotContains(p._row+1, p._col+1);
    }
    if(p._token!='w'){ //all things w cannot do
      if(p._row > 0 && p._col > 0)
        neigh[2] = spotContains(p._row-1, p._col-1);
      if(p._row > 0 && p._col < 7)
        neigh[3] = spotContains(p._row-1, p._col+1);
    }
    return neigh;
  }

  //go through all pieces to see what this spot contains
  char spotContains(int row, int col){
    for(Piece p : _pieces){
      if(p._row == row && p._col == col){
        return p._token;
      }
    }
    return 'x';
  }

  @Override
  //return resulting state from taking move x
  public CheckersGameState result (Move x){
    Move4 m = (Move4) x;
    Piece[] captures = m.capturedPieces();
    //Piece[] pieces;
    if(m._path.length == 4){ //only src and dest
      ArrayList<Piece> newPieces = new ArrayList<Piece>();
      for(Piece p : _pieces){
        boolean addPiece = true;
        if(p == m._piece) {
          Piece clone = p.clone();
          clone._row = m._path[2];   //is this actually changing the piece that belongs to _pieces?
          clone._col = m._path[3];
          if(m._path[2] == 7 && m._piece._token == 'w'){
            clone._token = 'W'; //king me
          }
          if(m._path[2] == 0 && m._piece._token == 'b'){
            clone._token = 'B'; //king me
          }
          newPieces.add(clone);
          addPiece = false;
        }
        for(Piece c : captures) {
          if(c==p){
            addPiece = false;
            break;
          }
        }
        if(addPiece){
          newPieces.add(p.clone());
        }
      }
      return new CheckersGameState4((_player+1)%2, newPieces);
    }
    //else consider jumps.
    //remove from array list if captured.
    //return (new CheckersGameState4((player+1)%2, pieces));
    return this;
  }

  @Override
  public void printState (){
    char[][] board = new char[8][8];
    for(Piece p: _pieces){
      board[p._row][p._col] = p._token;
    }
    for(int i = 7; i>=0; i--){
      for(int j= 0; j<8; j++){
        if(board[i][j]== '\u0000') System.out.print("-");
        else System.out.print("" + board[i][j]);
      }
      System.out.println("");
    }
  }

}
