/*
 *  ThreeInaRow class is a class that simulates
 *  the mathematical game or a problem
 *  in which the same entity cannot be repeated thrice
 *  in either rows or columns. Also, the number of
 *  unique objects in rows and columns should be
 *  equal. Here, the objects are colors represented by
 *  "B" for blue and "W" for white. 
 *
 *  @author: Sagar Poudel
 *  @version: 04-14-2017
 */

// Importing all the required classes
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class ThreeInaRow {
   // Declaring various data structures that will hold the game board and it's dimension
   int[][] board;
   String filename;
   int dimension;
   boolean solveNow;
/**
   * A constructor that helps initialize the data taken from the input file
   *
   * @param filename the name of the file where the data lies
   * @param N the dimention of the provided problem
*/
 
   public ThreeInaRow(String filename, int N){
      this.filename = filename;
      dimension = N;
      // Creates a two-dimentional aray of size of N
      board = new int[N][N];
      try{
         Scanner lineScan = new Scanner(new File(filename));
         for(int i = 0; i < N; i++){
            String line = lineScan.nextLine();
            for(int j=0; j < line.length(); j++){
               String word = line.substring(j,j+1);
               // Fills in the grid with values
               if(word.equals("B"))
                  board[i][j] = 1; // 1 represents the blue color
               else if(word.equals("W"))
                  board[i][j]= 2;  // 2 represents the white color
               else
                  board[i][j]= 0;  // 0 represents the space present
            }
         }
         solveNow = puzzleSolver(0,0);
      }
      /* Tries to catch a FileNotFoundException */
      catch(FileNotFoundException e){
         System.out.println("File not Found!");
      }
   }
 
/**
   * A private method that takes a row number, column number, 
   * and a color as input and determines weather or not
   * that particular color can be included on that row 
   * and column
   *
   * @param row the row number of the grid
   * @param col the column number of the grid
   * @param target the desired color to be inserted
   * @return true if the condition is feasible and false otherwise
*/     
   private boolean promising(int row, int col, int target){
      /* Checking the upper portion of the grid */
      if( row > 1 ){
         if( target == board[row-1][col] && target == board[row-2][col] )
            return false;}
      
      /* Checking the lower portion of the grid */
      if( row < dimension-2 ){
         if( target == board[row+1][col] && target == board[row+2][col] )
            return false;}
      
      /* Checking the left side of the grid */
      if( col > 1 ){
         if( target == board[row][col-1] && target == board[row][col-2] )
            return false;}
      
      /* Checking on the right side of the grid */
      if(col < dimension-2 ){
         if( target == board[row][col+1] && target == board[row][col+2])
            return false;}
      
      if(row > 0 && row < dimension-1 ){
         if(target == board[row-1][col] && target == board[row+1][col] )
            return false;}
      
      if(col > 0 && col < dimension-1 ){
         if( target == board[row][col-1] && target == board[row][col+1] )
            return false;}
   
      int numberOfRows = 0;
      int numberOfCols = 0;
      
      /* Making sure the equal distribution of colors over a column */ 
      for(int i = 0; i < dimension; i++){
         if(board[i][col] == target){
            numberOfRows++;}
      }
      if(numberOfRows >= (dimension/2))
         return false;
      
      /* Making sure the equal distribution of the colors over a row */ 
      for(int i = 0; i < dimension; i++){
         if(board[row][i] == target){
            numberOfCols++;}
      }
      if(numberOfCols >= (dimension/2))
         return false;
   
      return true;
   
   }   

/**
   * A method that returns the string representation of
   * the solution to an instance of the problem seperated
   * by a pipe( | ) per row
   *
   * @return the formatted rows of the solution set
*/  
   public String solution(){
      StringBuilder sb = new StringBuilder();
      if(solveNow){
         
         for(int i = 0; i < dimension; i++){
            if(i==0){
            /* Seperates the output by row of the grid */
               sb.append("|");
            }
            for(int j = 0; j < dimension; j++){
               if(board[i][j] == 1)
                  sb.append("B");
               else if(board[i][j]== 2)
                  sb.append("W");
            
            }
            sb.append("|");
         }
      }
      else{
         return "NONE";
      }
      return sb.toString();
   }
 
/** 
   * Method that returns the formatted grid after the
   * algorith is run and the solution has been found
   *
   * return the solution grid after backtracking completes
*/   
   public String toString(){
      StringBuilder builder = new StringBuilder();
      for(int i = 0; i < dimension; i++){
         for(int j = 0; j< dimension;j++){
            if(board[i][j] == 1)
               builder.append("B ");
            else if(board[i][j] == 2)
               builder.append("W ");
            else
               builder.append(" ");
         }
         builder.append("\n");
      } 
      return builder.toString();
   }
   
/**
   * Method that takes the row number and the column number
   * as input and solves the problem using the backtracking
   * technique. If a node is found to be promising, it is furthur 
   * explored. 
   *
   * @param row the row number of the grid
   * @param col the column number of the grid
   * @return true if the node is to be explored and false otherwise

*/
   public boolean puzzleSolver(int row, int col){
      int counter = 0;
      if(col >= dimension)
         return true;
   
      if( board[row][col] != 0)
         return puzzleSolver( nextRow(row,col), nextCol(row,col) );
      for(int loop = 1; loop < 3 ; loop++){
         if(loop == 1){
            if(promising(row,col,1)){
               board[row][col] = 1;
               if( puzzleSolver(nextRow(row,col),nextCol(row,col))){
                  return true;
               }
               board[row][col] = 0;
            }
            
            else if(promising (row,col,2) ){
               board[row][col] = 2;
               if(puzzleSolver( nextRow(row,col),nextCol(row,col))){
                  return true;
               }
               board[row][col] = 0;
            }
         }
         else {
            if(promising(row,col,2)){
               board[row][col] = 2;
               if(puzzleSolver(nextRow(row,col),nextCol(row,col))){
                  return true;
               }
               board[row][col] = 0;
            }
         }
      }
      return false;
   }

/**
   * A method that returns the next element to be taken into consideration
   *
   * @param row the row number of the grid
   * @param col the column number of the grid
   * @return the next element's column number
*/   
   public int nextCol(int row, int col) {
      if(row < dimension - 1)
         return col;
      else
         return col+1;
   }
   
/**
   * A method that returns the next element to be taken into consideration
   *
   * @param row the row number of the grid
   * @param col the column number of the grid
   * @return the next element's row number
*/   
   public int nextRow(int row,int col){
      if(row < dimension - 1)
         return row+1;
      else
         return 0;
   } 
} 
   
   
