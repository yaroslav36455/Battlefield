package ua.itea.view.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.function.Consumer;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;

import ua.itea.model.Engine;
import ua.itea.model.Field;
import ua.itea.model.BattleField;
import ua.itea.model.Cell;
import ua.itea.model.Placement;
import ua.itea.model.State;
import ua.itea.model.Team;
import ua.itea.model.Team.Squad;
import ua.itea.model.Team.Squad.Unit;
import ua.itea.model.util.MutablePosition;
import ua.itea.model.util.Position;
import ua.itea.model.util.Size;

public class Window extends JFrame {
	private ArrayList<MonochromePixels> pixelArray;
	private State state;
	private Engine engine;
	
	private Squad selectedSquad;
	private boolean isAdding;
//	
//	private JButton loadButton;
//	private JButton saveButton;
//	
//	private JButton beginButton;
//	private JButton continueButton;
//	private JButton pauseButton;
	
	private AddTeamDialog addTeamDialog;
	
	private TableManager tableManager;
	private JPanel teamPanel;
	private JPanel squadPanel;
	
	private JPanel teamTablePanel;
	private JPanel squadTablePanel;
	
	private JButton createTeam;
	private JButton removeTeam;
	private JButton createSquad;
	private JButton removeSquad;
	private JButton editSquad;
	private JButton createUnits;
	private JButton removeUnits;
	
	private ScaleableGLJPanel glPanel;
	private JComponent viewport;
	
	private JSplitPane orgPanel;
	
	private JComponent leftSide;
	private JComponent rightSide;

	public Window() {
		super("Battlefield");
	
		JSplitPane splitPane;
		JPanel statusPanel;
		JLabel statusLabel;
		
		setLayout(new BorderLayout());
		
		createTeam = createAddTeamButton();
		removeTeam = createRemoveTeamButton();
		createSquad = createAddSquadButton();
		removeSquad = createRemoveSquadButton();
		editSquad = createEditSquadButton();
		createUnits = createAddUnitsButton();
		removeUnits = createRemoveUnitsButton();
		teamPanel = createTeamTablePanel();
		squadPanel = createSquadTablePanel();
		
		glPanel = createGLPanel();
		
		leftSide = createLeftSide();
		rightSide = createRightSide();

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
		setExtendedState(MAXIMIZED_BOTH);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent windowevent) {
				dispose();
			}
	    });
		
		setKeyListeners();
	}
	
	private TableManager createTableManager() {
		TableManager newTableManager = new TableManager(
				new Consumer<FormationTable>() {
					/* team row selection */
			
					@Override
					public void accept(FormationTable squadTable) {
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
						
//						System.out.println("selection count " + squadTablePanel.getComponentCount());
						
						if (tableManager.isSelectedSquad()) {
							selectedSquad = tableManager.getSelectedSquad();
						}
					}
				},
				new Consumer<FormationTable>() {
					/* team row unselection */
					
					@Override
					public void accept(FormationTable squadTable) {
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
						
//						System.out.println("unselection count " + squadTablePanel.getComponentCount());
					}
				},
				new Consumer<TableRow>() {
					/* squad row selection */

					@Override
					public void accept(TableRow squadRow) {
						removeSquad.setEnabled(true);
						editSquad.setEnabled(true);
						createUnits.setEnabled(true);
						removeUnits.setEnabled(true);
						
						selectedSquad = tableManager.getSelectedSquad();
					}
				},
				new Consumer<TableRow>() {
					/* squad row unselection */
					
					@Override
					public void accept(TableRow squadRow) {
						removeSquad.setEnabled(false);
						editSquad.setEnabled(false);
						createUnits.setEnabled(false);
						removeUnits.setEnabled(false);
						
						selectedSquad = null;
					}
				});
		
		return newTableManager;
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
					engine.iterate();
					
					tableManager.update();
					updatePixelArray(state.getTeams());
					glPanel.display();
				}
			} 
		});
	}

	private JButton createAddTeamButton() {
		JFrame thisWindow = this;
		JButton button = new JButton("Add Team");
		
		button.setEnabled(true);
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
//				addTeamDialog = new AddTeamDialog(thisWindow, "Add Team");
//				addTeamDialog.refresh("Text #22", new Color((int) (Math.random() * Integer.MAX_VALUE)));
				
				Team newTeam = new Team();
				
				state.getTeams().add(newTeam);
				tableManager.addTeam(newTeam);
				
				updatePixelArray(state.getTeams());
				glPanel.display();
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
				Field<Cell> field = state.getField();
				Team team = tableManager.removeTeam();
				
				for (Squad squad : team) {
					for (Unit unit : squad) {
						field.get(unit.getPlacement().getPosition()).setUnit(null);
					}
					squad.disposeAllUnits();
				}
				team.removeAllSquads();
				
				tableManager.update();
				updatePixelArray(state.getTeams());
				glPanel.display();
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
				Team team = tableManager.getSelectedTeam();
				Squad squad = team.new Squad();
				
				tableManager.addSquad(squad);
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
				Team team = tableManager.getSelectedTeam();
				Squad squad = tableManager.removeSquad();
				Field<Cell> field = state.getField();
				
				for (Unit unit : squad) {
					field.get(unit.getPlacement().getPosition()).setUnit(null);
				}
				
				squad.disposeAllUnits();
				team.removeSquad(squad);
				
				tableManager.update();
				updatePixelArray(state.getTeams());
				glPanel.display();
			}
		});
		
		return button;
	}
	
	private JButton createEditSquadButton() {
		JButton button = new JButton("Edit Squad");
		
		button.setEnabled(false);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("unimplemented createEditSquadButton()");
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
				isAdding = true;
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
				isAdding = false;
			}
		});
		
		return button;
	}
	
	private JComponent createLeftSide() {
		JPanel panel = new JPanel();
		orgPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				  					 teamPanel, squadPanel);
		orgPanel.setOneTouchExpandable(true);
		orgPanel.setBorder(new TitledBorder("Organization"));
		
		panel.setLayout(new BorderLayout());
		panel.add(createMenuBar(), BorderLayout.NORTH);
		panel.add(orgPanel);
		
		orgPanel.setVisible(false);
		return panel;
	}
	
	private JComponent createRightSide() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout());
		
		return panel;
	}
	
	private MenuBar createMenuBar() {
		MenuBar menuBar = new MenuBar();
		Menu menu = menuBar.getMenu();
		
		menu.addCreateFieldListener(new ActionListener() {
			private RequestSizeDialog requestSizeDialog;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (requestSizeDialog == null) {
					requestSizeDialog = new RequestSizeDialog(Window.this);
					requestSizeDialog.setSizeConsumer(Window.this::createSimulation);
				}
				requestSizeDialog.setVisible(true);
			}
		});
		
		return menuBar;
	}
	
	private void createSimulation(Size fieldSize) {
		if (state == null || engine == null || tableManager == null) {
			state = new State(new BattleField(fieldSize), new ArrayList<>());
			engine = new Engine(state);
			tableManager = createTableManager();
		} else {
			state.getField().resize(fieldSize);
			state.getTeams().clear();
			tableManager.clear();
		}
		
		placePanels(fieldSize);
	}
	
	private void placePanels(Size fieldSize) {
		placeViewportPanel(fieldSize);
		placeOrganizationPanel();
	}
	
//	private void hidePanels() {
//		
//	}
	
	private void placeOrganizationPanel() {
		teamTablePanel.add(tableManager.getTeams().makeScrollable());
		orgPanel.setVisible(true);
		orgPanel.revalidate();
	}

//	private void hideOrganizationPanel() {
//		tablesPanel.setVisible(false);
//	}

	private void placeViewportPanel(Size size) {
		viewport = new JScrollPane(glPanel.makeViewport(size.getWidth(), size.getHeight(), 10),
				   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				   JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		rightSide.add(viewport);
		rightSide.setVisible(true);
		rightSide.revalidate();
	}

	private JPanel createTeamTablePanel() {
		JPanel panel = createTablePanel(createTeam, removeTeam);
		teamTablePanel = new JPanel();
		teamTablePanel.setLayout(new GridLayout());
		
		panel.add(teamTablePanel);
		
		return panel;
	}
	
	private JPanel createSquadTablePanel() {
		JPanel panel = createTablePanel(createSquad, removeSquad, editSquad,
									    createUnits, removeUnits);
		squadTablePanel = new JPanel();
		squadTablePanel.setLayout(new GridLayout());
		
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
	
	private ScaleableGLJPanel createGLPanel() {
		pixelArray = new ArrayList<>();
		
		GLProfile glProfile = GLProfile.getDefault();
		GLCapabilities capabilities = new GLCapabilities(glProfile);
		
		glPanel = new ScaleableGLJPanel(capabilities, pixelArray);
		glPanel.setCellPositionListener(new Consumer<Position>() {

			@Override
			public void accept(Position position) {
				Cell cell = state.getField().get(position);
				
				if (selectedSquad != null) {
					if (isAdding && !cell.hasUnit()) {
						MutablePosition mutablePosition = new MutablePosition(position.getX(), position.getY());
						Unit newUnit = selectedSquad.new Unit(100, new Placement(mutablePosition));
						
						cell.setUnit(newUnit);
						
					} else if (!isAdding && cell.hasUnit()) {
						Unit unit = cell.getUnit();
						
						if (selectedSquad.equalTo(unit.getSquad())) {
							unit.dispose();
							cell.setUnit(null);
						}; 
					}
					
					updatePixelArray(state.getTeams());
					tableManager.update();
					glPanel.display();
				}
			}
			
		});
		
		return glPanel;
	}
	
	private void updatePixelArray(ArrayList<Team> teams) {
		pixelArray.clear();
		
		for (Team team : teams) {
//			MonochromePixels monochromePixels = new MonochromePixels(team.getColor());
//			pixelArray.add(monochromePixels);
			
			for (Squad squad : team) {
				MonochromePixels monochromePixels = new MonochromePixels(squad.getColor());
				pixelArray.add(monochromePixels);
				
				for (Unit unit : squad) {
					monochromePixels.add(unit.getPlacement().getPosition());
				}
			}
		}
	}
}

