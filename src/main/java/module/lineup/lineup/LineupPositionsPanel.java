package module.lineup.lineup;

import core.gui.HOMainFrame;
import core.gui.RefreshManager;
import core.gui.Updatable;
import core.gui.theme.HOIconName;
import core.gui.theme.ImageUtilities;
import core.gui.theme.ThemeManager;
import core.model.HOVerwaltung;
import core.model.TranslationFacility;
import core.model.UserParameter;
import core.model.match.Weather;
import core.model.player.IMatchRoleID;
import core.model.player.Player;
import core.util.HOLogger;
import module.lineup.AllTeamsPanel;
import module.lineup.Lineup;
import module.lineup.LineupPanel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import static core.model.UserParameter.GOALKEEPER_AT_TOP;

/**
 * Create the panel allowing lineup creation
 */
public class LineupPositionsPanel extends core.gui.comp.panel.RasenPanel implements core.gui.Refreshable, Updatable, ActionListener {

	private final LineupPanel m_clLineupPanel;
	//private MatchAndLineupSelectionPanel m_jpMatchAndLineupSelectionPanel;
	//private MatchBanner m_jpMatchBanner;
	private final JButton m_jbFlipSide = new JButton(ThemeManager.getIcon(HOIconName.RELOAD));
	private PlayerPositionPanel m_clKeeper;
	private PlayerPositionPanel m_clLeftWingback;
	private PlayerPositionPanel m_clLeftCentralDefender;
	private PlayerPositionPanel m_clMiddleCentralDefender;
	private PlayerPositionPanel m_clRightCentralDefender;
	private PlayerPositionPanel m_clRightWingback;
	private PlayerPositionPanel m_clLeftWinger;
	private PlayerPositionPanel m_clLeftInnerMidfielder;
	private PlayerPositionPanel m_clCentralInnerMidfielder;
	private PlayerPositionPanel m_clRightInnerMidfielder;
	private PlayerPositionPanel m_clRightWinger;
	private PlayerPositionPanel m_clLeftForward;
	private PlayerPositionPanel m_clCentralForward;
	private PlayerPositionPanel m_clRightForward;
	private PlayerPositionPanel m_clSubstKeeper1;
	private PlayerPositionPanel m_clSubstKeeper2;
	private PlayerPositionPanel m_clSubstCentralDefender1;
	private PlayerPositionPanel m_clSubstCentralDefender2;
	private PlayerPositionPanel m_clSubstWingback1;
	private PlayerPositionPanel m_clSubstWingback2;
	private PlayerPositionPanel m_clSubstInnerMidfielder1;
	private PlayerPositionPanel m_clSubstInnerMidfielder2;
	private PlayerPositionPanel m_clSubstForward1;
	private PlayerPositionPanel m_clSubstForward2;
	private PlayerPositionPanel m_clSubstWinger1;
	private PlayerPositionPanel m_clSubstWinger2;
	private PlayerPositionPanel m_clSubstExtra1;
	private PlayerPositionPanel m_clSubstExtra2;
	private PlayerPositionPanel m_clCaptain;
	private PlayerPositionPanel m_clSetPieceTaker;
	private JLayeredPane centerPanel;
	private final SwapPositionsManager swapPositionsManager = new SwapPositionsManager(this);

	private Weather m_weather;
	private final boolean m_useWeatherImpact;

	private int matchMinute;

	public LineupPanel getLineupPanel() {
		return m_clLineupPanel;
	}

	public LineupPositionsPanel(LineupPanel parent, Weather weather, Boolean useWeatherImpact, int matchMinute) {
		m_clLineupPanel = parent;
		this.matchMinute = matchMinute;
		m_weather = weather;
		m_useWeatherImpact = useWeatherImpact;
		initComponents();
		RefreshManager.instance().registerRefreshable(this);
	}

	public JLayeredPane getCenterPanel() {
		return centerPanel;
	}

	@Override
	public final void actionPerformed(java.awt.event.ActionEvent actionEvent) {
		if (actionEvent.getSource().equals(m_jbFlipSide)) {
			HOVerwaltung.instance().getModel().getCurrentLineup().flipSide();
			var panel = HOMainFrame.instance().getLineupPanel();
			if (panel != null) {
				panel.update();
			}
		}
	}

	@Override
	public final void reInit() {
		refresh();
	}

	@Override
	public final void refresh() {

		HOLogger.instance().log(getClass(), "refresh() has been called");

		boolean bGroupFiltered = m_clLineupPanel.isAssistantGroupFilter();
		String sGroup = m_clLineupPanel.getAssistantGroup();
		boolean bSelectedGroupExcluded = m_clLineupPanel.isAssistantSelectedGroupExcluded();
		boolean bExcludeLast = m_clLineupPanel.isAssistantExcludeLastMatch();

		m_weather = m_clLineupPanel.getWeather();
		matchMinute = m_clLineupPanel.getSelectedMatchMinute();

		// All Player Positions Inform First 11
		List<Player> selectedPlayers = new ArrayList<>();
		List<Player> substitutes = new ArrayList<>();
		List<Player> allPlayers = HOVerwaltung.instance().getModel().getCurrentPlayers();
		List<Player> filteredPlayers = new ArrayList<>();
		Lineup lineup = HOVerwaltung.instance().getModel().getCurrentLineup();

		for (Player player : allPlayers) {
			// the first 11
			if (lineup.isPlayerInStartingEleven(player.getPlayerId())) {
				selectedPlayers.add(player);
			} else if (lineup.isSpielerInReserve(player.getPlayerId())) {
				substitutes.add(player);
			}
		}

		// Apply the Group Filter
		for (Player player : allPlayers) {
			if (player.isExternallyRecruitedCoach()) continue;
			// No Filter
			if (!bGroupFiltered || (sGroup.equals(player.getTeamGroup()) && !bSelectedGroupExcluded)
					|| (!sGroup.equals(player.getTeamGroup()) && bSelectedGroupExcluded)) {
				boolean include = true;
				if (bExcludeLast) {
					var matchlineupTeam = HOVerwaltung.instance().getModel().getPreviousLineup();
					if (matchlineupTeam != null) {
						var previousLineup = matchlineupTeam.getLineup();
						if (previousLineup.isPlayerInStartingEleven(player.getPlayerId())) {
							include = false;
						}
					}
				}
				if (include) {
					filteredPlayers.add(player);
				}
			}
		}

		m_clKeeper.refresh(filteredPlayers, selectedPlayers, substitutes, m_weather, m_useWeatherImpact, matchMinute);
		m_clLeftWingback.refresh(filteredPlayers, selectedPlayers, substitutes, m_weather, m_useWeatherImpact, matchMinute);
		m_clLeftCentralDefender.refresh(filteredPlayers, selectedPlayers, substitutes, m_weather, m_useWeatherImpact, matchMinute);
		m_clMiddleCentralDefender.refresh(filteredPlayers, selectedPlayers, substitutes, m_weather, m_useWeatherImpact, matchMinute);
		m_clRightCentralDefender.refresh(filteredPlayers, selectedPlayers, substitutes, m_weather, m_useWeatherImpact, matchMinute);
		m_clRightWingback.refresh(filteredPlayers, selectedPlayers, substitutes, m_weather, m_useWeatherImpact, matchMinute);
		m_clLeftWinger.refresh(filteredPlayers, selectedPlayers, substitutes, m_weather, m_useWeatherImpact, matchMinute);
		m_clLeftInnerMidfielder.refresh(filteredPlayers, selectedPlayers, substitutes, m_weather, m_useWeatherImpact, matchMinute);
		m_clCentralInnerMidfielder.refresh(filteredPlayers, selectedPlayers, substitutes, m_weather, m_useWeatherImpact, matchMinute);
		m_clRightInnerMidfielder.refresh(filteredPlayers, selectedPlayers, substitutes, m_weather, m_useWeatherImpact, matchMinute);
		m_clRightWinger.refresh(filteredPlayers, selectedPlayers, substitutes, m_weather, m_useWeatherImpact, matchMinute);
		m_clLeftForward.refresh(filteredPlayers, selectedPlayers, substitutes, m_weather, m_useWeatherImpact, matchMinute);
		m_clCentralForward.refresh(filteredPlayers, selectedPlayers, substitutes, m_weather, m_useWeatherImpact, matchMinute);
		m_clRightForward.refresh(filteredPlayers, selectedPlayers, substitutes, m_weather, m_useWeatherImpact, matchMinute);
		m_clSubstKeeper1.refresh(filteredPlayers, selectedPlayers, substitutes, m_weather, m_useWeatherImpact, matchMinute);
		m_clSubstKeeper2.refresh2(filteredPlayers, m_clSubstKeeper1.getiSelectedPlayerId(), m_weather, m_useWeatherImpact);
		m_clSubstCentralDefender1.refresh(filteredPlayers, selectedPlayers, substitutes, m_weather, m_useWeatherImpact, matchMinute);
		m_clSubstCentralDefender2.refresh2(filteredPlayers, m_clSubstCentralDefender1.getiSelectedPlayerId(), m_weather, m_useWeatherImpact);
		m_clSubstWingback1.refresh(filteredPlayers, selectedPlayers, substitutes, m_weather, m_useWeatherImpact, matchMinute);
		m_clSubstWingback2.refresh2(filteredPlayers, m_clSubstWingback1.getiSelectedPlayerId(), m_weather, m_useWeatherImpact);
		m_clSubstInnerMidfielder1.refresh(filteredPlayers, selectedPlayers, substitutes, m_weather, m_useWeatherImpact, matchMinute);
		m_clSubstInnerMidfielder2.refresh2(filteredPlayers, m_clSubstInnerMidfielder1.getiSelectedPlayerId(), m_weather, m_useWeatherImpact);
		m_clSubstForward1.refresh(filteredPlayers, selectedPlayers, substitutes, m_weather, m_useWeatherImpact, matchMinute);
		m_clSubstForward2.refresh2(filteredPlayers, m_clSubstForward1.getiSelectedPlayerId(), m_weather, m_useWeatherImpact);
		m_clSubstWinger1.refresh(filteredPlayers, selectedPlayers, substitutes, m_weather, m_useWeatherImpact, matchMinute);
		m_clSubstWinger2.refresh2(filteredPlayers, m_clSubstWinger1.getiSelectedPlayerId(), m_weather, m_useWeatherImpact);
		m_clSubstExtra1.refresh(filteredPlayers, selectedPlayers, substitutes, null, false, matchMinute);
		m_clSubstExtra2.refresh2(filteredPlayers, m_clSubstExtra1.getiSelectedPlayerId(), null, false);
		m_clSetPieceTaker.refresh(selectedPlayers, null, null, m_weather, m_useWeatherImpact, matchMinute);
		m_clCaptain.refresh(selectedPlayers, null, null, null, false, matchMinute);

		// Check
		lineup.checkAufgestellteSpieler();
	}


	@Override
	public final void update() {
		m_clLineupPanel.update();
	}


	private void initComponents() {
		setLayout(new BorderLayout());

		centerPanel = new JLayeredPane();
		centerPanel.setOpaque(false);

		final GridBagLayout layout = new GridBagLayout();
		centerPanel.setLayout(layout);

		final GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.insets = new Insets(3, 3, 3, 3);

		// Keeper
		constraints.gridx = 1 + getLineupColumnNumber(2); //3;
		constraints.gridy = getLineupRowNumber(0);
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		//constraints.anchor = GridBagConstraints.SOUTH;
		m_clKeeper = new PlayerPositionPanel(this, IMatchRoleID.keeper, m_weather, m_useWeatherImpact, matchMinute);
		swapPositionsManager.addSwapCapabilityTo(m_clKeeper);
		layout.setConstraints(m_clKeeper, constraints);
		centerPanel.add(m_clKeeper);

		// WBr ==========================================================================
		constraints.gridx = 1 + getLineupColumnNumber(0);
		constraints.gridy = getLineupRowNumber(1);
		constraints.insets = new Insets(3, 3, 3, 3);
		//constraints.anchor = GridBagConstraints.CENTER;
		m_clRightWingback = new PlayerPositionPanel(this, IMatchRoleID.rightBack, m_weather, m_useWeatherImpact, matchMinute);
		layout.setConstraints(m_clRightWingback, constraints);
		centerPanel.add(m_clRightWingback);
		swapPositionsManager.addSwapCapabilityTo(m_clRightWingback);
		m_clLineupPanel.addToAssistant(m_clRightWingback);

		// Defense line
		constraints.gridx = 1 + getLineupColumnNumber(1); //2;
		m_clRightCentralDefender = new PlayerPositionPanel(this,
				IMatchRoleID.rightCentralDefender, m_weather, m_useWeatherImpact, matchMinute);
		layout.setConstraints(m_clRightCentralDefender, constraints);
		centerPanel.add(m_clRightCentralDefender);
		swapPositionsManager.addSwapCapabilityTo(m_clRightCentralDefender);
		m_clLineupPanel.addToAssistant(m_clRightCentralDefender);

		constraints.gridx = 1 + getLineupColumnNumber(2); //3;
		m_clMiddleCentralDefender = new PlayerPositionPanel(this,
				IMatchRoleID.middleCentralDefender, m_weather, m_useWeatherImpact, matchMinute);
		layout.setConstraints(m_clMiddleCentralDefender, constraints);
		centerPanel.add(m_clMiddleCentralDefender);
		swapPositionsManager.addSwapCapabilityTo(m_clMiddleCentralDefender);
		m_clLineupPanel.addToAssistant(m_clMiddleCentralDefender);

		constraints.gridx = 1 + getLineupColumnNumber(3); //4;
		m_clLeftCentralDefender = new PlayerPositionPanel(this,
				IMatchRoleID.leftCentralDefender, m_weather, m_useWeatherImpact, matchMinute);
		layout.setConstraints(m_clLeftCentralDefender, constraints);
		centerPanel.add(m_clLeftCentralDefender);
		swapPositionsManager.addSwapCapabilityTo(m_clLeftCentralDefender);
		m_clLineupPanel.addToAssistant(m_clLeftCentralDefender);

		constraints.gridx = 1 + getLineupColumnNumber(4);//5;
		m_clLeftWingback = new PlayerPositionPanel(this, IMatchRoleID.leftBack, m_weather, m_useWeatherImpact, matchMinute);
		layout.setConstraints(m_clLeftWingback, constraints);
		centerPanel.add(m_clLeftWingback);
		swapPositionsManager.addSwapCapabilityTo(m_clLeftWingback);
		m_clLineupPanel.addToAssistant(m_clLeftWingback);

		// Midfield Line
		constraints.gridx = 1 + getLineupColumnNumber(0); //1;
		constraints.gridy = getLineupRowNumber(2);
		m_clRightWinger = new PlayerPositionPanel(this, IMatchRoleID.rightWinger, m_weather, m_useWeatherImpact, matchMinute);
		layout.setConstraints(m_clRightWinger, constraints);
		centerPanel.add(m_clRightWinger);
		swapPositionsManager.addSwapCapabilityTo(m_clRightWinger);
		m_clLineupPanel.addToAssistant(m_clRightWinger);

		constraints.gridx = 1 + getLineupColumnNumber(1); //2;
		m_clRightInnerMidfielder = new PlayerPositionPanel(this,
				IMatchRoleID.rightInnerMidfield, m_weather, m_useWeatherImpact, matchMinute);
		layout.setConstraints(m_clRightInnerMidfielder, constraints);
		centerPanel.add(m_clRightInnerMidfielder);
		swapPositionsManager.addSwapCapabilityTo(m_clRightInnerMidfielder);
		m_clLineupPanel.addToAssistant(m_clRightInnerMidfielder);

		constraints.gridx = 1 + getLineupColumnNumber(2); //3;
		m_clCentralInnerMidfielder = new PlayerPositionPanel(this,
				IMatchRoleID.centralInnerMidfield, m_weather, m_useWeatherImpact, matchMinute);
		layout.setConstraints(m_clCentralInnerMidfielder, constraints);
		centerPanel.add(m_clCentralInnerMidfielder);
		swapPositionsManager.addSwapCapabilityTo(m_clCentralInnerMidfielder);
		m_clLineupPanel.addToAssistant(m_clCentralInnerMidfielder);

		constraints.gridx = 1 + getLineupColumnNumber(3); //4;
		m_clLeftInnerMidfielder = new PlayerPositionPanel(this, IMatchRoleID.leftInnerMidfield, m_weather, m_useWeatherImpact, matchMinute);
		layout.setConstraints(m_clLeftInnerMidfielder, constraints);
		centerPanel.add(m_clLeftInnerMidfielder);
		swapPositionsManager.addSwapCapabilityTo(m_clLeftInnerMidfielder);
		m_clLineupPanel.addToAssistant(m_clLeftInnerMidfielder);

		constraints.gridx = 1 + getLineupColumnNumber(4); //5;
		m_clLeftWinger = new PlayerPositionPanel(this, IMatchRoleID.leftWinger, m_weather, m_useWeatherImpact, matchMinute);
		layout.setConstraints(m_clLeftWinger, constraints);
		centerPanel.add(m_clLeftWinger);
		swapPositionsManager.addSwapCapabilityTo(m_clLeftWinger);
		m_clLineupPanel.addToAssistant(m_clLeftWinger);

		// Forward line
		constraints.gridx = 1 + getLineupColumnNumber(1); //2;
		constraints.gridy = getLineupRowNumber(3);
		m_clRightForward = new PlayerPositionPanel(this, IMatchRoleID.rightForward, m_weather, m_useWeatherImpact, matchMinute);
		layout.setConstraints(m_clRightForward, constraints);
		centerPanel.add(m_clRightForward);
		swapPositionsManager.addSwapCapabilityTo(m_clRightForward);
		m_clLineupPanel.addToAssistant(m_clRightForward);

		constraints.gridx = 1 + getLineupColumnNumber(2); //3;
		m_clCentralForward = new PlayerPositionPanel(this, IMatchRoleID.centralForward, m_weather, m_useWeatherImpact, matchMinute);
		layout.setConstraints(m_clCentralForward, constraints);
		centerPanel.add(m_clCentralForward);
		swapPositionsManager.addSwapCapabilityTo(m_clCentralForward);
		m_clLineupPanel.addToAssistant(m_clCentralForward);

		constraints.gridx = 1 + getLineupColumnNumber(3); //4;
		m_clLeftForward = new PlayerPositionPanel(this, IMatchRoleID.leftForward, m_weather, m_useWeatherImpact, matchMinute);
		layout.setConstraints(m_clLeftForward, constraints);
		centerPanel.add(m_clLeftForward);
		swapPositionsManager.addSwapCapabilityTo(m_clLeftForward);
		m_clLineupPanel.addToAssistant(m_clLeftForward);

		// Captain and set pieces
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.SOUTH;
		m_clCaptain = new PlayerPositionPanel(this, IMatchRoleID.captain, null, false, matchMinute);
		m_clCaptain.addCaptainIcon();
		layout.setConstraints(m_clCaptain, constraints);
		centerPanel.add(m_clCaptain);

		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.NORTH;
		m_clSetPieceTaker = new PlayerPositionPanel(this, IMatchRoleID.setPieces, m_weather, m_useWeatherImpact, matchMinute);
		m_clSetPieceTaker.addSetPiecesIcon();
		layout.setConstraints(m_clSetPieceTaker, constraints);
		centerPanel.add(m_clSetPieceTaker);

		// Buttons to allocate team number to players currently on the line-up
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.CENTER;
		centerPanel.add(new AllTeamsPanel(), constraints);

		// Reserves Separation
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 7;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		JLabel label1 = new JLabel(TranslationFacility.tr("moduleLineup.LineupPanel.SubstitutesLabel"), JLabel.CENTER);
		Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
		label1.setBorder(border);
		Font labelFont = label1.getFont();
		label1.setBackground(Color.GRAY);
		label1.setForeground(ImageUtilities.getColorForContrast(Color.GRAY));
		label1.setFont(new Font(labelFont.getName(), Font.BOLD, 15));
		label1.setOpaque(true);
		layout.setConstraints(label1, constraints);
		centerPanel.add(label1);

		// The reserves
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		m_clSubstKeeper1 = new PlayerPositionPanel(this, IMatchRoleID.substGK1, m_weather, m_useWeatherImpact, matchMinute);
		layout.setConstraints(m_clSubstKeeper1, constraints);
		centerPanel.add(m_clSubstKeeper1);
		swapPositionsManager.addSwapCapabilityTo(m_clSubstKeeper1);

		constraints.gridx = 1;
		m_clSubstCentralDefender1 = new PlayerPositionPanel(this, IMatchRoleID.substCD1, m_weather, m_useWeatherImpact, matchMinute);
		layout.setConstraints(m_clSubstCentralDefender1, constraints);
		centerPanel.add(m_clSubstCentralDefender1);
		swapPositionsManager.addSwapCapabilityTo(m_clSubstCentralDefender1);

		constraints.gridx = 2;
		m_clSubstWingback1 = new PlayerPositionPanel(this, IMatchRoleID.substWB1, m_weather, m_useWeatherImpact, matchMinute);
		layout.setConstraints(m_clSubstWingback1, constraints);
		centerPanel.add(m_clSubstWingback1);
		swapPositionsManager.addSwapCapabilityTo(m_clSubstWingback1);

		constraints.gridx = 3;
		m_clSubstInnerMidfielder1 = new PlayerPositionPanel(this, IMatchRoleID.substIM1, m_weather, m_useWeatherImpact, matchMinute);
		layout.setConstraints(m_clSubstInnerMidfielder1, constraints);
		centerPanel.add(m_clSubstInnerMidfielder1);
		swapPositionsManager.addSwapCapabilityTo(m_clSubstInnerMidfielder1);

		constraints.gridx = 4;
		m_clSubstForward1 = new PlayerPositionPanel(this, IMatchRoleID.substFW1, m_weather, m_useWeatherImpact, matchMinute);
		layout.setConstraints(m_clSubstForward1, constraints);
		centerPanel.add(m_clSubstForward1);
		swapPositionsManager.addSwapCapabilityTo(m_clSubstForward1);

		constraints.gridx = 5;
		m_clSubstWinger1 = new PlayerPositionPanel(this, IMatchRoleID.substWI1, m_weather, m_useWeatherImpact, matchMinute);
		layout.setConstraints(m_clSubstWinger1, constraints);
		centerPanel.add(m_clSubstWinger1);
		swapPositionsManager.addSwapCapabilityTo(m_clSubstWinger1);

		constraints.gridx = 6;
		m_clSubstExtra1 = new PlayerPositionPanel(this, IMatchRoleID.substXT1);
		layout.setConstraints(m_clSubstExtra1, constraints);
		centerPanel.add(m_clSubstExtra1);
		swapPositionsManager.addSwapCapabilityTo(m_clSubstExtra1);

		constraints.gridx = 0;
		constraints.gridy = 6;
		m_clSubstKeeper2 = new PlayerPositionPanel(this, IMatchRoleID.substGK2, m_weather, m_useWeatherImpact, matchMinute);
		layout.setConstraints(m_clSubstKeeper2, constraints);
		centerPanel.add(m_clSubstKeeper2);

		constraints.gridx = 1;
		m_clSubstCentralDefender2 = new PlayerPositionPanel(this, IMatchRoleID.substCD2, m_weather, m_useWeatherImpact, matchMinute);
		layout.setConstraints(m_clSubstCentralDefender2, constraints);
		centerPanel.add(m_clSubstCentralDefender2);

		constraints.gridx = 2;
		m_clSubstWingback2 = new PlayerPositionPanel(this, IMatchRoleID.substWB2, m_weather, m_useWeatherImpact, matchMinute);
		layout.setConstraints(m_clSubstWingback2, constraints);
		centerPanel.add(m_clSubstWingback2);

		constraints.gridx = 3;
		m_clSubstInnerMidfielder2 = new PlayerPositionPanel(this, IMatchRoleID.substIM2, m_weather, m_useWeatherImpact, matchMinute);
		layout.setConstraints(m_clSubstInnerMidfielder2, constraints);
		centerPanel.add(m_clSubstInnerMidfielder2);

		constraints.gridx = 4;
		m_clSubstForward2 = new PlayerPositionPanel(this, IMatchRoleID.substFW2, m_weather, m_useWeatherImpact, matchMinute);
		layout.setConstraints(m_clSubstForward2, constraints);
		centerPanel.add(m_clSubstForward2);

		constraints.gridx = 5;
		m_clSubstWinger2 = new PlayerPositionPanel(this, IMatchRoleID.substWI2, m_weather, m_useWeatherImpact, matchMinute);
		layout.setConstraints(m_clSubstWinger2, constraints);
		centerPanel.add(m_clSubstWinger2);

		constraints.gridx = 6;
		m_clSubstExtra2 = new PlayerPositionPanel(this, IMatchRoleID.substXT2);
		layout.setConstraints(m_clSubstExtra2, constraints);
		centerPanel.add(m_clSubstExtra2);

		add(centerPanel, BorderLayout.CENTER);

	}

	/**
	 * swap the lineup row numbers, if goalkeeper should be displayed at the bottom
	 *
	 * @param i 0, goalkeeper at the top (no swap)
	 *          1, goalkeeper at the bottom (swap)
	 * @return int
	 */
	private int getLineupRowNumber(int i) {
		if (UserParameter.instance().lineupOrientationSetting == GOALKEEPER_AT_TOP) return i;
		return 3 - i;
	}

	/**
	 * swap the lineup column numbers, if goalkeeper should be displayed at the bottom
	 *
	 * @param i 0, goalkeeper at the top (no swap)
	 *          1, goalkeeper at the bottom (swap)
	 * @return int
	 */
	private int getLineupColumnNumber(int i) {
		if (UserParameter.instance().lineupOrientationSetting == GOALKEEPER_AT_TOP) return i;
		return 4 - i;
	}

	public ArrayList<PlayerPositionPanel> getAllPositions() {
		ArrayList<PlayerPositionPanel> pos = new ArrayList<>(14);
		pos.add(m_clCentralForward);
		pos.add(m_clCentralInnerMidfielder);
		pos.add(m_clLeftWingback);
		pos.add(m_clLeftWinger);
		pos.add(m_clLeftCentralDefender);
		pos.add(m_clLeftInnerMidfielder);
		pos.add(m_clLeftForward);
		pos.add(m_clMiddleCentralDefender);
		pos.add(m_clRightWingback);
		pos.add(m_clRightWinger);
		pos.add(m_clRightCentralDefender);
		pos.add(m_clRightInnerMidfielder);
		pos.add(m_clRightForward);
		pos.add(m_clKeeper);
		return pos;
	}

}
