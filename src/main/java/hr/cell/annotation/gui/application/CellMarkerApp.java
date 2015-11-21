package hr.cell.annotation.gui.application;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;

import hr.cell.annotation.data.drawutil.objectholder.ObjectHolder;
import hr.cell.annotation.data.graphicalobject.GraphicalObject;
import hr.cell.annotation.data.graphicalobject.impl.CompositGraphicalObject;
import hr.cell.annotation.data.graphicalobject.impl.RectangleGraphicalObject;
import hr.cell.annotation.data.graphicalobject.impl.cells.FullCellMarker;
import hr.cell.annotation.data.graphicalobject.impl.cells.PartCellMarker;
import hr.cell.annotation.data.graphicalobject.impl.cells.TrainingExampleGraphicalObject;
import hr.cell.annotation.gui.application.component.PaintComponent;
import hr.cell.annotation.gui.state.State;
import hr.cell.annotation.gui.state.appstate.AddShapeState;
import hr.cell.annotation.gui.state.appstate.IdleState;
import hr.cell.annotation.gui.state.appstate.MarkerState;
import hr.cell.annotation.gui.state.appstate.SelectionState;
import hr.cell.annotation.gui.state.appstate.SpecificSelectionCellAnnotationState;

public class CellMarkerApp {

	public static final String imgsDirPath = "images";
	public static final String objectHolderDataPath = "annotations";

	private JFrame frame;
	private ObjectHolder holder;
	private PaintComponent paintComp;
	private Map<String, GraphicalObject> protoMap;
	private State appState; // = new IdleState();
	private Map<String, State> stateMap;
	private String activeImage;
	private File[] imgFileList;
	private int imgFileListActiveIndex;
	private JLabel currentImageLabel;

	/**
	 * Create the application.
	 */
	public CellMarkerApp(List<GraphicalObject> list) {
		protoMap = new HashMap<>();
		for (GraphicalObject go : list) {
			protoMap.put(go.getShapeID(), go);
		}
		initialize();
	}

	public void setState(State state) {
		appState.onExit();
		this.appState = state;
		paintComp.setState(state);
	}

	private void setStateMap() {
		stateMap = new HashMap<>();
		stateMap.put("select", new SpecificSelectionCellAnnotationState(holder));
		stateMap.put("marker", new MarkerState(holder));
		stateMap.put("idle", new IdleState());
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setTitle("Image annotator");
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (paintComp.changed) {
					int result = checkSave(
							"You wish to exit application while still having unsaved data. Do you wish to save your data?");
					if(result == JOptionPane.CANCEL_OPTION) {
						return;
					} else {
						if(result == JOptionPane.YES_OPTION) {
							saveData();
						}
						frame.dispose();
					}
				} else {
					frame.dispose();
				}
			}
		});

		initializeUserInputHandlers();
		holder = new ObjectHolder();
		paintComp = new PaintComponent(holder);

		this.frame.setFocusable(true);
		paintComp.setFocusable(true);
		frame.getContentPane().add(paintComp, BorderLayout.CENTER);

		setStateMap();

		appState = stateMap.get("idle");
		paintComp.setState(appState);

		JToolBar toolBar = new JToolBar();
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);

		JButton selectButton = new JButton("select");
		toolBar.add(selectButton);
		selectButton.addActionListener((event) -> {
			setState(stateMap.get("select"));
		});

		// JButton markerButton = new JButton("annotate");
		// markerButton.addActionListener((event) -> {
		// setState(stateMap.get("marker"));
		// });
		// toolBar.add(markerButton);

		JButton loadButton = new JButton("load");
		loadButton.addActionListener((event) -> {
			JFileChooser chooser = getChooser();
			chooser.showOpenDialog(frame.getParent());
			File f = chooser.getSelectedFile();
			if (f == null || !f.exists()) {
				return;
			}

			if (loadData(f, protoMap)) {
				paintComp.repaint();
				this.frame.pack();
			}
		});
		toolBar.add(loadButton);

		JButton saveButton = new JButton("save");
		saveButton.addActionListener((event) -> {
			if (saveData()) {
				System.err.println("SAVED!");
			}
		});
		toolBar.add(saveButton);

		for (String name : protoMap.keySet()) {
			JButton shapeButton = new JButton(protoMap.get(name).getShapeName());
			shapeButton.addActionListener((event) -> {
				setState(new AddShapeState(holder, protoMap.get(name)));
			});
			toolBar.add(shapeButton);
		}
		TrainingExampleGraphicalObject comp = new TrainingExampleGraphicalObject(new ArrayList<>());
		protoMap.put(comp.getShapeID(), comp);

		currentImageLabel = new JLabel(activeImage);
		JPanel labelPanel = new JPanel(new BorderLayout());
		labelPanel.add(currentImageLabel, BorderLayout.EAST);
		frame.getContentPane().add(labelPanel, BorderLayout.SOUTH);

		imgFileList = new File(System.getProperty("user.dir") + "/" + imgsDirPath).listFiles();
		Arrays.sort(imgFileList, (f1, f2) -> f1.getName().compareTo(f2.getName()));
		loadData(imgFileList[0], protoMap);
		activeImage = imgFileList[0].getName();
	}

	private void initializeUserInputHandlers() {
		frame.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_ESCAPE:
					appState = stateMap.get("idle");
					frame.requestFocusInWindow();
					break;

				case KeyEvent.VK_RIGHT:
					int nextImgIndex = (imgFileListActiveIndex + 1) % imgFileList.length;
					saveData();
					loadData(imgFileList[nextImgIndex], protoMap);
					imgFileListActiveIndex = nextImgIndex;
					break;

				case KeyEvent.VK_LEFT:
					int precImgIndex = imgFileListActiveIndex - 1;
					if (precImgIndex < 0)
						precImgIndex += imgFileList.length;
					saveData();
					loadData(imgFileList[precImgIndex], protoMap);
					imgFileListActiveIndex = precImgIndex;
				default:
					break;
				}
			}
		});
	}

	private static JFileChooser getChooser() {
		JFileChooser chooser = new JFileChooser() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean accept(File f) {
				return true;
			}
		};
		FileNameExtensionFilter filter = new FileNameExtensionFilter("image file", "png", "jpg", "jpag", "gif");
		chooser.setFileFilter(filter);
		File imgsDir = new File(System.getProperty("user.dir") + "/" + imgsDirPath);
		chooser.setCurrentDirectory(imgsDir);
		return chooser;
	}

	private boolean saveData() {
		// JFileChooser chooser = getChooser();
		// chooser.showSaveDialog(frame.getParent());
		// File f = chooser.getSelectedFile();
		// if (f == null)
		// return;
		File f = new File(System.getProperty("user.dir") + "/" + objectHolderDataPath + "/"
				+ activeImage.split("[.]")[0] + ".txt");
		try {
			PrintWriter pw = new PrintWriter(f);
			List<String> rows = new LinkedList<>();
			for (GraphicalObject go : holder.list()) {
				go.save(rows);
			}
			for (String line : rows) {
				pw.write(line + System.lineSeparator());
			}
			pw.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean loadData(File f, Map<String, GraphicalObject> protoMap) {
		if (paintComp.changed) {
			int checkSavedRes = checkSave("Loading a new document while having unsaved changes to current document"
					+ System.lineSeparator() + "Do you wish to save changes that you made?" + System.lineSeparator()
					+ "(clicking \"no\" will result in loosing changes you made)");
			if (checkSavedRes == JOptionPane.CANCEL_OPTION) {
				return false;
			} else {
				if (checkSavedRes == JOptionPane.YES_OPTION) {
					saveData();
				}
			}
		}
		activeImage = f.getName();
		try {
			paintComp.setBGImage(ImageIO.read(f));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		f = new File(objectHolderDataPath + "/" + activeImage.split("[.]")[0] + ".txt");
		holder.clear();
		if (!f.exists()) {
			System.err.println("No corresponding data annotation for image: " + activeImage);
		} else {
			Stack<String> stack1 = new Stack<>();
			readFileLinesInRows(stack1, f);
			Stack<GraphicalObject> stack = new Stack<>();
			loadElementsFromStringStack(stack1, stack, protoMap);
			GraphicalObject obj;
			while (!stack.isEmpty()) {
				obj = stack.pop();
				holder.addGraphicalObject(obj);
			}
		}
		frame.pack();
		currentImageLabel.setText(activeImage);
		currentImageLabel.repaint();
		paintComp.changed = false;
		return true;
	}

	private int checkSave(String message) {

		int result = JOptionPane.showConfirmDialog(frame, message);

		return result;

	}

	private static void loadElementsFromStringStack(Stack<String> stack1, Stack<GraphicalObject> stack,
			Map<String, GraphicalObject> protoMap) {
		String line;
		while (!stack1.isEmpty()) {
			line = stack1.pop();
			int index = line.indexOf(' ');
			protoMap.get(line.substring(0, index)).load(stack, line.substring(index + 1));
		}

	}

	private static void readFileLinesInRows(Stack<String> stack, File f) {
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new BufferedInputStream(new FileInputStream(f))));
			String line = br.readLine();
			while (line != null) {
				stack.push(line);
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				List<GraphicalObject> objects = new ArrayList<>();
				objects.add(new FullCellMarker());
				objects.add(new PartCellMarker());
				try {
					CellMarkerApp gui = new CellMarkerApp(objects);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
