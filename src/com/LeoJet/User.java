package com.LeoJet;

import java.util.Scanner;

public class User
{
    // class variables
    private String userCommand;
    private int[] userSizeChoice;
    private boolean gameOver;

    public User()
    {
        this.userSizeChoice = new int[2];
        this.gameOver = false;
    }

    private boolean isValidInput(String input, String Check)
    {
        boolean allValid = true;

        switch (Check)
        {
            case "int" -> {
                // check if input string contains only numbers.

                for (int i = 0; i < input.length(); i++)
                {
                    if (!(Character.isDigit(input.charAt(i))))
                    {
                        allValid = false;
                        break;
                    }
                }
                return allValid;
            }
            case "String" -> {

                // check if input string contains only letters.
                for (int i = 0; i < input.length(); i++)
                {
                    if (!(Character.isLetter(input.charAt(i))))
                    {
                        allValid = false;
                        break;
                    }
                }
                return allValid;
            }
        }
        return false;
    }

    private void getUserInput() // Called when the game loop starts or restarts
    {
        // input variables
        int userChoice;
        int[] sizeChoice;
        String userResponse;

        // Create Scanner obj for input
        Scanner scanner = new Scanner(System.in);

        // introduce game to user
        System.out.println();
        System.out.println("=======================================\n====\tWelcome to Minesweeper!    ====\n=======================================\n");


        // let user choose game difficulty (easy, medium, hard, or custom). for GUI purposes show amount of cells to flag and show timer (advanced save best time in category: easy, medium, hard, or custom)
        System.out.print("Choose board size (enter option number): \n1) EASY (8 by 10)\n2) MEDIUM (14 by 18)\n3) HARD (20 by 24)\n4) CUSTOM\noption: ");
        userChoice = Integer.parseInt(scanner.nextLine());  // also validate that it's a number
        System.out.println();

        switch (userChoice)
        {
            case 1 -> {
                this.userSizeChoice = Board.EASY;
                System.out.println("board size set to: " + userSizeChoice[0] + " by " + userSizeChoice[1]);
            }
            case 2 -> {
                this.userSizeChoice = Board.MEDIUM;
                System.out.println("board size set to: " + userSizeChoice[0] + " by " + userSizeChoice[1]);
            }
            case 3 -> {
                this.userSizeChoice = Board.HARD;
                System.out.println("board size set to: " + userSizeChoice[0] + " by " + userSizeChoice[1]);
            }
            case 4 -> {
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
                    if (isValidInput(customInput[0], "int") && (isValidInput(customInput[1], "int")))
                    {
                        // convert to int
                        sizeChoice = new int[]{(Integer.parseInt(customInput[0])), (Integer.parseInt(customInput[1]))};

                        // further check validity
                        if (sizeChoice[0] > 3 && sizeChoice[1] > 3)
                        {
                            // set user choice
                            this.userSizeChoice = new int[]{sizeChoice[0], sizeChoice[1]};
                            System.out.println("board size set to: " + userSizeChoice[0] + " by " + userSizeChoice[1]);
                            validInput = true;
                        } else
                        {
                            System.out.println("ERROR: width and height must be greater than 3! This is in order to be able to generate mines.");// since mines are not generated around userInitPos
                        }
                    } else
                    {
                        System.out.println("ERROR: width and height must contain only numbers");
                    }
                }
            }
            default -> System.out.println("ERROR: You did not enter any of the options!");
        }
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
                    userPos[0] = Integer.parseInt(userInput[0]) -1; // -1 since index is 0 based
                    userPos[1] = Integer.parseInt(userInput[1]) -1;

                    // check if valid number range
                    if((userPos[0] >= 0 && userPos[0] < theBoard.getBoardSize()[0]) && (userPos[1] >= 0 && userPos[1] < theBoard.getBoardSize()[1]))
                        valid = true;
                    else
                        System.out.println("Error: x and y must be within board size range!!!");
                }else
                    System.out.println("Error: x and y must be a number!");
            }

        }else{
            while (!valid)
            {
                // ask user to enter position on board to select and perform an action
                System.out.println("Enter command(\"d\" for dig, \"f\" for flag) followed by position (x and y).");
                System.out.print("command: ");
                userCommand = scanner.nextLine();
                System.out.print("x: ");
                userInput[0] = scanner.next();
                System.out.print("y: ");
                userInput[1] = scanner.next();

                // check validity
                if((isValidInput(userInput[0], "int") && isValidInput(userInput[1], "int")) && (isValidInput(userCommand, "String")))
                {
                    // convert
                    userPos[0] = Integer.parseInt(userInput[0]) - 1;
                    userPos[1] = Integer.parseInt(userInput[1]) - 1;

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

        // reverse x and y for using in program (x and y are the opposite for a regular user).
        // reverse arr. since array for computers y(x in computers) starts from the top->bottom. in math it's bottom->top
        int revArrLen = theBoard.getBoardSize()[0];
        int[] revGridX = new int[revArrLen];

        int counter = 0;
        for(int i = revArrLen-1; i >= 0; i--)
        {
            revGridX[counter] = i;
            counter++;
        }

        userPos[1] = revGridX[userPos[1]];

        userPos = new int[]{userPos[1], userPos[0]};
        return userPos;
    }

    private boolean promptReplay()
    {
        // create scanner object
        Scanner scanner = new Scanner(System.in);

        System.out.print("Would you like to play again? (y/n)\n: ");
        String userResponse = scanner.next();
        System.out.println();

        if(userResponse.equals("y") || userResponse.equals("yes"))
        {
            // reset game over
            this.gameOver = false;
            return true;
        }
        else if(userResponse.equals("n") || userResponse.equals("no"))
        {
            System.out.println("Thank you for playing.");
            return false;
        }
        else
        {
            System.out.println("You did not enter: \"y\", \"yes\", \"n\", or \"no\"");
            return promptReplay(); // re prompt until user types correct command. recursion
        }
    }

    public void gameLoop()
    {
        do
        {
            // do once per game loop

            // get input
            getUserInput();

            // create board
            Board board = new Board(userSizeChoice[0], userSizeChoice[1]);

            // show board (first time all cells are hidden until user clicked)
            board.printBoard();

            // user selects a position of board. call setUserPosInit() with user selected position
            board.setUserPosInit(getUserPos(true, board));

            // board: generate mines
            board.GenerateMines();

            // uncover
            board.uncoverFromX(board.userPosInit[0], board.userPosInit[1]);
            board.resetUncover(); // reset for next uncover operation

            // show board
            board.printBoard();

            // to track user choice in each loop
            int[] userPosChoice;

            // game loop
            while (!this.gameOver)
            {
                // get input
                userPosChoice = getUserPos(false, board);

                switch (this.userCommand)
                {
                    case "f" ->
                    {
                        // flag cell of grid
                        if (Board.Grid[userPosChoice[0]][userPosChoice[1]].isCovered && !Board.Grid[userPosChoice[0]][userPosChoice[1]].flagged)    // cell is covered and not flagged
                        {
                            Board.Grid[userPosChoice[0]][userPosChoice[1]].flagged = true;
                            board.flags--;
                        } else
                        {
                            System.out.println("Error: you can't flag a spot that is uncovered or already flagged!");
                        }
                    }
                    case "u" ->
                    {
                        // un flag

                        // check that the position contains a flag.
                        if (Board.Grid[userPosChoice[0]][userPosChoice[1]].flagged)
                        {
                            Board.Grid[userPosChoice[0]][userPosChoice[1]].flagged = false;
                            board.flags++;
                        } else
                        {
                            System.out.println("Error: You can't un flag a spot that has no flag"); // note: for gui there is no option to un flag in this case.
                        }
                    }
                    case "d" ->
                    {
                        // dig cell (uncover cell)

                        if (Board.Grid[userPosChoice[0]][userPosChoice[1]].state.equals("M"))
                        {
                            System.out.println("You lost, you dug a mine!!!");
                            board.printBoardKey();
                            this.gameOver = true;
                        } else if (Board.Grid[userPosChoice[0]][userPosChoice[1]].state.equals("X"))   // the grid positions state holds an empty spot
                        {
                            // uncover from
                            board.uncoverFromX(userPosChoice[0], userPosChoice[1]);
                            board.resetUncover();
                        } else    // position on board is a number
                        {
                            Board.Grid[userPosChoice[0]][userPosChoice[1]].isCovered = false;
                        }
                    }
                    default ->
                            System.out.println("Nothing happens since you entered something other than: \"f\", \"u\", or \"d\" as the command!");
                }

                // print board
                if (!this.gameOver)  // We don't want to print the board for next user input if game over
                    board.printBoard();

                if (board.checkIfUserWon())
                {
                    System.out.println("You won!");
                    this.gameOver = true;
                    break;
                }
            }
        } while (promptReplay()); // Ask user if they want to play again.
    }
}
