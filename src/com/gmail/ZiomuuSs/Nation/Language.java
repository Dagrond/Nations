package com.gmail.ZiomuuSs.Nation;

import java.util.HashSet;

public class Language {
  public static enum SkillLevel {
    BEGINNER, ADVANCED, NATIVE;
  }
  private static HashSet<Language> languages = new HashSet<>(); //set of all languages
  private String name; //name of this language
  
  public Language(String name) {
    this.name = name;
    languages.add(this);
  }
  
  //getters
  @Override
  public String toString() {
    return name;
  }
  
  public static HashSet<Language> getLanguages() {
    return languages;
  }
}
