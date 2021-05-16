package com.LeoJet;

import java.util.Arrays;

import java.util.Random;

class Cell
{
    String state = "";
    boolean isCovered = false; // debug false.   should be set to true
}

public class Board {

    // class variables

    private static Cell[][] Grid;   // NOTE: changed string[][] to Cell[][]

    //private static Boolean[][] Cover;   // During play time this 2d array depicts which spots should be covered

    private int gridX, gridY;

    private int mines = 0;

    private int minesTotal = 0;

    private int[] userPosInit = new int[2];

    // class functions

    public Board(int rows, int columns) {
        // Create the board given width and height
        // test create array of objects
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

    private boolean setCellState(int x, int y, String state)
    {
        // check if out of bounds
        if((x >= 0 && x < gridX) && (y >= 0 && y < gridY))
        {
            Grid[x][y].state = state;
            return true;
        }
        return false;
    }

    public void setUserPosInit(int[] userPosInit)   // set where the user clicked the board initially. note: no mines can be generated around the user initial position.
    {
        this.userPosInit = userPosInit;

        // set that userPosInit spot and around cannot have a mine
        setCellState(userPosInit[0], userPosInit[1], "no mine");

        // set positions around
        setCellState(userPosInit[0]-1,userPosInit[1]-1, "no mine");  // top left
        setCellState(userPosInit[0]-1,   userPosInit[1],   "no mine");  // top
        setCellState(userPosInit[0]-1,userPosInit[1]+1, "no mine");  // top right
        setCellState(   userPosInit[0],  userPosInit[1]+1, "no mine");  // right
        setCellState(userPosInit[0]+1,userPosInit[1]+1, "no mine");  // bottom right
        setCellState(userPosInit[0]+1,   userPosInit[1],   "no mine");  // bottom
        setCellState(userPosInit[0]+1,userPosInit[1]-1, "no mine");  // bottom left
        setCellState(   userPosInit[0],  userPosInit[1]-1, "no mine");  // left

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

        int[][] RandomSpots = new int[randMinesCount][2];

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

        // Reset any cell state that's set to: no mine. since we already used the hint "no mine" and wont be using it later.
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
        // debug print
        //System.out.println("Grid before number placed: ");
        //printBoard(Grid);

        //System.out.println("Grid after number placed: ");
        addNumbers();

        //printBoard(Grid);


    }

    public static void printBoard(Cell[][] Board)    // TODO: figure out a way to print any type 2d array
    {
        for (Cell[] cells : Board) {
            for (Cell cell : cells) {
                System.out.print((!cell.isCovered && !cell.state.equals("no mine") ? cell.state : " ") + "  ");
            }
            System.out.println();
        }
    }

    // Getter methods
    public Cell[][] getBoard() { return Grid; }
}
