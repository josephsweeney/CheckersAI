
public class Move4 implements Move{
  int[] _path;
  Piece _piece;

  public int source(){ return 0;}
  public int destination(){ return 0;}

  public Move4(Piece p, int[] path){
    _piece = p;
    _path = path;
  }

  public Move4(Piece p, int dest){
    _piece = p;
    //_path = new int[4];
    if(dest == 0) {
      _path = new int[] {p._row, p._col, p._row + 1, p._col-1};
    }
    else if(dest == 1) {
      _path = new int[] {p._row, p._col, p._row + 1, p._col+1};
    }
    else if(dest == 2) {
      _path = new int[] {p._row, p._col, p._row - 1, p._col-1};
    }
    else if(dest == 3) {
      _path = new int[] {p._row, p._col, p._row - 1, p._col+1};
    }
    else System.out.println("Problem in Move constructor");
  }

  // Returns a list of captured positions
  public int[] captures(){
    return new int[2];
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
