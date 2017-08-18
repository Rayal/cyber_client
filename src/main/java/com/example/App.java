package com.example;

import com.example.userInput.UserInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

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
