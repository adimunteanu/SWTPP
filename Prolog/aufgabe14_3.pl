/* 4 Vikings */

/* FAKTEN */
viking(leif,5).
viking(erik,10).
viking(thor,20).
viking(lars,25).

/* REGELN */

/* Entweder einer oder zwei Vikinger gehen über die Brücke (von liste Left zu Liste Right)- nach rechts bevorzugt zwei Vikinger, daher also die Variante zuerst (Tiefensuche). Nach mehr als 60 min wird der Ausdruck false, also die Rekursion vorzeitig abgebrochen */

moveRight(Left,Right,History,Time) :- (member(X,Left), member(Y,Left), X\==Y, viking(X,T1), viking(Y,T2), T1 =< T2, delete(Left,X,Left1), delete(Left1,Y,Left2), Time1 is Time + max(T1,T2), Time1 =< 60, moveLeft(Left2, [X,Y|Right], [[X,Y]|History], Time1)) ;
									  (member(X,Left), viking(X,T1), delete(Left,X,Left1), Time1 is Time + T1, Time1 =< 60, moveLeft(Left1, [X|Right], [[X]|History], Time1)).

/* Die linke Seite der Brücke ist leer, das Ziel also erreicht -> Rekusionsanker für den Erfolgsfall */
moveLeft([ ],_,History,Time) :- write(History),write(Time).
/* Nach links soll bevorzugt nur ein Wikinger gehen, daher steht die erste Klausen vom ';' zuerst da. */
moveLeft(Left,Right,History,Time) :-  (member(X,Right), viking(X,T1),  delete(Right,X,Right1), Time1 is Time + T1, Time1 =< 60, moveRight([X|Left], Right1, [[X]|History], Time1));
									  (member(X,Right), member(Y,Right), X\==Y, viking(X,T1), viking(Y,T2), T1 =< T2, delete(Right,X,Right1), delete(Right1,Y,Right2), Time1 is Time + max(T1,T2), Time1 =< 60, moveRight([X,Y|Left], Right2, [[X,Y]|History], Time1))
									  .

/* um den Aufruf in der Console handlicher zu gestalten wird das hier schonmal abgekürzt */
solve :- moveRight([leif,erik,thor,lars], [ ], [ ],0).
									  