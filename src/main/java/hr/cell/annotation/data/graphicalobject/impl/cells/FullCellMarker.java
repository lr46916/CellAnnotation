package hr.cell.annotation.data.graphicalobject.impl.cells;

import java.awt.Color;

import hr.cell.annotation.data.graphicalobject.GraphicalObject;
import hr.cell.annotation.data.graphicalobject.impl.RectangleGraphicalObject;

public class FullCellMarker extends CellMarker {

	@Override
	protected Color getColor() {
		return Color.red;
	}

}
