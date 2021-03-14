create table Username.Person (
	Name int,
	Surname int,
	Mail int,
	Mobile int
);

create table Username.Dog (
	Name int,
	Age int
);

create view Username.v1 as
    select * from Username.Person;

CREATE OR REPLACE PROCEDURE proc1 (a IN INT, b IN OUT INT, c OUT INT) AS
    result INT;
BEGIN
    result := a + b;
    c := result;
END;
/

CREATE OR REPLACE PROCEDURE proc2 (a IN INT, b IN INT) AS
    result INT;
BEGIN
    result := 2 + 3;
END;
/

CREATE OR REPLACE PROCEDURE proc3 (a OUT INT, b OUT INT) AS
BEGIN
    a := 1;
    b := 2;
END;
/