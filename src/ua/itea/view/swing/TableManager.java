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
	private State state;
	private FormationTable teams;
	private ArrayList<FormationTable> squads;
	private Consumer<FormationTable> teamRowSelection;
	private Consumer<FormationTable> teamRowUnselection;
	private Consumer<TableRow> squadRowSelection;
	private Consumer<TableRow> squadRowUnselection;
	
	public TableManager(State state,
						Consumer<FormationTable> teamRowSelection,
						Consumer<FormationTable> teamRowUnselection,
						Consumer<TableRow> squadRowSelection,
						Consumer<TableRow> squadRowUnselection) {
		this.state = state;
		this.teams = createTeamTable();
		this.squads = new ArrayList<>();
		this.teamRowSelection = teamRowSelection;
		this.teamRowUnselection = teamRowUnselection;
		this.squadRowSelection = squadRowSelection;
		this.squadRowUnselection = squadRowUnselection;
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
		currentSquad.add(squad);
	}
	
	public Squad removeSquad() {
		FormationTable squadTable = squads.get(teams.getSelectedRow());
		
		return (Squad) squadTable.removeRow(squadTable.getSelectedRow());
	}
	
	public void addTeam(Team team) {
		squads.add(createSquadTable());
		teams.add(team);
	}
	
	public void removeTeam() {
		int index = teams.getSelectedRow();
		
		teams.removeRow(index);
		squads.remove(index);
	}
	
	public Team getSelectedTeam() {
		return (Team) teams.get(teams.getSelectedRow());
	}
	
	public Squad getSelectedSquad() {
		int teamSelectedRow = teams.getSelectedRow();
		int squadSelectedRow = squads.get(teamSelectedRow).getSelectedRow();
		
		FormationTable squadTable = squads.get(squadSelectedRow);
		
		return (Squad) squadTable.get(squadSelectedRow);
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
				teamRowUnselection.accept(squads.get(value.getValue()));
			}
			value.unselect();
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
