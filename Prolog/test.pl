start(b1).
end(b6).

edge(b1,b2).
edge(b1,b6).
edge(b2,b3).
edge(b2,b4).
edge(b3,b5).
edge(b4,b5).
edge(b5,b1).

constraint(b5,2).

node(n1).
node(n2).
node(n3).

leaf(l1,5).
leaf(l2,6).
leaf(l3,7).
leaf(l4,9).

left(n1,l1).
left(n2,n3).
left(n3,l3).

right(n1,n2).
right(n2,l2).
right(n3,l4).

/*node(X) :- start(X); end(X); edge(X,_); edge(_,X).*/

isEndNode(X) :- end(X), (\+ edge(X,_)).

count([],_,0).
count([X|T],X,C):- count(T,X,C1), C is C1+1.
count([H|T],X,C):- H \= X, count(T,X,C).

countAll(Gesucht, L, Erg3) :- count(L, Gesucht, Erg3).

path(A,B,Visited,Path) :- edge(A,B), append(Visited,[A,B],Path).
path(A,B,Visited,Path) :- edge(A,Via), countAll(A, Visited, CountA),
    constraint(A,Limit), CountA < Limit,
    path(Via,B,[Visited|A],Path).

child(X,Y) :- leaf(X,Y); left(X,Y); right(X,Y).

root(X) :- \+ child(_,X).

mergeSorted(L1,L2,L) :- mergeSortedH(L1,L2,[],L).

mergeSortedH([],[],L, Erg) :- Erg is L.
mergeSortedH([HL1|TL1],[HL2|TL2],L,Erg) :- HL1 < HL2, mergeSortedH(TL1,[HL2|TL2],[HL1|L],Erg).
mergeSortedH([HL1|TL1],[HL2|TL2],L,Erg) :- HL1 >= HL2, mergeSortedH([HL1|TL1],TL2,[HL2|L],Erg).

toList(X,L) :- toListH(X,[ ], L).

toListH(X,L,Erg) :- left(X,Via), toList(Via,L,Erg).
toListH(X,L,Erg) :- leaf(X,Leaf), append(L, [Leaf], Erg).
toListH(X,L,Erg) :- right(X,Via), toList(Via,L,Erg).
