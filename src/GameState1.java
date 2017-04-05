import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Random;

public class GameState1 implements CheckersGameState{

	public static void main(String[] args){
		//testing some methods
            Random r = new Random();
	            String[][] b3 = new String[8][8];
			b3[4][2] = "B";
			b3[3][1] = "w";
			b3[1][1] = "w";
			b3[1][3] = "w";
			b3[3][3] = "w";
			CheckersGameState g7 = new GameState1(b3,true);
			g7.printState();
			List<Move> m6 = g7.actions();
			for(Move m : m6){
				System.out.println(m);
			}
            g7.result(g7.actions().get(0)).printState();
            CheckersGameState g1 = new GameState1();
            g1.printState();
            for(Move m: g1.actions()){
                System.out.println(m);
            }
            g1.result(g1.actions().get(0)).printState();
            b3[5][3] = "w";
            b3[6][4] = "b";
            b3[4][2] = null;
            CheckersGameState g2 = new GameState1(b3, false);
            g2.printState();
            for(Move m: g2.actions()){
                System.out.println(m);
            }
            g2.result(g2.actions().get(0)).printState();
            CheckersGameState g3 = new GameState1(b3, true);
            g3.printState();
            for(Move m: g3.actions()){
                System.out.println(m);
            }
            g3.result(g3.actions().get(0)).printState();
            System.out.println("This is initial board followed by entire game played");
            CheckersGameState state = new GameState1();
            state.printState();
            List<Move> actions = state.actions();
            while(actions.size() > 0){
                for(Move m: actions){
                    System.out.println(m +" ~~ ");
                }
                state = state.result(actions.get(r.nextInt(actions.size())));
                state.printState();
                actions = state.actions();
            }

	}



	private boolean _player; //true is White, Black is false
	private String[][] _board; // 2D rep of board

	public GameState1(){
		String[][] b = new String[8][8];
		b[7][1]="b";
		b[7][3]="b";
		b[7][5]="b";
		b[7][7]="b";
		b[6][0]="b";
		b[6][2]="b";
		b[6][4]="b";
		b[6][6]="b";
		b[5][1]="b";
		b[5][3]="b";
		b[5][5]="b";
		b[5][7]="b";
		b[2][0]="w";
		b[2][2]="w";
		b[2][4]="w";
		b[2][6]="w";
		b[1][1]="w";
		b[1][3]="w";
		b[1][5]="w";
		b[1][7]="w";
		b[0][0]="w";
		b[0][2]="w";
		b[0][4]="w";
		b[0][6]="w";

		_board = b;
		_player = true;
	}

	public GameState1(String[][] board, boolean player){
		_player = player;
		_board = board;
	}




	public List<Move> actions() {
		List<Move> jumps = new ArrayList<Move>();
		List<Move> moves = new ArrayList<Move>();
		String[][] b = _board;
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				if(b[j][i]!=null && matches(i, j, _board)){//if not null and blacks turn
                    if(b[j][i].equals("w")){
                        generate_moves(i, j, 1, moves, jumps, false);
                    }
                    else if(b[j][i].equals("W")){
                        generate_moves(i, j, 1, moves, jumps, true);
                    }
                    else if(b[j][i].equals("b")){
                        generate_moves(i, j, -1, moves, jumps, false);
                    }
                    else if(b[j][i].equals("B")){
                        generate_moves(i, j, -1, moves, jumps, true);
                    }
			}
		}
        }
        if(jumps.isEmpty()){
            return moves;
        }
        return jumps;
	}

    private void generate_moves(int x, int y, int deltay, List<Move> moves, List<Move> jumps, boolean king){
        ArrayList<String> path = new ArrayList<String>();
        path.add(""+y+":"+x);
        calculate_jumps(x, y, deltay, king, path, jumps, this._board);
        if(jumps.isEmpty()){
            if(can_move(x+1, y+deltay)){
                ArrayList<String> m = new ArrayList<String>();
                m.add(""+y+":"+x);
                m.add(""+(y+deltay)+":"+(x+1));
                moves.add(new Move1(m));
            }
            if(can_move(x-1, y+deltay)){
                ArrayList<String> m = new ArrayList<String>();
                m.add(""+y+":"+x);
                m.add(""+(y+deltay)+":"+(x-1));
                moves.add(new Move1(m));
            }
            if(can_move(x-1, y-deltay) && king){
                ArrayList<String> m = new ArrayList<String>();
                m.add(""+y+":"+x);
                m.add(""+(y - deltay)+":"+(x-1));
                moves.add(new Move1(m));
            }
            if(can_move(x+1, y-deltay) && king){
                ArrayList<String> m = new ArrayList<String>();
                m.add(""+y+":"+x);
                m.add(""+(y-deltay)+":"+(x+1));
                moves.add(new Move1(m));
            }
        }
    }


    private boolean within_bounds(int x, int y){
        return (x<=7 && y<=7 && x>=0 && y>=0);
    }

    private boolean can_move(int x, int y){
        return within_bounds(x, y) && empty(x, y, this._board);
    }

    private boolean matches(int x, int y, String[][] board){
        return (board[y][x] != null) && (((board[y][x].equals("b")||board[y][x].equals("B"))&& this._player) || ((board[y][x].equals("w") || board[y][x].equals("W")) && !this._player));
    }

    private boolean empty(int x, int y, String[][] board){
        return board[y][x] == null;
    }

    private boolean can_jump(int x_i, int y_i, int x, int y, String[][] board){
        return (within_bounds(x_i + x, y_i + y) && !matches(x_i + x, y_i + y, board) && within_bounds(x_i + 2*x, y_i + 2*y) && empty(x_i + 2*x, y_i + 2*y, board) && board[y_i+y][x_i + x]!=null);
    }

    private boolean any_jumps(int x_i, int y_i, int y, String[][] board, boolean king){
        boolean forward = can_jump(x_i, y_i, 1, y, board) || can_jump(x_i, y_i, -1, y, board);
        if(!king){
           return forward;
        }
       boolean backward = can_jump(x_i, y_i, 1, -1 * y, board) || can_jump(x_i, y_i, -1, -1 * y, board);
       return backward || forward;
    }

    private String[][] copy(String[][] b){
        String[][] ret = new String[8][];
        for(int i=0; i<8; i++){
            ret[i] = b[i].clone();
        }
        return ret;
    }


    public void calculate_jumps(int x_i, int y_i, int y, boolean king, ArrayList<String> path, List<Move> jumps, String[][] board){
        if(!any_jumps(x_i, y_i, y, board, king)){
            if(path.size()>1){
               jumps.add(new Move1(path)); 
            }
            return;
        }
        if(can_jump(x_i, y_i, 1, y, board)){
            String j = "" + (y_i + 2*y)+ ":" + (x_i + 2);
            String[][] b = copy(board);
            b[y_i+y] [x_i + 1]= null;
            b[y_i + 2*y] [x_i +2]= b[y_i][x_i];
            b[y_i][x_i]=null;
            ArrayList<String> new_path = (ArrayList<String>) path.clone();
            new_path.add(j);
            calculate_jumps(x_i + 2, y_i + 2*y, y, king, new_path, jumps, b);
        }
        if(can_jump(x_i, y_i, -1, y, board)){
            String j = "" + (y_i + 2*y) + ":" +( x_i + -2);
            String[][] b = copy(board);
            b[y_i+y][x_i + -1]= null;
            b[y_i + 2*y][x_i +-2]= b[y_i][x_i];
            b[y_i][x_i]=null;
            ArrayList<String> new_path = (ArrayList<String>) path.clone();
            new_path.add(j);
            calculate_jumps(x_i -2, y_i + 2*y, y, king, new_path, jumps, b);
        }
        if(king && can_jump(x_i, y_i, -1, -1 *y, board)){
            String j = "" +  (y_i - (2*y))+ ":" + (x_i + -2);
            String[][] b = copy(board);
            b[y_i-y][x_i + -1] = null;
            b[y_i - 2*y][x_i +-2] = b[y_i][x_i];
            b[y_i][x_i]=null;
            ArrayList<String> new_path = (ArrayList<String>) path.clone();
            new_path.add(j);
            calculate_jumps(x_i + -2, y_i - 2*y, y, king, new_path, jumps, b);
        }
        if(king && can_jump(x_i, y_i, 1, -1 *y, board)){
            String j = "" +(y_i - (2*y)) + ":" + (x_i + 2) ;
            String[][] b = copy(board);
            b[y_i-y][x_i + 1] = null;
            b[y_i - 2*y][x_i +2]= b[y_i][x_i];
            b[y_i][x_i]=null;
            ArrayList<String> new_path = (ArrayList<String>) path.clone();
            new_path.add(j);
            calculate_jumps(x_i + 2, y_i - 2*y, y, king, new_path, jumps, b);
        }


    }

	//fix this
	public GameState1 result(Move y) {
        Move1 x = (Move1) y;
        String p = this._board[x.sourceX()][x.sourceY()];
        String[][] nb = copy(this._board);
        nb[x.sourceX()][x.sourceY()] = null;
        nb[x.destX()][x.destY()] = p;
        for(String k: x.kills()){
            int i = Integer.parseInt(k.split(",")[0]);
            int j = Integer.parseInt(k.split(",")[1]);
            nb[i][j] = null;
		}
        if(x.destX() == 0 && _player){
            nb[x.destX()][x.destY()] = "B";
        }
        if(x.destX() == 7 && !_player){
            nb[x.destX()][x.destY()] = "W";
        }

        return new GameState1(nb,!_player);
    }

	public void printState() {
		String[][] board = this._board;
		for(int i=7; i>=0; i--){
			System.out.println();
			for(int j=0; j<8; j++){
				if(board[i][j]==null)
					System.out.print("-");
				else System.out.print(board[i][j]);
			}
		}
		System.out.println("\n\n" + this.player() + "'s move");

	}
	public String player() {

		if(!this._player)
			return "White";
		return "Black";
	}

}
