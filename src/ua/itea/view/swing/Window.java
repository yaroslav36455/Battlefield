package ua.itea.view.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.function.Consumer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
	
	private Menu menu;

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
						squadTablePanel.revalidate();
//						squadTablePanel.repaint();

						squadTablePanel.add(squadTable.makeScrollable());
						
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
//						squadTablePanel.revalidate();
						squadTablePanel.repaint();
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
	
	private void resetEnabledButtons() {
		createTeam.setEnabled(true);
		removeTeam.setEnabled(false);
		createSquad.setEnabled(false);;
		removeSquad.setEnabled(false);;
		editSquad.setEnabled(false);;
		createUnits.setEnabled(false);;
		removeUnits.setEnabled(false);;
	}

	private JButton createAddTeamButton() {
		JButton button = new JButton("Add Team");
		
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
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
		menu = menuBar.getMenu();
		
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
		
		menu.addRemoveFieldListener(e->removeSimulation());
		
		menu.addUseTeamColorsListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				updatePixelArray(state.getTeams());
				glPanel.display();
			}
		});
		
		menu.addUseSquadColorsListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				updatePixelArray(state.getTeams());
				glPanel.display();
			}
		});
		
		menu.addExitListeners(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Window.this.dispatchEvent(new WindowEvent(Window.this, WindowEvent.WINDOW_CLOSING));
			}
		});
		
		return menuBar;
	}
	
	private void createSimulation(Size fieldSize) {
		state = new State(new BattleField(fieldSize), new ArrayList<>());
		engine = new Engine(state);
		
		tableManager = createTableManager();
		teamTablePanel.add(tableManager.getTeams().makeScrollable());
		
		placePanels(fieldSize);
		menu.setCreatingSuccess();
	}
	
	private void placePanels(Size fieldSize) {
		placeViewportPanel(fieldSize);
		placeOrganizationPanel();
		resetEnabledButtons();
	}
	
	private void placeOrganizationPanel() {
		orgPanel.setVisible(true);
		orgPanel.getParent().validate();
	}

	private void placeViewportPanel(Size size) {
		viewport = new JScrollPane(glPanel.makeViewport(size.getWidth(), size.getHeight(), 10),
				   				   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				   				   JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		rightSide.add(viewport);
		rightSide.validate();
	}
	
	private void removeSimulation() {
		state.getTeams().clear();
		tableManager.clear();
		
		orgPanel.setVisible(false);
		
		rightSide.removeAll();
		rightSide.repaint();
		
		viewport = null;
		
		teamTablePanel.removeAll();
		squadTablePanel.removeAll();
		
		updatePixelArray(state.getTeams());
		glPanel.display();
		menu.setRemovingSuccess();
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
					
					tableManager.update();
					updatePixelArray(state.getTeams());
					glPanel.display();
				}
			}
			
		});
		
		return glPanel;
	}
	
	private void updatePixelArray(ArrayList<Team> teams) {
		pixelArray.clear();
		
		if (menu.isTeamColors()) {
			for (Team team : teams) {
				MonochromePixels monochromePixels = new MonochromePixels(team.getColor());
				pixelArray.add(monochromePixels);
				
				for (Squad squad : team) {
					
					for (Unit unit : squad) {
						monochromePixels.add(unit.getPlacement().getPosition());
					}
				}
			}
		} else {
			for (Team team : teams) {
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
}

