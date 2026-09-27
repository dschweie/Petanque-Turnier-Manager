package de.petanqueturniermanager.algorithmen.common.ruleset.matchup;

import de.petanqueturniermanager.model.MeleeSpielRunde;
import de.petanqueturniermanager.model.NrComparable;
import de.petanqueturniermanager.model.Team;

/*!
 * \brief   Abstrakte Klasse für Regeln, die konfigurierbar sein sollen.
 * 
 * Immer dann, wenn es eine Vielzahl von Regeln gibt, besteht die Gefahr, dass 
 * einzelne Regeln zueinander im Widerspruch stehen und damit keine gute 
 * Konstellation für z.B. eine Turnierrunde gefunden werden kann. Diese Gefahr 
 * kann durch die Parameter Anzahl der Spieler und Anzahl gespielter Runden 
 * verschärft werden. 
 * 
 * Mit konfigurierbaren Regeln soll diesem Problem dahingehend begegnet werden,
 * dass der Anwender ein umfangreiches Regelwerk definieren kann und bei Bedarf
 * einzelne Regeln deaktivieren kann. Durch die Veränderung des Regelwerks zur
 * Laufzeit das Turnier mit einem einem umfangreichen Set an Regeln gestartet 
 * werden und im Laufe des Turniers kann es sich so anpassen, dass auch nach 
 * vielen Runden noch Runden generiert werden können, die gewissen 
 * Mindestanforderungen genügen.
 * 
 * Auch wenn das Interface IMatchup den Eindruck vermittelt, dass jede Regel 
 * konfigurierbar sein kann, wurden unterhalb von AbstractRuleDecorator bewusst
 * die beiden abstrakten Klassen AbstractStrictRuleDecorator und 
 * AbstractConfigurableRuleDecorator definiert, damit aus der Vererbung die 
 * Architekturentscheidung deutlich wird, ob eine Regel konfigurierbar sein soll
 * oder nicht.
 */
public abstract class AbstractConfigurableRuleDecorator extends AbstractRuleDecorator
{
    /*!
     * \brief   Attribut hält die Information, ob die Instanz deaktiviert werden kann
     *
     * Wenn dieses Attribut den Wert \c true hat, dann darf ein Aufrufer die 
     * Instanz deaktivieren und damit wird die Methode zur Prüfung nicht mehr
     * ausgeführt.
     * 
     * Durch den Konstruktor kann ein Aufrufer auf den Wert Einfluss nehmen. 
     * Danach ist ein direkter Zugriff auf das Attribut nicht geplant.
     */
    protected boolean suspendable;
    /*!
     * \brief   Attribut hält die Information, ob die Instanz hinsichtlich der Prüfung aktiv ist
     * 
     * Sobald eine Regel konfigurierbar ist und auch deaktiviert werden kann, 
     * ist dieses Attribut von Bedeutung, um zu entscheiden, ob die Regel in 
     * die Prüfung einbezohen werden soll oder nicht.
     */
    protected boolean effective;

    /*!
     * \brief   Konstruktor ohne Parameter
     * 
     * Der Konstruktor ohne Parameter wird aus Gründen des Komforts angeboten.
     * 
     * Er ruft den entsprechenden Konstruktor der Elternklasse auf und setzt die
     * beiden Attribute effective und suspendable auf \c true.
     */
    public AbstractConfigurableRuleDecorator()
    {
        super();
        this.effective = true;
        this.suspendable = true;
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
    public AbstractConfigurableRuleDecorator(boolean effective, boolean suspendable, IMatchup component) 
    {
        super(component);
        this.effective = effective;
        this.suspendable = suspendable;
    }

    /*!
     * \brief   Prüft die Informationen in matchup auf Zulässigkeit
     *    
     * \param   matchup         In dem Parameter kann eine Instanz von 
     *                          NrComparable übergeben werden, die mittels des
     *                          Regelwerkes zu prüfen ist.
     * 
     * Die Methode ist eine allgemeine Implementierzung für jede Regel, die 
     * parametrierbar ist. 
     * 
     * Eine Delegation an die Methode performCheck(NrComparable) darf nur 
     * erfolgen, wenn die Regel zum Zeitpunkt des Aufrufs aktiv ist.
     * 
     * \return  Der Rückgabewert soll das zusammengefasste Ergebnis von mehreren
     *          Prüfungen sein. Wenn \c true zurückgegen wird, dann kann der 
     *          Aufrufer davon ausgehen, dass alle Prüfungen den Wert \c true
     *          geliefert haben.
     */
    @Override
    public boolean isMatchupValid(NrComparable matchup)
    {
        return (!this.isRuleEffective() || this.performCheck(matchup)) && this.nextMatchupRule.isMatchupValid(matchup);
    }

    /*!
     * \brief   Prüft die Informationen in round auf Zulässigkeit
     *    
     * \param   round           In dem Parameter kann eine Instanz von 
     *                          MeleeSpielRunde übergeben werden, die mittels 
     *                          des Regelwerkes zu prüfen ist.
     * 
     * Die Methode ist eine allgemeine Implementierzung für jede Regel, die 
     * parametrierbar ist. 
     * 
     * Eine Delegation an die Methode performCheck(MeleeSpielRunde) darf nur 
     * erfolgen, wenn die Regel zum Zeitpunkt des Aufrufs aktiv ist.
     * 
     * \return  Der Rückgabewert soll das zusammengefasste Ergebnis von mehreren
     *          Prüfungen sein. Wenn \c true zurückgegen wird, dann kann der 
     *          Aufrufer davon ausgehen, dass alle Prüfungen den Wert \c true
     *          geliefert haben.
     */
    @Override 
    public boolean isMatchupValid(MeleeSpielRunde round) 
    {
        return (!this.isRuleEffective() || this.performCheck(round)) && this.nextMatchupRule.isMatchupValid(round);
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
     * Die Methode ist eine allgemeine Implementierzung für jede Regel, die 
     * parametrierbar ist. 
     * 
     * Eine Delegation an die Methode performCheck(Team, Team) darf nur 
     * erfolgen, wenn die Regel zum Zeitpunkt des Aufrufs aktiv ist.
     * 
     * \return  Der Rückgabewert soll das zusammengefasste Ergebnis von mehreren
     *          Prüfungen sein. Wenn \c true zurückgegen wird, dann kann der 
     *          Aufrufer davon ausgehen, dass alle Prüfungen den Wert \c true
     *          geliefert haben.
     */
    @Override 
    public boolean isMatchupValid(Team first, Team second)
    {
        return (!this.isRuleEffective() || this.performCheck(first, second)) && this.nextMatchupRule.isMatchupValid(first, second);
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

    /*!
     * \brief   Methode zur Deaktivierung der ersten, möglichen Regel
     *
     * Ein Regelwerk kann aus einer beliebigen Anzahl von Dekorierern bestehen.
     * Mit dieser Methode wird die erste Regel suspendiert, die in dem Regelwerk
     * auch deaktiviert werden kann.
     * 
     * \return  Die Methode liefert den Wert \c true, wenn durch den Aufruf der 
     *          Methode die erste Regel deaktiviert wurde.
     */
    @Override
    public boolean suspendFirstRule() 
    {
        return this.isRuleSuspendable()?this.suspend():this.nextMatchupRule.suspendFirstRule();
    }

    /*!
     * \brief   Methode zur Deaktivierung der letzten, möglichen Regel
     *
     * Ein Regelwerk kann aus einer beliebigen Anzahl von Dekorierern bestehen.
     * Mit dieser Methode wird die letzte Regel suspendiert, die in dem Regelwerk
     * auch deaktiviert werden kann.
     * 
     * \return  Die Methode liefert den Wert \c true, wenn durch den Aufruf der 
     *          Methode die letzte Regel deaktiviert wurde.
     */
    @Override
    public boolean suspendLastRule() 
    {
        return this.isRulesetSuspendable()?this.nextMatchupRule.suspendLastRule():this.suspend();
    }

    /*!
     * \brief   Methode zur Deaktivierung aller möglichen Regeln im Regelwerk
     * 
     * Mit der Methode lassen sich alle Regeln im Regelwerk deaktivieren, sofern
     * sie deaktivierbar sind.
     * 
     * \return  Die Methode liefert den Wert \c true, wenn mindestens eine Regel
     *          aus dem Regelwerk deaktiviert wurde.
     */
    @Override
    public boolean suspendAllRules() 
    {
        return this.suspend() || this.nextMatchupRule.suspendAllRules();
    }

    /*!
     * \brief   Methode liefert die Information, ob die konkrete Regel aktiv ist
     * 
     * \return  Die Methode liefert den Wert \c true, wenn die Regel innerhalb 
     *          des Regelwerkes aktiv ist.
     */
    @Override
    public boolean isRuleEffective() 
    {
        return true == this.effective;
    }

    /*!
     * \brief   Methode liefert die Information, ob die konkrete Regel deaktiviert werden kann
     * 
     * Damit eine Regel deaktivierbar ist, müssen zwei Bedingungen erfüllt sein:
     * \li  Die Regel muss die Eigenschaft besitzen, dass sie deaktiviert werden
     *      kann, was Teil der Konfiguration ist.
     * \li  Die Regel darf noch nicht deaktiviert sein.
     * 
     * \return  Die Methode liefert den Wert \c true, wenn die Regel deaktiviert
     *          werden kann.
     */
    @Override
    public boolean isRuleSuspendable() 
    {
        return true == (this.suspendable && this.effective);
    }

}
