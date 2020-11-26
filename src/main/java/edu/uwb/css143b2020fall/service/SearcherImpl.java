package edu.uwb.css143b2020fall.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
DO NOT CHANGE
 */

@Service
public class SearcherImpl implements Searcher {
    public List<Integer> search(String keyPhrase, Map<String, List<List<Integer>>> index) {
        List<Integer> result = new ArrayList<>();

        String[] keyWords = keyPhrase.split(" ");
        if(keyWords.length == 1)
        {
            if(index.containsKey(keyWords[0]) == false) return result;
            List<List<Integer>> wordLocations = index.get(keyWords[0]);
            for(int i = 0; i < wordLocations.size(); i++)
            {
                if(wordLocations.get(i).size() != 0)
                {
                    result.add(i);
                }
            }
        }
        else
        {
            for(String word : keyWords)
                if(index.containsKey(word) == false) return result;

            int numDocs = index.get(keyWords[0]).size();
            for(int docIndex = 0; docIndex < numDocs; docIndex++)
            {
                List<List<Integer>> wordPlaces = new ArrayList<>(keyWords.length);
                for(String word : keyWords)
                    wordPlaces.add(index.get(word).get(docIndex));

                if(placesInOrder(wordPlaces))
                    result.add(docIndex);
            }
        }

        return result;
    }

    private boolean placesInOrder(List<List<Integer>> wordPlaces)
    {
        for(int i = 0; i < wordPlaces.get(0).size(); i++)
        {
            boolean inOrder = placesInOrderHelper(wordPlaces, 1, wordPlaces.get(0).get(i));
            if(inOrder)
                return true;
        }
        return false;
    }
    private boolean placesInOrderHelper(List<List<Integer>> wordPlaces, int wordIndex, int previousPlace)
    {
        if(wordPlaces.size() <= wordIndex)
            return true;

        for(Integer place : wordPlaces.get(wordIndex))
        {
            if(place != previousPlace + 1)
                continue;
            return placesInOrderHelper(wordPlaces, wordIndex+1, previousPlace+1);
        }
        return false;
    }
}