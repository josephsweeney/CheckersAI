import java.util.ArrayList;

public class Move1 implements Move{
	//(x,y) current position, (x2,y2) position move to
    ArrayList<String> moves;

	public Move1(ArrayList<String> moves){
        this.moves = moves;
	}

    private int parseX(String move){
        return Integer.parseInt(move.split(":")[0]);
    }
    private int parseY(String move){
        return Integer.parseInt(move.split(":")[1]);
    }

    public int[] captures(){
        return null;
    }

    public ArrayList<String> kills(){
        ArrayList<String> captures = new ArrayList<String>();
        for(int i = 0; i<moves.size()-1; i++){
            if(Math.abs((parseX(moves.get(i)) - parseX(moves.get(i+1)))) == 2 || Math.abs((parseY(moves.get(i)) - parseY(moves.get(i+1)))) == 2){
                captures.add(averageX(moves.get(i), moves.get(i+1)) +","+averageY(moves.get(i), moves.get(i+1)));
            }
        }
        return captures;
    }

    private int averageX(String a, String b){
        return (parseX(a) + parseX(b))/2;
    }
    private int averageY(String a, String b){
        return (parseY(a) + parseY(b))/2;
    }

    public int destination(){
        return 0;
    }

    public int source(){
        return 0;
    }

    public int sourceX(){
        return parseX(moves.get(0));
    }

    public int sourceY(){
        return parseY(moves.get(0));
    }

    public int destX(){
        return parseX(moves.get(moves.size() -1));
    }
    public int destY(){
        return parseY(moves.get(moves.size() -1));
    }

	public String toString(){
        String ret = "";
        for(int i = 0; i < moves.size() -1; i++){
           ret += "(" + moves.get(i) + "):";
        }
       ret += "(" +moves.get(moves.size() -1) + ")";
      return ret;
	}
}
