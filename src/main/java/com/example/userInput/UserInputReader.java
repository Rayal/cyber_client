package com.example.userInput;

import com.example.game.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class UserInputReader {
    private static Logger logger = LoggerFactory.getLogger(UserInputReader.class);

    private Scanner scanner;
    //private RequestSender sender;
    private Game game;

    public UserInputReader()
    {
        logger.info("Creating new UserInputReader.");
        scanner = new Scanner(System.in);
        //sender = new RequestSender("http://localhost:8080");
        game = new Game(this);
    }

    public String getInput() {
        return scanner.nextLine();
    }

    private void parseInput(String input)
    {
        // Simple command mapping.
        String[] inputArray = input.split(" ");
        String cmd = inputArray[0];

        if(cmd.equalsIgnoreCase("game"))
        {
            logger.info("New game requested.");

            game.newGame();
        }
        else if (cmd.equalsIgnoreCase("hit"))
        {
            logger.info("Hit requested.");
        }
        else if (cmd.equalsIgnoreCase("stand"))
        {
            logger.info("Stand requested.");
        }
        else if (cmd.equalsIgnoreCase("end"))
        {
            logger.info("End of game requested.");
        }

        else if (cmd.equalsIgnoreCase("fund"))
        {
            logger.info("Increase in funds requested.");
            try {
                BigDecimal funds = new BigDecimal(inputArray [1]);

                if (game.addFunds(funds))
                {
                    System.out.println("Success.");
                }
                else
                {
                    System.out.println("Unable to add funds.");
                }
            }
            catch (Exception e)
            {
                logger.error(e.toString());
                System.out.println("Please enter an actual number value. Example: fund 5000");
            }
        }
        else if (cmd.equalsIgnoreCase("quit"))
        {
            logger.info("Ending program.");
        }
    }

    public void run()
    {
        logger.info("UserInputReader running");
        while (true)
        {
            try {
                TimeUnit.MILLISECONDS.sleep(10);// For aesthetic purposes.
            } catch (InterruptedException e) {
                logger.error(e.toString());
            }
            System.out.print("$ ");
            parseInput(scanner.nextLine());
        }
    }
}
