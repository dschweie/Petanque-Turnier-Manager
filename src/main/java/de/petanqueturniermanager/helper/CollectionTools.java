package de.petanqueturniermanager.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionTools {

    public static <T> boolean haveSameElementsRegardlessOfOrder(List<T> liste1, List<T> liste2) 
    {
        // Wenn eine der Listen null ist oder die Größen ungleich sind, sind sie nicht gleich
        if (liste1 == null || liste2 == null || liste1.size() != liste2.size())
            return false;

        // Häufigkeit der Elemente in der ersten Liste zählen
        Map<T, Integer> frequenzMap = new HashMap<>();
        for (T element : liste1) 
            frequenzMap.put(element, frequenzMap.getOrDefault(element, 0) + 1);

        // Mit der zweiten Liste abgleichen
        for (T element : liste2) 
        {
            if (!frequenzMap.containsKey(element)) 
                return false;
            int count = frequenzMap.get(element);
            if (count == 1) 
                frequenzMap.remove(element);
            else 
                frequenzMap.put(element, count - 1);
        }

        return frequenzMap.isEmpty();
    }

    
}
