import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Test{

    public static void main(String[] args){
        String[] b = {"-", "b", "-", "b", "-", "b", "-", "b",
                      "b", "-", "b", "-", "b", "-", "b", "-",
                       "-", "b", "-", "b", "-", "b", "-", "b",
                       "-", "-", "-", "-", "-", "-", "-", "-",
                       "-", "-", "-", "-", "-", "-", "-", "-",
                       "w", "-", "w", "-", "w", "-", "w", "-",
                       "-", "w", "-", "w", "-", "w", "-", "w",
                       "w", "-", "w", "-", "w", "-", "w", "-"};

        String[] b2 = {"-", "-", "-", "b", "-", "b", "-", "b",
                      "b", "-", "w", "-", "b", "-", "b", "-",
                       "-", "-", "-", "B", "-", "-", "-", "b",
                       "-", "-", "w", "-", "w", "-", "-", "-",
                       "-", "-", "-", "-", "-", "-", "-", "-",
                       "w", "-", "w", "-", "w", "-", "w", "-",
                       "-", "w", "-", "-", "-", "w", "-", "w",
                       "w", "-", "w", "-", "w", "-", "w", "-"};

        String[] b3 = {"-", "-", "-", "b", "-", "b", "-", "b",
                      "b", "-", "b", "-", "b", "-", "b", "-",
                       "-", "b", "-", "w", "-", "b", "-", "b",
                       "-", "-", "b", "-", "w", "-", "-", "-",
                       "-", "-", "-", "b", "-", "-", "-", "-",
                       "w", "-", "b", "-", "w", "-", "w", "-",
                       "-", "w", "-", "-", "-", "w", "-", "w",
                       "-", "-", "w", "-", "w", "-", "w", "-"};

        String[] b4 = {"-", "-", "-", "b", "-", "b", "-", "b",
                      "b", "-", "w", "-", "b", "-", "b", "-",
                       "-", "-", "-", "B", "-", "-", "-", "b",
                       "-", "-", "w", "-", "w", "-", "-", "-",
                       "-", "-", "-", "-", "-", "-", "-", "-",
                       "w", "-", "w", "-", "w", "-", "w", "-",
                       "-", "w", "-", "-", "-", "w", "-", "w",
                       "w", "-", "w", "-", "w", "-", "w", "-"};

        String[] b5 = {"-", "-", "-", "-", "-", "-", "-", "-",
                      "-", "-", "-", "-", "b", "-", "-", "-",
                       "-", "-", "-", "w", "-", "-", "-", "-",
                       "-", "-", "-", "-", "-", "-", "-", "-",
                       "-", "w", "-", "w", "-", "-", "-", "-",
                       "-", "-", "-", "-", "-", "-", "-", "-",
                       "-", "w", "-", "w", "-", "-", "-", "-",
                       "-", "-", "-", "-", "-", "-", "-", "-"};


        //Random r = new Random();

        //CheckersGameState s1 = new CheckersGameState3(1, b);
        //CheckersGameState s2 = new CheckersGameState3(2, b);
        //CheckersGameState s3 = new CheckersGameState3(1, b2);
        //CheckersGameState s4 = new CheckersGameState3(2, b2);
        //CheckersGameState s5 = new CheckersGameState3(1, b3);
        //CheckersGameState s6 = new CheckersGameState3(2, b3);
        CheckersGameState s7 = new CheckersGameState3(1, b4);
        Evaluator eval = new Evaluator00();
        CheckersAI ai = new CheckersAI(eval, 1);
        s7.printState();
        for(Move m: s7.actions()){
            System.out.println(m);
        }
        System.out.println(ai.minimax(s7, 3));
        //System.out.println("State 1");
        //printMoves(s1);
        //System.out.println("State 2");
        //printMoves(s2);
        //System.out.println("State 3");
        //printMoves(s3);
        //System.out.println("State 4");
        //printMoves(s4);
        //System.out.println("State 5");
        //printMoves(s5);
        //System.out.println("Result of State 5");
        //s5.result(s5.actions().get(0)).printState();
        //System.out.println("State 6");
        //printMoves(s6);
        //System.out.println("Result of State 6");
        //s6.result(s6.actions().get(0)).printState();
        //System.out.println("State 7");
        //printMoves(s7);
        //System.out.println("Result of State 7");
        //s7.result(s7.actions().get(0)).printState();
        //System.out.println("Playing game");
        //CheckersGameState s8 = new CheckersGameState3(1, b5);
        //printMoves(s8);
        //s8.result(s8.actions().get(0)).printState();
        //CheckersGameState state = new CheckersGameState3(1, b);
        //state.printState();
        //List<Move> moves = state.actions();
        //while(moves.size() > 0){
        // System.out.println("Possible actions: ");
        // for(Move move : moves) {
        //   System.out.println(move);
        // }

        //Move action = moves.get(r.nextInt(moves.size()));
        //System.out.println("Chosen action: " + action);
        //state = state.result(action);
        //state.printState();
        //moves = state.actions();
    }

    static void printMoves(CheckersGameState s){
        s.printState();
        for(Move m: s.actions()){
            System.out.println(m);
        }
        System.out.println();
    }
}
