package de.tuberlin.sese.swtpp.gameserver.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Field implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4587540154333050136L;
	
	int row;
	int col;
	
	public Field(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public Field(String pos) {
		this.row = 8 - Integer.valueOf(String.valueOf(pos.charAt(1)));
		this.col = pos.charAt(0) - 97;
	}
	
	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}
	
	@Override
	public String toString() {
		return  (char) (col + 97) + "" + (8 - row);
	}
	
	public ArrayList<Field> getFieldSurroundings() {
		ArrayList<Field> surroudings = new ArrayList<Field>();
		
		for (int i = -1; i <= 1; i++) {
			if(this.row + i < 0 || this.row + i > 7) continue;
			for (int j = -1; j <= 1; j++) {
				if(this.col + j < 0 || this.col + j > 7) continue;
				if(i == 0 && j == 0) continue;
				surroudings.add(new Field(this.row + i, this.col + j));
			}
		}
		
		return surroudings;
	}
}
