import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

import javax.swing.JOptionPane;

/**
    Test run of the ATM program by Group 8.
*/
public class ATMRun {
    public static void main(String[] args) {
        DisplayDriver ATMdisplay;

        try {
            Bank theBank = new Bank();
            ATMdisplay = new DisplayDriver(theBank);
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Error");
            return;
        }
    }
}
