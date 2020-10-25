package ua.itea.view.swing;

import java.util.ArrayList;
import java.util.function.Consumer;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ua.itea.model.State;

public class TableManager {	
	private State state;
	private OwnTable teams;
	private ArrayList<OwnTable> squads;
	Consumer<OwnTable> teamRowSelection;
	Consumer<OwnTable> teamRowUnselection;
	Consumer<TableRow> squadRowSelection;
	Consumer<TableRow> squadRowUnselection;
	
	public TableManager(State state,
						Consumer<OwnTable> teamRowSelection,
						Consumer<OwnTable> teamRowUnselection,
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
	
	public OwnTable getTeam() {
		return teams;
	}
	
	public OwnTable getSquad(int index) {
		return squads.get(index);
	}
	
	public OwnTable getSquad() {
		return squads.get(teams.getSelectedRow());
	}
	
	public void addSquad() {
		OwnTable currentSquad = squads.get(teams.getSelectedRow());
		currentSquad.add();
	}
	
	public void removeSquad() {
		OwnTable squadTable = squads.get(teams.getSelectedRow());
		
		squadTable.removeRow(squadTable.getSelectedRow());
	}
	
	public void addTeam() {
		squads.add(createSquadTable());
		teams.add();
	}
	
	public void removeTeam() {
		int index = teams.getSelectedRow();
		
		teams.removeRow(index);
		squads.remove(index);
	}
	
	private OwnTable createTeamTable() {
		OwnTable newTable =  new OwnTable();
		
		newTable.getSelectionModel().addListSelectionListener(new TeamListSelectionListener());
		return newTable;
	}
	
	private OwnTable createSquadTable() {
		OwnTable newTable =  new OwnTable();
		
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

		private OwnTable table;
		private Consumer<T> selectionListener;
		private Consumer<T> unselectionListener;
		private IntegerSelector value;
		
		public SquadListSelectionListener(OwnTable table,
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
