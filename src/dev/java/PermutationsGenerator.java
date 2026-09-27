import java.util.Arrays;

public class PermutationsGenerator {

    private final Integer[] feld;
    private boolean hatNaechste = true;
    private long index = 1; // 1-basierter Index

    public PermutationsGenerator(Integer[] startFeld) {
        // Für den lexikographischen Algorithmus muss das Startfeld sortiert sein
        this.feld = startFeld.clone();
        Arrays.sort(this.feld);
    }

    public boolean hatNaechste() {
        return hatNaechste;
    }

    /**
     * Berechnet die nächste Permutation direkt im Feld (In-Place)
     * und gibt einen formatierten String mit Index zurück.
     */
    public String naechste() {
        if (!hatNaechste) {
            return null;
        }

        // Aktuellen Zustand für die Ausgabe formatieren
        String ausgabe = index + ". " + Arrays.toString(feld);
        index++;

        // Berechne die nächste Permutation für den nächsten Aufruf
        hatNaechste = berechneNaechstePermutation();
        
        return ausgabe;
    }

    private boolean berechneNaechstePermutation() {
        // Schritt 1: Finde den größten Index k, sodass feld[k] < feld[k + 1]
        int k = -1;
        for (int i = feld.length - 2; i >= 0; i--) {
            if (feld[i] < feld[i + 1]) {
                k = i;
                break;
            }
        }

        // Wenn kein k gefunden wird, sind wir bei der letzten Permutation (rückwärts sortiert)
        if (k == -1) {
            return false;
        }

        // Schritt 2: Finde den größten Index l (l > k), sodass feld[k] < feld[l]
        int l = -1;
        for (int i = feld.length - 1; i > k; i--) {
            if (feld[k] < feld[i]) {
                l = i;
                break;
            }
        }

        // Schritt 3: Tausche feld[k] und feld[l]
        swap(k, l);

        // Schritt 4: Drehe die Reihenfolge vom Index k + 1 bis zum Ende des Feldes um
        reverse(k + 1, feld.length - 1);

        return true;
    }

    private void swap(int i, int j) {
        Integer temp = feld[i];
        feld[i] = feld[j];
        feld[j] = temp;
    }

    private void reverse(int start, int end) {
        while (start < end) {
            swap(start, end);
            start++;
            end--;
        }
    }

    // Test-Main-Methode
    public static void main(String[] args) {
        // Beispiel mit 3 Elementen (funktioniert genauso mit 20, läuft dann nur sehr lange)
        Integer[] meinFeld = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};         
        PermutationsGenerator generator = new PermutationsGenerator(meinFeld);
        
        // Verarbeitet jede Permutation einzeln, ohne den RAM zu belasten
        while (generator.hatNaechste()) {
            System.out.println(generator.naechste());
        }
    }
}