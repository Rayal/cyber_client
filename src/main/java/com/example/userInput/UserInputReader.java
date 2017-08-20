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
    private Game game;

    public UserInputReader(String arg)
    {
        logger.info("Creating new UserInputReader.");
        scanner = new Scanner(System.in);
        game = new Game(arg);
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
            try {
                BigDecimal bet = new BigDecimal(inputArray[1]);
                game.newGame(bet);
            }
            catch (Exception e)
            {
                game.newGame();
                logger.warn(e.toString());
            }
        }
        else if (cmd.equalsIgnoreCase("hit"))
        {
            logger.info("Hit requested.");
            game.gameAction("hit");
        }
        /*else if (cmd.equalsIgnoreCase("stand"))
        {
            logger.info("Stand requested.");
            game.gameAction("stand");
        }*/
        else if (cmd.equalsIgnoreCase("stand") || cmd.equalsIgnoreCase("end"))
        {
            logger.info("End of game requested.");
            game.gameAction("end");
            if (game.evaluate())
            {
                System.out.println("Player won!");
            }
            else
            {
                System.out.println("Player lost!");
            }
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

        else if (cmd.equalsIgnoreCase("withdraw"))
        {
            logger.info("Increase in funds requested.");
            try {
                BigDecimal funds = new BigDecimal(inputArray [1]);

                if (game.withdrawFunds(funds))
                {
                    System.out.println("Success.");
                }
                else
                {
                    System.out.println("Unable to withdraw funds.");
                }
            }
            catch (Exception e)
            {
                logger.error(e.toString());
                System.out.println("Please enter an actual number value. Example: withdraw 5000");
            }
        }
        /*else if (cmd.equalsIgnoreCase("quit"))
        {
            logger.info("Ending program.");
        }*/
        else
        {
            System.out.println("Input faulty. Possible commands: game, hit, stand, fund, end");

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
