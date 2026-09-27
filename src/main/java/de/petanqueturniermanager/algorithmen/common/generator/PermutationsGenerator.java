package de.petanqueturniermanager.algorithmen.common.generator;

/*!
 * \brief   Dieser Klasse ist geeignet, um iterativ die rechnerisch möglichen Permutationen zu bilden
 *
 * Aus rein mathematischer Sicht lassen sich durch Kombinatorik Konstellationen
 * bilden, die am Ende jede mögliche Konstellation abdecken. 
 * 
 * Dieser Generator wird über den Konstruktor initialisiert und anschließend
 * lassen sich Permutationen über die Methode getNext() abrufen.
 * 
 * In Verbindung mit der Methode hasNext() lässt sich sicherstellen, dass eine 
 * Permutation nur abgerufen wird, wenn es auch eine weitere tatsächlich gibt.
 * 
 * \note    Der Generator kann über die Konstellationen nur "vorwärts" 
 *          iterieren. Der Aufrufer muss selbst sicherstellen, dass er die 
 *          Ergebnisse so lange speichert, wie sie benötigt werden.
 */
public class PermutationsGenerator 
{
    /*!
     * \brief   In dem Attribut wird die nächste Permutation gehalten
     *
     * Wenn der Aufrufer eine Permutation über getNext() abruft, dann wird 
     * auch gleich geprüft, ob danach eine weitere Permutation erzeugt werden 
     * kann. 
     * 
     * Wenn es möglich ist, dann wird diese Permutation in diesem Attribut 
     * vorgehalten und parallel signalisiert das Attribut hasNext, ob eine 
     * weitere zur Verfügung steht.
     */
    private final int[] field;
    /*!
     * \brief   In dem Attribut wird die Information gehalten, ob eine weitere Permutation zurückgegeben werden kann
     *
     * Über die Getter-Methode hasNext() kann ein Nutzer des Generators in 
     * Erfahrung bringen, ob eine nächste Permutation abrufbar ist oder nicht.
     */
    private boolean hasNext = true;

    /*!
     * \brief   Standardkonstruktor der Klasse
     * 
     * \param   size            In dem Parameter ist die Anzahl der Elemente zu
     *                          übergeben.
     * 
     * Dieser Konstruktor spannt das Feld in der entsprechenden Größe auf.
     */
    public PermutationsGenerator(int size)
    {
        int[] temp = new int[size];
        for(int i=0; i<size; ++i)
            temp[i] = i;
        this.field = temp.clone();
    }

    /*!
     * \brief   Die Methode liefert die Information, ob es eine nächste Permutation gibt
     *
     * Über die Getter-Methode kann abgefragt werden, ob eine weitere 
     * Permutation über die Methode getNext() zurückgegeben werden kann.
     * 
     * \return  Die Methode liefert den Wert \c true, wenn es eine weitere 
     *          Permutation gibt.
     */
    public boolean hasNext() 
    {
        return this.hasNext;
    }

    /*!
     * \brief   Die Methode liefert die nächste Permutation
     *
     * Diese Methode liefert die nächste Permutation, die bereits in field 
     * hinterlegt ist.
     * 
     * Zusätzlich werden durch den Aufruf die beiden Attribute field und hasNext
     * aktualisiert.
     *
     * \return  Die nächste Permutation wird zurückgegen, sofern es eine weitere
     *          Permutation gibt. Wenn es keine Permutation mehr gibt, wird 
     *          \c null zurückgegeben.
     */
    public int[] getNext()
    {
        int[] retval = null;
        if (this.hasNext)
        {
            retval = this.field.clone();
            this.hasNext = this.generateNextPermutation();
        }
        return retval;
    }

    /*!
     * \brief   Die Methode ermittelt basierend auf der aktuellen Permutation die nächste
     *
     * Die Methode berechnet die nächste Permutation basierend auf der, die 
     * aktuell in dem Attribut field gespeichert ist.
     * 
     * Die Methode ist privat, da die nächste Permutation nur durch die Methode 
     * getNext() aufgerufen werden soll.
     */
    private boolean generateNextPermutation() 
    {
        boolean retval = false;
        // Schritt 1: Prüfung des Feldes auf Sortierung
        int k = -1;
        for (int i = this.field.length - 2; i >= 0; i--) 
        {
            if (this.field[i] < this.field[i + 1]) {
                k = i;
                break;
            }
        }

        // Wenn k == -1 ist, dann ist das Feld rückwärts sortiert und es gibt keine weitere Permutation
        if (k > -1) 
        {
            // Schritt 2: Finde den größten Index l (l > k), sodass feld[k] < feld[l]
            int l = -1;
            for (int i = this.field.length - 1; i > k; i--) 
            {
                if (this.field[k] < this.field[i]) {
                    l = i;
                    break;
                }
            }
            // Schritt 3: Tausche feld[k] und feld[l]
            swap(k, l);
            // Schritt 4: Drehe die Reihenfolge vom Index k + 1 bis zum Ende des Feldes um
            reverse(k + 1, this.field.length - 1);
            retval = true;
        }

        return retval;
    }

    /*!
     * \brief   Hilfsmethode zur Vertauschung von Elementen in der Permutation
     *
     * \param   i           Index des ersten Elements, dass zu tauschen ist.
     * \param   j           Index des zweiten Elements, dass zu tauschen ist.
     * 
     * Diese Methode ermöglicht das Vertauschen von zwei Elementen im Feld, das
     * die nächste Permutation enthalten soll.
     * 
     * Die Methode ist bewusst privat, da sie nur von den Methoden genutzt wird,
     * die zur Berechnung der nächsten Permutation benötigt wird.
     */
    private void swap(int i, int j) 
    {
        int temp = this.field[i];
        this.field[i] = this.field[j];
        this.field[j] = temp;
    }

    /*!
     * \brief   Hilfsmethode zur Umkehrung der Reihenfolge von Elementen in der Permutation
     *
     * \param   start       Index des ersten Elements, ab dem verändert werden soll.
     * \param   end         Index des letzten Elements, bis dem verändert werden soll.
     * 
     * Die Methode wird genutzt, um Elemente in einem Teil der Permutatuion 
     * hinsichtlich der Reihenfolge umzudrehen.
     * 
     * Die Methode ist bewusst privat, da sie nur von den Methoden genutzt wird,
     * die zur Berechnung der nächsten Permutation benötigt wird.
     */
    private void reverse(int start, int end) 
    {
        while (start < end) {
            swap(start, end);
            start++;
            end--;
        }
    }
}
