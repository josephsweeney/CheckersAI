public class Move3{

    int origin, destination;
    String[] steps;
    int[] kills;
    String check;

    public Move3(String steps){
        String[] s = steps.split(",");
        this.steps = s;
        this.origin = Integer.parseInt(s[0]);
        this.destination = Integer.parseInt(s[s.length - 1]);
        kills = calculate_kills(s);
    }

    public int[] calculate_kills(String[] steps){
        int diff = this.origin - this.destination;
        if(Math.abs(diff) == 4 || Math.abs(diff) == 5){
            return null;
        }
        int[] k = new int[steps.length - 1];
        for(int i = 0; i < steps.length - 1; i++){
           k[i] = (Integer.parseInt(steps[i]) + Integer.parseInt(steps[i+1]))/2;
        }
        return k;
    }

    public int origin(){
        return origin;
    }

    public int destination(){
        return destination;
    }

    public int[] kills(){
        return kills;
    }

//    private String convert(int pos){
//        int x;
//        int y;
//        if(pos < 8){
//            int x = 2 * p + 1;
//            int y = pos / 4;
//        }
//        else if(pos < 17){
//            int x = 
//
   public String toString(){
        String move = "(" + this.steps[0] + ":" + this.steps[1] + ")";
        if(this.steps.length > 2){
            for(int i = 1; i < this.steps.length - 1; i++){
               move += ":(" + this.steps[i] + ":" + this.steps[i+1] + ")";
            }
        }
        return move;
    }

}
