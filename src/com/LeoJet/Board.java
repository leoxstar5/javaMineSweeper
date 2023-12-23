package com.LeoJet;

import java.util.Random;

class Cell
{
    String state = "";
    boolean isCovered = true; // debug false.   should be set to true
    boolean uncoverChecked = false;
    boolean flagged = false;
}

public class Board {
    public static Cell[][] Grid;

    private final int gridX, gridY;

    private int mines = 0;

    private int minesTotal = 0;

    public int flags;

    public int[] userPosInit = new int[2];

    private boolean userInitSet = false;

    // default
    public static final int[] EASY    = {8,  10};
    public static final int[] MEDIUM  = {14, 18};
    public static final int[] HARD    = {20, 24};

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
        this.gridX = Grid.length;
        this.gridY = Grid[0].length;
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
        this.userInitSet = true;

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

        int boardCells = Grid.length * Grid[0].length;  // total number of cells in Grid

        // min and max for random amount of mines. depends on cell count of Grid
        int min = boardCells/5;
        int max = boardCells/4;

        // create instance of Random class
        Random rand = new Random();

        // set mines amount. Generate random integers in range min to max.
        int randMinesCount =  min + (rand.nextInt((max+1)-min));    // rand.nextInt(n) returns random number from 0 to n

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
                    this.minesTotal++;

        this.flags = this.minesTotal;   // set flags (flags left to put on board) to total mines
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
        for(Cell[] cells: Grid)
        {
            for(Cell cell: cells)
            {
                cell.uncoverChecked = false;
            }
        }
    }

    private void checkCell(int x, int y)
    {
        // check if not out of bounds
        if((x >= 0 && x < gridX) && (y >= 0 && y < gridY))
        {
            // uncover
            if(Character.isDigit(Grid[x][y].state.charAt(0))) // uncover cell that has a number
                Grid[x][y].isCovered = false;
            else if(Grid[x][y].state.equals("X") && !Grid[x][y].uncoverChecked)   // cell that has x was not yet checked for uncovering
                uncoverFromX(x, y); // recursion (indirect recursion; called from another function)
        }
    }

    public void uncoverFromX(int x, int y)   // check surrounding x if any numbers then uncover if x surrounding then use recursion
    {
        // check if not out of bounds
        if((x >= 0 && x < gridX) && (y >= 0 && y < gridY))
        {
            // uncover the cell that contains x (pos from parameters)
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

    // check if the user won
    public boolean checkIfUserWon()
    {
        int cellsCorrect = 0;

        for(Cell[] cells: Grid)
        {
            for(Cell cell: cells)
            {
                if(cell.flagged && cell.state.equals("M")){
                    cellsCorrect++;
                }
                if(!cell.state.equals("M") && !cell.isCovered)
                    cellsCorrect++;
            }
        }

        // return if user won
        return cellsCorrect == gridX * gridY;
    }

    public void printBoard()
    {
        int xCount = this.gridX;

        int yCount = 1;

        // print flags left
        if(this.userInitSet)
            System.out.println("\nflags left: " + this.flags);

        // border
        for(int i = 0; i < Grid[0].length * 3; i++) System.out.print("=");

        // print cells
        System.out.println();
        for (Cell[] cells : Grid) {
            for (Cell cell : cells) {
                System.out.print((!cell.isCovered && !cell.state.equals("no mine") ? cell.state : (cell.flagged ? "f": "#")) + "  ");
            }
            System.out.println("|" + xCount--);
        }

        // Horizontal indicator
        // border
        for(int i = 0; i < Grid[0].length * 3; i++) System.out.print("=");
        System.out.println();

        // indicator
        for (int i = 0; i < Grid[0].length; i++)
        {
            System.out.print((yCount++) + "  ");
            if(yCount > 9) yCount = 0;    // reset counter. otherwise number misplaces next numbers on horizontal indicator compared to board.
        }
        System.out.println();
    }

    public void printBoardKey()
    {
        System.out.println("\nBOARD KEY!!!");
        for(Cell[] cells: Grid)
        {
            for(Cell cell: cells)
            {
                System.out.print(cell.state + "  ");
            }
            System.out.println();
        }
        System.out.println("BOARD KEY!!!");
    }

    // Getter methods
    public int[] getBoardSize() { return new int[]{gridX, gridY}; }
}
