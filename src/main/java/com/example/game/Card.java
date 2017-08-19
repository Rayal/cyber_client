package com.example.game;

public class Card {
    private static final int deckSize = 13;
    private static final String[] suits = {
            "CLUBS",
            "DIAMONDS",
            "HEARTS",
            "SPADES"
    };

    public static String value(int card)
    {
        int cardValue = (card % deckSize) + 1;
        String value = "";
        if (cardValue == 1)
        {
            value += "ACE";
        }
        else if (cardValue == 11)
        {
            value += "JACK";
        }
        else if (cardValue == 12)
        {
            value += "QUEEN";
        }
        else if (cardValue == 13)
        {
            value += "KING";
        }
        else
        {
            value += String.valueOf(cardValue);
        }
        for (int i = 1; i <= 4; i++)
        {
            if (card < deckSize * i)
            {
                value += " " + suits[i - 1];
                break;
            }
        }
        return value;
    }
}
