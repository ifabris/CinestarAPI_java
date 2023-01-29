

create procedure selectDirectorsInMovies
	@FilmId int
as
begin
	select distinct Osoba.Ime, Osoba.Prezime from FilmDjelatnik
	inner join Osoba on FilmDjelatnik.OsobaID = IDOsoba
	where FilmId = @FilmId
end