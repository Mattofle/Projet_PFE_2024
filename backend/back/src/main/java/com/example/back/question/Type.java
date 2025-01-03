package com.example.back.question;

import lombok.Getter;

/**
 * Enum representing the type of a question.
 */
@Getter
public enum Type {
  ALL("ALL"),
  OWNED_FACILITY("OWNED_FACILITY"),
  FACILITY("FACILITY"),
  WORKERS("WORKERS"),
  PRODUITS("PRODUITS");

  private final String type;

  Type(String type) {
    this.type = type;
  }

}
