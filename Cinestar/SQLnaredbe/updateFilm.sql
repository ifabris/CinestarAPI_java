CREATE PROCEDURE updateFilm
	@Naziv NVARCHAR(300),
	@Opis NVARCHAR(300),
	@Trajanje int,
	@IDFilm INT
	 
AS 
BEGIN 
	UPDATE Film SET 
		Naziv = @Naziv,
		Opis = @Opis,
		Trajanje = @Trajanje
	WHERE 
		IDFilm = @IDFilm
END