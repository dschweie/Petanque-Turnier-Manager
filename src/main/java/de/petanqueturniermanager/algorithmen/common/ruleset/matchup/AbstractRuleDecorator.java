package de.petanqueturniermanager.algorithmen.common.ruleset.matchup;

import de.petanqueturniermanager.model.MeleeSpielRunde;
import de.petanqueturniermanager.model.NrComparable;
import de.petanqueturniermanager.model.Team;

/*!
 * \brief   Die abstrakte Klasse ist ein Platzhalter, der grundlegende Mechaniken der Dekorierer implementiert
 *
 * Aus konkreten Dekorierern lässt sich ein Regelwerk abbilden. Der Aufrufer 
 * kennt dabei nur genau eine Instanz, ohne zu wissen, ob es sich um eine 
 * konkrete Komponente oder einen Dekorierer im Sinne des Entwurfsmusters 
 * handelt.
 * 
 * Ein Regelwerk wird aus mehreren Regeln bestehen, bei denen jede Regel als 
 * Dekorierer zu implementieren ist und immer nur auf eine Instanz zeigt, die 
 * entweder wieder Dekorierer oder konkrete Komponente (DefaultCoreRule) ist.
 * 
 * In dieser Klasse werden die Methoden des Interface IMatchup implementiert, 
 * die sich auf das Delegieren in der Kette beziehen. 
 * 
 * \attention   Diese Klasse nicht als direkte Elternklasse für eine Klasse 
 *              verwendet werden, die eine konkrete Regel implementiert und
 *              nicht abstrakt ist. Konkrete Regeln sollen von den Klassen 
 *              AbstractConfigurableRuleDecorator und 
 *              AbstractStrictRuleDecorator erben.
 * 
 * \todo        Mit Java 17 besteht die Möglichkeit, Klassen als versiegelte 
 *              Klassen zu definieren.<br/>
 *              Danach lassen sich die Klassen wie folgt deklarieren:<br/>
 *              <pre>public abstract sealed class AbstractRuleDecorator implements IMatchup permits AbstractStrictRuleDecorator,  AbstractConfigurableRuleDecorator 
 * public abstract non-sealed class AbstractStrictRuleDecorator extends AbstractRuleDecorator
 * public abstract non-sealed class AbstractConfigurableRuleDecorator extends AbstractRuleDecorator</pre>
 *              Da dieses Sprachkonstrukt von doxygen in der Versiomn 1.19.0 
 *              nicht korrekt abgebildet wird, wurde darauf verzeichtet.
 *
 */
public abstract class AbstractRuleDecorator implements IMatchup {

    /*!
     * \brief   In dem Attribut wird die folgende Komponente gehalten
     * 
     * Das Attribut ergibt sich aus dem Entwurfsmuster und ist die Referenz auf
     * die folgende Komponente.
     */
    protected IMatchup nextMatchupRule = null;

    /*!
     * \brief   Konstruktor ohne Parameter
     *
     * Mit parameterlosen Konstruktor kann ein Dekorierer instanziert werden und
     * in diesem Fall wird das Attribut nextMatchupRule mit einer Instanz der
     * Klasse DefaultCoreRule initalisiert.
     */
    protected AbstractRuleDecorator()
    {
        this.nextMatchupRule = new DefaultCoreRule();
    }

    /*!
     * \brief   Konstruktor, dem die zuverknüpfende Komponente übergeben wird
     *
     * \param   component       In dem Parameter ist die Komponente zu 
     *                          übergeben, mit der die Verknüpfung zu belegen
     *                          ist.
     * 
     * Über diesen Konstruktor lässt sich ein Dekorierer auch mit einem anderen
     * Dekorierer verknüpfen, um auf diese Weise ein Regelwerk zu bilden.
     */
    protected AbstractRuleDecorator(IMatchup component)
    {
        this.nextMatchupRule = component;
    }

    /*!
     * \brief   Prüft die Informationen in matchup auf Zulässigkeit
     *    
     * \param   matchup         In dem Parameter kann eine Instanz von 
     *                          NrComparable übergeben werden, die mittels des
     *                          Regelwerkes zu prüfen ist.
     * 
     * Das Ergebnis der Prüfung lässt sich in dieser Methode durch einen
     * allgemeinen Ausdruck definieren.
     * \code{.java}
     * return this.performCheck(matchup) && this.nextMatchupRule.isMatchupValid(matchup);
     * \endcode
     * 
     * Die Methode performCheck(NrComparable) ist in jeder Regel dann 
     * individuell zu implementieren.
     * 
     * \return  Der Rückgabewert soll das zusammengefasste Ergebnis von mehreren
     *          Prüfungen sein. Wenn \c true zurückgegen wird, dann kann der 
     *          Aufrufer davon ausgehen, dass alle Prüfungen den Wert \c true
     *          geliefert haben.
     */
    @Override
    public boolean isMatchupValid(NrComparable matchup)
    {
        return this.performCheck(matchup) && this.nextMatchupRule.isMatchupValid(matchup);
    }

    /*!
     * \brief   Prüft die Informationen in round auf Zulässigkeit
     *    
     * \param   round           In dem Parameter kann eine Instanz von 
     *                          MeleeSpielRunde übergeben werden, die mittels 
     *                          des Regelwerkes zu prüfen ist.
     * 
     * Das Ergebnis der Prüfung lässt sich in dieser Methode durch einen
     * allgemeinen Ausdruck definieren.
     * \code{.java}
     * return this.performCheck(round) && this.nextMatchupRule.isMatchupValid(round);
     * \endcode
     * 
     * Die Methode performCheck(MeleeSpieleRunde) ist in jeder Regel dann 
     * individuell zu implementieren.
     * 
     * \return  Der Rückgabewert soll das zusammengefasste Ergebnis von mehreren
     *          Prüfungen sein. Wenn \c true zurückgegen wird, dann kann der 
     *          Aufrufer davon ausgehen, dass alle Prüfungen den Wert \c true
     *          geliefert haben.
     */
    @Override 
    public boolean isMatchupValid(MeleeSpielRunde round) 
    {
        return this.performCheck(round) && this.nextMatchupRule.isMatchupValid(round);
    }

    /*!
     * \brief   Prüft eine Begegnung aus zwei Teams
     * 
     * \param   first           In dem Parameter ist das erste Team zu 
     *                          übergeben, dass alleine oder in Verbindung mit
     *                          dem anderen zweiten Parameter geprüft werden
     *                          soll.
     * \param   second          In dem Parameter ist das zweite Team zu 
     *                          übergeben, dass alleine oder in Verbindung mit
     *                          dem anderen Parameter geprüft werden soll.
     * 
     * Das Ergebnis der Prüfung lässt sich in dieser Methode durch einen
     * allgemeinen Ausdruck definieren.
     * \code{.java}
     * return this.performCheck(first, second) && this.nextMatchupRule.isMatchupValid(first, second);
     * \endcode
     * 
     * Die Methode performCheck(Team, Team) ist in jeder Regel dann 
     * individuell zu implementieren.
     * 
     * \return  Der Rückgabewert soll das zusammengefasste Ergebnis von mehreren
     *          Prüfungen sein. Wenn \c true zurückgegen wird, dann kann der 
     *          Aufrufer davon ausgehen, dass alle Prüfungen den Wert \c true
     *          geliefert haben.
     */
    @Override 
    public boolean isMatchupValid(Team first, Team second)
    {
        return this.performCheck(first, second) && this.nextMatchupRule.isMatchupValid(first, second);
    }

    /*!
     * \brief   Default-Implementierung, die <strong> immer \c true</strong> zurückgibt
     *
     * \param   matchup         In dem Parameter ist der Prüfgegenstand zu 
     *                          übergeben, auf dem die Prüfung erfolgen soll.
     * 
     * Diese Methode soll genutzt werden, um die Prüfung der konkreten Regel zu
     * definieren. Eine Regel, die auf der Basis des Prüfgegenstands wirken 
     * soll, ist in der entsprechenden Regel zu überschreiben.
     * 
     * Die Implementierung auf dieser Ebene hat den Vorteil, dass in den Regeln
     * nur die relevanten Prüfmethoden überschrieben werden müssen.
     * 
     * Durch den Wert \c true verhält sich die Regel in einer Kette von Regeln 
     * in Bezug auf das zusammengesetzte Prüfergebnis neutral, was auch 
     * beabsichtigt ist.
     * 
     * \todo    Aktuell ist die Methode aus Gründen einer allgemeinen 
     *          Schnittstelle in der Klasse implementiert worden.<br/>
     *          Unter Umständen ist diese Methode auch überflüssig, da sie 
     *          bisher im Rahmen der Neuimplementierung nicht benötigt wird.
     * 
     * \return  Die Methode liefert konstant \c true zurück.
     */
    protected boolean performCheck(NrComparable matchup)
    {
        return true;
    }
    
    /*!
     * \brief   Default-Implementierung, die <strong> immer \c true</strong> zurückgibt
     *
     * \param   round           In dem Parameter ist der Prüfgegenstand zu 
     *                          übergeben, auf dem die Prüfung erfolgen soll.
     * 
     * Diese Methode soll genutzt werden, um die Prüfung der konkreten Regel zu
     * definieren. Eine Regel, die auf der Basis des Prüfgegenstands wirken 
     * soll, ist in der entsprechenden Regel zu überschreiben.
     * 
     * Die Implementierung auf dieser Ebene hat den Vorteil, dass in den Regeln
     * nur die relevanten Prüfmethoden überschrieben werden müssen.
     * 
     * Durch den Wert \c true verhält sich die Regel in einer Kette von Regeln 
     * in Bezug auf das zusammengesetzte Prüfergebnis neutral, was auch 
     * beabsichtigt ist.
     * 
     * \todo    Aktuell ist die Methode aus Gründen einer allgemeinen 
     *          Schnittstelle in der Klasse implementiert worden.<br/>
     *          Unter Umständen ist diese Methode auch überflüssig, da die 
     *          Methode performCheck(Team first, Team second) ausreichen kann.
     * 
     * \return  Die Methode liefert konstant \c true zurück.
     */
    protected boolean performCheck(MeleeSpielRunde round)
    {
        return true;
    }
    
    /*!
     * \brief   Default-Implementierung, die <strong> immer \c true</strong> zurückgibt
     *
     * \param   first           In dem Parameter ist das erste Team zu
     *                          übergeben, dass in die Prüfung einbezogen werden
     *                          soll.
     * \param   second          In dem Parameter ist das zweite Team zu
     *                          übergeben, dass in die Prüfung einbezogen werden
     *                          soll.
     * 
     * Diese Methode soll genutzt werden, um die Prüfung der konkreten Regel zu
     * definieren. Eine Regel, die auf der Basis des Prüfgegenstands wirken 
     * soll, ist in der entsprechenden Regel zu überschreiben.
     * 
     * Die Implementierung auf dieser Ebene hat den Vorteil, dass in den Regeln
     * nur die relevanten Prüfmethoden überschrieben werden müssen.
     * 
     * Durch den Wert \c true verhält sich die Regel in einer Kette von Regeln 
     * in Bezug auf das zusammengesetzte Prüfergebnis neutral, was auch 
     * beabsichtigt ist.
     * 
     * \return  Die Methode liefert konstant \c true zurück.
     */
    protected boolean performCheck(Team first, Team second)
    {
        return true;
    }

    /*!
     * \brief   	Methode liefert die Information, ob es in dem Regelwerk 
     *              mindestens eine Regel gibt, die noch deaktiviert werden kann
     * 
     * Mit dieser Methode kann der Aufrufer erfragen, ob es in dem Regelwerk 
     * mindestens eine Regel gibt, die deaktiviert werden kann.
     * 
     * Das Deaktivieren von Regeln kann hilfreich sein, um das Regelwerk 
     * dynamisch anzupassen, um z.B. eine Spielpaarung zu finden, die mit einem
     * "strengen" Regelwerk nicht mehr zu finden ist.
     * 
     * Da sich die Methode mit dem Umfang an Methoden implementieren lässt, die
     * durch die Schnittstelle IMatchup vorgegeben sind, ist sie hier für alle 
     * Regeln allgemeingültig implementiert.
     * 
     * \return  Die Methode liefert den Wert \c true, solange es in dem 
     *          Regelwerk noch mindestens eine Regel gibt, die deaktiviert 
     *          werden kann.
     */
    @Override
    public boolean isRulesetSuspendable() 
    {
        return (this.isRuleSuspendable() || this.nextMatchupRule.isRuleSuspendable());
    }

}
