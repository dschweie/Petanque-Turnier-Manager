package de.petanqueturniermanager.algorithmen.supermelee.rules;

import de.petanqueturniermanager.algorithmen.common.ruleset.matchup.AbstractConfigurableRuleDecorator;
import de.petanqueturniermanager.algorithmen.common.ruleset.matchup.DefaultCoreRule;
import de.petanqueturniermanager.algorithmen.common.ruleset.matchup.IMatchup;
import de.petanqueturniermanager.model.Team;

/*!
 * \brief       Die Regel prüft, ob die Spieler am aktuellen Spieltag nicht in der gleichen Mannschaft gespielt haben.
 */
public class ParticipantsWereNotTeammatesToday extends AbstractConfigurableRuleDecorator
{
    /*!
     * \brief   Konstruktor mit alle Parametern
     * 
     * \param   effective       Über diesen Parameter kann gesteuert werden, ob 
     *                          die Regel zum Zeitpunkt der Instanzierung 
     *                          aktiv sein soll oder nicht.
     * \param   suspendable     Über diesen Parameter kann gesteuert werden, ob 
     *                          die Regel nach der Instanzierung deaktivierbar 
     *                          sein soll oder nicht.
     * \param   component       In dem Parameter ist die Komponente zu 
     *                          übergeben, mit der die Verknüpfung zu belegen
     *                          ist.
     * 
     * Dieser Konstruktor bietet alle Parameter an, die in der Instanz 
     * parametriert werden können.
     * 
     * \note    Der Konstruktor nimmt keine Prüfung der Parameter vor. Wenn 
     *          effective den Wert \c false hat, dann ist in der aktuellen 
     *          Implementierung die Regel ohne Belang. Eine spätere Aktivierung
     *          ist derzeit nicht vorgesehen.
     */    
    public ParticipantsWereNotTeammatesToday(boolean effective, boolean suspendable, IMatchup component) 
    {
        super(effective, suspendable, component);
    }

    /*!
     * \brief   Die Methode prüft, ob kein Spieler am aktuellen Spieltag mit einem anderen gespielt hat
     * 
     * \param   first           In dem Parameter ist das erste Team zu
     *                          übergeben, dass in die Prüfung einbezogen werden
     *                          soll.
     * \param   second          In dem Parameter ist das zweite Team zu
     *                          übergeben, dass in die Prüfung einbezogen werden
     *                          soll.
     * 
     * \todo    Diese Prüfung ist noch nicht implementiert.<br/>
     *          Über das Team kann die Methode keine Information auf die Runden
     *          des aktuellen Spieltages erhalten und somit liefert die Prüfung
     *          den Wert \c true, um die Spielpaarung nicht zu blockieren.
     * 
     * \return  Die Methode liefert den Wert \c true, wenn die Spieler in den
     *          Teams am aktuellen Spieltag noch nicht in einem Team gespielt 
     *          haben.
     */
    @Override
    protected boolean performCheck(Team first, Team second) 
    {
        return true;
    }

    
}
