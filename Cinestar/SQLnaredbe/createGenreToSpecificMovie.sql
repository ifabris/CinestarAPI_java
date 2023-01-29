create proc createGenreToSpecificMovie
	@idMovie int,
	@idGenre int
as
begin 
	insert into ZarnFilm(FilmId,ZanrID) values(@idMovie,@idGenre)
end