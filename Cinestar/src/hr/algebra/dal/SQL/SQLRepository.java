package hr.algebra.dal.SQL;

import hr.algebra.dal.Repository;
import hr.algebra.model.Actor;
import hr.algebra.model.Director;
import hr.algebra.model.Film;
import hr.algebra.model.Genre;
import hr.algebra.model.Person;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;

/**
 *
 * @author Ivan
 */
public class SQLRepository implements Repository {

    private static final String ID_MOVIE = "IDFilm";
    private static final String TITLE = "Naziv";
    private static final String DESCRIPTION = "Opis";
    private static final String RUN_TIME = "Trajanje";
    private static final String PICTURE_PATH = "Slika";

    private static final String CREATE_MOVIE = "{ CALL createMovie (?,?,?,?,?) }";
    private static final String CREATE_ONE_MOVIE = "{ CALL createOneFilm (?,?,?,?) }";
    private static final String UPDATE_MOVIE = "{ CALL updateFilm (?,?,?,?) }";
    private static final String DELETE_MOVIE = "{ CALL deleteFilm (?) }";
    private static final String DELETE_MOVIES = "{ CALL deleteMovies }";
    private static final String SELECT_MOVIE = "{ CALL selectFilm (?) }";
    private static final String SELECT_MOVIES = "{ CALL selectFilms }";
    private static final String CREATE_DIRECTORS = "{ CALL CreateDirector (?,?,?,?) }";
    private static final String SELECT_DIRECTORS = "{ CALL selectDirectors }";
    private static final String SELECT_ACTORS = "{ CALL selectActors }";
    private static final String CREATE_GENRES = "{ CALL createGenre (?,?) }";
    private static final String SELECT_GENRES = "{ CALL selectGenres }";
    private static final String CREATE_MOVIE_DIRECTOR = "{ CALL createFilmDjelatnik (?,?,?) }";
    private static final String DELETE_FILM_DJELATNIK = "{ CALL deleteFilmDjelatnik }";
    private static final String CREATE_MOVIE_ZANR = "{ CALL createFilmGenre (?,?,?) }";
    private static final String DELETE_FILM_GENRE = "{ CALL deleteFilmGenre }";
    private static final String DELETE_OSOBA = "{ CALL deleteOsoba (?) }";
    private static final String CREATE_ACTOR_TO_SPECIFIC_MOVIE = "{ CALL createActorToSpecificMovie (?,?) }";
    private static final String CREATE_GENRE_TO_SPECIFIC_MOVIE = "{ CALL createGenreToSpecificMovie (?,?) }";

    @Override
    public void createMovie(Film film) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_ONE_MOVIE)) {

                stmt.setString(1, film.getTitle());
                stmt.setString(2, film.getDescription());
                stmt.setInt(3, film.getRunTime());
                stmt.setString(4, film.getPicturePath());


            stmt.executeUpdate();
        }
    }

    @Override
    public void createMovies(List<Film> films) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_MOVIE)) {

            for (Film film : films) {
                stmt.setString(1, film.getTitle());
                stmt.setString(2, film.getDescription());
                stmt.setInt(3, film.getRunTime());
                stmt.setString(4, film.getPicturePath());
                stmt.registerOutParameter(5, Types.INTEGER);
                stmt.executeUpdate();
            }
        }
    }

    @Override
    public void updateMovie(int id, Film data) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(UPDATE_MOVIE)) {

            stmt.setString(1, data.getTitle());
            stmt.setString(2, data.getDescription());
            stmt.setInt(3, data.getRunTime());
            stmt.setInt(4, id);

            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteMovie(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_MOVIE)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Film> selectMovie(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_MOVIE)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(new Film(
                            rs.getInt(ID_MOVIE),
                            rs.getString(TITLE),
                            rs.getString(DESCRIPTION),
                            rs.getInt(RUN_TIME),
                            rs.getString(PICTURE_PATH)));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Film> selectMovies() throws Exception {
        List<Film> movies = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_MOVIES);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                movies.add(new Film(
                        rs.getInt(ID_MOVIE),
                        rs.getString(TITLE),
                        rs.getString(DESCRIPTION),
                        rs.getInt(RUN_TIME),
                        rs.getString(PICTURE_PATH)
                ));
            }
        }

        return movies;
    }

    @Override
    public void deleteMovies() throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_MOVIES)) {

            stmt.executeUpdate();
        }
    }

    @Override
    public void createDirectors(List<Director> directors) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_DIRECTORS)) {

            for (Director director : directors) {
                stmt.setString(1, director.getFirstName());
                stmt.setString(2, director.getLastName());
                stmt.setInt(3, 1);
                stmt.registerOutParameter(4, Types.INTEGER);

                stmt.executeUpdate();
            }
        }
    }

    @Override
    public List<Director> selectDirectors() throws Exception {
        List<Director> directors = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_DIRECTORS);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                directors.add(new Director(
                        rs.getInt(ID_OSOBA),
                        rs.getString(IME),
                        rs.getString(PREZIME),
                        rs.getInt(TIP_ID)
                ));
            }
        }
        return directors;
    }
    private static final String TIP_ID = "TipID";
    private static final String PREZIME = "Prezime";
    private static final String IME = "Ime";
    private static final String ID_OSOBA = "IDOsoba";

    @Override
    public void createActors(List<Actor> actors) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_DIRECTORS)) {

            for (Actor actor : actors) {
                stmt.setString(1, actor.getFirstName());
                stmt.setString(2, actor.getLastName());
                stmt.setInt(3, 2);
                stmt.registerOutParameter(4, Types.INTEGER);

                stmt.executeUpdate();
            }
        }
    }

    @Override
    public List<Actor> selectActors() throws Exception {
        List<Actor> actors = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_ACTORS);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                actors.add(new Actor(
                        rs.getInt(ID_OSOBA),
                        rs.getString(IME),
                        rs.getString(PREZIME),
                        rs.getInt(TIP_ID)
                ));
            }
        }
        return actors;
    }

    @Override
    public void createGenres(List<Genre> genres) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_GENRES)) {

            for (Genre zanr : genres) {
                stmt.setString(1, zanr.getName());
                stmt.registerOutParameter(2, Types.INTEGER);

                stmt.executeUpdate();
            }
        }
    }

    @Override
    public List<Genre> selectGenres() throws Exception {
        List<Genre> genres = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_GENRES);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                genres.add(new Genre(
                        rs.getInt("IDZanr"),
                        rs.getString("Naziv")
                ));
            }
        }
        return genres;
    }

    @Override
    public void createMoviesDirectors(List<Film> movies, List<Director> directors, List<Director> directorsFromSQL, List<Film> moviesFromSQL) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_MOVIE_DIRECTOR)) {
            Director dir = new Director();
            Film movie = new Film();
            for (int i = 0; i < movies.size(); i++) {
                for (int j = 0; j < directors.size(); j++) {
                    if (movies.get(i).getDirector().contains(directors.get(j))) {
                        for (Director director : directorsFromSQL) {
                            for (Film film : moviesFromSQL) {
                                if (directors.get(j).firstName.equals(director.firstName) && directors.get(j).lastName.equals(director.lastName) && movies.get(i).getTitle().equals(film.getTitle())) {
                                    dir = director;
                                    movie = film;

                                    stmt.setInt(1, movie.getId());
                                    stmt.setInt(2, dir.getId());
                                    stmt.registerOutParameter(3, Types.INTEGER);

                                    stmt.executeUpdate();
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    @Override
    public List<Director> selectDirectorsInMovie(Film movie) throws Exception {
        try {
            DataSource dataSource = DataSourceSingleton.getInstance();
            Connection con = dataSource.getConnection();
            PreparedStatement statement = con.prepareStatement("select distinct Osoba.Ime, Osoba.Prezime from FilmDjelatnik inner join Osoba on FilmDjelatnik.OsobaID = IDOsoba where FilmId =" + movie.getId() + " and Osoba.TipID=1");
            ResultSet result = statement.executeQuery();
            List<Director> directors = new ArrayList<>();

            while (result.next()) {
                directors.add(new Director(result.getString(IME), result.getString(PREZIME)));
            }
            return directors;
        } catch (SQLException sQLException) {
            System.out.println("NEBU ISLO MALISKO");
            return null;
        }
    }

    @Override
    public void createMovieActors(List<Film> movies, List<Actor> actors, List<Actor> actorsFromSQL, List<Film> moviesFromSQL) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_MOVIE_DIRECTOR)) {
            for (int i = 0; i < movies.size(); i++) {

                for (int j = 0; j < actors.size(); j++) {

                    if (movies.get(i).getActors().contains(actors.get(j))) {
                        for (Actor actor : actorsFromSQL) {
                            if (actors.get(j).firstName.equals(actor.firstName)) {
                                for (Film film : moviesFromSQL) {
                                    String[] str = film.getTitle().split(" ");
                                    if (movies.get(i).getTitle().contains(str[0])) {
                                        stmt.setInt(1, film.getId());
                                        stmt.setInt(2, actor.getId());
                                        stmt.registerOutParameter(3, Types.INTEGER);

                                        stmt.executeUpdate();
                                    }

                                }

                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<Actor> selectActorsInMovie(Film movie) throws Exception {
        try {
            DataSource dataSource = DataSourceSingleton.getInstance();
            Connection con = dataSource.getConnection();
            PreparedStatement statement = con.prepareStatement("select distinct Osoba.Ime, Osoba.Prezime from FilmDjelatnik inner join Osoba on FilmDjelatnik.OsobaID = IDOsoba where FilmId =" + movie.getId() + " and Osoba.TipID=2");
            ResultSet result = statement.executeQuery();
            List<Actor> actors = new ArrayList<>();

            while (result.next()) {

                actors.add(new Actor(result.getString(IME), result.getString(PREZIME)));
            }
            return actors;
        } catch (SQLException sQLException) {
            System.out.println("NEBU ISLO MALISKO- Rober me Diro");
            return null;
        }
    }

    @Override
    public void deleteFilmDjelatnik() throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_FILM_DJELATNIK)) {

            stmt.executeUpdate();
        }
    }

    @Override
    public void createMovieGenre(List<Film> movies, List<Genre> genres,
            List<Film> moviesFromSQL, List<Genre> genreFromSQL) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_MOVIE_ZANR)) {
            Genre gen = new Genre();
            Film movie = new Film();
            for (int i = 0; i < movies.size(); i++) {
                for (int j = 0; j < genres.size(); j++) {
                    if (movies.get(i).getGenre().contains(genres.get(j))) {
                        for (Genre genre : genreFromSQL) {
                            for (Film film : moviesFromSQL) {
                                if (genres.get(j).getName().equals(genre.getName()) && movies.get(i).getTitle().equals(film.getTitle())) {
                                    gen = genre;
                                    movie = film;

                                    stmt.setInt(1, movie.getId());
                                    stmt.setInt(2, gen.getId());
                                    stmt.registerOutParameter(3, Types.INTEGER);

                                    stmt.executeUpdate();
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    @Override
    public List<Genre> selectGenreInMovie(Film movie) throws Exception {
        try {
            DataSource dataSource = DataSourceSingleton.getInstance();
            Connection con = dataSource.getConnection();
            PreparedStatement statement = con.prepareStatement("select distinct Naziv from ZarnFilm inner join Zanr on Zanr.IDZanr=ZanrID where FilmId= " + movie.getId());
            ResultSet result = statement.executeQuery();
            List<Genre> genres = new ArrayList<>();

            while (result.next()) {
                genres.add(new Genre(result.getString(TITLE)));
            }
            return genres;
        } catch (SQLException sQLException) {
            System.out.println("NEBU ISLO MALISKO");
            return null;
        }
    }

    @Override
    public void deleteFilmGenre() throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_FILM_GENRE)) {

            stmt.executeUpdate();
        }
    }

    @Override
    public void createNewActorMovie(Film film, Actor actor) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_MOVIE_DIRECTOR)) {
            stmt.setInt(1, film.getId());
            stmt.setInt(2, actor.getId());
            stmt.registerOutParameter(3, Types.INTEGER);

            stmt.executeUpdate();
        }
    }

    @Override
    public void createNewActorMovie(Film film, Director director) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_MOVIE_DIRECTOR)) {
            stmt.setInt(1, film.getId());
            stmt.setInt(2, director.getId());
            stmt.registerOutParameter(3, Types.INTEGER);

            stmt.executeUpdate();
        }
    }

    @Override
    public void createNewGenreMovie(Film film, Genre genre) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_MOVIE_ZANR)) {
            stmt.setInt(1, film.getId());
            stmt.setInt(2, genre.getId());
            stmt.registerOutParameter(3, Types.INTEGER);

            stmt.executeUpdate();
        }
    }

    @Override
    public void createActor(Actor actor) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_DIRECTORS)) {

            stmt.setString(1, actor.getFirstName());
            stmt.setString(2, actor.getLastName());
            stmt.setInt(3, 2);
            stmt.registerOutParameter(4, Types.INTEGER);

            stmt.executeUpdate();
        }

    }

    @Override
    public void createDirector(Director director) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_DIRECTORS)) {

            stmt.setString(1, director.getFirstName());
            stmt.setString(2, director.getLastName());
            stmt.setInt(3, 1);
            stmt.registerOutParameter(4, Types.INTEGER);

            stmt.executeUpdate();
        }
    }

    @Override
    public void deletePerson(Person person) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_OSOBA)) {

            stmt.setInt(1, person.id);

            stmt.executeUpdate();
        }
    }

    @Override
    public void createActorToSpecificMovie(Film movie, List<Actor> actors, List<Actor> actorsInSQL) throws SQLException {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_ACTOR_TO_SPECIFIC_MOVIE)) {
            for (Actor actor : actors) {
                for (Actor actor1 : actorsInSQL) {
                    if (actor.firstName == null ? actor1.firstName == null : actor.firstName.equals(actor1.firstName)) {
                        stmt.setInt(1, movie.getId());
                        stmt.setInt(2, actor1.getId());
                        stmt.executeUpdate();
                    }
                }

            }

        }
    }

    @Override
    public void createDirectorToSpecificMovie(Film movie, List<Director> directors, List<Director> directorsInSQL) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_ACTOR_TO_SPECIFIC_MOVIE)) {
            for (Director director : directors) {
                for (Director director1 : directorsInSQL) {
                    if (director.firstName == null ? director1.firstName == null : director.firstName.equals(director1.firstName)) {
                        stmt.setInt(1, movie.getId());
                        stmt.setInt(2, director1.getId());
                        stmt.executeUpdate();
                    }
                }

            }

        }
    }

    @Override
    public void createGenreToSpecificMovie(Film movie, List<Genre> genres, List<Genre> genresInSQL) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_GENRE_TO_SPECIFIC_MOVIE)) {
            for (Genre genre : genres) {
                for (Genre genre1 : genresInSQL) {
                    if (genre.getName().equals(genre1)) {
                        stmt.setInt(1, movie.getId());
                        stmt.setInt(2, genre1.getId());
                        stmt.executeUpdate();
                    }
                }

            }

        }
    }
}
