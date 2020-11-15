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
	private Runner colorChanged;
	private Runner teamRowSelection;
	private Runner teamRowUnselection;
	private Runner squadRowSelection;
	private Runner squadRowUnselection;
	
	public TableManager() {
		this.teams = createTeamTable();
		this.squads = new ArrayList<>();
	}
	
	public void setColorChanged(Runner colorChanged) {
		this.colorChanged = colorChanged;
	}
	
	public void setTeamRowSelection(Runner teamRowSelection) {
		this.teamRowSelection = teamRowSelection;
	}
	
	public void setTeamRowUnselection(Runner teamRowUnselection) {
		this.teamRowUnselection = teamRowUnselection;
	}
	
	public void setSquadRowSelection(Runner squadRowSelection) {
		this.squadRowSelection = squadRowSelection;
	}
	
	public void setSquadRowUnselection(Runner squadRowUnselection) {
		this.squadRowUnselection = squadRowUnselection;
	}
	
	public void update() {
		teams.update();
		
		int selectedTeamRow = teams.getSelectedRow(); 
			
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
		currentSquad.add(new SquadTableRow(squad, colorChanged));
	}
	
	public Squad removeSquad() {
		FormationTable squadTable = squads.get(teams.getSelectedRow());
		TableRow tableRow = squadTable.removeRow(squadTable.getSelectedRow());
				
		return ((SquadTableRow) tableRow).getSquad();
	}
	
	public void addTeam(Team team) {
		squads.add(createSquadTable());
		teams.add(new TeamTableRow(team, colorChanged));
	}
	
	public Team removeTeam() {
		int index = teams.getSelectedRow();
		TableRow tableRow = teams.removeRow(index);

		squads.remove(index);
		
		return ((TeamTableRow) tableRow).getTeam();
	}
	
	public void clear() {
		teams.clear();
		for (FormationTable squad : squads) {
			squad.clear();
		}
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
	
	public boolean isSelectedSquad() {
		return squads.get(teams.getSelectedRow()).getSelectedRow() != -1;
	}
	
	private FormationTable createTeamTable() {
		FormationTable newTable =  new FormationTable();
		
		newTable.getSelectionModel().addListSelectionListener(new TeamListSelectionListener());
		return newTable;
	}
	
	private FormationTable createSquadTable() {
		FormationTable newTable =  new FormationTable();
		
		SquadListSelectionListener slsl
						= new SquadListSelectionListener(newTable,
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
						lsm.clearSelection();
						unselect();	
					} else {
						select(index);	
					}
				}
			}
		}
		
		private void unselect() {
			if (value.isSelected()) {
//				System.out.println("selected: " + value);
				teamRowUnselection.run();
			}
			value.unselect();
//			System.out.println("selected: " + value);
			squadRowUnselection.run();
		}
		
		private void select(int index) {
			teamRowSelection.run();
			value.select(index);
			
			if (squads.get(index).isSelectedOrdinaryRow()) {
				squadRowSelection.run();
			} else {
				squadRowUnselection.run();
			}
		}
	}
	
	private static class SquadListSelectionListener implements ListSelectionListener {

		private FormationTable table;
		private Runner selectionListener;
		private Runner unselectionListener;
		private IntegerSelector value;
		
		public SquadListSelectionListener(FormationTable table,
										  Runner selectionListener,
										  Runner unselectionListener) {
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
						lsm.clearSelection();
						unselect();	
					} else {
						select(index);	
					}
				}
			}
		}
		
		private void unselect() {
			if (value.isSelected()) {
				unselectionListener.run();
			}
			value.unselect();
		}
		
		private void select(int index) {
			if (!table.isTotalRow(index)) {
				selectionListener.run();
				value.select(index);
			}
		}
	}
}
