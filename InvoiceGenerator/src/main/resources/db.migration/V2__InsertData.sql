insert into ChargingStation(Id, Longitude,Latitude,Available)
values  (1,0.0, 0.0, true);

insert into Customer(Id,Email,FirstName,LastName,Country,City,PostalCode,Street)
values (1,'test@gmx.at', 'Test', 'User', 'AT', 'Wien', '1130', 'Teststraße 12');

insert into kWhPrice(Id,Price,DateTimeFrom,DateTimeTill)
values (1,10.0, current_timestamp, current_timestamp);

insert into ChargingStationData(Id,ChargingStation_Id,Customer_Id,DateTime,kwh,kWhPrice_Id)
values (1,1,1,current_timestamp,20,1);

insert into Customer(Id,Email,FirstName,LastName,Country,City,PostalCode,Street)
values (2,'test2@gmx.at', 'Test2', 'User2', 'AT', 'Wien', '1130', 'Teststraße 12');

insert into kWhPrice(Id,Price,DateTimeFrom,DateTimeTill)
values (2,12.0, current_timestamp, current_timestamp);

insert into ChargingStationData(Id,ChargingStation_Id,Customer_Id,DateTime,kwh,kWhPrice_Id)
values (3,1,2,current_timestamp,50,2);