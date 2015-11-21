package hr.cell.annotation.gui.application.component;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

import hr.cell.annotation.data.drawutil.objectholder.ObjectHolder;
import hr.cell.annotation.data.drawutil.objectholder.ObjectHolderListener;
import hr.cell.annotation.data.graphicalobject.GraphicalObject;
import hr.cell.annotation.gui.state.State;

public class PaintComponent extends JComponent implements ObjectHolderListener {

	private ObjectHolder holder;
	private State state;
	private Image backgroundImage;

	public PaintComponent(ObjectHolder holder) {
		super();
		this.holder = holder;
		holder.addObjectHolderListener(this);
		initListeners();
	}

	public void setState(State state) {
		this.state = state;
	}
	
	public void setBGImage(Image img){
		backgroundImage = img;
		this.setSize(new Dimension(img.getWidth(null), img.getHeight(null)));
		this.setPreferredSize(this.getSize());
	}

	public Image getBGImage(){
		return backgroundImage;
	}
	
	private void initListeners() {

		this.addKeyListener(new KeyListener() {

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
				state.keyPressed(e.getKeyCode());
			}
		});

		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				state.mouseUp(e.getPoint(),
						e.isShiftDown(), e.isControlDown());
			}

			@Override
			public void mousePressed(MouseEvent e) {
				state.mouseDown(e.getPoint(),
						e.isShiftDown(), e.isControlDown());
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				e.getComponent().requestFocus();
			}

		});

		this.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseDragged(MouseEvent e) {
				state.mouseDragged(e.getPoint());
			}
		});

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g.drawImage(backgroundImage, 0, 0, null);
		for (GraphicalObject o : holder.list()) {
			o.render(g2d);
			state.afterDraw(g2d, o);
		}
		state.afterDraw(g2d);
	}

	@Override
	public void objectsChanged() {
		this.repaint();
	}
}
