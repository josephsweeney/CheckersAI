import java.util.Arrays;

public class Move4 implements Move{
  int[] _path;
  Piece _piece;
  Piece[] _captured;

  public int source(){ return 0;}
  public int destination(){ return 0;}

  public Move4(Piece p, int[] path){
    _piece = p;
    _path = path;
  }

  public Move4(Piece p, int[] dest, Piece captured){
    _piece = p;
    _captured = new Piece[] {captured};
    _path = new int[] {p._row, p._col, dest[0], dest[1]};
  }

  public Move4(Move4 lastMove, Piece captured, int[] dest){
    _piece = lastMove._piece;
    _captured = Arrays.copyOf(lastMove._captured, lastMove._captured.length +1);
    _captured[_captured.length-1] = captured;
    _path = Arrays.copyOf(lastMove._path, lastMove._path.length +2);
    _path[_path.length-2] = dest[0];
    _path[_path.length-1] = dest[1];
  }

  public Move4(Piece p, int destIndex){
    _piece = p;
    //_path = new int[4];
    if(destIndex == 0) {
      _path = new int[] {p._row, p._col, p._row + 1, p._col-1};
    }
    else if(destIndex == 1) {
      _path = new int[] {p._row, p._col, p._row + 1, p._col+1};
    }
    else if(destIndex == 2) {
      _path = new int[] {p._row, p._col, p._row - 1, p._col-1};
    }
    else if(destIndex == 3) {
      _path = new int[] {p._row, p._col, p._row - 1, p._col+1};
    }
    else System.out.println("Problem in Move constructor");
  }

  // Returns a list of captured positions
  public int[] captures(){
    return new int[2];
  }

  public boolean notCaptured(Piece p){
    for(Piece c : _captured){
      if(c==p){
        return false;
      }
    }
    return true;
  }

  public Piece[] capturedPieces(){
    return _captured;
  }

  // Returns the string representation of a move
  public String toString(){
    String output = "";
    for(int i = 0; i<_path.length; i++) {
       output += "(" + _path[i] + ":" + _path[++i] + ")";
    }
   return output;
  }
}
