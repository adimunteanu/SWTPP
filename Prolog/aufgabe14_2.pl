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

printList([ ]) :- true.
printList([H|T]) :- write(H),write(" "),printList(T).

rev([ ],Rev,Result) :- Result=Rev.
rev([H|T],Rev,Result) :- rev(T,[H|Rev],Result).
/* wrapper fuer rev */
revertList(L,R) :- rev(L,[ ],R).

middle(L,Erg) :- middleH(L,L,Erg).
middleH([H|_], [_], H).
middleH([_|LR], [_,_|LRFast], V) :- middleH(LR,LRFast,V).


slice([],_,_,[]).
slice([H|_],0,0,[H]).
slice([LInH|LInT],0,IR,[LInH|Rest]) :- IR > 0, IR1 is IR -1, 
                                       slice(LInT,0,IR1, Rest), !.
slice([_|LInT],IL,IR,Rest) :- IL > 0, IL1 is IL -1, IR1 is IR -1, 
                              slice(LInT,IL1,IR1, Rest).

/* Route mit Zyklenpruefung: hier wird die Route am Ende gedruckt 
   (richtige Reihenfolge) */
route(X,Y,Visited) :- \+ member(X,Visited), \+ member(Y,Visited), 
                         directConnection(X,Y,_), 
						 revertList([Y,X|Visited],FinalRoute), 
						 printList(FinalRoute).
route(X,Z,Visited) :- \+ member(X,Visited), directConnection(X,Y,_), 
                         route(Y,Z,[X|Visited]).

/* Alternative (fuer allRoutes und shortestRoute): FinalRoute 
   erhaelt gefundende Route (richtige Reihenfolge) und FinalDist 
   die Laenge. So koennen diese Werte weiterverwendet werden. */
route2(X,Y,Visited,Dist,[X,Y]) :- \+ member(X,Visited), 
                                  \+ member(Y,Visited), 
                                  directConnection(X,Y,Dist).
route2(X,Z,Visited,Dist,[X|Route1]) :- 
            \+ member(X,Visited), 
            directConnection(X,Y,D), 
            route2(Y,Z,[X|Visited], Dist1, Route1), Dist is Dist1 + D.

allRoutes(X,Y,ResultList) :- 
  findall((Route,Distance), route2(X,Y,[ ],Distance,Route),ResultList).

shortestRoute(X,Y,Shortest) :- 
  setof((Distance,Route), route2(X,Y,[ ],Distance,Route),[Shortest|_]).

/* QUERIES 

?- printList([hallo,1,[1,2,3,4,5],komplex(eins,zwei)]).
hallo 1 [1,2,3,4,5] komplex(eins,zwei) 
true.

?- revertList([1,2,3,4,5], L).
L = [5, 4, 3, 2, 1].

?- route(berlin, muenchen, [ ]).
berlin muenchen 
true ;
berlin frankfurt muenchen 
true ;
berlin frankfurt koeln stuttgart muenchen 
true ;
false.

?- route2(berlin,muenchen,[ ],D,R).
R = [berlin, muenchen],
D = 700 ;
R = [berlin, frankfurt, muenchen],
D = 800 ;
R = [berlin, frankfurt, koeln, stuttgart, muenchen],
D = 1250 ;
false.

?- allRoutes(berlin,muenchen,L).
L = [ ([berlin, muenchen], 700), ([berlin, frankfurt, muenchen], 800), 
      ([berlin, frankfurt, koeln, stuttgart, muenchen], 1250)].

?- shortestRoute(berlin,muenchen,L).
L = (700, [berlin, muenchen]).
*/