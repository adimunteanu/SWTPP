/* FAKTEN */

street(hamburg,berlin,300).
street(berlin,muenchen,700).

street(muenchen,frankfurt,300).
street(frankfurt,berlin,500).
street(muenchen,stuttgart, 300).
street(stuttgart,koeln, 250).
street(frankfurt,koeln, 200).

/* QUERIES

?- street(hamburg,berlin,300).
true.

?- street(muenchen,frankfurt,_).
true;
false.

?- street(berlin,muenchen,S).
S = 700.

?- street(berlin,X,_).
X = muenchen.

*/

/*
Diese Faktenbasis ist minimal im Sinne der Aufgabenstellung,
in der Strassen beschrieben werden. Ein vollstaendiges Bild
der Topographie, in der auch Orte ohne Strasse moeglich waeren,
koennte durch zusaetzliche Praedikate erreicht werden, z.B.:
city(bielefeld).

Sofern wir aber annehmen, dass alle Orte auch mindestens an
einer Strasse sind, kann city aus den Strassen ermittelt werden:

city(X) :- street(X,_,_); street(_,_,X).
*/

directConnection(X,Y,D) :- street(X,Y,D) ; street(Y,X,D).

dayTrip(X,Y,Max,D) :- directConnection(X,Y,D), D < Max.

oneStopConnection(X,Y,L) :-
    directConnection(X,Z,D1), directConnection(Z,Y,D2), L is D1 + D2.

shortestOneStop(A,B,VIA,LK) :- directConnection(A,VIA,L1),
                            directConnection(VIA,B,L2), LK is L1+L2,
                            \+ (oneStopConnection(A,B,LL), LL < LK).

pretty(X,Y) :-  printConnection(X,Y).

printConnection(X,Y) :- directConnection(X,Y,S),
                        write(X), write("--->"),
                        write(Y), write(": "),write(S).

dump(X,Y) :- pretty(X,Y), write("\n"), false.

connection(A,B) :- directConnection(A,B,_).
connection(A,B) :- directConnection(A,Via,_), connection(Via,B).

count(A,B,2) :- directConnection(A,B,_).
count(A,B,Erg) :- directConnection(A,Via,_), count(Via,B,Erg1), Erg is Erg1 + 1.

countSol(A,B, Count) :- directConnection(A,B,_), C is Count +2,
                     write("Weg mit "), write(C), write(" Orten gefunden.").
countSol(A,B, Count) :- directConnection(A,C,_), Count1 is Count +1,
                     countSol(C,B,Count1).

distance(A,B,Dist) :- directConnection(A,B,Dist).
distance(A,B,Erg) :- directConnection(A,Via,D1), distance(Via,B,D2), Erg is D1 + D2.

distance2(A,B,Dist,Erg) :- directConnection(A,B,D), Erg is Dist + D.
distance2(A,B,Dist,Erg) :- directConnection(A,Via,D1), Dist1 is Dist + D1,
                           distance2(Via,B,Dist1,Erg).

route(A,B) :- directConnection(A,B,_), write(A), write(" "), write(B).
route(A,B) :- directConnection(A,Via,_),write(A), write(" "), route(Via,B).

primeHelper(_,1) :- !.
primeHelper(N,C) :- C > 1, 0 =\= N mod C, CNeu is C - 1, primeHelper(N, CNeu).

prime(X) :- X > 1, Start is X - 1, primeHelper(X,Start).

printList([]) :- !.
printList([H|T]) :- write(H), write(" "), printList(T).

rev([ ],Reversed, Result) :- Result = Reversed.
rev([H|T],Reversed, Result) :- rev(T,[H|Reversed],Result).

middle(L,Erg) :- middleH(L,L,Erg).
middleH([H|_],[_], H).
middleH([_|LR], [_,_|LRFast], V) :- middleH(LR,LRFast,V).

slice([],_,_,[]).
slice([H|_],0,0,[H]).
slice([LInH|LInT],0,IR,[LInH|Rest]) :- IR > 0, IR1 is IR -1,
                                       slice(LInT,0,IR1, Rest), !.
slice([_|LInT],IL,IR,Rest) :- IL > 0, IL1 is IL -1, IR1 is IR -1,
                              slice(LInT,IL1,IR1, Rest).

revertList(L,R) :- rev(L,[ ],R).

route(X,Y,Visited) :- directConnection(X,Y,_), \+ member(X,Visited), \+ member(Y,Visited),
                           revertList([Y,X|Visited], FinalRoute),
                           printList(FinalRoute).
route(X,Y,Visited) :- directConnection(X,Via,_), \+ member(X,Visited), route(Via,Y, [X|Visited]).

