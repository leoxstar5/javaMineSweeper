
package com.LeoJet;

import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

/*
        // Get user grid size
        // Vars
        int[] size = new int[2];

        Scanner scanner = new Scanner(System.in);

        // get X

        System.out.print("Enter x: ");

        size[0] = Integer.parseInt(scanner.nextLine());

        System.out.println();

        // get Y

        System.out.print("Enter y: ");

        size[1] = Integer.parseInt(scanner.nextLine());

        System.out.println();



        // Create the Board
        Board board = new Board(size[0], size[1]);

        // First get user click position then generate mines (make sure no mines generate around click position.)

        // Generate the mines. (and fill in numbers)
        board.GenerateMines();

        board.printBoard(board.getBoard());
*/
//        Board board = new Board(10, 10);
//
//        //Board.printBoard(board.getBoard());
//
//        int[] userPos = {1, 1};
//
//        board.setUserPosInit(userPos);
//
//        board.GenerateMines();
//
//        // print board
//        Board.printBoard(board.getBoard());

        // create user obj
        User user = new User();
        user.gameLoop();

    }
}
