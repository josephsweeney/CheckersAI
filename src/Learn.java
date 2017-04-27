import java.util.Random;

public class Learn{

    public static void main(String[] args){
        LearningEvaluator le =  new LearningEvaluator("../src/weights/alpha.csv");
        BaseEvaluator be = new BaseEvaluator("../src/weights/beta.csv");
        CheckersAI alpha = new CheckersAI(le, 1);
        CheckersAI beta = new CheckersAI(be, 2);
        learn(alpha, beta, le, be);

    }

    // need to decide what to do if we are going on the wrong track
    // samuel resets one of the weights to be zero


    // for draws, make function called is_improved that checks if piece count is greater than 4 (king is worth 2)
    // for learning rate, first 30 with .1, next 30 with .05, then final 30 with .01 and see what happens

    public static void learn(CheckersAI alpha, CheckersAI beta, LearningEvaluator le, BaseEvaluator be){
        final int num_games = 5;
        final int iterations = 20;

        Random rand = new Random();
        for(int j = 0; j < iterations; j++){
            for(int i = 1; i <= num_games; i++){ // play num_games amount of games
                System.out.println("playing game " + i);
                int player = rand.nextInt(2) + 1; // choose which player alpha plays as
                play(alpha, beta, le, player, true); // alpha and beta play a game
                le.updateWeights(.1); // get new weights using data from game
            }
            faceBeta(alpha, beta, le, be);
        }
    }

    public static void faceBeta(CheckersAI alpha, CheckersAI beta, LearningEvaluator le, BaseEvaluator be){
        boolean w1;
        boolean w2;
        CheckersGameState s;
        System.out.println("facing beta");
        s = new CheckersGameState3();
        w1 = play(alpha, beta, le, 1, false);
        w2 = play(alpha, beta, le, 2, false);

        System.out.println("alpha won " + w1 + " " + w2);
        if(w1 && w2){
            System.out.println("updating beta");
            le.commitWeights("../src/weights/beta.csv");
            be.refreshWeights();
        }
        else{
            be.commitWeights("../src/weights/alpha.csv");
            le.refreshWeights();
        }



    }



    public static boolean play(CheckersAI alpha, CheckersAI beta, LearningEvaluator le, int player, boolean learning){
        CheckersGameState current = new CheckersGameState3();
        int other = 1 - (player - 1)  + 1;
        alpha.setPlayer(player);
        beta.setPlayer(other);
        int moves = 0;
        if(other == 1){ // if beta goes first, make a move
            current = current.result(beta.minimax(current, 7));
            moves++;
        }
        int same_moves = 0;
        Move lastmove = null;
        Move secondlast = null;
        while(!current.isTerminal() && same_moves <= 3 && moves <= 200){
            Move next = alpha.minimax(current, 7); // get alpha's move
            moves++;
            if(secondlast != null && next.toString().equals(secondlast.toString())){
                same_moves++;
            }
            secondlast = lastmove;
            lastmove = next;
            if(learning){
                le.addData(current.getFeatures(alpha.getPlayer()), next.getValue()); // add this moves data to the data set (the value of the state is stored in the move. there is probably a better way to do this)
            }
            current = current.result(next); // make the move
            moves++;
            //current.printState();
            if(current.isTerminal()){ // if alpha won, then break
                break;
            }
            current = current.result(beta.minimax(current, 7)); // beta's move

        }
        current.printState();
        System.out.println("playing as " + alpha.getPlayer());
        return (current.winner() == alpha.getPlayer() || improved(current, alpha.getPlayer()));
    }

    public static boolean improved(CheckersGameState i, int player){
        CheckersGameState3 s = (CheckersGameState3) i;
        return s.numPieces(player) >= (2 + s.numPieces(1 + (1 - (player-1))));
    }

}
