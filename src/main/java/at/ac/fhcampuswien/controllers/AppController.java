package at.ac.fhcampuswien.controllers;

import at.ac.fhcampuswien.api.NewsApi;
import at.ac.fhcampuswien.downloader.Downloader;
import at.ac.fhcampuswien.enums.Country;
import at.ac.fhcampuswien.enums.Endpoint;
import at.ac.fhcampuswien.exception.NewsApiException;
import at.ac.fhcampuswien.models.Article;
import at.ac.fhcampuswien.models.NewsResponse;
import at.ac.fhcampuswien.models.Source;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AppController {
    private List<Article> articles;
    NewsResponse newsResponse = new NewsResponse();
    Source source = new Source();

    public AppController() {
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public List<Article> getArticles() {
        return articles;
    }

    // Method is needed for exercise 4 - ignore for exercise 2 solution
    // returns number of downloaded article urls
    public int downloadURLs(Downloader downloader) {
        List<String> urls = new ArrayList<>();

        //TODO extract urls from articles with java stream

        return downloader.process(urls);
    }

    /**
     * gets the size of last fetched articles
     *
     * @return size of article list
     */
    public int getArticleCount() {
        if (articles != null) {
            return articles.size();
        }
        return 0;
    }

    /**
     * get the top headlines from austria via newsapi
     *
     * @return article list
     */
    public List<Article> getTopHeadlinesAustria() throws NewsApiException {
        NewsApi api = new NewsApi("corona", Country.at, Endpoint.TOP_HEADLINES);
        NewsResponse response = api.requestData();

        if (response != null) {
            articles = response.getArticles();
            return response.getArticles();
        }
        return new ArrayList<>();
    }

    public String printAmountNYTArticles(){
        if (!articles.isEmpty()){
            return ""+articles.stream().filter(article -> article.getSource().getName().equals("New York Times"))
                    .count();
        }else{
            return "No Articles in the List!";
        }
    }

    /**
     * returns all articles that do contain "bitcoin"
     * in their title from newsapi
     *
     * @return filtered list
     */
    public List<Article> getAllNewsBitcoin() throws NewsApiException {
        NewsApi api = new NewsApi("bitcoin", Endpoint.EVERYTHING);
        NewsResponse response = api.requestData();

        if (response != null) {
            articles = response.getArticles();
            return response.getArticles();
        }
        return new ArrayList<>();
    }

    /**
     * filters a given article list based on a query
     *
     * @param query    to filter by
     * @param articles list to filter
     * @return filtered list
     */
    private static List<Article> filterList(String query, List<Article> articles) {
        List<Article> filtered = new ArrayList<>();
        for (Article i : articles) {
            if (i.getTitle().toLowerCase().contains(query)) {
                filtered.add(i);
            }
        }
        return filtered;
    }
    public String printSourceWithMostArticles(){
        if (!articles.isEmpty()){
            return articles.stream()
                    //Quelle: https://stackoverflow.com/questions/22989806/find-the-most-common-string-in-arraylist User:ChandraBhan Singh
                    .collect(Collectors.groupingBy(article -> article.getSource().getName(), Collectors.counting()))
                    .entrySet()
                    .stream()
                    .max(Map.Entry.comparingByValue())
                    .get()
                    .getKey();
        }else{
            return "No Articles in the List!";
        }
    }


    // NYT did not work, no articles were found, so we used CNN.
    public String printAmountOfCNNArticles() {
        if (!articles.isEmpty()) {
            return "" + articles.stream().filter(article -> article.getSource().getName().equals("CNN"))
                    .count();
        }else {
            return "No Articles found in the List!";
        }
    }

    public String printLongestAuthorName(){
        if (!articles.isEmpty()){
            return articles.stream()
                   .filter(article -> article.getAuthor()!= null)
                   .max(Comparator.comparing(article -> article.getAuthor().length()))
                   .get().getAuthor();
        }else{
            return "No Articles in the List!";
        }
    }

    public List<Article> printTitlesWithLessThan15(){
        if (!articles.isEmpty()){
            setArticles(articles.stream()
                    .filter(article -> article.getTitle()
                            .length() < 15).collect(Collectors.toList()));
            newsResponse.setTotalResults(articles.size());
            return articles;
        }
        else{
            return new ArrayList<>();
        }
    }

    public List<Article> printArticlesWithLongestDescription(){
        for(int i = 0; i < articles.size();i++){
            if(articles.get(i).getDescription() == null){
                articles.get(i).setDescription("");
            }
        }
        if(!articles.isEmpty()){
            setArticles(articles.stream()
//                    .filter(article -> article.getDescription() != null)   //if we want to remove all articles with no description
                    .sorted(Comparator.comparingInt((Article article) -> article.getDescription().length())
                            .thenComparing(Article::getDescription))
                    .collect(Collectors.toList()));
            return articles;

        }else{
            return new ArrayList<>();
        }
    }
}






























