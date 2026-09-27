package de.petanqueturniermanager.algorithmen.supermelee.rules;

import de.petanqueturniermanager.algorithmen.common.ruleset.matchup.AbstractStrictRuleDecorator;
import de.petanqueturniermanager.algorithmen.common.ruleset.matchup.IMatchup;
import de.petanqueturniermanager.algorithmen.supermelee.SuperMeleeWithDecorator;
import de.petanqueturniermanager.model.Spieler;
import de.petanqueturniermanager.model.Team;

/*!
 * \brief       Die Regel soll vermeiden, dass Spieler nur selten in ein Doublette eingeteilt werden
 * 
 * Die Klasse Spieler hat die Information, wie oft ein Spieler bereits im 
 * Doublette eingesetzt war.
 * 
 * Ferner geht die Klasse davon aus, dass die Klasse für eine Instanz von Typ 
 * SuperMeleeWithDecorator arbeitet und diese Instanz kennt alle Teilnehmer und
 * eine Information zur Obergrenze für Einsätze im Doublette.
 * 
 * Falls es sich bei einem zu prüfenden Team um ein Doublette handelt, wird
 * geprüft, ob aller Spieler des Doublettes bisher unterhalb der Obergrenze 
 * eingesetzt wurden.
 * 
 * Da die Obergrenze vor einem neuen Generierungslauf angepasst und mit
 * zunehmender Anzahl an Einsätzen im Doublette erhöht wird, kann dieser Regel
 * stets aktiv bleiben und ist daher nicht konfigurierbar.
 */
public class ParticipantsMeetDoubletteValue extends AbstractStrictRuleDecorator
{
    /*!
     * \brief   In dem Attribut wird die Referenz zum Algorithmus gehalten
     * 
     * Die Klasse, in der die Regel instanziert wird, hält Informationen bereit,
     * die zur Prüfung der Regel verwendet werden. Konkret wird die Methode 
     * getDoubletteMaximum() genutzt.
     * 
     * \see SuperMeleeWithDecorator.getDoubletteMaximum()
     * \see ParticipantsMeetDoubletteValue.performCheckTeamForDoublette(Team)
     */
    private SuperMeleeWithDecorator algorithmInstance = null;

    /*!
     * \brief   Konstruktor der Klasse 
     *
     * \param   component       In dem Parameter ist die Komponente zu 
     *                          übergeben, mit der die Verknüpfung zu belegen
     *                          ist.
     * \param   instance        In dem Parameter ist die Klasse zu übergeben, 
     *                          in der die Runde generiert wird, damit die 
     *                          Regel auf zusätzliche Informationen zugreifen
     *                          kann, die nicht über das Team oder die Spieler
     *                          ermittelt werden können.
     * 
     * Mit dem Konstruktor werden die Attribute der Klasse instanziert.
     */
    public ParticipantsMeetDoubletteValue(IMatchup component, SuperMeleeWithDecorator instance)
    {
        super(component);
        this.algorithmInstance = instance;
    }

    /*!
     * \brief   Prüfung der Spieler im Team, falls es sich um ein Doublette handelt
     *
     * \param   first           In dem Parameter ist das erste Team zu
     *                          übergeben, dass in die Prüfung einbezogen werden
     *                          soll.
     * \param   second          In dem Parameter ist das zweite Team zu
     *                          übergeben, dass in die Prüfung einbezogen werden
     *                          soll.
     * 
     * Diese Methode wird in dieser Klasse implementiert, damit die Prüfung auch
     * ausgeführt werden kann.
     * 
     * Da die Prüfung für beide Teams erfolgen muss, nutzt die Methode die 
     * performCheckTeamForDoublette(Team).
     * 
     * \return  Die Methode liefert den Wert \c true, wenn 
     *            - kein Zugriff auf die Instanz SuperMeleeWithDecorator möglich ist.
     *            - kein Team als Doublette geplant ist.
     *            - alle Spieler, die für ein Doublette vorgesehen sind, unter 
     *              der zulässigen Obergrenze sind.
     */
    @Override
    protected boolean performCheck(Team first, Team second) 
    {
        return null==this.algorithmInstance?true:this.performCheckTeamForDoublette(first) && this.performCheckTeamForDoublette(second);
    }

    /*!
     * \brief   Die Methode prüft, ob im Falle eines Doublette die Spieler dazu eingeteilt werden können
     * 
     * \param   team            In dem Parameter wird ein Team erwartet, dass 
     *                          geprüft werden soll.
     * 
     * Die Methode muss nur dann eine Prüfung ausführen, wennn team genau zwei
     * Spieler enthält. 
     * 
     * In diesem Fall wird für beide Spieler geprüft, ob die Anzahl der 
     * bisherigen Einsätze im Doublette kleiner ist als die ermittelte 
     * Obergrenze.
     * 
     * \return  Die Methode liefert \c true zurück, wenn
     *            - das übergebene Team kein Doublette ist oder 
     *            - alle Spieler hinsichtlich der bisherigen Einsätze im 
     *              Doublette unterhalb der Obergrenze bleiben.
     */
    private boolean performCheckTeamForDoublette(Team team)
    {
        switch(team)
        {
            case null -> {  // Wenn kein Team übergeben wird, ist keine Prüfung möglich
                return true; }  
            case Team t when 2 == t.spieler().size() -> { // Team ist Doublette
                return (    (team.spieler().get(0).getAnzMalKleinesTeam() < this.algorithmInstance.getDoubletteMaximum()) 
                         && (team.spieler().get(1).getAnzMalKleinesTeam() < this.algorithmInstance.getDoubletteMaximum()));
            }
            default -> {    // In allen anderen Fällen ist keine Prüfung erforderlich
                return true;}
        }
    }
}
