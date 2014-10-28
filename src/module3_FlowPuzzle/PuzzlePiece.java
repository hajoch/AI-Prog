package module3_FlowPuzzle;

import java.awt.Color;
import java.util.ArrayList;

public class PuzzlePiece extends Object {
	
	public final String ID;
	
	public final int X;
	public final int Y;
	
	private ArrayList<PuzzlePiece> connectedPieces;
	
	public PuzzlePiece(int x, int y) {
		this.X = x;
		this.Y = y;
		ID = (x*100)+""+(y*100);
	}
	
	public void setConnectedPieces(ArrayList<PuzzlePiece> conn) {
		this.connectedPieces = conn;
	}
	
	public ArrayList<PuzzlePiece> getConnectedPieces() {
		return (ArrayList<PuzzlePiece>) connectedPieces;//.clone();
	}
}
