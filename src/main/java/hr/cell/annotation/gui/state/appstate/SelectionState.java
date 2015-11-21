package hr.cell.annotation.gui.state.appstate;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;

import hr.cell.annotation.data.drawutil.GeometryUtil;
import hr.cell.annotation.data.drawutil.RenderUtil;
import hr.cell.annotation.data.drawutil.objectholder.ObjectHolder;
import hr.cell.annotation.data.graphicalobject.GraphicalObject;
import hr.cell.annotation.gui.state.State;

public class SelectionState implements State {

	private ObjectHolder holder;
	private GraphicalObject selObj;
	private int currHPIndex = -1;
	private Point translatationStartPoint;

	public SelectionState(ObjectHolder oh) {
		this.holder = oh;
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
		// GraphicalObject selObj =
		// holder.findSelectedGraphicalObject(mousePoint);
		if (selObj == null) {
			return;
		}

		if (translatationStartPoint != null) {
			selObj.translate(GeometryUtil.pointDiff(mousePoint, translatationStartPoint));
			translatationStartPoint = mousePoint;
		} else {
			if (currHPIndex == -1)
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
		default:
			return;
		}
		holder.notifyListeners();
	}

	@Override
	public void afterDraw(Graphics2D r, GraphicalObject go) {
		RenderUtil.renderRectangle(go.getBoundingBox(), Color.BLACK, null, r);
		if (selObj == go) {
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
