package com.LeoJet;

import java.util.Scanner;

public class User
{
    // class variables
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

    public void gameLoop()
    {

        while (!gameOver)
        {
            // get input
            getUserInput();

            // create board
            Board board = new Board(userSizeChoice[0], userSizeChoice[1]);

            // show board (first time all cells are hidden until user clicked)
            // debug we are skipping TODO: Add in.

            // user selects a position of board. call setUserPosInit() with user selected position
            int[] userPosInit = new int[]{2, 2};
            board.setUserPosInit(userPosInit);

            // board: generate mines, cover board, uncover from last selected cell
            board.GenerateMines();

            // show board
            Board.printBoard(board.getBoard());

            break;
        }


    }
}
