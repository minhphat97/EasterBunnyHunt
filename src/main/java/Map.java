import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.Scanner;

/**
 *this class will be used to read the map from the maps file and initialize the environment
 * <p>
 *it will have public 2d array for level data so still compadable with maze class
 */
 public class Map {
    private final int N_ROW = 12;
    private final int N_COL = 24;
    /**object array map*/
    public Environment[][] screenData = new Environment[N_ROW][N_COL];
    /**Containe number taht can be translated to a specific object*/
    public short[][] levelData = new short[N_ROW][N_COL];
    private final short EMPTY = 0;//used to translate map of numbers to objects
    private final short WALL = 1;
    private final short EGG = 2;
    private final short DOOR = 3;
    private final short TRAP = 7;
    private final short THORNBUSH = 8;

    /**
     *initialize map of environment objects for maze
     */
    public Map(){
        readLevel();
        initLevel();
    }

    /**
     * reads map matrix from maps text file into levelData
     * Sets the map for the game, prints exception if file not found
     */
    private void readLevel() {
        String Maps[] = {"maps/map1.txt", "maps/map2.txt"};
        int max = 2;
        int min = 1;
        Random random = new Random();
        int option = random.nextInt((max - min) + 1) + min;
        try {
            URL myObj;
            if (option == 1) {//choose random map
                myObj = this.getClass().getResource(Maps[0]);
            } else {
                myObj = this.getClass().getResource(Maps[1]);
            }
            Scanner myReader = new Scanner(myObj.openStream());
            ;//to read through file
            int r = 0;
            while (r < N_ROW) {//read through file
                String wholeRow = myReader.nextLine();//read and save next line in file
                String[] rowData = wholeRow.split(",", N_COL);//split up row into elements
                int c = 0;
                for (String obj : rowData)//turn comma seperated row into column entries
                {
                    levelData[r][c] = Short.parseShort(obj);//assign to proper location/element, in level
                    c++;
                }
                r++;
            }
            myReader.close();
        } catch (IOException e) {
            System.out.println("An error occurred loading map from file.");
        }
    }

    /**
     * Translates the level map short array into Environement objects at proper locations
     */
    private void initLevel() {
        for (int r = 0; r < N_ROW; ++r) {
            for (int c = 0; c < N_COL; ++c) {
                switch (levelData[r][c]) {//decode the short value into coresponding GameObject
                    case EMPTY: screenData[r][c] = new Cell(); break;
                    case WALL: screenData[r][c] = new Wall(); break;
                    case EGG: screenData[r][c] = new Egg(); break;
                    case DOOR: screenData[r][c] = new Door(); break;
                    case TRAP: screenData[r][c] = new TrapPunishment(); break;
                    case THORNBUSH: screenData[r][c] = new ThornBushPunishment(); break;
                }
            }
        }
    }
}
