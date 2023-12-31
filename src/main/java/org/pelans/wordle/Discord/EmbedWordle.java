package org.pelans.wordle.Discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.pelans.wordle.Database.Entities.UserWord;
import org.pelans.wordle.util.Emojis;
import org.pelans.wordle.util.Language;

import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class EmbedWordle {

    private static EmbedBuilder base(UserWord userWord, Language lan, boolean hideWords, boolean showAviableLetters) {
        EmbedBuilder eb = new EmbedBuilder();
        String type = userWord.isFirstGame() ? lan.get("Daily WORDLE") : lan.get("Practice WORDLE");
        eb.setTitle( String.format(":cherry_blossom: %s :cherry_blossom: (%s %s) :palm_tree:",
                type, userWord.getCorrectWord().length(), lan.get("letters")));
        StringBuilder sb = new StringBuilder();
        for (String word : userWord.getWords()) {
            if(word == null) {
                sb.append(":black_large_square:".repeat(userWord.getCorrectWord().length()));
            } else {
                for(String text : getColors(word, userWord.getFormattedCorrectWord(), hideWords)) {
                    sb.append(text);
                }
            }
            sb.append("\n");
        }
        if(showAviableLetters) { //Must be improved
            String wordleEmojis = sb.toString();
            sb.append(String.format("__**%s:**__\n",lan.get("Letters")));
            sb.append(getEmojis("qwertyuiop", wordleEmojis) + "\n");
            if(lan.getLan().equals("es-ES"))
                sb.append(getEmojis("asdfghjklñ", wordleEmojis) + "\n");
            else
                sb.append(getEmojis("asdfghjkl", wordleEmojis) + ":black_large_square:\n");

            sb.append(":black_large_square:" + getEmojis("zxcvbnm", wordleEmojis) + ":black_large_square::black_large_square:\n");
        }
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        sb.append(String.format("\n%s: <t:%s:R>\n",lan.get("Next daily WORDLE"), c.getTimeInMillis()/1000));
        eb.setDescription(sb);
        return eb;
    }

    public static MessageEmbed wordle(UserWord userWord, Language lan, boolean hideWords){
        boolean showAviableLetters = true;
        EmbedBuilder eb = base(userWord, lan, hideWords, showAviableLetters);

        if(!userWord.hashWon() && userWord.isComplete() && !hideWords) {
            StringBuilder sb = eb.getDescriptionBuilder();
            sb.append(String.format("\n%s **__%s__**\n",lan.get("The word is"), userWord.getCorrectWord()));
        }

        return eb.build();
    }

    public static MessageEmbed shareWordle(UserWord userWord, Language lan, boolean hideWords){
        boolean showAviableLetters = false;
        EmbedBuilder eb = base(userWord, lan, hideWords, showAviableLetters);
        StringBuilder sb = eb.getDescriptionBuilder();
        sb.append("\n ");
        if(userWord.hashWon()) {
            sb.append(String.format("%s: <@%s> :trophy:", lan.get("Won by"), userWord.getMemberId().getUserId()));
        } else {
            if(!hideWords)
                sb.append(String.format("%s: **__%s__** \n", lan.get("The word is"), userWord.getCorrectWord()));
            sb.append(String.format("%s: <@%s> :skull_crossbones:\n", lan.get("Lost by"), userWord.getMemberId().getUserId()));
        }
        if(userWord.hashWon()) {
            eb.setColor(Color.GREEN);
        } else {
            eb.setColor(Color.RED);
        }
        return eb.build();
    }

    public static MessageEmbed wordle(UserWord userWord, Language lan, boolean hideWords, String additionalMessage) {
        boolean showAviableLetters = true;
        EmbedBuilder eb = base(userWord, lan, hideWords, showAviableLetters);
        StringBuilder sb = eb.getDescriptionBuilder();
        sb.append("\n").append(additionalMessage);

        if(!userWord.hashWon() && userWord.isComplete() && !hideWords) {
            sb.append(String.format("\n%s **__%s__**\n",lan.get("The word is"), userWord.getCorrectWord()));
        }

        return eb.build();
    }

    private static List<String> getColors(String word1, String word2, boolean hideWords) {
        List<String> result = new ArrayList<String>();
        List<Character> remaining1 = new ArrayList<Character>();
        List<Character> remaining2 = new ArrayList<Character>();
        for (int i=0; i<word1.length(); i++) {
            if(word1.charAt(i) == word2.charAt(i)) {
                if(!hideWords)
                    result.add(Emojis.getGreen(word1.charAt(i)));
                else
                    result.add(":green_square:");
                remaining1.add(null);
                remaining2.add(null);
            } else {
                if(!hideWords)
                    result.add(Emojis.getBlack(word1.charAt(i)));
                else
                    result.add(":red_square:");
                remaining1.add(word1.charAt(i));
                remaining2.add(word2.charAt(i));
            }
        }
        for(int i=0; i<remaining1.size(); i++) {
            if(remaining1.get(i) == null)
                continue;
            if(remaining2.contains(remaining1.get(i))) {
                int index = remaining2.indexOf(remaining1.get(i));
                remaining2.set(index, null);
                if(!hideWords)
                    result.set(i,Emojis.getYellow(remaining1.get(i)));
                else
                    result.set(i,":yellow_square:");
            }
        }
        return result;
    }

    private static String getEmojis(String textToConvert, String textToCompare) {
        //This method will be improved
        StringBuilder result = new StringBuilder();
        String textToCompareLowered = textToCompare.toLowerCase();
        for (int i=0; i<textToConvert.length(); i++) {
            String letter = String.valueOf(textToConvert.toLowerCase().charAt(i));
            if (letter.equals("ñ")) {
                letter = "nn";
            }
            if (textToCompareLowered.contains("green_" + letter + ":")) {
                result.append(Emojis.getGreen(textToConvert.toLowerCase().charAt(i)));
            } else if (textToCompareLowered.contains("yellow_" + letter + ":")) {
                result.append(Emojis.getYellow(textToConvert.toLowerCase().charAt(i)));
            } else if (textToCompareLowered.contains("black_" + letter + ":")) {
                result.append(Emojis.getBlack(textToConvert.toLowerCase().charAt(i)));
            } else {
                result.append(Emojis.getGrey(textToConvert.toLowerCase().charAt(i)));
            }
        }
        return result.toString();
    }

}
