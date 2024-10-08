// %1374340947:de.hattrickorganizer.gui.matches%
package module.matches;

import core.constants.player.*;
import core.db.DBManager;
import core.gui.comp.entry.ColorLabelEntry;
import core.gui.comp.entry.DoubleLabelEntries;
import core.gui.comp.entry.RatingTableEntry;
import core.gui.comp.panel.ImagePanel;
import core.gui.theme.HOIconName;
import core.gui.theme.ImageUtilities;
import core.gui.theme.ThemeManager;
import core.model.HOVerwaltung;
import core.model.TranslationFacility;
import core.model.UserParameter;
import core.model.match.MatchLineup;
import core.model.match.MatchLineupPosition;
import core.model.player.IMatchRoleID;
import core.model.player.MatchRoleID;
import core.model.player.Player;
import core.util.HOLogger;
import core.util.Helper;
import module.playerOverview.PlayerStatusLabelEntry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serial;
import java.text.NumberFormat;

/**
 * Zeigt Details zu einem Player zu einer Zeit an
 */
final class SpielerDetailDialog extends JDialog {

	@Serial
	private static final long serialVersionUID = 7104209757847006926L;
	private final Dimension COMPONENTENSIZE3 = new Dimension(Helper.calcCellWidth(100),
			Helper.calcCellWidth(18));
	private final Dimension COMPONENTENSIZE4 = new Dimension(Helper.calcCellWidth(50),
			Helper.calcCellWidth(18));
	private final ColorLabelEntry m_jpAggressivitaet = new ColorLabelEntry("",
			ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_STANDARD, SwingConstants.LEFT);
	private final ColorLabelEntry m_jpAlter = new ColorLabelEntry("", ColorLabelEntry.FG_STANDARD,
			ColorLabelEntry.BG_STANDARD, SwingConstants.CENTER);
	private final ColorLabelEntry m_jpAnsehen = new ColorLabelEntry("",
			ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_STANDARD, SwingConstants.LEFT);
	private final ColorLabelEntry m_jpAufgestellt = new ColorLabelEntry("",
			ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_STANDARD, SwingConstants.CENTER);
	private final ColorLabelEntry m_jpBestPos = new ColorLabelEntry("",
			ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_STANDARD, SwingConstants.LEFT);
	private final ColorLabelEntry m_jpCharakter = new ColorLabelEntry("",
			ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_STANDARD, SwingConstants.LEFT);
	private final ColorLabelEntry m_jpErfahrung = new ColorLabelEntry("",
			ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_PLAYERSPECIALVALUES, SwingConstants.LEFT);
	private final ColorLabelEntry m_jpErfahrung2 = new ColorLabelEntry("",
			ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_PLAYERSPECIALVALUES,
			SwingConstants.CENTER);
	private final ColorLabelEntry m_jpFluegelspiel = new ColorLabelEntry("",
			ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_SINGLEPLAYERVALUES, SwingConstants.LEFT);
	private final ColorLabelEntry m_jpFluegelspiel2 = new ColorLabelEntry("",
			ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_SINGLEPLAYERVALUES,
			SwingConstants.CENTER);
	private final ColorLabelEntry m_jpForm = new ColorLabelEntry("", ColorLabelEntry.FG_STANDARD,
			ColorLabelEntry.BG_PLAYERSPECIALVALUES, SwingConstants.LEFT);
	private final ColorLabelEntry m_jpForm2 = new ColorLabelEntry("", ColorLabelEntry.FG_STANDARD,
			ColorLabelEntry.BG_PLAYERSPECIALVALUES, SwingConstants.CENTER);
	private final ColorLabelEntry m_jpFuehrung = new ColorLabelEntry("",
			ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_PLAYERSPECIALVALUES, SwingConstants.LEFT);
	private final ColorLabelEntry m_jpFuehrung2 = new ColorLabelEntry("",
			ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_PLAYERSPECIALVALUES,
			SwingConstants.CENTER);
	private final ColorLabelEntry m_jpHattriks = new ColorLabelEntry("",
			ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_STANDARD, SwingConstants.CENTER);
	private final ColorLabelEntry m_jpKondition = new ColorLabelEntry("",
			ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_SINGLEPLAYERVALUES, SwingConstants.LEFT);
	private final ColorLabelEntry m_jpKondition2 = new ColorLabelEntry("",
			ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_SINGLEPLAYERVALUES,
			SwingConstants.CENTER);
	private final ColorLabelEntry m_jpName = new ColorLabelEntry("", ColorLabelEntry.FG_STANDARD,
			ColorLabelEntry.BG_STANDARD, SwingConstants.LEFT);
	private final ColorLabelEntry m_jpNationalitaet = new ColorLabelEntry("",
			ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_STANDARD, SwingConstants.CENTER);
	private final ColorLabelEntry m_jpPasspiel = new ColorLabelEntry("",
			ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_SINGLEPLAYERVALUES, SwingConstants.LEFT);
	private final ColorLabelEntry m_jpPasspiel2 = new ColorLabelEntry("",
			ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_SINGLEPLAYERVALUES,
			SwingConstants.CENTER);
	private final ColorLabelEntry m_jpSpezialitaet = new ColorLabelEntry("",
			ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_STANDARD, SwingConstants.LEFT);
	private final ColorLabelEntry m_jpSpielaufbau = new ColorLabelEntry("",
			ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_SINGLEPLAYERVALUES, SwingConstants.LEFT);
	private final ColorLabelEntry m_jpSpielaufbau2 = new ColorLabelEntry("",
			ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_SINGLEPLAYERVALUES,
			SwingConstants.CENTER);
	private final ColorLabelEntry m_jpStandards = new ColorLabelEntry("",
			ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_SINGLEPLAYERVALUES, SwingConstants.LEFT);
	private final ColorLabelEntry m_jpStandards2 = new ColorLabelEntry("",
			ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_SINGLEPLAYERVALUES,
			SwingConstants.CENTER);
	private final ColorLabelEntry m_jpToreFreund = new ColorLabelEntry("",
			ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_STANDARD, SwingConstants.CENTER);
	private final ColorLabelEntry m_jpToreGesamt = new ColorLabelEntry("",
			ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_STANDARD, SwingConstants.CENTER);
	private final ColorLabelEntry m_jpToreLiga = new ColorLabelEntry("",
			ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_STANDARD, SwingConstants.CENTER);
	private final ColorLabelEntry m_jpTorePokal = new ColorLabelEntry("",
			ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_STANDARD, SwingConstants.CENTER);
	private final ColorLabelEntry m_jpTorschuss = new ColorLabelEntry("",
			ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_SINGLEPLAYERVALUES, SwingConstants.LEFT);
	private final ColorLabelEntry m_jpTorschuss2 = new ColorLabelEntry("",
			ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_SINGLEPLAYERVALUES,
			SwingConstants.CENTER);
	private final ColorLabelEntry m_jpTorwart = new ColorLabelEntry("",
			ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_SINGLEPLAYERVALUES, SwingConstants.LEFT);
	private final ColorLabelEntry m_jpTorwart2 = new ColorLabelEntry("",
			ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_SINGLEPLAYERVALUES,
			SwingConstants.CENTER);
	private final ColorLabelEntry m_jpVerteidigung = new ColorLabelEntry("",
			ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_SINGLEPLAYERVALUES, SwingConstants.LEFT);
	private final ColorLabelEntry m_jpVerteidigung2 = new ColorLabelEntry("",
			ColorLabelEntry.FG_STANDARD, ColorLabelEntry.BG_SINGLEPLAYERVALUES,
			SwingConstants.CENTER);
	private final DoubleLabelEntries m_jpGehalt = new DoubleLabelEntries(ColorLabelEntry.BG_STANDARD);
	private final DoubleLabelEntries m_jpGruppeSmilie = new DoubleLabelEntries(
			ColorLabelEntry.BG_STANDARD);
	private final DoubleLabelEntries m_jpMartwert = new DoubleLabelEntries(ColorLabelEntry.BG_STANDARD);
	private final DoubleLabelEntries m_jpWertAussenVert = new DoubleLabelEntries(
			ColorLabelEntry.BG_PLAYERSPOSITIONVALUES);
	private final DoubleLabelEntries m_jpWertAussenVertDef = new DoubleLabelEntries(
			ColorLabelEntry.BG_PLAYERSSUBPOSITIONVALUES);
	private final DoubleLabelEntries m_jpWertAussenVertIn = new DoubleLabelEntries(
			ColorLabelEntry.BG_PLAYERSSUBPOSITIONVALUES);
	private final DoubleLabelEntries m_jpWertAussenVertOff = new DoubleLabelEntries(
			ColorLabelEntry.BG_PLAYERSSUBPOSITIONVALUES);
	private final DoubleLabelEntries m_jpWertFluegel = new DoubleLabelEntries(
			ColorLabelEntry.BG_PLAYERSPOSITIONVALUES);
	private final DoubleLabelEntries m_jpWertFluegelDef = new DoubleLabelEntries(
			ColorLabelEntry.BG_PLAYERSSUBPOSITIONVALUES);
	private final DoubleLabelEntries m_jpWertFluegelIn = new DoubleLabelEntries(
			ColorLabelEntry.BG_PLAYERSSUBPOSITIONVALUES);
	private final DoubleLabelEntries m_jpWertFluegelOff = new DoubleLabelEntries(
			ColorLabelEntry.BG_PLAYERSSUBPOSITIONVALUES);
	private final DoubleLabelEntries m_jpWertInnenVert = new DoubleLabelEntries(
			ColorLabelEntry.BG_PLAYERSPOSITIONVALUES);
	private final DoubleLabelEntries m_jpWertInnenVertAus = new DoubleLabelEntries(
			ColorLabelEntry.BG_PLAYERSSUBPOSITIONVALUES);
	private final DoubleLabelEntries m_jpWertInnenVertOff = new DoubleLabelEntries(
			ColorLabelEntry.BG_PLAYERSSUBPOSITIONVALUES);
	private final DoubleLabelEntries m_jpWertMittelfeld = new DoubleLabelEntries(
			ColorLabelEntry.BG_PLAYERSPOSITIONVALUES);
	private final DoubleLabelEntries m_jpWertMittelfeldAus = new DoubleLabelEntries(
			ColorLabelEntry.BG_PLAYERSSUBPOSITIONVALUES);
	private final DoubleLabelEntries m_jpWertMittelfeldDef = new DoubleLabelEntries(
			ColorLabelEntry.BG_PLAYERSSUBPOSITIONVALUES);
	private final DoubleLabelEntries m_jpWertMittelfeldOff = new DoubleLabelEntries(
			ColorLabelEntry.BG_PLAYERSSUBPOSITIONVALUES);
	private final DoubleLabelEntries m_jpWertSturmAus = new DoubleLabelEntries(
			ColorLabelEntry.BG_PLAYERSSUBPOSITIONVALUES);
	private final DoubleLabelEntries m_jpWertSturm = new DoubleLabelEntries(
			ColorLabelEntry.BG_PLAYERSPOSITIONVALUES);
	private final DoubleLabelEntries m_jpWertSturmDef = new DoubleLabelEntries(
			ColorLabelEntry.BG_PLAYERSSUBPOSITIONVALUES);
	private final DoubleLabelEntries m_jpWertTor = new DoubleLabelEntries(
			ColorLabelEntry.BG_PLAYERSPOSITIONVALUES);
	private final RatingTableEntry m_jpAktuellRating = new RatingTableEntry();
	private final RatingTableEntry m_jpRating = new RatingTableEntry();
	private final PlayerStatusLabelEntry m_jpStatus = new PlayerStatusLabelEntry();

	private final DoubleLabelEntries[] playerPositionValues = new DoubleLabelEntries[]{m_jpWertTor,
			m_jpWertInnenVert, m_jpWertInnenVertAus, m_jpWertInnenVertOff, m_jpWertAussenVert,
			m_jpWertAussenVertIn, m_jpWertAussenVertOff, m_jpWertAussenVertDef, m_jpWertMittelfeld,
			m_jpWertMittelfeldAus, m_jpWertMittelfeldOff, m_jpWertMittelfeldDef, m_jpWertFluegel,
			m_jpWertFluegelIn, m_jpWertFluegelOff, m_jpWertFluegelDef, m_jpWertSturm,
			m_jpWertSturmAus, m_jpWertSturmDef};

	private final byte[] playerPosition = new byte[]{IMatchRoleID.KEEPER,
			IMatchRoleID.CENTRAL_DEFENDER, IMatchRoleID.CENTRAL_DEFENDER_TOWING,
			IMatchRoleID.CENTRAL_DEFENDER_OFF, IMatchRoleID.BACK,
			IMatchRoleID.BACK_TOMID, IMatchRoleID.BACK_OFF, IMatchRoleID.BACK_DEF,
			IMatchRoleID.MIDFIELDER, IMatchRoleID.MIDFIELDER_TOWING,
			IMatchRoleID.MIDFIELDER_OFF, IMatchRoleID.MIDFIELDER_DEF,
			IMatchRoleID.WINGER, IMatchRoleID.WINGER_TOMID, IMatchRoleID.WINGER_OFF,
			IMatchRoleID.WINGER_DEF, IMatchRoleID.FORWARD, IMatchRoleID.FORWARD_TOWING,
			IMatchRoleID.FORWARD_DEF

	};

	public SpielerDetailDialog(JFrame owner, MatchLineupPosition matchplayer, MatchLineup matchlineup) {
		super(owner);
		HOLogger.instance().log(getClass(), "SpielerDetailDialog");
		Player player = DBManager.instance().getSpielerAtDate(matchplayer.getPlayerId(),
				matchlineup.getMatchDate().toDbTimestamp());

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		// Nicht gefunden
		if (player == null) {
			Helper.showMessage(owner,
					TranslationFacility.tr("Fehler_Spielerdetails"),
					TranslationFacility.tr("Fehler"), JOptionPane.ERROR_MESSAGE);
			return;
		}

		HOLogger.instance().log(getClass(), "Show Player: " + player.getFullName());

		setTitle(player.getFullName() + " (" + player.getPlayerId() + ")");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				UserParameter.instance().spielerDetails_PositionX = getLocation().x;
				UserParameter.instance().spielerDetails_PositionY = getLocation().y;
			}
		});

		initComponents(player, matchplayer);

		m_jpRating.setRating((float) matchplayer.getRating() * 2, true);
		m_jpAktuellRating.setRating(DBManager.instance().getLetzteBewertung4Spieler(
				player.getPlayerId()));
		setLabels(player);

		pack();
		setSize(getSize().width + Helper.calcCellWidth(30), getSize().height + 10);
		setLocation(core.model.UserParameter.instance().spielerDetails_PositionX,
				core.model.UserParameter.instance().spielerDetails_PositionY);
		setVisible(true);
	}

	private void setLabels(Player m_clPlayer) {
		Player m_clVergleichsPlayer = HOVerwaltung.instance().getModel()
				.getCurrentPlayer(m_clPlayer.getPlayerId());

		m_jpName.setText(m_clPlayer.getFullName());
		m_jpAlter.setText(m_clPlayer.getAge() + "");
		m_jpNationalitaet.setIcon(ImageUtilities.getCountryFlagIcon(m_clPlayer.getNationalityId()));

		var lineup = HOVerwaltung.instance().getModel().getCurrentLineup();
		if (lineup.isPlayerInLineup(m_clPlayer.getPlayerId())
				&& (lineup.getPositionByPlayerId(m_clPlayer.getPlayerId()) != null)) {
			m_jpAufgestellt.setIcon(ImageUtilities.getImage4Position(
					lineup.getPositionByPlayerId(m_clPlayer.getPlayerId()),
					m_clPlayer.getShirtNumber()));
			m_jpAufgestellt.setText(MatchRoleID.getNameForPosition(lineup.getPositionByPlayerId(m_clPlayer.getPlayerId())
					.getPosition()));
		} else {
			m_jpAufgestellt.setIcon(ImageUtilities.getImage4Position(null,
					m_clPlayer.getShirtNumber()));
			m_jpAufgestellt.setText("");
		}

		m_jpGruppeSmilie.getLeft().setAlignment(SwingConstants.CENTER);
		m_jpGruppeSmilie.getRight().setAlignment(SwingConstants.CENTER);
		m_jpGruppeSmilie.getLeft().setIcon(ThemeManager.getIcon(m_clPlayer.getTeamGroup()));
		m_jpGruppeSmilie.getRight()
				.setIcon(ThemeManager.getIcon(m_clPlayer.getInfoSmiley()));

		m_jpStatus.setPlayer(m_clPlayer);

		if (m_clVergleichsPlayer == null) {
			String bonus = "";
			int gehalt = (int) (m_clPlayer.getWage() / core.model.UserParameter.instance().FXrate);
			String gehalttext = NumberFormat.getCurrencyInstance().format(gehalt);

			if (m_clPlayer.getBonus() > 0) {
				bonus = " (" + m_clPlayer.getBonus() + "% "
						+ TranslationFacility.tr("Bonus") + ")";
			}

			m_jpGehalt.getLeft().setText(gehalttext + bonus);
			m_jpGehalt.getRight().clear();
			m_jpMartwert.getLeft().setText(m_clPlayer.getTsi() + "");
			m_jpMartwert.getRight().clear();
			m_jpForm.setText(PlayerAbility.getNameForSkill(m_clPlayer.getForm()));
			m_jpForm2.clear();
			m_jpKondition.setText(PlayerAbility.getNameForSkill(m_clPlayer.getStamina()));
			m_jpKondition2.clear();
			m_jpTorwart.setText(PlayerAbility.getNameForSkill(m_clPlayer.getGoalkeeperSkill()
                    + m_clPlayer.getSub4Skill(PlayerSkill.KEEPER)));
			m_jpTorwart2.clear();
			m_jpVerteidigung.setText(PlayerAbility.getNameForSkill(m_clPlayer.getDefendingSkill()
                    + m_clPlayer.getSub4Skill(PlayerSkill.DEFENDING)));
			m_jpVerteidigung2.clear();
			m_jpSpielaufbau.setText(PlayerAbility.getNameForSkill(m_clPlayer.getPlaymakingSkill()
                    + m_clPlayer.getSub4Skill(PlayerSkill.PLAYMAKING)));
			m_jpSpielaufbau2.clear();
			m_jpPasspiel.setText(PlayerAbility.getNameForSkill(m_clPlayer.getPassingSkill()
                    + m_clPlayer.getSub4Skill(PlayerSkill.PASSING)));
			m_jpPasspiel2.clear();
			m_jpFluegelspiel.setText(PlayerAbility.getNameForSkill(m_clPlayer.getWingerSkill()
                    + m_clPlayer.getSub4Skill(PlayerSkill.WINGER)));
			m_jpFluegelspiel2.clear();
			m_jpStandards.setText(PlayerAbility.getNameForSkill(m_clPlayer.getSetPiecesSkill()
                    + m_clPlayer.getSub4Skill(PlayerSkill.SETPIECES)));
			m_jpStandards2.clear();
			m_jpTorschuss.setText(PlayerAbility.getNameForSkill(m_clPlayer.getScoringSkill()
                    + m_clPlayer.getSub4Skill(PlayerSkill.SCORING)));
			m_jpTorschuss2.clear();
			m_jpErfahrung.setText(PlayerAbility.getNameForSkill(m_clPlayer.getExperience()));
			m_jpErfahrung2.clear();
			m_jpFuehrung.setText(PlayerAbility.getNameForSkill(m_clPlayer.getLeadership()));
			m_jpFuehrung2.clear();
			m_jpBestPos.setText(MatchRoleID.getNameForPosition(m_clPlayer.getIdealPosition())
					+ " (" + m_clPlayer.getIdealPositionRating() + ")");
			for (int i = 0; i < playerPositionValues.length; i++) {
				showNormal(playerPositionValues[i], playerPosition[i], m_clPlayer);
			}
		} else {
			String bonus = "";
			int gehalt = (int) (m_clPlayer.getWage() / core.model.UserParameter.instance().FXrate);
			int gehalt2 = (int) (m_clVergleichsPlayer.getWage() / core.model.UserParameter
					.instance().FXrate);
			String gehalttext = NumberFormat.getCurrencyInstance().format(gehalt);

			if (m_clPlayer.getBonus() > 0) {
				bonus = " (" + m_clPlayer.getBonus() + "% "
						+ TranslationFacility.tr("Bonus") + ")";
			}

			m_jpGehalt.getLeft().setText(gehalttext + bonus);
			m_jpGehalt.getRight().setSpecialNumber((gehalt2 - gehalt), true);
			m_jpMartwert.getLeft().setText(m_clPlayer.getTsi() + "");
			m_jpMartwert.getRight().setSpecialNumber(
					(m_clVergleichsPlayer.getTsi() - m_clPlayer.getTsi()), false);
			m_jpForm.setText(PlayerAbility.getNameForSkill(m_clPlayer.getForm()));
			m_jpForm2.setGraphicalChangeValue(
					m_clVergleichsPlayer.getForm() - m_clPlayer.getForm(), !m_clPlayer.isGoner(),
					true);
			m_jpKondition.setText(PlayerAbility.getNameForSkill(m_clPlayer.getStamina()));
			m_jpKondition2.setGraphicalChangeValue(m_clVergleichsPlayer.getStamina()
					- m_clPlayer.getStamina(), !m_clVergleichsPlayer.isGoner(), true);
			m_jpTorwart.setText(PlayerAbility.getNameForSkill(m_clPlayer.getGoalkeeperSkill()
                    + m_clPlayer.getSub4Skill(PlayerSkill.KEEPER)));
			m_jpTorwart2.setGraphicalChangeValue(
					m_clVergleichsPlayer.getGoalkeeperSkill() - m_clPlayer.getGoalkeeperSkill(),
					m_clVergleichsPlayer.getSub4Skill(PlayerSkill.KEEPER)
							- m_clPlayer.getSub4Skill(PlayerSkill.KEEPER),
					!m_clVergleichsPlayer.isGoner(), true);
			m_jpVerteidigung.setText(PlayerAbility.getNameForSkill(m_clPlayer.getDefendingSkill()
                    + m_clPlayer.getSub4Skill(PlayerSkill.DEFENDING)));
			m_jpVerteidigung2.setGraphicalChangeValue(
					m_clVergleichsPlayer.getDefendingSkill() - m_clPlayer.getDefendingSkill(),
					m_clVergleichsPlayer.getSub4Skill(PlayerSkill.DEFENDING)
							- m_clPlayer.getSub4Skill(PlayerSkill.DEFENDING),
					!m_clVergleichsPlayer.isGoner(), true);
			m_jpSpielaufbau.setText(PlayerAbility.getNameForSkill(m_clPlayer.getPlaymakingSkill()
                    + m_clPlayer.getSub4Skill(PlayerSkill.PLAYMAKING)));
			m_jpSpielaufbau2.setGraphicalChangeValue(
					m_clVergleichsPlayer.getPlaymakingSkill() - m_clPlayer.getPlaymakingSkill(),
					m_clVergleichsPlayer.getSub4Skill(PlayerSkill.PLAYMAKING)
							- m_clPlayer.getSub4Skill(PlayerSkill.PLAYMAKING),
					!m_clVergleichsPlayer.isGoner(), true);
			m_jpPasspiel.setText(PlayerAbility.getNameForSkill(m_clPlayer.getPassingSkill()
                    + m_clPlayer.getSub4Skill(PlayerSkill.PASSING)));
			m_jpPasspiel2.setGraphicalChangeValue(
					m_clVergleichsPlayer.getPassingSkill() - m_clPlayer.getPassingSkill(),
					m_clVergleichsPlayer.getSub4Skill(PlayerSkill.PASSING)
							- m_clPlayer.getSub4Skill(PlayerSkill.PASSING),
					!m_clVergleichsPlayer.isGoner(), true);
			m_jpFluegelspiel.setText(PlayerAbility.getNameForSkill(m_clPlayer.getWingerSkill()
                    + m_clPlayer.getSub4Skill(PlayerSkill.WINGER)));
			m_jpFluegelspiel2.setGraphicalChangeValue(
					m_clVergleichsPlayer.getWingerSkill() - m_clPlayer.getWingerSkill(),
					m_clVergleichsPlayer.getSub4Skill(PlayerSkill.WINGER)
							- m_clPlayer.getSub4Skill(PlayerSkill.WINGER),
					!m_clVergleichsPlayer.isGoner(), true);
			m_jpStandards.setText(PlayerAbility.getNameForSkill(m_clPlayer.getSetPiecesSkill()
                    + m_clPlayer.getSub4Skill(PlayerSkill.SETPIECES)));
			m_jpStandards2.setGraphicalChangeValue(
					m_clVergleichsPlayer.getSetPiecesSkill() - m_clPlayer.getSetPiecesSkill(),
					m_clVergleichsPlayer.getSub4Skill(PlayerSkill.SETPIECES)
							- m_clPlayer.getSub4Skill(PlayerSkill.SETPIECES),
					!m_clVergleichsPlayer.isGoner(), true);
			m_jpTorschuss.setText(PlayerAbility.getNameForSkill(m_clPlayer.getScoringSkill()
                    + m_clPlayer.getSub4Skill(PlayerSkill.SCORING)));
			m_jpTorschuss2.setGraphicalChangeValue(
					m_clVergleichsPlayer.getScoringSkill() - m_clPlayer.getScoringSkill(),
					m_clVergleichsPlayer.getSub4Skill(PlayerSkill.SCORING)
							- m_clPlayer.getSub4Skill(PlayerSkill.SCORING),
					!m_clVergleichsPlayer.isGoner(), true);
			m_jpErfahrung.setText(PlayerAbility.getNameForSkill(m_clPlayer.getExperience()));
			m_jpErfahrung2.setGraphicalChangeValue(m_clVergleichsPlayer.getExperience()
					- m_clPlayer.getExperience(), !m_clPlayer.isGoner(), true);
			m_jpFuehrung.setText(PlayerAbility.getNameForSkill(m_clPlayer.getLeadership()));
			m_jpFuehrung2.setGraphicalChangeValue(m_clVergleichsPlayer.getLeadership()
					- m_clPlayer.getLeadership(), !m_clVergleichsPlayer.isGoner(), true);
			m_jpBestPos.setText(MatchRoleID.getNameForPosition(m_clPlayer
					.getIdealPosition())
					+ " ("
					+ m_clPlayer.getIdealPositionRating() + ")");

			for (int i = 0; i < playerPositionValues.length; i++) {
				showWithCompare(playerPositionValues[i], playerPosition[i], m_clPlayer,
						m_clVergleichsPlayer);
			}
		}
		m_jpToreFreund.setText(m_clPlayer.getFriendlyGoals() + "");
		m_jpToreLiga.setText(m_clPlayer.getLeagueGoals() + "");
		m_jpTorePokal.setText(m_clPlayer.getCupGameGoals() + "");
		m_jpToreGesamt.setText(m_clPlayer.getTotalGoals() + "");
		m_jpHattriks.setText(m_clPlayer.getHatTricks() + "");
		m_jpSpezialitaet.setText(PlayerSpeciality.toString(m_clPlayer.getSpecialty()));
		m_jpSpezialitaet.setIcon(ImageUtilities.getSmallPlayerSpecialtyIcon(HOIconName.SPECIALTIES[m_clPlayer.getSpecialty()]));
		m_jpAggressivitaet.setText(PlayerAggressiveness.toString(m_clPlayer.getAggressivity()));

		// Dreher!
		m_jpAnsehen.setText(PlayerAgreeability.toString(m_clPlayer.getGentleness()));
		m_jpCharakter.setText(PlayerHonesty.toString(m_clPlayer.getHonesty()));

	}

	private void initComponents(Player player, MatchLineupPosition matchplayer) {
		JComponent component;

		getContentPane().setLayout(new BorderLayout());

		JPanel panel = new ImagePanel();
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();

		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 0.0;
		constraints.weighty = 1.0;
		constraints.insets = new Insets(1, 2, 1, 1);
		panel.setLayout(layout);

		JLabel label;

		// Leerzeile
		label = new JLabel("  ");
		constraints.gridx = 3;
		constraints.weightx = 0.0;
		constraints.gridy = 0;
		constraints.gridheight = 11;
		layout.setConstraints(label, constraints);
		panel.add(label);
		constraints.gridheight = 1;

		label = new JLabel(TranslationFacility.tr("ls.player.name"));
		constraints.gridx = 0;
		constraints.weightx = 0.0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		layout.setConstraints(label, constraints);
		panel.add(label);
		constraints.gridx = 1;
		constraints.weightx = 1.0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		component = m_jpName.getComponent(false);
		layout.setConstraints(component, constraints);
		panel.add(component);

		label = new JLabel(TranslationFacility.tr("ls.player.age"));
		constraints.gridx = 0;
		constraints.weightx = 0.0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		layout.setConstraints(label, constraints);
		panel.add(label);
		constraints.gridx = 1;
		constraints.weightx = 1.0;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		component = m_jpAlter.getComponent(false);
		layout.setConstraints(component, constraints);
		panel.add(component);

		label = new JLabel(TranslationFacility.tr("ls.player.nationality"));
		constraints.gridx = 0;
		constraints.weightx = 0.0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		layout.setConstraints(label, constraints);
		panel.add(label);
		constraints.gridx = 1;
		constraints.weightx = 1.0;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		component = m_jpNationalitaet.getComponent(false);
		layout.setConstraints(component, constraints);
		panel.add(component);

		label = new JLabel(TranslationFacility.tr("Aufgestellt"));
		constraints.gridx = 0;
		constraints.weightx = 0.0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		layout.setConstraints(label, constraints);
		panel.add(label);
		constraints.gridx = 1;
		constraints.weightx = 1.0;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		component = m_jpAufgestellt.getComponent(false);
		layout.setConstraints(component, constraints);
		panel.add(component);

		label = new JLabel(TranslationFacility.tr("Aktuell") + " "
				+ TranslationFacility.tr("Rating"));
		constraints.gridx = 0;
		constraints.weightx = 0.0;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		layout.setConstraints(label, constraints);
		panel.add(label);
		constraints.gridx = 1;
		constraints.weightx = 1.0;
		constraints.gridy = 4;
		constraints.gridwidth = 2;
		component = m_jpAktuellRating.getComponent(false);
		layout.setConstraints(component, constraints);
		panel.add(component);

		label = new JLabel(TranslationFacility.tr("BestePosition"));
		constraints.gridx = 0;
		constraints.weightx = 0.0;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		layout.setConstraints(label, constraints);
		panel.add(label);
		constraints.gridx = 1;
		constraints.weightx = 1.0;
		constraints.gridy = 5;
		constraints.gridwidth = 2;
		component = m_jpBestPos.getComponent(false);
		layout.setConstraints(component, constraints);
		panel.add(component);

		label = new JLabel(TranslationFacility.tr("Gruppe"));
		constraints.gridx = 4;
		constraints.weightx = 0.0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		layout.setConstraints(label, constraints);
		panel.add(label);

		component = m_jpGruppeSmilie.getComponent(false);
		constraints.gridx = 5;
		constraints.weightx = 1.0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;

		layout.setConstraints(component, constraints);
		panel.add(component);

		label = new JLabel(TranslationFacility.tr("Status"));
		constraints.gridx = 4;
		constraints.weightx = 0.0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		layout.setConstraints(label, constraints);
		panel.add(label);
		constraints.gridx = 5;
		constraints.weightx = 1.0;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		component = m_jpStatus.getComponent(false);
		layout.setConstraints(component, constraints);
		panel.add(component);

		label = new JLabel(TranslationFacility.tr("ls.player.wage"));
		constraints.gridx = 4;
		constraints.weightx = 0.0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		layout.setConstraints(label, constraints);
		panel.add(label);
		constraints.gridx = 5;
		constraints.weightx = 1.0;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		component = m_jpGehalt.getComponent(false);
		layout.setConstraints(component, constraints);
		panel.add(component);

		label = new JLabel(TranslationFacility.tr("ls.player.tsi"));
		constraints.gridx = 4;
		constraints.weightx = 0.0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		layout.setConstraints(label, constraints);
		panel.add(label);
		constraints.gridx = 5;
		constraints.weightx = 1.0;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		component = m_jpMartwert.getComponent(false);
		layout.setConstraints(component, constraints);
		panel.add(component);

		label = new JLabel(TranslationFacility.tr("Rating"));
		constraints.gridx = 4;
		constraints.weightx = 0.0;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		layout.setConstraints(label, constraints);
		panel.add(label);
		constraints.gridx = 5;
		constraints.weightx = 1.0;
		constraints.gridy = 4;
		constraints.gridwidth = 2;
		component = m_jpRating.getComponent(false);
		layout.setConstraints(component, constraints);
		panel.add(component);

		// /////////////////////////////////////////////////////////////////////////////////////
		// Leerzeile
		label = new JLabel();
		constraints.gridx = 0;
		constraints.weightx = 0.0;
		constraints.gridy = 6;
		constraints.gridwidth = 4;
		layout.setConstraints(label, constraints);
		panel.add(label);
		constraints.gridwidth = 1;

		label = new JLabel(TranslationFacility.tr("ls.player.experience"));
		constraints.gridx = 0;
		constraints.weightx = 0.0;
		constraints.gridy = 7;
		layout.setConstraints(label, constraints);
		panel.add(label);
		constraints.gridx = 1;
		constraints.weightx = 1.0;
		constraints.gridy = 7;
		component = m_jpErfahrung.getComponent(false);
		component.setPreferredSize(COMPONENTENSIZE3);
		layout.setConstraints(component, constraints);
		panel.add(component);
		constraints.gridx = 2;
		constraints.weightx = 1.0;
		constraints.gridy = 7;
		component = m_jpErfahrung2.getComponent(false);
		component.setPreferredSize(COMPONENTENSIZE4);
		layout.setConstraints(component, constraints);
		panel.add(component);

		label = new JLabel(TranslationFacility.tr("ls.player.form"));
		constraints.gridx = 4;
		constraints.weightx = 0.0;
		constraints.gridy = 7;
		layout.setConstraints(label, constraints);
		panel.add(label);
		constraints.gridx = 5;
		constraints.weightx = 1.0;
		constraints.gridy = 7;
		component = m_jpForm.getComponent(false);
		component.setPreferredSize(COMPONENTENSIZE3);
		layout.setConstraints(component, constraints);
		panel.add(component);
		constraints.gridx = 6;
		constraints.weightx = 1.0;
		constraints.gridy = 7;
		component = m_jpForm2.getComponent(false);
		component.setPreferredSize(COMPONENTENSIZE4);
		layout.setConstraints(component, constraints);
		panel.add(component);

		label = new JLabel(TranslationFacility.tr("ls.player.skill.stamina"));
		constraints.gridx = 0;
		constraints.weightx = 0.0;
		constraints.gridy = 8;
		layout.setConstraints(label, constraints);
		panel.add(label);
		constraints.gridx = 1;
		constraints.weightx = 1.0;
		constraints.gridy = 8;
		component = m_jpKondition.getComponent(false);
		component.setPreferredSize(COMPONENTENSIZE3);
		layout.setConstraints(component, constraints);
		panel.add(component);
		constraints.gridx = 2;
		constraints.weightx = 1.0;
		constraints.gridy = 8;
		component = m_jpKondition2.getComponent(false);
		component.setPreferredSize(COMPONENTENSIZE4);
		layout.setConstraints(component, constraints);
		panel.add(component);

		label = new JLabel(TranslationFacility.tr("ls.player.skill.keeper"));
		constraints.gridx = 4;
		constraints.weightx = 0.0;
		constraints.gridy = 8;
		layout.setConstraints(label, constraints);
		panel.add(label);
		constraints.gridx = 5;
		constraints.weightx = 1.0;
		constraints.gridy = 8;
		component = m_jpTorwart.getComponent(false);
		component.setPreferredSize(COMPONENTENSIZE3);
		layout.setConstraints(component, constraints);
		panel.add(component);
		constraints.gridx = 6;
		constraints.weightx = 1.0;
		constraints.gridy = 8;
		component = m_jpTorwart2.getComponent(false);
		component.setPreferredSize(COMPONENTENSIZE4);
		layout.setConstraints(component, constraints);
		panel.add(component);

		label = new JLabel(TranslationFacility.tr("ls.player.skill.playmaking"));
		constraints.gridx = 0;
		constraints.weightx = 0.0;
		constraints.gridy = 9;
		layout.setConstraints(label, constraints);
		panel.add(label);
		constraints.gridx = 1;
		constraints.weightx = 1.0;
		constraints.gridy = 9;
		component = m_jpSpielaufbau.getComponent(false);
		component.setPreferredSize(COMPONENTENSIZE3);
		layout.setConstraints(component, constraints);
		panel.add(component);
		constraints.gridx = 2;
		constraints.weightx = 1.0;
		constraints.gridy = 9;
		component = m_jpSpielaufbau2.getComponent(false);
		component.setPreferredSize(COMPONENTENSIZE4);
		layout.setConstraints(component, constraints);
		panel.add(component);

		label = new JLabel(TranslationFacility.tr("ls.player.skill.passing"));
		constraints.gridx = 4;
		constraints.weightx = 0.0;
		constraints.gridy = 9;
		layout.setConstraints(label, constraints);
		panel.add(label);
		constraints.gridx = 5;
		constraints.weightx = 1.0;
		constraints.gridy = 9;
		component = m_jpPasspiel.getComponent(false);
		component.setPreferredSize(COMPONENTENSIZE3);
		layout.setConstraints(component, constraints);
		panel.add(component);
		constraints.gridx = 6;
		constraints.weightx = 1.0;
		constraints.gridy = 9;
		component = m_jpPasspiel2.getComponent(false);
		component.setPreferredSize(COMPONENTENSIZE4);
		layout.setConstraints(component, constraints);
		panel.add(component);

		label = new JLabel(TranslationFacility.tr("ls.player.skill.winger"));
		constraints.gridx = 0;
		constraints.weightx = 0.0;
		constraints.gridy = 10;
		layout.setConstraints(label, constraints);
		panel.add(label);
		constraints.gridx = 1;
		constraints.weightx = 1.0;
		constraints.gridy = 10;
		component = m_jpFluegelspiel.getComponent(false);
		component.setPreferredSize(COMPONENTENSIZE3);
		layout.setConstraints(component, constraints);
		panel.add(component);
		constraints.gridx = 2;
		constraints.weightx = 1.0;
		constraints.gridy = 10;
		component = m_jpFluegelspiel2.getComponent(false);
		component.setPreferredSize(COMPONENTENSIZE4);
		layout.setConstraints(component, constraints);
		panel.add(component);

		label = new JLabel(TranslationFacility.tr("ls.player.skill.defending"));
		constraints.gridx = 4;
		constraints.weightx = 0.0;
		constraints.gridy = 10;
		layout.setConstraints(label, constraints);
		panel.add(label);
		constraints.gridx = 5;
		constraints.weightx = 1.0;
		constraints.gridy = 10;
		component = m_jpVerteidigung.getComponent(false);
		component.setPreferredSize(COMPONENTENSIZE3);
		layout.setConstraints(component, constraints);
		panel.add(component);
		constraints.gridx = 6;
		constraints.weightx = 1.0;
		constraints.gridy = 10;
		component = m_jpVerteidigung2.getComponent(false);
		component.setPreferredSize(COMPONENTENSIZE4);
		layout.setConstraints(component, constraints);
		panel.add(component);

		label = new JLabel(TranslationFacility.tr("ls.player.skill.scoring"));
		constraints.gridx = 0;
		constraints.weightx = 0.0;
		constraints.gridy = 11;
		layout.setConstraints(label, constraints);
		panel.add(label);
		constraints.gridx = 1;
		constraints.weightx = 1.0;
		constraints.gridy = 11;
		component = m_jpTorschuss.getComponent(false);
		component.setPreferredSize(COMPONENTENSIZE3);
		layout.setConstraints(component, constraints);
		panel.add(component);
		constraints.gridx = 2;
		constraints.weightx = 1.0;
		constraints.gridy = 11;
		component = m_jpTorschuss2.getComponent(false);
		component.setPreferredSize(COMPONENTENSIZE4);
		layout.setConstraints(component, constraints);
		panel.add(component);

		label = new JLabel(TranslationFacility.tr("ls.player.skill.setpieces"));
		constraints.gridx = 4;
		constraints.weightx = 0.0;
		constraints.gridy = 11;
		layout.setConstraints(label, constraints);
		panel.add(label);
		constraints.gridx = 5;
		constraints.weightx = 1.0;
		constraints.gridy = 11;
		component = m_jpStandards.getComponent(false);
		component.setPreferredSize(COMPONENTENSIZE3);
		layout.setConstraints(component, constraints);
		panel.add(component);
		constraints.gridx = 6;
		constraints.weightx = 1.0;
		constraints.gridy = 11;
		component = m_jpStandards2.getComponent(false);
		component.setPreferredSize(COMPONENTENSIZE4);
		layout.setConstraints(component, constraints);
		panel.add(component);

		// //////////////////////////////////////////////////////////////////////
		// Leerzeile
		label = new JLabel("  ");
		constraints.gridx = 7;
		constraints.weightx = 0.0;
		constraints.gridy = 0;
		constraints.gridheight = 11;
		layout.setConstraints(label, constraints);
		panel.add(label);
		constraints.gridheight = 1;

		label = new JLabel(TranslationFacility.tr("ls.player.leadership"));
		constraints.gridx = 8;
		constraints.weightx = 0.0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		layout.setConstraints(label, constraints);
		panel.add(label);
		constraints.gridx = 9;
		constraints.weightx = 1.0;
		constraints.gridy = 0;
		component = m_jpFuehrung.getComponent(false);
		component.setPreferredSize(COMPONENTENSIZE3);
		layout.setConstraints(component, constraints);
		panel.add(component);
		constraints.gridx = 10;
		constraints.weightx = 1.0;
		constraints.gridy = 0;
		component = m_jpFuehrung2.getComponent(false);
		component.setPreferredSize(COMPONENTENSIZE4);
		layout.setConstraints(component, constraints);
		panel.add(component);

		label = new JLabel(TranslationFacility.tr("ls.player.speciality"));
		constraints.gridx = 8;
		constraints.weightx = 0.0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		layout.setConstraints(label, constraints);
		panel.add(label);
		constraints.gridx = 9;
		constraints.weightx = 1.0;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		component = m_jpSpezialitaet.getComponent(false);
		layout.setConstraints(component, constraints);
		panel.add(component);

		label = new JLabel(TranslationFacility.tr("ls.player.aggressiveness"));
		constraints.gridx = 8;
		constraints.weightx = 0.0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		layout.setConstraints(label, constraints);
		panel.add(label);
		constraints.gridx = 9;
		constraints.weightx = 1.0;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		component = m_jpAggressivitaet.getComponent(false);
		layout.setConstraints(component, constraints);
		panel.add(component);

		label = new JLabel(TranslationFacility.tr("ls.player.agreeability"));
		constraints.gridx = 8;
		constraints.weightx = 0.0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		layout.setConstraints(label, constraints);
		panel.add(label);
		constraints.gridx = 9;
		constraints.weightx = 1.0;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		component = m_jpAnsehen.getComponent(false);
		layout.setConstraints(component, constraints);
		panel.add(component);

		label = new JLabel(TranslationFacility.tr("ls.player.honesty"));
		constraints.gridx = 8;
		constraints.weightx = 0.0;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		layout.setConstraints(label, constraints);
		panel.add(label);
		constraints.gridx = 9;
		constraints.weightx = 1.0;
		constraints.gridy = 4;
		constraints.gridwidth = 2;
		component = m_jpCharakter.getComponent(false);
		layout.setConstraints(component, constraints);
		panel.add(component);

		// 2 Leerzeile
		label = new JLabel();
		constraints.gridx = 11;
		constraints.weightx = 0.0;
		constraints.gridy = 5;
		constraints.gridwidth = 3;
		constraints.gridheight = 2;
		layout.setConstraints(label, constraints);
		panel.add(label);
		constraints.gridwidth = 1;
		constraints.gridheight = 1;

		label = new JLabel(TranslationFacility.tr("ToreFreund"));
		constraints.gridx = 8;
		constraints.weightx = 0.0;
		constraints.gridy = 7;
		constraints.gridwidth = 1;
		layout.setConstraints(label, constraints);
		panel.add(label);
		constraints.gridx = 9;
		constraints.weightx = 1.0;
		constraints.gridy = 7;
		constraints.gridwidth = 2;
		component = m_jpToreFreund.getComponent(false);
		layout.setConstraints(component, constraints);
		panel.add(component);

		label = new JLabel(TranslationFacility.tr("ToreLiga"));
		constraints.gridx = 8;
		constraints.weightx = 0.0;
		constraints.gridy = 8;
		constraints.gridwidth = 1;
		layout.setConstraints(label, constraints);
		panel.add(label);
		constraints.gridx = 9;
		constraints.weightx = 1.0;
		constraints.gridy = 8;
		constraints.gridwidth = 2;
		component = m_jpToreLiga.getComponent(false);
		layout.setConstraints(component, constraints);
		panel.add(component);

		label = new JLabel(TranslationFacility.tr("TorePokal"));
		constraints.gridx = 8;
		constraints.weightx = 0.0;
		constraints.gridy = 9;
		constraints.gridwidth = 1;
		layout.setConstraints(label, constraints);
		panel.add(label);
		constraints.gridx = 9;
		constraints.weightx = 1.0;
		constraints.gridy = 9;
		constraints.gridwidth = 2;
		component = m_jpTorePokal.getComponent(false);
		layout.setConstraints(component, constraints);
		panel.add(component);

		label = new JLabel(TranslationFacility.tr("ls.player.career_goals"));
		constraints.gridx = 8;
		constraints.weightx = 0.0;
		constraints.gridy = 10;
		constraints.gridwidth = 1;
		layout.setConstraints(label, constraints);
		panel.add(label);
		constraints.gridx = 9;
		constraints.weightx = 1.0;
		constraints.gridy = 10;
		constraints.gridwidth = 2;
		component = m_jpToreGesamt.getComponent(false);
		layout.setConstraints(component, constraints);
		panel.add(component);

		label = new JLabel(TranslationFacility.tr("Hattricks"));
		constraints.gridx = 8;
		constraints.weightx = 0.0;
		constraints.gridy = 11;
		constraints.gridwidth = 1;
		layout.setConstraints(label, constraints);
		panel.add(label);
		constraints.gridx = 9;
		constraints.weightx = 1.0;
		constraints.gridy = 11;
		constraints.gridwidth = 2;
		component = m_jpHattriks.getComponent(false);
		layout.setConstraints(component, constraints);
		panel.add(component);

		// //////////////////////////////////////////////////////////////////////
		// Leerzeile
		label = new JLabel("  ");
		constraints.gridx = 11;
		constraints.weightx = 0.0;
		constraints.gridy = 0;
		constraints.gridheight = 18;
		constraints.gridwidth = 1;
		layout.setConstraints(label, constraints);
		panel.add(label);
		constraints.gridheight = 1;

		for (int i = 0; i < playerPositionValues.length; i++) {
			label = new JLabel(MatchRoleID.getShortNameForPosition(playerPosition[i]));
			label.setToolTipText(MatchRoleID.getNameForPosition(playerPosition[i]));
			initBlueLabel(i, constraints, layout, panel, label);
			initBlueField(i, constraints, layout, panel,
					playerPositionValues[i].getComponent(false));
		}

		// //////////////////////////////////////////////////////////////////////
		final float[] rating = core.db.DBManager.instance().getBewertungen4Player(
				player.getPlayerId());
		final float[] ratingPos = core.db.DBManager.instance().getBewertungen4PlayerUndPosition(
				player.getPlayerId(), matchplayer.getPosition());

		// Rating insgesamt
		GridBagLayout sublayout = new GridBagLayout();
		GridBagConstraints subconstraints = new GridBagConstraints();
		subconstraints.anchor = GridBagConstraints.CENTER;
		subconstraints.fill = GridBagConstraints.HORIZONTAL;
		subconstraints.weightx = 1.0;
		subconstraints.weighty = 0.0;
		subconstraints.insets = new Insets(1, 2, 1, 1);

		JPanel subpanel = new ImagePanel(sublayout);
		subpanel.setBorder(BorderFactory.createTitledBorder(TranslationFacility.tr("Rating")));

		subconstraints.gridx = 0;
		subconstraints.gridy = 0;
		subconstraints.weightx = 0.0;
		label = new JLabel(TranslationFacility.tr("Maximal"));
		sublayout.setConstraints(label, subconstraints);
		subpanel.add(label);

		subconstraints.gridx = 0;
		subconstraints.gridy = 1;
		label = new JLabel(TranslationFacility.tr("Minimal"));
		sublayout.setConstraints(label, subconstraints);
		subpanel.add(label);

		subconstraints.gridx = 0;
		subconstraints.gridy = 2;
		label = new JLabel(TranslationFacility.tr("Durchschnitt"));
		sublayout.setConstraints(label, subconstraints);
		subpanel.add(label);

		subconstraints.gridx = 0;
		subconstraints.gridy = 3;
		label = new JLabel(TranslationFacility.tr("Spiele"));
		sublayout.setConstraints(label, subconstraints);
		subpanel.add(label);

		subconstraints.gridx = 1;
		subconstraints.gridy = 0;
		subconstraints.weightx = 1.0;

		RatingTableEntry ratingentry = new RatingTableEntry((int) (rating[0] * 2));

		sublayout.setConstraints(ratingentry.getComponent(false), subconstraints);
		subpanel.add(ratingentry.getComponent(false));

		subconstraints.gridx = 1;
		subconstraints.gridy = 1;
		ratingentry = new RatingTableEntry((int) (rating[1] * 2));
		sublayout.setConstraints(ratingentry.getComponent(false), subconstraints);
		subpanel.add(ratingentry.getComponent(false));

		subconstraints.gridx = 1;
		subconstraints.gridy = 2;
		ratingentry = new RatingTableEntry(Math.round(rating[2] * 2));
		sublayout.setConstraints(ratingentry.getComponent(false), subconstraints);
		subpanel.add(ratingentry.getComponent(false));

		subconstraints.gridx = 2;
		subconstraints.gridy = 0;
		subconstraints.weightx = 0.0;
		label = new JLabel(rating[0] + "");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		sublayout.setConstraints(label, subconstraints);
		subpanel.add(label);

		subconstraints.gridx = 2;
		subconstraints.gridy = 1;
		label = new JLabel(rating[1] + "");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		sublayout.setConstraints(label, subconstraints);
		subpanel.add(label);

		subconstraints.gridx = 2;
		subconstraints.gridy = 2;
		label = new JLabel(core.util.Helper.round(rating[2], 2) + "");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		sublayout.setConstraints(label, subconstraints);
		subpanel.add(label);

		subconstraints.gridx = 1;
		subconstraints.gridy = 3;
		subconstraints.gridwidth = 2;
		label = new JLabel(((int) rating[3]) + "", SwingConstants.CENTER);
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		sublayout.setConstraints(label, subconstraints);
		subpanel.add(label);

		constraints.gridx = 0;
		constraints.gridy = 13;
		constraints.weightx = 0.5;
		constraints.gridheight = 4;
		constraints.gridwidth = 5;

		layout.setConstraints(subpanel, constraints);
		panel.add(subpanel);

		// Rating Position
		sublayout = new GridBagLayout();
		subconstraints = new GridBagConstraints();
		subconstraints.anchor = GridBagConstraints.CENTER;
		subconstraints.fill = GridBagConstraints.HORIZONTAL;
		subconstraints.weightx = 1.0;
		subconstraints.weighty = 0.0;
		subconstraints.insets = new Insets(1, 2, 1, 1);
		subpanel = new ImagePanel(sublayout);
		subpanel.setBorder(BorderFactory.createTitledBorder(TranslationFacility.tr("Rating")
				+ " "
				+ MatchRoleID.getNameForPosition(MatchRoleID
				.getPosition(matchplayer.getRoleId(), matchplayer.getBehaviour()))));

		subconstraints.gridx = 0;
		subconstraints.gridy = 0;
		subconstraints.weightx = 0.0;
		label = new JLabel(TranslationFacility.tr("Maximal"));
		sublayout.setConstraints(label, subconstraints);
		subpanel.add(label);

		subconstraints.gridx = 0;
		subconstraints.gridy = 1;
		label = new JLabel(TranslationFacility.tr("Minimal"));
		sublayout.setConstraints(label, subconstraints);
		subpanel.add(label);

		subconstraints.gridx = 0;
		subconstraints.gridy = 2;
		label = new JLabel(TranslationFacility.tr("Durchschnitt"));
		sublayout.setConstraints(label, subconstraints);
		subpanel.add(label);

		subconstraints.gridx = 0;
		subconstraints.gridy = 3;
		label = new JLabel(TranslationFacility.tr("Spiele"));
		sublayout.setConstraints(label, subconstraints);
		subpanel.add(label);

		subconstraints.gridx = 1;
		subconstraints.gridy = 0;
		subconstraints.weightx = 1.0;
		ratingentry = new RatingTableEntry((int) (ratingPos[0] * 2));

		// ratingentry.getComponent ( false ).setPreferredSize ( new Dimension(
		// 120, 14 ) );
		sublayout.setConstraints(ratingentry.getComponent(false), subconstraints);
		subpanel.add(ratingentry.getComponent(false));

		subconstraints.gridx = 1;
		subconstraints.gridy = 1;
		ratingentry = new RatingTableEntry((int) (ratingPos[1] * 2));
		sublayout.setConstraints(ratingentry.getComponent(false), subconstraints);
		subpanel.add(ratingentry.getComponent(false));

		subconstraints.gridx = 1;
		subconstraints.gridy = 2;
		ratingentry = new RatingTableEntry(Math.round(ratingPos[2] * 2));
		sublayout.setConstraints(ratingentry.getComponent(false), subconstraints);
		subpanel.add(ratingentry.getComponent(false));

		subconstraints.gridx = 2;
		subconstraints.gridy = 0;
		subconstraints.weightx = 0.0;
		label = new JLabel(ratingPos[0] + "");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		sublayout.setConstraints(label, subconstraints);
		subpanel.add(label);

		subconstraints.gridx = 2;
		subconstraints.gridy = 1;
		label = new JLabel(ratingPos[1] + "");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		sublayout.setConstraints(label, subconstraints);
		subpanel.add(label);

		subconstraints.gridx = 2;
		subconstraints.gridy = 2;
		label = new JLabel(core.util.Helper.round(ratingPos[2], 2) + "");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		sublayout.setConstraints(label, subconstraints);
		subpanel.add(label);

		subconstraints.gridx = 1;
		subconstraints.gridy = 3;
		subconstraints.gridwidth = 2;
		label = new JLabel(((int) ratingPos[3]) + "", SwingConstants.CENTER);
		label.setHorizontalAlignment(JLabel.RIGHT);
		sublayout.setConstraints(label, subconstraints);
		subpanel.add(label);

		constraints.gridx = 6;
		constraints.gridy = 13;
		constraints.weightx = 0.5;
		constraints.gridheight = 4;
		constraints.gridwidth = 5;
		layout.setConstraints(subpanel, constraints);
		panel.add(subpanel);

		getContentPane().add(panel, BorderLayout.CENTER);
	}

	/**
	 * init a label
	 *
	 * @param y
	 * @param constraints
	 * @param layout
	 * @param panel
	 * @param label
	 */
	private void initBlueLabel(int y, GridBagConstraints constraints, GridBagLayout layout,
							   JPanel panel, JLabel label) {
		setPosition(constraints, 12, y);
		constraints.weightx = 0.0;
		layout.setConstraints(label, constraints);
		panel.add(label);
	}

	/**
	 * init a value field
	 *
	 * @param y
	 * @param constraints
	 * @param layout
	 * @param panel
	 * @param component
	 */
	private void initBlueField(int y, GridBagConstraints constraints, GridBagLayout layout,
							   JPanel panel, JComponent component) {
		setPosition(constraints, 13, y);
		constraints.weightx = 1.0;
		layout.setConstraints(component, constraints);
		panel.add(component);
	}

	/**
	 * set position in gridBag
	 *
	 * @param c
	 * @param x
	 * @param y
	 */
	private void setPosition(GridBagConstraints c, int x, int y) {
		c.gridx = x;
		c.gridy = y;
	}

	private void showNormal(DoubleLabelEntries labelEntry, byte playerPosition, Player m_clPlayer) {
		var ratingPredictionModel = HOVerwaltung.instance().getModel().getRatingPredictionModel();
		labelEntry.getLeft().setText(
				Helper.round(ratingPredictionModel.getPlayerMatchAverageRating(m_clPlayer, playerPosition), core.model.UserParameter.instance().nbDecimals) + "");
		labelEntry.getRight().clear();
	}

	private void showWithCompare(DoubleLabelEntries labelEntry, byte playerPosition, Player m_clPlayer, Player m_clVergleichsPlayer) {
		var ratingPredictionModel = HOVerwaltung.instance().getModel().getRatingPredictionModel();
		var r = ratingPredictionModel.getPlayerMatchAverageRating(m_clPlayer, playerPosition);
		labelEntry.getLeft().setText(Helper.round(r, core.model.UserParameter.instance().nbDecimals) + "");
		labelEntry.getRight().setSpecialNumber((float) (r - ratingPredictionModel.getPlayerMatchAverageRating(m_clVergleichsPlayer, playerPosition)), false);
	}
}
