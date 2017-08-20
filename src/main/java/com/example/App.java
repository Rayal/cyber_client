package com.example;

import com.example.game.Game;
import com.example.userInput.UserInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.System.exit;

/**
 * Hello world!
 *
 */

public class App 
{
    static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) {
        logger.info("Hello World!");

        UserInputReader reader = null;
        try {
            reader = new UserInputReader(args[0]);
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            System.out.println("Please give the game server url as a parameter");
            exit(-1);
        }

        reader.run();
    }
}
