package hr.cell.annotation.data.graphicalobject.impl.cells;

import java.awt.Color;
import java.util.Stack;

import hr.cell.annotation.data.graphicalobject.GraphicalObject;
import hr.cell.annotation.data.graphicalobject.impl.RectangleGraphicalObject;

public class FullCellMarker extends RectangleGraphicalObject {

	private FullCellMarker(int x, int y, int width, int height) {
		super(x,y,width,height);
	}
	
	public FullCellMarker() {
		super(0,0, 50,50);
	}
	
	@Override
	protected Color getColor() {
		return Color.red;
	}
	
	@Override
	public String getShapeID() {
		return "@FULLCELL";
	}
	
	@Override
	public String getShapeName() {
		return "Full cell marker";
	}
	
	@Override
	public GraphicalObject duplicate() {
		return new FullCellMarker();
	}
	
	@Override
	public void load(Stack<GraphicalObject> stack, String data) {
		String[] parts = data.split("\\s+");
		stack.push(new FullCellMarker(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]),
				Integer.parseInt(parts[2]), Integer.parseInt(parts[3])));
	}
	
	//TODO LOAD AND SAVE
	
}
