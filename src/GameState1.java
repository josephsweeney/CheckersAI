import java.util.ArrayList;
import java.util.List;

public class GameState1 implements CheckersGameState{

	public static void main(String[] args){
		//testing some methods
		GameState1 game = new GameState1(); //initial game state
			game.printState();	//print the state
			List<Move> moves = game.actions();
			System.out.println();
			System.out.println("Available Moves:");
			for(Move m : moves){
				m.printMove();
			}
			System.out.println("\n" + "Test1: Move(2,1) to (3,0) result (moving black piece foward");
			CheckersGameState result = game.result(new Move(2,1,3,0,false));
			result.printState();
		List<Move> moves2 = result.actions();
			System.out.println();
			System.out.println("Available Moves:");
			for(Move m : moves2){
				m.printMove();
			}
			System.out.println("\n" + "Test2: Move(5,2) to (4,1) result (moving white piece foward");
			CheckersGameState result2 = result.result(new Move(5,2,4,1,false));
			result2.printState();
		List<Move> moves3 = result2.actions();
			System.out.println();
			System.out.println("Available Moves:");
			for(Move m3 : moves3){
				m3.printMove();
			}
			System.out.println("\n" + "Test3: Move(3,0) to (5,2) result (jumping and capturing a piece)");
			CheckersGameState result3 = result2.result(new Move(3,0,5,2,true));
			result3.printState();

			String[][] b = new String[8][8];
			b[1][0] = "w";
			b[0][3] = "b";
			b[0][5] = "b";
			GameState1 g = new GameState1(b,false);
			g.printState();
			List<Move> m4 = g.actions();
			for(Move m : m4){
				m.printMove();
			}

			System.out.println("\n" + "Test4: Move(1,0) to (0,1) result (creating a king)");
			CheckersGameState g2 = g.result(new Move(1,0,0,1,false));
			g2.printState();

			String[][] b2 = new String[8][8];
			b2[4][2] = "W";
			b2[3][1] = "b";
			b2[3][3] = "B";
			b2[5][1] = "b";
			b2[5][3] = "b";
			GameState1 g3 = new GameState1(b2,false);
			g3.printState();
			List<Move> m5 = g3.actions();
			for(Move m : m5){
				m.printMove();
			}
			System.out.println("\n" + "Test6: Move(4,2) to (2,0) result (king jumping backward and capturing)");
			CheckersGameState g4 = g3.result(new Move(4,2,2,0,true));
			g4.printState();
	}



	private boolean _player; //true is White, Black is false
	private String[][] _board; // 2D rep of board

	public GameState1(){
		String[][] b = new String[8][8];
		b[0][1]="b";
		b[0][3]="b";
		b[0][5]="b";
		b[0][7]="b";
		b[1][0]="b";
		b[1][2]="b";
		b[1][4]="b";
		b[1][6]="b";
		b[2][1]="b";
		b[2][3]="b";
		b[2][5]="b";
		b[2][7]="b";
		b[5][0]="w";
		b[5][2]="w";
		b[5][4]="w";
		b[5][6]="w";
		b[6][1]="w";
		b[6][3]="w";
		b[6][5]="w";
		b[6][7]="w";
		b[7][0]="w";
		b[7][2]="w";
		b[7][4]="w";
		b[7][6]="w";

		_board = b;
		_player = true;
	}

	public GameState1(String[][] board, boolean player){
		_player = player;
		_board = board;
	}


	@Override
	public List<Move> actions() {
		GameState1 game = this;
		ArrayList<Move> list = new ArrayList<>();
		String[][] b = game._board;
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				if(b[i][j]!=null && this._player){//if not null and blacks turn
					if(b[i][j] == "b"){
						if(game.canJumpL(i,j)){list.add(new Move(i,j,i+2,j-2,true));}
						if(game.canJumpR(i,j)){list.add(new Move(i,j,i+2,j+2,true));}
						if(game.canMoveL(i,j)){list.add(new Move(i,j,i+1,j-1,false));}
						if(game.canMoveR(i,j)){list.add(new Move(i,j,i+1,j+1,false));}

					}
					else if(b[i][j] == "B"){
						if(game.canJumpL(i,j)){list.add(new Move(i,j,i+2,j-2,true));}
						if(game.canJumpR(i,j)){list.add(new Move(i,j,i+2,j+2,true));}
						if(game.canMoveL(i,j)){list.add(new Move(i,j,i+1,j-1,false));}
						if(game.canMoveR(i,j)){list.add(new Move(i,j,i+1,j+1,false));}
						if(game.canBackL(i,j)){list.add(new Move(i,j,i-1,j-1,false));}
						if(game.canBackR(i,j)){list.add(new Move(i,j,i-1,j+1,false));}
						if(game.canJumpBackL(i,j)){list.add(new Move(i,j,i-2,j-2,true));}
						if(game.canJumpBackR(i,j)){list.add(new Move(i,j,i-2,j-2,true));}
					}
				}
				else if(b[i][j]!=null && !this._player){
					if(b[i][j] == "w"){
						if(game.canJumpBackL(i,j)){list.add(new Move(i,j,i-2,j-2,true));}
						if(game.canJumpBackR(i,j)){list.add(new Move(i,j,i-2,j+2,true));}
						if(game.canBackL(i,j)){list.add(new Move(i,j,i-1,j-1,false));}
						if(game.canBackR(i,j)){list.add(new Move(i,j,i-1,j+1,false));}
					}
					else if(b[i][j] == "W"){
						if(game.canJumpL(i,j)){list.add(new Move(i,j,i+2,j-2,true));}
						if(game.canJumpR(i,j)){list.add(new Move(i,j,i+2,j+2,true));}
						if(game.canMoveL(i,j)){list.add(new Move(i,j,i+1,j-1,false));}
						if(game.canMoveR(i,j)){list.add(new Move(i,j,i+1,j+1,false));}
						if(game.canBackL(i,j)){list.add(new Move(i,j,i-1,j-1,false));}
						if(game.canBackR(i,j)){list.add(new Move(i,j,i-1,j+1,false));}
						if(game.canJumpBackL(i,j)){list.add(new Move(i,j,i-2,j-2,true));}
						if(game.canJumpBackR(i,j)){list.add(new Move(i,j,i-2,j+2,true));}
					}
				}

			}
		}
		return list;
	}
	@Override
	//fix this
	public CheckersGameState result(Move x) {
		GameState1 game = this;
		String[][] b = this._board;
		boolean p = this._player;
		CheckersGameState next = null;
			//check if this situation will result in a king B
			if((x._x == 6 && x._x2 == 7)&& (b[x._x][x._y] != "W")){
				b[x._x2][x._y2] = "B";
				b[x._x][x._y] = null;
				if(game.canJumpBackL(x._x2, x._y2) || game.canJumpBackR(x._x2, x._y2) ){
					next = new GameState1(b,p);
				}
				else
				next = new GameState1(b,!p);
			}
			//check if this will result in king W
			else if((x._x == 1 && x._x2 == 0)&& (b[x._x][x._y] != "B")){
				b[x._x2][x._y2] = "W";
				b[x._x][x._y] = null;
				if(game.canJumpL(x._x2, x._y2) || game.canJumpR(x._x2, x._y2)){
					next = new GameState1(b,p);
				}
				else
				next = new GameState1(b,!p);
			}
			//if you get a king by jumping into end of whites side, change to B and remove captured piece
			else if((x._x == 5 && x._x2 == 7)&& (b[x._x][x._y] != "W")){
				b[x._x2][x._y2] = "B";
				b[x._x][x._y] = null;
					if(x._y2>x._y){ //jumping right, set the one you jumped over to be null
						b[x._x+1][x._y+1] = null;
					}
						else{
							b[x._x+1][x._y-1] = null;
						}
					if(game.canJumpBackL(x._x2, x._y2) || game.canJumpBackR(x._x2, x._y2)){
						//if there is another jump we can take, return state with same players turn
						next = new GameState1(b,p);
					}
					else{
						next = new GameState1(b,!p);
					}
			}
			//if you get a king by jumping to end of blacks side, change to W and remove captured piece
			else if((x._x == 2 && x._x2 == 0)&& (b[x._x][x._y] != "B")){
				b[x._x2][x._y2] = "W";
				b[x._x][x._y] = null;
					if(x._y2>x._y){ //jumping right, set the one you jumped over to be null
						b[x._x-1][x._y+1] = null;
					}
					else{
						b[x._x-1][x._y-1] = null;
					}
				if(game.canJumpL(x._x2, x._y2) || game.canJumpR(x._x2, x._y2)){
					//if there is another jump we can take, return state with same players turn
					next = new GameState1(b,p);
				}
				else{
					next = new GameState1(b,!p);
				}
			}
			//if just a normal jump
			else if(x._b){
				String s = b[x._x][x._y];
				b[x._x2][x._y2] = s;
					if(x._y2>x._y && x._x2>x._x){
						b[x._x+1][x._y+1] = null;
						b[x._x][x._y]=null;
						next = new GameState1(b,!p);
					}
					else if(x._y2<x._y && x._x2>x._x){
						b[x._x+1][x._y-1] = null;
						b[x._x][x._y]=null;
						next = new GameState1(b,!p);
					}
					else if(x._y2>x._y && x._x2<x._x){
						b[x._x-1][x._y+1] = null;
						b[x._x][x._y]=null;
						next = new GameState1(b,!p);
					}
					else if(x._y2<x._y && x._x2<x._x){
						b[x._x-1][x._y-1] = null;
						b[x._x][x._y]=null;
						next = new GameState1(b,!p);
					}
						if(s == "B" || s == "W"){
							if(game.canJumpL(x._x2,x._y2) || game.canJumpR(x._x2,x._y2) || game.canJumpBackL(x._x2,x._y2)|| game.canJumpR(x._x2,x._y2)){
								next = new GameState1(b,p);
							}
							else next = new GameState1(b,!p);
						}
			}
			else{
				b[x._x2][x._y2] = b[x._x][x._y];
				 b[x._x][x._y] = null;
				 next = new GameState1(b, !p);
			}

		return next;
	}

	@Override
	public void printState() {
		String[][] board = this._board;
		for(int i=0; i<8; i++){
			System.out.println();
			for(int j=0; j<8; j++){
				if(board[i][j]==null)
					System.out.print("-");
				else System.out.print(board[i][j]);
			}
		}
		System.out.println("\n\n" + this.player() + "'s move");

	}
	@Override
	public String player() {

		if(!this._player)
			return "White";
		return "Black";
	}

	private boolean canBackL(int i, int j){
		GameState1 game = this;
		String[][] b = game._board;
			if(j!=0 && i!=0){
				if(b[i-1][j-1]==null){
					return true;
			}
			}
			return false;
	}
	private boolean canMoveL(int i, int j){
		GameState1 game = this;
		String[][] b = game._board;
			if(j!=0 && i!=7){
				if(b[i+1][j-1]==null){
					return true;
			}
			}
			return false;
	}
	private boolean canBackR(int i, int j){
		GameState1 game = this;
		String[][] b = this._board;
			if(j!=7 && i!=0){
				if(b[i-1][j+1]==null){
					return true;
			}
			}
			return false;
	}
	private boolean canMoveR(int i, int j){
		GameState1 game = this;
		String[][] b = this._board;
			if(j!=7 && i!=7){
				if(b[i+1][j+1]==null){
					return true;
			}
			}
			return false;
	}
	private boolean canJumpBackL(int i, int j){
		GameState1 game = this;
		String[][] b = this._board;
			if(j!=0 && j!=1 && i!=0 && i!=1){
				if(!game._player){
					if(b[i-1][j-1] =="b" || b[i-1][j-1]=="B"){
						if(b[i-2][j-2]==null){
							return true;
						}
					}
				}
				else if(game._player){
					if(b[i-1][j-1] =="w" || b[i-1][j-1]=="W"){
						if(b[i-2][j-2]==null){
							return true;
						}
					}
				}
			}
			return false;
	}
	private boolean canJumpL(int i, int j){
		GameState1 game = this;
		String[][] b = this._board;
			if(j!=0 && j!=1 && i!=6 && i!=7){
				if(b[i+1][j-1] =="w" || b[i+1][j-1]=="W" || b[i][j] == "B" || b[i][j] == "W"){
					if(b[i+2][j-2]==null){
						return true;
					}
				}
			}
			return false;
	}
	private boolean canJumpBackR(int i, int j){
		GameState1 game = this;
		String[][] b = this._board;
			if(j!=6 && j!=7 && i!=0 && i!=1){
				if(!game._player){
					if(b[i-1][j+1]=="b" || b[i-1][j+1]=="B"){
						if(b[i-2][j+2]==null){
							return true;
						}
					}
				}
				else if(game._player){
					if(b[i-1][j+1]=="w" || b[i-1][j+1]=="W"){
						if(b[i-2][j+2]==null){
							return true;
						}
					}
				}
			}
			return false;
	}
	private boolean canJumpR(int i, int j){
		GameState1 game = this;
		String[][] b = this._board;
			if(j!=6 && j!=7 && i!=6 && i!=7){
				if(b[i+1][j+1]=="w" || b[i+1][j+1]=="W" || b[i][j] == "B" || b[i][j] == "W"){
					if(b[i+2][j+2]==null){
						return true;
					}
				}
			}
			return false;
	}

}
