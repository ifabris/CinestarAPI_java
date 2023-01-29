alter procedure selectMovieByName
	@Name nvarchar(100)
as
begin
	select * from Film where Film.Naziv=@Name
end