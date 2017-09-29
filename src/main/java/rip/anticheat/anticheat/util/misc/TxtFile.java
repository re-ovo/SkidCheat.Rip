/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.java.JavaPlugin
 */
package rip.anticheat.anticheat.util.misc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.plugin.java.JavaPlugin;

public class TxtFile {
    private File File;
    private String Name;
    private List<String> Lines = new ArrayList<String>();

    public TxtFile(JavaPlugin javaPlugin, String string, String string2) {
        this.File = new File(javaPlugin.getDataFolder() + string);
        this.File.mkdirs();
        this.File = new File(javaPlugin.getDataFolder() + string, String.valueOf(String.valueOf(string2)) + ".txt");
        try {
            this.File.createNewFile();
        }
        catch (IOException iOException) {
            // empty catch block
        }
        this.Name = string2;
        this.readTxtFile();
    }

    public void clear() {
        this.Lines.clear();
    }

    public void addLine(String string) {
        this.Lines.add(string);
    }

    public void write() {
        try {
            FileWriter fileWriter = new FileWriter(this.File, false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (String string : this.Lines) {
                bufferedWriter.write(string);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
            fileWriter.close();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void readTxtFile() {
        this.Lines.clear();
        try {
            String string;
            FileReader fileReader = new FileReader(this.File);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((string = bufferedReader.readLine()) != null) {
                this.Lines.add(string);
            }
            bufferedReader.close();
            fileReader.close();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public String getName() {
        return this.Name;
    }

    public String getText() {
        String string = "";
        int n = 0;
        while (n < this.Lines.size()) {
            String string2 = this.Lines.get(n);
            string = String.valueOf(String.valueOf(string)) + string2 + (this.Lines.size() - 1 == n ? "" : "\n");
            ++n;
        }
        return string;
    }

    public List<String> getLines() {
        return this.Lines;
    }
}

