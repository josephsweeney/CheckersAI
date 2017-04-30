import java.util.Random;

public class Learn{
    CheckersAI alpha;
    CheckersAI beta;
    LearningEvaluator le;
    EndEvaluator endle;
    BaseEvaluator be;
    EndEvaluator endbe;

    public static void main(String[] args){
        Learn learn = new Learn();
        learn.learn();
    }
    public Learn(){
      alpha = new CheckersAI(le, 1);
      beta = new CheckersAI(be, 2);
      le =  new LearningEvaluator("../src/weights/alpha.csv");
      be = new BaseEvaluator("../src/weights/beta.csv");
      endle = new EndEvaluator("../src/weights/endalpha.csv");
      endbe = new EndEvaluator("../src/weights/endbeta.csv");
    }

    // need to decide what to do if we are going on the wrong track
    // samuel resets one of the weights to be zero


    // for draws, make function called is_improved that checks if piece count is greater than 4 (king is worth 2)
    // for learning rate, first 30 with .1, next 30 with .05, then final 30 with .01 and see what happens

    public void learn(){
        final int num_games = 30;
        final int iterations = 7;

        Random rand = new Random();
        for(int j = 0; j < iterations; j++){
            for(int i = 1; i <= num_games; i++){ // play num_games amount of games
                alpha.eval = le;
                beta.eval = be;
                System.out.println("playing game " + i);
                int player = rand.nextInt(2) + 1; // choose which player alpha plays as
                play(player, true); // alpha and beta play a game
                le.updateWeights(learningParameter(i, num_games)); // get new weights using data from game
                endle.updateWeights(learningParameter(i, num_games)); // get new weights using data from game
                //le.updateWeights(.1); // get new weights using data from game
            }
            faceBeta();
        }
    }

    public static double learningParameter(int played, int games){
        if(played <= .80 * games){
            return .10;
        }
        else{
            double remaining = games - .80 * games;
            double divisor = remaining - (games - played);
            return .10/(divisor);
        }
    }

    public void faceBeta(){
        boolean w1;
        boolean w2;
        CheckersGameState s;
        System.out.println("facing beta");
        s = new CheckersGameState3();
        w1 = play(1, false);
        w2 = play(2, false);

        System.out.println("alpha won " + w1 + " " + w2);
        if(w1 && w2){
            System.out.println("updating beta");
            le.commitWeights("../src/weights/beta.csv");
            endle.commitWeights("../src/weights/endbeta.csv");
            be.refreshWeights();
            endbe.refreshWeights();
        }
        else{
            be.commitWeights("../src/weights/alpha.csv");
            le.refreshWeights();
            endbe.commitWeights("../src/weights/endalpha.csv");
            endle.refreshWeights();
        }



    }

    public boolean play(int player, boolean learning){
        int min_ply = 7;
        int incr_ply = 0;
        boolean switchedToEndGame = false;
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
            if(current.isEndGame() && !switchedToEndGame){
              switchedToEndGame = true;
              alpha.eval = endle;
              beta.eval = endbe;
              min_ply += incr_ply;
            }
            Move next = alpha.minimax(current, min_ply); // get alpha's move
            moves++;
            if(secondlast != null && next.toString().equals(secondlast.toString())){
                same_moves++;
            }
            secondlast = lastmove;
            lastmove = next;
            if(learning && !switchedToEndGame){
                le.addData(current.getFeatures(alpha.getPlayer()), next.getValue()); // add this moves data to the data set (the value of the state is stored in the move. there is probably a better way to do this)
            }
            else if(learning && switchedToEndGame){
              endle.addData(current.getEndGameFeatures(alpha.getPlayer()), next.getValue());
            }
            current = current.result(next); // make the move
            moves++;
            //current.printState();
            if(current.isTerminal()){ // if alpha won, then break
                break;
            }
            current = current.result(beta.minimax(current, min_ply)); // beta's move

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
