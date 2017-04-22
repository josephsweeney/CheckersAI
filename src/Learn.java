import java.util.Random;

public class Learn{

    public static void main(String[] args){

        final int num_games = 15;
        LearningEvaluator le =  new LearningEvaluator("../src/weights/alpha.csv", .1);
        BaseEvaluator be = new BaseEvaluator("../src/weights/beta.csv");
        CheckersAI alpha = new CheckersAI(le, 1);
        CheckersAI beta = new CheckersAI(be, 2);
        CheckersGameState s;

        int played = 0;
        int won = 0;
        int winner;

        for(int i = 0; i < num_games; i++){ // play num_games amount of games
            s = new CheckersGameState3();
            winner = play(s, alpha, beta, le); // alpha and beta play a game
            le.updateWeights(); // get new weights using data from game
            played++;
            if(winner == alpha.getPlayer()){
                won++;
            }
            if(played == 10){
                if(won >= 7){ // if alpha wins 7 of every ten games, make beta use alpha's new evaluator
                    le.commitWeights("../src/weights/beta.csv");
                    be.refreshWeights();
                }
                played = 0;
                won = 0;
            }
        }

    }

    // need to decide what to do if we are going on the wrong track
    // samuel resets one of the weights to be zero




    public static int play(CheckersGameState s, CheckersAI alpha, CheckersAI beta, LearningEvaluator le){
        System.out.println("playing");
        CheckersGameState current = s;
        int moves = 0; // draw after 200 moves
        Random rand = new Random();
        int player = rand.nextInt(2) + 1; // choose which player alpha plays as
        int other = 1 - (player - 1)  + 1;
        System.out.println("playing as " + player);
        alpha.setPlayer(player);
        beta.setPlayer(other);
        current.printState();
        if(other == 1){ // if beta goes first, make a move
            current = current.result(beta.minimax(current, 7));
            current.printState();
            moves++;
        }
        while(!current.isTerminal() && moves <= 50){
            System.out.println("alphas moves:");
            System.out.println(current.actions());
            Move next = alpha.minimax(current, 7); // get alpha's move
            le.addData(current.getFeatures(alpha.getPlayer()), next.getValue()); // add this moves data to the data set (the value of the state is stored in the move. there is probably a better way to do this)
            current = current.result(next); // make the move
            current.printState();
            moves++;
            if(current.isTerminal()){ // if alpha won, then break
                break;
            }
            current = current.result(beta.minimax(current, 7)); // beta's move
            current.printState();
            moves++;

        }
        return current.winner();
    }
}
