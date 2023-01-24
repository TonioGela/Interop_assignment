# Interop assignment

## General Requirements
- You may use whatever programming language you prefer using only its standard library (i.e. no external libraries)
- You should commit your code on GitHub or any other SCM repository you prefer and send us the link
- You should deliver the sources of your application, with a README that explains how to compile and run it
- The task requirements are trivial, we are more interested in design decisions, code layout and approach

**IMPORTANT**: Implement the requirements focusing on writing the best code you can produce.

# CSV Previewer

For this assignment you should write an application that accepts a csv file and prints a report of its content.

### CSV Structure
The expected structure of the input file is following:

```csv
id     ,name    ,surname   ,birth-year ,country       ,amount  ,currency
10001  ,Mario   ,Catozzo   ,      1956 ,Greece        ,100000  ,EUR
10002  ,Galileo ,Galilei   ,      1564 ,Italy         ,-100    ,ITL
10003  ,Ursula  ,Le Guin   ,      1929 ,United States ,1234567 ,USD
10004  ,Martin  ,Odersky   ,      1958 ,Germany       ,90345   ,EUR
10005  ,Anakin  ,Skywalker ,      1990 ,Tatooine      ,5       ,ITL
10005  ,Darth   ,Vader     ,      1990 ,Coruscant     ,654321  ,ITL
10006  ,Karen   ,          ,      1975 ,United States ,1450    ,USD
10007  ,Luigi   ,Mario     ,      1981 ,Japan         ,110     ,
       ,Jimmy   ,Ridimmi   ,      1931 ,Faroe Islands ,30      ,GBP
100ten ,Ranma   ,Saotome   ,      1996 ,Japan         ,3489    ,EUR
```

The following field contraints MUST be enforced by the application:
- `id` MUST be a non empty positive unique number
- At least one between `name` and `surname` MUST be non empty
- If there's an `amount`, `currency` SHOULD be present otherwise assume `EUR`

The application should not break when encounters an unsatisfied field constraint but it should report the error including the id or row number

---

### Report Structure

- Assuming:
    - 1 GBP == 1.18 EUR
    - 1 EUR == 1 USD
    - 1 EUR == 1936.27 ITL

  Calculate and report who is the richest and least rich person

  ```
  Richest: Mario Catozzo, Poorest: Luigi Mario
  ```

- Get a list of the surnames of the greek users if any, otherwise report the lack of greek users
   ```
   Greek users: ...
   ```
- Get the list of the countries using ITL as a currency
  ```
  ITL using countries: ...
  ```
- Get the top 5 list of countries with the highest number of users and report the number of users too
   ```
   United States 3
   Tatooine      1
   ...
   ```

- flatmap

- OPTIONAL: Assume to have a second csv file with the same users but with different cash amounts, tell for each person presente in ambo i file if he/she/it/they is/are richer or poorer and the money delta

   CSV 1:
   ```
   ...
   10002,Galileo,Galilei,1564,Italy,-100,ITL
   ...
   ```

   CSV 2:
   ```
   ...
   10002,Galileo,Galilei,1564,Italy,200,USD
   ...
   ```

   result: 
   ```
   Galileo Galilei is 200.0005 USD richer than before
   ```
