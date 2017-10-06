# Coding Challenge Project For Walmart -- A Ticketing Service

This is a Java / Maven / Spring application for Walmart coding challenge  

* Make sure you are using JDK 1.8 and Maven 3.x
* The application use a property file as arguments for number of rows, columns and seat hold expiration time.
  The unit test is based on the default property file, change of property file may lead to the failure of unit testing.
  But you are welcomed to play around with the property file when run the application with mvn exec:java. 
* The change of calculation of weighted average in **private int getSeatScore(int rowNum, int colNum)** will also lead to
  the failure of unit testing. But you are welcomed to play around with the weighted average, the algorithm will always
  give you the best seats on top of the current weighted average calculation.

## How to Run 

```bash

# clone the repo
$ git clone https://github.com/YixingCheng/walmart-ticket my-app

# change directory to your app
$ cd my-app

# install the dependencies
$ mvn install

# build the project
$ mvn package

# run unit test
$ mvn test

# run application
$ mvn exec:java

```

## Some Explanations

Here is a list of core assumptions and ideas that I used to implement the project

* the findAndHoldSeats() in TicketService needs to find best seats, but how do we define best?
  Based on our common sense when watch a movie, the seat in the center of the venue are usually the best seat.
  So I use a rating system for all the seats. The seat in the center will be rated as 100 and the seats in four corners
  will be rated as 0. The score changes linearly from center to edge. 
  I also give the row higher weight than column, because people prefer sit in the middle row instead of middle column if
  they can only choose one. So that's the reason why you see (rowScore * 2 + colScore * 1) / 3 at the end of that method.
  
 ```
  So based on this algorithm, a 9 * 17 venue will be rated like:
  
  0  4  8  12 16 20 24 28 32 28 24 20 16 12  8  4  0
  16 20 24 28 32 36 40 44 48 44 40 36 32 28 24 20 16
  33 37 41 45 49 53 57 61 65 61 57 53 49 45 41 37 33
  50 54 58 62 66 70 74 78 82 78 74 70 66 62 58 54 50
  66 70 74 78 82 86 90 94 98 94 90 86 82 78 74 70 66
  50 54 58 62 66 70 74 78 82 78 74 70 66 62 58 54 50
  33 37 41 45 49 53 57 61 65 61 57 53 49 45 41 37 33
  16 20 24 28 32 36 40 44 48 44 40 36 32 28 24 20 16
  0  4  8  12 16 20 24 28 32 28 24 20 16 12 8  4  0  
  
  a 10 * 18 venue will be rated like:
  
  0  4  8  12 16 20 24 28 32 32 28 24 20 16 12  8  4  0
  16 20 24 28 32 36 40 44 48 48 44 40 36 32 28 24 20 16
  33 37 41 45 49 53 57 61 65 65 61 57 53 49 45 41 37 33
  50 54 58 62 66 70 74 78 82 82 78 74 70 66 62 58 54 50
  66 70 74 78 82 86 90 94 98 98 94 90 86 82 78 74 70 66
  66 70 74 78 82 86 90 94 98 98 94 90 86 82 78 74 70 66
  50 54 58 62 66 70 74 78 82 82 78 74 70 66 62 58 54 50
  33 37 41 45 49 53 57 61 65 65 61 57 53 49 45 41 37 33
  16 20 24 28 32 36 40 44 48 48 44 40 36 32 28 24 20 16
  0  4  8  12 16 20 24 28 32 32 28 24 20 16 12 8  4  0
 ``` 

* To implement findAndHoldSeats(), I use some common sense again.
  Because we always want to seat with your family or friends, so the best group of seats
  should adjacent to each other. Together with the seat scores, **the problem now is to find
  adjacent seats with highest total score within available seats.**
  I use **DFS** to search for the seat combination with highest socre.
  
```
    _ : unbooked,   # : hold,  * : reserved/commited
    
    If we reserve 5 seats at the very begining, the result should be
    
    _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ 
    _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ 
    _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ 
    _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ 
    _ _ _ _ _ _ # # # # # _ _ _ _ _ _ 
    _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ 
    _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ 
    _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ 
    _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ 
    
    since these 5 seats have a score of 94, 98, 90, 94, 90 which is the highest in all avaialbe combination
    
    If then we randomly book some seats like:
    
    _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ 
    _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ 
    _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ 
    _ _ _ _ _ # # # # _ _ _ _ _ _ _ _ 
    _ _ _ _ # _ # # # # # _ # _ _ _ _ 
    _ _ _ _ _ _ # # # # # _ _ _ _ _ _ 
    _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ 
    _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ 
    _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ 
    

    and then we book another 5 seats, the result should be
    
    @: newly booked seats
    
    _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ 
    _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ 
    _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ 
    _ _ _ _ _ # # # # @ @ @ _ _ _ _ _ 
    _ _ _ _ # _ # # # # # @ # _ _ _ _ 
    _ _ _ _ _ _ # # # # # @ _ _ _ _ _ 
    _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ 
    _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ 
    _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ 
    
    since these 5 adjacent seats have a score of 74, 86, 70, 70, 78 which is the highest in all avaialbe combination
    
   
``` 

# Questions and Comments: waldenlaker@gmail.com





