/*
 *  DPKnapsack class is a class that simulates
 *  the problem of a typical 0-1 knapsack 
 *  problem whose goal is to choose the
 *  optimal selection of items given their
 *  value and weights. Also, the items can only
 *  be selected once.
 *  
 *  @author: Sagar Poudel
 *  @version: 03-14-2017
 */

// Importing all the required classes
import java.util.Scanner;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;

public class DPKnapsack{

// Declaration of various data structures to hold the value, weight and the solution table for the 0-1 KnapSack problem
   private List<Integer> weights;
   private List<Integer> values;
   private List<String> itemNames;
   private int tableCapacity;
   private int[][] table;     // Two dimensional table that will hold the weight table values
      
/**
   * Creates a class constructor that 
   * takes file name as an argument
   * as well as the table capacity 
   * and helps add different values
   * and data onto their respective
   * arraylists.   
   
   @param itemFile the name of the text file that contains the data
   @param capacity the weight holding threshold for a given scenerio
*/
   public DPKnapsack(int capacity, String itemFile){
      try{
         // Taking input from a file
         Scanner scan = new Scanner(new File(itemFile));
         tableCapacity = capacity;
         weights = new ArrayList<>();
         values = new ArrayList<>();
         itemNames = new ArrayList<>();         
         
         while(scan.hasNext()){
            // Categorizing the data contained within the file
            String itemName = scan.next();
            int weight = scan.nextInt();
            int value = scan.nextInt();
           
           // Adding the individual data and values into their respective ArrayLists
            itemNames.add(itemName);
            weights.add(weight);
            values.add(value);
            
            scan.nextLine();
         }               
      }
      // Attempt to catch an FileNotFound exception
      catch(FileNotFoundException e)
      {
         System.out.println(itemFile+" not found!");
      }    
   }

/**
   * Given the name of the item and the
   * threshold of the items container, 
   * this method helps conclude weather
   * or not the item was included in the
   * set of optimal solution
   
   @param item the name of the item whose inclusiveness is to be checked
   @param maxCapacity the maximum weight that the container can hold     
   @return included, true if the item belongs to the optimal solution otherwise false
*/ 
   public boolean contains(String item, int maxCapacity){
      boolean included = false;
      // Utilizes a private method, itemsIncluded that returns the list of all included items from a optimal solution
      for(int i= 0; i<itemsIncluded(maxCapacity).size(); i++){
         // If a match is detected, it can be concluded that the searched for item is included
         if(itemNames.get(itemsIncluded(maxCapacity).get(i)).equals(item)){
            included = true;
         }
      }
      return included;
   }

/**
   * This method helps calculate the optimal weight
   * for a given 0-1 KnapSack problem, which is 
   * the total weight of the items included in 
   * the most efficient or optimal solution.  
   
   @return optimalWeight, the optimal weight for a given knapsack problem 
*/
   public int optimalWeight(){
      // Uses the private method optimalWeightCalculator
      return optimalWeightCalculator(tableCapacity);
   }
 
/**
   * This method returns the total number of items
   * that were included in the set of optimal
   * solution. 
   
   @return optimalNumber, the count of the items included in the optimal solution set 
*/
   public int optimalNumber(){
      /* Since, itemIncluded returns an ArrayList of included items, it's size gives the number
         of items included */
      return itemsIncluded(tableCapacity).size();
   }

/**
   * Given the name of the item, 
   * this method helps conclude weather
   * or not the item was included in the
   * set of optimal solution
   
   @param item the name of the item whose inclusiveness is to be checked
   @return true if the given items lies in the solution set and false otherwise
*/ 
   public boolean contains(String item){
      // Uses the other contains method with the maximum holding capacity of the container
      return contains(item,tableCapacity);  
   }

/**  
    * String representation and summary of the 
    * solution set that contains the items 
    * included, their weights, values, and the
    * total alongside proper formatting.
   
    @return summary, a brief account of the items included in the solution set
*/
   public String solution(){
      return solution(tableCapacity);
   }  
/**
   * Given a custom threshold for a container,
   * this method helps calculate the optimal weight
   * for a given 0-1 KnapSack problem, which is 
   * the total weight of the items included in 
   * the most efficient or optimal solution.  
   
   @param maxWeight a custom integer that is the most that the container can hold
   @return optimalWeight, the optimal weight given a custom holding capacity 
*/
   public int optimalWeight(int maxWeight){
      /* Use of the private method, optimalWeightCalculator that keeps track of 
         the number of items included in the optimal solution */
      return optimalWeightCalculator(maxWeight);
   }

/**
   * This method returns the total number of items
   * that were included in the set of optimal
   * solution for a given a new threshold for the
   * container. 
   
   @param maxWeight the maximum weight that the container can behold
   @return optimalNumber, the count of the items included in the optimal solution set 
*/
   public int optimalNumber(int maxWeight){
   /* Since, itemIncluded, a private method returns an ArrayList of included items,
      it's size gives the number of items included */
      return itemsIncluded(maxWeight).size();
   }

/**  
    * String representation and summary of the 
    * solution set that contains the items 
    * included, their weights, values, and the
    * total alongside proper formatting when 
    * provided with a custom holding capacity
    * for a items container.
   
    @param maxWeight the maximum weight that the container can behold
    @return summary, a brief account of the items included in the solution set
*/
   public String solution(int maxWeight){
      // Initializing the string that will be returned
      String solutionSummary = "";
      String header = String.format("\n%15s%15s%15s\n", "Item Name" , "Weight", "Value");
      String line = "-----------------------------------------------";
      String topic = String.format("%30s\n", "SOLUTION SET");

      int netWeight = 0;
      int netValue = 0;
      // Used for appropriate format of output
      for(int i = itemNames.size(); i >=1; i--){
         if(knapSolver(maxWeight)[i][maxWeight] != knapSolver(maxWeight)[i-1][maxWeight]){
         
            solutionSummary += String.format("%15s%15d%15d\n",itemNames.get(i-1), weights.get(i-1),values.get(i-1));
            /* Calculates the total sum of the weights and values of the included items */
            netWeight += weights.get(i-1);
            netValue += values.get(i-1);
            
            maxWeight -= weights.get(i-1);
         }
      }
      String footer = String.format("%15s%15d%15d", "Total", netWeight, netValue); 

      // Displaying the formatted string representation of the solution set
      return  topic + line + header + line + "\n"+ solutionSummary +line + "\n"+ footer  +"\n" +line ;
   }

/**
   * A private method developed to ease the process
   * of the solution to the knapsack problem. This 
   * method takes the threshold and distributes
   * the weights and the values of the items to
   * create a two-dimentional array/table.
   
   @param tableCapacity the threshold for the items container
   @return table, the weight table for the given problem
*/
   private int[][] knapSolver(int tableCapacity){
      // the number of values or items is the number of required columns
      int numberOfCols = values.size();
      // Creates a table
      table = new int[numberOfCols+1][tableCapacity+1];
      for (int item=0;item<=numberOfCols;item++){
         for (int weight=0;weight<=tableCapacity;weight++){
            // Filling up the zeros' column and row         
            if (item==0||weight==0){
               table[item][weight] = 0;
            }
            else if(weights.get(item-1) > weight){
               table[item][weight]=table[item-1][weight];       }
            else{ 
               int previousValue = table[item-1][weight];
               int newValue = values.get(item-1)+table[item-1][weight-weights.get(item-1)];
               table[item][weight]=Math.max(previousValue, newValue);
            }
         }
      }
      return table;               
   }
/**
   * Another private method that returns the list
   * of items that were included in the optimal solution set
      
   @param capacity the threshold for the items container
   @return includedItems, a list of items that were in the optimal solution set
*/
   private ArrayList<Integer> itemsIncluded(int capacity){
      ArrayList<Integer> includedItems = new ArrayList<Integer>();
      // Iterating through all the items
      for (int item = itemNames.size(); item>=1; item--){
         // Looking to see if a change has been registered
         boolean itemsIncluded = knapSolver(capacity)[item][capacity] != knapSolver(capacity)[item-1][capacity];
         if(itemsIncluded){
            // Includes the item onto the ArrayList
            includedItems.add(item-1);
            capacity -=  weights.get(item-1);
         }
      }
      return includedItems;
   }
/**
   * Private method that keeps track of the total
   * weight of the items that were in the optimal
   * solution.
         
   @param capacity the threshold for the items container
   @return optimalMeasure, the sum of all the items placed under optimal solution
*/
   private int optimalWeightCalculator(int capacity){
      int optimalMeasure =0;
      for (int a: itemsIncluded(capacity)){
         // adds up the weights of the included items retrieved from the method, itemsIncluded
         optimalMeasure+=weights.get(a);
      }
      return optimalMeasure;
   }

}















         if(loop == 1){
            if( promising( row,col,1 ) ){
               board[row][col] = 1;
               if(puzzleSolver( nextRow(row,col),nextCol(row,col)) )
                  return true;
               else
                  board[row][col] = 0;
            }
            else if( promising(row,col,3) ){
               board[row][col] = 3;
               if( puzzleSolver( nextRow(row,col),nextCol(row,col)) )
                  return true;
               else
                  board[row][col] = 0;
            }
            
         }
         
         else {
            if( promising(row,col,3) ){
               board[row][col] = 3;
               if( puzzleSolver( nextRow(row,col),nextCol(row,col)) )
                  return true;
               else
                  board[row][col] = 0;
               
            }
         }