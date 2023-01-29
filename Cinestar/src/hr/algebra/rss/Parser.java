/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.rss;

import hr.algebra.factory.ParserFactory;
import hr.algebra.factory.UrlConnectionFactory;
import hr.algebra.model.Actor;
import hr.algebra.model.Director;
import hr.algebra.model.Film;
import hr.algebra.model.Genre;
import hr.algebra.utils.FileUtils;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author Ivan
 */
public class Parser {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.RFC_1123_DATE_TIME;
    private static final Random RANDOM = new Random();

    private static final String RSS_URL = "https://www.blitz-cinestar.hr/rss.aspx?najava=1";
    private static final int TIMEOUT = 10000;
    private static final String REQUEST_METHOD = "GET";
    private static final String ATTRIBUTE_URL = "img src";
    private static final String EXT = ".jpg";
    private static final String DIR = "assets";

    public static List<Film> parse() throws IOException, XMLStreamException {
        List<Film> movies = new ArrayList<>();
        HttpURLConnection con = UrlConnectionFactory.getHttpUrlConnection(RSS_URL, TIMEOUT, REQUEST_METHOD);
        XMLEventReader reader = ParserFactory.createStaxParser(con.getInputStream());

        Optional<TagType> tagType = Optional.empty();
        Film film = null;
        StartElement startElement = null;
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            switch (event.getEventType()) {
                case XMLStreamConstants.START_ELEMENT:
                    startElement = event.asStartElement();
                    String qName = startElement.getName().getLocalPart();
                    tagType = TagType.from(qName);

                    if (tagType.isPresent() && TagType.ITEM == tagType.get()) {
                        film = new Film();
                        movies.add(film);
                        break;
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if (tagType.isPresent()) {
                        Characters characters = event.asCharacters();
                        String data = characters.getData().trim();
                        switch (tagType.get()) {
                            case TITLE:
                                if (film != null && !data.isEmpty()) {
                                    film.setTitle(data);
                                }
                                break;
                            case DESCRIPTION:
                                if (film != null && !data.isEmpty()) {
                                    film.setDescription(data);

                                }
                                break;
                            case PUB_DATE:
                                if (film != null && !data.isEmpty()) {
                                    LocalDateTime publishedDate = LocalDateTime.parse(data, DATE_TIME_FORMATTER);
                                    film.setPubDate(publishedDate);
                                }
                                break;
                            case ORIG_NAZIV:
                                if (film != null && !data.isEmpty()) {
                                    film.setOrigNaziv(data);
                                }
                                break;
                            case REDATELJ:
                                if (film != null && !data.isEmpty()) {
                                    List<Director> directors = new ArrayList<>();
                                    if (data.contains(",")) {
                                        String[] str = data.split(" ");
                                        Director dir1 = new Director(str[0], str[1].replace(",", ""));
                                        Director dir2 = new Director(str[2], str[3]);
                                        directors.add(dir1);
                                        directors.add(dir2);
                                    } else {
                                        String[] str = data.split(" ");
                                        Director dir1 = new Director(str[0], str[1]);
                                        directors.add(dir1);
                                    }
                                    film.setDirector(directors);

                                }
                                break;
                            case ACTORS:
                                if (film != null && !data.isEmpty()) {
                                    List<Actor> actors = new ArrayList<>();
                                      String[] str = data.split(",");
                                    try {
                                        for (int i = 0; i < str.length; i++) {
                                        String[] imePrezime = str[i].split(" ");
                                        if (i == 0) {
                                            Actor act = new Actor(imePrezime[0], imePrezime[1].replace(",", ""));
                                            actors.add(act);
                                        } else if(imePrezime.length==4) {
                                            Actor act = new Actor(imePrezime[1], imePrezime[2]+ " " +imePrezime[3].replace(",", ""));
                                            actors.add(act);
                                        }
                                        else{
                                            Actor act = new Actor(imePrezime[1], imePrezime[2].replace(",", ""));
                                            actors.add(act);
                                        }
                                        film.setActors(actors);

                                    }  
                                    } catch (Exception e) {
                                        break;
                                    }     
                                }
                                break;

                            case TRAJANJE:
                                if (film != null && !data.isEmpty()) {
                                    film.setRunTime(Integer.parseInt(data));
                                }
                                break;
                            case ZANR:
                                if (film != null && !data.isEmpty()) {
                                    List<Genre> genres = new ArrayList<>();
                                    if(data.contains(",")){
                                        String[] str = data.split(" ");
                                        for (int i = 0; i < str.length; i++) {
                                            Genre gen = new Genre(str[i].replace(",","").toLowerCase());
                                            genres.add(gen);
                                        }
                                    }else{
                                        Genre gen = new Genre(data);
                                        genres.add(gen);
                                    }
                                    
                                    film.setGenre(genres);
                                }
                                break;
                            case PLAKAT:
                                if (film != null && !data.isEmpty()) {
                                    film.setPicturePath(data);

                                    handlePicture(film, data);

                                }
                                break;

                        }
                    }
                    break;
            }
        }
        return movies;
    }

    private static void handlePicture(Film film, String pictureUrl) throws IOException {

        pictureUrl = pictureUrl.replaceAll("http", "https");
        String ext = pictureUrl.substring(pictureUrl.lastIndexOf("."));
        if (ext.length() > 4) {
            ext = EXT;
        }
        String pictureName = Math.abs(RANDOM.nextInt()) + ext;
        String localPicturePath = DIR + File.separator + pictureName;

        FileUtils.copyFromUrl(pictureUrl, localPicturePath);
        film.setPicturePath(localPicturePath);
    }

    private enum TagType {

        ITEM("item"),
        TITLE("title"),
        LINK("link"),
        DESCRIPTION("description"),
        PUB_DATE("pubDate"),
        ORIG_NAZIV("orignaziv"),
        ACTORS("glumci"),
        TRAJANJE("trajanje"),
        ZANR("zanr"),
        PLAKAT("plakat"),
        REDATELJ("redatelj");
        private final String name;

        private TagType(String name) {
            this.name = name;
        }

        private static Optional<TagType> from(String name) {
            for (TagType value : values()) {
                if (value.name.equals(name)) {
                    return Optional.of(value);
                }
            }
            return Optional.empty();
        }

    }

}
