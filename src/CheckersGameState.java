import java.util.List;
public interface CheckersGameState {
String player ();
List < Move > actions ();
CheckersGameState result ( Move x );
boolean isTerminal();
void printState ();
public double[] getFeatures(int player);
}
