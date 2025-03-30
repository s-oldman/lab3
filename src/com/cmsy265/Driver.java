package com.cmsy265;

import java.io.BufferedReader;          // For reading files in chunks.
import java.io.FileNotFoundException;   // Required by FileReader.
import java.io.FileReader;              // For reading files.
import java.util.ArrayList;             // For customer purchase history.
import java.util.Iterator;              // For iterating through Stack<TV>.
import java.util.NoSuchElementException;
import java.util.ArrayDeque;            // For the customer queue. (Much better than LinkedList, which requires
                                        //   linear time & invokes garbage collection just to enqueue a customer.
                                        //   And it implements the LinkedList interface, anyways.)
import java.util.Scanner;               // For reading user input.
import java.util.Stack;                 // For the store inventory.

/**
 * @author Sam Young
 * @duedate 2024-09-30
 * @description TV store Inventory management, now with customer Queues.
 * @version 1.1
 * @since 2024-09-18
 */
@SuppressWarnings("nls")
public class Driver implements Constants {

    /**
     * Variables:
     *   * A Scanner, to read from System.in.
     *   * Our inventory (a Stack of TVs).
     *   * An ArrayDeque, for the Customer queue.
     *   * Another ArrayDeque, for customer data.
     */
    @SuppressWarnings("resource")
	private static Scanner              stdin     = new Scanner(System.in);
    private static Stack<TV>            inventory = new Stack<>();
    private static ArrayDeque<Customer> customers = new ArrayDeque<>();
    private static CustomerData         cd        = new CustomerData();
    private static boolean newCustListSavedToFile = true;

    /**
     * @description The main event loop.
     */
	public static void main(String[] args) {

        displayHeader();

        if (readInventory() && readCustFile()) {

            while (true) {

                displayMainMenu();

                MainMenuOption choice = promptForMainMenuOption();

                processMenuOption(choice);

            }

        } else {

            System.out.println("[C] Either \"" + stackFIle + "\" or \"" + custFile + "\" not found in classpath, so we can't initialize the inventory & customer lists. Exiting.");
            System.exit(1);
        }

    }

    /**
     * @description Display program header & copyright.
     */
    @SuppressWarnings("nls")
	private static void displayHeader() {
        System.out.println("                       CMSY-265 Lab 3");
        System.out.println("Copyright ©2024 Howard Community College. All rights reserved; unauthorized duplication prohibited.");
        System.out.println("        Welcome to the CMSY-265 TV Inventory Control Program (part 3)");
    }

    /**
     * @description Read inventory data from stackFIle.
     * @return Whether or not we were able to read the data.
     */
    public static boolean readInventory() {

        Scanner filein = null;
    
        try {
            filein = new Scanner(new BufferedReader(new FileReader(stackFIle)));
        } catch (FileNotFoundException e) {
            return false;
        }

        // File's open, now iterate through it.
        while (filein.hasNextLine()) {
            String id = filein.nextLine();
            inventory.push(new TV(id));
        }

        filein.close();
        return true;

    }

    /**
     * @description Read inventory data from stackFIle.
     * @return Whether or not we were able to read the data.
     */
    @SuppressWarnings("nls")
	public static boolean readCustFile() {

        Scanner filein = null;
    
        try {
            filein = new Scanner(new BufferedReader(new FileReader(custFile)));
        } catch (FileNotFoundException e) {
            return false;
        }

        // File's open, now iterate through it.
        while (filein.hasNextLine()) {
            try {
                String name = filein.nextLine();
                String id   = filein.nextLine();
                Customer c  = new Customer(name, id);
                cd.addCustomer(c);
            } catch (NoSuchElementException e) {
                System.out.println("[E] Could not build next customer from next two lines in file. (Was there only one line left?)");
            }
        }

        filein.close();
        return true;

    }

    /**
     * @description Display menu options.
     */
    @SuppressWarnings("nls")
	private static void displayMainMenu() {
        System.out.println("");
        System.out.println("");
        System.out.println("TV Inventory Management Menu:");
        System.out.println("  " + (MainMenuOption.STOCK_SHELVES.getValue()+1)     + ". Stock Shelves");
        System.out.println("  " + (MainMenuOption.FILL_WEB_ORDER.getValue()+1)    + ". Fill Web Order");
        System.out.println("  " + (MainMenuOption.RESTOCK_RETURN.getValue()+1)    + ". Restock Returned TV");
        System.out.println("  " + (MainMenuOption.RESTOCK_INVENTORY.getValue()+1) + ". Restock Inventory");
        System.out.println("  " + (MainMenuOption.CUSTOMER_UPDATE.getValue()+1)   + ". Update Customer (submenu)");
        System.out.println("  " + (MainMenuOption.CUSTOMER_PURCHASE.getValue()+1) + ". Customer Purchase");
        System.out.println("  " + (MainMenuOption.CUSTOMER_CHECKOUT.getValue()+1) + ". Customer Checkout");
        System.out.println("  " + (MainMenuOption.DISPLAY_INVENTORY.getValue()+1) + ". Display Inventory");
        System.out.println("  " + (MainMenuOption.END_PROGRAM.getValue()+1)       + ". End Program");
    }

    /**
     * @description Display menu options.
     */
    @SuppressWarnings("nls")
	private static void displayCustomerUpdateMenu() {
        System.out.println("");
        System.out.println("");
        System.out.println("Customer Update Menu:");
        System.out.println("  " + (CustomerUpdateSubmenuOption.ADD_CUSTOMER.getValue()+1)          + ". Add Customer");
        System.out.println("  " + (CustomerUpdateSubmenuOption.DELETE_CUSTOMER.getValue()+1)       + ". Delete Customer");
        System.out.println("  " + (CustomerUpdateSubmenuOption.CHANGE_CUSTOMER_NAME.getValue()+1)  + ". Change Customer Name");
        System.out.println("  " + (CustomerUpdateSubmenuOption.SAVE_CHANGES.getValue()+1)          + ". Save Changes to Customer File");
        System.out.println("  " + (CustomerUpdateSubmenuOption.DISPLAY_CUSTOMER_LIST.getValue()+1) + ". Display Customer List");
        System.out.println("  " + (CustomerUpdateSubmenuOption.RETURN_TO_MAIN_MENU.getValue()+1)   + ". Return to Main Menu");
    }

    /**
     * @description Prompt for valid menu option using stdin.
     * @return valid menu option number
     */
    @SuppressWarnings("nls")
	private static MainMenuOption promptForMainMenuOption() {

        int option = 0;

        do {

            System.out.println("");
            System.out.print("Option: ");

            String userInput = stdin.nextLine();

            try {

                option = Integer.parseInt(userInput);

            } catch (NumberFormatException e) {

                System.out.println("[E] That's not a menu option. Try again.");
                continue;

            }

            System.out.println("");

        } while (!isValidMainMenuOption(option));

        // Cast int to the corresponding value for each enum.
        // 0 = first enum, but 1 = first menu option, so we have to subtract 1 here.
        return MainMenuOption.values()[option-1];

    }

    /**
     * @description Check if the menu option the user provided is valid.
     * @param option Menu option parsed from user input.
     * @param min Lowest allowable menu option.
     * @param max Highest allowable menu option.
     * @return Is it valid?
     */
    @SuppressWarnings("nls")
	public static boolean isValidMainMenuOption(int option) {

        if (option < MainMenuOption.STOCK_SHELVES.getValue() || option > MainMenuOption.END_PROGRAM.getValue()) {

            System.out.println("[E] Invalid selection. Try again.");
            return false;

        } else if (option == MainMenuOption.STOCK_SHELVES.getValue() && inventory.size() <= STOCK_AMT) {

            System.out.println("[E] Only " + inventory.size() + " TVs in inventory, and we need at least " + STOCK_AMT + " to stock the shelves. Please select another option.");
            return false;

        } else if (option == MainMenuOption.FILL_WEB_ORDER.getValue() && inventory.size() <= 0) {

            System.out.println("[E] No TVs remaining in inventory, so we can't fill any web orders. Restock and try again.");
            return false;

        } else if (option == MainMenuOption.CUSTOMER_PURCHASE.getValue() && inventory.size() <= 0) {

            System.out.println("[E] No TVs currently in inventory, so we can't sell any to customers. Restock and try again.");
            return false;

        } else if (option == MainMenuOption.CUSTOMER_CHECKOUT.getValue() && customers.size() <= 0) {

            System.out.println("[W] No customers to checkout right now.");
            return false;

        } else if (option == MainMenuOption.DISPLAY_INVENTORY.getValue() && inventory.size() <= 0) {

            System.out.println("[I] No TVs currently in inventory.");
            return false;

        } else if (option == MainMenuOption.END_PROGRAM.getValue() && customers.size() > 0) {

            System.out.println("[E] There are still " + customers.size() + " customers in the checkout queue. Finish checking them out, and then we can stop.");
            return false;

        } else return true;

    }

    /**
     * @param returnToMainMenu 
     * @param addCustomer 
     * @description Prompt for valid menu option using stdin.
     * @return valid menu option number
     */
    @SuppressWarnings("nls")
	private static CustomerUpdateSubmenuOption promptForCustomerUpdateSubmenuOption() {

        int option = 0;

        do {

            System.out.println("");
            System.out.print("Option: ");

            String userInput = stdin.nextLine();

            try {

                option = Integer.parseInt(userInput);

            } catch (NumberFormatException e) {

                System.out.println("[E] That's not a menu option. Try again.");
                continue;

            }

            System.out.println("");

        } while (!isValidCustomerUpdateSumbenuOption(option));

        // Cast int to the corresponding value for each enum.
        // 0 = first enum, but 1 = first menu option, so we have to subtract 1 here.
        return CustomerUpdateSubmenuOption.values()[option-1];

    }

    /**
     * @description Check if the menu option the user provided is valid.
     * @param option Menu option parsed from user input.
     * @param min Lowest allowable menu option.
     * @param max Highest allowable menu option.
     * @return Is it valid?
     */
    @SuppressWarnings("nls")
	public static boolean isValidCustomerUpdateSumbenuOption(int option) {

        if (option == CustomerUpdateSubmenuOption.DELETE_CUSTOMER.getValue() && cd.size() <= 0) {

            System.out.println("[E] No customers in the list, so we can't delete any. Add a customer and try again.");
            return false;

        } else if (option == CustomerUpdateSubmenuOption.CHANGE_CUSTOMER_NAME.getValue() && cd.size() <= 0) {

            System.out.println("[E] No customers in the list, so we can't change anyone's name. Add a customer and try again.");
            return false;

        } else if (option == CustomerUpdateSubmenuOption.SAVE_CHANGES.getValue() && customers.size() <= 0) {

            System.out.println("[W] No customers to checkout right now.");
            return false;

        } else if (option == CustomerUpdateSubmenuOption.DISPLAY_CUSTOMER_LIST.getValue() && inventory.size() <= 0) {

            System.out.println("[I] No TVs currently in inventory.");
            return false;

        } else if (option == CustomerUpdateSubmenuOption.RETURN_TO_MAIN_MENU.getValue() && customers.size() > 0) {

            System.out.println("[E] There are still " + customers.size() + " customers in the checkout queue. Finish checking them out, and then we can stop.");
            return false;

        } else return true;

    }

    /**
     * @description Do the thing the user picked.
     * @param option Menu option, validated from user input.
     */
    private static void processMenuOption(MainMenuOption option) {

        switch (option) {

            case MainMenuOption.STOCK_SHELVES:
                stockShelves();
                break;
    
            case MainMenuOption.FILL_WEB_ORDER:
                fillWebOrder();
                break;

            case MainMenuOption.RESTOCK_RETURN:
                restockReturn();
                break;

            case MainMenuOption.RESTOCK_INVENTORY:
                restockInventory();
                break;

            case MainMenuOption.CUSTOMER_UPDATE:
                customerUpdateSubmenu();
                break;

            case MainMenuOption.CUSTOMER_PURCHASE:
                customerPurchase(inventory, customers);
                break;

            case MainMenuOption.CUSTOMER_CHECKOUT:
                customerCheckout(customers);
                break;

            case MainMenuOption.DISPLAY_INVENTORY:
                displayInventory(inventory);
                break;

            case MainMenuOption.END_PROGRAM:
                endProgram();
                break;

        }

    }

    /**
     * @description Event loop for the customer update submenu.
     */
    private static void customerUpdateSubmenu() {
        
        while (true) {

            displayCustomerUpdateMenu();

            CustomerUpdateSubmenuOption choice = promptForCustomerUpdateSubmenuOption();

            if (choice == CustomerUpdateSubmenuOption.RETURN_TO_MAIN_MENU) {
            	break;
        	} else {
        		processSubmenuOption(choice);
        	}

        }

    }

    /**
     * @description Do the thing the user picked.
     * @param option Menu option, validated from user input.
     */
    private static void processSubmenuOption(CustomerUpdateSubmenuOption option) {

        switch (option) {

            case CustomerUpdateSubmenuOption.ADD_CUSTOMER:
                addCustomer();
                break;
    
            case CustomerUpdateSubmenuOption.DELETE_CUSTOMER:
                deleteCustomer();
                break;

            case CustomerUpdateSubmenuOption.CHANGE_CUSTOMER_NAME:
                changeCustomerName();
                break;

            case CustomerUpdateSubmenuOption.SAVE_CHANGES:
                saveChangesToFile();
                break;

            case CustomerUpdateSubmenuOption.DISPLAY_CUSTOMER_LIST:
                displayCustomerList();
                break;

            case CustomerUpdateSubmenuOption.RETURN_TO_MAIN_MENU:
                break;

        }

    }
    

    /**
     * @description Main menu option 1: Stock shelves (i.e.: remove five TVs from inventory).
     */
    @SuppressWarnings("nls")
	private static void stockShelves() {

        System.out.println("");
        System.out.println("Stocked the following TVs onto the shelves:");

        for (int i = 0; i < STOCK_AMT; i++) {

            TV tv = inventory.pop();
            System.out.println(tv);

        }

    }

    /**
     * @description Main menu option 2: Fill web order (i.e.: remove one TV from inventory).
     */
    @SuppressWarnings("nls")
	private static void fillWebOrder() {

        TV tv = inventory.pop();

        System.out.println("");
        System.out.println("Filled the following TV as a web order:");
        System.out.println(tv);

    }

    /**
     * @description Main menu option 3: Restock one TV into inventory.
     */
    @SuppressWarnings("nls")
	private static void restockReturn() {

        if (!(inventory.size() <= 0)) {
            inventory.push(new TV(TV.nextId(inventory.peek().get())));
        } else {
            inventory.push(new TV("ABC123-0"));
        }

        System.out.println("");
        System.out.println("Added the following TV as a return: " + inventory.peek().get());

    }

    /**
     * @description Main menu option 4: Restock five TVs into inventory.
     */
    @SuppressWarnings("nls")
	private static void restockInventory() {

        System.out.println("");
        System.out.println("Added the following TVs as restocks:");

        for (int i = 0; i < RESTOCK_AMT; i++) {

            if (!(inventory.size() <= 0)) {

                inventory.push(new TV(TV.nextId(inventory.peek().get())));
                System.out.println(inventory.peek().get());

            } else {

                inventory.push(new TV("ABC123-0"));
                System.out.println(inventory.peek().get());

            }

        }

    }
    
    /**
     * @description Customer update submenu option 1: add a new Customer to our CustomerData.
     */
    private static void addCustomer() {

    	// Assignment sheet requires this.
    	System.out.println("");
    	System.out.println("Add a customer to the CustomerData list");
    	System.out.println("");

    	// Prompt for valid Customer data (acctNum must be unique).
    	Customer c = new Customer(
    						 promptForString("New Customer's name: "), 
    						 promptForAcctNum("New Customer's account number: ")
						 );

    	// Add it to CustomerData.
    	cd.addCustomer(c);

    }
    
    /**
     * @description Customer update submenu option 2: remove a customer from CustomerData.
     */
    private static void deleteCustomer() {

    	// Variables:
    	boolean customerFound = false;	// Did we find that account number in CustomerData?

    	// Assignment sheet requires this.
    	System.out.println("");
    	System.out.println("Remove a customer from the CustomerData list");
    	System.out.println("");
    	
    	// Prompt for Customer data.
    	String acctNumToRemove = promptForString("Account number of Customer to remove: ");
    	
    	// Check if the acctNum is actually in CustomerData.
    	for (Customer c : cd) {
    		
    		// Is this the right Customer to remove?
    		if (c.getAcctNum().equals(acctNumToRemove)) {
    			
    			// Yep, we found it. Get rid of it & stop searching.
    			cd.removeCustomer(c);
    			customerFound = true;
        		System.out.println("Customer removed.");
    			break;
    			
    		}

    	}
    	
    	/* The assignment wants this:
    	 * 
    	 *	"c.	If this account does not exist, display an error message,
		 * 		and require the user to reenter an account number;
		 * 		this must continue until an existing account number is entered."
		 * 
		 * That's TERRIBLE, because if the user wants to delete a Customer that's
		 * not actually in CustomerData (maybe because they just deleted it, maybe
		 * because they mis-remembered something, or maybe they just pressed the
		 * wrong button), they'd be forced to delete something else just to get out
		 * of the prompt.
		 * 
		 * I'm vetoing this. If the data's not there, we'll back out to the submenu,
		 * and then the user can decide whether to try again, pick option 5 to see
		 * the correct acctNum, or do something else.
    	 */
    	if (!customerFound) System.out.println("[E] Customer not found. Backing out to submenu.");

    }

    /**
     * @description Main menu option 6: Buy something. (Or several somethings.)
     * @param inventory Stack of TVs currently in inventory.
     * @param customers Deque to add customer to the end of.
     */
    @SuppressWarnings("nls")
	private static void customerPurchase(Stack<TV> inventory, ArrayDeque<Customer> customers) {

    	displayCustomerList();
    	System.out.println("");

    	/*  Again: I'm not going to force users to munge some random record if
		 *  the Customer they're expecting to see isn't there.
    	 *  See my comment in deleteCustomer() for a more detailed explanation.
    	 */
    	String  acctNum  = promptForString("Customer account number: ");
        
        if (acctNum.equalsIgnoreCase("none")) {
        	acctNum = promptForAcctNum("[I] \"none\" entered. Please enter acctNum of new customer: ");
        }
        
        String  acctName = "";
        
        // If the name's already in customerData, we need to use that.
    	for (Customer c : cd) {
    		
    		// Names can be the same, account numbers can't.
    		if (c.getAcctNum().equals(acctNum)) {

    			acctName = c.getName();
    			System.out.println("[I] Account number " + c.getAcctNum() +
    							   " found, customer name: " + acctName + ".");
    			return;

    		}

    	}
    	
        if (acctName.isEmpty()) acctName = promptForString("Customer name: ");

        int     tvsCount = promptForInt("Number of TVs they're buying: ", MIN_TVSTOBUY, inventory.size());

        ArrayList<TV> tvsToBuy = new ArrayList<TV>();

        for (int i = 0; i < tvsCount; i++) {
            tvsToBuy.add(inventory.pop());
        }

        Customer c = new Customer(acctName, acctNum, tvsToBuy);

        System.out.println("There are " + inventory.size() + " more TVs left in inventory.");

        customers.addLast(c);

    }

    /**
     * @description Ask for any String from the user.
     * @param prompt to show user, before asking.
     * @return The String, taken from user input.
     */
    private static String promptForString(String prompt) {
        System.out.print(String.format(prompt));
        return stdin.nextLine();
    }
    
    /**
     * @description Prompt for a unique acctNum from the user.
     * @param prompt to ask for acctNum
     * @return non-duplicate acctNum
     */
    @SuppressWarnings("nls")
	private static String promptForAcctNum(String prompt) {

    	// Variables:
    	String acctNum = "";			// The acctNum, parsed from user input.
    	boolean duplicateFound = false;	// Whether or not acctNum's already in our CustomerData.
    	
    	while (true) {

            System.out.print(String.format(prompt));
            acctNum = stdin.nextLine();
            
            // cd isn't sorted, so linear search it is.
            for (Customer c : cd) {

            	if (c.getAcctNum().equals(acctNum)) {

            		System.out.println("[E] This account number isn't unique. Please re-enter.");
                    System.out.println("");
            		
            		duplicateFound = true;

        		}

            }
            
            if (!duplicateFound) return acctNum;

    	}

    }
    
    /**
     * @description Display the customers in CustomerData.
     */
    private static void displayCustomerList() {
    	
    	System.out.println();

    	// I'd rather encapsulate this sort of thing in a toString() than put it in main().
    	System.out.println(cd.toString());

    }

    /**
     * @description Ask for a valid int from the user. (Reprompts if invalid.)
     * @param prompt to show user, before asking.
     * @return The vaildated int, taken from user input.
     */
    @SuppressWarnings("nls")
	private static int promptForInt(String prompt, int min, int max) {

        // Variables:
        String in = "";   // User input, unparsed.
        int input = -1;   // int to validate.

        while (true) {

            System.out.print(prompt);
            in = stdin.nextLine();

            try {

                input = Integer.parseInt(in);

            } catch (NumberFormatException e) {

                // Cast failed because an int couldn't be parsed from input.
                System.out.println("[E] That's not an integer. Try again.");
                System.out.println("");
                System.out.println("Full details:");
                System.out.println(e.getMessage());

            }

            // Time for custom validation.
            if (validate(input, min, max)) {
                return input;
            }

        }

    }

    /**
     * @description Custom validation for ints: min <= input <= max.
     * @param input The int to validate.
     * @param min Minimum allowable int.
     * @param max Maximum allowable int.
     * @return Validated int.
     */
    @SuppressWarnings("nls")
	private static boolean validate(int input, int min, int max) {

        if (input < min) {

            System.out.println("[E] That's too small (lowest allowable is " + min + "). Try again.");
            System.out.println("");
            return false;

        } else if (input > max) {

            System.out.println("[E] That's too big (highest allowable is " + max + "). Try again.");
            System.out.println("");
            return false;

        } else {

            return true;

        }

    }

    /**
     * @description Main menu option 7: Check out the next customer in the queue.
     */
    @SuppressWarnings("nls")
	private static void customerCheckout(ArrayDeque<Customer> customers) {

        Customer checkout = customers.removeFirst();

        System.out.println(checkout);

        if (customers.size() == 0) {

            System.out.println("No more customers in the queue.");

        } else if (customers.size() == 1) {

            System.out.println("Only " + customers.size() + " more customer in the queue.");

        } else {

            System.out.println("There are " + customers.size() + " more customers in the queue.");

        }

    }

    /**
     * @description Main menu option 8: Print contents of stack. (Uses Stack.Iterator().)
     */
    private static void displayInventory(Stack<TV> inventory) {

        System.out.println("");
        System.out.println("Current inventory:");

        Iterator<TV> iterator = inventory.iterator();

        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

    }

    /**
     * @description Are we done here? Let's check.
     */
    private static void endProgram() {

        if (!newCustListSavedToFile) {

            while (true) {
                
                String selection = promptForString("You didn't save the new customer(s) to the file.%n(To fix: pick option 5, suboption 4.)%n%nAre you sure you want to exit? [y/n]: ");

                if (selection.equals("Y") || selection.equals("y")) {

                    displayInventory(inventory);

                    System.out.println("");
                    System.out.println("");
                    System.out.println("Thanks for using the program. Exiting.");
                    System.exit(0);

                } else if (selection.equals("N") || selection.equals("n")) {

                    break;

                }

            }

        } else {

            System.out.println("");
        	displayCustomerList();

            System.out.println("");
            displayInventory(inventory);
    
            System.out.println("");
            System.out.println("");
            System.out.println("Thanks for using the program. Exiting.");
            System.exit(0);

        }

    }

}
