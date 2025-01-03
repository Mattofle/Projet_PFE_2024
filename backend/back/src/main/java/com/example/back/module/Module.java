package com.example.back.module;

import jakarta.persistence.*;
import lombok.*;

/**
 * Class representing a module in the database.
 * This class is a JPA entity linked to the "modules" table in the "bd_pfe" schema.
 */
@Entity
@Table(name = "modules", schema = "bd_pfe")
@NoArgsConstructor
@AllArgsConstructor
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer moduleId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, name = "totalscore")
    private double totalScore;

    @Column(name = "parentmodule")
    private Integer parentModule;

    //get
    /**
     * Gets the module ID.
     *
     * @return the module ID
     */
    public Integer getModuleId() {
        return this.moduleId;
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Gets the total score.
     *
     * @return the total score
     */
    public double getTotalScore() {
        return this.totalScore;
    }

    /**
     * Gets the parent module.
     *
     * @return the parent module
     */
    public Integer getParentModule() {
        return this.parentModule;
    }


    @Override
    public String toString() {
        return "Module{" +
            "moduleId=" + moduleId +
            ", title='" + title + '\'' +
            ", totalScore=" + totalScore +
            ", parentModule=" + parentModule +
            '}';
    }
}
