class Piece{
  //represents a game piece
  char _token;
  int  _row;
  int  _col;
  boolean _empty;

  public Piece(char t, int r, int c) {
    _token = t;
    _row   = r;
    _col   = c;
    _empty = false;
  }

  public Piece(boolean empty){
    _empty = empty;
  }

  public Piece clone() {
    return new Piece(_token, _row, _col);
  }
}
