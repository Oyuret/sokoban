
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Mattias Lidén
 * Last modified: 2013-09-26
 * 
 */

/*
 * The main purpose of this class is to test if a provided answer really leads to a final state where all the boxes are on every goal.
 * 
 * The algorithm isnt that advanced.
 * It reads the map, reads the moves and then try to move the player according to each step of the moves provided.
 * Every step is checked for validity. The test is stopped if a step is invalid (for example sokoban tries to walk into a wall) since the path cannot lead to a legal success.
 * 
 * 
 * How to use this MapTester:
 * In order to read the maps it needs the url to the maps in perhaps dropbox
 * 
 * Everything in this Class is static.
 * You can either call the TestMap.testSolution() method with the given arguments: 
 *   .testSolution(  map (char[][]) ,   the solution (String: UURDLUU) ,  say if you want to print the solutiontest stepwise (true/false))
 * for example MapTester.testSolution( 23, "UURR", true) would test the solution "UURR" on map 23 and print every step along the way
 * 
 * You can also run the main of the class.
 * Main is programmed in such way that it will test the given maps in order of the range given underneith and print whether or not the map is solved correctly, or it got out of memory.
 * 
 * IF you run the main, then you might want to change the static private variables by hand, No arguments is taken at the moment. Might be up for a change lateron.
 * 
 */
public class MapTester {
    
    private static int pR = -1, pC = -1;
    private static String myUrlToMapsInDropbox = "D:/Dropbox/DD2380-2013/Code/100maps/test";
    private static int startMap = 71;
    private static int finalMap = 78;
    private static int timeLimit = 11; //11 seconds
    private static int[] unsolvedMapsDueToTimeLimit = new int[finalMap-startMap +1];
    private static int[] mapsWithWrongAnswer = new int[finalMap-startMap +1];
    private static int[] solvedMaps = new int[finalMap-startMap +1];
    
    private static boolean printProgramOutput = false;  // If this is true, you'll get the program respons printed after solution is tested
    private static boolean printProgramOutputWhenWrongAnswer = false; //this is to see if the wrong answer is wrong or has an errorMessage
    private static boolean showSteps = false;           // Mark this as true if you want to see every step on the map the solution will provide
    private static boolean printIfSolved = false;         // If true, map will show if solved or not, if false, only time spent will be showed.
    
    private static char[][] getMap(int mapNumber) throws FileNotFoundException, IOException{
        
        /* this makes sure the numberformat is correct with numberlength changes*/
          String mapNumberTransformation = "00" + Integer.toString(mapNumber);
          mapNumberTransformation = mapNumberTransformation.substring(mapNumberTransformation.length()-3);
        /*  */
        
        //Find the parsed map according to a given number
        BufferedReader br = new BufferedReader(new FileReader( myUrlToMapsInDropbox + mapNumberTransformation + ".in"));
        
        //basic variable definition
        String[] mapRows = new String[200];
        String maprow;
        int maxColLen = -1;
        int rowLen = 0;
        
        //To read the map and save it as a char[][] i need to know which row has the most amount of signs, 
        //As long as theres another row to read, read it and see if is is longer than the longest row found
        
        for(; (maprow = br.readLine()) != null; rowLen++ ){
            mapRows[rowLen] = maprow;
            
            maxColLen = Math.max(maxColLen, maprow.length());
        }
        
        //now that sizes are know, its time to actually save the map.
        char[][] map = new char[rowLen][maxColLen];
        for(int r = 0; r < rowLen ; r++){
            for(int c = 0; c < mapRows[r].length(); c++){

                map[r][c] = mapRows[r].charAt(c);
                
                //if the player is found, then the startpositino is found for the testing
                if(mapRows[r].charAt(c)=='@' || mapRows[r].charAt(c)=='+'){
                    pR = r;
                    pC = c;
                }
            }
            for (int c= mapRows[r].length(); c < maxColLen; c++){
                map[r][c] = ' ';  
            }
        }
        return map;
    }

    public static boolean testSolution(int mapNumber, String solution, boolean printSteps) throws FileNotFoundException, IOException{
        
        char[][] map = getMap(mapNumber);
        char[] steps = solution.toCharArray();
        int r,c;
        boolean validMove = true;
        for(char step: steps){
            
            //Move up
            if(step == 'u' || step == 'U'){
                validMove = movePlayer((r=-1), (c = 0), map);
            }
            //Move right
            else if(step == 'r' || step == 'R'){
                validMove =movePlayer((r=0), (c = 1), map);
                
            }
            //Move down
            else if((step == 'd' || step == 'D')){
                validMove = movePlayer((r=1), (c = 0), map);                
            }
            //Move Left
            else if (step == 'l' || step == 'L'){
                validMove = movePlayer((r = 0) , (c = -1), map);
            }
            //Not a valid move
            else {
                System.out.println("I read somthing i couldnt recognize as a valid move: '" + step +"'" );
               return false;
            }
            
            //If every step should be printed (for better followup, print the step and the map
            if(printSteps){
                System.out.println("move: " + step);
                printMap(map);
            } 
            //If theres an unmovable obstacle in the way
            if(!validMove){
                System.out.println("You wanted to do the following move: '" + step + "', but i cannot see how that move could be possible!");
                break;
            }
            
        }
        //All moves done and everything went ok. Time to check if its a succesful solution.
        if(validMove){
            return checkIfAllGoalsAreCovered(map);
        
        }
        return false;
    }
    
    private static boolean checkIfAllGoalsAreCovered(char[][] map){
        //Go through the map and see if there is any goal without a box
                
        for(int r = 0; r < map.length; r++){
            for (int c= 0; c< map[0].length; c++){
                if(map[r][c] == '.' || map[r][c] == '+'){
                    return false;
                }
            }
        }        
        return true;
    }
        
    private static boolean movePlayer(int rowChange,int colChange, char[][] map){
        
        //if player isnt in the anticipated position
        if(map[pR][pC] != '@' && map[pR][pC] != '+'){
            System.out.print("I couldnt find the player, ");
            System.out.println("instead I found " +map[pR][pC] + " " + (map[pR][pC] != '@'));
            return false;
        }
        
        //Player moving freely over free spaces or goals
        //if the given move is to an empty square, just do the move
        if(map[pR+rowChange][pC+colChange]== ' '){
            map[pR+rowChange][pC+colChange] = '@';
            map[pR][pC] = (map[pR][pC] == '@') ? ' ' : '.'; //player can either stand on a free field or a goal.
            pR = pR + rowChange;
            pC = pC + colChange;
            return true;
        }
        
        // if the given move is to an empty goal, do the move, but adapt the player sign.
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
    
    private static void printMap(char[][] map){
        System.out.println(convertMapToString(map));
    }
    
    private static String convertMapToString(char[][] map){
        /* this method makes sure the map can be transfered between programs */
        
        StringBuilder sb = new StringBuilder();
        // map.length    = rowLen  
        // map[0].length = colLen
        for (int r = 0; r< map.length; r++){
            for (int c = 0; c < map[0].length; c++ ){
                sb.append(map[r][c]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException{

        int nSolvedMaps = 0,
            nMapsWrongAnswer = 0,
            nMapsOutOfTime = 0,
            nMapsTried = 0;
        
        long totalTime = 0;
        
        for (int mapNr = startMap ; mapNr <= finalMap; mapNr++){
            
            //Before Main is started, we make sure the buffered System.in is loaded with the map Main is supposed to solve
            //Otherwise the mapreading of Main must be done by hand.
            
            PipedOutputStream inwriter = new PipedOutputStream();
            PipedInputStream input = new PipedInputStream(inwriter);
            System.setIn(input);
            inwriter.write("1\n".getBytes());
            inwriter.write(convertMapToString(getMap(mapNr)).getBytes());

            //Now lets set up a reader of the output of Main to stdout before running Main
            PrintStream originalOut = System.out; // Save the old one in order to be able to use it lateron.
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream newOut = new PrintStream(baos);
            System.setOut(newOut);

            nMapsTried++;
            long startTime = System.nanoTime(); //Also a timer is needed for the application measurement.
            long timeUsed = 0;
  
            Main.walkingDistance = null;
            
 //           Main.main(null);            
    /*                
     *      If you want to test without timelimit, remove this section below and use the commented main call above instead
     *      With the timelimit you wont get to see any error messages, like nullpointerexception
     * 
     */
            boolean timeout = false;
            //running Main with a timelimit includer
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<String> future = executor.submit(new Task());
            try {
               String a = future.get(MapTester.timeLimit, TimeUnit.SECONDS);
            } catch (ExecutionException ex) {
            } catch (TimeoutException ex) {
                timeout = true;
            } catch (InterruptedException ex) {
            }
            executor.shutdownNow();
  
      /*    remove to here if you dont want the timelimit */
            
            timeUsed = System.nanoTime()-startTime;
            
            
            System.setOut(originalOut); // Returning the print ability

            //For the reading of the output. It comes in bytes and can be converted to chars that lateron is converted to a string
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            StringBuilder sb = new StringBuilder();
            char c;
            while(bais.available()>2 ){
                sb.append((char)bais.read());
            }
              
            if(!printIfSolved){
                if(timeout){
                    System.out.println(12.0);
                }
                else{
                    System.out.println(timeUsed/1000000000.0);
                }
            }
            
            //Print if map is solved
            if(MapTester.testSolution(mapNr, sb.toString(), showSteps)){
              
              if(printIfSolved){
                System.out.println("Map " + mapNr + " solved in " + (timeUsed/(double)1000000000) + " seconds." );
              }
              solvedMaps[nSolvedMaps++] = mapNr;
              totalTime += timeUsed;
            }
            else{
                if(timeout){
                    if(printIfSolved){
                        System.err.println("Map " + mapNr + " was not solved withing timelimit.");
                    }
                    unsolvedMapsDueToTimeLimit[nMapsOutOfTime++] = mapNr;
                    totalTime += timeLimit*1000000000;
                }
                else{
                    if(printIfSolved){              
                        System.err.println("Map " + mapNr + " gave wrong answer after " + (timeUsed/(double)1000000000) + " seconds.");
                    }
                    mapsWithWrongAnswer[nMapsWrongAnswer++] = mapNr;
                    totalTime += timeUsed;
                    
                    if(!printProgramOutput && (printProgramOutputWhenWrongAnswer)){
                        System.out.println("Answer given: " + sb.toString() + "\n");
                    }
                }
            }
            
            if(printProgramOutput){
                System.out.println("Answer given: " + sb.toString() + "\n");
            }
            
            inwriter.close();
            input.close();
            newOut.close();
            baos.close();
            bais.close();
        }
        
        System.out.println("\n\n*************************");
        System.out.print("Maps solved: " +nSolvedMaps + "\t\t\t{"); for(int i = 0; i < nSolvedMaps; i++){System.out.print(solvedMaps[i] + ", ");}System.out.println("}");
        System.out.print("Maps with wrong answer: " +nMapsWrongAnswer + "\t{");for(int i = 0; i < nMapsWrongAnswer; i++){System.out.print(mapsWithWrongAnswer[i] + ", ");}System.out.println("}");
        System.out.print("Maps out of time: " + nMapsOutOfTime + "\t\t{"); for(int i = 0; i < nMapsOutOfTime; i++){System.out.print(unsolvedMapsDueToTimeLimit[i]+ ", ");}System.out.println("}");
        System.out.println("-------------------------");
        System.out.println("Maps in total: " + nMapsTried);
        System.out.println("Mean time per map: " + ((totalTime/1000000000.0)/nMapsTried) + " seconds." );
        System.out.println("*************************");
        System.out.flush();
    }
}

class Task implements Callable<String> {
    @Override
    public String call() throws Exception {
        Main.main(null);
        return "ready!";
    }
}