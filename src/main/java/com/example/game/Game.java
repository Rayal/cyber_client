package com.example.game;

import com.example.connection.RequestSender;
import com.example.userInput.UserInputReader;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Game {
    static Logger logger = LoggerFactory.getLogger(Game.class);

    String username;
    RequestSender requestSender;
    UserInputReader userInputReader;

    int gameId;

    ArrayList<String> playerHand;
    ArrayList<String> dealerHand;

    public Game(UserInputReader inputReader)
    {
        logger.info("Creating new Game Object");

        playerHand = new ArrayList<String>();
        dealerHand = new ArrayList<String>();

        userInputReader = inputReader;
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

    public boolean newGame()
    {
        JSONObject request = new JSONObject();
        try {
            request.accumulate("username", username);

        } catch (Exception e) {
            logger.error(e.toString());
        }

        String[] retVal = requestSender.sendRequest("/game", "PUT", request);

        if (Integer.parseInt(retVal[0]) != HttpStatus.SC_OK)
        {
            logger.warn("Server response not OK");
            String problem = retVal[1].split(":")[1].replace("}", "");
            System.out.println(String.format("Message from the server: %s", problem));
            return false;
        }

        logger.info("Parsing server response");
        try {
            JSONObject jsonObject = new JSONObject(retVal[1]);
            String[] playerHand = ((String) jsonObject.get("playerHand"))
                    .replace("[", "")
                    .replace("]", "")
                    .split(", ");
            int dealerHand = (Integer) jsonObject.get("dealerHand");


            for (String s : playerHand)
            {
                int card = Integer.valueOf(s);
                this.playerHand.add(Card.value(card));
            }
            this.dealerHand.add(Card.value(dealerHand));
        } catch (JSONException e) {
            logger.error(e.toString());
        }

        printHand();
        return true;
    }

    public boolean addFunds(BigDecimal funds)
    {
        JSONObject request = new JSONObject();
        String[] retVal = null;
        try {
            request.accumulate("username", username);
            request.accumulate("funds", funds);
        } catch (Exception e) {
            logger.error(e.toString());
        }
        retVal = requestSender.sendRequest("/funds", "POST", request);

        if (Integer.parseInt(retVal[0]) == HttpStatus.SC_OK)
        {
            return true;
        }
        String problem = retVal[1].split(":")[1].replace("}", "");
        System.out.println(String.format("Message from the server: %s", problem));
        return false;
    }
}
