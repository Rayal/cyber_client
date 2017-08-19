package com.example;

import com.example.game.Game;
import com.example.userInput.UserInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 */

public class App 
{
    static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) {
        logger.info("Hello World!");

        UserInputReader reader = new UserInputReader();

        reader.run();
    }
}
