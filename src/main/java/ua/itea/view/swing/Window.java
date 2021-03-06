package ua.itea.view.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.function.Consumer;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
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
import ua.itea.model.util.Size;

public class Window extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private State state;
	private Engine engine;
	
	private Squad selectedSquad;
	
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
	
	private ScaleableGLJPanel glPanel;
	private JComponent viewport;
	
	private JSplitPane orgPanel;
	
	private JComponent leftSide;
	private JComponent rightSide;
	
	private Menu menu;
	private JLabel statusLabel;

	public Window() {
		super("Battlefield");
	
		JSplitPane splitPane;
		JPanel statusPanel;
		
		setLayout(new BorderLayout());
		
		createTeam = createAddTeamButton();
		removeTeam = createRemoveTeamButton();
		createSquad = createAddSquadButton();
		removeSquad = createRemoveSquadButton();
		editSquad = createEditSquadButton();
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

		Dimension minSize = new Dimension(1000, 500);
		setMinimumSize(minSize);
		setExtendedState(MAXIMIZED_BOTH);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setKeyListeners();
	}
	
	private TableManager createTableManager() {
		TableManager newTableManager = new TableManager();
		
		newTableManager.setColorChanged(this::redraw);
		newTableManager.setTeamRowSelection(this::teamRowSelection);
		newTableManager.setTeamRowUnselection(this::teamRowUnselection);
		newTableManager.setSquadRowSelection(this::squadRowSelection);
		newTableManager.setSquadRowUnselection(this::squadRowUnselection);
		
		return newTableManager;
	}
	
	private void teamRowSelection() {
		removeTeam.setEnabled(true);
		createSquad.setEnabled(true);
		
		squadTablePanel.removeAll();
		squadTablePanel.revalidate();
//		squadTablePanel.repaint();
		
		squadTablePanel.add(tableManager.getSquad().makeScrollable());
	}
	
	private void teamRowUnselection() {
		removeTeam.setEnabled(false);
		createSquad.setEnabled(false);
		
		squadTablePanel.removeAll();
//		squadTablePanel.revalidate();
		squadTablePanel.repaint();
	}
	
	private void squadRowSelection() {
		removeSquad.setEnabled(true);
		editSquad.setEnabled(true);
		
		selectedSquad = tableManager.getSelectedSquad();
	}
	
	private void squadRowUnselection() {
		removeSquad.setEnabled(false);
		editSquad.setEnabled(false);
		
		selectedSquad = null;
	}

	private void setKeyListeners() {
		JRootPane pane = getRootPane();
		InputMap inputMap = pane.getInputMap();
		ActionMap actionMap = pane.getActionMap();
		
		inputMap.put(KeyStroke.getKeyStroke("SPACE"), "iterate");
		actionMap.put("iterate", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				iterate();
			}
		});
	}
	
	private void iterate() {
		if (engine != null && tableManager != null) {
			engine.iterate();
			tableManager.update();
			
			statusLabel.setText(getStatusLabelText());
	
			redraw();
		}
	}
	
	private String getStatusLabelText() {
		return "Iteration:" + state.getIteration();
	}

	private JButton createAddTeamButton() {
		JButton button = new JButton("Add Team");
		
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Team newTeam = new Team();
				
				state.getTeams().add(newTeam);
				tableManager.addTeam(newTeam);
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
				redraw();
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
				redraw();
			}
		});
		
		return button;
	}
	
	private JButton createEditSquadButton() {
		JButton button = new JButton("Edit Stats");
		
		button.addActionListener(new ActionListener() {
			private RequestStatsDialog editSquadDialog;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (editSquadDialog == null) {
					editSquadDialog = new RequestStatsDialog(Window.this);
				}
				
				editSquadDialog.setStatsConsumer(selectedSquad::setStats);
				editSquadDialog.setStats(selectedSquad.getStats());
				editSquadDialog.setVisible(true);
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
					requestSizeDialog.setFieldSize(new Size(50, 50));
				}
				requestSizeDialog.setVisible(true);
			}
		});
		
		menu.addRemoveFieldListener(e->removeSimulation());
		
		menu.addUseTeamColorsListener(e->redraw());
		menu.addUseSquadColorsListener(e->redraw());
		
		menu.addHelpListener(e->JOptionPane.showMessageDialog(this,
				"* Create field\n"
				+ "* Add teams and squads\n"
				+ "* Add units to the field\n"
				+ "  * Select squad\n"
				+ "  * Drag the LMB across the field to add units\n"
				+ "  * Drag the RMB across the field to kill units\n"
				+ "  * Repeat this for different teams\n"
				+ "* Press the SPACE, when on the field there are units"
				+ " of two or more different teams to iterate",
				"Help", JOptionPane.INFORMATION_MESSAGE));
		
		menu.addExitListener(e->dispatchEvent(new WindowEvent(this,
				WindowEvent.WINDOW_CLOSING)));
		
		return menuBar;
	}
	
	private void createSimulation(Size fieldSize) {
		state = new State(new BattleField(fieldSize), new ArrayList<>());
		engine = new Engine(state);
		
		tableManager = createTableManager();
		teamTablePanel.add(tableManager.getTeams().makeScrollable());
		
		placePanels(fieldSize);
		menu.setCreatingSuccess();
		statusLabel.setText(getStatusLabelText());
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
	
	private void resetEnabledButtons() {
		createTeam.setEnabled(true);
		removeTeam.setEnabled(false);
		createSquad.setEnabled(false);
		removeSquad.setEnabled(false);
		editSquad.setEnabled(false);
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
		
		menu.setRemovingSuccess();
		statusLabel.setText("");
		redraw();
	} 

	private JPanel createTeamTablePanel() {
		JPanel panel = createTablePanel(createTeam, removeTeam);
		teamTablePanel = new JPanel();
		teamTablePanel.setLayout(new GridLayout());
		
		panel.add(teamTablePanel);
		
		return panel;
	}
	
	private JPanel createSquadTablePanel() {
		JPanel panel = createTablePanel(createSquad, removeSquad, editSquad);
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
		GLProfile glProfile = GLProfile.getDefault();
		GLCapabilities capabilities = new GLCapabilities(glProfile);
		
		glPanel = new ScaleableGLJPanel(capabilities);
		glPanel.setConsumerForLMB(new Consumer<Point>() {
			
			@Override
			public void accept(Point point) {
				if (selectedSquad != null) {
					Cell cell = state.getField().get(point.x, point.y);
					
					if (!cell.hasUnit()) {
						MutablePosition pos = new MutablePosition(point.x, point.y);
						Unit newUnit = selectedSquad.new Unit(new Placement(pos));
						
						cell.setUnit(newUnit);
					}
					
					tableManager.update();
					redraw();
				}
			}
		});
		
		glPanel.setConsumerForRMB(new Consumer<Point>() {

			@Override
			public void accept(Point point) {
				if (selectedSquad != null) {
					Cell cell = state.getField().get(point.x, point.y);
					
					if (cell.hasUnit()) {
						Unit unit = cell.getUnit();
						
						if (selectedSquad.equalTo(unit.getSquad())) {
							unit.dispose();
							cell.setUnit(null);
						};
					}
					
					tableManager.update();
					redraw();
				}
			}
			
		});
		return glPanel;
	}
	
	private void redraw() {
		glPanel.setPixelArray(getPixelArray(state.getTeams()));
		glPanel.display();
	}
	
	private ArrayList<MonochromePixels> getPixelArray(ArrayList<Team> teams) {
		ArrayList<MonochromePixels> pixelArray = new ArrayList<>();
		
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
		
		return pixelArray;
	}
}

