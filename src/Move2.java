
public class Move2 implements Move {
  public int src, dest;
  public int[] path;
  public int captures;

  public Move2(int src, int dest) {
    this.src = src;
    this.dest = dest;
    path = new int[] {src, dest};
  }
  public Move2(int[] p) {
    src = p[0];
    dest = p[p.length - 1];
    path = p;
  }

  public int source() {
    return src;
  }

  public int destination() {
    return dest;
  }

  private int[] neighbors(int index) {
    int[] neighbors = new int[4];
    int row = (index - index%4)/4;
    int col = index%4;
    if (row % 2 == 0) {
      neighbors[0] = index + 4;
      neighbors[1] = index + 5;
      neighbors[2] = index - 4;
      neighbors[3] = index - 3;

      if(col == 3) {
        neighbors[1] = -1;
        neighbors[3] = -1;
      }
    }
    else {
      neighbors[0] = index + 3;
      neighbors[1] = index + 4;
      neighbors[2] = index - 5;
      neighbors[3] = index - 4;

      if(col == 0) {
        neighbors[0] = -1;
        neighbors[2] = -1;
      }
    }

    for(int i = 0; i<4; i++) {
      if(neighbors[i] < 0 || neighbors[i] >=32) {
        neighbors[i] = -1;
      }
    }

    return neighbors;
  }

  public int[] captures() {
    int[] captures = new int[path.length-1];
    for(int i = 0; i<path.length-1; i++) {
      int[] neighbors = neighbors(path[i]);
      boolean direct = false;
      for(int j = 0; j<4; j++) {
        if(neighbors[j] == path[i+1]){
          direct = true;
          break;
        }
        else if(neighbors[j] != -1 && neighbors(neighbors[j])[j] == path[i+1]) {
          captures[i] = neighbors[j];
        }
      }
      if(direct){
        captures[i] = -1;
      }
    }
    return captures;
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
      output += i!=0 ? ":" : "";
      output += convert(path[i]);
    }
    return output;
  }
}
