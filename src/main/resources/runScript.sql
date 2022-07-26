--CREATE DATABASE co_mute; you will need to manually create the schema
DROP TABLE IF EXISTS Registration;
DROP TABLE IF EXISTS CarpoolOpportunity;
DROP TABLE IF EXISTS JoinedCarpoolOpportunity;

CREATE TABLE co_mute.Registration(
   First_Name VARCHAR(255),
   Last_Name VARCHAR(255),
   Email VARCHAR(255),
   Phone_number VARCHAR(12),
   Password VARCHAR(255)
);

CREATE TABLE co_mute.CarpoolOpportunity(
    id INT NOT NULL AUTO_INCREMENT,
    Departure VARCHAR(255),
    Expected_arrival VARCHAR(255),
    Origin VARCHAR(255),
    Days_available INT,
    Destination VARCHAR(255),
    Available_seats INT,
    Leader VARCHAR(255),
    Email VARCHAR(255),
    Notes TEXT,
    Created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(id)
);


CREATE TABLE co_mute.JoinedCarpoolOpportunity(
    id INT NOT NULL AUTO_INCREMENT,
    Departure VARCHAR(255),
    Expected_arrival VARCHAR(255),
    Origin VARCHAR(255),
    Days_available INT,
    Destination VARCHAR(255),
    Available_seats INT,
    Leader VARCHAR(255),
    JoinedBy VARCHAR(255),
    Email VARCHAR(255),
    Notes TEXT,
    Created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(id)
);


--Registration
insert into Registration values('Jack', 'Lungelo','jk@email.com', '01254879568', 'pass1');
insert into Registration values('Mark', 'Doe', 'md@email.com', '01254879568', 'pass1');
insert into Registration values('May', 'Mthembu', 'mm@email.com', '01254879568', 'pass1');
insert into Registration values('Jason', 'Dlamini', 'jd@email.com', '01254879568', 'pass1');



--Car pool
insert into CarpoolOpportunity (Departure,Expected_arrival,Origin,Days_available,Destination,Available_seats,Leader,Email,Notes,Created_at)
values('17:45:10', '18:15:30','Slovo taxi rank',2,'Midrand',3,'Jack','mu@gmail.com','for emergencies, call me on 0124578541','2022-06-15 17:45:10');

insert into CarpoolOpportunity (Departure,Expected_arrival,Origin,Days_available,Destination,Available_seats,Leader,Email,Notes,Created_at)
values('17:45:10', '18:15:30','Slovo taxi rank',2,'greens',3,'Jack','Ma@gmail.com','for emergencies, call me on 0124578541','2022-06-15 17:45:10');

insert into CarpoolOpportunity (Departure,Expected_arrival,Origin,Days_available,Destination,Available_seats,Leader,Email,Notes,Created_at)
values('17:45:10', '18:15:30','Slovo taxi rank',2,'Slovo',3,'Jack','PK@gmail.com','for emergencies, call me on 0124578541','2022-06-15 17:45:10');

----Joined carpool
insert into JoinedCarpoolOpportunity (Departure,Expected_arrival,Origin,Days_available,Destination,Available_seats,Leader,JoinedBy,Email,Notes,Created_at)
values('17:45:10', '18:15:30','Slovo taxi rank',2,'Midrand',2,'Thabiso','pd@gmail.com','jk@gmail.com','for emergencies, call me on 0124578541','2022-06-15 17:45:10');

insert into JoinedCarpoolOpportunity (Departure,Expected_arrival,Origin,Days_available,Destination,Available_seats,Leader,JoinedBy,Email,Notes,Created_at)
values('17:45:10', '18:15:30','Slovo taxi rank',2,'Midrand',2,'Jack','pd@gmail.com','jk@gmail.com','for emergencies, call me on 0124578541','2022-06-15 17:45:10');