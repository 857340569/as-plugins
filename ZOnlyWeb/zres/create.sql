create database zonly_tools
create table if not exists SmsInfo(id integer primary key auto_increment,msgType nvarchar(10),msgNumber nvarchar(20),msgName nvarchar(20),msgTime nvarchar(20),msgContent nvarchar(1000))