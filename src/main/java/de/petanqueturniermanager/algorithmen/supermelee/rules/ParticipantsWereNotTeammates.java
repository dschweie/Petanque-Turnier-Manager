package de.petanqueturniermanager.algorithmen.supermelee.rules;

import java.util.List;

import de.petanqueturniermanager.algorithmen.common.ruleset.matchup.AbstractConfigurableRuleDecorator;
import de.petanqueturniermanager.algorithmen.common.ruleset.matchup.IMatchup;
import de.petanqueturniermanager.model.Spieler;
import de.petanqueturniermanager.model.Team;

/*!
 * \brief       Die Regel prüft, ob zwei Spieler in dem Team noch nicht miteinander gespielt haben.
 * 
 * Die Turnierform Supermêlée ist darauf ausgelegt, dass ein Spieler in jeder
 * Runde auf neue Mitspieler trifft.
 * 
 * Mit dieser Regel lässt sich eine Begegnung darauf überprüfen, dass keiner 
 * der Spieler bereits mit einen im gleichen Team gespielt hat.
 * 
 * Diese Regel kann auch zwischenmenschlichen Spannungen vorbeugen, wenn die 
 * Beteiligten wissen, dass sie im Grunde nur einmal miteinander spielen müssen.
 */
public class ParticipantsWereNotTeammates extends AbstractConfigurableRuleDecorator 
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
    public ParticipantsWereNotTeammates(boolean effective, boolean suspendable, IMatchup component) 
    {
        super(effective, suspendable, component);
    }

    /*!
     * \brief   Die Methode hat zur Aufgabe die Prüfung der Regel zu implementieren
     *
     * \param   first           In dem Parameter ist das erste Team zu
     *                          übergeben, dass in die Prüfung einbezogen werden
     *                          soll.
     * \param   second          In dem Parameter ist das zweite Team zu
     *                          übergeben, dass in die Prüfung einbezogen werden
     *                          soll.
     * 
     * Die Prüfung, dass kein Spieler eines Teams bereits mit einem anderen 
     * Spieler im Team gespielt hat, ist für die Teams \a first und \a second
     * getrennt voneinander auszuführen. Da die Prüfung jeweils identisch ist,
     * wurde sie in die Methode performCheck(List<Spieler>) ausgelagert und 
     * die Methode liefert ein zusammengesetztes Ergebnis aus den 
     * Einzelprüfungen zurück.
     * 
     * \return  Die Methode liefert den Wert \c true zurück, wenn bei beiden 
     *          Teams die Spieler noch nicht miteinander gespielt haben.
     */
    @Override
    protected boolean performCheck(Team first, Team second) {
        return this.performCheck(first.spieler()) && this.performCheck(second.spieler());
    }

    /*!
     * \brief   Die Methode prüft über die jeweiligen Spieler, ob diese bereits zusammengespielt haben
     *
     * \param   participants    Der Methode sind die zu prüfenden Spieler als
     *                          Liste zu übergeben.
     * 
     * Die Methode durchläuft die Liste der Spieler und prüft, ob diese jeweils
     * noch nicht mit den Spielern in einem Team waren, die in der Liste hinter
     * ihnen stehen. 
     * 
     * \return  Die Methode liefert den Wert \c true, wenn die Regel in Bezug 
     *          auf die Liste \a participants zutrifft.
     */
    private boolean performCheck(List<Spieler> participants)
    {
        boolean retval=true;
        for(int i=0; i<participants.size(); ++i)
            for(int j=i+1; j<participants.size(); ++j)
                retval &= !(participants.get(i).warImTeamMit(participants.get(j)));
        return retval;
    }
}
