package hr.cell.annotation.data.graphicalobject.impl.cells;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

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
	
	@Override
	public void load(Stack<GraphicalObject> stack, String data) {
		Scanner scan = new Scanner(data);

		int num = scan.nextInt();

		List<GraphicalObject> list = new LinkedList<>();
		for (int i = 0; i < num; i++) {
			list.add(stack.pop());
		}

		stack.push(new TrainingExampleGraphicalObject(list));

		scan.close();
	}

}
