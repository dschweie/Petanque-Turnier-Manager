package de.petanqueturniermanager.algorithmen.supermelee;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IntSummaryStatistics;
import java.util.List;

import de.petanqueturniermanager.algorithmen.common.generator.PermutationsGenerator;
import de.petanqueturniermanager.algorithmen.common.ruleset.matchup.DefaultCoreRule;
import de.petanqueturniermanager.algorithmen.common.ruleset.matchup.IMatchup;
import de.petanqueturniermanager.algorithmen.supermelee.rules.ParticipantsDidNotMeet;
import de.petanqueturniermanager.algorithmen.supermelee.rules.ParticipantsMeetDoubletteValue;
import de.petanqueturniermanager.exception.AlgorithmenException;
import de.petanqueturniermanager.helper.CollectionTools;
import de.petanqueturniermanager.model.MeleeSpielRunde;
import de.petanqueturniermanager.model.Spieler;
import de.petanqueturniermanager.model.SpielerMeldungen;
import de.petanqueturniermanager.model.Team;

/*!
 * \brief       Diese Klasse bildet Runden mit einem dynamischen Regelwerk
 *
 * 
 */
public class SuperMeleeWithDecorator extends SuperMeleePaarungenV2 
{
    private static final Logger logger = LogManager.getLogger(SuperMeleeWithDecorator.class);

    private int uniqueTeamId = 0;
    private List<Spieler>   participants=null;
    private IMatchup ruleset;
    private PermutationsGenerator generator=null;
    private int maximumForDoublette = 1;
    
    /*!
     * \brief   Standardkonstruktor für die Klasse
     *
     * Über diesen Standardkonstruktor wird das Regelwerk zur Prüfung der 
     * Partien initialisiert.
     */
    public SuperMeleeWithDecorator() {
        super();
        this.ruleset =  new ParticipantsDidNotMeet( true, 
                                                    true, 
                                                    new ParticipantsMeetDoubletteValue(new DefaultCoreRule(),this), 
                                                    this);
    }

    @Override 
    public MeleeSpielRunde neueSpielrunde(  int rndNr, 
                                            SpielerMeldungen meldungen) 
                           throws AlgorithmenException 
    {
        checkNotNull(meldungen, "Meldungen = null");
        return neueSpielrundeTripletteMode(rndNr, meldungen, false);
    }

    /*!
     * \brief   Methode zur Generierung der nächsten Spielrunde unter Berücksichtigung des Regelwerkes
     *
     * \param   rndNr           In dem Parameter ist die Nummer der Runde zu 
     *                          übergeben, die im Rückgabewert wieder enthalten
     *                          ist.
     * \param   meldungen       In dem Parameter werden die Spieler erwartet, 
     *                          die in der zu generierenden Runde mitspielen 
     *                          sollen.
     * \param   nurDoublette    Dieser Parameter ist ohne Funktion und wurde
     *                          aus Gründen der Kompatibilität übernommen.
     * 
     * Die Methode generiert eine neue Runde auf Basis der aller möglichen 
     * Permutationen, die mathematisch für die Anzahl der Spieler möglich sind.
     * Die Partien, die sich aus der Permutation ergeben, werden dann gegen das 
     * Regelwerk geprüft. 
     * 
     * Wenn das Regelwerk eine Permutation als nicht regelkonform bewertet, wird
     * die Permutation verworfen und die nächste herangezigen.
     * 
     * Wenn nach dem Durchlaufen aller Permutationen keine regelkonform war, 
     * dann wird das Regelwerk "gelockert", indem die erste konfigurierbare 
     * Regel deaktiviert wird. Anschließend wird erneut durch alle Permutationen
     * iteriert.
     * 
     * \throws  AlgorithmenException    Sollte der Punkt erreicht werden, dass
     *                                  keine Permutation regelkonform ist und
     *                                  das Regelwerk nicht weiter "gelockert" 
     *                                  werden kann, dann wird die Exception mit
     *                                  einer entsprechenden Meldung geworfen.
     * 
     * \return  Die Methode liefert eine Instanz der Klasse MeleeSpielRunde, die
     *          dem Regelwerk entspricht und alle Spieler berücksichtigt.
     */
    @Override 
    public MeleeSpielRunde neueSpielrundeTripletteMode( int rndNr, 
                                                        SpielerMeldungen meldungen, 
                                                        boolean nurDoublette)
                           throws AlgorithmenException 
    {
        List<Team> grid = new ArrayList<>();
        checkNotNull(meldungen, "Meldungen = null");
        checkArgument((7 != meldungen.size()),"Mit 7 Spielern lässt sich keine Spielrunde generieren.");
        checkArgument((3 < meldungen.size()), "Es werden mindestens vier Spieler benötigt, um eine Spielrunde zu generieren.");
        
        this.updateParticipants(meldungen.spieler());

        while (grid.isEmpty()) 
        {
            List<Team> encounter = null;

            if(this.generator.hasNext())
            {   // Der Generator kennt eine Permutation, die gute Paarung sein kann
                int[] permutation = this.generator.getNext();
                int index = permutation.length - 1;

                do {
                    switch((index+1) % 6)
                    {
                        case 1, 2, 3, 4: // Es muss mindestens mit einem Doublette gespielt werden
                            encounter = this.tryDoubletteVSDoublette(this.participants.get(permutation[index]), this.participants.get(permutation[index-1]), this.participants.get(permutation[index-2]), this.participants.get(permutation[index-3]));
                            if(null != encounter)
                            { // Die Partie kann nach derzeitigem Stand gespielt werden.
                                grid.addAll(encounter);
                                index -= 4;
                            }
                            break;
                        case 5: // Es muss genau einmal Doublette gegen Triplette gespielt werden
                            encounter = this.tryDoubletteVSTriplette(this.participants.get(permutation[index]), this.participants.get(permutation[index-1]), this.participants.get(permutation[index-2]), this.participants.get(permutation[index-3]), this.participants.get(permutation[index-4]));
                            if(null != encounter)
                            {   // Die Partie kann nach derzeitigem Stand gespielt werden.
                                grid.addAll(encounter);
                                index -=5;
                            }
                        break;
                        default: // Es werden Triplettes hinzugefügt
                            encounter = this.tryTripletteVSTriplette(this.participants.get(permutation[index]), this.participants.get(permutation[index-1]), this.participants.get(permutation[index-2]), this.participants.get(permutation[index-3]), this.participants.get(permutation[index-4]), this.participants.get(permutation[index-5]));
                            if(null != encounter)
                            { // Die Partie kann nach derzeitigem Stand gespielt werden.
                                grid.addAll(encounter);
                                index -= 6;
                            }
                    }
                } while((0 < index) && (null != encounter));

                if(null == encounter) 
                {   // In diesem Fall war mindestens eine Partie nicht passend.
                    grid.clear();
                }
            }
            else
            {   // Ende der Permutationen ist erreicht
                if(this.ruleset.isRulesetSuspendable())
                {   
                    this.ruleset.suspendFirstRule();    // Regelwerk aufweichen
                    this.resetPermutationGenerator();   // wieder von vorne suchen
                    grid.clear();                       // keine Lösung gefunden
                } else {
                    // Mit dem Regelwerk ist kann keine Begegnung generiert werden
                    String spieltagPraefix = spieltagNrFuerLog > 0 ? "Spieltag " + spieltagNrFuerLog + ", " : "";
                    logger.warn("{}Spielrunde {}: Alle möglichen Spielerkombinationen ausgeschöpft ({} Spieler).",
                            spieltagPraefix, rndNr, this.participants.size());
                    throw new AlgorithmenException(
                            "Keine gültige Spielrunde für Runde " + rndNr + " möglich — "
                            + "alle Spielerkombinationen sind ausgeschöpft. "
                            + "Möglicherweise müssen Wiederholungen in den Regeln zugelassen werden.");
                }
            }
        }

        // neue Runde ist gefunden und Rückgabewert kann instanziert werden.
        this.finalizeGrid(grid);
        MeleeSpielRunde retval = new MeleeSpielRunde(rndNr);
        retval.addTeamsWennNichtVorhanden(grid);
        return retval;
    }

    /*!
     * \brief   Die Methode erzeugt aus den Spieler zwei Doublettes
     *          und prüft, ob diese gemäß Regelwerk gegeneinander antreten können
     *
     * \param   firstA          erster Spieler der ersten Mannschaft
     * \param   secondA         zweiter Spieler der ersten Mannschaft
     * \param   firstB          erster Spieler der zweiten Mannschaft
     * \param   secondB         zweiter Spieler der zweiten Mannschaft
     * 
     * Die Methode erzeugt zunächst aus den Spielern die beiden Teams und 
     * übergibt diese dann zur Prüfung an die Methode tryEncounter(Team, Team)
     * 
     * \return  Die Methode reicht das Ergebnis von tryEncounter(Team, Team) 
     *          direkt an den Aufrufer weiter.
     * 
     * \see     SuperMeleeWithDecorator.tryEncounter(Team, Team)
     */
    private List<Team> tryDoubletteVSDoublette(Spieler firstA, Spieler secondA, Spieler firstB, Spieler secondB)
    {
        Team teamA = this.buildDoublette(firstA, firstB);
        Team teamB = this.buildDoublette(secondA, secondB);
        return this.tryEncounter(teamA, teamB);
    }

    /*!
     * \brief   Die Methode erzeugt aus den Spieler ein Doublette und ein Triplette 
     *          und prüft, ob diese gemäß Regelwerk gegeneinander antreten können
     *
     * \param   firstA          erster Spieler der ersten Mannschaft
     * \param   secondA         zweiter Spieler der ersten Mannschaft
     * \param   firstB          erster Spieler der zweiten Mannschaft
     * \param   secondB         zweiter Spieler der zweiten Mannschaft
     * \param   thirdB          dritter Spieler der zweiten Mannschaft
     * 
     * Die Methode erzeugt zunächst aus den Spielern die beiden Teams und 
     * übergibt diese dann zur Prüfung an die Methode tryEncounter(Team, Team)
     * 
     * \return  Die Methode reicht das Ergebnis von tryEncounter(Team, Team) 
     *          direkt an den Aufrufer weiter.
     * 
     * \see     SuperMeleeWithDecorator.tryEncounter(Team, Team)
     */
    private List<Team> tryDoubletteVSTriplette(Spieler firstA, Spieler secondA, Spieler firstB, Spieler secondB, Spieler thirdB)
    {
        Team teamA = this.buildDoublette(firstA, firstB);
        Team teamB = this.buildTriplette(secondA, secondB, thirdB);
        return this.tryEncounter(teamA, teamB);
    }

    /*!
     * \brief   Die Methode erzeugt aus den Spieler zwei Triplettes und prüft, 
     *          ob diese gemäß Regelwerk gegeneinander antreten können
     *
     * \param   firstA          erster Spieler der ersten Mannschaft
     * \param   secondA         zweiter Spieler der ersten Mannschaft
     * \param   thirdA          dritter Spieler der ersten Mannschaft
     * \param   firstB          erster Spieler der zweiten Mannschaft
     * \param   secondB         zweiter Spieler der zweiten Mannschaft
     * \param   thirdB          dritter Spieler der zweiten Mannschaft
     * 
     * Die Methode erzeugt zunächst aus den Spielern die beiden Teams und 
     * übergibt diese dann zur Prüfung an die Methode tryEncounter(Team, Team)
     * 
     * \return  Die Methode reicht das Ergebnis von tryEncounter(Team, Team) 
     *          direkt an den Aufrufer weiter.
     * 
     * \see     SuperMeleeWithDecorator.tryEncounter(Team, Team)
     */
    private List<Team> tryTripletteVSTriplette(Spieler firstA, Spieler secondA, Spieler thirdA, Spieler firstB, Spieler secondB, Spieler thirdB)
    {
        Team teamA = this.buildTriplette(firstA, firstB, thirdA);
        Team teamB = this.buildTriplette(secondA, secondB, thirdB);
        return this.tryEncounter(teamA, teamB);
    }

    /*!
     * \brief   Methode prüft eine Partie gegen das Regelwerk und liefert das Ergebnis zurück
     * 
     * \param   teamA           In dem Parameter wird das erste Team der Partie
     *                          erwartet.
     * \param   teamB           In dem Parameter wird das zweite Team der Partie
     *                          erwartet.
     * 
     * Die Methode prüft die Partie \a teamA : \a teamB gegen das Regelwerk und
     * liefert über den Rückgabewert das Ergebnis der Prüfung zurück.
     * 
     * \return  Die Methode liefert 
     *            - den Wert \c null, wenn die Partie \a teamA gegen \a teamB 
     *              gegen das Regelwerk verstößt.
     *            - eine List<Team> mit \a teamA und \a teamB wenn das Regelwerk
     *              erfüllt ist.
     */
    private List<Team> tryEncounter(Team teamA, Team teamB)
    {
        if((null == teamA) || (null == teamB))
        {   // da ein Team nicht erzeugt wurde, gibt es keine Lösung
            if(null != teamA)
                this.decreaseUniqueTeamId();
            if(null != teamB)
                this.decreaseUniqueTeamId();
            return null;
        } else {
            // in diesem Fall ist zu prüfen, ob die Konstellation passt
            if(this.ruleset.isMatchupValid(teamA, teamB))
            {   // Spielpaarung ist möglich
                List<Team> retval = new ArrayList<>();
                retval.add(teamA);
                retval.add(teamB);
                return retval;
            } else {
                // in diesem Fall ist die Spielpaarung zu verwerfen
                this.uniqueTeamId -=2;
                return null;
            }
        }
    }

    /*!
     * \brief   Die Methode erzeut ein Team als Doublette
     * 
     * \param   first           erster Spieler des Teams
     * \param   second          zweiter Spieler des Teams
     * 
     * Mit der Methode wird eine Instanz der Klasse Team erzeugt, die ein 
     * Doublette repräsentieren soll.
     * 
     * \return  Es wird eine Instanz der Klasse Team mit zwei Spielern 
     *          zurückgegeben.
     */
    private Team buildDoublette(Spieler first, Spieler second)
    {
        Team retval = Team.from(this.getUniqueTeamId());
        try
        {
            retval.addSpielerWennNichtVorhanden(first);
            retval.addSpielerWennNichtVorhanden(second);
            return retval;
        } catch (Exception e) {
            this.decreaseUniqueTeamId();
            return null;
        }
    }

    /*!
     * \brief   Die Methode erzeut ein Team als Triplette
     * 
     * \param   first           erster Spieler des Teams
     * \param   second          zweiter Spieler des Teams
     * \param   third           dritter Spieler des Teams
     * 
     * Mit der Methode wird eine Instanz der Klasse Team erzeugt, die ein 
     * Triplette repräsentieren soll.
     * 
     * \return  Es wird eine Instanz der Klasse Team mit drei Spielern 
     *          zurückgegeben.
     */
    private Team buildTriplette(Spieler first, Spieler second, Spieler third)
    {
        Team retval = Team.from(this.getUniqueTeamId());
        try
        {
            retval.addSpielerWennNichtVorhanden(first);
            retval.addSpielerWennNichtVorhanden(second);
            retval.addSpielerWennNichtVorhanden(third);
            return retval;
        } catch (Exception e) {
            this.decreaseUniqueTeamId();
            return null;
        }
    }

    /*!
     * \brief   Die Methode erhöht den Team-Zähler um den Wert 1
     * 
     * Die Methode erhöht den Team-Zähler um den Wert 1.
     */
    private int getUniqueTeamId()
    {
        return ++this.uniqueTeamId;
    }

    /*!
     * \brief   Die Methode reduziert den Team-Zähler um den Wert 1
     * 
     * Die Methode reduziert den Team-Zähler um den Wert 1.
     */
    private void decreaseUniqueTeamId()
    {
        --this.uniqueTeamId;
    }

    /*!
     * \brief   Falls es Änderungen in Teilnehmerfeld gab, sind diese zu berücksichtigen
     * 
     * \param   currentParticipants     In dem Parameter wird eine Liste von 
     *                          Teilnehmern erwartet, die ab der kommenden 
     *                          Runde eingeplant werden sollen.
     * 
     * Grundsätzlich lässt der Turniermodus es zu, dass in jeder Runde neue 
     * Teilnehmer hinzukommen oder ausscheiden können. Diese Änderungen haben 
     * Auswirkungen auf das Bilden von neuen Runden und aus diesem Grund prüft
     * die Methode, ob sich das Teilnehmerfeld zur letzten Runde geändert hat.
     * 
     * Wenn es sich verändert hat, dann wird
     *   - der Grenzwert für die zulässige Anzahl an Einsätzen im Doublette aktualisiert und
     *   - der Generator für die Permutationen zurückgesetzt.
     */
    private void updateParticipants(List<Spieler> currentParticipants)
    {
        if(!CollectionTools.haveSameElementsRegardlessOfOrder(this.participants, currentParticipants))
        {   // In diesem Fall stimmen die Teilnehmer der aktuellen Runde nicht mit denen der letzten Runde überein
            this.participants = currentParticipants;
            Collections.shuffle(this.participants);
            this.updateDoubletteValue();
            this.resetPermutationGenerator();
        }        
    }

    /*!
     * \brief   Mit der Methode wird der Generator zurückgesetzt, der Permutationen liefert
     * 
     * Da der Generator sich nicht direkt zurücksetzen lässt, instanziert die
     * Methode einfach die Klasse erneut und damit gilt sie als "zurückgesetzt".
     */
    private void resetPermutationGenerator()
    {
        this.generator = new PermutationsGenerator(this.participants.size());
    }

    /*!
     * \brief   Die Methode nimmt administrative Aufgaben vor, damit weitere Runden zuverlässig gebildet werden können.
     *
     * \param   runde           In dem Parameter werden Paarungen erwartet, die
     *                          für die neue Runde stehen.
     * 
     * Im Parameter \a runde werden Paarungen erwartet, die eine neue Spielrunde
     * im Turnier repräsentieren. Bevor die Runde losgehen kann, sind ein paar
     * administrative Schritte erforderlich:
     *   - bei den Spielern müssen die Mitspieler eingetragen werden
     *   - bei den Spielern müssen die Gegner eingetragen werden 
     *   - bei Spielern, die im Doublette eingesetzt werden, ist das zu protokollieren
     */
    private void finalizeGrid(List<Team> runde) 
    {
        for(int i=0; i < runde.size(); ++i)
        {
            Team team = runde.get(i);

            for(Spieler it: team.spieler())
            {
                // optional muss ein Einsatz im Doublette erfasst werden
                if(2 == team.size())
                    it.incAnzMalKleinesTeam();

                // alle Mitspieler sind zu erfassen (Spieler prüft auf "selbst")
                for(Spieler teammate: team.spieler())
                    it.addWarImSpielMit(teammate);

                // Gegner sind zu erfassen, falls zweite Mannschaft einer Partie
                if(1 == i % 2)
                { // ungerader Index steht für 2. Mannschaft einer Partie
                  // Somit gibt es immer das Team runde.get(i-1)
                  for(Spieler opps: runde.get(i-1).spieler())
                    it.addGegner(opps); // addGegner sorgt für opps.addGegner(it)
                }
            }
        }
    }


    /*!
     * \brief   Getter-Methode für die Nummer des Spieltags.
     *
     * Getter-Methode für die Nummer des Spieltages. 
     * 
     * \return  Wenn die Turnierform nur einen Spieltag kennt, dann wird
     *          der Wert 0 zurückgegen und ansonsten steht der Rückgabewert für
     *          den entsprechenden Spieltag.
     */
    public int getMatchdayNumber()
    {
        return this.spieltagNrFuerLog;
    }

    /*!
     * \brief   Die Methode berechnet die Grenze für die Anzahl an Doublettes neu
     * 
     * Üblicherweise soll im Supermêlée Triplette gespielt werden und die 
     * Einteilung in ein Doublette wird nur dann vorgenommen, wenn die Anzahl 
     * der Spieler nicht aufgeht.
     * 
     * Damit stellt das Doublette eine Ausnahme dar, die möglichst selten zur
     * Anwendung kommen soll.
     * 
     * Da jeder Spieler in einem Attribut die Anzahl der Einsätze in Doublette
     * gespeichert hat, wird mit dieser Methode der Grenzwert berechnet, der bei
     * einem Spieler nicht überschritten werden darf.
     * 
     * Wenn die Anzahl der Einsätze im Doublette bei den Spielern 
     * unterschiedlich ist, dann wird der Grenzwert auf die größte Anzahl an
     * Einsätzen gesetzt, die für einen Spieler ermittelt wurde.
     * 
     * Wenn alle Spieler gleich oft im Doublette eingesetzt waren, wird der Wert
     * um den Wert 1 erhöht und damit lassen von der Tendenz alle Spieler in ein
     * Doublette einplanen.
     */
    public void updateDoubletteValue()
    {
        if(null == this.participants || this.participants.isEmpty())
        { // In diesem Fall kann kein vernünftiger Wert ermittelt werden.
            this.maximumForDoublette = -1;
        }
        else
        {
            @SuppressWarnings("null")
            IntSummaryStatistics stats = participants.stream()
                                            .mapToInt(Spieler::getAnzMalKleinesTeam)
                                            .summaryStatistics();
            this.maximumForDoublette = stats.getMax();
            int smaller = 0;
            for(Spieler p: this.participants)
                smaller += (this.maximumForDoublette < p.getAnzMalKleinesTeam())?1:0;
            
            if((stats.getMin() == this.maximumForDoublette) || (smaller < 10))
                ++this.maximumForDoublette;
        }
    }

    /*!
     * \brief   Getter-Methode für maximumForDoublette
     *
     * Die Methode ist die Getter-Methode die den Grenzwert für die Einsätze im
     * Doublette liefert.
     */
    public int getDoubletteMaximum()
    {
        return this.maximumForDoublette;
    }

}
