

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Matt
 */
public class TestMap {
    
    private static int pR = -1, pC = -1;
    
    public static boolean testSolution(int mapNumber, String solution, boolean printSteps) throws FileNotFoundException, IOException{
        
        char[][] map = getMap(mapNumber);
        char[] steps = solution.toCharArray();
        int r,c;
        boolean validMove = true;
        for(char step: steps){
            
            if(step == 'u' || step == 'U'){
                validMove = movePlayer((r=-1), (c = 0), map);
            }
            else if(step == 'r' || step == 'R'){
                validMove =movePlayer((r=0), (c = 1), map);
                
            }
            else if((step == 'd' || step == 'D')){
                validMove = movePlayer((r=1), (c = 0), map);                
            }
            else if (step == 'l' || step == 'L'){
                validMove = movePlayer((r = 0) , (c = -1), map);
            }else {
               return false;
            }
                
            System.out.println("move: " + step);
            printMap(map);
            
            if(!validMove){
                System.out.println("couldnt make move: " + step);
                break;
            }
        }
        if(validMove){
            return checkIfAllGoalsAreCovered(map);
        
        }
        return false;
    }
    
    private static boolean checkIfAllGoalsAreCovered(char[][] map){
        
        for(int r = 0; r < map.length; r++){
            for (int c= 0; c< map[0].length; c++){
                if(map[r][c] == '.' || map[r][c] == '+'){
                    return false;
                }
            }
        }        
        return true;
    }
    
    public static void testMap(int map){
        
    }
    
    private static boolean movePlayer(int rowChange,int colChange, char[][] map){
        
        //if player is in the anticipated position
        if(map[pR][pC] != '@' && map[pR][pC] != '+'){
            System.out.println("couldnt find player");
            System.out.println("instead i found " +map[pR][pC] + " " + (map[pR][pC] != '@'));
            return false;
        }
        
        //if move is to an empty square, just do the move
        if(map[pR+rowChange][pC+colChange]== ' '){
            map[pR+rowChange][pC+colChange] = '@';
            map[pR][pC] = (map[pR][pC] == '@') ? ' ' : '.'; //player can either stand on a free field or a goal.
            pR = pR + rowChange;
            pC = pC + colChange;
            return true;
        }
        
        // if move is to an empty goal, do the move, but adapt the player sign.
        if(map[pR+rowChange][pC+colChange]== '.'){
            map[pR+rowChange][pC+colChange] = '+';
            map[pR][pC] = (map[pR][pC] == '@') ? ' ' : '.';
            pR = pR + rowChange;
            pC = pC + colChange;
            return true;
        }
        
        //if a box (not on goal) is in the way and will be pushed
        //4 cases, either the box is pushed to a goal, to an empty space, into another box (not possible to push), and into a wall (not possible to push)
        if(map[pR+rowChange][pC+colChange]== '$'){
                       
            //if box has an empty square in the pushdirection, just move the box and the player one step
            if(map[pR+rowChange+rowChange][pC+colChange+colChange] == ' '){
                map[pR+rowChange+rowChange][pC+colChange+colChange] = '$';
                map[pR+rowChange][pC+colChange] = '@';
                map[pR][pC] = (map[pR][pC] == '@') ? ' ' : '.';
                pR = pR + rowChange;
                pC = pC + colChange;
                return true;
            }
            
            //if a box will be pushed into a goal, push the box and move the player, adapt the sign of the box.
            if(map[pR+rowChange+rowChange][pC+colChange+colChange] == '.'){
                map[pR+rowChange+rowChange][pC+colChange+colChange] = '*';
                map[pR+rowChange][pC+colChange] = '@';
                map[pR][pC] = (map[pR][pC] == '@') ? ' ' : '.';
                pR = pR + rowChange;
                pC = pC + colChange;
                return true;
            }
            return false;            
        }
        
        //if a box (on goal) is in the way and has to be pushed
        //4 cases, either the box is pushed to a goal, to an empty space, into another box (not possible to push), and into a wall (not possible to push)
        if(map[pR+rowChange][pC+colChange]== '*'){
                       
            //if box has an empty square in the pushdirection, just move the box and the player one step
            if(map[pR+rowChange+rowChange][pC+colChange+colChange] == ' '){
                map[pR+rowChange+rowChange][pC+colChange+colChange] = '$';
                map[pR+rowChange][pC+colChange] = '+';
                map[pR][pC] = (map[pR][pC] == '@') ? ' ' : '.';
                pR = pR + rowChange;
                pC = pC + colChange;
                return true;
            }
            
            //if a box will be pushed into a goal, push the box and move the player, adapt the sign of the box.
            if(map[pR+rowChange+rowChange][pC+colChange+colChange] == '.'){
                map[pR+rowChange+rowChange][pC+colChange+colChange] = '*';
                map[pR+rowChange][pC+colChange] = '+';
                map[pR][pC] = (map[pR][pC] == '@') ? ' ' : '.';
                pR = pR + rowChange;
                pC = pC + colChange;
                return true;
            }
            return false;            
        }
        
        return false;
    }
    
    private static char[][] getMap(int mapNumber) throws FileNotFoundException, IOException{
              
        BufferedReader br = new BufferedReader(new FileReader("maps/" + mapNumber));
        String[] mapRows = new String[30];
        String maprow;
        int maxColLen = -1;
        int rowLen = 0;
        for(; (maprow = br.readLine()) != null; rowLen++ ){
            mapRows[rowLen] = maprow;
            
            maxColLen = Math.max(maxColLen, maprow.length());
        }
        
        char[][] map = new char[rowLen][maxColLen];
        for(int r = 0; r < rowLen ; r++){
            for(int c = 0; c < mapRows[r].length(); c++){

                map[r][c] = mapRows[r].charAt(c);
                
                if(mapRows[r].charAt(c)=='@' || mapRows[r].charAt(c)=='+'){
                    pR = r;
                    pC = c;
                }
            }
            for (int c= mapRows[r].length(); c < maxColLen; c++){
                map[r][c] = ' ';  
            }
        }
        printMap(map);
        return map;
    }
    private static void printMap(char[][] map){
        StringBuilder sb = new StringBuilder();
        // map.length    = rowLen 
        // map[0].length = colLen
        for (int r = 0; r< map.length; r++){
            for (int c = 0; c < map[0].length; c++ ){
                sb.append(map[r][c]);
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException{
        System.out.println("is solved: " + TestMap.testSolution(22, "DDDRR", true));
    }
}
