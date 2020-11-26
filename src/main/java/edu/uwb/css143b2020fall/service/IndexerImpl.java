package edu.uwb.css143b2020fall.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IndexerImpl implements Indexer {
    public Map<String, List<List<Integer>>> index(List<String> docs) {
        Map<String, List<List<Integer>>> indexes = new HashMap<>();

        int size = docs.size();

        //wordMap is [doc number, place in doc] -> each word, basically a reversed version of final product
        Map<int[], String> wordMap = new HashMap<>();
        for(int docI = 0; docI < size; docI++)
        {
            String[] words = docs.get(docI).split(" ");

            //below code is to get ride of empty words (those are just "")
            int emptyCounter = 0;
            for(String word : words)
                if(word.equals("")) emptyCounter++;

            if(emptyCounter != 0)
            {
                String[] newWords = new String[words.length-emptyCounter];
                int newWordsIndex = 0;
                for(int wordsIndex = 0; wordsIndex < words.length; wordsIndex++)
                {
                    if(words[wordsIndex].equals(""))
                        continue;
                    newWords[newWordsIndex++] = words[wordsIndex];
                }
                words = newWords;
            }

            //now that the empty words are gone, add values to wordMap
            for(int wordI = 0; wordI < words.length; wordI++)
                wordMap.put(new int[]{docI, wordI}, words[wordI]);
        }

        for(Map.Entry<int[], String> entry : wordMap.entrySet())
        {
            int docNum = entry.getKey()[0];
            int wordNum = entry.getKey()[1];
            String word = entry.getValue();

            if(indexes.containsKey(word))
            {
                //how many times has this word been in this document before
                if(indexes.get(word).get(docNum).size() == 0)
                {
                    indexes.get(word).set(docNum, Arrays.asList(wordNum));
                }
                else
                {
                    List<Integer> temp = indexes.get(word).get(docNum);
                    ArrayList<Integer> wordsInDocSoFar = new ArrayList<>(temp.size());
                    for(int i = 0; i < temp.size(); i++)
                        wordsInDocSoFar.add(temp.get(i));

                    for(int i = 0; i < wordsInDocSoFar.size(); i++)
                    {
                        if(wordsInDocSoFar.get(i) > wordNum)
                        {
                            wordsInDocSoFar.add(i, wordNum);
                            break;
                        }
                        if(i == wordsInDocSoFar.size()-1)
                        {
                            wordsInDocSoFar.add(wordNum);
                            break;
                        }
                    }

                    indexes.get(word).set(docNum, wordsInDocSoFar);
                }
            }
            else
            {
                List<List<Integer>> newWordValue = new ArrayList<>(size);
                for(int i = 0; i < size; i++)
                    newWordValue.add( new ArrayList<>(0));
                newWordValue.set(docNum, Arrays.asList(wordNum));

                indexes.put(word, newWordValue);
            }
        }

        return indexes;
    }
}