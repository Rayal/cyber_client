package com.example.game;

import com.example.connection.RequestSender;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Game {
    private static Logger logger = LoggerFactory.getLogger(Game.class);

    private String username;
    private RequestSender requestSender;

    private ArrayList<String> playerHand;
    private ArrayList<String> dealerHand;

    private ArrayList<Integer> playerCards;
    private ArrayList<Integer> dealerCards;

    public Game()
    {
        logger.info("Creating new Game Object");

        playerHand = new ArrayList<String>();
        dealerHand = new ArrayList<String>();
        playerCards = new ArrayList<Integer>();
        dealerCards = new ArrayList<Integer>();

        requestSender = new RequestSender("http://localhost:8080");

        RandomIdentifier randomIdentifier = new RandomIdentifier(10);
        JSONObject request = new JSONObject();

        logger.info("Attempting login.");
        String[] retval = null;
        while (true)
        {
            try {
                TimeUnit.MILLISECONDS.sleep(10);// For aesthetic purposes.
            } catch (InterruptedException e) {
                logger.error(e.toString());
            }

            username = randomIdentifier.getIdentifier();

            try {
                request.accumulate("username", username);
                retval = requestSender.sendRequest("/login", "POST", request);
            } catch (Exception e) {
                logger.error(e.toString());
            }

            if (retval != null)
            {
                if (Integer.parseInt(retval[0]) == HttpStatus.SC_OK)
                    break;
            }
            request.remove("username");
        }
        logger.info(String.format("Login successful. Username %s", username));
    }

    private void printHand()
    {
        System.out.print("Player hand: ");
        for (String card : playerHand)
        {
            System.out.print(card + "  ");
        }
        System.out.println();

        System.out.print("Dealer hand: ");
        for (String card : dealerHand)
        {
            System.out.print(card + "  ");
        }
        System.out.println();
    }

    private boolean parseResponse(String[] retVal)
    {
        if (Integer.parseInt(retVal[0]) != HttpStatus.SC_OK)
        {
            logger.warn("Server response not OK");
            String problem = retVal[1].split(":")[1].replace("}", "");
            System.out.println(String.format("Message from the server: %s", problem));
            return false;
        }

        logger.info(String.format("Parsing server response\n %s", retVal[1]));
        String[] playerHand = null;
        String[] dealerHand = null;
        try {
            JSONObject jsonObject = new JSONObject(retVal[1]);
            playerHand = ((String) jsonObject.get("playerHand"))
                    .replace("[", "")
                    .replace("]", "")
                    .split(", ");

            dealerHand = ((String) jsonObject.get("dealerHand").toString())
                    .replace("[", "")
                    .replace("]", "")
                    .replaceAll("\"", "")
                    .split(", ");

        } catch (JSONException e) {
            logger.error(e.toString());
        }


        if (playerHand != null)
        {
            this.playerHand.clear();
            this.playerCards.clear();
            for (String s : playerHand) {
                int card = Integer.valueOf(s);
                this.playerCards.add(card);
                this.playerHand.add(Card.value(card));
            }
        }
        this.dealerHand.clear();
        this.dealerCards.clear();
        for (String s : dealerHand)
        {
            int card = Integer.valueOf(s);
            dealerCards.add(card);
            this.dealerHand.add(Card.value(card));
        }

        printHand();
        return true;
    }

    public boolean newGame()
    {
        JSONObject request = new JSONObject();
        try {
            request.accumulate("username", username);

        } catch (Exception e) {
            logger.error(e.toString());
        }

        String[] retVal = requestSender.sendRequest("/game", "PUT", request);

        return parseResponse(retVal);
    }

    public boolean addFunds(BigDecimal funds)
    {
        JSONObject request = new JSONObject();

        try {
            request.accumulate("username", username);
            request.accumulate("funds", funds);
        } catch (Exception e) {
            logger.error(e.toString());
        }
        String[] retVal = requestSender.sendRequest("/funds", "POST", request);

        if (Integer.parseInt(retVal[0]) == HttpStatus.SC_OK)
        {
            return true;
        }
        String problem = retVal[1].split(":")[1].replace("}", "");
        System.out.println(String.format("Message from the server: %s", problem));
        return false;
    }

    public boolean gameAction(String gameAction)
    {
        JSONObject request = new JSONObject();
        try {
            request.accumulate("username", username);
            request.accumulate("gameAction", gameAction);
        } catch (Exception e) {
            logger.error(e.toString());
        }
        String[] retVal = requestSender.sendRequest("/game/play", "POST", request);

        return parseResponse(retVal);
    }

    public boolean evaluate()
    {
        return value(playerCards) > value(dealerCards);
    }

    private int value(ArrayList<Integer> cards)
    {
        int value[] = new int[2];
        for (int i : cards)
        {
            i = (i % 13) + 1;
            if (i == 1) {
                value[0] += i;
                value[1] += 10;
            }
            else if (i > 10)
            {
                value[0] += 10;
                value[1] += 10;
            }
            else {
                value[0] += i;
            }
        }
        int bestValue = 0;
        for (int val : value)
        {
            logger.info(String.valueOf(val));
            if (val <= 21 && val > bestValue)
                bestValue = val;
        }
        return bestValue;
    }
}
