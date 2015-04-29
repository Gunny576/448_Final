import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JFrame;
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
     * Default serial version UID
     */
    private static final long serialVersionUID = 1L;

    private BorderLayout master;
    
    private JButton depositButton;
    private JButton withdrawalButton;
    private JToggleButton balanceButton;
    private JButton cancelButton;
    private JButton OKButton;
    
    private JLabel display;
    private JLabel balanceLabel;
    private NumPad pad;
    private JPanel buttonPanel;
    private JPanel padArea;
    private JPanel transactPanel;
    private JPanel transactArea;
    
    private DisplayDriver theATM;
    private static final int FRAME_WIDTH = 300;
    private static final int FRAME_HEIGHT = 300;
    private static final Color BACKGROUND = new Color(0, 206, 209);
    
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
        pad = new NumPad();
        padArea = new JPanel();
        padArea.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        padArea.add(pad, gbc);
        padArea.setBackground(BACKGROUND);

        display = new JLabel();
        display.setHorizontalAlignment(SwingConstants.CENTER);
        Border border = BorderFactory.createLineBorder(Color.BLACK, 3);
        display.setBorder(border);
        display.setForeground(Color.WHITE);
        display.setBackground(Color.BLACK);
        display.setOpaque(true);
        
        OKButton = new JButton("   OK   ");
        OKButton.addActionListener(new OKButtonListener());
        
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new CancelButtonListener());
        
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

        buttonPanel = new JPanel();
        buttonPanel.add(OKButton);
        buttonPanel.add(new JLabel("       "));
        buttonPanel.add(cancelButton);
        buttonPanel.setBackground(BACKGROUND);
        
        transactPanel = new JPanel();
        transactPanel.setLayout(new GridLayout(4, 1, 5, 5));
        transactPanel.add(balanceButton);
        transactPanel.add(balanceLabel);
        transactPanel.add(depositButton);
        transactPanel.add(withdrawalButton);
        transactPanel.setBackground(BACKGROUND);
        
        transactArea = new JPanel();
        transactArea.setLayout(new GridBagLayout());
        transactArea.add(transactPanel, gbc);
        transactArea.setBackground(BACKGROUND);
        
        setLayout(new BorderLayout());
        master = (BorderLayout)getLayout();
        add(display, BorderLayout.NORTH);
        add(padArea, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        showState();
        
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
            display.setText("<html><center>Error: Account not found<br>Please try again</center></html>");
            cancelButton.setText("   Exit   ");
            remove(transactArea);
            transactArea.setVisible(false);
            add(padArea, BorderLayout.CENTER);
            padArea.setVisible(true);
            break;
        case DisplayDriver.PIN:
            display.setText("<html><center>Enter PIN,<br>then press OK</center></html>");
            cancelButton.setText("Cancel");
            remove(transactArea);
            transactArea.setVisible(false);
            add(padArea, BorderLayout.CENTER);
            padArea.setVisible(true);
            break;
        case DisplayDriver.PINFAIL:
            display.setText("<html><center>Error: Wrong PIN<br>Please try again</center></html>");
            cancelButton.setText("Cancel");
            remove(transactArea);
            transactArea.setVisible(false);
            add(padArea, BorderLayout.CENTER);
            padArea.setVisible(true);
            break;
        case DisplayDriver.TRANSACT:
            display.setText("<html><center>Account " 
                    + theATM.getAccountNumber() 
                    + "<br>Select transaction type or view balance</center></html>");
            cancelButton.setText("Logout");
            remove(padArea);
            padArea.setVisible(false);
            add(transactArea, BorderLayout.CENTER);
            transactArea.setVisible(true);
            break;
        case DisplayDriver.DEPOSIT:
            display.setText("<html><center>Account " 
                    + theATM.getAccountNumber() 
                    + "<br>Enter amount to deposit</center></html>");
            cancelButton.setText("Cancel");
            remove(transactArea);
            transactArea.setVisible(false);
            add(padArea, BorderLayout.CENTER);
            padArea.setVisible(true);
            break;
        case DisplayDriver.WITHDRAW:
            display.setText("<html><center>Account " 
                    + theATM.getAccountNumber() 
                    + "<br>Enter amount to withdraw</center></html>");
            cancelButton.setText("Cancel");
            remove(transactArea);
            transactArea.setVisible(false);
            add(padArea, BorderLayout.CENTER);
            padArea.setVisible(true);
            break;
        case DisplayDriver.START:
        default:
            display.setText("<html><center>Enter account number,<br>then press OK</center></html>");
            cancelButton.setText("   Exit   ");
            remove(transactArea);
            transactArea.setVisible(false);
            add(padArea, BorderLayout.CENTER);
            padArea.setVisible(true);
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
    
    private class OKButtonListener implements ActionListener {  
        public void actionPerformed(ActionEvent event) {  
            int state = theATM.getState();
            switch (state) {
            case DisplayDriver.START:
            case DisplayDriver.ACCTFAIL:
                theATM.setAccountNumber((int) pad.getValue());
                break;
            case DisplayDriver.PIN:
                balanceButton.setSelected(false);
                balanceLabel.setText(" $ _____ ");
                // Intentional fall-through
            case DisplayDriver.PINFAIL:
                theATM.attemptPin((int) pad.getValue());
                break;
            case DisplayDriver.TRANSACT:
                // No action
                break;
            case DisplayDriver.DEPOSIT:
                theATM.deposit(pad.getValue());
                break;
            case DisplayDriver.WITHDRAW:
                theATM.withdraw(pad.getValue());
                break;
            default:
                break;
            }
            showState();
        }
    }
    
    private class CancelButtonListener implements ActionListener {  
        public void actionPerformed(ActionEvent event) {  
            int state = theATM.getState();
            if (state == DisplayDriver.START || state == DisplayDriver.ACCTFAIL)
                System.exit(0);
            else {
                balanceButton.setSelected(false);
                balanceLabel.setText(" $ _____ ");
                theATM.back();
            }
            showState();
        }
    }
    
    private class DButtonListener implements ActionListener {  
        public void actionPerformed(ActionEvent event) {  
            int state = theATM.getState();
            if (state == DisplayDriver.TRANSACT) {
                theATM.selectDeposit();
            }
            showState();
        }
    }
    
    private class WButtonListener implements ActionListener {  
        public void actionPerformed(ActionEvent event) {  
            int state = theATM.getState();
            if (state == DisplayDriver.TRANSACT) {
                theATM.selectWithdraw();
            }
            showState();
        }
    }
    
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
}
