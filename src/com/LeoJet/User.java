package com.LeoJet;

import java.util.Scanner;

public class User
{
    // class variables
    private String userCommand;
    private int[] userSizeChoice;
    private boolean quit;
    private boolean gameOver;
    private boolean userWon;

    public User()
    {
        this.userSizeChoice = new int[2];
        this.quit = false;
        this.gameOver = false;
    }

    private boolean isValidInput(String input, String Check)
    {
        boolean allValid = true;

        switch (Check)
        {
            case "int":
                // check if input string contains only numbers.

                for (int i = 0; i < input.length(); i++)
                {
                    if(!(Character.isDigit(input.charAt(i))))
                    {
                        allValid = false;
                        break;
                    }
                }

                return allValid;

            case "String":

                // check if input string contains only letters.
                for (int i = 0; i < input.length(); i++)
                {
                    if(!(Character.isLetter(input.charAt(i))))
                    {
                        allValid = false;
                        break;
                    }
                }
                return allValid;
        }
        return false;
    }

    private void getUserInput() // Called when the game loop starts or restarts
    {
        // input variables
        int userChoice;
        int[] sizeChoice;
        String userResponse;

        Scanner scanner = new Scanner(System.in);

        // introduce game to user
        System.out.println();
        System.out.println("=======================================\n====\tWelcome to Minesweeper!    ====\n=======================================\n> Press \"q\" or \"quit\" anytime to quit");

        // explain game mechanics


        // let user choose game difficulty (easy, medium, hard, or custom). for GUI purposes show amount of cells to flag and show timer (advanced save best time in category: easy, medium, hard, or custom)
        System.out.print("Choose board size (enter option number): \n1) EASY (8 by 10)\n2) MEDIUM (14 by 18)\n3) HARD (20 by 24)\n4) CUSTOM\noption: ");
        userChoice = Integer.parseInt(scanner.nextLine());  // also validate that it's a number
        System.out.println();

        switch (userChoice)
        {
            case 1:
                this.userSizeChoice = Board.EASY;
                System.out.println("board size set to: " + userSizeChoice[0] + " by " + userSizeChoice[1]);
                break;
            case 2:
                this.userSizeChoice = Board.MEDIUM;
                System.out.println("board size set to: " + userSizeChoice[0] + " by " + userSizeChoice[1]);
                break;
            case 3:
                this.userSizeChoice = Board.HARD;
                System.out.println("board size set to: " + userSizeChoice[0] + " by " + userSizeChoice[1]);
                break;
            case 4:
                boolean validInput = false;
                String[] customInput = new String[2];

                System.out.println("You chose option 4. Enter a custom size for the board.");
                while (!validInput)
                {
                    System.out.print("Enter width: ");
                    userResponse = scanner.next();
                    customInput[1] = userResponse;

                    System.out.print("Enter Height: ");
                    userResponse = scanner.next();
                    customInput[0] = userResponse;

                    // check validity
                    if(isValidInput(customInput[0], "int") && (isValidInput(customInput[1], "int")))
                    {
                        // convert to int
                        sizeChoice = new int[]{(Integer.parseInt(customInput[0])), (Integer.parseInt(customInput[1]))};

                        // further check validity
                        if(sizeChoice[0] > 3 && sizeChoice[1] > 3)
                        {
                            // set user choice
                            this.userSizeChoice = new int[]{sizeChoice[0], sizeChoice[1]};
                            System.out.println("board size set to: " + userSizeChoice[0] + " by " + userSizeChoice[1]);
                            validInput = true;
                        }else
                        {
                            System.out.println("ERROR: width and height must be greater than 3!");
                        }
                    }else
                    {
                        System.out.println("ERROR: width and height must contain only numbers");
                    }
                }
                break;
            default:
                System.out.println("ERROR: You did not enter any of the options!");
                break;
        }

        // when user types start then start the game loop
    }

    private int[] getUserPos(boolean initial, Board theBoard)
    {
        String userCommand;
        String[] userInput = new String[2];
        int[] userPos = new int[2];
        boolean valid = false;

        // create scanner object
        Scanner scanner = new Scanner(System.in);

        if(initial){
            while (!valid)
            {
                // ask user to enter position on board
                System.out.print("Enter position to start game.\nx: ");
                userInput[0] = scanner.next();
                System.out.print("y: ");
                userInput[1] = scanner.next();

                // check validity
                if(isValidInput(userInput[0], "int") && isValidInput(userInput[1], "int"))
                {
                    // covert
                    userPos[0] = Integer.parseInt(userInput[0]);
                    userPos[1] = Integer.parseInt(userInput[1]);

                    // check if valid number range
                    if((userPos[0] >= 0 && userPos[0] < theBoard.getBoardSize()[0]) && (userPos[1] >= 0 && userPos[1] < theBoard.getBoardSize()[1]))
                        valid = true;
                    else
                        System.out.println("Error: x and y must be within board size range!");
                }else
                    System.out.println("Error: x and y must be a number!");
            }

        }else{
            while (!valid)
            {
                // ask user to enter position on board to select and perform an action
                System.out.print("Enter command(\"d\" for dig, \"f\" for flag) followed by x pos and y. e.g. command: d, x: 3, y: 8\ncommand: ");
                userCommand = scanner.next();
                System.out.print("x: ");
                userInput[0] = scanner.next();
                System.out.print("y: ");
                userInput[1] = scanner.next();

                // check validity
                if((isValidInput(userInput[0], "int") && isValidInput(userInput[1], "int")) && (isValidInput(userCommand, "String")))
                {
                    // convert
                    userPos[0] = Integer.parseInt(userInput[0]);
                    userPos[1] = Integer.parseInt(userInput[1]);

                    // check if valid number range
                    if((userPos[0] >= 0 && userPos[0] < theBoard.getBoardSize()[0]) && (userPos[1] >= 0 && userPos[1] < theBoard.getBoardSize()[1]))
                    {
                        valid = true;

                        this.userCommand = userCommand;

                    } else
                        System.out.println("Error: x and y must be within board size range!");
                }else
                    System.out.println("Error: command must be a string, and positions must only contain numbers");
            }
        }
        return userPos;
    }

    public void gameLoop()
    {

        while (!quit)
        {
            // do once per game loop
            
            // get input
            getUserInput();

            // create board
            Board board = new Board(userSizeChoice[0], userSizeChoice[1]);

            // show board (first time all cells are hidden until user clicked)
            // debug we are skipping TODO: Add in.
            Board.printBoard(board.getBoard());

            // user selects a position of board. call setUserPosInit() with user selected position
            ///int[] userPosInit = new int[]{2, 10};
            //board.setUserPosInit(userPosInit);
            board.setUserPosInit(getUserPos(true, board));

            // board: generate mines, cover board, uncover from last selected cell
            board.GenerateMines();
            
            // uncover
            board.uncoverFromX(board.userPosInit[0], board.userPosInit[1]);
            board.resetUncover(); // reset for next uncover operation

            // show board
            Board.printBoard(board.getBoard());


            // game loop
            int[] userPosChoice;
            while(!this.gameOver)
            {
                // get input
                userPosChoice = getUserPos(false, board);

                if(this.userCommand == "f")
                {
                    // TODO: implement un flag
                    // flag cell of grid
                    Board.Grid[userPosChoice[0]][userPosChoice[1]].flagged = true;
                }
                else if(this.userCommand == "u")// un flag
                {
                    Board.Grid[userPosChoice[0]][userPosChoice[1]].flagged = false;
                }
                else if(this.userCommand == "d")
                {
                    if(Board.Grid[userPosChoice[0]][userPosChoice[1]].state == "M")
                    {
                        this.gameOver = true;
                        this.userWon = false;
                        break;
                    }else   // the grid positions state holds a number or empty spot
                        Board.Grid[userPosChoice[0]][userPosChoice[1]].isCovered = false;
                }

                // uncover from
                if(Board.Grid[userPosChoice[0]][userPosChoice[1]].state == "X")
                {
                    board.uncoverFromX(userPosChoice[0], userPosChoice[1]);
                    board.resetUncover();
                }

                // print board
                Board.printBoard(board.getBoard());
            }

            break;
        }


    }
}
