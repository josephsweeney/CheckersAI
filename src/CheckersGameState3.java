import java.util.List;
import java.util.LinkedList;

public class CheckersGameState3 implements CheckersGameState{

    int player;
    int[] board;

    public CheckersGameState3(int player, int[] board){
        this.player = player;
        this.board = board;
    }

    public CheckersGameState3() {
      this.player = 1;
      this.board = new int[]{
        1,1,1,1,
        1,1,1,1,0,
        1,1,1,1,
        0,0,0,0,0,
        0,0,0,0,
        2,2,2,2,0,
        2,2,2,2,
        2,2,2,2
      };
    }

    public CheckersGameState3(int player, String[] board){
        this.player = player;
        this.board = to_array(board);
    }

    public int convert(String s){
        if(s.equals("-")){
            return 0;
        }
        else if(s.equals("b")){
            return 1;
        }
        else if(s.equals("w")){
            return 2;
        }
        else if(s.equals("B")){
            return 3;
        }
        else{
            return 4;
        }
    }

    public int[] to_array(String[] board){
        int[] b = new int[35];
        int i = 0;
        int j = 0;
        int added = 0;
        boolean leading = false;
        while(i < 35 ){
            if(i % 9 == 8){
                b[i] = 0;
            }
            else if(leading){
                b[i] = convert(board[j]);
            }
            else{
                b[i] =convert(board[j+1]);
            }
            if(i % 9 == 8){

                i++;
                continue;
            }
            j = j + 2;
            i++;
            added++;
            if(added == 4){
                added = 0;
                leading = !leading;
            }
            if(j == 35){
                added =0;
                leading = false;
            }
        }
        return b;
    }

    public String player(){
        if(this.player == 1){
            return "black";
        }
        else{
            return "white";
        }
    }

    private String character(int p){
        if(p == 0){
            return "-";
        }
        else if (p==1){
            return "b";
        }
        else if (p==2){
            return "w";
        }
        else if (p==3){
            return "B";
        }
        else{
            return "W";
        }
    }

    private boolean current_player(int sq, int[] board){
        return (board[sq] == this.player || board[sq] - 2 == this.player);
    }

    private boolean can_kill(int sq, int[] board){
        return valid_square(sq) && !empty(board, sq) && !current_player(sq, board);
    }

    private boolean valid_square(int x){
        return (x >= 0 && x <= 34 && x % 9 != 8);
    }

    private boolean empty(int[] board , int sq){
        return board[sq] == 0;
    }

    private boolean can_move(int orig, int delta, int[] board){
        return (valid_square(orig + delta) && empty(board, orig + delta));
    }

    private boolean can_jump(int orig, int delta, int[] board){
        return (can_kill(orig+delta, board) && valid_square(orig + (2 * delta)) && empty(board, orig + 2 * delta));
    }

    private boolean any_jumps(int orig, int delta1, int delta2, int[] board, boolean king){
        boolean forward = can_jump(orig, delta1, board) || can_jump(orig, delta2, board);
        if(!king)
            return forward;
        boolean backward = can_jump(orig, delta1 * -1, board) || can_jump(orig, delta2 * -1, board);
        return backward || forward;
    }

    public List<Move> actions(){
        LinkedList<Move> moves = new LinkedList<Move>();
        LinkedList<Move> jumps = new LinkedList<Move>();
        for(int i = 0; i < this.board.length; i++){
            if(current_player(i, this.board)){
                if(this.player == 1){
                    if(this.board[i] == 3){
                        generate_moves(i, 4, 5, moves, jumps, true);
                    }
                    else{
                        generate_moves(i, 4, 5, moves, jumps, false);
                    }
                }
                else{
                    if(this.board[i] == 4){
                        generate_moves(i, -4, -5, moves, jumps, true);
                    }
                    else{
                        generate_moves(i, -4, -5, moves, jumps,false);
                    }
                }
            }
        }
        if(jumps.isEmpty()){
            return moves;
        }
        return jumps;
    }


    private void generate_moves(int origin, int delta1, int delta2, List<Move> moves, List<Move> jumps, boolean king){
        calculate_jumps("" + origin, this.board, origin, delta1, delta2, jumps, king);
        if(jumps.isEmpty()){
            if(can_move(origin, delta1, this.board)){
                String act = origin + "," + (origin + delta1);
                moves.add(new Move3(act));
            }
            if(can_move(origin, delta2, this.board)){
                String act = origin + "," + (origin + delta2);
                moves.add(new Move3(act));
            }
            if(king && can_move(origin, -1 * delta1, this.board)){
                String act = origin + "," + (origin - delta1);
                moves.add(new Move3(act));
            }
            if(king && can_move(origin, -1 * delta2, this.board)){
                String act = origin + "," + (origin - delta2);
                moves.add(new Move3(act));
            }
        }
    }


    private void calculate_jumps(String path, int[] b, int orig, int delta1, int delta2, List<Move> jumps, boolean king){
        if(!any_jumps(orig, delta1, delta2, b, king)){
            if(path.contains(",")){
                jumps.add(new Move3(path));
            }
            return;
        }
        if(can_jump(orig, delta1, b)){
            int jump = orig + 2 * delta1;
            int[] b2 = b.clone();
            b2[orig + delta1] = 0;
            b2[orig + 2 * delta1] = b2 [orig];
            b2[orig] = 0;
            calculate_jumps(path + "," + jump, b2, jump, delta1, delta2, jumps, king);
        }
        if(can_jump(orig, delta2, b)){
            int jump = orig + 2 * delta2;
            int[] b3 = b.clone();
            b3[orig + delta2] = 0;
            b3[orig + 2 * delta2] = b3[orig];
            b3[orig] = 0;
            calculate_jumps(path + "," + jump, b3, jump, delta1, delta2, jumps, king);
        }
        if(king && can_jump(orig, -1 * delta1, b)){
            int jump = orig + -2 * delta1;
            int[] b4 = b.clone();
            b4[orig + (-1 * delta1)] = 0;
            b4[orig + (-2 * delta1)] = b4[orig];
            b4[orig] = 0;
            calculate_jumps(path + "," + jump, b4, jump, delta1, delta2, jumps, king);
        }
        if(king && can_jump(orig, -1 * delta2, b)){
            int jump = orig + -2 * delta2;
            int[] b5 = b.clone();
            b5[orig + -1 * delta2] = 0;
            b5[orig + (-2 * delta2)] = b5[orig];
            b5[orig] = 0;
            calculate_jumps(path + "," + jump, b5, jump, delta1, delta2, jumps, king);
        }
    }


    public CheckersGameState3 result(Move x){
        int[] newState = this.board.clone();
        int type = this.board[x.source()];
        newState[x.source()] = 0;
        newState[x.destination()] = type;
        if(x.destination() < 4 &&  this.player == 2){
            newState[x.destination()] = 4;
        }
        else if(x.destination() > 30 && this.player == 1){
           newState[x.destination()] = 3;
        }
        if(x.captures() != null){
            for(int k: x.captures()){
                newState[k] = 0;
            }
        }
        return new CheckersGameState3(other(this.player), newState);
    }

    private int other(int player){
        if(player == 1){
            return 2;
        }
        else{
            return 1;
        }
    }

   public boolean myPiece(int i, int player){
        if(i == player || i == player +2){
            return true;
        }
        else{
            return false;
        }
      ///if(this.player == 1 && i == 1 || this.player == 1 && i == 3) //black
      ///  return true;
      ///if(this.player == 0 && i == 2 || this.player == 0 && i == 4) //white
      ///  return true;
      ///else return false;
   }

   public double pieceRatio(int player){
     double total = 0.0;
     double mypieces = 0.0;
     for(int i = 0; i<this.board.length; i++){
       if(i%9!=8){
         if(this.board[i] != 0 )    total+=1.0;
         if(myPiece(this.board[i], player)) mypieces+=1.0;
       }
     }
     //System.out.println("" + mypieces);
     return mypieces/total;
   }

   //features
   public boolean isLoner(int pos){
	   if(this.board[pos-5] == 0 && this.board[pos-4] ==0 && this.board[pos+4] ==0 && this.board[pos+5] ==0){
		   return true;
	   }
	   return false;
   }
   public boolean isSafe(int pos){
	   if(pos%9==4 || pos%9==3){
		   return true;
	   }
	   return false;
   }

   public boolean isTerminal(){
       double rat = pieceRatio(this.player);
       return (rat == 0 || rat == 1);
    }

   public void printState(){
        boolean leading = false;
        int printed = 0;
        for(int i = 0; i < this.board.length; i++){
            if(i % 9 != 8){
                if(leading){
                    System.out.print(character(this.board[i]) + "-");
                }
                else{
                    System.out.print("-" + character(this.board[i]));
                }
                printed++;
            }
            if(printed == 4){
                leading = !leading;
                printed = 0;
                System.out.print("\n");
            }
        }
        System.out.print("\n");
    }
}
