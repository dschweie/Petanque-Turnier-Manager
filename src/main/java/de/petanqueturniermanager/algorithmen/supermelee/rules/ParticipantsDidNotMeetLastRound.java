package de.petanqueturniermanager.algorithmen.supermelee.rules;

import de.petanqueturniermanager.algorithmen.common.ruleset.matchup.IMatchup;
import de.petanqueturniermanager.algorithmen.supermelee.SuperMeleeWithDecorator;
import de.petanqueturniermanager.model.Team;

/*!
 * \brief       Die Regel prüft, dass die einzelnen Spieler sich in der letzten Runde nicht "begegnet" sind
 *
 * Die Turnierform Supermêlée ist darauf ausgelegt, dass ein Spieler in jeder
 * Runde auf neue Mitspieler trifft.
 * 
 * Wenn das Feld an Spielern zu klein ist, oder die Anzahl der Runden zu groß
 * wird, dann kann es dazu kommen, Begegnungen sich nicht mehr vermeiden lassen.
 * Mit dieser Regel wird dann zumindest vermieden, dass zwei Spieler keine zwei
 * aufeinanderfolgenden Runden miteinander spielen.
 */
public class ParticipantsDidNotMeetLastRound extends ParticipantsDidNotMeet
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
    public ParticipantsDidNotMeetLastRound(boolean effective, boolean suspendable, IMatchup component,
            SuperMeleeWithDecorator instance) {
        super(effective, suspendable, component, instance);
    }

    /*!
     * \brief   Die Methode prüft, ob kein Spieler bereits einem der anderen in der letzten Runde begegnet ist.
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
     *          nicht entscheiden kann, ob zwei Spieler in der letzten Runde 
     *          begegnet sind.<br/> Eine Idee kann sein, dass die Klasse Spieler 
     *          erweitert wird und zu einem Gegner oder Mitspieler auch die 
     *          Information zu Spieltag und Runde hinterlegt.
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
     * \brief   Methode zur Deaktivierung der Regel
     * 
     * Über diese Methode kann die Regel deaktiviert werden.
     * 
     * Die Methode wurde bewusst auf \c protected gesetzt, da der Aufrufer nicht
     * gezielt eine Regel deaktivieren können soll.
     * 
     * \return  Die Methode liefert den Wert \c true zurück, wenn die Regel 
     *          deaktiviert werden konnte.
     */
    @Override
    protected boolean suspend() 
    {
        if(this.suspendable && this.effective)
        {
            this.effective = false;
            return true;
        }
        else
            return false;
    }

}
