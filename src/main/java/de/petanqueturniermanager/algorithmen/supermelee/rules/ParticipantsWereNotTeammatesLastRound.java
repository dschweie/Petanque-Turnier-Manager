package de.petanqueturniermanager.algorithmen.supermelee.rules;

import de.petanqueturniermanager.algorithmen.common.ruleset.matchup.AbstractStrictRuleDecorator;
import de.petanqueturniermanager.algorithmen.common.ruleset.matchup.IMatchup;
import de.petanqueturniermanager.model.Team;

/*!
 * \brief   Die Regel prüft, ob die Spieler innerhalb eines Teams in der letzten Runde nicht zusammengespielt haben.
 *
 * Diese Regel soll sicherstellen, dass Spieler in einem Turnier keine zwei 
 * aufeinanderfolgende Runden mit dem selben Mitspieler spielen.
 */
public class ParticipantsWereNotTeammatesLastRound extends AbstractStrictRuleDecorator{

    /*!
     * \brief   Standardkonstruktor der Klasse ParticipantsWereNotTeammatesLastRound
     *
     * \param   component       In dem Parameter ist die Komponente zu 
     *                          übergeben, mit der die Verknüpfung zu belegen
     *                          ist.
     * 
     * Über diesen Konstruktor wird sichergestellt, dass entsprechend dem 
     * Entwurfsmuster Dekorierer, die Instanz mit der Komponente verbunden wird,
     * die den Nachfolger repräsentiert.
     */
    public ParticipantsWereNotTeammatesLastRound(IMatchup component) 
    {
        super(component);
    }

    /*!
     * \brief   In dieser Methode ist die konkrete Prüfung zu implementieren.
     *
     * \param   first           In dem Parameter ist das erste Team zu
     *                          übergeben, dass in die Prüfung einbezogen werden
     *                          soll.
     * \param   second          In dem Parameter ist das zweite Team zu
     *                          übergeben, dass in die Prüfung einbezogen werden
     *                          soll.
     * 
     * \todo    Aktuell wird einfach der Wert \c true zurückgegeben.<br/>
     *          Der Spieler hat zwar die Information, mit welchen Mitspielern er
     *          bereits gespielt hat. Da die Information in einer HashMap 
     *          gehalten wird, kann nicht davon ausgegangen werden, dass der 
     *          letzte Eintrag für den Mitspieler der letzten Runde steht.
     * 
     * \return  Die Methode liefert aktuell konstant den Wert \c true.
     */
    @Override
    protected boolean performCheck(Team first, Team second) 
    {
        return true;
    }
}
