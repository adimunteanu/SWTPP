city(hamburg).
city(berlin).
city(koeln).
city(frankfurt).
city(stuttgart).
city(muenchen).

street(berlin,hamburg,300).
street(berlin,frankfurt,500).
street(muenchen,berlin,700).
street(frankfurt,muenchen,300).
street(frankfurt,koeln,200).
street(koeln,stuttgart,250).
street(stuttgart,muenchen,300).

father(anakin, luke).
father(anakin, leia).
mother(shmi, anakin).
grandmother(G, S) :- mother(G, C), (father(C, S); mother(C, S)).


directConnection(Start, Ziel, Entfernung) :-
    street(Start, Ziel, Entfernung);
    street(Ziel, Start, Entfernung).

dayTrip(Start, Ziel, Entfernung, Maximallaenge) :-
    directConnection(Start, Ziel, Entfernung),
    Entfernung =< Maximallaenge.

oneStopConnection(Start, Ziel, Entfernung) :-
    directConnection(Start, Zwischen, L1),
    directConnection(Zwischen, Ziel, L2),
    L is L1 + L2.

shortestOneStop(Start, Ziel, Via, LK) :-
    directConnection(Start, Via, L1),
    directConnection(Via, Ziel, L2),
    L is L1 + L2,
    (oneStopConnection(Start, Ziel, Ln), Ln < L).
