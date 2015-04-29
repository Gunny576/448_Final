import javax.swing.JFrame;

/**
    Digital Automated Teller Machine.
*/
public class DisplayDriver {
    private int state;
    private int accountNumber;
    private int accountPin;
    private Bank theBank;
    
    public static final int START = 1;
    public static final int PIN = 2;
    public static final int TRANSACT = 3;
    public static final int ACCTFAIL = 4;
    public static final int PINFAIL = 5;
    public static final int WITHDRAW = 6;
    public static final int DEPOSIT = 7;
    
    /**
        Constructor for the DisplayDriver class
        @param aBank the bank where all accounts reside
    */     
    public DisplayDriver(Bank aBank) {
        theBank = aBank;
        
        init();
    }
    
    /**
        Initialization method to keep the constructor clean
    */   
    private void init() {
        accountNumber = -1;
        accountPin = -1;
        state = START;
        
        JFrame frame = new ATMFrame(this);
        frame.setTitle("Group 8 Bank ATM");        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /** 
        Sets the current account number and sets state to PIN. 
        (Precondition: state is START or ACCTFAIL)
        @param number the account number.
    */
    public void setAccountNumber(int number) {
        assert state == START || state == ACCTFAIL;
        if (theBank.findAccount(number)) {
            accountNumber = number;
            state = PIN;
        }
        else
            state = ACCTFAIL;
    }
    
    /** 
        Gets the current account number. 
        (Precondition: state is TRANSACT)
        @return the balance
    */
    public int getAccountNumber() {
        assert state == TRANSACT;
        return accountNumber;
    }

    /** 
        Tries a PIN for the current account.
        If found sets state to TRANSACT, else to PINFAIL.
        (Precondition: state is PIN)
        @param pin the PIN to attempt
    */
    public void attemptPin(int pin) {
        assert state == PIN;
        if (theBank.tryPin(accountNumber, pin)) {
            accountPin = pin;
            state = TRANSACT;
        }
        else
            state = PINFAIL;
    }
    
    /** 
        Changes the state to WITHDRAW. 
        (Precondition: state is TRANSACT)
    */
    public void selectWithdraw() {
        assert state == TRANSACT;
        state = WITHDRAW;
    }
    
    /** 
        Withdraws amount from current account. 
        (Precondition: state is TRANSACT)
        @param value the amount to withdraw
    */
    public void withdraw(double value) {  
        assert state == TRANSACT;
        theBank.withdraw(accountNumber, accountPin, value);
        back();
    }
    
    /** 
        Changes the state to DEPOSIT. 
        (Precondition: state is TRANSACT)
    */
    public void selectDeposit() {
        assert state == TRANSACT;
        state = DEPOSIT;
    }
    
    /** 
        Deposits amount to current account. 
        (Precondition: state is TRANSACT)
        @param value the amount to deposit
    */
    public void deposit(double value) {  
        assert state == TRANSACT;
        theBank.deposit(accountNumber, accountPin, value);
        back();
    }

    /** 
        Gets the balance of the current account. 
        (Precondition: state is TRANSACT)
        @return the balance
    */
    public double getBalance() {  
        assert state == TRANSACT;
        return theBank.getBalance(accountNumber, accountPin);
    }

    /**
        Moves back to the initial state.
    */
    public void back() {
        if (state > TRANSACT)
            state = TRANSACT;
        else
            state = START;
    }

    /**
        Gets the current state of the ATM display.
        @return the current state
    */
    public int getState() {
        return state;
    }
}
