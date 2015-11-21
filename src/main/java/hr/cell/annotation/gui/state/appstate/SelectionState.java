package hr.cell.annotation.gui.state.appstate;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import hr.cell.annotation.data.drawutil.GeometryUtil;
import hr.cell.annotation.data.drawutil.RenderUtil;
import hr.cell.annotation.data.drawutil.objectholder.ObjectHolder;
import hr.cell.annotation.data.graphicalobject.GraphicalObject;
import hr.cell.annotation.data.graphicalobject.impl.CompositGraphicalObject;
import hr.cell.annotation.gui.state.State;

public class SelectionState implements State {

	protected ObjectHolder holder;
	protected GraphicalObject selObj;
	protected Set<GraphicalObject> selectedObjects;
	protected int currHPIndex = -1;
	protected Point translatationStartPoint;

	public SelectionState(ObjectHolder oh) {
		this.holder = oh;
		selectedObjects = new HashSet<>();
	}

	@Override
	public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
		if (!ctrlDown) {
			selObj = holder.findSelectedGraphicalObject(mousePoint);
			if (shiftDown) {
				translatationStartPoint = mousePoint;
			} else {
				translatationStartPoint = null;
			}
			if (selObj != null) {
				selectedObjects.add(selObj);
			} else {
				selectedObjects.clear();
			}
		} else {
			selObj = holder.findSelectedGraphicalObject(mousePoint);
			selectedObjects.add(selObj);
		}
		holder.notifyListeners();
	}

	@Override
	public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
		currHPIndex = -1;
		translatationStartPoint = null;
	}

	@Override
	public void mouseDragged(Point mousePoint) {
		if (selectedObjects.isEmpty()) {
			return;
		}
		if (translatationStartPoint != null) {
			selectedObjects.stream()
					.forEach((obj) -> obj.translate(GeometryUtil.pointDiff(mousePoint, translatationStartPoint)));
			translatationStartPoint = mousePoint;
		} else {
			if (currHPIndex == -1 && selectedObjects.size() == 1)
				currHPIndex = holder.findSelectedHotPoint(selObj, mousePoint);
			if (currHPIndex != -1) {
				selObj.setHotPoint(currHPIndex, mousePoint);
			}
		}

	}

	@Override
	public void keyPressed(int keyCode) {
		switch (keyCode) {
		case KeyEvent.VK_UP:
			selObj.translate(new Point(0, -1));
			break;
		case KeyEvent.VK_DOWN:
			selObj.translate(new Point(0, 1));
			break;

		case KeyEvent.VK_RIGHT:
			selObj.translate(new Point(1, 0));
			break;

		case KeyEvent.VK_LEFT:
			selObj.translate(new Point(-1, 0));
			break;

		case KeyEvent.VK_PLUS:
			holder.increaseZ(selObj);
			break;

		case KeyEvent.VK_MINUS:
			holder.decreaseZ(selObj);
			break;

		case KeyEvent.VK_DELETE:
			if (selObj != null)
				holder.removeGraphicalObject(selObj);
			break;

		case KeyEvent.VK_G:
			selectedObjects.stream().forEach((obj) -> holder.removeGraphicalObject(obj));
			CompositGraphicalObject cmp = new CompositGraphicalObject(new ArrayList<>(selectedObjects));
			selObj = cmp;
			holder.addGraphicalObject(selObj);
			selectedObjects.clear();
			selectedObjects.add(cmp);
			break;
		case KeyEvent.VK_U:
			// System.out.println("eto me: " + selObj.getShapeName());
			if (selObj != null && selObj instanceof CompositGraphicalObject) {
				CompositGraphicalObject comp = (CompositGraphicalObject) selObj;
				List<GraphicalObject> parts = comp.getParts();
				holder.removeGraphicalObject(selObj);
				for (GraphicalObject go : parts) {
					holder.addGraphicalObject(go);
				}
				selObj = comp;
			}
			break;
		default:
			return;
		}
		holder.notifyListeners();
	}

	@Override
	public void afterDraw(Graphics2D r, GraphicalObject go) {
		if (selectedObjects.contains(go))
			RenderUtil.renderRectangle(go.getBoundingBox(), Color.BLACK, null, r);
		if (selObj == go && selectedObjects.size() == 1) {
			Point[] hotSpots = new Point[go.getNumberOfHotPoints()];
			for (int i = 0; i < go.getNumberOfHotPoints(); i++) {
				hotSpots[i] = go.getHotPoint(i);
			}
			RenderUtil.renderHotSpots(hotSpots, r);
		}
	}

	@Override
	public void afterDraw(Graphics2D r) {
		// nothing
	}

	@Override
	public void onExit() {
		selObj = null;
		currHPIndex = -1;
		holder.notifyListeners();
	}
}
