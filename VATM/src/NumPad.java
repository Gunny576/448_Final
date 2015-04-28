import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
    A JPanel component that simulates a number pad for numerical entry.
*/
public class NumPad extends JPanel {
    /**
     * Default serial version UID
     */
    private static final long serialVersionUID = 1L;
    
    private JPanel buttonPanel;
    private JButton clearButton;
    private JTextField display;
    private String pinString;
    private int state;
    
    public static final int START = 1;
    public static final int PIN = 2;
    public static final int TRANSACT = 3;
    public static final int ACCTFAIL = 4;
    public static final int PINFAIL = 5;
    
    /**
        Constructor for the keypad class.
    */
    public NumPad() {  
        init();
    }
    
    /**
        Initialization method to keep the constructor clean.
    */
    private void init() {
        setLayout(new BorderLayout());
        
        // Add display field
    
        display = new JTextField();
        add(display, "North");
        
        pinString = "";

        // Make button panel

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 3));
        
        // Add digit buttons
        
        addButton("7");
        addButton("8");
        addButton("9");
        addButton("4");
        addButton("5");
        addButton("6");
        addButton("1");
        addButton("2");
        addButton("3");
        addButton("0");        
        addButton(".");
        
        // Add clear entry button
        
        clearButton = new JButton("CLR");
        buttonPanel.add(clearButton);

        class ClearButtonListener implements ActionListener {  
            public void actionPerformed(ActionEvent event) {  
                display.setText("");
                pinString = "";
            }
        }
        ActionListener listener = new ClearButtonListener();        

        clearButton.addActionListener(new 
                ClearButtonListener());        
        
        add(buttonPanel, "Center");
        setState(START);
        setSize(300, 300);
    }

    /**
        Adds a button to the button panel 
        @param label the button label
    */
    private void addButton(final String label) {  
        class DigitButtonListener implements ActionListener {  
            public void actionPerformed(ActionEvent event) {  

                // Don't add two decimal points
                if (label.equals(".") && display.getText().indexOf(".") != -1) 
                    return;
                else if (label.equals(".") && (state == START || state == ACCTFAIL || state == PIN || state == PINFAIL))
                    return;
                else if (state == PIN || state == PINFAIL) {
                    if (display.getText().length() >= 4)
                        return;
                    else {
                        // Append label text to pinString and * to display
                        pinString = pinString + label;
                        display.setText(display.getText() + '*');
                    }
                }
                else
                    // Append label text to display
                    display.setText(display.getText() + label);
            }
        }

        JButton button = new JButton(label);
        buttonPanel.add(button);
        ActionListener listener = new DigitButtonListener();
        button.addActionListener(listener);
    }

    /** 
        Gets the value that the user entered. 
        @return the value in the text field, or the PIN value
    */
    public double getValue() {  
        try {
            if (state == PIN || state == PINFAIL)
                return Double.parseDouble(pinString);
            else
                return Double.parseDouble(display.getText());
        }
        catch (Exception e) {
            return -1.0;
        }
    }
    
    /** 
        Clears the display and PIN string. 
    */
    public void clear() {  
        display.setText("");
        pinString = "";
    }
    
    /** 
        Sets the current ATM state.
        @param aState the state of the ATM
    */
    public void setState(int aState) {
        state = aState;
    }
}
