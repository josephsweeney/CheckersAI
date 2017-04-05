class Piece{
  //represents a game piece
  char _token;
  int  _row;
  int  _col;
  public Piece(char t, int r, int c) {
    _token = t;
    _row   = r;
    _col   = c;
  }

  public Piece clone() {
    return new Piece(_token, _row, _col);
  }
}
