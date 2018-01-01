package com.vhdlparser;

import java.util.ArrayList;

public class Process {
    public int lineNumber;
    public Boolean clocked = false;
    public String clockName = null;
    public Boolean outputCorrect = true;

    public String code;
    public ArrayList<String> sensitivityList = new ArrayList<>();
    public ArrayList<String> outputPortsUsed = new ArrayList<>();
    public ArrayList<Annotation> annotations = new ArrayList<>();

    public Process (String code) {
        this.code =  code;
    }

    public String toString () {
        String clockedString = clocked ? "Clocked\n" : "Non-clocked\n";
        return clockedString + clockName + "\n" + sensitivityList + "\n" + code;
    }
}
