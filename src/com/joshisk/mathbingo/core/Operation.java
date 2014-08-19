package com.joshisk.mathbingo.core;

public class Operation {

	public int getOperand1() {
		return operand1;
	}

	public void setOperand1(int operand1) {
		this.operand1 = operand1;
	}

	public int getOperand2() {
		return operand2;
	}

	public void setOperand2(int operand2) {
		this.operand2 = operand2;
	}

	public int getOperator() {
		return operator;
	}

	public void setOperator(int operator) {
		this.operator = operator;
	}

	private int operand1;
	private int operand2;
	private int operator;
	
	public Operation(int operand1, int operand2, int operator) {
		
		this.operand1 = operand1;
		this.operand2 = operand2;
		this.operator = operator;
	}
	
	public int result() {
		switch(operator) {
		case Board.DIVIDED_BY_SIGN:
			return operand1/operand2;
		case Board.MULTIPLY_SIGN:
			return operand1 * operand2;
		case Board.PLUS_SIGN:
			return operand1 + operand2;
		case Board.MINUS_SIGN:
			return operand1 - operand2;
		}
		return -1;
	}
	
	public String toString() {
		return operand1 + " " + operator + " " + operand2;
	}
	
	@Override
	public boolean equals(Object op) {
		if(! (op instanceof Operation))
			return false;
		
		Operation operation = (Operation) op;
		if(this.operator == operation.operator) {
			if((this.operand1 == operation.operand1 && this.operand2 == operation.operand2)
					|| (this.operand1 == operation.operand2 && this.operand2 == operation.operand1)) {
				return true;
			}
		}
		return false;
	}
}
