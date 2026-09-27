package de.petanqueturniermanager.algorithmen.common.ruleset.matchup;

import de.petanqueturniermanager.model.MeleeSpielRunde;
import de.petanqueturniermanager.model.NrComparable;
import de.petanqueturniermanager.model.Team;

/*!
 * \brief      Das Interface repräsentiert die Komponente gemäß des Strukturmusters des Dekorierers.
 *
 * Das Entwurfsmuster Dekorierer wird angewendet, wenn eine Klasse um 
 * Funktionen zu erweitern.
 * 
 * In dem konkreten Fall soll das Entwurfsmuster verwendet werden, um eine 
 * Folge von Prüfungen zu repräsenieren, die erfüllt sein müssen, um eine 
 * Spielpaarung als gültig anzuerkennen.
 * 
 * \dot
digraph {
    splines=ortho;
    nodesep=0.5;
    ranksep=0.4;
    
    subgraph interface { 
        IMatchup[shape=rect label="IMatchup"] 
    }
    subgraph component { 
        rank=same;
        DefaultCoreRule[shape=rect label="DefaultCoreRule"]
        AbstractRuleDecorator[shape=rect label="AbstractRuleDecorator"]
    }
    subgraph connlayer2 {
        cl2sp1[shape=point, width=0, height=0];
    }
    subgraph ruletypelevel { 
        rank=same;
        AbstractStrictRuleDecorator[shape=rect label="AbstractStrictRuleDecorator"]
        AbstractConfigurableRuleDecorator[shape=rect label="AbstractConfigurableRuleDecorator"]
    }
    
    IMatchup -> { DefaultCoreRule AbstractRuleDecorator } [style=invis];
    AbstractRuleDecorator -> cl2sp1 [style=invis];
    cl2sp1 -> { AbstractStrictRuleDecorator AbstractConfigurableRuleDecorator} [style=invis];

    {DefaultCoreRule AbstractRuleDecorator} -> IMatchup [style=dashed arrowhead=onormal]
    AbstractRuleDecorator -> IMatchup [dir=back arrowtail=odiamond]
    
    {AbstractStrictRuleDecorator AbstractConfigurableRuleDecorator} -> cl2sp1 [dir=none]
    cl2sp1 -> AbstractRuleDecorator
}
 * \enddot
 * 
 * Dabei wurde das Entwurfsmuster des Dekorierers bewusst genutzt, da 
 * vorstellbar ist, dass Paarungen in Abhängigkeit der Turnierform anderen 
 * Regeln folgen sollen. Daraus ergibt sich, dass die Zusatzfunktionalität
 * variabel hinzugefügt werden soll, was der Dekorierer erlaubt.
 * 
 * Durch die gemeinsame Schnittstelle von der konkreten Komponente und den 
 * möglichen Dekorierern, die der konkreten Komponente vorgeschaltet sind, ist 
 * für den Aufrufer nicht transparent, ob er mit der Komponente oder einem 
 * Dekorierer kommuniziert.
 * 
 * Die Dekorierer sollen als Kindklassen der Klasse AbstractRuleDecorator 
 * implementiert werden und der Dekorierer implementiert eine Prüfung, die ein
 * Ergebnis in Form eines Wahrheitswertes liefert.
 * 
 * Mit der Klasse DefaultCoreRule steht eine einfache Implementierung zur 
 * Verfügung, die dieses Interface implementiert und selbst nicht mehr 
 * Entscheidungen delegiert.
 * 
 * Ein Instanz der Klasse DefaultCoreRule mit möglichen Dekoriererklassen bildet
 * die Struktur, die eine Paarung auf Gültigkeit prüft. Um zu verhindern, dass 
 * in einem Turnier keine weitere Spielrunde gefunden werden kann, haben die 
 * Regeln die Besonderheit, dass sie, je nach Art der Regel, deaktivierbar sind.
 * 
 * \see     DefaultCoreRule
 * \see     AbstractRuleDecorator
 * \see     AbstractStrictRuleDecorator
 * \see     AbstractConfigurableRuleDecorator
 */
public interface IMatchup {

    /*!
     * \brief   Prüft die Informationen in matchup auf Zulässigkeit
     *    
     * \param   matchup         In dem Parameter kann eine Instanz von 
     *                          NrComparable übergeben werden, die mittels des
     *                          Regelwerkes zu prüfen ist.
     * 
     * Mit dieser Methode soll es möglich sein, dass ein Instanz der Klasse
     * NrComparable geprüft werden kann. Die Klasse NrComparable ist 
     * Elternklasse für unterschiedliche Konstrukte, die sich als Prüfobjekte
     * eignen.
     * 
     * \return  Der Rückgabewert soll das zusammengefasste Ergebnis von mehreren
     *          Prüfungen sein. Wenn \c true zurückgegen wird, dann kann der 
     *          Aufrufer davon ausgehen, dass alle Prüfungen den Wert \c true
     *          geliefert haben.
     */
    public boolean isMatchupValid(NrComparable matchup);
    
    /*!
     * \brief   Prüft die Informationen in round auf Zulässigkeit
     *    
     * \param   round           In dem Parameter kann eine Instanz von 
     *                          MeleeSpielRunde übergeben werden, die mittels 
     *                          des Regelwerkes zu prüfen ist.
     * 
     * Mit dieser Methode soll es möglich sein, dass ein Instanz der Klasse
     * MeleeSpielRunde geprüft werden kann. 
     * 
     * \return  Der Rückgabewert soll das zusammengefasste Ergebnis von mehreren
     *          Prüfungen sein. Wenn \c true zurückgegen wird, dann kann der 
     *          Aufrufer davon ausgehen, dass alle Prüfungen den Wert \c true
     *          geliefert haben.
     */
    public boolean isMatchupValid(MeleeSpielRunde round);
    
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
     * Zwei Teams bilden in aller Regel eine Partie. Von daher werden 
     * Implementierungen der Schnittstelle die erste Mannschaft als Gegner der 
     * zweiten Mannschaft interpretieren.
     * 
     * \return  Der Rückgabewert soll das zusammengefasste Ergebnis von mehreren
     *          Prüfungen sein. Wenn \c true zurückgegen wird, dann kann der 
     *          Aufrufer davon ausgehen, dass alle Prüfungen den Wert \c true
     *          geliefert haben.
     */
    public boolean isMatchupValid(Team first, Team second);

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
    public boolean suspendFirstRule();

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
    public boolean suspendLastRule();

    /*!
     * \brief   Methode zur Deaktivierung aller möglichen Regeln im Regelwerk
     * 
     * Mit der Methode lassen sich alle Regeln im Regelwerk deaktivieren, sofern
     * sie deaktivierbar sind.
     * 
     * \return  Die Methode liefert den Wert \c true, wenn mindestens eine Regel
     *          aus dem Regelwerk deaktiviert wurde.
     */
    public boolean suspendAllRules();

    /*!
     * \brief   Methode liefert die Information, ob die konkrete Regel aktiv ist
     * 
     * \return  Die Methode liefert den Wert \c true, wenn die Regel innerhalb 
     *          des Regelwerkes aktiv ist.
     */
    public boolean isRuleEffective();

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
    public boolean isRuleSuspendable();

    /*!
     * \brief   Methode liefert die Information, ob es in dem Regelwerk 
     *          mindestens eine Regel gibt, die noch deaktiviert werden kann
     * 
     * \return  Die Methode liefert den Wert \c true, solange es in dem 
     *          Regelwerk noch mindestens eine Regel gibt, die deaktiviert 
     *          werden kann.
     */
    public boolean isRulesetSuspendable();
}
