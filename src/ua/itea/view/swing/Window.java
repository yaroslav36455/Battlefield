package ua.itea.view.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.colorchooser.DefaultColorSelectionModel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.plaf.basic.BasicSplitPaneUI.BasicHorizontalLayoutManager;
import javax.swing.plaf.basic.BasicSplitPaneUI.BasicVerticalLayoutManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.jogamp.common.util.locks.RecursiveLock;
import com.jogamp.nativewindow.NativeSurface;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAnimatorControl;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLCapabilitiesImmutable;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLDrawable;
import com.jogamp.opengl.GLDrawableFactory;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.GLRunnable;
import com.jogamp.opengl.GLStateKeeper.Listener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.AnimatorBase;
import com.jogamp.opengl.util.FPSAnimator;
import com.sun.org.apache.xerces.internal.xinclude.XPointerSchema;

import sun.swing.table.DefaultTableCellHeaderRenderer;
import ua.itea.model.Engine;
import ua.itea.model.Flag;
import ua.itea.model.Neighbours;
import ua.itea.model.PathFinder;
import ua.itea.model.State;
import ua.itea.model.util.CardinalPoints;
import ua.itea.model.util.MutablePosition;
import ua.itea.model.util.Position;
import ua.itea.model.util.Size;

public class Window extends JFrame {
	private State state;
	private Engine engine;
//	
//	private JButton loadButton;
//	private JButton saveButton;
//	
//	private JButton beginButton;
//	private JButton continueButton;
//	private JButton pauseButton;
	
	private TableManager tableManager;
	private JPanel teamPanel;
	private JPanel squadPanel;
	
	private JPanel teamTablePanel;
	private JPanel squadTablePanel;
	
	private JButton createTeam;
	private JButton removeTeam;
	private JButton createSquad;
	private JButton removeSquad;
	private JButton createUnits;
	private JButton removeUnits;
	
	private OpenGLPanel glPanel;

	public Window() {
		super("Battlefield");
	
		JSplitPane splitPane;
		JComponent leftSide;
		JComponent rightSide;
		JPanel statusPanel;
		JLabel statusLabel;
		
		setLayout(new BorderLayout());
		
		createTeam = createAddTeamButton();
		removeTeam = createRemoveTeamButton();
		createSquad = createAddSquadButton();
		removeSquad = createRemoveSquadButton();
		createUnits = createAddUnitsButton();
		removeUnits = createRemoveUnitsButton();
		teamPanel = createTeamTablePanel();
		squadPanel = createSquadTablePanel();

		leftSide = createLeftSide();
		rightSide = createRightSide(300, 300);
		leftSide.setBorder(new TitledBorder("Organization"));

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
								   leftSide, rightSide);
		splitPane.setOneTouchExpandable(true);
		splitPane.setResizeWeight(0.5);
		
		statusLabel = new JLabel();
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		statusPanel.setPreferredSize(new Dimension(getWidth(), 16));
		statusPanel.add(statusLabel);

		add(splitPane);
		add(statusPanel, BorderLayout.SOUTH);

		Dimension minSize = new Dimension(200, 200);
		setMinimumSize(minSize);
		setSize(minSize);
		setExtendedState(MAXIMIZED_BOTH);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing( WindowEvent windowevent ) {
				dispose();
				System.exit(0);
				}
	         });
		
		tableManager = new TableManager(null,
				new Consumer<OwnTable>() {
					/* team row selection */
			
					@Override
					public void accept(OwnTable squadTable) {
						removeTeam.setEnabled(true);
						createSquad.setEnabled(true);
						if (squadTable.isSelectedOrdinaryRow()) {
							removeSquad.setEnabled(true);
						} else {
							removeSquad.setEnabled(false);
						}
						
						squadTablePanel.removeAll();
//						squadTablePanel.remove(currentSquadScrollPane);
//						currentSquadScrollPane = squadTable.makeScrollable();
						squadTablePanel.add(squadTable.makeScrollable());
						
//						squadTable.setVisible(true);
//						squadTable.revalidate();
//						squadTable.repaint();
						
//						currentSquadScrollPane.setVisible(true);
//						currentSquadScrollPane.revalidate();
//						currentSquadScrollPane.repaint();
						
//						squadTablePanel.setVisible(true);
//						squadTablePanel.revalidate();
//						squadTablePanel.repaint();
						
						squadPanel.setVisible(true);
						squadPanel.revalidate();
						squadPanel.repaint();
						
//						leftSide.setVisible(true);
//						leftSide.revalidate();
//						leftSide.repaint();
						
						System.out.println("selection count " + squadTablePanel.getComponentCount());
					}
				},
				new Consumer<OwnTable>() {
					/* team row unselection */
					
					@Override
					public void accept(OwnTable squadTable) {
						removeTeam.setEnabled(false);
						createSquad.setEnabled(false);
						
						squadTablePanel.removeAll();
//						squadTablePanel.remove(currentSquadScrollPane);
						
//						squadTable.setVisible(false);
//						squadTable.revalidate();
//						squadTable.repaint();
						
//						currentSquadScrollPane.setVisible(false);
//						currentSquadScrollPane.revalidate();
//						currentSquadScrollPane.repaint();
						
//						squadTablePanel.setVisible(false);
//						squadTablePanel.revalidate();
//						squadTablePanel.repaint();
						
						squadPanel.setVisible(false);
						squadPanel.revalidate();
						squadPanel.repaint();
						
//						leftSide.setVisible(false);
//						leftSide.revalidate();
//						leftSide.repaint();
						
						System.out.println("unselection count " + squadTablePanel.getComponentCount());
					}
				},
				new Consumer<TableRow>() {
					/* squad row selection */

					@Override
					public void accept(TableRow squadRow) {
						removeSquad.setEnabled(true);
					}
				},
				new Consumer<TableRow>() {
					/* squad row unselection */
					
					@Override
					public void accept(TableRow squadRow) {
						removeSquad.setEnabled(false);
					}
				});
		
		teamTablePanel.setLayout(new GridLayout());
		teamTablePanel.add(tableManager.getTeam().makeScrollable());
		
		squadTablePanel.setLayout(new GridLayout());
		
		setKeyListeners();
	}
	
	private void setKeyListeners() {
		addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				/* empty */
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				/* empty */
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					glPanel.display();
				}
			}
		});
		
	}

	private JButton createAddTeamButton() {
		JButton button = new JButton("Add Team");
		
		button.setEnabled(true);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tableManager.addTeam();
			}
		});
		
		return button;
	}
	
	private JButton createRemoveTeamButton() {
		JButton button = new JButton("Remove Team");
		
		button.setEnabled(false);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tableManager.removeTeam();
			}
		});
		
		return button;
	}
	
	private JButton createAddSquadButton() {
		JButton button = new JButton("Add Squad");
		
		button.setEnabled(false);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tableManager.addSquad();
			}
		});
		
		return button;
	}
	
	private JButton createRemoveSquadButton() {
		JButton button = new JButton("Remove Squad");
		
		button.setEnabled(false);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tableManager.removeSquad();
			}
		});
		
		return button;
	}
	
	private JButton createAddUnitsButton() {
		JButton button = new JButton("Add Units");
		
		button.setEnabled(false);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("unimplemented createAddUnitsButton()");
			}
		});
		
		return button;
	}
	
	private JButton createRemoveUnitsButton() {
		JButton button = new JButton("Remove Units");
		
		button.setEnabled(false);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("unimplemented createRemoveUnitsButton()");
			}
		});
		
		return button;
	}
	
	private JComponent createLeftSide() {
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
											  teamPanel, squadPanel);
		splitPane.setOneTouchExpandable(true);
		return splitPane;
	}
	
	private JPanel createTeamTablePanel() {
		JPanel panel = createTablePanel(createTeam, removeTeam);
		teamTablePanel = new JPanel();
		panel.add(teamTablePanel);
		
		return panel;
	}
	
	private JPanel createSquadTablePanel() {
		JPanel panel = createTablePanel(createSquad, removeSquad,
									    createUnits, removeUnits);
		squadTablePanel = new JPanel();
		panel.add(squadTablePanel);
		
		return panel;
	}
	
	private static JPanel createTablePanel(JButton ...buttons) {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		for (JButton jButton : buttons) {
			buttonPanel.add(jButton);
			jButton.setFocusable(false);
		}
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(buttonPanel, BorderLayout.SOUTH);
		
		return panel;
	}
	
	private JComponent createRightSide(int width, int height) {
		GLProfile glProfile = GLProfile.getDefault();
		GLCapabilities capabilities = new GLCapabilities(glProfile);
		
		ArrayList<MonochromePixels> pixelArray = new ArrayList<>();
//		for (int i = 0; i < 3; i++) {
//			MonochromePixels pixels = new MonochromePixels(new Color((float)Math.random(),
//																	 (float)Math.random(),
//																	 (float)Math.random()));
//			for (int j = 0; j < 500; j++) {
//				pixels.add(new Point((int)(Math.random() * (width + 1)),
//								     (int)(Math.random() * (heignt + 1))));
//			}
//			pixelArray.add(pixels);
//		}
		
		Point pointA = new Point(31, 289);
		Point pointB = new Point(250, 23);
		Position positionA = new Position((int) pointA.getX(), (int) pointA.getY());
		Position positionB = new Position((int) pointB.getX(), (int) pointB.getY());
//		Position[] blocks = new Position[width - 1];
//		int j = 0;
//		for (int i = 0; i < blocks.length;) {
//			if (j++ != 80) {
//				blocks[i] = new Position(i, Math.min(j, height));
//				i++;
//			}
//		}
		Position[] blocks = new Position[width];
		int j = 0;
		for (int i = 0; i < blocks.length; i++) {
			blocks[i] = new Position(i, j++);
		}
		
		MonochromePixels pixels;
		
		pixels = new MonochromePixels(new Color(0.f, 0.9f, 0.f));
		pixels.add(pointA);
		pixelArray.add(pixels);
		
		pixels = new MonochromePixels(new Color(0.f, .5f, 1.f));
		pixels.add(pointB);
		pixelArray.add(pixels);
		
		pixels = new MonochromePixels(new Color(0.5f, 0.5f, 0.5f));
		for (Position position : blocks) {
			pixels.add(new Point(position.getX(), position.getY()));
		}
		pixelArray.add(pixels);
		
		PathFinder pathFinder = new PathFinder(new Size(width + 1, height + 1));
		pathFinder.setIsDestinationPredicate(new Predicate<Position>() {
			private Neighbours neighbours = new Neighbours();
			
			@Override
			public boolean test(Position t) {
				neighbours.setPosition(positionB);
				for (CardinalPoints direction : CardinalPoints.values()) {
					if (t.equalTo(neighbours.getNeighbour(direction))) {
						return true;
					}
				}
				return false;
			}
		});
		
		pathFinder.setMovementAllowedPredicate(new Predicate<Position>() {

			@Override
			public boolean test(Position t) {
				if (t.equalTo(positionA) || t.equalTo(positionB)) {
					return false;
				}
				for (Position pos : blocks) {
					if(t.equalTo(pos)) {
						return false;
					}
				}
				
				return true;
			}
			
		});
		
		ArrayList<CardinalPoints> cardinalPoints = pathFinder.find(positionA, 2);
		pixels = new MonochromePixels(new Color(0.85f, 0.f, 0.f));
		MutablePosition mutablePosition = new MutablePosition(positionA);
		for (CardinalPoints direction : cardinalPoints) {
			mutablePosition.move(direction);
			pixels.add(new Point(mutablePosition.getX(), mutablePosition.getY()));
		}
		System.out.println("size: " + pixels.size());
		pixelArray.add(pixels);
		
		glPanel = new OpenGLPanel(capabilities, pixelArray);
		return new JScrollPane(glPanel.makeViewport(width, height),
							   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
							   JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}
	
//	private class ColorChooser extends JColorChooser {
//
//		public ColorChooser(Color color) {
//			super(color);
//		}
//	}
//
//	private class MainMenuPanel extends JPanel {
//		private JButton newBattle;
//		private JButton loadBattle;
//		private JButton saveBattle;
//		private JButton endBattle;
//	}
//
//	private class CreateFieldPanel extends JPanel {
//		private JButton createField;
//		private JLabel widthLabel;
//		private JLabel heightLabel;
//		private JTextField widthTextField;
//		private JTextField heightTextField;
//
//		public CreateFieldPanel() {
//			createField = new JButton("Create");
//		}
//	}
//
//	private class TeamModifierPane extends JPanel {
//
//	}
//
//	private class StatePane extends JPanel {
//		
//	}
}
