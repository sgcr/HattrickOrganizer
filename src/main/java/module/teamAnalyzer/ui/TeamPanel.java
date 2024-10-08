package module.teamAnalyzer.ui;

import core.gui.comp.panel.RasenPanel;
import core.model.HOVerwaltung;
import core.model.TranslationFacility;
import core.model.player.IMatchRoleID;
import core.model.player.Player;
import core.rating.RatingPredictionModel;
import module.lineup.Lineup;
import module.teamAnalyzer.SystemManager;
import module.teamAnalyzer.manager.PlayerDataManager;
import module.teamAnalyzer.ui.lineup.FormationPanel;
import module.teamAnalyzer.vo.TeamLineup;
import module.teamAnalyzer.vo.UserTeamSpotLineup;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;

public class TeamPanel extends JPanel {
    //~ Instance fields ----------------------------------------------------------------------------
	private final FormationPanel lineupPanel = new FormationPanel();
    private final PlayerPanel keeper = new PlayerPanel();
    private final PlayerPanel leftAttacker = new PlayerPanel();
    private final PlayerPanel leftBack = new PlayerPanel();
    private final PlayerPanel leftCentral = new PlayerPanel();
    private final PlayerPanel leftMidfielder = new PlayerPanel();
    private final PlayerPanel leftWinger = new PlayerPanel();
    private final PlayerPanel rightAttacker = new PlayerPanel();
    private final PlayerPanel rightBack = new PlayerPanel();
    private final PlayerPanel rightCentral = new PlayerPanel();
    private final PlayerPanel rightMidfielder = new PlayerPanel();
    private final PlayerPanel rightWinger = new PlayerPanel();
    private final PlayerPanel middleCentral = new PlayerPanel();
    private final PlayerPanel centralMidfielder = new PlayerPanel();
    private final PlayerPanel centralAttacker = new PlayerPanel ();
    JPanel grassPanel = new RasenPanel();

    ManMarkingOrderDisplay manMarkingOrderDisplay;

    //~ Constructors -------------------------------------------------------------------------------

    /**
     * Creates a new TeamPanel object.
     */
    public TeamPanel() {
        jbInit();
    }

    //~ Methods ------------------------------------------------------------------------------------
    public TeamLineupData getMyTeamLineupPanel() {
        return lineupPanel.getMyTeam();
    }

    public TeamLineupData getOpponentTeamLineupPanel() {
        return lineupPanel.getOpponentTeam();
    }

    public void jbInit() {
        grassPanel.setLayout(new BorderLayout());
        grassPanel.add(lineupPanel, BorderLayout.CENTER);
        setMyTeam();
        setLayout(new BorderLayout());
        fillPanel(lineupPanel.getOpponentTeam().getKeeperPanel(), keeper);
        fillPanel(lineupPanel.getOpponentTeam().getLeftWingbackPanel(), leftBack);
        fillPanel(lineupPanel.getOpponentTeam().getLeftCentralDefenderPanel(), leftCentral);
        fillPanel(lineupPanel.getOpponentTeam().getRightCentralDefenderPanel(), rightCentral);
        fillPanel(lineupPanel.getOpponentTeam().getRightWingbackPanel(), rightBack);
        fillPanel(lineupPanel.getOpponentTeam().getLeftWingPanel(), leftWinger);
        fillPanel(lineupPanel.getOpponentTeam().getLeftMidfieldPanel(), leftMidfielder);
        fillPanel(lineupPanel.getOpponentTeam().getRightMidfieldPanel(), rightMidfielder);
        fillPanel(lineupPanel.getOpponentTeam().getRightWingPanel(), rightWinger);
        fillPanel(lineupPanel.getOpponentTeam().getLeftForwardPanel(), leftAttacker);
        fillPanel(lineupPanel.getOpponentTeam().getRightForwardPanel(), rightAttacker);
        fillPanel(lineupPanel.getOpponentTeam().getCentralForwardPanel(), centralAttacker);
        fillPanel(lineupPanel.getOpponentTeam().getCentralMidfieldPanel(), centralMidfielder);
        fillPanel(lineupPanel.getOpponentTeam().getMiddleCentralDefenderPanel(), middleCentral);
        

        JScrollPane scrollPane = new JScrollPane(grassPanel);

//        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
//        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void reload(TeamLineup teamLineup, int week, int season) {
        if (teamLineup != null) {
            lineupPanel.getOpponentTeam().setTeamName(SystemManager.getActiveTeamName() + " ("
                                                      + SystemManager.getActiveTeamId() + ")");
            keeper.reload(teamLineup.getSpotLineup(IMatchRoleID.keeper), week, season);
            leftBack.reload(teamLineup.getSpotLineup(IMatchRoleID.leftBack), week, season);
            leftCentral.reload(teamLineup.getSpotLineup(IMatchRoleID.leftCentralDefender), week, season);
            rightCentral.reload(teamLineup.getSpotLineup(IMatchRoleID.rightCentralDefender), week, season);
            rightBack.reload(teamLineup.getSpotLineup(IMatchRoleID.rightBack), week, season);
            leftWinger.reload(teamLineup.getSpotLineup(IMatchRoleID.leftWinger), week, season);
            leftMidfielder.reload(teamLineup.getSpotLineup(IMatchRoleID.leftInnerMidfield), week, season);
            rightMidfielder.reload(teamLineup.getSpotLineup(IMatchRoleID.rightInnerMidfield), week, season);
            rightWinger.reload(teamLineup.getSpotLineup(IMatchRoleID.rightWinger), week, season);
            leftAttacker.reload(teamLineup.getSpotLineup(IMatchRoleID.leftForward), week, season);
            rightAttacker.reload(teamLineup.getSpotLineup(IMatchRoleID.rightForward), week, season);
            centralAttacker.reload(teamLineup.getSpotLineup(IMatchRoleID.centralForward), week, season);
            centralMidfielder.reload(teamLineup.getSpotLineup(IMatchRoleID.centralInnerMidfield), week, season);
            middleCentral.reload(teamLineup.getSpotLineup(IMatchRoleID.middleCentralDefender), week, season);
            
            lineupPanel.getOpponentTeam().setLeftAttack(teamLineup.getRating().getLeftAttack());
            lineupPanel.getOpponentTeam().setLeftDefence(teamLineup.getRating().getLeftDefense());
            lineupPanel.getOpponentTeam().setRightAttack(teamLineup.getRating().getRightAttack());
            lineupPanel.getOpponentTeam().setRightDefence(teamLineup.getRating().getRightDefense());
            lineupPanel.getOpponentTeam().setMiddleAttack(teamLineup.getRating().getCentralAttack());
            lineupPanel.getOpponentTeam().setMiddleDefence(teamLineup.getRating().getCentralDefense());
            lineupPanel.getOpponentTeam().setMidfield(teamLineup.getRating().getMidfield());

            setMyTeam();

            if ( this.lineupPanel.displayBothTeams() ) {

                // Display man marking order
                Lineup ownLineup = getOwnLineup();
                if ( ownLineup != null) {
                    var manMarkingOrder = ownLineup.getManMarkingOrder();
                    if (manMarkingOrder != null) {
                        var manMarker = manMarkingOrder.getSubjectPlayerID();
                        var manMarkerPos = ownLineup.getPositionByPlayerId(manMarker).getId();
                        var manMarkedPos = teamLineup.getPositionByPlayerId(manMarkingOrder.getObjectPlayerID());
                        var from = lineupPanel.getMyTeam().getPanel(manMarkerPos);
                        if (manMarkingOrderDisplay == null) {
                            manMarkingOrderDisplay = new ManMarkingOrderDisplay(grassPanel);
                        }
                        if (manMarkedPos > 0) {
                            var to = lineupPanel.getOpponentTeam().getPanel(manMarkedPos);
                            manMarkingOrderDisplay.set(from, to);
                        } else {
                            // TODO: Display warning about failed man marking order
                            manMarkingOrderDisplay.set(from, from);
                        }
                    } else if (manMarkingOrderDisplay != null) {
                        manMarkingOrderDisplay = null;
                    }
                }
            }

        } else {
            lineupPanel.getOpponentTeam().setTeamName(TranslationFacility.tr("TeamPanel.TeamMessage")); //$NON-NLS-1$

            keeper.reload(null, 0, 0);
            leftBack.reload(null, 0, 0);
            leftCentral.reload(null, 0, 0);
            rightCentral.reload(null, 0, 0);
            rightBack.reload(null, 0, 0);
            leftWinger.reload(null, 0, 0);
            leftMidfielder.reload(null, 0, 0);
            rightMidfielder.reload(null, 0, 0);
            rightWinger.reload(null, 0, 0);
            leftAttacker.reload(null, 0, 0);
            rightAttacker.reload(null, 0, 0);
            centralAttacker.reload(null, 0, 0);
            centralMidfielder.reload(null, 0, 0);
            middleCentral.reload(null, 0, 0);
            
            
            lineupPanel.getOpponentTeam().setLeftAttack(0);
            lineupPanel.getOpponentTeam().setLeftDefence(0);
            lineupPanel.getOpponentTeam().setRightAttack(0);
            lineupPanel.getOpponentTeam().setRightDefence(0);
            lineupPanel.getOpponentTeam().setMiddleAttack(0);
            lineupPanel.getOpponentTeam().setMiddleDefence(0);
            lineupPanel.getOpponentTeam().setMidfield(0);
        }
        fillPanel(lineupPanel.getOpponentTeam().getKeeperPanel(), keeper);
        fillPanel(lineupPanel.getOpponentTeam().getLeftWingbackPanel(), leftBack);
        fillPanel(lineupPanel.getOpponentTeam().getLeftCentralDefenderPanel(), leftCentral);
        fillPanel(lineupPanel.getOpponentTeam().getRightCentralDefenderPanel(), rightCentral);
        fillPanel(lineupPanel.getOpponentTeam().getRightWingbackPanel(), rightBack);
        fillPanel(lineupPanel.getOpponentTeam().getLeftWingPanel(), leftWinger);
        fillPanel(lineupPanel.getOpponentTeam().getLeftMidfieldPanel(), leftMidfielder);
        fillPanel(lineupPanel.getOpponentTeam().getRightMidfieldPanel(), rightMidfielder);
        fillPanel(lineupPanel.getOpponentTeam().getRightWingPanel(), rightWinger);
        fillPanel(lineupPanel.getOpponentTeam().getLeftForwardPanel(), leftAttacker);
        fillPanel(lineupPanel.getOpponentTeam().getRightForwardPanel(), rightAttacker);
        fillPanel(lineupPanel.getOpponentTeam().getCentralForwardPanel(), centralAttacker);            
        fillPanel(lineupPanel.getOpponentTeam().getMiddleCentralDefenderPanel(), middleCentral);
        fillPanel(lineupPanel.getOpponentTeam().getCentralMidfieldPanel(), centralMidfielder);

        lineupPanel.reload(SystemManager.isLineup.isSet(), SystemManager.isMixedLineup.isSet());
        grassPanel.repaint();
    }

    private void setMyTeam() {
    	HashMap<Integer, UserTeamPlayerPanel> list = new HashMap<>();
        Lineup lineup = getOwnLineup();
        if ( lineup == null ) return;

        var ratingPredictionModel = HOVerwaltung.instance().getModel().getRatingPredictionModel();

        for (int spot : IMatchRoleID.aFieldMatchRoleID) {
            Player player = lineup.getPlayerByPositionID(spot);
            UserTeamPlayerPanel pp = new UserTeamPlayerPanel();

            if (player != null) {
                UserTeamSpotLineup spotLineup = new UserTeamSpotLineup();
                spotLineup.setAppearance(0);
                spotLineup.setName(" " + player.getShortName());
                spotLineup.setPlayerId(player.getPlayerId());
                spotLineup.setSpecialEvent(player.getSpecialty());
                spotLineup.setTacticCode(lineup.getTactic4PositionID(spot));
                spotLineup.setPosition(lineup.getEffectivePos4PositionID(spot));
                spotLineup.setRating(ratingPredictionModel.getPlayerMatchAverageRating(player, lineup.getEffectivePos4PositionID(spot)));

                int status = getPlayerStatus(player);
                spotLineup.setStatus(status);
                spotLineup.setSpot(spot);
                spotLineup.setTactics(new ArrayList<>());
                pp.reload(spotLineup);
                
            } else {
                pp.reload(null);
            }
            list.put(spot, pp);
        }

        lineupPanel.getMyTeam().setTeamName(HOVerwaltung.instance().getModel().getBasics().getTeamName() + " ("
                                            + HOVerwaltung.instance().getModel().getBasics().getTeamId() + ")");
        fillPanel(lineupPanel.getMyTeam().getKeeperPanel(), list.get(IMatchRoleID.keeper));
        fillPanel(lineupPanel.getMyTeam().getLeftWingbackPanel(), list.get(IMatchRoleID.leftBack));
        fillPanel(lineupPanel.getMyTeam().getLeftCentralDefenderPanel(), list.get(IMatchRoleID.leftCentralDefender));
        fillPanel(lineupPanel.getMyTeam().getRightCentralDefenderPanel(), list.get(IMatchRoleID.rightCentralDefender));
        fillPanel(lineupPanel.getMyTeam().getRightWingbackPanel(), list.get(IMatchRoleID.rightBack));
        fillPanel(lineupPanel.getMyTeam().getLeftWingPanel(), list.get(IMatchRoleID.leftWinger));
        fillPanel(lineupPanel.getMyTeam().getLeftMidfieldPanel(), list.get(IMatchRoleID.leftInnerMidfield));
        fillPanel(lineupPanel.getMyTeam().getRightMidfieldPanel(), list.get(IMatchRoleID.rightInnerMidfield));
        fillPanel(lineupPanel.getMyTeam().getRightWingPanel(), list.get(IMatchRoleID.rightWinger));
        fillPanel(lineupPanel.getMyTeam().getLeftForwardPanel(), list.get(IMatchRoleID.leftForward));
        fillPanel(lineupPanel.getMyTeam().getRightForwardPanel(), list.get(IMatchRoleID.rightForward));
        fillPanel(lineupPanel.getMyTeam().getCentralForwardPanel(), list.get(IMatchRoleID.centralForward));
        fillPanel(lineupPanel.getMyTeam().getCentralMidfieldPanel(), list.get(IMatchRoleID.centralInnerMidfield));
        fillPanel(lineupPanel.getMyTeam().getMiddleCentralDefenderPanel(), list.get(IMatchRoleID.middleCentralDefender));

        lineupPanel.getMyTeam().setLeftAttack(convertRating(ratingPredictionModel.getAverageRating(lineup, RatingPredictionModel.RatingSector.ATTACK_LEFT, 90)));
        lineupPanel.getMyTeam().setLeftDefence(convertRating(ratingPredictionModel.getAverageRating(lineup, RatingPredictionModel.RatingSector.DEFENCE_LEFT, 90)));
        lineupPanel.getMyTeam().setRightAttack(convertRating(ratingPredictionModel.getAverageRating(lineup, RatingPredictionModel.RatingSector.ATTACK_RIGHT, 90)));
        lineupPanel.getMyTeam().setRightDefence(convertRating(ratingPredictionModel.getAverageRating(lineup, RatingPredictionModel.RatingSector.DEFENCE_RIGHT, 90)));
        lineupPanel.getMyTeam().setMiddleAttack(convertRating(ratingPredictionModel.getAverageRating(lineup, RatingPredictionModel.RatingSector.ATTACK_CENTRAL, 90)));
        lineupPanel.getMyTeam().setMiddleDefence(convertRating(ratingPredictionModel.getAverageRating(lineup, RatingPredictionModel.RatingSector.DEFENCE_CENTRAL, 90)));
        lineupPanel.getMyTeam().setMidfield(convertRating(ratingPredictionModel.getAverageRating(lineup, RatingPredictionModel.RatingSector.MIDFIELD, 90)));
    }

    private static int getPlayerStatus(Player player) {
        int cards = player.getTotalCards();
        int injury = player.getInjuryWeeks();

        int injuryStatus, bookingStatus;

        switch (cards) {
            case 1 -> bookingStatus = PlayerDataManager.YELLOW;
            case 2 ->  bookingStatus = PlayerDataManager.DOUBLE_YELLOW;
            case 3 ->  bookingStatus = PlayerDataManager.SUSPENDED;
            default -> bookingStatus = 0;
        }

        switch (injury) {
            case -1 -> injuryStatus = 0;
            case 0 -> injuryStatus = PlayerDataManager.BRUISED;
            default -> injuryStatus = PlayerDataManager.INJURED;
        }

        int transferListedStatus  = player.getTransferListed() * PlayerDataManager.TRANSFER_LISTED;

        return injuryStatus + 10 * bookingStatus + 100 * transferListedStatus;
    }

    private int convertRating(double rating) {
        return RatingUtil.getIntValue4Rating(rating);
    }

    private void fillPanel(JPanel panel, PlayerPanel playerPanel) {
        panel.removeAll();
        
        // Don't add the panel of an empty position.
        if (playerPanel.getContainsPlayer()) {
            panel.setLayout(new GridBagLayout());
            var constraints = new GridBagConstraints();
            constraints.gridy=0;
            constraints.gridx=0;
            constraints.weightx=1;
            constraints.weighty=1;
            constraints.insets = new Insets(5, 5, 5, 5);
            constraints.fill = GridBagConstraints.BOTH;
            panel.add(playerPanel, constraints);
        }
    }

    public Lineup getOwnLineup() {
        return HOVerwaltung.instance().getModel().getCurrentLineup();
    }

    private static class ManMarkingOrderDisplay extends JPanel {
        int xfrom, yfrom, xto, yto;
        JComponent parent;
        public ManMarkingOrderDisplay(JPanel grassPanel) {
            parent = grassPanel;
            parent.add(this,0);
        }

        public void set(JPanel from, JPanel to) {
            xfrom = x(from) + (int) (from.getWidth() * 0.15);
            yfrom = y(from) + (int) (from.getHeight() * 0.25);
            xto = x(to) + (int) (to.getWidth() * 0.85);
            yto = y(to) + (int) (to.getHeight() * 0.75);
        }

        private int y(Container component) {
            var ret = component.getY();
            if ( component.getParent() != parent) ret += y(component.getParent());
            return ret;
        }

        private int x(Container component) {
            var ret = component.getX();
            if ( component.getParent() != parent) ret += x(component.getParent());
            return ret;
        }

        @Override
        protected void paintComponent(Graphics gIn) {
            //super.paintComponent(gIn);
            // adapted from aioobe, https://stackoverflow.com/questions/4112701/drawing-a-line-with-arrow-in-java
            double dx = xto - xfrom, dy = yto - yfrom;
            double angle = Math.atan2(dy, dx);
            int len = (int) Math.sqrt(dx*dx + dy*dy);
            AffineTransform at = AffineTransform.getTranslateInstance(xfrom, yfrom);
            at.concatenate(AffineTransform.getRotateInstance(angle));

            Graphics2D g = (Graphics2D) gIn.create();
            g.transform(at);

            g.setPaint(Color.RED);
            g.setStroke(new BasicStroke(5));
            // Draw horizontal arrow starting in (0, 0)
            int ARR_SIZE = 8;
            g.drawLine(0, 0, len- ARR_SIZE, 0);
            g.fillPolygon(new int[] {len, len- ARR_SIZE, len- ARR_SIZE, len},
                    new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 4);
        }

    }
}
