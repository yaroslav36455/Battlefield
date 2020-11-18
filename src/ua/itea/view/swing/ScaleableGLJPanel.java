package ua.itea.view.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.function.Consumer;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;

import ua.itea.model.util.MutablePosition;
import ua.itea.model.util.Position;

public class ScaleableGLJPanel extends GLJPanel {
	private static final long serialVersionUID = 1L;
	
	private ArrayList<MonochromePixels> pixelArray;
	private int fieldWidth;
	private int fieldHeight;
	private float scale;
	private ViewportMouseListener viewportMouseListener;

	public ScaleableGLJPanel(GLCapabilities capabilities) {
		super(capabilities);
		pixelArray = new ArrayList<>();
		viewportMouseListener = new ViewportMouseListener();

		setFocusable(false);
		setListeners();
	}

	public void setPixelArray(ArrayList<MonochromePixels> pixelArray) {
		this.pixelArray = pixelArray;
	}

	public void setConsumerForLMB(Consumer<Position> cellConsumerForLMB) {
		viewportMouseListener.setConsumerForLMB(cellConsumerForLMB);
	}
	
	public void setConsumerForRMB(Consumer<Position> setConsumerForRMB) {
		viewportMouseListener.setConsumerForRMB(setConsumerForRMB);
	}

	private void setListeners() {
		addGLEventListener(new GLEventListener() {

			@Override
			public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
				/* empty */
			}

			@Override
			public void init(GLAutoDrawable drawable) {
				GL2 gl2 = drawable.getGL().getGL2();
				drawable.getGL().setSwapInterval(1);
				gl2.glClearColor(0.0f, 0.2f, 0.3f, 1.0f);

				gl2.glMatrixMode(GL2.GL_PROJECTION);
				gl2.glLoadIdentity();

				GLU glu = new GLU();
				glu.gluOrtho2D(0.0f, drawable.getSurfaceWidth() / scale, 0.0f, drawable.getSurfaceHeight() / scale);

				gl2.glMatrixMode(GL2.GL_MODELVIEW);
				gl2.glLoadIdentity();
			}

			@Override
			public void dispose(GLAutoDrawable drawable) {
				/* empty */
			}

			@Override
			public void display(GLAutoDrawable drawable) {
				GL2 gl2 = drawable.getGL().getGL2();

				gl2.glClear(GL.GL_COLOR_BUFFER_BIT);

				gl2.glLoadIdentity();
				gl2.glBegin(GL.GL_TRIANGLES);

				for (MonochromePixels monochromePixels : pixelArray) {
					Color color = monochromePixels.getColor();
					gl2.glColor3f(color.getRed() / 255.f, color.getGreen() / 255.f, color.getBlue() / 255.f);

					for (Position pos : monochromePixels) {
						float x = pos.getX() + 1;
						float y = pos.getY() + 1;

						gl2.glVertex2f(x - 1, y);
						gl2.glVertex2f(x, y);
						gl2.glVertex2f(x, y - 1);

						gl2.glVertex2f(x - 1, y);
						gl2.glVertex2f(x - 1, y - 1);
						gl2.glVertex2f(x, y - 1);

					}
				}

				gl2.glEnd();
			}
		});

		addMouseListener(viewportMouseListener);
		addMouseMotionListener(viewportMouseListener);
	}

	public JPanel makeViewport(int width, int height, float scale) {
		JPanel panel = new JPanel();
		GridBagConstraints gbc = new GridBagConstraints();

		this.fieldWidth = width;
		this.fieldHeight = height;
		this.scale = scale;
		setPreferredSize(new Dimension((int) (width * scale), (int) (height * scale)));

		panel.setLayout(new GridBagLayout());
		gbc.anchor = GridBagConstraints.CENTER;
		panel.add(this, gbc);

		panel.setBackground(new Color(0, 0, 0));
		return panel;
	}
	
	private class ViewportMouseListener implements MouseListener,
												   MouseMotionListener {

		private Consumer<Position> cellPositionForLMB;
		private Consumer<Position> cellPositionForRMB;

		public void setConsumerForLMB(Consumer<Position> cellPositionForLMB) {
			this.cellPositionForLMB = cellPositionForLMB;
		}

		public void setConsumerForRMB(Consumer<Position> cellPositionForRMB) {
			this.cellPositionForRMB = cellPositionForRMB;
		}

		@Override
		public void mouseDragged(MouseEvent mouseEvent) {
			MutablePosition currentPosition = determinePosition(mouseEvent);

			if (withinTheField(currentPosition)) {
				if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
					cellPositionForLMB.accept(currentPosition);
				} else if (SwingUtilities.isRightMouseButton(mouseEvent)) {
					cellPositionForRMB.accept(currentPosition);
				}
			}
		}

		@Override
		public void mouseMoved(MouseEvent mouseEvent) {
			/* empty */
		}

		@Override
		public void mouseClicked(MouseEvent mouseEvent) {
			/* empty */
		}

		@Override
		public void mousePressed(MouseEvent mouseEvent) {
			MutablePosition currentPosition = determinePosition(mouseEvent);
			
			if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
				cellPositionForLMB.accept(currentPosition);
			} else if (SwingUtilities.isRightMouseButton(mouseEvent)) {
				cellPositionForRMB.accept(currentPosition);
			}
		}

		@Override
		public void mouseReleased(MouseEvent mouseEvent) {
			/* empty */
		}

		@Override
		public void mouseEntered(MouseEvent mouseEvent) {
			/* empty */
		}

		@Override
		public void mouseExited(MouseEvent mouseEvent) {
			/* empty */
		}
		
		private boolean withinTheField(MutablePosition mutablePosition) {
			return mutablePosition.getX() >= 0
					&& mutablePosition.getX() < fieldWidth
					&& mutablePosition.getY() >= 0
					&& mutablePosition.getY() < fieldHeight;
		}
		
		private MutablePosition determinePosition(MouseEvent mouseEvent) {
			Point point = mouseEvent.getPoint();
			MutablePosition mutablePosition = new MutablePosition();

			mutablePosition.setX((int) (point.getX() / scale));
			mutablePosition.setY(-((int) (point.getY() / scale)) + (fieldHeight - 1));
			
			return mutablePosition;
		}
	}
}
