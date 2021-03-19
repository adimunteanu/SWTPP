/* FAKTEN */

street(hamburg,berlin,300).
street(berlin,muenchen,700).
street(muenchen,frankfurt,300).
street(frankfurt,berlin,500).
street(muenchen,stuttgart, 300).
street(stuttgart,koeln, 250).
street(frankfurt,koeln, 200).

/* REGELN */
directConnection(X,Y,S) :- street(X,Y,S) ; street(Y,X,S).

dayTrip(X,Y,Max,S) :- directConnection(X,Y,S),S < Max.

oneStopConnection(A,B,L) :- directConnection(A,C,L1),
                            directConnection(C,B,L2), L is L1+L2.

shortestOneStop(A,B,VIA,LK) :- directConnection(A,VIA,L1), 
                            directConnection(VIA,B,L2), LK is L1+L2, 
                            \+ (oneStopConnection(A,B,LL), LL < LK).
							
pretty(X,Y) :- printConnection(X,Y).
printConnection(X,Y) :- directConnection(X,Y,S),
                          write(X), write("--->"),
                          write(Y),write(": "),write(S).

dump(X,Y) :- pretty(X,Y), write("\n"), false.

/* BEISPIEL-QUERIES

?- directConnection(berlin,muenchen,_).
true;
false.

directConnection(muenchen,berlin,_).
true.

?- directConnection(muenchen,berlin,S).
S = 700.


?- dayTrip(berlin, Y, 301,S).
Y = hamburg,
S = 300 ;
false.

?- dayTrip(berlin, Y, 501,S).
Y = hamburg,
S = 300 ;
Y = frankfurt,
S = 500.

 ?- oneStopConnection(hamburg,berlin).
false.

?- oneStopConnection(hamburg,frankfurt).
true ;
false.

?- oneStopConnection(berlin, X).
X = frankfurt ;
X = stuttgart ;
X = berlin ;
X = berlin ;
X = berlin ;
X = koeln ;
X = muenchen.

?- pretty(muenchen, berlin).
muenchen--->berlin: 700
true.

*/
