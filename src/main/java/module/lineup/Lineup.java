package module.lineup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import core.db.DBManager;
import core.model.HOVerwaltung;
import core.model.TranslationFacility;
import core.model.match.IMatchDetails;
import core.model.match.MatchKurzInfo;
import core.model.match.MatchLineupPosition;
import core.model.match.Weather;
import core.model.player.IMatchRoleID;
import core.model.player.MatchRoleID;
import core.model.player.Player;
import core.util.HOLogger;
import core.util.StringUtils;
import module.lineup.assistant.LineupAssistant;
import module.lineup.substitution.model.GoalDiffCriteria;
import module.lineup.substitution.model.MatchOrderType;
import module.lineup.substitution.model.RedCardCriteria;
import module.lineup.substitution.model.Substitution;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.max;


public class Lineup{

	public static final byte SYS_433 = 0;
	public static final byte SYS_442 = 1;
	public static final byte SYS_532 = 2;
	public static final byte SYS_541 = 3;
	public static final byte SYS_352 = 4;
	public static final byte SYS_343 = 5;
	public static final byte SYS_451 = 6;
	public static final byte SYS_MURKS = 7; // unknown / invalid system
	public static final byte SYS_523 = 8;
	public static final byte SYS_550 = 9;
	public static final byte SYS_253 = 10;

	// TODO: remove assistant from Lineup class
	/** Aufstellungsassistent */
	private final LineupAssistant m_clAssi = new LineupAssistant();

	/** positions */
	@SerializedName("positions")
	@Expose
	private Vector<MatchLineupPosition> m_vFieldPositions = new Vector<>();
	/** bench */
	@SerializedName("bench")
	@Expose
	private Vector<MatchLineupPosition> m_vBenchPositions = new Vector<>();
	@SerializedName("substitutions")
	@Expose
	private List<Substitution> substitutions = new ArrayList<>();
	@SerializedName("kickers")
	@Expose
	private Vector<MatchLineupPosition> penaltyTakers = new Vector<>();

	/** captain */
	@SerializedName("captain")
	@Expose
	private int m_iKapitaen = -1;

	/** set pieces take */
	@SerializedName("setPieces")
	@Expose
	private int m_iKicker = -1;

	private Vector<MatchLineupPosition> replacedPositions = new Vector<>();
	MatchLineupPosition captain;
	MatchLineupPosition setPiecesTaker;
	private Player.ManMarkingPosition manMarkingPosition;

	private void setCaptain(MatchLineupPosition position) {
		this.captain = position;
		this.m_iKapitaen = position.getPlayerId();
	}

	private void setSetPiecesTaker(MatchLineupPosition position) {
		this.setPiecesTaker = position;
		this.m_iKicker = position.getPlayerId();
	}

	public Player.ManMarkingPosition getManMarkingPosition() {
		return this.manMarkingPosition;
	}

	public void setManMarkingPosition(Player.ManMarkingPosition manMarkingPosition) {
		this.ratingRevision++;
		this.manMarkingPosition = manMarkingPosition;
	}

	public void setPlayers(List<MatchLineupPosition> matchLineupPositions) {
		initPositionen553(); // reset all
		for (var position : matchLineupPositions){
			setPosition(position);
		}
	}

	public long getRatingRevision() {
		return ratingRevision;
	}

    public void setSubstitutionsUpdated() {
		ratingRevision++;
    }

    private static class Settings {
		/** Attitude */
		@SerializedName("speechLevel")
		@Expose
		private int m_iAttitude;

		/** TacticType */
		@SerializedName("tactic")
		@Expose
		private int m_iTacticType;

		/** Style of play */
		@SerializedName("coachModifier")
		@Expose
		private int m_iStyleOfPlay;

		//NOTE: newLineup is required by HT - do not delete even if it seems unused !
		@SerializedName("newLineup")
		@Expose
		private String newLineup = "";

	}

	@SerializedName("settings")
	@Expose
	Settings settings = new Settings();

	/** PullBackMinute **/
	private int pullBackMinute = 90; // no pull back

	// TODO -> MatchKurzInfo
	/** Home/Away/AwayDerby */
	private short m_sLocation = -1;

	private int m_iArenaId = -1;
    private Weather m_cWeather = Weather.NULL;
	private Weather.Forecast m_cWeatherForecast = Weather.Forecast.NULL;

	/**
	 * The rating revision number gets incremented each time a rating relevant property gets changes.
	 * This is used by rating calculating caches, that will reset, when they see a new revision number.
	 */
	private long ratingRevision=0;

	// ~ Constructors
	// -------------------------------------------------------------------------------

	/**
	 * Creates a new Aufstellung object.
	 */
	public Lineup() {
		initPositionen553();
	}

	/**
	 * Creates a new instance of Lineup
	 * <p>
	 * Probably up for change with new XML?
	 */
	public Lineup(Properties properties) {
		try {
			// Positionen erzeugen
			m_vFieldPositions.add(new MatchLineupPosition(IMatchRoleID.keeper, Integer
					.parseInt(properties.getProperty("keeper", "0")), (byte) 0));
			m_vFieldPositions.add(new MatchLineupPosition(IMatchRoleID.rightBack, Integer
					.parseInt(properties.getProperty("rightback", "0")), Byte.parseByte(properties
					.getProperty("order_rightback", "0"))));
			m_vFieldPositions.add(new MatchLineupPosition(IMatchRoleID.rightCentralDefender, Integer
					.parseInt(properties.getProperty("rightcentraldefender", "0")), Byte
					.parseByte(properties.getProperty("order_rightcentraldefender", "0"))));
			m_vFieldPositions.add(new MatchLineupPosition(IMatchRoleID.middleCentralDefender, Integer
					.parseInt(properties.getProperty("middlecentraldefender", "0")), Byte
					.parseByte(properties.getProperty("order_middlecentraldefender", "0"))));
			m_vFieldPositions.add(new MatchLineupPosition(IMatchRoleID.leftCentralDefender, Integer
					.parseInt(properties.getProperty("leftcentraldefender", "0")), Byte
					.parseByte(properties.getProperty("order_leftcentraldefender", "0"))));
			m_vFieldPositions.add(new MatchLineupPosition(IMatchRoleID.leftBack, Integer
					.parseInt(properties.getProperty("leftback", "0")), Byte.parseByte(properties
					.getProperty("order_leftback", "0"))));
			m_vFieldPositions.add(new MatchLineupPosition(IMatchRoleID.rightWinger, Integer
					.parseInt(properties.getProperty("rightwinger", "0")), Byte
					.parseByte(properties.getProperty("order_rightwinger", "0"))));
			m_vFieldPositions.add(new MatchLineupPosition(IMatchRoleID.rightInnerMidfield, Integer
					.parseInt(properties.getProperty("rightinnermidfield", "0")), Byte.parseByte(properties
					.getProperty("order_rightinnermidfield", "0"))));
			m_vFieldPositions.add(new MatchLineupPosition(IMatchRoleID.centralInnerMidfield, Integer
					.parseInt(properties.getProperty("middleinnermidfield", "0")), Byte.parseByte(properties
					.getProperty("order_centralinnermidfield", "0"))));
			m_vFieldPositions.add(new MatchLineupPosition(IMatchRoleID.leftInnerMidfield, Integer
					.parseInt(properties.getProperty("leftinnermidfield", "0")), Byte.parseByte(properties
					.getProperty("order_leftinnermidfield", "0"))));
			m_vFieldPositions.add(new MatchLineupPosition(IMatchRoleID.leftWinger, Integer
					.parseInt(properties.getProperty("leftwinger", "0")), Byte.parseByte(properties
					.getProperty("order_leftwinger", "0"))));
			m_vFieldPositions.add(new MatchLineupPosition(IMatchRoleID.rightForward, Integer
					.parseInt(properties.getProperty("rightforward", "0")), Byte.parseByte(properties
					.getProperty("order_rightforward", "0"))));
			m_vFieldPositions.add(new MatchLineupPosition(IMatchRoleID.centralForward, Integer
					.parseInt(properties.getProperty("centralforward", "0")), Byte.parseByte(properties
					.getProperty("order_centralforward", "0"))));
			m_vFieldPositions.add(new MatchLineupPosition(IMatchRoleID.leftForward, Integer
					.parseInt(properties.getProperty("leftforward", "0")), Byte.parseByte(properties
					.getProperty("order_leftforward", "0"))));

			m_vBenchPositions.add(new MatchLineupPosition(IMatchRoleID.substGK1, Integer.parseInt(properties.getProperty("substgk1", "0")), (byte) 0));
			m_vBenchPositions.add(new MatchLineupPosition(IMatchRoleID.substCD1, Integer.parseInt(properties.getProperty("substcd1", "0")), (byte) 0));
			m_vBenchPositions.add(new MatchLineupPosition(IMatchRoleID.substWB1, Integer.parseInt(properties.getProperty("substwb1", "0")), (byte) 0));
			m_vBenchPositions.add(new MatchLineupPosition(IMatchRoleID.substIM1, Integer.parseInt(properties.getProperty("substim1", "0")), (byte) 0));
			m_vBenchPositions.add(new MatchLineupPosition(IMatchRoleID.substFW1, Integer.parseInt(properties.getProperty("substfw1", "0")), (byte) 0));
			m_vBenchPositions.add(new MatchLineupPosition(IMatchRoleID.substWI1, Integer.parseInt(properties.getProperty("substwi1", "0")), (byte) 0));
			m_vBenchPositions.add(new MatchLineupPosition(IMatchRoleID.substXT1, Integer.parseInt(properties.getProperty("substxt1", "0")), (byte) 0));

			m_vBenchPositions.add(new MatchLineupPosition(IMatchRoleID.substGK2, Integer.parseInt(properties.getProperty("substgk2", "0")), (byte) 0));
			m_vBenchPositions.add(new MatchLineupPosition(IMatchRoleID.substCD2, Integer.parseInt(properties.getProperty("substcd2", "0")), (byte) 0));
			m_vBenchPositions.add(new MatchLineupPosition(IMatchRoleID.substWB2, Integer.parseInt(properties.getProperty("substwb2", "0")), (byte) 0));
			m_vBenchPositions.add(new MatchLineupPosition(IMatchRoleID.substIM2, Integer.parseInt(properties.getProperty("substim2", "0")), (byte) 0));
			m_vBenchPositions.add(new MatchLineupPosition(IMatchRoleID.substFW2, Integer.parseInt(properties.getProperty("substfw2", "0")), (byte) 0));
			m_vBenchPositions.add(new MatchLineupPosition(IMatchRoleID.substWI2, Integer.parseInt(properties.getProperty("substwi2", "0")), (byte) 0));
			m_vBenchPositions.add(new MatchLineupPosition(IMatchRoleID.substXT2, Integer.parseInt(properties.getProperty("substxt2", "0")), (byte) 0));

			var tactic = properties.getProperty("tactictype");
			if (StringUtils.isEmpty(tactic)|| tactic.equals("null")) // to avoid exception when match is finish
				settings.m_iTacticType = 0;
			else
				settings.m_iTacticType = Integer.parseInt(tactic);

			String attitude = properties.getProperty("installning", "0");
			if (StringUtils.isEmpty(attitude) || attitude.equals("null") ) // to avoid exception when match is finish
				settings.m_iAttitude = 0;
			else
				settings.m_iAttitude = Integer.parseInt(attitude);

			var propStyleOfPlay = properties.getProperty("styleofplay");
			if (StringUtils.isEmpty(propStyleOfPlay) || propStyleOfPlay.equals("null")) // to avoid exception when match is finish
				settings.m_iStyleOfPlay = 0;
			else
				settings.m_iStyleOfPlay = Integer.parseInt(propStyleOfPlay);

			// and read the sub contents
			int iSub = 0;
			while(true){
				var subs = getSubstitution(properties, iSub++);
				if (subs != null) {
					this.substitutions.add(subs);
				}
				else {
					break;
				}
			}

			// Add the penalty takers

			for (int i = 0; i < 11; i++) {
				penaltyTakers.add(new MatchLineupPosition(i + IMatchRoleID.penaltyTaker1, Integer
						.parseInt(properties.getProperty("penalty" + i, "0")), (byte) 0));
			}

		} catch (Exception e) {
			HOLogger.instance().warning(getClass(), "Aufstellung.<init1>: " + e);
			HOLogger.instance().log(getClass(), e);
			m_vFieldPositions.removeAllElements();
			m_vBenchPositions.removeAllElements();
			initPositionen553();
		}

		try { // captain + set pieces taker
			setKicker(Integer.parseInt(properties.getProperty("kicker1", "0")));
			setCaptain(Integer.parseInt(properties.getProperty("captain", "0")));
		} catch (Exception e) {
			HOLogger.instance().warning(getClass(), "Aufstellung.<init2>: " + e);
			HOLogger.instance().log(getClass(), e);
		}
	}

	private Substitution getSubstitution(Properties properties, int i) {
		var prefix = "subst" + i;
		var playerorderString = properties.getProperty(prefix + "playerorderid");
		if (playerorderString == null) return null;
		var playerorderid = Integer.parseInt(playerorderString);
		if (playerorderid < 0) return null;

		return  new Substitution(
				Integer.parseInt(properties.getProperty(prefix + "playerorderid")),
				Integer.parseInt(properties.getProperty(prefix + "playerin")),
				Integer.parseInt(properties.getProperty(prefix + "playerout")),
				Byte.parseByte(properties.getProperty(prefix + "ordertype")),
				Byte.parseByte(properties.getProperty(prefix + "matchminutecriteria")),
				Byte.parseByte(properties.getProperty(prefix + "pos")),
				Byte.parseByte(properties.getProperty(prefix + "behaviour")),
				RedCardCriteria.parse(properties.getProperty(prefix + "card")),
				GoalDiffCriteria.parse(properties.getProperty(prefix + "standing")));
	}

//	/**
//	 * get the tactic level for AiM/AoW
//	 *
//	 * @return tactic level
//	 */
//	public final float getTacticLevelAimAow() {
//		return HOVerwaltung.instance().getModel().getRatingPredictionModel().getTacticRating()
//		return Math.max(1, new RatingPredictionManager(this, HOVerwaltung.instance().getModel().getTeam()).getTacticLevelAowAim());
//	}
//
//	/**
//	 * get the tactic level for counter
//	 *
//	 * @return tactic level
//	 */
//	public final float getTacticLevelCounter() {
//		return (new RatingPredictionManager(this, HOVerwaltung.instance().getModel().getTeam())).getTacticLevelCounter();
//	}
//
//	/**
//	 * get the tactic level for pressing
//	 *
//	 * @return tactic level
//	 */
//	public final float getTacticLevelPressing() {
//		return Math.max(1, new RatingPredictionManager(this, HOVerwaltung.instance().getModel().getTeam()).getTacticLevelPressing());
//	}
//
//	/**
//	 * get the tactic level for Long Shots
//	 *
//	 * @return tactic level
//	 */
//	public final float getTacticLevelLongShots() {
//		return Math.max(1, new RatingPredictionManager(this, HOVerwaltung.instance().getModel().getTeam()).getTacticLevelLongShots());
//	}
//	public final float getTacticLevelCreative() {
//		return Math.max(1, new RatingPredictionManager(this, HOVerwaltung.instance().getModel().getTeam()).getTacticLevelCreative());
//	}

	/**
	 * Setter for property m_iAttitude.
	 * 
	 * @param m_iAttitude
	 *            New value of property m_iAttitude.
	 */
	public final void setAttitude(int m_iAttitude) {
		this.ratingRevision++;
		this.settings.m_iAttitude = m_iAttitude;
	}

	/**
	 * Getter for property m_iAttitude.
	 * 
	 * @return Value of property m_iAttitude.
	 */
	public final int getAttitude() {
		return settings.m_iAttitude;
	}
	
	public String getAttitudeName(int attitude) {
		return switch (attitude) {
			case IMatchDetails.EINSTELLUNG_NORMAL -> TranslationFacility.tr("ls.team.teamattitude_short.normal");
			case IMatchDetails.EINSTELLUNG_PIC -> TranslationFacility.tr("ls.team.teamattitude_short.playitcool");
			case IMatchDetails.EINSTELLUNG_MOTS -> TranslationFacility.tr("ls.team.teamattitude_short.matchoftheseason");
			default -> TranslationFacility.tr("Unbestimmt");
		};
	}

	public void setStyleOfPlay(int style) {
		ratingRevision++;
		settings.m_iStyleOfPlay = style;
	}
	
	public int getCoachModifier() {
		return settings.m_iStyleOfPlay;
	}

	/**
	 * Auto-select the set best captain.
	 */
	public final void setAutoKapitaen(@Nullable List<Player> players) {
		float maxValue = -1;

		if (players == null) {
			players = HOVerwaltung.instance().getModel().getCurrentPlayers();
		}

		if (players != null) {
			for (Player player : players) {
				if (m_clAssi.isPlayerInStartingEleven(player.getPlayerId(), m_vFieldPositions)) {
					int curPlayerId = player.getPlayerId();
					float curCaptainsValue = HOVerwaltung.instance().getModel().getCurrentLineupTeam().getLineup()
							.getAverageExperience(curPlayerId);
					if (maxValue < curCaptainsValue) {
						maxValue = curCaptainsValue;
						m_iKapitaen = curPlayerId;
					}
				}
			}
		}
	}

	/**
	 * Auto-select the set best pieces taker.
	 */
	public final void setAutoKicker(@Nullable List<Player> players) {
		double maxStandard = -1;
		int form = -1;

		if (players == null) {
			players = HOVerwaltung.instance().getModel().getCurrentPlayers();
		}

		Vector<MatchLineupPosition> noKeeper = new Vector<>(m_vFieldPositions);
		for (var pos : noKeeper) {
			if (pos.getId() == IMatchRoleID.keeper) {
				noKeeper.remove(pos);
				break;
			}
		}

		if (players != null) {
			var ratingPredictionModel = HOVerwaltung.instance().getModel().getRatingPredictionModel();
			for (Player player : players) {
				if (m_clAssi.isPlayerInStartingEleven(player.getPlayerId(), noKeeper)) {
					var sp = ratingPredictionModel.getPlayerSetPiecesStrength(player);
					if (sp > maxStandard) {
						maxStandard = sp;
						form = player.getForm();
						m_iKicker = player.getPlayerId();
					} else if ((sp == maxStandard) && (form < player.getForm())) {
						maxStandard = sp;
						form = player.getForm();
						m_iKicker = player.getPlayerId();
					}
				}
			}
		}
	}

	/**
	 * Get the average experience of all players in lineup using the formula
	 * from kopsterkespits: teamxp = ((sum of teamxp + xp of
	 * captain)/12)*(1-(7-leadership of captain)*5%)
	 *
	 * @param captainsId use this player as captain (<= 0 for current captain)
	 * @return float
	 */
	public final float getAverageExperience(int captainsId) {
		float value = 0;

		Player captain = null;
		List<Player> players = HOVerwaltung.instance().getModel().getCurrentPlayers();

		if (players != null) {
			for (Player player : players) {
				if (m_clAssi.isPlayerInStartingEleven(player.getPlayerId(), m_vFieldPositions)) {
					value += player.getExperience();
					if (captainsId > 0) {
						if (captainsId == player.getPlayerId()) {
							captain = player;
						}
					} else if (m_iKapitaen == player.getPlayerId()) {
						captain = player;
					}
				}
			}
		}
		if (captain != null) {
			value = ((value + captain.getExperience()) / 12)
					* (1f - (float) (7 - captain.getLeadership()) * 0.05f);
		} else {
			// HOLogger.instance().log(getClass(),
			// "Can't calc average experience, captain not set.");
			value = -1f;
		}
		return value;
	}

//	public void setRatings() {
//		final RatingPredictionManager rpManager;
//		Ratings oRatings = new Ratings();
//		boolean bForm = true;
//
//		if ((HOVerwaltung.instance().getModel() != null) && HOVerwaltung.instance().getModel().getID() != -1) {
//			var hoModel = HOVerwaltung.instance().getModel();
//			var ratingPredictionModel = hoModel.getRatingPredictionModel();
//			var lineup = hoModel.getLineupWithoutRatingRecalc();
//			rpManager = new RatingPredictionManager(this, HOVerwaltung.instance().getModel().getTeam());
//			oRatings.setLeftDefense(ratingPredictionModel.getAverageRating(lineup, RatingPredictionModel.RatingSector.Defence_Left, 90));
//			oRatings.setCentralDefense(rpManager.getCentralDefenseRatings(bForm, true));
//			oRatings.setRightDefense(rpManager.getRightDefenseRatings(bForm, true));
//			oRatings.setMidfield(rpManager.getMFRatings(bForm, true));
//			oRatings.setLeftAttack(rpManager.getLeftAttackRatings(bForm, true));
//			oRatings.setCentralAttack(rpManager.getCentralAttackRatings(bForm, true));
//			oRatings.setRightAttack(rpManager.getRightAttackRatings(bForm, true));
//			oRatings.computeHatStats();
//			oRatings.computeLoddarStats();
//			this.oRatings = oRatings;
//		} else {
//			this.oRatings = new Ratings();
//		}
//	}

//	/**
//	 * This version of the function is called during HOModel creation to avoid back looping
//	 */
//	 public void setRatings(int hrfID) {
//		 final RatingPredictionManager rpManager;
//		 Ratings oRatings = new Ratings();
//		 boolean bForm = true;
//
//		if ((HOVerwaltung.instance().getModel() != null) && HOVerwaltung.instance().getModel().getID() != -1) {
//			Team _team = DBManager.instance().getTeam(hrfID);
//			rpManager = new RatingPredictionManager(this, _team);
//			oRatings.setLeftDefense(rpManager.getLeftDefenseRatings(bForm, true));
//			oRatings.setCentralDefense(rpManager.getCentralDefenseRatings(bForm, true));
//			oRatings.setRightDefense(rpManager.getRightDefenseRatings(bForm, true));
//			oRatings.setMidfield(rpManager.getMFRatings(bForm, true));
//			oRatings.setLeftAttack(rpManager.getLeftAttackRatings(bForm, true));
//			oRatings.setCentralAttack(rpManager.getCentralAttackRatings(bForm, true));
//			oRatings.setRightAttack(rpManager.getRightAttackRatings(bForm, true));
//			oRatings.computeHatStats();
//			oRatings.computeLoddarStats();
//			this.oRatings = oRatings;
//		}
//		else {
//			this.oRatings = new Ratings(); }
//	}



//	public Ratings getRatings() {
//		    if(oRatings == null)
//			{
//				setRatings();
//			}
//			return oRatings;
//		}



	/**
	 * Setter for property m_iKapitaen.
	 * 
	 * @param m_iKapitaen
	 *            New value of property m_iKapitaen.
	 */
	public final void setCaptain(int m_iKapitaen) {
		this.setCaptain(new MatchLineupPosition(IMatchRoleID.captain, m_iKapitaen,0));
	}

	/**
	 * Getter for property m_iKapitaen.
	 * 
	 * @return Value of property m_iKapitaen.
	 */
	public final int getCaptain() {
		return m_iKapitaen;
	}

	/**
	 * Setter for property m_iKicker.
	 * 
	 * @param m_iKicker
	 *            New value of property m_iKicker.
	 */
	public final void setKicker(int m_iKicker) {
		this.setSetPiecesTaker(new MatchLineupPosition(IMatchRoleID.setPieces, m_iKicker,0));
	}

	/**
	 * Getter for property m_iKicker.
	 * 
	 * @return Value of property m_iKicker.
	 */
	public final int getKicker() {
		return m_iKicker;
	}



	/**
	 * convert reduced float rating (1.00....20.99) to original integer HT
	 * rating (1...80) one +0.5 is because of correct rounding to integer
	 */
	public static int HTfloat2int(double x) {
		return (int) (((x - 1.0f) * 4.0f) + 1.0f);
	}


	/**
	 * Get the short name for a formation constant.
	 */
	public static String getNameForSystem(byte system) {
		return switch (system) {
			case SYS_451 -> "4-5-1";
			case SYS_352 -> "3-5-2";
			case SYS_442 -> "4-4-2";
			case SYS_343 -> "3-4-3";
			case SYS_433 -> "4-3-3";
			case SYS_532 -> "5-3-2";
			case SYS_541 -> "5-4-1";
			case SYS_523 -> "5-2-3";
			case SYS_550 -> "5-5-0";
			case SYS_253 -> "2-5-3";
			default -> TranslationFacility.tr("Unbestimmt");
		};
	}

	/**
	 * Get the position type (byte in IMatchRoleID).
	 */
	public final byte getEffectivePos4PositionID(int positionsid) {
		try {
			var posid = getPositionById(positionsid);
			if ( posid != null){
				return posid.getPosition();
			}
		} catch (Exception e) {
			HOLogger.instance().error(getClass(), "getEffectivePos4PositionID: " + e);
		}
		return IMatchRoleID.UNKNOWN;
	}

	/**
	 * Setter for property m_sHeimspiel.
	 * 
	 * @param location
	 *            New value of property m_sHeimspiel.
	 */
	public final void setLocation(short location) {
		ratingRevision++;
		this.m_sLocation = location;
	}

	/**
	 * Get the location constant for the match (home/away/awayderby)
	 * 
	 * @return the location constant for the match
	 */
	public final short getLocation() {
		if ( m_sLocation == -1 && !isUpcomingMatchLoaded()) {	getUpcomingMatch();	}
		return m_sLocation;
	}

	public final  void setWeather( Weather weather)
	{
		ratingRevision++;
		this.m_cWeather = weather;
	}

	public final Weather getWeather()
	{
		if (m_cWeather == null || (m_cWeather == Weather.NULL && !isUpcomingMatchLoaded())) {getUpcomingMatch();	}
		return  m_cWeather;
	}

	public final void setWeatherForecast(Weather.Forecast weatherForecast){
		this.m_cWeatherForecast = weatherForecast;
	}

	public final Weather.Forecast getWeatherForecast()
	{
		if (this.m_cWeatherForecast == Weather.Forecast.NULL &&  !isUpcomingMatchLoaded()) {	getUpcomingMatch();	}
		return  m_cWeatherForecast;
	}

	private boolean isUpcomingMatchLoaded() { return m_iArenaId>=0; }

	public boolean setUpcomingMatch(MatchKurzInfo match){

		ratingRevision++;

        if (match == null) {
			m_sLocation = 0;
			m_iArenaId = 0;
            m_cWeather = Weather.NULL;
			m_cWeatherForecast = Weather.Forecast.NULL;
			HOLogger.instance().warning(getClass(), "no match to determine location");
			return false;
		}

		if (match.getMatchType().isOfficial()) {
			if (match.isNeutral()) {
				m_sLocation = IMatchDetails.LOCATION_NEUTRAL;
			}
			if (match.isDerby()) {
				m_sLocation = IMatchDetails.LOCATION_AWAYDERBY;
			}
			if (!match.isNeutral() && !match.isDerby()) {
				if (match.isHomeMatch()) {
					m_sLocation = IMatchDetails.LOCATION_HOME;
				} else {
					m_sLocation = IMatchDetails.LOCATION_AWAY;
				}
			}
		} else {
			m_sLocation = IMatchDetails.LOCATION_TOURNAMENT;
		}

		m_iArenaId = match.getArenaId();
        m_cWeather = match.getWeather();
		if (m_cWeather == null) m_cWeather = Weather.NULL;
		m_cWeatherForecast = match.getWeatherForecast();
		if (m_cWeatherForecast == null) m_cWeatherForecast = Weather.Forecast.NULL;

		return true;
	}

	private void getUpcomingMatch() {
		try {
			final int teamId = HOVerwaltung.instance().getModel().getBasics().getTeamId();
			MatchKurzInfo match = DBManager.instance().getFirstUpcomingMatchWithTeamId(teamId);
			setUpcomingMatch(match);
		} catch (Exception e) {
			HOLogger.instance().error(getClass(), "getUpcomingMatch: " + e);
			m_sLocation = 0;
		}
	}

	/**
	 * Umrechnung von double auf 1-80 int
	 *
	 * @deprecated use RatingUtil.getIntValue4Rating(double rating) instead
	 */
	@Deprecated
	public final int getIntValue4Rating(double rating) {
		return (int) (((float) (rating - 1) * 4f) + 1);
	}

	/**
	 * Get the player object by position id.
	 */
	public Player getPlayerByPositionID(int positionId) {
		try {
			var posid = getPositionById(positionId);
			if ( posid != null) {
				return HOVerwaltung.instance().getModel()
						.getCurrentPlayer(posid.getPlayerId());
			}
		} catch (Exception e) {
			HOLogger.instance()
					.error(getClass(), "getPlayerByPositionID(" + positionId + "): " + e);
		}
		return null;
	}

	public String tryGetPlayerNameByPositionID(int positionId) {
		try {
			var posid = getPositionById(positionId);
			if (posid != null){
				return HOVerwaltung.instance().getModel().getCurrentPlayer(posid.getPlayerId()).getShortName();
			}
		} catch (Exception ignored) {
		}
		return "           ";
	}

	/**
	 * Get the position object by position id.
	 */
	public final @Nullable MatchLineupPosition getPositionById(int iPositionID) {
		for (var position : m_vFieldPositions) {
			if (position.getId() == iPositionID) {
				return position;
			}
		}
		for (var position : m_vBenchPositions) {
			if (position.getId() == iPositionID) {
				return position;
			}
		}
		return null;
	}

	/**
	 * Get the position object by player id.
	 */
	public final MatchLineupPosition getPositionByPlayerId(int playerid) {
		return getPositionByPlayerId(playerid, true);
	}

	public final MatchLineupPosition getPositionByPlayerId(int playerid, boolean includeReplacedPlayers) {
		MatchLineupPosition ret = getPositionByPlayerId(playerid, m_vFieldPositions);
		if ( ret == null ) ret = getPositionByPlayerId(playerid, m_vBenchPositions);
		if ( ret == null & includeReplacedPlayers) ret = getPositionByPlayerId(playerid, replacedPositions);
		return ret;
	}

	private MatchLineupPosition getPositionByPlayerId(int playerid, Vector<MatchLineupPosition> positions) {
		for (MatchLineupPosition position : positions) {
			if (position.getPlayerId() == playerid) {
				return position;
			}
		}
		return null;
	}

	public final void setPosition(MatchLineupPosition position)
	{
		if ( position.isFieldMatchRoleId()){
			ratingRevision++;
			setPosition(this.m_vFieldPositions, position);
		}
		else if (position.isSubstitutesMatchRoleId() || position.isBackupsMatchRoleId()){
			setPosition(this.m_vBenchPositions,position);
		}
		else if ( position.isPenaltyTakerMatchRoleId()){
			setPosition(this.penaltyTakers,position);
		}
		else if ( position.getId() == IMatchRoleID.setPieces){
			setSetPiecesTaker(position);
		}
		else if ( position.getId() == IMatchRoleID.captain){
			setCaptain(position);
		}
		else if ( position.isReplacedMatchRoleId()){
			setPosition(this.replacedPositions,position);
		}
	}

	private void setPosition(Vector<MatchLineupPosition> m_vPositionen, MatchLineupPosition spos) {
		for (int j = 0; j < m_vPositionen.size(); j++) {
			if (m_vPositionen.get(j).getId() == spos.getId()) {
				m_vPositionen.setElementAt(spos, j);
				return;
			}
		}
	}

	/**
	 * Clears all positions of content by creating a new, empty lineup.
	 */
	public final void clearLineup() {
		ratingRevision++;
		initPositionen553();
	}

	/**
	 * Getter for property m_vPositionen.
	 *
	 * @return Value of property m_vPositionen.
	 */
	public final Vector<MatchLineupPosition> getAllPositions() {
		Vector<MatchLineupPosition> ret = new Vector<>();
		if (m_vFieldPositions != null) ret.addAll(m_vFieldPositions);
		if (m_vBenchPositions != null) ret.addAll(m_vBenchPositions);
		if (replacedPositions != null) ret.addAll(replacedPositions);
		if (penaltyTakers != null) ret.addAll(penaltyTakers);
		if (captain != null) ret.add(captain);
		if (setPiecesTaker != null) ret.add(setPiecesTaker);
		return ret;
	}

	public final Vector<MatchLineupPosition> getFieldPositions(){
		return m_vFieldPositions;
	}

	public Vector<MatchLineupPosition> getReplacedPositions(){return replacedPositions;}

	/**
	 * Place a player to a certain position and check/solve dependencies.
	 */
	public final void setSpielerAtPosition(int positionsid, int spielerid, byte tactic) {
		final MatchLineupPosition pos = getPositionById(positionsid);
		if (pos != null) {
			setSpielerAtPosition(positionsid, spielerid);
			pos.setBehaviour(tactic);

			pos.getPosition();
		}
	}

	/**
	 * Place a player to a certain position and check/solve dependencies.
	 */
	public final void setSpielerAtPosition(int positionID, int playerID) {
		final MatchRoleID position = getPositionById(positionID);
		if ( position != null) {
			if ( position.getPlayerId() != playerID) {
				if ( playerID != 0 ) {
					MatchRoleID oldPlayerRole = getPositionByPlayerId(playerID);
					if (oldPlayerRole != null) {
						if (position.isFieldMatchRoleId()) {
							ratingRevision++;
							//if player changed is in starting eleven it has to be remove from previous occupied positions
							oldPlayerRole.setPlayerIdIfValidForLineup(0, this);
							if (oldPlayerRole.isSubstitutesMatchRoleId()) {
								removeObjectPlayerFromSubstitutions(playerID);
								// player can occupy multiple bench positions
								oldPlayerRole = getPositionByPlayerId(playerID);
								while (oldPlayerRole != null) {
									oldPlayerRole.setPlayerIdIfValidForLineup(0, this);
									oldPlayerRole = getPositionByPlayerId(playerID);
								}
							}
						} else {
							// position is on bench (or backup), remove him from field position, but not from other bench positions
							if (oldPlayerRole.isFieldMatchRoleId()) {
								ratingRevision++;
								oldPlayerRole.setPlayerIdIfValidForLineup(0, this);
							}
						}
					}
				}
				position.setPlayerIdIfValidForLineup(playerID, this);
			}
		}
	}

	/**
	 * Player is no longer on the bench and must be removed from substitution list
	 * if it was planned that he should replace another player
	 */
	private void removeObjectPlayerFromSubstitutions(int playerID) {
		for(Substitution substitution: this.substitutions){
			if (substitution.getOrderType() == MatchOrderType.SUBSTITUTION &&
					substitution.getObjectPlayerID() == playerID){
				ratingRevision++;
				this.substitutions.remove(substitution);
				break;
			}
		}
	}

	/**
	 * Check, if the player is in the lineup.
	 */
	public final boolean isPlayerInLineup(int spielerId) {
		//return m_clAssi.isPlayerInLineup(spielerId, m_vPositionen);
		return getPositionByPlayerId(spielerId) != null;
	}

	/**
	 * Check, if the player is in the starting 11.
	 */
	public final boolean isPlayerInStartingEleven(int spielerId) {
		//return m_clAssi.isPlayerInStartingEleven(spielerId, m_vPositionen);
		return getPositionByPlayerId(spielerId, m_vFieldPositions) != null;
	}

	/**
	 * Check, if the player is a subsitute
	 */
	public final boolean isPlayerASub(int spielerId) {
		//return m_clAssi.isPlayerASub(spielerId, m_vPositionen);
		final MatchRoleID role = getPositionByPlayerId(spielerId, m_vBenchPositions);
		return role != null && !role.isBackupsMatchRoleId();
	}

	/**
	 * Check, if the player is a substitute or a backup.
	 */
	public final boolean isSpielerInReserve(int spielerId) {
		//return (m_clAssi.isPlayerInLineup(spielerId, m_vPositionen) && !m_clAssi
		//			.isPlayerInStartingEleven(spielerId, m_vPositionen));
		return getPositionByPlayerId(spielerId, m_vBenchPositions) != null;
	}


	/**
	 * Returns a list of match orders for this lineup.
	 *
	 * @return the substitutions for this lineup. If there are no substitutions,
	 *         an empty list will be returned.
	 */
	public List<Substitution> getSubstitutionList() {
		return this.substitutions;
	}

	/**
	 * Sets the provided list of match orders as list.
	 * list may contain a maximum of three substitutions
	 * additional number of position swap and behaviour changes depends on tactic assistant level
	 * list may contain one man marking order
	 *
	 * @param subs List of match orders
	 */
	public void setSubstitionList(List<Substitution> subs) {
		ratingRevision++;
		if (subs == null) {
			this.substitutions = new ArrayList<>();
		} else {
			this.substitutions = new ArrayList<>(subs);
		}
	}

	/**
	 * Set a new man marking match order.
	 * An existing man marking order will be replaced by the new one.
	 *
	 * @param markerId id of own man marking player
	 * @param opponentMarkedId id of opponents man marked player
	 */
	public void setManMarkingOrder(int markerId, int opponentMarkedId){
		removeManMarkingOrder();	//there can only be one
		var newSub = new Substitution(MatchOrderType.MAN_MARKING);
		newSub.setSubjectPlayerID(markerId);
		newSub.setObjectPlayerID(opponentMarkedId);
		this.substitutions.add(newSub);
	}

	/**
	 * Remove an existing man marking order.
	 * Does nothing if no man marking order exists.
	 */
	public void removeManMarkingOrder() {
		ratingRevision++;
		for ( var s : this.substitutions){
			if ( s.getOrderType() == MatchOrderType.MAN_MARKING) {
				this.substitutions.remove(s);
				break;
			}
		}
	}

	public Substitution getManMarkingOrder(){
		for ( var s : this.substitutions){
			if ( s.getOrderType() == MatchOrderType.MAN_MARKING) {
				return s;
			}
		}
		return null;
	}

	public List<MatchLineupPosition> getPenaltyTakers() {
		return this.penaltyTakers;
	}

	public void setPenaltyTakers(List<MatchLineupPosition> positions) {
		this.penaltyTakers = new Vector<>(positions);
		// chpp match order requires exactly 11 penalty takers
		for ( int i=this.penaltyTakers.size(); i<11; i++){
			this.penaltyTakers.add(new MatchLineupPosition(0,0,IMatchRoleID.NORMAL));
		}
	}

	/**
	 * Get the system name.
	 */
	public final String getSystemName(byte system) {
		return getNameForSystem(system);
	}

	/**
	 * Get tactic type for a position-id.
	 */
	public final byte getTactic4PositionID(int positionsid) {
		try {
			var posid = getPositionById(positionsid);
			if ( posid != null){
				return posid.getTactic();
			}
		} catch (Exception e) {
			HOLogger.instance().error(getClass(), "getTactic4PositionID: " + e);
		}
		return IMatchRoleID.UNKNOWN;
	}

//	public final float getTacticLevel(int type) {
//		return switch (type) {
//			case IMatchDetails.TAKTIK_PRESSING -> getTacticLevelPressing();
//			case IMatchDetails.TAKTIK_KONTER -> getTacticLevelCounter();
//			case IMatchDetails.TAKTIK_MIDDLE, IMatchDetails.TAKTIK_WINGS -> getTacticLevelAimAow();
//			case IMatchDetails.TAKTIK_LONGSHOTS -> getTacticLevelLongShots();
//			case IMatchDetails.TAKTIK_CREATIVE -> getTacticLevelCreative();
//			default -> 0.0f;
//		};
//	}

	/**
	 * Setter for property m_iTacticType.
	 * 
	 * @param m_iTacticType
	 *            New value of property m_iTacticType.
	 */
	public final void setTacticType(int m_iTacticType) {
		ratingRevision++;
		this.settings.m_iTacticType = m_iTacticType;
	}

	/**
	 * Getter for property m_iTacticType.
	 * 
	 * @return Value of property m_iTacticType.
	 */
	public final int getTacticType() {
		return settings.m_iTacticType;
	}

	/**
	 * Get the formation xp for the current formation.
	 */
	public final int getExperienceForCurrentTeamFormation() {
		return switch (getCurrentTeamFormationCode()) {
			case SYS_MURKS -> -1;
			case SYS_451 -> HOVerwaltung.instance().getModel().getTeam().getFormationExperience451();
			case SYS_352 -> HOVerwaltung.instance().getModel().getTeam().getFormationExperience352();
			case SYS_442 -> HOVerwaltung.instance().getModel().getTeam().getFormationExperience442();
			case SYS_343 -> HOVerwaltung.instance().getModel().getTeam().getFormationExperience343();
			case SYS_433 -> HOVerwaltung.instance().getModel().getTeam().getFormationExperience433();
			case SYS_532 -> HOVerwaltung.instance().getModel().getTeam().getFormationExperience532();
			case SYS_541 -> HOVerwaltung.instance().getModel().getTeam().getFormationExperience541();
			case SYS_523 -> HOVerwaltung.instance().getModel().getTeam().getFormationExperience523();
			case SYS_550 -> HOVerwaltung.instance().getModel().getTeam().getFormationExperience550();
			case SYS_253 -> HOVerwaltung.instance().getModel().getTeam().getFormationExperience253();
			default -> -1;
		};
	}


	/**
	 * Check if the players are still in the team (not sold or fired).
	 */
	public final void checkAufgestellteSpieler() {
		for (var pos : getAllPositions()) {
			// existiert Player noch ?
			if ((HOVerwaltung.instance().getModel() != null)
					&& (HOVerwaltung.instance().getModel().getCurrentPlayer(pos.getPlayerId()) == null)) {
				// nein dann zuweisung aufheben
				pos.setPlayerIdIfValidForLineup(0, this);
			}
		}
	}

	/**
	 * Assistant to create automatically the lineup
	 */
	public final void optimizeLineup(List<Player> players, byte sectorsStrengthPriority, boolean withForm,
									 boolean idealPosFirst, boolean considerInjured, boolean considereSuspended, boolean useAverageRating) {
		m_clAssi.doLineup(getAllPositions(), players, sectorsStrengthPriority, withForm, idealPosFirst,
				considerInjured, considereSuspended, useAverageRating, getWeather());
		setAutoKicker(null);
		setAutoKapitaen(null);
	}

	public final String getCurrentTeamFormationString() {
		final int iNbDefs = getNbDefenders();
		final int iNbMids = getNbMidfields();
		final int iNbFwds = getNbForwards();
		return iNbDefs + "-" + iNbMids + "-" + iNbFwds;
	}

	public final byte getCurrentTeamFormationCode() {
		final int defenders = getNbDefenders();
		final int midfielders = getNbMidfields();
		final int forwards = getNbForwards();
		if (defenders == 2) {
			// 253
			if (midfielders == 5 && forwards == 3) {
				return SYS_253;
			}
			// MURKS
			else {
				return SYS_MURKS;
			}
		} else if (defenders == 3) {
			// 343
			if (midfielders == 4 && forwards == 3) {
				return SYS_343;
			} // 352
			else if (midfielders == 5 && forwards == 2) {
				return SYS_352;
			}
			// MURKS
			else {
				return SYS_MURKS;
			}
		} else if (defenders == 4) {
			// 433
			if (midfielders == 3 && forwards == 3) {
				return SYS_433;
			} // 442
			else if (midfielders == 4 && forwards == 2) {
				return SYS_442;
			} // 451
			else if (midfielders == 5 && forwards == 1) {
				return SYS_451;
			}
			// MURKS
			else {
				return SYS_MURKS;
			}
		} else if (defenders == 5) {
			// 532
			if (midfielders == 3 && forwards == 2) {
				return SYS_532;
			} // 541
			else if (midfielders == 4 && forwards == 1) {
				return SYS_541;
			} // 523
			else if (midfielders == 2 && forwards == 3) {
				return SYS_523;
			} // 550
			else if (midfielders == 5 && forwards == 0) {
				return SYS_550;
			}
			// MURKS
			else {
				return SYS_MURKS;
			}
		} // MURKS
		else {
			return SYS_MURKS;
		}
	}

	private void swapContentAtPositions(int pos1, int pos2) {
		int id1 = 0;
		int id2 = 0;

		var tac1 = getTactic4PositionID(pos1);
		var tac2 = getTactic4PositionID(pos2);

		if (getPlayerByPositionID(pos1) != null) {
			id1 = getPlayerByPositionID(pos1).getPlayerId();
			setSpielerAtPosition(pos1, 0);
		}
		if (getPlayerByPositionID(pos2) != null) {
			id2 = getPlayerByPositionID(pos2).getPlayerId();
			setSpielerAtPosition(pos2, 0);
		}
		setSpielerAtPosition(pos2, id1, tac1);
		setSpielerAtPosition(pos1, id2, tac2);
	}

	/**
	 * Swap corresponding right/left players and orders.
	 */
	public final void flipSide() {
		swapContentAtPositions(IMatchRoleID.rightBack, IMatchRoleID.leftBack);
		swapContentAtPositions(IMatchRoleID.rightCentralDefender,
				IMatchRoleID.leftCentralDefender);
		swapContentAtPositions(IMatchRoleID.rightWinger, IMatchRoleID.leftWinger);
		swapContentAtPositions(IMatchRoleID.rightInnerMidfield,
				IMatchRoleID.leftInnerMidfield);
		swapContentAtPositions(IMatchRoleID.rightForward, IMatchRoleID.leftForward);
	}

	/**
	 * Remove all players from all positions.
	 */
	public final void resetStartingLineup() {
		m_clAssi.resetPositionsbesetzungen(getAllPositions());
	}

	/**
	 * Remove a spare player.
	 */
	public final void resetSubstituteBench() {
		m_clAssi.resetPositionsbesetzungen(m_vBenchPositions);
	}

	/**
	 * Resets the orders for all positions to normal
	 */
	public final void resetPositionOrders() {
		m_clAssi.resetPositionOrders(m_vFieldPositions);
	}


	/**
	 * Calculate the amount af defenders.
	 */
	private int getNbDefenders() {
		int anzahl = 0;
		anzahl += getAnzPosImSystem(IMatchRoleID.BACK);
		anzahl += getAnzPosImSystem(IMatchRoleID.BACK_TOMID);
		anzahl += getAnzPosImSystem(IMatchRoleID.BACK_OFF);
		anzahl += getAnzPosImSystem(IMatchRoleID.BACK_DEF);
		return anzahl + getAnzInnenverteidiger();
	}

	/**
	 * Calculate the amount of central defenders.
	 */
	private int getAnzInnenverteidiger() {
		int anzahl = 0;
		anzahl += getAnzPosImSystem(IMatchRoleID.CENTRAL_DEFENDER);
		anzahl += getAnzPosImSystem(IMatchRoleID.CENTRAL_DEFENDER_TOWING);
		anzahl += getAnzPosImSystem(IMatchRoleID.CENTRAL_DEFENDER_OFF);
		return anzahl;
	}

	/**
	 * Get the total amount of midfielders in the lineup.
	 */
	private int getNbMidfields() {
		int anzahl = 0;
		anzahl += getAnzPosImSystem(IMatchRoleID.WINGER);
		anzahl += getAnzPosImSystem(IMatchRoleID.WINGER_TOMID);
		anzahl += getAnzPosImSystem(IMatchRoleID.WINGER_OFF);
		anzahl += getAnzPosImSystem(IMatchRoleID.WINGER_DEF);
		return anzahl + getAnzInneresMittelfeld();
	}

	/**
	 * Get the amount of inner midfielders in the lineup.
	 */
	private int getAnzInneresMittelfeld() {
		int anzahl = 0;
		anzahl += getAnzPosImSystem(IMatchRoleID.MIDFIELDER);
		anzahl += getAnzPosImSystem(IMatchRoleID.MIDFIELDER_OFF);
		anzahl += getAnzPosImSystem(IMatchRoleID.MIDFIELDER_DEF);
		anzahl += getAnzPosImSystem(IMatchRoleID.MIDFIELDER_TOWING);
		return anzahl;
	}

	/**
	 * Get the amount of strikers in the lineup.
	 */
	private int getNbForwards() {
		int anzahl = 0;
		anzahl += getAnzPosImSystem(IMatchRoleID.FORWARD);
		anzahl += getAnzPosImSystem(IMatchRoleID.FORWARD_DEF);
		anzahl += getAnzPosImSystem(IMatchRoleID.FORWARD_TOWING);
		return anzahl;
	}

	/**
	 * Generic "counter" for the given position in the current lineup.
	 */
	private int getAnzPosImSystem(byte positionId) {
		int anzahl = 0;
		for (var pos : m_vFieldPositions) {
			if ((positionId == pos.getPosition())
					&& (pos.getId() < IMatchRoleID.startReserves)
					&& (pos.getPlayerId() > 0)) {
				++anzahl;
			}
		}
		return anzahl;
	}

	/**
	 * @return true if less than 11 players on field, false if 11 (or more) on
	 *         field
	 */
	public boolean hasFreePosition() {
		int numPlayers = 0;
		for (var pos : m_vFieldPositions) {
			if (pos.getPlayerId() != 0) numPlayers++;
		}
		return numPlayers != 11;
	}

	/**
	 * Initializes the 553 lineup
	 */
	private void initPositionen553() {

		ratingRevision++;

		m_vFieldPositions = new Vector<>();
		for ( int i=IMatchRoleID.keeper; i<= IMatchRoleID.leftForward; i++) {
			m_vFieldPositions.add(new MatchLineupPosition(i, 0, (byte)0 ));
		}
		m_vBenchPositions = new Vector<>();
		for ( int i=IMatchRoleID.substGK1; i<= IMatchRoleID.substXT2; i++) {
			m_vBenchPositions.add(new MatchLineupPosition(i, 0, (byte)0 ));
		}
		penaltyTakers = new Vector<>();
		for (int i = IMatchRoleID.penaltyTaker1; i <= IMatchRoleID.penaltyTaker11; i++) {
			penaltyTakers.add(new MatchLineupPosition( i, 0, (byte) 0));
		}
		replacedPositions = new Vector<>();
		for (int i = IMatchRoleID.FirstPlayerReplaced; i <= IMatchRoleID.ThirdPlayerReplaced; i++) {
			replacedPositions.add(new MatchLineupPosition( i, 0, (byte) 0));
		}
	}

	/**
	 * @return the pullBackMinute
	 */
	public int getPullBackMinute() {
		return pullBackMinute;
	}

	/**
	 * @param pullBackMinute
	 *            the pullBackMinute to set
	 */
	public void setPullBackMinute(int pullBackMinute) {
		this.pullBackMinute = pullBackMinute;
	}

	public List<MatchLineupPosition> getFieldPlayers(int minute){
		var ret = new ArrayList<>(this.getFieldPositions());
		this.substitutions.stream()
				.filter(s->s.getMatchMinuteCriteria()<=minute)
				.sorted(Comparator.comparingInt(Substitution::getMatchMinuteCriteria))
				.forEach(s->applySubstitution(ret, s));
		return ret;
	}

	public Set<Integer> getLineupChangeMinutes(){
		return this.substitutions.stream().map(e->max(0,e.getMatchMinuteCriteria())).collect(Collectors.toSet());
	}

	public static void applySubstitution(List<MatchLineupPosition> positions, Substitution s) {
		switch (s.getOrderType()) {

			case SUBSTITUTION -> {
				var substitutedPlayer = positions.stream().filter(p -> p.getPlayerId() == s.getSubjectPlayerID()).findFirst();
				if (substitutedPlayer.isPresent()) {
					var substituted = substitutedPlayer.get();
					var behaviour = s.getBehaviour();
					if ( behaviour == -1 ) behaviour = substituted.getBehaviour();
					var roleId = getRoleIdAfterSubstitution(positions, s, substituted);
					if ( roleId == -1) {
						return; // invalid role id
					}
					positions.remove(substitutedPlayer.get());
					positions.add(new MatchLineupPosition(roleId, s.getObjectPlayerID(), behaviour, s.getMatchMinuteCriteria()));
				}
				else {
					HOLogger.instance().warning(Lineup.class, String.format("The player id: %s cannot do the substitution", s.getSubjectPlayerID()));
				}
			}
			case POSITION_SWAP -> {
				var player1 = positions.stream().filter(p->p.getPlayerId() == s.getSubjectPlayerID()).findFirst();
				if ( player1.isPresent()){
					var player2 = positions.stream().filter(p->p.getPlayerId() == s.getObjectPlayerID()).findFirst();
					if ( player2.isPresent()){
						// do not change existing positions, since they are used in caches of prediction models, create new ones
						positions.remove(player1.get());
						positions.remove(player2.get());
						positions.add(new MatchLineupPosition(player1.get().getRoleId(), player2.get().getPlayerId(), player2.get().getBehaviour(), player2.get().getStartMinute()));
						positions.add(new MatchLineupPosition(player2.get().getRoleId(), player1.get().getPlayerId(), player1.get().getBehaviour(), player1.get().getStartMinute()));
					}
					else {
						HOLogger.instance().warning(Lineup.class, String.format("The player id: %s is (no longer) in lineup.", s.getObjectPlayerID()));
					}
				}
				else{
					HOLogger.instance().warning(Lineup.class, String.format("The player id: %s is (no longer) in lineup.", s.getSubjectPlayerID()));
				}
			}
			case NEW_BEHAVIOUR -> {
				var player = positions.stream().filter(p->p.getPlayerId() == s.getSubjectPlayerID()).findFirst();
				if ( player.isPresent()){
					var roleId = getRoleIdAfterSubstitution(positions, s, player.get());
					if ( roleId == -1) return;
					var behaviour = s.getBehaviour();
					if ( behaviour == -1 ) behaviour = MatchRoleID.NORMAL;
					positions.remove(player.get());
					positions.add(new MatchLineupPosition(roleId, player.get().getPlayerId(), behaviour, player.get().getStartMinute()));
				}
				else {
					HOLogger.instance().warning(Lineup.class, String.format("The player id: %s is (no longer) in lineup.", s.getSubjectPlayerID()));
				}
			}
			case MAN_MARKING -> {
				//TODO
			}
		}
	}

	private static byte getRoleIdAfterSubstitution(List<MatchLineupPosition> positions, Substitution s, MatchLineupPosition substituted) {
		var roleId = s.getRoleId();
		if ( roleId == -1){
			roleId = (byte)substituted.getRoleId();
		}
		else if ( roleId != (byte) substituted.getRoleId()){
			// Check if position is free
			int r = roleId;
			var playerAtRoleid = positions.stream().filter(p->p.getRoleId()==r).findFirst();
			if ( playerAtRoleid.isPresent()){
				HOLogger.instance().warning(Lineup.class, String.format("The player id: %s cannot do the substitution. Position is not free.", s.getObjectPlayerID()));
				return -1; // invalid
			}
		}
		return roleId;
	}

	public void adjustBackupPlayers() {
		Player player = this.getPlayerByPositionID(IMatchRoleID.substGK1);
		int substGK = (player == null) ? 0 : player.getPlayerId();

		player = this.getPlayerByPositionID(IMatchRoleID.substCD1);
		int substCD = (player == null) ? 0 : player.getPlayerId();

		player = this.getPlayerByPositionID(IMatchRoleID.substWB1);
		int substWB = (player == null) ? 0 : player.getPlayerId();

		player = this.getPlayerByPositionID(IMatchRoleID.substIM1);
		int substIM = (player == null) ? 0 : player.getPlayerId();

		player = this.getPlayerByPositionID(IMatchRoleID.substFW1);
		int substFW = (player == null) ? 0 : player.getPlayerId();

		player = this.getPlayerByPositionID(IMatchRoleID.substWI1);
		int substWI = (player == null) ? 0 : player.getPlayerId();

		player = this.getPlayerByPositionID(IMatchRoleID.substXT1);
		int substXT = (player == null) ? 0 : player.getPlayerId();

		ArrayList<Integer> authorisedBackup = new ArrayList<>(Arrays.asList(substGK, substCD, substWB, substIM, substFW, substWI, substXT));

		// remove player from backup position if not listed as a sub anymore
		adjustBackupPlayer(IMatchRoleID.substGK2, substGK, authorisedBackup);
		adjustBackupPlayer(IMatchRoleID.substCD2, substCD, authorisedBackup);
		adjustBackupPlayer(IMatchRoleID.substWB2, substWB, authorisedBackup);
		adjustBackupPlayer(IMatchRoleID.substIM2, substIM, authorisedBackup);
		adjustBackupPlayer(IMatchRoleID.substFW2, substFW, authorisedBackup);
		adjustBackupPlayer(IMatchRoleID.substWI2, substWI, authorisedBackup);
		adjustBackupPlayer(IMatchRoleID.substXT2, substXT, authorisedBackup);

	}

	public void adjustBackupPlayer(int iBackupPositionID, int iPlayerIDCorrespondingSub, ArrayList<Integer> authorisedBackup) {
		Player player = this.getPlayerByPositionID(iBackupPositionID);
		if (player != null){
			int backupID = player.getPlayerId();
			if ((backupID == iPlayerIDCorrespondingSub) || !(authorisedBackup.contains(backupID)))
			{
				// need to remove that player from that backup position
				this.setSpielerAtPosition(iBackupPositionID, 0);
			}
		}
	}

	public String toJson()
	{
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		return gson.toJson(this);
	}

	/**
	► IFK:
	off IFK = 0.5*mAtt + 0.3*mSP + 0.09*SPshooter
	def IFK = 0.4*mDef + .3*mSP + 0.1*SPgk + 0.08*GKgk

	mAtt: outfield players attack skill average
	mDEf: outfield players defence skill average
	 */
	public double getRatingIndirectSetPiecesAtt() {
		double teamAtt = 0; // team score sum
		double teamSP = 0;  // team set pieces sum
		double spSP = 0;    // set pieces taker set pieces
		int n = 0;

		for (var pos : m_vFieldPositions) {
			Player p = this.getPlayerByPositionID(pos.getId());
			if ( p != null){
				teamAtt += p.getScoringSkill();
				teamSP += p.getSetPiecesSkill();
				if ( p.getPlayerId() == getKicker()){
					spSP = p.getSetPiecesSkill();
				}
			}
			n++;
		}
		if ( n > 1){
			teamAtt/=n;
			teamSP /=n;
		}
		return .5*teamAtt + .3*teamSP + .09*spSP;
	}

	public double getRatingIndirectSetPiecesDef() {
		double teamDef = 0; // team defence sum
		double teamSP = 0;  // team set pieces sum
		double spGK = 0;    // keeper's set pieces
		double gkGK = 0;  	// keeper's keeper skill
		int n = 0;

		Player keeper = getPlayerByPositionID(IMatchRoleID.keeper);
		if ( keeper != null){
			spGK = keeper.getSetPiecesSkill();
			gkGK = keeper.getGoalkeeperSkill();
		}

		for (var pos : m_vFieldPositions) {
			Player p = this.getPlayerByPositionID(pos.getId());
			if ( p != null){
				n++;
				teamDef += p.getDefendingSkill();
				teamSP += p.getSetPiecesSkill();
			}
		}
		if ( n > 1){
			teamDef/=n;
			teamSP /=n;
		}
		return .4*teamDef + .3*teamSP + .1*spGK + .08*gkGK;
	}

}
