CREATE PROCEDURE selectFilm
	@IDFilm INT
AS 
BEGIN 
	SELECT 
		* 
	FROM 
		Film
	WHERE 
		IDFilm = @IDFilm
END
GO