import java.util.List;
public interface CheckersGameState {
String player ();
List < Move > actions ();
CheckersGameState result ( Move x );
boolean isTerminal();
int winner();
void printState ();
public double[] getFeatures(int player);
public boolean isEndGame();
public double[] getEndGameFeatures(int player);
}
