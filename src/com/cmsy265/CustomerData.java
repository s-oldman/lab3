package com.cmsy265;

import java.io.Serializable;    // Directly required by assignment.
import java.util.Iterator;      // For iterating over customerData.
import java.util.ArrayDeque;    // For the customer queue. (Much better than LinkedList, which requires
                                //   linear time & invokes garbage collection just to enqueue a customer.
                                //   And it implements the LinkedList interface, anyways.)

/**
 * @author Sam Young
 * @description An ArrayDeque of Customers we can iterate on.
 * @version 1.0
 * @since 2024-10-09
 */

public class CustomerData implements Serializable, Iterable<Customer> {

	/**
     * @description Just one private variable: the ArrayDeque<Customer>.
     */
    private ArrayDeque<Customer> customerData;

    /**
     * @description Default (and only) constructor: just instantiate the ArrayDeque<>.
     */
    public CustomerData() {
       this.customerData = new ArrayDeque<Customer>(); 
    }

    /**
     * @description Iterator over customerData.
     * @return The iterator.
     */
    @Override
    public Iterator<Customer> iterator() {
        return customerData.iterator();
    }

    /**
     * @description Add customer to list.
     * @param Customer to add.
     */
    public void addCustomer(Customer c) {
        customerData.add(c);
    }

    /**
     * @description Remove customer from list.
     * @param Customer to remove.
     */
    public void removeCustomer(Customer c) {
        customerData.remove(c);
    }

    /**
     * @description Update customer name, given acctNum.
     * @param String acctNum of Customer to update.
     */
    public void updateCustomerName(String acctNum, String newName) {
        for (Customer c : customerData) {
            if (c.getAcctNum().equals(acctNum)) {
                c.setName(newName);
                break;  // If there's multiple possible records to update, we only want to touch the first match.
            }
        }
    }

    /**
     * @description Find customer, given acctNum.
     * @param String acctNum of customer to find.
     * @return Customer found.
     */
    public Customer findCustomer(String acctNum) {
        
        for (Customer c : customerData) {
            if (c.getAcctNum().equals(acctNum)) return c;
        }

        // If we get here, we didn't find a customer by that name. So we need to return an empty object with a special name.
        //   (Why? Because the assignment says this function MUST ALWAYS return a Customer.)
        return new Customer("customerNotFound", "-1");

    }

    /**
     * @description Return size of customerData.
     * @return int containing number of Customer entries in customerData.
     */
    public int size() {
        return customerData.size();
    }

    /**
     * @description Print formatted customer list. (Breaking out the formatting into a separate method b/c I want this class to have a toString() method.)
     */
    public void displayCustomerList() {
        System.out.print(this.toString());
    }

    /**
     * @description Formatted list of customers in customerData.
     * @return String containing (multi-line) formatted list.
     */
    public String toString() {

        String list = "";

        // Assuming that spaces here in the header & in delimiters are fine.
        // (I gather that the "no spaces" requirement is mainly about making sure we use fstring padding.)
        list += String.format("Customer List:%n");
        list += String.format("--------------%n");

        int custNum = 1;
        for (Customer c : customerData) {
            list += "Customer" + String.format("%1$3d", custNum);
            list += ": " + String.format("%1$20s", c.getName());
            list += ", Account Number:" + String.format("%1$10s", c.getAcctNum());
            list += String.format("%n");
            custNum++;
        }

        return list;

    }

}
