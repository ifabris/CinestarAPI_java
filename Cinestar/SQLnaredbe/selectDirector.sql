CREATE PROCEDURE selectDirectors
AS 
BEGIN 
	SELECT * FROM Osoba where TipID=1
END
GO