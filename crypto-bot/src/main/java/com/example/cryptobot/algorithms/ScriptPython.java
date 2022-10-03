package com.example.cryptobot.algorithms;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ScriptPython {
    private Process process;
    private final String path;

    public ScriptPython(String path) {
        this.path = path;
        this.process = null;
    }

    public void runScript() throws IOException {
        // this throws IOException that will be caught in service.
        System.out.println(" py -3.10 " + path);
        process =  Runtime.getRuntime().exec(" py -3.10 " + path);
    }

    public void stopScript() {
        process.destroyForcibly();
    }
}