package com.example.back.glossaire;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "glossaires", schema = "bd_pfe")
@ToString
@NoArgsConstructor
public class Glossaire {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer glossaireId;

  @Column(nullable = false)
  private String word;

  @Column(nullable = false)
  private String definition;

  private String comment;
  private String info;

  /**
   * Gets the glossaire ID.
   *
   * @return the glossaire ID
   */
  public Integer getGlossaireId() {
    return glossaireId;
  }

  /**
   * Sets the glossaire ID.
   *
   * @param glossaireId the ID to set
   */
  public void setGlossaireId(Integer glossaireId) {
    this.glossaireId = glossaireId;
  }

  /**
   * Gets the word.
   *
   * @return the word
   */
  public String getWord() {
    return word;
  }

  /**
   * Sets the word.
   *
   * @param word the word to set
   */
  public void setWord(String word) {
    this.word = word;
  }

  /**
   * Gets the definition.
   *
   * @return the definition
   */
  public String getDefinition() {
    return definition;
  }

  /**
   * Sets the definition.
   *
   * @param definition the definition to set
   */
  public void setDefinition(String definition) {
    this.definition = definition;
  }

  /**
   * Gets the comment.
   *
   * @return the comment
   */
  public String getComment() {
    return comment;
  }

  /**
   * Sets the comment.
   *
   * @param comment the comment to set
   */
  public void setComment(String comment) {
    this.comment = comment;
  }

  /**
   * Gets the info.
   *
   * @return the info
   */
  public String getInfo() {
    return info;
  }

  /**
   * Sets the info.
   *
   * @param info the info to set
   */
  public void setInfo(String info) {
    this.info = info;
  }

}