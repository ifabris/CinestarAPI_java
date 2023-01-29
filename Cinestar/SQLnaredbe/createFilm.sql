CREATE PROCEDURE createFilm
	@Naziv NVARCHAR(300),
	@Opis NVARCHAR(300),
	@Trajanje time,
	@Slika NVARCHAR(90),
	@Id INT OUTPUT
AS 
BEGIN 
	INSERT INTO Film VALUES(@Naziv, @Opis, @Trajanje, @Slika)
	SET @Id = SCOPE_IDENTITY()
END
GO
