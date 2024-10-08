package module.training.ui.renderer;

import core.constants.player.PlayerAbility;
import core.constants.player.PlayerSkill;
import core.gui.theme.HOColorName;
import core.gui.theme.ThemeManager;
import core.model.HOVerwaltung;
import core.model.TranslationFacility;
import core.model.player.Player;
import core.training.WeeklyTrainingType;
import core.util.HOLogger;
import module.training.ui.TrainingLegendPanel;
import module.training.ui.model.TrainingModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.Serial;


/**
 * Renderer for the TrainingRecap Table (Prediction)
 *
 * @author <a href=mailto:draghetto@users.sourceforge.net>Massimiliano Amato</a>
 */
public class TrainingRecapRenderer extends DefaultTableCellRenderer {
    /**
	 *
	 */
    private final TrainingModel trainingModel;
	@Serial
	private static final long serialVersionUID = -4088001127909689247L;

	private static final Color TABLE_BG = ThemeManager.getColor(HOColorName.TABLEENTRY_BG);
	private static final Color SELECTION_BG = ThemeManager.getColor(HOColorName.TABLE_SELECTION_BG);
	private static final Color TABLE_FG = ThemeManager.getColor(HOColorName.TABLEENTRY_FG);
	private static final Color BIRTHDAY_BG = ThemeManager.getColor(HOColorName.TRAINING_BIRTHDAY_BG);
	private static final Color FULL_TRAINING_BG = ThemeManager.getColor(HOColorName.TRAINING_FULL_BG);
	private static final Color PARTIAL_TRAINING_BG = ThemeManager.getColor(HOColorName.TRAINING_PARTIAL_BG);
	private static final Color OSMOSIS_TRAINING_BG = ThemeManager.getColor(HOColorName.TRAINING_OSMOSIS_BG);

	public TrainingRecapRenderer(TrainingModel trainingModel) {
		this.trainingModel = trainingModel;
	}

	//~ Methods ------------------------------------------------------------------------------------

    /* (non-Javadoc)
     * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, Object, boolean, boolean, int, int)
     */
    @Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Reset default values
        this.setForeground(TABLE_FG);
        if (isSelected)
        	this.setBackground(SELECTION_BG);
        else
        	this.setBackground(TABLE_BG);

        String text = null;
        String tooltip = null;
        Icon icon = null;

        try {
        	String s = (String) table.getValueAt(row, column);
            int playerId;

            // fetch playerId (last column) from table
        	playerId = Integer.parseInt((String)table.getValueAt(row, table.getColumnCount()-1));
        	Player player = HOVerwaltung.instance().getModel().getCurrentPlayer(playerId);

        	/* If there is some kind of skillup information
        	 * in the table cell (s) -> extract it
        	 * (it is in the format "SKILLTYPE SKILLLEVEL CHANGE",
        	 * e.g. "3 10.00 1"  for skillup to outstanding playmaking)
			 *      "3 10.99 -1" for skilldrop to outstanding playmaking
        	 */

        	if (s != null && !s.isEmpty()) {
        		String[] skills = s.split(" "); //$NON-NLS-1$
        		var skillType = PlayerSkill.fromInteger(Integer.parseInt(skills[0]));
        		int change = Integer.parseInt((skills[2])); // +1: skillup; -1: skilldrop
        		icon = TrainingLegendPanel.getSkillupTypeIcon(skillType, change);
        		double val = Double.parseDouble(skills[1]);
        		String skillLevelName = PlayerAbility.getNameForSkill(val, true);
        		tooltip = skillType.getLanguageString()+": " + skillLevelName;
        		text = skillLevelName;
        	}

            if (player != null) {
				if (!isSelected && this.trainingModel.getFutureTrainings().size() > column) {
					// Check player's training priority
					var training = this.trainingModel.getFutureTrainings().get(column);
					var	wt = WeeklyTrainingType.instance(training.getTrainingType());
					var prio = player.getFuturePlayerTrainingPriority(wt, training.getTrainingDate());
					if (prio != null) {
						switch (prio) {
							case FULL_TRAINING:
								this.setBackground(FULL_TRAINING_BG);
								break;
							case PARTIAL_TRAINING:
								if (this.trainingModel.isPartialTrainingAvailable(new int [] {column})) {
									this.setBackground(PARTIAL_TRAINING_BG);
								}
								break;
							case OSMOSIS_TRAINING:
								if ( this.trainingModel.isOsmosisTrainingAvailable(new int[]{column})){
									this.setBackground(OSMOSIS_TRAINING_BG);
								}
								break;
						}
					}
				}
            	// Check if player has birthday
            	// every row is an additional week
				var realPlayerAge = player.getAlterWithAgeDays();
            	int calcPlayerAgePrevCol = (int) (realPlayerAge + column*7d/112d);
            	int calcPlayerAgeThisCol = (int) (realPlayerAge + (column+1)*7d/112d);
            	// Birthday in this week! Set BG color
            	if (calcPlayerAgePrevCol < calcPlayerAgeThisCol) {
            		String ageText =  TranslationFacility.tr("ls.player.age.birthday")
    								+ " (" + calcPlayerAgeThisCol + " "
    								+  TranslationFacility.tr("ls.player.age.years")
    								+ ")";

            		if (text == null || text.isEmpty()) {
            			text = ageText;
            		} else {
            			tooltip = "<html>" + tooltip + "<br>" + ageText + "</html>";
            		}
            		if (!isSelected){
						this.setBackground(BIRTHDAY_BG);
					}
            	}
            }

            if (tooltip == null)
            	tooltip = text;

            this.setToolTipText(tooltip);
    		this.setText(text);
    		this.setIcon(icon);
        } catch (Exception e) {
			HOLogger.instance().error(e.getClass(), e);
        }
        return this;
    }
}
