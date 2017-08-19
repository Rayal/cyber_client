package com.example.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class RandomIdentifier {
    static Logger logger = LoggerFactory.getLogger(RandomIdentifier.class);

    private static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String lower = upper.toLowerCase();
    private static final String digits = "0123456789";

    private Random random;
    private int length;

    public RandomIdentifier(int length)
    {
        random = new Random();
        this.length = length;
    }

    public String getIdentifier() {
        String possibilities = (upper + logger + digits);
        StringBuilder retval = new StringBuilder();

        for (int i = 0; i < length; i++)
        {
            retval.append(possibilities.charAt(random.nextInt(possibilities.length())));
        }

        return retval.toString();
    }
}
