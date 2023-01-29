alter proc CreateDirector
	@Ime nvarchar(50),
	@Prezime nvarchar(50),
	@Tip int,
	@Id INT OUTPUT
as

	if(select count(*) from Osoba where Prezime=@Prezime and TipID=@Tip)<1
begin
	insert into Osoba values(@Ime,@Prezime,@Tip)
	set @id = SCOPE_IDENTITY()
end