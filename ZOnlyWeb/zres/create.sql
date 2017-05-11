create database zonly_tools
create table if not exists SmsInfo(id integer primary key autoincrement,msgFrom nvarchar(20),msgTo nvarchar(20),msgTime nvarchar(20),msgContent nvarchar(1000))
