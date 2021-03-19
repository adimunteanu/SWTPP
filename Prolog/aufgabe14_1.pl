/* FAKTEN */

street(hamburg,berlin,300).
street(berlin,muenchen,700).
street(muenchen,frankfurt,300).
street(frankfurt,berlin,500).
street(muenchen,stuttgart, 300).
street(stuttgart,koeln, 250).
street(frankfurt,koeln, 200).

/* REGELN */
directConn(X,Y,S) :- street(X,Y,S) ; street(Y,X,S).

connection(A,B) :- directConn(A,B,_).
connection(A,B) :- directConn(A,C,_), connection(C,B).

/* statt Count=2 direkt 2 links von :- */
count(A,B, Count) :- directConn(A,B,_), C is Count +2,
                     write("Weg mit "), write(C), write(" Orten gefunden.").
count(A,B, Count) :- directConn(A,C,_), Count1 is Count +1, 
                     count(C,B,Count1). 
/* query mit ?- count(berlin,stuttgart,0). */                     
					 
distance(A,B,Dist) :- directConn(A,B,Dist).
distance(A,B,Dist) :- directConn(A,C,S), distance(C,B,Dist1), 
                      Dist is Dist1 + S.

/* tail recursive */
distance2(A,B, Dist, Erg) :- directConn(A,B,D), Erg is Dist + D.
distance2(A,B, Dist, Erg) :- directConn(A,C,D), Dist1 is Dist +D
                            , distance2(C,B,Dist1,Erg).					  
/* query mit ?- distance2(berlin,stuttgart,0,Erg). */
							
/* Route ohne Zyklenpruefung: 
   Staedte koennen mehrfach angefahren werden */
route(X,Y) :- directConn(X,Y,_), write(Y), write(" "), write(X).
route(X,Z) :- directConn(X,Y,_), route(Y,Z), write(" "), write(X).


primeHelper(_, 2).
primeHelper(X, H) :- H2 is H-1, \+ (0 is X mod H2), 
                     primeHelper(X, H2).
prime(X) :-  X > 1, primeHelper(X,X).
/*
effizienter:
*/
prime2(X) :-  Y is floor(sqrt(X)) +1, primeHelper(X,Y).

/* QUERIES 

?- connection(berlin, muenchen).
true ;
true ;
true ;
true ;
true ;
true ;
true ; ......... usw

?- count(hamburg,muenchen, C).
3
true ;
5
true ;
6
true ;
8
true ; .......... usw

?- distance(muenchen,koeln, D).
D = 500 ;
D = 2000 ;
D = 3500 ;
D = 5000 ;
D = 6500 ;
D = 8000 ;
D = 9500 ........usw

?- route(berlin,muenchen).
berlin muenchen
true ;
muenchen frankfurt muenchen berlin
true ;
muenchen berlin  frankfurt muenchen berlin
true ;
muenchen frankfurt muenchen berlin frankfurt muenchen berlin
true ;
muenchen berlin frankfurt muenchen berlin frankfurt muenchen berlin
true ;
muenchen frankfurt muenchen berlin frankfurt muenchen berlin 
frankfurt muenchen berlin
true ..........usw

*/