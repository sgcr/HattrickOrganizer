package core.module;

import core.gui.comp.entry.ColorLabelEntry;
import core.gui.comp.renderer.HODefaultTableCellRenderer;
import core.gui.theme.HOIconName;
import core.gui.theme.ThemeManager;
import core.model.TranslationFacility;
import core.module.config.ModuleConfigDialog;
import core.option.OptionManager;
import tool.updater.TableEditor;
import tool.updater.TableModel;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serial;

import static core.gui.theme.HOColorName.DARK_GRAY;


class ModuleConfigPanelTable extends JTable implements ActionListener{
	@Serial
	private static final long serialVersionUID = 1L;
	private static final String[] stateDescriptions = {TranslationFacility.tr("Deactivated"),TranslationFacility.tr("Activated"),TranslationFacility.tr("Autostart")};
	protected String[] columnNames = {TranslationFacility.tr("Status"),TranslationFacility.tr("Name"),TranslationFacility.tr("Optionen")};
	private final TableEditor editor = new TableEditor();

	
	ModuleConfigPanelTable(){
		initialize();
	}

	private void initialize() {
		refresh();
		setRowHeight(25);
		setDefaultRenderer(Object.class, new HODefaultTableCellRenderer());
		getTableHeader().setReorderingAllowed(false);
	}
	
	protected TableModel getTableModel() {
		IModule[] modules = ModuleManager.instance().getTempModules(); 
		Object[][] value = new Object[modules.length][columnNames.length];

		for (int i = 0; i < modules.length; i++) {
			value[i][0] = getComboBox(modules[i]);
			value[i][1] = new ColorLabelEntry(modules[i].getDescription());
			if(modules[i].getStatus() == IModule.STATUS_DEACTIVATED)
				((ColorLabelEntry)value[i][1]).setFGColor(ThemeManager.getColor(DARK_GRAY));
			value[i][2] = modules[i].hasConfigPanel()?getButton(modules[i]):"";
		}

        return new TableModel(value, columnNames);

	}


	private JComboBox getComboBox(IModule module){
		JComboBox cBox = new JComboBox(stateDescriptions);
		cBox.putClientProperty("MODULE", module);
		cBox.setSelectedIndex(module.getStatus());
		cBox.addActionListener(this);
		return cBox;
	}

	private JButton getButton(IModule module) {
		JButton tmp = new JButton(ThemeManager.getIcon(HOIconName.INFO));
		tmp.putClientProperty("MODULE", module);
		//tmp.setOpaque(false);
		tmp.setEnabled(module.getStatus()>IModule.STATUS_DEACTIVATED);
		tmp.addActionListener(this);
		return tmp;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JComboBox box){
			IModule module = (IModule)box.getClientProperty("MODULE");
			int index = box.getSelectedIndex();
			OptionManager.instance().setRestartNeeded();
			module.setStatus(index);
			refresh();
		} else if(e.getSource() instanceof JButton button){
			IModule module = (IModule)button.getClientProperty("MODULE");
			ModuleConfigDialog dialog = new ModuleConfigDialog((JDialog)getTopLevelAncestor(), module);
			dialog.setVisible(true);
		}
		
	}
	
	void refresh(){
		setModel(getTableModel());
		TableColumn c = getColumn(columnNames[0]);
		c.setCellEditor(editor);
		c.setMinWidth(120);
		c.setMaxWidth(150);
		c = getColumn(columnNames[2]);
		c.setCellEditor(editor);
		c.setMinWidth(50);
		c.setMaxWidth(60);
	}

	
}
