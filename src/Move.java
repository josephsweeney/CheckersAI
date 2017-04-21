
public interface Move {
  public int source();
  public int destination();
  // Returns a list of captured positions
  public int[] captures();
  // Returns the string representation of a move
  public String toString();

  boolean isJump();

  public void setValue(double value);

  public double getValue();


}
