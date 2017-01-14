package net.jacobmason.VelocityVortexScorekeeperServer.messages;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JacobAMason on 1/14/17.
 */
public class Match {
    private int matchNumber;
    private int red1;
    private int red2;
    private int blue1;
    private int blue2;

    public static List<Match> readMatchFile(String file) throws IOException {
        BufferedReader fIN = new BufferedReader(new FileReader(file));
        String line;
        List<Match> matches = new ArrayList<>();
        while((line = fIN.readLine()) != null) {
            matches.add(Match.getMatch(line));
        }
        return matches;
    }

    public Match() {
    }

    public static Match getMatch(String matchFileLine) {
        String[] match = matchFileLine.split("\\|", 9);
        return new Match(Integer.parseInt(match[2]), Integer.parseInt(match[3]), Integer.parseInt(match[4]), Integer.parseInt(match[6]), Integer.parseInt(match[7]));
    }

    public Match(int matchNumber, int red1, int red2, int blue1, int blue2) {
        this.matchNumber = matchNumber;
        this.red1 = red1;
        this.red2 = red2;
        this.blue1 = blue1;
        this.blue2 = blue2;
    }

    public int getMatchNumber() {
        return matchNumber;
    }

    public int getRed1() {
        return red1;
    }

    public int getRed2() {
        return red2;
    }

    public int getBlue1() {
        return blue1;
    }

    public int getBlue2() {
        return blue2;
    }

    @Override
    public String toString() {
        return "Match{" +
                "matchNumber=" + matchNumber +
                ", red1=" + red1 +
                ", red2=" + red2 +
                ", blue1=" + blue1 +
                ", blue2=" + blue2 +
                '}';
    }
}
