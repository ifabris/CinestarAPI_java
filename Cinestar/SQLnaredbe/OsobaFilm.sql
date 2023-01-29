create proc FilmOsoba
	@FilmID int,
	@OsobaID int,
	@Id INT OUTPUT
as
begin
	insert into FilmDjelatnik values(@FilmID,@OsobaID)
	set @id = SCOPE_IDENTITY()
end