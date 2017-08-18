package com.example.userInput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class UserInputReader {
    static Logger logger = LoggerFactory.getLogger(UserInputReader.class);

    Scanner scanner;
    public UserInputReader()
    {
        logger.info("Creating new UserInputReader.");
        scanner = new Scanner(System.in);
    }

    private void parseInput(String input)
    {
        // Simple command mapping.
        String[] inputArray = input.split(" ");
        String cmd = inputArray[0];

        if(cmd.equalsIgnoreCase("game"))
        {
            logger.info("New game requested.");
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
