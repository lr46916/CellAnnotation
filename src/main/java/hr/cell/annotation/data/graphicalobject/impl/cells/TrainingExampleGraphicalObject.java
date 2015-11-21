package hr.cell.annotation.data.graphicalobject.impl.cells;

import java.util.ArrayList;
import java.util.List;

import hr.cell.annotation.data.graphicalobject.GraphicalObject;
import hr.cell.annotation.data.graphicalobject.impl.CompositGraphicalObject;

public class TrainingExampleGraphicalObject extends CompositGraphicalObject {

	public TrainingExampleGraphicalObject(List<GraphicalObject> objects) {
		super(objects);
	}

	@Override
	public String getShapeName() {
		return "TRAINEXAMPLE";
	}

	@Override
	public GraphicalObject duplicate() {
		return new TrainingExampleGraphicalObject(new ArrayList<>(super.getParts()));
	}

	@Override
	public String getShapeID() {
		return "@TRAINEXAMPLE";
	}

}
