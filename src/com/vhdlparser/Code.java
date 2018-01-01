package com.vhdlparser;

import java.io.*;
import java.util.ArrayList;

public class Code {
    private static Code instance = null;
    public String srcFile = "";

    public ArrayList<String> inputPorts = new ArrayList<>();
    public ArrayList<String> outputPorts = new ArrayList<>();

    private static ArrayList<Process> processes = new ArrayList<>();
    private static ArrayList<Annotation> annotations = new ArrayList<>();

    private Code () {}

    public static Code getInstance () {
        if (instance == null) {
            instance = new Code ();
        }
        return instance;
    }

    public void addProcess (Process p) { processes.add(p); }

    public void addAnnotation (Annotation s){
        annotations.add (s);
    }

    public void addInput (String s) { inputPorts.add(s); }

    public void addOutput (String s) { outputPorts.add(s); }

    public void generateAnnotations (){
        ArrayList<String> repeated_outputs = new ArrayList<>();
        for (Process p : processes) {

            if (p.clocked ) {
                if (!p.sensitivityList.contains("clk") || p.sensitivityList.size() == 0) {
                    p.annotations.add(new Annotation("-- psl always false // Process is clocked yet it is not sensitive to clk", p.lineNumber, "file"));
                }
                else if (p.sensitivityList.size() > 1) {
                    p.annotations.add(new Annotation("-- psl always false // Process is clocked, are the other signal(s) in sensitivity list necessary?", p.lineNumber, srcFile));
                }
            }

            ArrayList<String> tmp = new ArrayList<>(p.outputPortsUsed);
            tmp.retainAll(repeated_outputs);
            if (tmp.size() > 0) {
                for (String port : tmp) {
                    p.annotations.add(new Annotation("-- psl always false // The output port '" + port + "' is written into from more than one process", p.lineNumber, srcFile));
                }
            }
            repeated_outputs.addAll(p.outputPortsUsed);

        }
    }

    public void printAnnotations (){
        for (Annotation a : annotations) {
            System.out.println(a);
        }
    }

    public void printProcesses (){
        for (Process p : processes) {
            System.out.println(p);
        }
    }


    public void generateOutput() {
        for (Process p : processes) {
            for (Annotation a : p.annotations) {
                System.out.println(a.getFile() + ":" + a.getLineNumber() + ": " + a.getText());
            }
        }
    }
    /*public void generateFile() throws IOException {
        File outFile = new File (srcFile + ".o");
        if (!outFile.exists()) {
            outFile.createNewFile();
        }
        BufferedReader br = new BufferedReader(new FileReader(srcFile));
        BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));

        String line;
        int processNumber = 0;
        int lineNumber = 1;
        while ((line = br.readLine()) != null) {
            if (processes.size() > processNumber && lineNumber == processes.get(processNumber).lineNumber) {
                for (Annotation annotation : processes.get(processNumber).annotations) {
                    bw.write(annotation.getText() + "\n");
                }
                processNumber++;
            }
            bw.write(line + "\n");
            lineNumber++;
        }
        bw.close();
    }*/
}
