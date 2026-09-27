package de.petanqueturniermanager.algorithmen.common.ruleset.matchup;

import de.petanqueturniermanager.model.MeleeSpielRunde;
import de.petanqueturniermanager.model.NrComparable;
import de.petanqueturniermanager.model.Team;

/*!
 * \brief   Die Klasse ist konkrete Komponente, die durch Dekorierer 
 *          Zusatzfunktionen erhält
 * 
 * Die Zusatzfunktionen, die aus den Dekorierern sich ergeben, bilden das 
 * Regelwerk, welches es zu prüfen gilt. Diese Klasse bildet das letzte Glied
 * einer Kette von Regeln und daher liefern einige Methoden unveränderliche 
 * Werte.
 * 
 * Die Klasse selbst beinhaltet keinerlei Regelprüfung und kann somit als 
 * "Abschluss" des Regelwerks interpretiert werden, das aus einer Kette von 
 * Regeln besteht.
 * 
 * \see     IMatchup
 */
public class DefaultCoreRule implements IMatchup {

    /*!
     * \brief   Die Methode liefert <b>immer \c true</b>
     *
     * \param   matchup         In dem Parameter kann eine Instanz von 
     *                          NrComparable übergeben werden, die mittels des
     *                          Regelwerkes zu prüfen ist.
     * 
     * Die Standardimplementierung der konkreten Komponente kennt keine 
     * Prüfungen, die auf dem Parameter matchup auszuführen sind. Aus diesem 
     * Grund liefert die Methode den Wert \c true, da dieser Wert das 
     * Prüfergebnis nicht verfälscht.
     * 
     * \return  Der Rückgabewert ist immer \c true.
     */
    @Override 
    public boolean isMatchupValid(NrComparable matchup) 
    {
        return true;
    }

    /*!
     * \brief   Die Methode liefert <b>immer \c true</b>
     *
     * \param   round           In dem Parameter kann eine Instanz von 
     *                          MeleeSpielRunde übergeben werden, die mittels 
     *                          des Regelwerkes zu prüfen ist.
     * 
     * Die Standardimplementierung der konkreten Komponente kennt keine 
     * Prüfungen, die auf dem Parameter round auszuführen sind. Aus diesem 
     * Grund liefert die Methode den Wert \c true, da dieser Wert das 
     * Prüfergebnis nicht verfälscht.
     * 
     * \return  Der Rückgabewert ist immer \c true.
     */
    @Override 
    public boolean isMatchupValid(MeleeSpielRunde round) 
    {
        return true;
    }

    /*!
     * \brief   Die Methode liefert <b>immer \c true</b>
     * 
     * \param   first           In dem Parameter wird das erste Team der 
     *                          Begegnung erwartet.
     * \param   second          In dem Parameter wird das zweite Team der 
     *                          Begegnung erwartet.
     * 
     * Die Standardimplementierung der konkreten Komponente kennt keine 
     * Prüfungen, die auf dem Parameter matchup auszuführen sind. Aus diesem 
     * Grund liefert die Methode den Wert \c true, da dieser Wert das 
     * Prüfergebnis nicht verfälscht.
     * 
     * \return  Der Rückgabewert ist immer \c true.
     */
    @Override
    public boolean isMatchupValid(Team first, Team second)
    {
        return true;
    }

    /*!
     * \brief   Die Methode liefert <b>immer \c false</b>
     *
     * Da die Klasse im Grunde den "Abschluss" einer Kette von Regeln bildet, 
     * ohne selbst Regelfunktionalität zu implementieren wird grundsätzlich der
     * Wert \c false zurückgegeben, da die Regel selbst nicht deaktiviert werden
     * kann und an keine andere Regel delegiert werden kann.
     * 
     * \return  Der Rückgabewert ist immer \c false.
     */
    @Override
    public boolean suspendFirstRule() {
        return false;
    }

    /*!
     * \brief   Die Methode liefert <b>immer \c false</b>
     *
     * Da die Klasse im Grunde den "Abschluss" einer Kette von Regeln bildet, 
     * ohne selbst Regelfunktionalität zu implementieren wird grundsätzlich der
     * Wert \c false zurückgegeben, da die Regel selbst nicht deaktiviert werden
     * kann und an keine andere Regel delegiert werden kann.
     * 
     * \return  Der Rückgabewert ist immer \c false.
     */
    @Override
    public boolean suspendLastRule() {
        return false;
    }

    /*!
     * \brief   Die Methode liefert <b>immer \c false</b>
     *
     * Da die Klasse im Grunde den "Abschluss" einer Kette von Regeln bildet, 
     * ohne selbst Regelfunktionalität zu implementieren wird grundsätzlich der
     * Wert \c false zurückgegeben, da die Regel selbst nicht deaktiviert werden
     * kann und an keine andere Regel delegiert werden kann.
     * 
     * \return  Der Rückgabewert ist immer \c false.
     */
    @Override
    public boolean suspendAllRules() {
        return false;
    }

    /*!
     * \brief   Die Methode liefert <b>immer \c true</b>
     *
     * Diese Methode liefert den Wert \c true, da diese Regel nicht deaktiviert
     * werden kann. Auch wenn die Klasse selbst keine Prüfung ausführt muss sie
     * die Information liefern, ob die Regel aktiv ist.
     * 
     * \return  Der Rückgabewert ist immer \c true.
     */
    @Override
    public boolean isRuleEffective() {
        return true;
    }

    /*!
     * \brief   Die Methode liefert <b>immer \c false</b>
     *
     * Diese Regel darf nicht deaktiviert werden und aus diesem Grund wird 
     * konstant der Wert \c false zurückgegeben.
     * 
     * \return  Der Rückgabewert ist immer \c false.
     */
    @Override
    public boolean isRuleSuspendable() {
        return false;
    }

    /*!
     * \brief   Die Methode liefert <b>immer \c false</b>
     *
     * Das Regelwerk endet in dieser Instanz und wenn man rein diese Regel als 
     * Regelwerk betrachten möchte, dass ist nachvollziehbar, dass der Wert 
     * \c false zurückgegeben werden muss, da auch die einzelne Regel nicht
     * deaktiviert werden kann.
     * 
     * \return  Der Rückgabewert ist immer \c false.
     */
    @Override
    public boolean isRulesetSuspendable() {
        return false;
    }
   
}
