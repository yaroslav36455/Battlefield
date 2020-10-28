package ua.itea.view.swing;

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

import ua.itea.model.Color;
import ua.itea.model.util.Position;

public class ScaleableGLJPanel extends GLJPanel {
	
	private ArrayList<MonochromePixels> pixelArray;
	private float scale;
	
	public ScaleableGLJPanel(GLCapabilities capabilities, ArrayList<MonochromePixels> pixelArray) {
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
					gl2.glColor3f(color.red(), color.green(), color.blue());
					
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
				System.out.println(pixelArray);
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
		
		return panel;
	}
}
