package p2;

import javafx.application.Application;
import p2.binarytree.AutoComplete;
import p2.gui.MyApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * Main entry point in executing the program.
 */
public class Main {

    /**
     * Main entry point in executing the program.
     *
     * @param args program arguments, currently ignored
     */
    public static void main(String[] args) {
        AutoComplete ac = new AutoComplete();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Main.class.getResourceAsStream("words_alpha.txt"))))) {
            String line;
            while ((line = br.readLine()) != null) {
                ac.insert(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ac.autoComplete("bat", 30).forEach(System.out::println);


        //Application.launch(MyApplication.class, args);

    }
}
