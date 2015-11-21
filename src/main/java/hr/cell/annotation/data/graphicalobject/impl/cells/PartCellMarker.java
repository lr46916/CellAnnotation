package hr.cell.annotation.data.graphicalobject.impl.cells;

import java.awt.Color;
import java.util.Stack;

import hr.cell.annotation.data.graphicalobject.GraphicalObject;
import hr.cell.annotation.data.graphicalobject.impl.RectangleGraphicalObject;

public class PartCellMarker extends RectangleGraphicalObject {

	private PartCellMarker(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	public PartCellMarker() {
		super(0, 0, 50, 50);
	}

	@Override
	protected Color getColor() {
		return Color.green;
	}

	@Override
	public String getShapeID() {
		return "@PARTCELL";
	}

	@Override
	public String getShapeName() {
		return "Part cell marker";
	}

	@Override
	public GraphicalObject duplicate() {
		return new PartCellMarker();
	}
	
	@Override
	public void load(Stack<GraphicalObject> stack, String data) {
		String[] parts = data.split("\\s+");
		stack.push(new PartCellMarker(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]),
				Integer.parseInt(parts[2]), Integer.parseInt(parts[3])));
	}

}
