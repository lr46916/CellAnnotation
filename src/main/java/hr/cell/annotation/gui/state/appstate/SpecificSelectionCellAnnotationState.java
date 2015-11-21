package hr.cell.annotation.gui.state.appstate;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import hr.cell.annotation.data.drawutil.objectholder.ObjectHolder;
import hr.cell.annotation.data.graphicalobject.impl.CompositGraphicalObject;
import hr.cell.annotation.data.graphicalobject.impl.cells.TrainingExampleGraphicalObject;

public class SpecificSelectionCellAnnotationState extends SelectionState {

	public SpecificSelectionCellAnnotationState(ObjectHolder oh) {
		super(oh);
	}
	
	@Override
	public void keyPressed(int keyCode) {
		switch (keyCode) {
		case KeyEvent.VK_G:
			selectedObjects.stream().forEach((obj) -> holder.removeGraphicalObject(obj));
			CompositGraphicalObject cmp = new TrainingExampleGraphicalObject(new ArrayList<>(selectedObjects));
			selObj = cmp;
			holder.addGraphicalObject(selObj);
			selectedObjects.clear();
			selectedObjects.add(cmp);
			break;
		default:
			super.keyPressed(keyCode);
			return;
		}
		holder.notifyListeners();
	}

}
