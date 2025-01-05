# COM534 Assignment Repository

### This program was written in Kotlin verion 2.0.0 using Compose version 1.6.10 framework 
###  The program was built using JDK version 21 and Gradle 8.5

## Setup
The program should simply run when pressing the play icon within the main.kt.

If not:

To run the program ensure the sources directory is set to "src":
- Open in IntelliJ IDEA
- Right click the "src" directory -> Mark Directory As -> Sources Root
- Navigate to "src/main/kotlin/Main.kt". Then click Configure, when "Kotlin not configured is shown.
- Then press run

Alternatively:
- Open in IntelliJ IDEA
- File -> Project Structure
- Under Project Settings > Modules
- Under 'Sources' tab, right-click on 'src' folder and select 'Sources'
- Apply Changes

## Database
This is an SQLite Database written using SQLite v3.4.4 

In the case, where you want to restore database, connect to ``university.db`` through SQLite
and run the provided ```universityDBScript.sql``` from there.
This is currently the easiest method to restore the DB to factory settings

## Using the Application:
By default, the database contains one admin user and two regular users. 
The details are: 
#### Admin User: 
    username: admin  
    password: password
#### Regular Users:
    username: john  
    password: 123

    username: bob  
    password: 456

### The following defaults are created
    University: Solent
    Buildings:
        Name: The Spark
        Code: TS
        Rooms:
            Room Number: 101
            Type: Windows Room
            Computers: 10

            Room Number: 202:
            Type: Linux Room:
            Computers: 8
    
    Name: Herbert Collins
    Code: HS
    Rooms:
            Room Number: 303
            Type: Mac Room
            Computers: 2        

    Bookings:
        John booked TS101-1 on Monday 9am-11am
        John booked TS101-3 on Wednesday 9am-11am
        John booked HC303-4 on Friday 3pm-5pm
        Bob booked TS101-1 on Monday 11am-1pm
        Bob booked TS202-5 on Thursday 11am-1pm
        Bob booked HC303-1 on Tuesday 1pm-3pm