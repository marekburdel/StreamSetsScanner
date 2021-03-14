create table Car (
	Model int,
	Year int,
	Engine int,
	TopSpeed int
);

create table Dog (
	Name int,
	Age int
);

create view S1.view1 as
    select * from S1.Car;
