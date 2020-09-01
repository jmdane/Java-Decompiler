package com.jmdane;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import com.strobel.assembler.metadata.JarTypeLoader;
import com.strobel.decompiler.Decompiler;
import com.strobel.decompiler.DecompilerSettings;
import com.strobel.decompiler.PlainTextOutput;

public class Decompile {

    public static ArrayList<ArrayList<String>> DecompiledData = new ArrayList<>();

    public static ArrayList<String> DecompileJar(String jarPath) throws IOException {
        // Your jar file
        JarFile jar = new JarFile(jarPath);
        // Getting the files into the jar
        Enumeration<? extends JarEntry> entries = jar.entries();

        //New arraylist for input and output
        ArrayList<String> Data = new ArrayList<>();
        // Iterates into the files in the jar file
        while (entries.hasMoreElements()) {
            ZipEntry Entry = entries.nextElement();

            // Check if this is a class
            if (Entry.getName().endsWith(".class")) {

                // Relative path of file into the jar.
                String className = Entry.getName();

                // Complete class name
                className = className.replace(".class", "");
                // Set decompiler settings
                new DecompilerSettings();
                DecompilerSettings settings = DecompilerSettings.javaDefaults();
                // Set type loader to jar file
                settings.setTypeLoader(new JarTypeLoader(jar));

                // Define string output
                StringWriter classData = new StringWriter();
                // Decompile class to string
                Decompiler.decompile(className, new PlainTextOutput(classData), settings);

                //Get exact classname method
                String[] classnamestring = Entry.getName().split("/");

                //Classname ,  Packagename , jar Path , class source code
                String outputstring = "Class Name: "+classnamestring[classnamestring.length - 1]+"\n"+
                        "Package Name: "+className+"\n"+
                        "Jar Path: "+jarPath+"\n"+
                        classData;
                //Add data per jar
                Data.add(outputstring);
            }
        }
        //return jar data
        return Data;
    }

}