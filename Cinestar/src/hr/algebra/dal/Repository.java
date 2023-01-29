/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal;

import hr.algebra.model.Actor;
import hr.algebra.model.Director;
import hr.algebra.model.Film;
import hr.algebra.model.Genre;
import hr.algebra.model.Person;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Ivan
 */
public interface Repository {

    void createMovie(Film film) throws Exception;

    void createMovies(List<Film> movies) throws Exception;

    void updateMovie(int id, Film data) throws Exception;

    void deleteMovie(int id) throws Exception;

    Optional<Film> selectMovie(int id) throws Exception;

    List<Film> selectMovies() throws Exception;

    void deleteMovies() throws Exception;

    void createDirectors(List<Director> directors) throws Exception;

    List<Director> selectDirectors() throws Exception;

    void createActors(List<Actor> actors) throws Exception;

    List<Actor> selectActors() throws Exception;

    void createGenres(List<Genre> genres) throws Exception;

    List<Genre> selectGenres() throws Exception;

    void createMoviesDirectors(List<Film> movies, List<Director> directors, List<Director> directorsFromSQL, List<Film> moviesFromSQL) throws Exception;

    List<Director> selectDirectorsInMovie(Film movie) throws Exception;

    void createMovieActors(List<Film> movies, List<Actor> actors, List<Actor> actorsFromSQL, List<Film> moviesFromSQL) throws Exception;

    List<Actor> selectActorsInMovie(Film movie) throws Exception;

    void deleteFilmDjelatnik() throws Exception;

    void createMovieGenre(List<Film> movies, List<Genre> genres, List<Film> moviesFromSQL, List<Genre> genreFromSQL) throws Exception;

    List<Genre> selectGenreInMovie(Film movie) throws Exception;

    void deleteFilmGenre() throws Exception;

    void createNewActorMovie(Film film, Actor actor) throws Exception;

    void createNewActorMovie(Film film, Director director) throws Exception;

    void createNewGenreMovie(Film film, Genre genre) throws Exception;

    void createActor(Actor actor) throws Exception;

    void createDirector(Director director) throws Exception;

    void deletePerson(Person person) throws Exception;

    void createActorToSpecificMovie(Film movie, List<Actor> actors,List<Actor> actorsInSQL ) throws Exception;

    void createDirectorToSpecificMovie(Film movie, List<Director> directors,List<Director> directorsInSQL ) throws Exception;

    void createGenreToSpecificMovie(Film movie, List<Genre> genres, List<Genre> genresInSQL) throws Exception;
}
