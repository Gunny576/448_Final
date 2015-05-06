import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
    A JFrame to display the ATM GUI.
*/
public class ATMFrame extends JFrame {
    /**
     * Default serial version UID for the JFrame.
     */
    private static final long serialVersionUID = 1L;

    // Handle for "this"
    private ATMFrame master;
    
    // JButton objects
    private JButton depositButton;
    private JButton withdrawalButton;
    private JToggleButton balanceButton;
    private JButton closeButton;
    private JButton cancelButton;
    private JButton OKButton;
    private JButton createButton;
    
    // JLabel objects
    private JLabel display;
    private JLabel balanceLabel;
    private JLabel blankLabel;
    private JPanel buttonPanel;
    private JPanel bottomPanel;
    private JPanel padArea;
    private JPanel transactPanel;
    private JPanel transactArea;
    
    // Number pad
    private NumPad pad;
    
    private DisplayDriver theATM;
    private static final int FRAME_WIDTH = 300;
    private static final int FRAME_HEIGHT = 300;
    private static final Color BACKGROUND = new Color(0, 206, 209);
    
    // Keeps track of the transaction result
    private double transactResult = -2.0;
    
    /**
        Constructor for the ATM GUI.
    */
    public ATMFrame(DisplayDriver anATM) {  
        theATM = anATM;
        
        init();
    }
    
    /**
        Initialization method to keep the constructor clean.
    */
    private void init() {
        master = this;
    	
        // Set up layout of the frame
    	pad = new NumPad();
        padArea = new JPanel();
        padArea.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        padArea.add(pad, gbc);
        padArea.setBackground(BACKGROUND);

        // Set up the numerical display above the number pad
        display = new JLabel();
        display.setHorizontalAlignment(SwingConstants.CENTER);
        Border border = BorderFactory.createLineBorder(Color.BLACK, 3);
        display.setBorder(border);
        display.setForeground(Color.WHITE);
        display.setBackground(Color.BLACK);
        display.setOpaque(true);
        
        // Set up the buttons
        OKButton = new JButton("   OK   ");
        OKButton.addActionListener(new OKButtonListener());
        
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new CancelButtonListener());
        
        createButton = new JButton("Create New Account");
        createButton.addActionListener(new CreateButtonListener());
        blankLabel = new JLabel("                                                   ");
        blankLabel.setPreferredSize(new Dimension(createButton.getWidth(), 26));
        blankLabel.setMinimumSize(new Dimension(createButton.getWidth(), 26));
        blankLabel.setBackground(BACKGROUND);
        blankLabel.setVisible(false);
        blankLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        depositButton = new JButton("Deposit");
        depositButton.addActionListener(new DButtonListener());

        withdrawalButton = new JButton("Withdrawal");
        withdrawalButton.addActionListener(new WButtonListener());
        
        balanceButton = new JToggleButton("Show Balance");
        balanceButton.addActionListener(new BButtonListener());
        balanceLabel = new JLabel(" $ _____ ");
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        balanceLabel.setBackground(Color.BLACK);
        balanceLabel.setOpaque(true);
        balanceLabel.setForeground(Color.GREEN);
        balanceLabel.setFont(new Font("Serif", Font.BOLD, 18));
        
        closeButton = new JButton("Close Account");
        closeButton.addActionListener(new CButtonListener());
        
        //Add buttons to their appropriate panels
        buttonPanel = new JPanel();
        buttonPanel.add(OKButton);
        buttonPanel.add(new JLabel("       "));
        buttonPanel.add(cancelButton);
        buttonPanel.setBackground(BACKGROUND);
        
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.add(buttonPanel);
        bottomPanel.add(createButton);
        createButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createButton.setVisible(true);
        bottomPanel.setBackground(BACKGROUND);
        
        transactPanel = new JPanel();
        transactPanel.setLayout(new GridLayout(5, 1, 5, 5));
        transactPanel.add(balanceButton);
        transactPanel.add(balanceLabel);
        transactPanel.add(depositButton);
        transactPanel.add(withdrawalButton);
        transactPanel.add(closeButton);
        transactPanel.setBackground(BACKGROUND);
        
        transactArea = new JPanel();
        transactArea.setLayout(new GridBagLayout());
        transactArea.add(transactPanel, gbc);
        transactArea.setBackground(BACKGROUND);
        
        setLayout(new BorderLayout());
        add(display, BorderLayout.NORTH);
        add(padArea, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        showState();
        
        // Set default action for Enter key
        JRootPane rootPane = SwingUtilities.getRootPane(OKButton); 
        rootPane.setDefaultButton(OKButton);

        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setBackground(BACKGROUND);
    }
    
    /** 
        Updates appropriate components for new state.
    */
    public void showState() {  
        int state = theATM.getState();
        pad.clear();
        pad.setState(state);
        switch (state) {
        case DisplayDriver.ACCTFAIL:
            // User has just entered an invalid account number
            display.setText("<html><center>Error: Account not found<br>Please try again</center></html>");
            cancelButton.setText("   Exit   ");
            remove(transactArea);
            transactArea.setVisible(false);
            add(padArea, BorderLayout.CENTER);
            padArea.setVisible(true);
            bottomPanel.remove(blankLabel);
            blankLabel.setVisible(false);
            bottomPanel.add(createButton);
            createButton.setVisible(true);
            break;
        case DisplayDriver.PIN:
            // User has entered a valid account number and needs to enter a pin
            display.setText("<html><center>Enter PIN,<br>then press OK</center></html>");
            cancelButton.setText("Cancel");
            remove(transactArea);
            transactArea.setVisible(false);
            add(padArea, BorderLayout.CENTER);
            padArea.setVisible(true);
            bottomPanel.remove(createButton);
            createButton.setVisible(false);
            bottomPanel.add(blankLabel);
            blankLabel.setVisible(false);
            break;
        case DisplayDriver.PINFAIL:
            // User has just entered the wrong pin
            display.setText("<html><center>Error: Wrong PIN<br>Please try again</center></html>");
            cancelButton.setText("Cancel");
            remove(transactArea);
            transactArea.setVisible(false);
            add(padArea, BorderLayout.CENTER);
            padArea.setVisible(true);
            bottomPanel.remove(createButton);
            createButton.setVisible(false);
            bottomPanel.add(blankLabel);
            blankLabel.setVisible(false);
            break;
        case DisplayDriver.CREATEACCT:
        	// User has just selected the Create Account option
            display.setText("<html><center>Enter account number for new account,<br>then press OK</center></html>");
            cancelButton.setText("Cancel");
            remove(transactArea);
            transactArea.setVisible(false);
            add(padArea, BorderLayout.CENTER);
            padArea.setVisible(true);
            bottomPanel.remove(createButton);
            createButton.setVisible(false);
            bottomPanel.add(blankLabel);
            blankLabel.setVisible(false);
        	break;
        case DisplayDriver.CREATEPIN:
        	// User has entered their new account number and needs to enter the new pin
            display.setText("<html><center>Enter PIN for new account,<br>then press OK</center></html>");
            cancelButton.setText("Cancel");
            remove(transactArea);
            transactArea.setVisible(false);
            add(padArea, BorderLayout.CENTER);
            padArea.setVisible(true);
            bottomPanel.remove(createButton);
            createButton.setVisible(false);
            bottomPanel.add(blankLabel);
            blankLabel.setVisible(false);
        	break;
        case DisplayDriver.TRANSACT:
            // User has logged in and needs to select an action
            display.setText("<html><center>Account " 
                    + theATM.getAccountNumber() 
                    + "<br>Select transaction type or view balance</center></html>");
            cancelButton.setText("Logout");
            remove(padArea);
            padArea.setVisible(false);
            add(transactArea, BorderLayout.CENTER);
            transactArea.setVisible(true);
            bottomPanel.remove(createButton);
            createButton.setVisible(false);
            bottomPanel.add(blankLabel);
            blankLabel.setVisible(false);
            // Show confirmation/error messages as pop-ups
            if (transactResult >= -1.0) {
            	if (theATM.getPrevState() == DisplayDriver.DEPOSIT) {
            		if (transactResult >= 0) {
            			JOptionPane.showMessageDialog(this, "Deposit of $" + String.format("%.2f", transactResult) + " was successful");
            		}
            		else {
            			JOptionPane.showMessageDialog(this, "Error: Deposit unsuccessful");
            		}
            	}
            	else if (theATM.getPrevState() == DisplayDriver.WITHDRAW) {
            		if (transactResult >= 0) {
            			JOptionPane.showMessageDialog(this, "Withdrawal of $" + String.format("%.2f", transactResult) + " was successful");
            		}
            		else {
            			JOptionPane.showMessageDialog(this, "Error: Insufficient funds");
            		}
            	}
            	transactResult = -2.0;
            }
            break;
        case DisplayDriver.DEPOSIT:
            // User has just selected the Deposit option
            display.setText("<html><center>Account " 
                    + theATM.getAccountNumber() 
                    + "<br>Enter amount to deposit</center></html>");
            cancelButton.setText("Cancel");
            remove(transactArea);
            transactArea.setVisible(false);
            add(padArea, BorderLayout.CENTER);
            padArea.setVisible(true);
            bottomPanel.remove(createButton);
            createButton.setVisible(false);
            bottomPanel.add(blankLabel);
            blankLabel.setVisible(false);
            break;
        case DisplayDriver.WITHDRAW:
            // User has just selected the Withdraw option
            display.setText("<html><center>Account " 
                    + theATM.getAccountNumber() 
                    + "<br>Enter amount to withdraw</center></html>");
            cancelButton.setText("Cancel");
            remove(transactArea);
            transactArea.setVisible(false);
            add(padArea, BorderLayout.CENTER);
            padArea.setVisible(true);
            bottomPanel.remove(createButton);
            createButton.setVisible(false);
            bottomPanel.add(blankLabel);
            blankLabel.setVisible(false);
            break;
        case DisplayDriver.START:
        default:
            // User is at the start screen
            display.setText("<html><center>Enter account number,<br>then press OK</center></html>");
            cancelButton.setText("   Exit   ");
            remove(transactArea);
            transactArea.setVisible(false);
            add(padArea, BorderLayout.CENTER);
            padArea.setVisible(true);
            bottomPanel.remove(blankLabel);
            blankLabel.setVisible(true);
            bottomPanel.add(createButton);
            createButton.setVisible(true);
            // Show confirmation of logout or account closure
            if (theATM.getPrevState() >= DisplayDriver.TRANSACT && theATM.getPrevState() < DisplayDriver.CLOSED) {
            	JOptionPane.showMessageDialog(this, "Successfully logged out");
            }
            else if (theATM.getPrevState() == DisplayDriver.CLOSED) {
            	JOptionPane.showMessageDialog(this, "Account successfully closed");
            }
            break;
        }
    }
    
    /** 
        Sets the current ATM state.
        @param aState the state of the ATM 
    */
    public void setState(int state) {
        pad.setState(state);
    }
    
    /**
        Displays a pop-up error for account number failure
    */
    public void newAccountNumberFail() {
    	JOptionPane.showMessageDialog(this, "Error: Account number already exists");
    }
    
    /**
        Displays a pop-up error for new pin failure
    */
    public void newAccountPinFail() {
    	JOptionPane.showMessageDialog(this, "Error: Invalid PIN");
    }
    
    /**
        Displays a pop-up confirmation for new account creation
    */
    public void newAccountSuccess() {
    	JOptionPane.showMessageDialog(this, "Successfully created new account");
    }
    
    /**
        An action listener for the OK button
    */
    private class OKButtonListener implements ActionListener {  
        public void actionPerformed(ActionEvent event) {  
            int state = theATM.getState();
            switch (state) {
            case DisplayDriver.START:
            	// Intentional fall-through
            case DisplayDriver.ACCTFAIL:
                theATM.setAccountNumber((int) pad.getValue());
                break;
            case DisplayDriver.PIN:
                // Intentional fall-through
            case DisplayDriver.PINFAIL:
                balanceButton.setSelected(false);
                balanceLabel.setText(" $ _____ ");
                theATM.attemptPin((int) pad.getValue());
                break;
            case DisplayDriver.CREATEACCT:
            	if (!theATM.createAccountNumber((int) pad.getValue())) {
            		newAccountNumberFail();
            	}
            	break;
            case DisplayDriver.CREATEPIN:
            	if (theATM.createPin((int) pad.getValue())) {
            		newAccountSuccess();
            	}
            	else {
            		newAccountPinFail();
            	}
            	break;
            case DisplayDriver.TRANSACT:
                // No action
                break;
            case DisplayDriver.DEPOSIT:
                balanceButton.setSelected(false);
                balanceLabel.setText(" $ _____ ");
                transactResult = theATM.deposit(pad.getValue());
                break;
            case DisplayDriver.WITHDRAW:
                balanceButton.setSelected(false);
                balanceLabel.setText(" $ _____ ");
                transactResult = theATM.withdraw(pad.getValue());
                break;
            default:
                break;
            }
            showState();
        }
    }
    
    /**
        An action listener for the Cancel button
    */
    private class CancelButtonListener implements ActionListener {  
        public void actionPerformed(ActionEvent event) {  
            int state = theATM.getState();
            if (state == DisplayDriver.START || state == DisplayDriver.ACCTFAIL) {
                System.exit(0);
            }
            else {
                balanceButton.setSelected(false);
                balanceLabel.setText(" $ _____ ");
                transactResult = -2.0;
                theATM.back();
            }
            showState();
        }
    }
    
    /**
        An action listener for the Create Account button
    */
    private class CreateButtonListener implements ActionListener {
    	public void actionPerformed(ActionEvent event) {
    		int state = theATM.getState();
    		if (state == DisplayDriver.START || state == DisplayDriver.ACCTFAIL) {
    			theATM.createAccount();
    		}
    		showState();
    	}
    }
    
    /**
        An action listener for the Deposit button
    */
    private class DButtonListener implements ActionListener {  
        public void actionPerformed(ActionEvent event) {  
            int state = theATM.getState();
            if (state == DisplayDriver.TRANSACT) {
                theATM.selectDeposit();
            }
            showState();
        }
    }
    
    /**
        An action listener for the Withdraw button
    */
    private class WButtonListener implements ActionListener {  
        public void actionPerformed(ActionEvent event) {  
            int state = theATM.getState();
            if (state == DisplayDriver.TRANSACT) {
                theATM.selectWithdraw();
            }
            showState();
        }
    }
    
    /**
        An action listener for the Show Balance button
    */
    private class BButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            int state = theATM.getState();
            if (state == DisplayDriver.TRANSACT) {
                if (balanceButton.isSelected()) {
                    double balance = theATM.getBalance();
                    balanceLabel.setText(" $ " + String.format("%.2f", balance) + " ");
                }
                else {
                    balanceLabel.setText(" $ _____ ");
                }
            }
            showState();
        }
    }
    
    /**
        An action listener for the Close Account button
    */
    private class CButtonListener implements ActionListener {  
        public void actionPerformed(ActionEvent event) {  
            int state = theATM.getState();
            if (state == DisplayDriver.TRANSACT) {
                // Prompt for confirmation before closing the account
                int response = JOptionPane.showConfirmDialog(master, "Are you sure you want to close this account?", "Please Confirm", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                	theATM.closeAccount();
                	theATM.back();
                }
                else {
                	balanceButton.setSelected(false);
                    balanceLabel.setText(" $ _____ ");
                }
            }
            showState();
        }
    }
}
