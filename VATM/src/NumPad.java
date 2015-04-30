import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

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
    private JLabel display;
    private String pinString;
    private int state;
    
    public static final int START = 1;
    public static final int ACCTFAIL = 2;
    public static final int PIN = 3;
    public static final int PINFAIL = 4;
    public static final int CREATEACCT = 5;
    public static final int CREATEPIN = 6;
    public static final int TRANSACT = 7;
    public static final int WITHDRAW = 8;
    public static final int DEPOSIT = 9;
    public static final int CLOSED = 10;
    
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
        display = new JLabel();
        display.setText("");
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

        clearButton.addActionListener(new 
                ClearButtonListener());        
        
        add(buttonPanel, "Center");
        display.setPreferredSize(new Dimension(buttonPanel.getWidth(), 20));
        display.setMinimumSize(new Dimension(buttonPanel.getWidth(), 20));
        display.setBorder(new EmptyBorder(0, 4, 0, 4));
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
                else if (label.equals(".") && (state == START || state == ACCTFAIL || state == PIN || state == PINFAIL || state == CREATEACCT ||state == CREATEPIN))
                    return;
                else if (state == PIN || state == PINFAIL || state == CREATEPIN) {
                    if (display.getText().length() >= 4)
                        return;
                    else {
                        // Append label text to pinString and * to display
                        pinString = pinString + label;
                        display.setText(display.getText() + '*');
                    }
                }
                else if (display.getText().contains(".") && display.getText().substring(display.getText().indexOf('.')).length() > 2) {
                    return;
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
            if (state == PIN || state == PINFAIL || state == CREATEPIN)
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
