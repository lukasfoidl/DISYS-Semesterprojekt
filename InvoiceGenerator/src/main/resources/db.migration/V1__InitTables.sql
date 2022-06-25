create table ChargingStation(
    Id bigserial primary key,
    Longitude float not null,
    Latitude float not null,
    Available bool not null
);

create table Customer(
    Id bigserial primary key,
    Email text not null ,
    FirstName text not null ,
    LastName text not null ,
    Country text not null,
    City text not null ,
    PostalCode int not null ,
    Street text not null
);

create table kWhPrice(
    Id bigserial primary key ,
    Price float not null ,
    DateTimeFrom timestamp not null ,
    DateTimeTill timestamp not null
);

create table ChargingStationData(
    Id bigserial primary key,
    ChargingStation_Id int references ChargingStation(Id),
    Customer_Id int references Customer(Id),
    DateTime timestamp not null,
    kwh float not null,
    kWhPrice_Id int references  kWhPrice(Id)
);
