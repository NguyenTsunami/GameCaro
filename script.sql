create database [TicTacToeDB]
go
use TicTacToeDB
go
create table Player
(
	id int identity primary key,
	name varchar(200),
	acc varchar(20),
	pass varchar(20),
	ava varchar(100),
	score int
)
----------------------------------------------


update Player
set name = 'Aoi Sora'
where id = 28
select * from Player
				