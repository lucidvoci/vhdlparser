package com.vhdlparser;

public class Annotation {
    private String text;
    private int lineNumber;
    private String file;

    public Annotation (String text, int lineNumber, String file) {
        this.text = text;
        this.lineNumber = lineNumber;
        this.file = file;
    }

    public String getText() {
        return text;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getFile() {
        return file;
    }
}
