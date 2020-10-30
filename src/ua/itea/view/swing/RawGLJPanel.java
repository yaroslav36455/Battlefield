package ua.itea.view.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;

import ua.itea.model.util.Position;

public class RawGLJPanel extends GLJPanel {
	
	private ArrayList<MonochromePixels> pixelArray;
	
	public RawGLJPanel(GLCapabilities capabilities, ArrayList<MonochromePixels> pixelArray) {
		super(capabilities);
		this.pixelArray = pixelArray;
		
		setListeners();
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
		        glu.gluOrtho2D(0.0f, drawable.getSurfaceWidth(), 0.0f, drawable.getSurfaceHeight());

		        gl2.glMatrixMode(GL2.GL_MODELVIEW);
		        gl2.glLoadIdentity();

		        gl2.glViewport(0, 0, drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
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
//				gl2.glLoadIdentity();
//				gl2.glBegin(GL.GL_POINTS);
//				for (int i = 0; i <= drawable.getSurfaceWidth(); i++) {
//					for (int j = 0; j <= drawable.getSurfaceHeight(); j++) {
//						gl2.glColor3f((float)i/drawable.getSurfaceWidth(),
//									  (float)j/drawable.getSurfaceHeight(), 0);
//						gl2.glVertex2f(i, j);
//					}
//				}
//				gl2.glVertex2f(1.0f, 1.0f);
//				gl2.glEnd();
				
		        // draw a triangle filling the window
//		        gl2.glLoadIdentity();
//		        gl2.glBegin(GL.GL_TRIANGLES);
//		        gl2.glColor3f(1, 0, 0);
//		        gl2.glVertex2f(0, 0);
//		        gl2.glColor3f(0, 1, 0);
//		        gl2.glVertex2f(drawable.getSurfaceWidth(), 0);
//		        gl2.glColor3f(0, 0, 1);
//		        gl2.glVertex2f(drawable.getSurfaceWidth()/2.f, drawable.getSurfaceHeight());
//		        gl2.glEnd();
				
				gl2.glLoadIdentity();
				gl2.glBegin(GL.GL_POINTS);
				for (MonochromePixels monochromePixels : pixelArray) {
					Color color = monochromePixels.getColor();
					
					gl2.glColor3f(color.getRed() / 255.f,
								  color.getGreen() / 255.f,
								  color.getBlue() / 255);
					for (Position pos : monochromePixels) {
						gl2.glVertex2f(pos.getX() + 1, pos.getY() + 1);
					}
				}
				
				gl2.glEnd();
				
//				System.out.println(pixelArray);
				System.out.println("end");
			}
		});
	}
	
	public JPanel makeViewport(int width, int height) {
		JPanel panel = new JPanel();
		GridBagConstraints gbc = new GridBagConstraints();
		
		setPreferredSize(new Dimension(width, height));

		panel.setLayout(new GridBagLayout());
		gbc.anchor = GridBagConstraints.CENTER;
		panel.add(this, gbc);
		
		return panel;
	}
}
