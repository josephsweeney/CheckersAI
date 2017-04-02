
public class Move2 implements Move {
  public int src, dest;
  public int[] path;
  public int captures;

  public Move2(int src, int dest, int capture) {
    this.src = src;
    this.dest = dest;
    path = new int[] {src, dest};
    this.captures = capture;
  }
  public Move2(int[] p, int capture) {
    src = p[0];
    dest = p[p.length - 1];
    path = p;
    this.captures = capture;
  }

  private String convert(int position) {
    // Converts a position to the correct String
    int col = position % 4;
    int row = (position - col) / 4;
    col = col * 2;
    if(row%2 == 0) {
      col ++;
    }
    return "("+(7-row)+":"+col+")";
  }

  public String toString() {
    String output = "";
    for(int i = 0; i<path.length; i++) {
      if(i!=0){output += ":";}
      output += convert(path[i]);
    }
    return output;
  }
}
