import java.util.List;
import java.util.LinkedList;

public class CheckersGameState3{

    int player;
    int[] board;

    public CheckersGameState3(int player, int[] board){
        this.player = player;
        this.board = board;
    }

    String player(){
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
        LinkedList<Move> actions = new LinkedList<Move>();
        for(int i = 0; i < this.board.length; i++){
            if(current_player(i, this.board)){
                if(this.player == 1){
                    if(this.board[i] == 3){
                        generate_moves(i, 4, 5, actions, true);
                    }
                    else{
                        generate_moves(i, 4, 5, actions, false);
                    }
                }
                else{
                    if(this.board[i] == 4){
                        generate_moves(i, -4, -5, actions, true);
                    }
                    else{
                        generate_moves(i, -4, -5, actions, false);
                    }
                }
            }
        }
        return actions;
    }


    private void generate_moves(int origin, int delta1, int delta2, List<Move> actions, boolean king){
        if(can_move(origin, delta1, this.board)){
            String act = origin + "," + (origin + delta1);
            actions.add(new Move(act));
        }
        if(can_move(origin, delta2, this.board)){
            String act = origin + "," + (origin + delta2);
            actions.add(new Move(act));
        }
        if(king && can_move(origin, -1 * delta1, this.board)){
            String act = origin + "," + (origin - delta1);
            actions.add(new Move(act));
        }
        if(king && can_move(origin, -1 * delta2, this.board)){
            String act = origin + "," + (origin - delta2);
            actions.add(new Move(act));
        }
        calculate_jumps("" + origin, this.board, origin, delta1, delta2, actions, king);
    }


    private void calculate_jumps(String path, int[] b, int orig, int delta1, int delta2, List<Move> actions, boolean king){
        if(!any_jumps(orig, delta1, delta2, b, king)){
            if(path.contains(",")){
                actions.add(new Move(path));
            }
            return;
        }
        if(can_jump(orig, delta1, b)){
            int jump = orig + 2 * delta1;
            int[] b2 = b.clone();
            b2[orig + delta1] = 0;
            calculate_jumps(path + "," + jump, b2, jump, delta1, delta2, actions, king);
        }
        if(can_jump(orig, delta2, b)){
            int jump = orig + 2 * delta2;
            int[] b3 = b.clone();
            b3[orig + delta2] = 0;
            calculate_jumps(path + "," + jump, b3, jump, delta1, delta2, actions, king);
        }
        if(king && can_jump(orig, -1 * delta1, b)){
            int jump = orig + -2 * delta1;
            int[] b4 = b.clone();
            b4[orig + (-1 * delta1)] = 0;
            calculate_jumps(path + "," + jump, b4, jump, delta1, delta2, actions, king);
        }
        if(king && can_jump(orig, -1 * delta2, b)){
            int jump = orig + -2 * delta2;
            int[] b5 = b.clone();
            b5[orig + -1 * delta2] = 0;
            calculate_jumps(path + "," + jump, b5, jump, delta1, delta2, actions, king);
        }
    }


    CheckersGameState3 result(Move x){
        int[] newState = this.board.clone();
        newState[x.destination()] = this.board[x.origin()];
        newState[x.origin()] = 0;
        if(x.destination < 4 &&  this.player == 2){
            newState[x.destination()] = 4;
        }
        else if(x.destination() > 30 && this.player == 1){
           newState[x.destination()] = 3;
        }
        if(x.kills() != null){
            for(int k: x.kills()){
                newState[k] = 0;
            }
        }
        return new CheckersGameState3(1 - this.player, newState);
    }

    void printState(){
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


