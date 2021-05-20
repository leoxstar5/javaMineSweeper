package com.LeoJet;

import java.util.Random;

class Cell
{
    String state = "";
    boolean isCovered = true; // debug false.   should be set to true
    boolean check = false; // this var indicates if a cell should be checked for uncovering
    boolean uncoverChecked = false;
    boolean flagged = false;
}

public class Board {

    // class variables

    public static Cell[][] Grid;   // NOTE: changed string[][] to Cell[][]

    //private static Boolean[][] Cover;   // During play time this 2d array depicts which spots should be covered

    private int gridX, gridY;

    private int mines = 0;

    private int minesTotal = 0;

    public int[] userPosInit = new int[2];

    // default

    public static final int[] EASY    = {8,  10};
    public static final int[] MEDIUM  = {14, 18};
    public static final int[] HARD    = {20, 24};

    // class functions

    // constructor
    public Board(int rows, int columns) {
        // Create the board and set to array of objects given width and height
        Grid = new Cell[rows][columns];

        // Assign each Grid index to a Cell object instance and set state to "X"
        for (int i = 0; i < Grid.length; i++)
        {
            for (int j = 0; j < Grid[0].length; j++)
            {
                Grid[i][j] = new Cell();
                Grid[i][j].state = "X";
            }
        }

        // assign class variables
        gridX = Grid.length;
        gridY = Grid[0].length;
    }


    private void setCellState(int x, int y)
    {
        // check if out of bounds
        if((x >= 0 && x < gridX) && (y >= 0 && y < gridY))
        {
            Grid[x][y].state = "no mine";
        }
    }

    public void setUserPosInit(int[] userPosInit)   // set where the user clicked the board initially. note: no mines can be generated around the user initial position.
    {
        this.userPosInit = userPosInit;

        // set that userPosInit spot and around cannot have a mine
        setCellState(userPosInit[0], userPosInit[1]);

        // set positions around
        setCellState(userPosInit[0]-1,userPosInit[1]-1);  // top left
        setCellState(userPosInit[0]-1,   userPosInit[1]);    // top
        setCellState(userPosInit[0]-1,userPosInit[1]+1);  // top right
        setCellState(   userPosInit[0],  userPosInit[1]+1);  // right
        setCellState(userPosInit[0]+1,userPosInit[1]+1);  // bottom right
        setCellState(userPosInit[0]+1,   userPosInit[1]);    // bottom
        setCellState(userPosInit[0]+1,userPosInit[1]-1);  // bottom left
        setCellState(   userPosInit[0],  userPosInit[1]-1);  // left

    }

    private void addRandomMines(){  // note: must call after calling setUserPosInit()  this function (addRandomMines) requires userPosInit to be set.
        // TODO: implement userPosInit when creating the mines.

        int boardCells = Grid.length * Grid[0].length;  // total number of cells in Grid

        // min and max for random amount of mines. depends on cell count of Grid
        int min = (int)(boardCells/5);
        int max = (int)(boardCells/4);

        // create instance of Random class
        Random rand = new Random();

        // set mines amount. Generate random integers in range min to max.
        int randMinesCount =  min + (rand.nextInt((max+1)-min));

        int[][] RandomSpots = new int[randMinesCount][2]; // create array of positions

        // fill random spots array
        for(int i = 0; i < randMinesCount; i++)
        {
            RandomSpots[i][0] = (rand.nextInt(gridX)); // contains random between {0->gridX, 0->gridY}
            RandomSpots[i][1] = (rand.nextInt(gridY));
        }

        // Loop over Grid and add the mines
        for(int i = 0; i < randMinesCount; i++)
        {
            // don't add a mine around user position
            if (!(Grid[RandomSpots[i][0]][RandomSpots[i][1]].state.equals("no mine")))
            {
                Grid[RandomSpots[i][0]][RandomSpots[i][1]].state = "M";
            }
        }

        // Reset any cell state that's set to: no mine. since we already used the hint "no mine" and won't be using it later.
        for(Cell[] cells: Grid)
            for(Cell cell: cells)
                if(cell.state.equals("no mine"))
                    cell.state = "X";

        // count mines
        for(Cell[] cells: Grid)
            for(Cell cell: cells)
                if(cell.state.equals("M"))
                    minesTotal++;

        // debug print mines count
        System.out.println("total mines: " + minesTotal);
    }

    private void checkIfMine(int x, int y)
    {
        // Make sure not out of bounds
        if((x >= 0 && x < gridX) && (y >= 0 && y < gridY))
        {
            if(Grid[x][y].state.equals("M")) mines++;
        }
    }


    private void addNumbers(){

        for(int i = 0; i < gridX; i++)
        {
            for(int j = 0; j < gridY; j++)
            {

                // Spot is not a mine (can't add numbers in a mine)
                if(!Grid[i][j].state.equals("M"))
                {
                    checkIfMine(i-1, j-1);   // top left
                    checkIfMine(i-1,    j);     // top
                    checkIfMine(i-1, j+1);   // top right
                    checkIfMine(   i,   j+1);   // right
                    checkIfMine(i+1, j+1);   // bottom right
                    checkIfMine(i+1,    j);     // bottom
                    checkIfMine(i+1, j-1);   // bottom left
                    checkIfMine(   i,   j-1);   // left

                    // Add the number to Grid index
                    if(mines > 0)
                        Grid[i][j].state = Integer.toString(mines);
                }

                // Reset for next iteration
                mines = 0;
            }
        }

    }

    public void GenerateMines(){

        addRandomMines();
        addNumbers();

    }

    // Uncover
    public void resetUncover()
    {
        for(int i = 0; i < Grid.length; i++)
        {
            for(int j = 0; j < Grid[0].length; j++)
            {
                if(Grid[i][j].uncoverChecked)
                    Grid[i][j].uncoverChecked = false;
            }
        }
    }

    private void checkCell(int x, int y)
    {
        if((x >= 0 && x < gridX) && (y >= 0 && y < gridY))
        {
            // uncover
            if(Character.isDigit(Grid[x][y].state.charAt(0)))
                Grid[x][y].isCovered = false;
            else if(Grid[x][y].state == "X")
                if(!Grid[x][y].uncoverChecked)  // cell that has x was not yet checked for uncovering
                    uncoverFromX(x, y); // recursion
        }
    }

    public void uncoverFromX(int x, int y)   // check surrounding x if any numbers then uncover if x surrounding then use recursion
    {
        // check if out of bounds
        if((x >= 0 && x < gridX) && (y >= 0 && y < gridY))
        {
            // uncover the cell that contains x (pos from in parameters)
            Grid[x][y].isCovered = false;
            Grid[x][y].uncoverChecked = true;

            // check surrounding
            checkCell(x-1, y-1);
            checkCell(x-1, y);
            checkCell(x-1, y+1);
            checkCell(x, y+1);
            checkCell(x+1, y+1);
            checkCell(x+1, y);
            checkCell(x+1, y-1);
            checkCell(x, y-1);
        }
    }

    public static void printBoard(Cell[][] Board)    // TODO: figure out a way to print any type 2d array
    {
        for (Cell[] cells : Board) {
            for (Cell cell : cells) {
                System.out.print((!cell.isCovered && !cell.state.equals("no mine") ? cell.state : (cell.flagged ? "f": "#")) + "  ");
            }
            System.out.println();
        }
    }

    // Getter methods
    public Cell[][] getBoard() { return Grid; }

    public int[] getBoardSize() { return new int[]{gridX, gridY}; }
}
