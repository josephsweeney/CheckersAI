import java.util.Arrays;

public class Test{

    public static void main(String[] args){
        String[] b = {"-", "b", "-", "b", "-", "b", "-", "b",
                      "-", "-", "-", "-", "B", "-", "b", "-",
                       "-", "w", "-", "w", "-", "b", "-", "-",
                       "-", "-", "-", "-", "b", "-", "w", "-",
                       "-", "-", "-", "-", "-", "-", "-", "-",
                       "w", "-", "w", "-", "w", "-", "w", "-",
                       "-", "w", "-", "-", "-", "w", "-", "w",
                       "w", "-", "w", "-", "w", "-", "w", "-"};
        String[] b2 = {"-", "-", "-", "b", "-", "b", "-", "b",
                      "b", "-", "w", "-", "b", "-", "b", "-",
                       "-", "b", "-", "B", "-", "b", "-", "b",
                       "-", "-", "w", "-", "w", "-", "-", "-",
                       "-", "-", "-", "-", "-", "-", "-", "-",
                       "w", "-", "w", "-", "w", "-", "w", "-",
                       "-", "w", "-", "-", "-", "w", "-", "w",
                       "w", "-", "w", "-", "w", "-", "w", "-"};

        CheckersGameState3 s2 = new CheckersGameState3(1, b2);
        s2.printState();
        s2.result(s2.actions().get(1)).printState();
        for(Move m: s2.actions()){
            System.out.println(m);
        }
    }
}
