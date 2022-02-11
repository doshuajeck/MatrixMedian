/******************************************************************************************************
* Joshua Deck                                                                                         *
* CS-203                                                                                              *
* Summer 2020                                                                                         *
* Program to compute the median of an input Matrix using Mergesort and Binary Search.                 *
*                                                                                                     *
* Driver class: manages calls to the two algorithms and instantiates the matrix.                      *
******************************************************************************************************/

import java.util.*;
import java.io.*;

public class Driver
{
   private static int[][] matrix; //The matrix to get from the file
   
   //Counters for base operations
   private static int mergeCount = 0;
   private static int binaryCount = 0;
   
   
   
   /******************************************************************************************************
   * main(args)                                                                                          *
   *                                                                                                     *
   * Purpose: Manages the running of the program and calls to other methods                              *
   * Returns: void                                                                                       *
   * Parameters:                                                                                         *
   *        String [] args - arguments input from the command line; the name of the file to read from    *
   ******************************************************************************************************/    
   public static void main(String [] args)
   {
      if (initialize(args))
      {
         //Set up median value
         double median = computeMedian(convertToArray(matrix));
         
         //Merge sort               
         mergeSort(matrix);
         System.out.println("Number of comparisons: " + mergeCount + "\n");
         
         //Binary Search          
         int [] medians = new int[matrix.length];
         for (int row = 0; row < matrix.length; row++)
         {
            medians[row] = binarySearch(matrix[row], 0, matrix[0].length - 1);
         }
         
         //Find the closest median from the list of medians
         int closestMedian = Integer.MAX_VALUE;
         for (int val : medians)
         {
            if (Math.abs(val - median) - 15 < Math.abs(closestMedian - median))
               closestMedian = val;
         }
         System.out.println("The median found using Binary Search is " + closestMedian);
         System.out.println("Number of comparisons: " + binaryCount);
      }
   }
   
   
   
   /******************************************************************************************************
   * initialize(args)                                                                                    *
   *                                                                                                     *
   * Purpose: Initializes the graph using the adjacency matrix found in the input file.                  *
   * Returns: boolean - whether the file was successfully converted into a matrix or not                 *
   * Parameters:                                                                                         *
   *        String[] args -  arguments input from the command line; the name of the file to read from    *
   ******************************************************************************************************/   
   private static boolean initialize(String [] args)
   {
      try
      {
         File inputFile = new File(args[0]);
         if (inputFile.exists())
         {
            //Store input lines in a list
            Scanner fileReader = new Scanner(inputFile);
            ArrayList<String> lines = new ArrayList<>();
            int numberOfLines = 0;
            while (fileReader.hasNextLine())
            {
               lines.add(fileReader.nextLine());
               numberOfLines++;
            }
            
            //Initialize 2D array     
            matrix = new int[numberOfLines][numberOfLines];                 
            for (int currentLine = 0; currentLine < numberOfLines; currentLine++)
            {
               //String line = lines.get(currentLine);
               Scanner lineReader = new Scanner(lines.get(currentLine));
               for (int index = 0; index < numberOfLines; index++)
               {
                  matrix[currentLine][index] = lineReader.nextInt();
               }     
                
            }
                   
            fileReader.close();
            return true;
         }
         else
         {            
            throw new FileNotFoundException();
         }         
      }
      catch (FileNotFoundException e) { System.out.println("ERROR: File name invalid."); }
      catch (Exception e) { System.out.println("ERROR: Please check that command arguments are valid."); }
      return false;
   }
   
   
   
   /******************************************************************************************************
   * mergeSort(matrix) and mergeSort(input, values)                                                      *
   *                                                                                                     *
   * Purpose: Finds the median of the matrix using MergeSort.                                            *
   * Returns: void                                                                                       *
   * Parameters:                                                                                         *
   *        int [][] matrix - the matrix to read                                                         *
   *        int [] input - the array to sort using MergeSort                                             *
   *        int values - the number of values in the array                                               *
   ******************************************************************************************************/  
   private static void mergeSort(int [][] matrix)
   {
      int [] matrixArray = convertToArray(matrix);
      
      //Call mergesort using newly generated array
      mergeSort(matrixArray, matrix.length * matrix.length);
      
      //Compute median and print
      System.out.println("The median found using Merge Sort is " + computeMedian(matrixArray));
   }
   private static void mergeSort(int [] input, int values)
   {
      //If only one value, no sorting is needed
      if (values < 2)
         return;
      
      //Initialize midpoint
      int halfLength = values / 2;
      
      //Sub-arrays
      int [] leftChild = new int[halfLength];
      int [] rightChild = new int[values - halfLength];
      
      //Fill in the children
      for (int index = 0; index < halfLength; index++)
         leftChild[index] = input[index];
      
      for (int index = halfLength; index < values; index++)
         rightChild[index - halfLength] = input[index];
      
      //Recursive calls
      mergeSort(leftChild, halfLength);
      mergeSort(rightChild, values - halfLength);
         
      //Actually merge the children
      merge(input, leftChild, rightChild, halfLength, values - halfLength);
   }
   
   
   
   /******************************************************************************************************
   * merge(input, leftChild, rightChild, left, right)                                                    *
   *                                                                                                     *
   * Purpose: Merges components together.                                                                *
   * Returns: void                                                                                       *
   * Parameters:                                                                                         *
   *        int [] input - the array to merge children into                                              *
   *        int [] leftChild - the first child to merge                                                  *
   *        int [] rightChild - the second child to merge                                                *
   *        int left/int right - the edges of the merge                                                  *
   ******************************************************************************************************/  
   private static void merge(int [] input, int [] leftChild, int [] rightChild, int left, int right)
   {
      int index= 0;
      int leftIndex = 0;
      int rightIndex = 0;
      while (index < left && leftIndex < right) 
      {         
         if (leftChild[index] <= rightChild[leftIndex]) 
         {
            mergeCount++;
            input[rightIndex++] = leftChild[index++];
         }
         else 
         {
            input[rightIndex++] = rightChild[leftIndex++];
         }
      }
      while (index < left) 
          input[rightIndex++] = leftChild[index++];
      while (leftIndex < right)
          input[rightIndex++] = rightChild[leftIndex++];
   }
   
   
   
   /******************************************************************************************************
   * computeMedian(matrixArray)                                                                          *
   *                                                                                                     *
   * Purpose: Computes the median of an input array                                                      *
   * Returns: int - the computed median                                                                  *
   * Parameters:                                                                                         *
   *        int [] matrixArray - the array to compute the median from                                    *
   ******************************************************************************************************/  
   private static int computeMedian(int [] matrixArray)
   {
      int higherMid = (matrixArray.length / 2);
      int lowerMid = higherMid - 1;
      int median;
      
      if (matrixArray.length % 2 != 0)
         median = matrixArray[higherMid];
      else
         median = (matrixArray[lowerMid] + matrixArray[higherMid]) / 2;
         
      return median;
   }
   

   
   /******************************************************************************************************
   * binarySearch(array, first, last)                                                                    *
   *                                                                                                     *
   * Purpose: Employs binary search to find the median in a given array                                  *
   * Returns: int - the median found using binary search                                                 *
   * Parameters:                                                                                         *
   *        int [] array - the array to search                                                           *
   *        int first - the leftmost point of the binary search                                          *
   *        int last - the rightmost point of the binary search                                          *
   ******************************************************************************************************/  
   private static int binarySearch(int [] array, int first, int last)
   {
      //Initialize values for conditionals
      int median = (array[0] + array[array.length - 1]) / 2;
      int count = countLesserValues(matrix, median);
      int goal = (matrix.length * matrix[0].length + 1) / 2;
      
      if (array.length == 2) //If two values remain, return the closest to the median
      {
         binaryCount++;
         int differenceOne = Math.abs(array[0] - median);
         int differenceTwo = Math.abs(array[1] - median);
         if (differenceOne == 0)
         {
            binaryCount++;
            return array[1];
         }
         else if (differenceTwo == 0)
         {
            binaryCount += 2;
            return array[0];
         }

         if (differenceOne < differenceTwo)
         {
            binaryCount += 3;
            return array[0];
         }
         binaryCount += 3;
         return array[1];
      }
      
      //If only one value exists in the array, return it
      if (array.length == 1)     
      {
         binaryCount += 2; 
         return array[0];
      }
      
      //If invalid array, return an error value
      if (array.length == 0 || first >= last)
      {
         binaryCount += 3;
         return -1;
      }
      
      //If number of lesser values is right, the median was found
      if (count == goal)
      {
         binaryCount += 4;
         return median;
      }            
      else if (count > goal) //If number is LARGER than it should, shift left
      {
         binaryCount += 5;
         int [] subArray = Arrays.copyOfRange(array, first, (first + last) / 2 + 1);
         return binarySearch(subArray, 0, (first + last + 1) / 2);
      }
      else if (count < goal) //If number is SMALLER than it should, shift right
      {
         binaryCount += 6;
         int [] subArray = Arrays.copyOfRange(array, (first + last) / 2 + 1, last + 1);
         return binarySearch(subArray, 0, (first + last + 1) / 2);
      }
      else
      {
         binaryCount += 6;
         return -1;
      }
   }



   /******************************************************************************************************
   * countLesserValues(input, val)                                                                       *
   *                                                                                                     *
   * Purpose: Counts how many values in a given matrix are less than the input value                     *
   * Returns: int - the count of how many lesser values exist in the matrix                              *
   * Parameters:                                                                                         *
   *        int [][] input - the matrix to scan for lower values                                         *
   *        int val - the value to compare against the matrix                                            *
   ******************************************************************************************************/  
   public static int countLesserValues(int [][] input, int val)
   {
      int count = 0;
      for (int row = 0; row < input.length; row++)
         for (int col = 0; col < input[0].length; col++)
         {            
            if (input[row][col] <= val)
               count++;               
            else
               col = input[0].length;
         }
      binaryCount += 3;
      return count;
   }
   
   
   
   /******************************************************************************************************
   * convertToArray(input)                                                                               *
   *                                                                                                     *
   * Purpose: Converts an input matrix into an array                                                     *
   * Returns: int [] - the converted matrix                                                              *
   * Parameters:                                                                                         *
   *        int [][] input - the matrix to convert                                                       *
   ******************************************************************************************************/     
   private static int [] convertToArray(int [][] input)
   {
      //Instantiating the array
      int [] matrixArray = new int[input.length * input.length];
      int index = 0;
      
      //Read the matrix and convert to an array
      for (int row = 0; row < input.length; row++)
         for (int col = 0; col < input[0].length; col++)
         {
            matrixArray[index] = input[row][col];
            index++;
         }
      
      return matrixArray;
   }  
}