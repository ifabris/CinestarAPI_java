create proc createOneFilm
	@Naziv nvarchar(100),
	@Opis nvarchar(max),
	@Trajanje int,
	@PicturePath nvarchar(max)
as
begin
	insert into Film values (@Naziv,@Opis,@Trajanje,@PicturePath)
end

