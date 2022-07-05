package ru.example.socnetwork.model;

import lombok.Data;

@Data
public class ClassMethodInformation {

  private String className;
  private String methodName;
  private String outputLine;

  public ClassMethodInformation(String className, String methodName, String outputLine) {
    this.className = className;
    this.methodName = methodName;
    this.outputLine = outputLine;
  }

}
