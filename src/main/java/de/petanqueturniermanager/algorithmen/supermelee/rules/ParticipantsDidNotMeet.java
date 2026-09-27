package de.petanqueturniermanager.algorithmen.supermelee.rules;

import java.util.List;

import de.petanqueturniermanager.algorithmen.common.ruleset.matchup.AbstractConfigurableRuleDecorator;
import de.petanqueturniermanager.algorithmen.common.ruleset.matchup.DefaultCoreRule;
import de.petanqueturniermanager.algorithmen.common.ruleset.matchup.IMatchup;
import de.petanqueturniermanager.algorithmen.supermelee.SuperMeleeWithDecorator;
import de.petanqueturniermanager.model.Spieler;
import de.petanqueturniermanager.model.Team;

/*!
 * \brief       Die Regel prüft, dass die einzelnen Spieler sich in den vorherigen Runden nicht "begegnet" sind
 *
 * Die Turnierform Supermêlée ist darauf ausgelegt, dass ein Spieler in jeder
 * Runde auf neue Mitspieler trifft.
 * 
 * Mit dieser Regel lässt sich eine Begegnung darauf überprüfen, dass keiner 
 * der Spieler bereits mit oder gegen einen der anderen gespielt hat.
 * 
 * \note        Diese Regel lässt sich nur gut erfüllen, wenn die Anzahl der 
 *              Turnierteilnehmer ausreichend hoch ist. Mit jeder Runde, die im 
 *              Turnier mehr gespielt wird, sinkt die Wahrscheinlichkeit, dass 
 *              die Regel noch erfüllt werden kann. Aus diesem Grund wird 
 *              empfohlen, dass die Regel nur mit suspendable = \c true 
 *              verwendet wird.
 */
public class ParticipantsDidNotMeet  extends AbstractConfigurableRuleDecorator
{
    /*!
     * \brief   In dem Attribut wird die Referenz zum Algorithmus gehalten
     * 
     * Die Klasse, in der die Regel instanziert wird, hält Informationen bereit,
     * die zur Prüfung der Regel verwendet werden.
     */
    protected SuperMeleeWithDecorator algorithmInstance = null;
    /*!
     * \brief   Konstruktor ohne Parameter
     * 
     * Dieser Konstruktor instanziert die Regel als aktiv, konfigurierbar und 
     * einer Instanz der Klasse DefaultCoreRule wird als Nachfolger gesetzt.
     */
    public ParticipantsDidNotMeet()
    {
        super(true, true, new DefaultCoreRule());
    }

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
    public ParticipantsDidNotMeet(boolean effective, boolean suspendable, IMatchup component, SuperMeleeWithDecorator instance)
    {
        super(effective, suspendable, component);
        this.algorithmInstance = instance;
    }

    /*!
     * \brief   Die Methode prüft, ob kein Spieler bereits einem der anderen begegnet ist.
     * 
     * \param   first           In dem Parameter ist das erste Team zu
     *                          übergeben, dass in die Prüfung einbezogen werden
     *                          soll.
     * \param   second          In dem Parameter ist das zweite Team zu
     *                          übergeben, dass in die Prüfung einbezogen werden
     *                          soll.
     * 
     * Diese Methode bildet zunächst aus beiden Teams eine Liste von Spielern 
     * und anschließend wird geprüft, ob kein Spieler bisher mit oder gegen den
     * anderen gespielt hat.
     * 
     * \return  Die Methode liefert den Wert \c true, wenn kein Spieler in einer
     *          vorherigen Runde mit einem der anderen in der gleichen 
     *          Begegnung eingeteilt war.
     */
    @Override
    protected boolean performCheck(Team first, Team second) 
    {
        List<Spieler> participants = first.spieler();
        participants.addAll(second.spieler());

        for(int i=0; i<participants.size(); ++i)
            for(int j=i+1; j<participants.size(); ++j)
                if(     participants.get(i).warImSpielMit(participants.get(j)) 
                    ||  participants.get(i).warImSpielMit(participants.get(j)) )
                    return false;
        return true;
    }
    
    /*!
     * \brief   Die Methode deaktiviert die Regel und ergänzt das Regelwerk um mindestens eine Regel
     * 
     * Wenn die aktuelle Regel deaktiviert wird, dann ist klar dass sich in der
     * aktuellen Begegnung mindestens zwei Spieler erneut begegnen müssen, damit
     * eine Runde erzeugt werden kann.
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
     *   - er mit dem Spieler noch nicht in einem Team gespielt hat und
     *   - bei Turnieren mit mehreren Spieltagen, die beiden sich am heutigen Spieltag nicht begegnet sind.
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
            this.nextMatchupRule = new ParticipantsWereNotTeammates(true, true, this.nextMatchupRule);
            if((null != this.algorithmInstance) && (0 < this.algorithmInstance.getMatchdayNumber()))
            { // Es ist eine Spieltagnummer gesetzt und damit sollen sich Spieler heute nicht begegnen
                this.nextMatchupRule = new ParticipantsDidNotMeetToday(true, true, this.nextMatchupRule, this.algorithmInstance);
            }
            return true;
        }
        else
            return false;
    }
}
