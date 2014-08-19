package com.joshisk.mathbingo.core;


import java.awt.font.NumericShaper;
import java.security.AllPermission;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.joshisk.mathbingo.ResourceManager;

public class Board {

	private static final ResourceManager RM = ResourceManager.getInstance();
	
	/***************************** CONSTANTS ***********************/
	private static long RANDOM_SEED = System.currentTimeMillis();
	private static final byte DEFAULT_SIZE = 6;
	public static final byte DIVIDED_BY_SIGN = -1;
	public static final byte MULTIPLY_SIGN = -2;
	public static final byte PLUS_SIGN = -3;
	public static final byte MINUS_SIGN = -4;

	private static final byte MATRIX_EMPTY_GRID_VALUE = -100;

	/***************************** MEMBER VARIABLES ***********************/

	private byte size; // Byte (8-bits) is sufficient for size
	private byte matrix[][]; //TODO: Only a number upto 127 can be kept in the matrix

	public ArrayList<Operation> allOperations = new ArrayList<Operation>();
	private byte resultNumber;
	private byte minResultNumber = RM.DEFAULT_MINIMUM_RESULT_NUMBER;
	private byte maxResultNumber = RM.DEFAULT_MAXIMUM_RESULT_NUMBER;
	private byte minGridNumber = RM.DEFAULT_MINIMUM_GRID_NUMBER;
	private byte maxGridNumber = RM.DEFAULT_MAXIMUM_GRID_NUMBER;
	private byte maxDivideCount;
	private byte maxMultiplyCount; // All of these are getting initialized properly in constructor
	private byte maxPlusCount;
	private byte maxMinusCount;

	private byte divideCount = 0; // Counter of how many division signs used
	private byte multiplyCount = 0;
	private byte plusCount = 0;
	private byte minusCount = 0;
	/**********************************************************************/

	public Board() {
		this(DEFAULT_SIZE);
	}

	public Board(byte size) {
		RANDOM_SEED = System.currentTimeMillis();
		this.size = size;
		matrix = new byte[this.size][this.size];

		maxDivideCount = (byte) ((size * size * RM.DEFAULT_DIVIDE_PERCENT)/100);
		maxMultiplyCount = (byte) ((size * size * RM.DEFAULT_MULTIPLY_PERCENT)/100);
		maxPlusCount = (byte) ((size * size * RM.DEFAULT_PLUS_PERCENT)/100);
		maxMinusCount = (byte) ((size * size * RM.DEFAULT_MINUS_PERCENT)/100);

		initMatrix();
		generateMatrix();

	}

	/***************************** MEMBER FUNCTIONS ***********************/

	/**
	 * Initialize all cells of the Matrix to MATRIX_EMPTY_GRID_VALUE
	 */
	private void initMatrix() {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				matrix[i][j] = MATRIX_EMPTY_GRID_VALUE;
			}
		}
	}

	private int getFreeRowFromFreeCellNumber (int freeCell) {
		return freeCell/size;
	}
	private int getFreeColumnFromFreeCellNumber (int freeCell) {
		return freeCell%size;
	}
	private int getIndexFromRowCol(int row, int col) {
		return row * size + col;
	}


	private boolean isSignReachedMaximumCount(byte sign) {
		switch(sign) {
		case DIVIDED_BY_SIGN:
			if(divideCount < maxDivideCount)
				return false;
			break;
		case MULTIPLY_SIGN:
			if(multiplyCount < maxMultiplyCount)
				return false;
			break;
		case MINUS_SIGN:
			if(minusCount < maxMinusCount)
				return false;
			break;
		case PLUS_SIGN:
			if(plusCount < maxPlusCount)
				return false;
			break;
		}
		return true;
	}
	
	private static boolean isPrime(int number) {
		for(int i = 2; i <= number/2; i++) {
			if(number % i == 0) {
				return false;
			}
		}
		
		return true;
	}
	private void generateResultNumber() {
		Random rand = new Random(RANDOM_SEED);
		while(true) { // To skip if the result number if prime as this is very easy to guess for multiplications
			resultNumber = (byte)(rand.nextInt(maxResultNumber - minResultNumber) + minResultNumber);
			if(!isPrime(resultNumber))
				break;
		}
	}
	
	/**
	 * Generate random Matrix
	 */
	private void generateMatrix() {
		
		Random rand = new Random(RANDOM_SEED);
		generateResultNumber();

		System.out.println("DEBUG: Matrix generation Started for resultNumber: "+resultNumber);

		int numberOfTries = 0;
		int noOfAttemptsTried = 0;
		int maxAttempts = resultNumber + 1;
		while(allOperations.size() < (size * size)/3) {
			//Random rand = new Random(RANDOM_SEED);
			// Generate a sign (/,*,+,-) randomly which is number b/w 1 and 4 inclusive
			//rand.setSeed(System.currentTimeMillis());
			byte randomSign = (byte)(rand.nextInt(4) + 1);
			randomSign = (byte) - randomSign; // Negate the number to match one of the values of the sign
											  //    listed as final variable

//			System.out.println("Random Sign is: "+randomSign);

			if(isSignReachedMaximumCount(randomSign)) {
				continue;
			} else {
				switch(randomSign) {
				case DIVIDED_BY_SIGN:   divideCount++;
				break;
				case MULTIPLY_SIGN:     multiplyCount++;
				break;
				case PLUS_SIGN:     plusCount++;
				break;
				case MINUS_SIGN:     minusCount++;
				break;
				}
			}

			byte value1 = 0;
			byte value2 = 0;

			switch(randomSign) {
			case DIVIDED_BY_SIGN:

				value2 = (byte) (rand.nextInt((maxGridNumber/resultNumber) - 1)+2); // Ignoring 1 from value as it is too easy
				value1 = (byte) (value2 * resultNumber);

				//TODO: Have to put an extra check to verify value2 is not zero
				break;
			case MULTIPLY_SIGN:

				noOfAttemptsTried = 0;
			    maxAttempts = resultNumber + 1;
				do {
					value1 = (byte) (rand.nextInt(resultNumber-2) + 2); // Ignoring 1 from value as it is too easy
					if(resultNumber % value1  == 0 ) {
						value2 = (byte) (resultNumber / value1);
						break;
					}
					noOfAttemptsTried++;
				} while(noOfAttemptsTried < maxAttempts);
				if(noOfAttemptsTried >= maxAttempts) {
					System.out.println("ERROR: Couldn't generate proper value2, some thing wrong");
					System.out.println("ERROR:    ResultNumber is: "+resultNumber+" Value1 is: "+value1+ " Operation is Multiply");
					continue;
				}
				break;
			case PLUS_SIGN:

				value1 = (byte) (rand.nextInt(resultNumber-1) + 1);
				value2 = (byte) (resultNumber - value1);

				break;
			case MINUS_SIGN:

				noOfAttemptsTried = 0;
				maxAttempts = maxGridNumber;
				do {
					value1 = (byte) (rand.nextInt(maxGridNumber - resultNumber - 1) + resultNumber + 1);
					if(resultNumber%value1  != 0 ) {
						value2 = (byte) (value1 - resultNumber);
						break;
					}
					noOfAttemptsTried++;
				} while(noOfAttemptsTried < maxAttempts);
				if(noOfAttemptsTried >= maxAttempts) {
					System.out.println("ERROR: Couldn't generate proper value2, some thing wrong");
					System.out.println("ERROR:    ResultNumber is: "+resultNumber+" Value1 is: "+value1+ "Operation is MINUS");
					continue;
				}

				break;
			default:
				// Shouldn't come to this place. Otherwise some thing seriously wrong
				System.out.println("ERROR: Came to default block. Some thing seriously wrong");
			}

			//System.out.println("DEBUG:    "+value1 + " " + randomSign + " " + value2);
			Operation operToAdd = new Operation(value1, value2, randomSign);
			if(numberOfTries >= (size * size)/2 || !operationAlreadyExisting(operToAdd)) {
				allOperations.add(operToAdd);
			} else {
				numberOfTries++;
			}
			
		}
		
		shuffleMatrix();
	}
	
	public boolean operationAlreadyExisting(Operation operation) {
		for (Iterator iterator = allOperations.iterator(); iterator.hasNext();) {
			Operation oper = (Operation) iterator.next();
			if(oper.equals(operation)) {
				return true;
			}
		}
		return false;
	}

	public void printMatrix() {
		System.out.println("DEBUG: Printing the Matrix");
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] == MATRIX_EMPTY_GRID_VALUE) {
					System.out.print("E   ");
				} else if (matrix[i][j] == DIVIDED_BY_SIGN) {
					System.out.print("\u00F7   ");
				} else if (matrix[i][j] == MULTIPLY_SIGN) {
					System.out.print("X   ");
				} else if (matrix[i][j] == MINUS_SIGN) {
					System.out.print("-   ");
				} else if (matrix[i][j] == PLUS_SIGN) {
					System.out.print("+   ");
				} else {
					System.out.print(matrix[i][j] + "   ");
				}
			}
			System.out.println();
		}
	}

	public byte getSize() {
		return size;
	}

	public void setSize(byte size) {
		this.size = size;
	}

	public byte[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(byte[][] matrix) {
		this.matrix = matrix;
	}

	public byte getResultNumber() {
		return resultNumber;
	}

	public void setResultNumber(byte resultNumber) {
		this.resultNumber = resultNumber;
	}

	public boolean isSignAt(int i, int j) {
		int value = matrix[i][j];
		switch(value) {
		case DIVIDED_BY_SIGN:
			return true;
		case MULTIPLY_SIGN:
			return true;
		case PLUS_SIGN:
			return true;
		case MINUS_SIGN:
			return true;
		default:
			return false;
		}
	}

	public String getValueAt(int i, int j) {
		int value = matrix[i][j];
		switch(value) {
		case DIVIDED_BY_SIGN:
			return String.valueOf('\u00F7');
		case MULTIPLY_SIGN:
			return String.valueOf('\u00D7');
		case PLUS_SIGN:
			return String.valueOf('+');
		case MINUS_SIGN:
			return String.valueOf('-');
		default:
			return String.valueOf(value);
		}
	}

	public static int convertOperatorToInt(String operator) {
		if(operator.equals("+")) {
			return PLUS_SIGN;
		} else if(operator.equals("-")) {
			return MINUS_SIGN;
		} else if(operator.equals("\u00D7")) {
			return MULTIPLY_SIGN;
		} else if(operator.equals("\u00F7")) {
			return DIVIDED_BY_SIGN;
		} else {
			return MATRIX_EMPTY_GRID_VALUE;
		}
	}
	public int getValueAtInt(int i, int j) {
		int value = matrix[i][j];
		return value;
	}
	
	public void shuffleMatrix() {
		int tempArray[] = new int[size * size];
		
		for (int i = 0; i < tempArray.length; i++) {
			tempArray[i] = MATRIX_EMPTY_GRID_VALUE;
		}
		
		/*   Operators Processing */
		// Duplicate allOperations
		ArrayList<Operation> allOperationsCopy = new ArrayList<Operation>();
		for (Iterator iterator = allOperations.iterator(); iterator.hasNext();) {
			Operation operation = (Operation) iterator.next();
			allOperationsCopy.add(operation);
		}
		
		// Populate operators in array
		int index1 = 1;
		boolean flag = new Random().nextBoolean();
		
		while(allOperationsCopy.size() > 0) {
			Random random = new Random(System.currentTimeMillis());
			int index = random.nextInt(allOperationsCopy.size());
			Operation operation = allOperationsCopy.get(index);
//			System.out.println("DEBUG "+index1);
			tempArray[index1] = operation.getOperator();
			if(!flag) 
				index1 += 2;
			else 
				index1 += 3;
			
			flag = !flag;
			allOperationsCopy.remove(index);
		}
		
		/*   Operand 1 Processing */
		// Duplicate allOperations
		allOperationsCopy = new ArrayList<Operation>();
		for (Iterator iterator = allOperations.iterator(); iterator.hasNext();) {
			Operation operation = (Operation) iterator.next();
			allOperationsCopy.add(operation);
		}
		
		// Populate operand 1 in array
		while(allOperationsCopy.size() > 0) {
			Random random = new Random(System.currentTimeMillis());
			int index = random.nextInt(allOperationsCopy.size());
			Operation operation = allOperationsCopy.get(index);
			for (int i = 0; i < tempArray.length; i++) {
				if(tempArray[i] == MATRIX_EMPTY_GRID_VALUE) {
					tempArray[i] = operation.getOperand1();
					break;
				}
			}
			
			allOperationsCopy.remove(index);
		}
		
		/*   Operand 2 Processing */
		// Duplicate allOperations
		allOperationsCopy = new ArrayList<Operation>();
		for (Iterator iterator = allOperations.iterator(); iterator.hasNext();) {
			Operation operation = (Operation) iterator.next();
			allOperationsCopy.add(operation);
		}
		
		// Populate operand 2 in array
		while(allOperationsCopy.size() > 0) {
			Random random = new Random(System.currentTimeMillis());
			int index = random.nextInt(allOperationsCopy.size());
			Operation operation = allOperationsCopy.get(index);
			for (int i = 0; i < tempArray.length; i++) {
				if(tempArray[i] == MATRIX_EMPTY_GRID_VALUE) {
					tempArray[i] = operation.getOperand2();
					break;
				}
			}
			
			allOperationsCopy.remove(index);
		}
		

		int tempMatrixI = 0;
		int tempMatrixJ = 0;
		
		for (int i = 0; i < tempArray.length; i++) {
			
			if(i != 0 && i % size == 0) {
				tempMatrixI++;
				tempMatrixJ = 0;
			}
			
			matrix[tempMatrixI][tempMatrixJ++] = (byte)tempArray[i];
			
		}
		
//		printMatrix();
	}
	
	public int[] getHint() {
		int[] resultArray = new int[3];
		
		Random rand = new Random();
		int randomNumber = rand.nextInt(allOperations.size());
		
		boolean operand1Found = false;
		boolean operand2Found = false;
		boolean operatorFound = false;
		
		Operation operation = allOperations.get(randomNumber);
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if(!operand1Found && matrix[i][j] == operation.getOperand1()) {
					resultArray[0] = getIndexFromRowCol(i, j);
					operand1Found = true;
				} else if(!operand2Found && matrix[i][j] == operation.getOperand2()) {
					resultArray[2] = getIndexFromRowCol(i, j);
					operand2Found = true;
				} else if(!operatorFound && matrix[i][j] == operation.getOperator()) {
					resultArray[1] = getIndexFromRowCol(i, j);
					operatorFound = true;
				}
			}
		}
		return resultArray;
	}

} // Class END