alter procedure createGenre
	@Naziv nvarchar(50),
	@Id INT OUTPUT
as
	if(select count(*) from Zanr where Naziv=@Naziv)<1
begin
	insert into Zanr values (@Naziv)
	SET @Id = SCOPE_IDENTITY()
end