package ua.itea.view.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
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
import javax.swing.table.TableModel;

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
	private Size size = new Size(50, 50);
	private ArrayList<MonochromePixels> pixelArray;
	private State state;
	private Engine engine;
	private Squad squadAA;
	private Squad squadBA;
	
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
		editSquad = createEditSquadButton();
		createUnits = createAddUnitsButton();
		removeUnits = createRemoveUnitsButton();
		teamPanel = createTeamTablePanel();
		squadPanel = createSquadTablePanel();

		leftSide = createLeftSide();
		rightSide = createRightSide();
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
		
		tableManager = new TableManager(
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
						
						System.out.println("selection count " + squadTablePanel.getComponentCount());
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
						
						System.out.println("unselection count " + squadTablePanel.getComponentCount());
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
		
		teamTablePanel.setLayout(new GridLayout());
		teamTablePanel.add(tableManager.getTeams().makeScrollable());
		
		squadTablePanel.setLayout(new GridLayout());
		
		setKeyListeners();
		initializeSimulation();
	}

	private void initializeSimulation() {
		Size size = new Size(50, 50);
		Field<Cell> field = new BattleField(size);
		
		Team teamA = new Team();
		Team teamB = new Team();
		
		squadAA = teamA.new Squad();
		squadBA = teamB.new Squad();
		
		squadAA.setColor(new Color(1.f, 0.f, 0.f));
		squadBA.setColor(new Color(0.f, 1.f, 1.f));
		
		teamA.setColor(squadAA.getColor());
		teamB.setColor(squadBA.getColor());
		
		for (int i = 0; i < 40; i++) {
			MutablePosition position = null;
			do {
				position = new MutablePosition((int) (Math.random() * 10),
											   (int) (Math.random() * 10) + 40);
			} while (field.get(position).hasUnit());
			
			Unit newUnit = squadAA.new Unit(100, new Placement(position));
			field.get(position).setUnit(newUnit);
		}
		
		for (int i = 0; i < 40; i++) {
			MutablePosition position = null;
			do {
				position = new MutablePosition((int) (Math.random() * 10) + 40,
											   (int) (Math.random() * 10));
			} while (field.get(position).hasUnit());
			
			Unit newUnit = squadAA.new Unit(100, new Placement(position));
			field.get(position).setUnit(newUnit);
		}

		ArrayList<Team> teams = new ArrayList<Team>();
		teams.add(teamA);
		teams.add(teamB);
		
		state = new State(field, teams);
		engine = new Engine(state);
		
		for(Team team : state.getTeams()) {
			tableManager.addTeam(team);
			for (Squad squad : team) {
				tableManager.addSquad(squad);
			}
		}
		
		updatePixelArray(state.getTeams());
		glPanel.display();
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
					System.out.println("iteraterd");
					
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
				System.out.println("unimplemented createAddUnitsButton()");
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
				System.out.println("unimplemented createRemoveUnitsButton()");
				isAdding = false;
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
		JPanel panel = createTablePanel(createSquad, removeSquad, editSquad,
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
	
	private JComponent createRightSide() {
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
		
		return new JScrollPane(glPanel.makeViewport(size.getWidth(), size.getHeight(), 10),
							   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
							   JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}
	
	private void updatePixelArray(ArrayList<Team> teams) {
		pixelArray.clear();
		
		for (Team team : teams) {
			MonochromePixels monochromePixels = new MonochromePixels(team.getColor());
			
			pixelArray.add(monochromePixels);
			for (Squad squad : team) {
				for (Unit unit : squad) {
					monochromePixels.add(unit.getPlacement().getPosition());
				}
			}
		}
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
