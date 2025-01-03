package com.example.back.scoring;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Class representing a score in the database.
 * This class is a JPA entity linked to the "scoreesg" table in the "bd_pfe" schema.
 */
@NoArgsConstructor
@AllArgsConstructor
public class ScoreESG {

    private double score;

    private double scoreEngament;
    private double scoreMax;

    /**
     * Gets the score.
     *
     * @return the score
     */
    public double getScore() {
        return score;
    }

    /**
     * Sets the score.
     *
     * @param score the score to set
     */
    public void setScore(double score) {
        this.score = score;
    }


    /**
     * Gets the score engament.
     *
     * @return the score engament
     */
    public double getScoreEngament() {
        return scoreEngament;
    }

    /**
     * Sets the score engament.
     *
     * @param scodeEngament the score engament to set
     */
    public void setScoreEngament(double scodeEngament) {
        this.scoreEngament = scodeEngament;
    }

    /**
     * Gets the score max.
     *
     * @return the score max
     */
    public double getScoreMax() {
        return scoreMax;
    }

    /**
     * Sets the score max.
     *
     * @param scoreMax the score max to set
     */
    public void setScoreMax(double scoreMax) {
        this.scoreMax = scoreMax;
    }
}
