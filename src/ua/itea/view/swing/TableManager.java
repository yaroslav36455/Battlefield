package ua.itea.view.swing;

import java.util.ArrayList;
import java.util.function.Consumer;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ua.itea.model.State;
import ua.itea.model.Team;
import ua.itea.model.Team.Squad;

public class TableManager {	
	private FormationTable teams;
	private ArrayList<FormationTable> squads;
	private Consumer<FormationTable> teamRowSelection;
	private Consumer<FormationTable> teamRowUnselection;
	private Consumer<TableRow> squadRowSelection;
	private Consumer<TableRow> squadRowUnselection;
	
	public TableManager(Consumer<FormationTable> teamRowSelection,
						Consumer<FormationTable> teamRowUnselection,
						Consumer<TableRow> squadRowSelection,
						Consumer<TableRow> squadRowUnselection) {
		this.teams = createTeamTable();
		this.squads = new ArrayList<>();
		this.teamRowSelection = teamRowSelection;
		this.teamRowUnselection = teamRowUnselection;
		this.squadRowSelection = squadRowSelection;
		this.squadRowUnselection = squadRowUnselection;
	}
	
	public void update() {
		teams.update();
		
		int selectedTeamRow = teams.getSelectedRow(); 
		System.out.println("selected teamRow " + selectedTeamRow);
			
		if (selectedTeamRow != -1) {
			squads.get(selectedTeamRow).update();
		}
	}
	
	public FormationTable getTeams() {
		return teams;
	}
	
	public FormationTable getSquad(int index) {
		return squads.get(index);
	}
	
	public FormationTable getSquad() {
		return squads.get(teams.getSelectedRow());
	}
	
	public void addSquad(Squad squad) {
		FormationTable currentSquad = squads.get(teams.getSelectedRow());
		currentSquad.add(new SquadTableRow(squad));
	}
	
	public Squad removeSquad() {
		FormationTable squadTable = squads.get(teams.getSelectedRow());
		TableRow tableRow = squadTable.removeRow(squadTable.getSelectedRow());
				
		return ((SquadTableRow) tableRow).getSquad();
	}
	
	public void addTeam(Team team) {
		squads.add(createSquadTable());
		teams.add(new TeamTableRow(team));
	}
	
	public Team removeTeam() {
		int index = teams.getSelectedRow();
		TableRow tableRow = teams.removeRow(index);
		
		squads.remove(index);
		
		return ((TeamTableRow) tableRow).getTeam();
	}
	
	public Team getSelectedTeam() {
		return ((TeamTableRow) teams.getRow(teams.getSelectedRow())).getTeam();
	}
	
	public Squad getSelectedSquad() {
		int teamSelectedRow = teams.getSelectedRow();
		int squadSelectedRow = squads.get(teamSelectedRow).getSelectedRow();
		
		FormationTable squadTable = squads.get(teamSelectedRow);
		
		return ((SquadTableRow) squadTable.getRow(squadSelectedRow)).getSquad();
	}
	
	private FormationTable createTeamTable() {
		FormationTable newTable =  new FormationTable();
		
		newTable.getSelectionModel().addListSelectionListener(new TeamListSelectionListener());
		return newTable;
	}
	
	private FormationTable createSquadTable() {
		FormationTable newTable =  new FormationTable();
		
		SquadListSelectionListener<TableRow> slsl
						= new SquadListSelectionListener<>(newTable,
					    squadRowSelection, squadRowUnselection);
		newTable.getSelectionModel().addListSelectionListener(slsl);
		return newTable;
	}
	
	private class TeamListSelectionListener implements ListSelectionListener {

		private IntegerSelector value = new IntegerSelector();
		
		@Override
		public void valueChanged(ListSelectionEvent e) {
			ListSelectionModel lsm = (ListSelectionModel) e.getSource();
			
			if (!e.getValueIsAdjusting()) {
				if(lsm.isSelectionEmpty()) {
					unselect();
				} else {
					/* Only single selection allowed */
					int index = lsm.getMinSelectionIndex();
					
					if (teams.getTotalRowIndex() == index) {
						unselect();	
					} else {
						select(index);	
					}
				}
			}
		}
		
		private void unselect() {
			if (value.isSelected()) {
				System.out.println("selected: " + value);
				teamRowUnselection.accept(squads.get(value.getValue()));
			}
			value.unselect();
			System.out.println("selected: " + value);
		}
		
		private void select(int index) {
			teamRowSelection.accept(squads.get(index));
			value.select(index);
		}
	}
	
	private static class SquadListSelectionListener<T> implements ListSelectionListener {

		private FormationTable table;
		private Consumer<T> selectionListener;
		private Consumer<T> unselectionListener;
		private IntegerSelector value;
		
		public SquadListSelectionListener(FormationTable table,
										  Consumer<T> selectionListener,
										  Consumer<T> unselectionListener) {
			this.table = table;
			this.selectionListener = selectionListener;
			this.unselectionListener = unselectionListener;
			value = new IntegerSelector();
		}
		
		@Override
		public void valueChanged(ListSelectionEvent e) {
			ListSelectionModel lsm = (ListSelectionModel) e.getSource();
			
			if (!e.getValueIsAdjusting()) {
				if(lsm.isSelectionEmpty()) {
					unselect();
				} else {
					/* Only single selection allowed */
					int index = lsm.getMinSelectionIndex();
					
					if (table.getTotalRowIndex() == index) {
						unselect();	
					} else {
						select(index);	
					}
				}
			}
		}
		
		private void unselect() {
			if (value.isSelected()) {
				unselectionListener.accept(null);
			}
			value.unselect();
		}
		
		private void select(int index) {
			if (!table.isTotalRow(index)) {
				selectionListener.accept(null);
				value.select(index);
			}
		}
	}
}
