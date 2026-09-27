package de.petanqueturniermanager.algorithmen.common.ruleset.matchup;

/*!
 * \brief   Abstrakte Klasse für Regeln, die nur aktiv sein können
 * 
 * Diese abstrakte Klasse ist geeignet als Elternklasse für die Regeln, die
 * in dem Regelwerk immer greifen sollen.
 */
public abstract class AbstractStrictRuleDecorator extends AbstractRuleDecorator{

    /*!
     * \brief   Konstruktor ohne Parameter
     *
     * Mit parameterlosen Konstruktor kann ein Dekorierer instanziert werden und
     * in diesem Fall wird das Attribut nextMatchupRule mit einer Instanz der
     * Klasse DefaultCoreRule initalisiert.
     */
    protected AbstractStrictRuleDecorator()
    {
        super();
    }

    /*!
     * \brief   Standardkonstruktor der Klasse AbstractStrictRuleDecorator
     * 
     * \param   component       Der Konstruktor benötigt eine Instanz, die den 
     *                          Nachfolger in der Regelkette repräsentiert.
     * 
     * Der Standardkonstruktor ruft den entsprechenden Konstruktor der Klasse 
     * AbstractRuleDecorator auf, in der die wesentlichen Schritte zur 
     * Initialisierung vorgenommen werden.
     */
    protected AbstractStrictRuleDecorator(IMatchup component) 
    {
        super(component);
    }

    /*!
     * \brief   Standardimplementierung für Regeln, die nicht deaktiviert werden können
     *
     * Alle Regeln, die von dieser abstrakten Klasse erben, sollen nicht 
     * deaktivierbar sein.
     * 
     * Damit ist der Methodenaufruf an den Nachfolger zu übergeben und das 
     * Ergebnis des Methodenaufrufs wird an den Aufrufer zurückgegeben.
     * 
     * \return  Die Methode liefert im Rückgabewert die Information, ob in der
     *          Kette für eine Regel die Deaktivierung erfolgreich ausgeführt
     *          werden konnte.
     */
    @Override
    public boolean suspendFirstRule() 
    {
        return this.nextMatchupRule.suspendFirstRule();
    }

    /*!
     * \brief   Standardimplementierung für Regeln, die nicht deaktiviert werden können
     *
     * Alle Regeln, die von dieser abstrakten Klasse erben, sollen nicht 
     * deaktivierbar sein.
     * 
     * Damit ist der Methodenaufruf an den Nachfolger zu übergeben und das 
     * Ergebnis des Methodenaufrufs wird an den Aufrufer zurückgegeben.
     * 
     * \return  Die Methode liefert im Rückgabewert die Information, ob in der
     *          Kette für eine Regel die Deaktivierung erfolgreich ausgeführt
     *          werden konnte.
     */
    @Override
    public boolean suspendLastRule() 
    {
        return this.nextMatchupRule.suspendLastRule();
    }

    /*!
     * \brief   Standardimplementierung für Regeln, die nicht deaktiviert werden können
     *
     * Alle Regeln, die von dieser abstrakten Klasse erben, sollen nicht 
     * deaktivierbar sein.
     * 
     * Damit ist der Methodenaufruf an den Nachfolger zu übergeben und das 
     * Ergebnis des Methodenaufrufs wird an den Aufrufer zurückgegeben.
     * 
     * \return  Die Methode liefert im Rückgabewert die Information, ob in der
     *          Kette für eine Regel die Deaktivierung erfolgreich ausgeführt
     *          werden konnte.
     */
    @Override
    public boolean suspendAllRules() 
    {
        return this.nextMatchupRule.suspendAllRules();
    }

    /*!
     * \brief   Die Methode liefert <strong>immer \c false</strong> zurück
     * 
     * Die Instanzen, die von dieser Klasse erben, sollen hinsichtlich der 
     * Deaktivierbarkeit nicht konfigurierbar sein. Da sie aber das Interface
     * IMatchup vollständig implementieren müssen, wird konstant \c false 
     * zurückgegeben und dieses lässt sich auf Ebene dieser Klasse 
     * allgemeingültig implementieren.
     * 
     * \return  Die Methode liefert den konstanten Wert \c false zurück, da 
     *          alle Regeln, die von dieser Klasse erben, nicht deaktiviert 
     *          werden können.
     */
    @Override
    public boolean isRuleSuspendable() 
    {
        return false;
    }
    
    /*!
     * \brief   Die Methode liefert <strong>immer \c true</strong> zurück
     * 
     * Die Instanzen, die von dieser Klasse erben, sollen hinsichtlich der 
     * Deaktivierbarkeit nicht konfigurierbar sein. Da sie aber das Interface
     * IMatchup vollständig implementieren müssen, wird konstant \c true
     * zurückgegeben und dieses lässt sich auf Ebene dieser Klasse 
     * allgemeingültig implementieren.
     * 
     * \return  Die Methode liefert den konstanten Wert \c true zurück, da 
     *          alle Regeln, die von dieser Klasse erben, stets aktiv sind.
     */
    @Override
    public boolean isRuleEffective() 
    {
        return true;
    }

}
