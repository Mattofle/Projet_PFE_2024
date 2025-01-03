package com.example.back.moduleCompany;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

/**
 * Class representing a module in the database.
 * This class is a JPA entity linked to the "modulecompany" table in the "bd_pfe" schema.
 */
@Entity
@Table(name = "modulecompany",
        schema = "bd_pfe"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ModuleCompanyid.class)
public class ModuleCompany {

    @Id
    @Column(name = "module", nullable = false)
    private int moduleId;

    @Id
    @Column(name = "company", nullable = false)
    private int companyId;

    @Column(name="date_form")
    private Date date_form;

    @Column()
    private double score;

    @Column(name = "engagementscore")
    private double score_engagement;

    @Column()
    private double scoremax;

    @Column()
    private boolean is_validated;




        /**
         * Gets the module ID.
         *
         * @return the module ID
         */
        public int getModuleId() {
                return moduleId;
        }

        /**
         * Sets the module ID.
         *
         * @param moduleId the ID to set
         */
        public void setModuleId(int moduleId) {
                this.moduleId = moduleId;
        }

        /**
         * Gets the company ID.
         *
         * @return the company ID
         */
        public int getCompanyId() {
                return companyId;
        }

        /**
         * Sets the company ID.
         *
         * @param companyId the ID to set
         */
        public void setCompanyId(int companyId) {
                this.companyId = companyId;
        }

        /**
         * Gets the date of the form.
         *
         * @return the date of the form
         */
        public Date getDate_form() {
                return date_form;
        }

        /**
         * Sets the date of the form.
         *
         * @param date the date to set
         */
        public void setDate_form(Date date) {
                this.date_form = date;
        }

        /**
         * Gets the score.
         *
         * @return the score
         */
        public Double getScore() {
                return score;
        }

        /**
         * Sets the score.
         *
         * @param score the score to set
         */
        public void setScore(Double score) {
                this.score = score;
        }

        /**
         * Gets the engagement score.
         *
         * @return the engagement score
         */
        public double getScore_engagement() {
                return score_engagement;
        }

        /**
         * Sets the engagement score.
         *
         * @param score_engagement the score to set
         */
        public void setScore_engagement(double score_engagement) {
                this.score_engagement = score_engagement;
        }

        /**
         * Gets the maximum score.
         *
         * @return the maximum score
         */
        public double getScoremax() {
                return scoremax;
        }

        /**
         * Sets the maximum score.
         *
         * @param scoremax the score to set
         */
        public void setScoremax(double scoremax) {
                this.scoremax = scoremax;
        }

        /**
         * Gets if the module is validated.
         *
         * @return if the module is validated
         */
        public boolean isIs_validated() {
                return is_validated;
        }

        /**
         * Sets if the module is validated.
         *
         * @param is_validated if the module is validated
         */
        public void setIs_validated(boolean is_validated) {
                this.is_validated = is_validated;
        }
}
