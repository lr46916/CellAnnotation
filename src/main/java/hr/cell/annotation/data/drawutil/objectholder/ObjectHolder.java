package hr.cell.annotation.data.drawutil.objectholder;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import hr.cell.annotation.data.graphicalobject.GraphicalObject;
import hr.cell.annotation.data.graphicalobject.listener.GraphicalObjectListener;

public class ObjectHolder {
	public final static double SELECTION_PROXIMITY = 10;

	// Kolekcija svih grafičkih objekata:
	private List<GraphicalObject> objects = new ArrayList<>();
	// Read-Only proxy oko kolekcije grafičkih objekata:
	private List<GraphicalObject> roObjects = Collections
			.unmodifiableList(objects);
	// Kolekcija prijavljenih promatrača:
	private List<ObjectHolderListener> listeners = new ArrayList<>();
	// Kolekcija selektiranih objekata:
	private List<GraphicalObject> selectedObjects = new ArrayList<>();
	// Read-Only proxy oko kolekcije selektiranih objekata:
	private List<GraphicalObject> roSelectedObjects = Collections
			.unmodifiableList(selectedObjects);

	// Promatrač koji će biti registriran nad svim objektima crteža...
	private final GraphicalObjectListener goListener = new GraphicalObjectListener() {
		
		@Override
		public void graphicalObjectUpdate(GraphicalObject go) {
			notifyListeners();
		}

	};

	// Brisanje svih objekata iz modela (pazite da se sve potrebno odregistrira)
	// i potom obavijeste svi promatrači modela
	public void clear() {
		Iterator<GraphicalObject> it = objects.iterator();
		while (it.hasNext()) {
			GraphicalObject tmp = it.next();
			tmp.removeGraphicalObjectListener(goListener);
			it.remove();
		}
		it = selectedObjects.iterator();
		while (it.hasNext()) {
			GraphicalObject tmp = it.next();
			tmp.removeGraphicalObjectListener(goListener);
			it.remove();
		}
		notifyListeners();
	}

	// Dodavanje objekta u dokument (pazite je li već selektiran; registrirajte
	// model kao promatrača)
	public void addGraphicalObject(GraphicalObject obj) {
		objects.add(obj);
//		if (obj.isSelected()) {
//			selectedObjects.add(obj);
//		}
		obj.addGraphicalObjectListener(goListener);
		notifyListeners();
	}

	// Uklanjanje objekta iz dokumenta (pazite je li već selektiran;
	// odregistrirajte model kao promatrača)
	public void removeGraphicalObject(GraphicalObject obj) {
		objects.remove(obj);
		selectedObjects.remove(obj);
		obj.removeGraphicalObjectListener(goListener);
		notifyListeners();
	}

	// Vrati nepromjenjivu listu postojećih objekata (izmjene smiju ići samo
	// kroz metode modela)
	public List<GraphicalObject> list() {
		return roObjects;
	}

	// Prijava...
	public void addObjectHolderListener(ObjectHolderListener l) {
		listeners.add(l);
	}

	// Odjava...
	public void removeObjectHolderListener(ObjectHolderListener l) {
		listeners.remove(l);
	}

	// Obavještavanje...
	public void notifyListeners() {
		for (ObjectHolderListener dml : listeners) {
			dml.objectsChanged();
		}
	}

	// Vrati nepromjenjivu listu selektiranih objekata
	public List<GraphicalObject> getSelectedObjects() {
		return roSelectedObjects;
	}

	// Pomakni predani objekt u listi objekata na jedno mjesto kasnije...
	// Time će se on iscrtati kasnije (pa će time možda veći dio biti vidljiv)
	public void increaseZ(GraphicalObject go) {
		int index = objects.indexOf(go);
		if (index == objects.size() - 1) {
			return;
		}
		objects.remove(index);
		objects.add(index + 1, go);
	}

	// Pomakni predani objekt u listi objekata na jedno mjesto ranije...
	public void decreaseZ(GraphicalObject go) {
		int index = objects.indexOf(go);
		if (index == 0) {
			return;
		}
		objects.remove(index);
		objects.add(index - 1, go);
	}

	// Pronađi postoji li u modelu neki objekt koji klik na točku koja je
	// predana kao argument selektira i vrati ga ili vrati null. Točka selektira
	// objekt kojemu je najbliža uz uvjet da ta udaljenost nije veća od
	// SELECTION_PROXIMITY. Status selektiranosti objekta ova metoda NE dira.
	public GraphicalObject findSelectedGraphicalObject(Point mousePoint) {
		GraphicalObject closest = null;
		double min = Double.MAX_VALUE;
		for (GraphicalObject obj : objects) {
			double dist = obj.selectionDistance(mousePoint);
			if (min > dist) {
				min = dist;
				closest = obj;
			}
		}
		if (min > SELECTION_PROXIMITY) {
			return null;
		}
		return closest;
	}

	// Pronađi da li u predanom objektu predana točka miša selektira neki
	// hot-point.
	// Točka miša selektira onaj hot-point objekta kojemu je najbliža uz uvjet
	// da ta
	// udaljenost nije veća od SELECTION_PROXIMITY. Vraća se indeks hot-pointa
	// kojeg bi predana točka selektirala ili -1 ako takve nema. Status
	// selekcije
	// se pri tome NE dira.
	public int findSelectedHotPoint(GraphicalObject object, Point mousePoint) {
		double min = Double.MAX_VALUE;
		int len = object.getNumberOfHotPoints();
		int index = -1;
		for (int i = 0; i < len; i++) {
			double dist = object.getHotPointDistance(i, mousePoint);
			if (min > dist) {
				min = dist;
				index = i;
			}
		}
		if (min > SELECTION_PROXIMITY) {
			return -1;
		}
		return index;
	}
}
