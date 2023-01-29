CREATE PROCEDURE deleteFilm
	@IDFilm INT	 
AS 
BEGIN 
	DELETE  
	FROM 
			Film
	WHERE 
		IDFilm = @IDFilm
END