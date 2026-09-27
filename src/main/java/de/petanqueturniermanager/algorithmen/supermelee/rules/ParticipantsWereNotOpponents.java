package de.petanqueturniermanager.algorithmen.supermelee.rules;

import de.petanqueturniermanager.algorithmen.common.ruleset.matchup.AbstractConfigurableRuleDecorator;
import de.petanqueturniermanager.algorithmen.common.ruleset.matchup.DefaultCoreRule;
import de.petanqueturniermanager.algorithmen.common.ruleset.matchup.IMatchup;
import de.petanqueturniermanager.model.Spieler;
import de.petanqueturniermanager.model.Team;

/*!
 * \brief   Die Regel prüft, ob in der Begegnung kein Spieler auf einen Spieler trifft, gegen den er bereits gespielt hat
 * 
 * Die Regel prüft für die einzelnen Spieler, dass die gegnerischen Spieler in 
 * keiner vorherigen Runde als Gegner gesetzt wurden.
 */
public class ParticipantsWereNotOpponents extends AbstractConfigurableRuleDecorator
{
    /*!
     * \brief   Konstruktor ohne Parameter
     * 
     * Dieser Konstruktor instanziert die Regel als aktiv, konfigurierbar und 
     * einer Instanz der Klasse DefaultCoreRule wird als Nachfolger gesetzt.
     */
    public ParticipantsWereNotOpponents()
    {
        super(true, true, new DefaultCoreRule());
    }

    /*!
     * \brief   Konstruktor nur mit einer Instanz als Nachfolger
     * 
     * \param   component       In dem Parameter ist die Komponente zu 
     *                          übergeben, mit der die Verknüpfung zu belegen
     *                          ist.
     * 
     * Dieser Konstruktor instanziert die Regel als aktiv und konfigurierbar. 
     * Die Instanz component wird als Nachfolger hinterlegt.
     */
    public ParticipantsWereNotOpponents(IMatchup component) 
    {
        super(true, true, component);
    }

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
    public ParticipantsWereNotOpponents(boolean effective, boolean suspendable, IMatchup component) 
    {
        super(effective, suspendable, component);
    }

    /*!
     * \brief   Die Methode implementiert die Prüfung, ob kein Spieler einem Gegner aus einer vorherigen Runde begegnet
     *
     * \param   first           In dem Parameter ist das erste Team zu
     *                          übergeben, dass in die Prüfung einbezogen werden
     *                          soll.
     * \param   second          In dem Parameter ist das zweite Team zu
     *                          übergeben, dass in die Prüfung einbezogen werden
     *                          soll.
     * 
     * In einem Team sind Spieler enthalten und jede Instanz eines Spielers 
     * kennt die bisherigen Gegner aus den vorherigen Runden. Folglich ist es 
     * ausreichend, dass geprüft wird, ob kein Spieler aus Team first bereits
     * gegen einen Spieler aus Team second gespielt hat.
     * 
     * \return  Die Methode liefert den Wert \c true, wenn kein Spieler bereits
     *          gegen einen Spieler aus dem anderen Team angetreten ist.
     */
    @Override
    protected boolean performCheck(Team first, Team second) 
    {
        for(Spieler playerA: first.spieler())
            for(Spieler playerB: second.spieler())
                if(playerA.warGegnerVon(playerB))
                    return false;
        return true;
    }
}
