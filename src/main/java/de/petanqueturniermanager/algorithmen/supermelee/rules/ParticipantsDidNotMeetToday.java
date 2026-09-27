package de.petanqueturniermanager.algorithmen.supermelee.rules;

import de.petanqueturniermanager.algorithmen.common.ruleset.matchup.IMatchup;
import de.petanqueturniermanager.algorithmen.supermelee.SuperMeleeWithDecorator;
import de.petanqueturniermanager.model.Team;

/*!
 * \brief       Die Regel prüft, dass die einzelnen Spieler sich in den Runden des aktuellen Spieltages nicht "begegnet" sind
 *
 * Die Turnierform Supermêlée ist darauf ausgelegt, dass ein Spieler in jeder
 * Runde auf neue Mitspieler trifft.
 * 
 * Mit dieser Regel lassen sich Begegnungen von zwei Spielern am gleichen 
 * Spieltag vermeiden.
 */
public class ParticipantsDidNotMeetToday extends ParticipantsDidNotMeet
{

    /*!
     * \brief   Konstruktor mit allen Parametern
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
     * \param   instance        Dieser Parameter wird benötigt, um der Regel 
     *                          Zugriff auf die Klasse zu geben, die mehr 
     *                          Informationen zum Turnier hat. Konkret wird bei
     *                          der Deaktivierung der Regel geprüft, ob das 
     *                          Turnier über mehrere Spieltage geplant ist.
     * 
     * Dieser Konstruktor bietet alle Parameter an, die in der Instanz 
     * parametriert werden können.
     * 
     * \note    Der Konstruktor nimmt keine Prüfung der Parameter vor. Wenn 
     *          effective den Wert \c false hat, dann ist in der aktuellen 
     *          Implementierung die Regel ohne Belang. Eine spätere Aktivierung
     *          ist derzeit nicht vorgesehen.
     */    
    public ParticipantsDidNotMeetToday(boolean effective, boolean suspendable, IMatchup component, SuperMeleeWithDecorator instance) 
    {
        super(effective, suspendable, component, instance);
    }

    /*!
     * \brief   Die Methode prüft, ob kein Spieler bereits einem der anderen am heutigen Spieltag begegnet ist.
     * 
     * \param   first           In dem Parameter ist das erste Team zu
     *                          übergeben, dass in die Prüfung einbezogen werden
     *                          soll.
     * \param   second          In dem Parameter ist das zweite Team zu
     *                          übergeben, dass in die Prüfung einbezogen werden
     *                          soll.
     * 
     * \todo    Die Methode ist zum gegenwärtigen Stand nicht implementiert, da
     *          die Regel keinen Zugriff auf alle Begegnungen hat und somit 
     *          nicht entscheiden kann, ob zwei Spieler sich am heutigen 
     *          Spieltag bereits begegnet sind.<br/>
     *          Eine Idee kann sein, dass die Klasse Spieler erweitert 
     *          wird und zu einem Gegner oder Mitspieler auch die Information 
     *          zu Spieltag und Runde hinterlegt.
     * 
     * \return  Zum gegenwärtigen Stand liefert die Methode konstant \c true 
     *          zurück, um das Regelwerk nicht zu blockieren.
     */    
    @Override
    protected boolean performCheck(Team first, Team second) 
    {
        return true;
    }

    /*!
     * \brief   Die Methode deaktiviert die Regel und ergänzt das Regelwerk um mindestens eine Regel
     * 
     * Wenn die aktuelle Regel deaktiviert wird, dann ist klar dass sich in der
     * aktuellen Begegnung mindestens zwei Spieler am heutigen Spieltag erneut 
     * begegnen müssen, damit eine Runde erzeugt werden kann.
     * 
     * Die Regel selbst darf aus der Kette nicht entfernt werden, da sie den
     * möglichen Vorgänger von sich nicht kennt. Die Regel kann aber zwischen 
     * sich selbst und seinen Nachfolger neue Regeln einfügen. Somit wird die 
     * neue Regelkette als Nachfolger eingehängt und die letzte Regel der neuen
     * Regelkette bekommt den ursprünglichen Nachfolger dieser Regel übergeben.
     * 
     * Das oberste Ziel soll sein, dass ein Mitspieler möglichst viele neue 
     * Mitspieler kennenlernt. Wenn also ein Spieler erneut auf einen anderen 
     * treffen muss, dann soll gelten, dass
     *   - bei Turnieren mit mehreren Spieltagen, die beiden am heutigen Spieltag nicht in einem Team gespielt haben und
     *   - er mit dem Spieler in der letzten Runde nicht zusammen oder gegeneinander gespielt hat.
     * 
     * \return  Die Methode liefert den Wert \c true zurück, wenn die Regel 
     *          erfolgreich deaktiviert werden konnte.
     */
    @Override
    protected boolean suspend() 
    {
        if(this.suspendable && this.effective)
        {
            // Regel wird selbst auf deaktiviert gesetzt 
            this.effective = false;
            // In die Kette kommen "weichere" Regeln hinein
            if((null != this.algorithmInstance) && (0 < this.algorithmInstance.getMatchdayNumber()))
            { // Es ist eine Spieltagnummer gesetzt und damit sollen sich Spieler heute nicht begegnen
                this.nextMatchupRule = new ParticipantsWereNotTeammatesToday(true, true, this.nextMatchupRule);
            }
            this.nextMatchupRule = new ParticipantsDidNotMeetLastRound(true, false, this.nextMatchupRule, this.algorithmInstance);
            return true;
        }
        else
            return false;
    }

}
