package test;

import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class WordCounter {

    private int initialDepth = 0;

    private void getWordOccurencies(String url, String word, int maxDepth, int currentDepth) throws IOException{
        int wordCounter = 0;
        String docText;
        try {
            Document doc = Jsoup.connect(url).get();
            Element body = doc.body();

            //Word Counter
            docText = body.text();
            List<String> words = Arrays.stream(docText.split(" ")).toList();
            for (String w : words){
                if (w.equals(word)) wordCounter++;
            }
            if (wordCounter > 0){
                System.out.println("Word \"" + word + "\" found " + wordCounter + " times in " + url + " (depth: " + currentDepth + ")");
            }

            //Visit all the links
            if (currentDepth++ < maxDepth) {
                Elements links = body.getElementsByTag("a");
                for (Element link : links){
                    if (link.attr("href").startsWith("https://")) {
                        getWordOccurencies(link.attr("href"), word, maxDepth, currentDepth);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Not valid URL: " + url);
        }

    }

    public void getWordOccurencies(String url, String word, int depth) throws IOException {
        getWordOccurencies(url, word, depth, initialDepth);
    }
}
