
public class Move {
	//(x,y) current position, (x2,y2) position move to
	int _x;
	int _y;
	int _x2;
	int _y2;
	boolean _b;

	public Move(int x, int y, int x2, int y2, boolean b){
		_x = x;
		_y = y;
		_x2 = x2;
		_y2 = y2;
		_b = b;

	}

	public void printMove(){
			System.out.println("("+this._x+","+this._y+"):("+this._x2+","+this._y2+")" );
	}
}
