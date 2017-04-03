
public interface Move {
  // Starting and ending Positions
  // Returns a list of captured positions
  public int[] captures();
  // Returns the string representation of a move
  public String toString();

}
