package com.cmsy265;

import java.math.BigDecimal;    // So we don't get floating-point errors when doing basic money math.

/**
 * @author Sam Young
 * @description Interface implemented by all classes (including the driver). (Only contains program constants, no methods!)
 * @version 1.2
 * @since 2024-10-09
 */
public interface Constants {

    /**
     * Enums for menu options
     */
    enum MainMenuOption {
        STOCK_SHELVES(1),
        FILL_WEB_ORDER(2),
        RESTOCK_RETURN(3),
        RESTOCK_INVENTORY(4),
        CUSTOMER_UPDATE(5),
        CUSTOMER_PURCHASE(6),
        CUSTOMER_CHECKOUT(7),
        DISPLAY_INVENTORY(8),
        END_PROGRAM(9);
        //Boilerplate, so we can use these values as ints the way the assignment wants us to.
        private final int value;
        private MainMenuOption(int value) {this.value = value;}
        public int getValue() {return value;}
    }

    enum CustomerUpdateSubmenuOption {
        ADD_CUSTOMER(1),
        DELETE_CUSTOMER(2),
        CHANGE_CUSTOMER_NAME(3),
        SAVE_CHANGES(4),
        DISPLAY_CUSTOMER_LIST(5),
        RETURN_TO_MAIN_MENU(6);
        //Boilerplate, so we can use these values as ints the way the assignment wants us to.
        private final int value;
        private CustomerUpdateSubmenuOption(int value) {this.value = value;}
        public int getValue() {return value;}
    }

    /**
     * How many TVs to push() or pop() when using menu options
     * 1 or 4, respectively.
     */
    final int STOCK_AMT   = 5;
    final int RESTOCK_AMT = 5;

    /**
     * File names (relative to classpath root):
     *   * List of TV IDs (plaintext).
     *   * List of customers (plaintext).
     */
    final String stackFile  = "txt/stack.txt";
    final String custFile   = "txt/CustFile.txt";

    /**
     * Constants for calculating cost.
     */
    final int        CENTS_DIGITS = 2;
    final BigDecimal TV_COST      = new BigDecimal("199.95");
    final BigDecimal SALES_TAX    = new BigDecimal("0.06");

    /**
     * Constants for validating ints (acctNum and tvsToBuy).
     */
    final int MIN_TVSTOBUY = 1;
    final int MIN_ACCT_NUM = 1;
    final int MAX_ACCT_NUM = 999999;

}
