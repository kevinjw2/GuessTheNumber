drop database if exists guessthenumber;
create database guessthenumber;

use guessthenumber;

create table game (
	gameId int primary key auto_increment,
    answer varchar(4) not null,
    isFinished bool not null
);

create table round (
	roundId int primary key auto_increment,
    gameId int not null,
    guess varchar(4) not null,
    `time` datetime not null,
    result varchar(7),
    foreign key (gameId) references game(gameId)
);