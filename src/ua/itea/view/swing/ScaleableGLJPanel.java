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

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;

import sun.security.ec.point.MutablePoint;
import ua.itea.model.util.MutablePosition;
import ua.itea.model.util.Position;

public class ScaleableGLJPanel extends GLJPanel {
	
	private ArrayList<MonochromePixels> pixelArray;
	private float scale;
	private Consumer<Position> cellPosition = new Consumer<Position>() {

		@Override
		public void accept(Position position) {
			System.out.println(position);
		}
		
	};
	
	public ScaleableGLJPanel(GLCapabilities capabilities, ArrayList<MonochromePixels> pixelArray) {
		super(capabilities);
		this.pixelArray = pixelArray;
		
		setListeners();
	}
	
	public void setCellPositionListener(Consumer<Position> cellPosition) {
		this.cellPosition = cellPosition;
	}
	
	private void setListeners() {
		addGLEventListener(new GLEventListener() {
			
			@Override
			public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void init(GLAutoDrawable drawable) {
				setFocusable(false);
				
				GL2 gl2 = drawable.getGL().getGL2();
				drawable.getGL().setSwapInterval(1);
				gl2.glClearColor(0.0f, 0.2f, 0.3f, 1.0f);
				
		        gl2.glMatrixMode(GL2.GL_PROJECTION);
		        gl2.glLoadIdentity();

		        // coordinate system origin at lower left with width and height same as the window
		        GLU glu = new GLU();
		        glu.gluOrtho2D(0.0f, drawable.getSurfaceWidth() / scale, 0.0f, drawable.getSurfaceHeight() / scale);

		        gl2.glMatrixMode(GL2.GL_MODELVIEW);
		        gl2.glLoadIdentity();

		        gl2.glViewport(0, 0, (int) (drawable.getSurfaceWidth() * scale),
		        					 (int) (drawable.getSurfaceHeight() * scale));
			}
			
			@Override
			public void dispose(GLAutoDrawable drawable) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void display(GLAutoDrawable drawable) {
				GL2 gl2 = drawable.getGL().getGL2();
				
				gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
				System.out.println("display(...)");
				
				gl2.glLoadIdentity();
				gl2.glBegin(GL.GL_TRIANGLES);
				
				for (MonochromePixels monochromePixels : pixelArray) {
					Color color = monochromePixels.getColor();
					gl2.glColor3f(color.getRed() / 255.f,
							  	  color.getGreen() / 255.f,
							  	  color.getBlue() / 255.f);
					
					for (Position pos : monochromePixels) {
						float x = pos.getX() + 1;
						float y =  pos.getY() + 1;
						
						gl2.glVertex2f(x - 1, y);
						gl2.glVertex2f(x, y);
						gl2.glVertex2f(x, y - 1);
						
						gl2.glVertex2f(x - 1, y);
						gl2.glVertex2f(x - 1, y - 1);
						gl2.glVertex2f(x, y - 1);
					 
					}
				}
				
				gl2.glEnd();
				
				System.out.println("end");
			}
		});
		
		addMouseListener(new MouseListener() {
			private MutablePosition currentPosition = new MutablePosition();
			
			@Override
			public void mouseReleased(MouseEvent e) {
				System.out.println("mouseReleased");
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				System.out.println("mousePressed");
				Point point = e.getPoint();
				
				currentPosition.setX((int) (point.getX() / scale));
				currentPosition.setY(-((int) (point.getY() / scale)) + 49);
				cellPosition.accept(currentPosition);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				System.out.println("mouseExited");
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				System.out.println("mouseEntered");
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("mouseClicked");
			}
		});
		
		addMouseMotionListener(new MouseMotionListener() {
			private MutablePosition oldPosition = new MutablePosition(Integer.MAX_VALUE, Integer.MAX_VALUE);
			private MutablePosition currentPosition = new MutablePosition();
			
			@Override
			public void mouseMoved(MouseEvent e) {
//				System.out.println("mouseMoved");
				/* empty */
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
//				Point point = e.getPoint();
//				
//				currentPosition.setX((int) (point.getX() / scale));
//				currentPosition.setY((int) (point.getY() / scale));
//				
//				if (!currentPosition.equalTo(oldPosition)) {
//					oldPosition.setX(currentPosition.getX());
//					oldPosition.setY(currentPosition.getY());
//					cellPosition.accept(currentPosition);
//					System.out.println("mouseDragged");
//				}
			}
		});
	}
	
	public JPanel makeViewport(int width, int height, float scale) {
		JPanel panel = new JPanel();
		GridBagConstraints gbc = new GridBagConstraints();
		
		this.scale = scale;
		setPreferredSize(new Dimension((int) (width * scale), (int) (height * scale)));

		panel.setLayout(new GridBagLayout());
		gbc.anchor = GridBagConstraints.CENTER;
		panel.add(this, gbc);
		
		panel.setBackground(new Color(0, 0, 0));
		return panel;
	}
}
