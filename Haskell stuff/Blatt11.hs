-- wird gebraucht in der Zusatzaufgabe für ord
import Data.Char

-- blatt_10 stuff
-- fee
fee :: Int
fee = 1

-- charge
charge :: Int -> Int
charge a = if a > fee then a - fee else 0

-- putChips
putChips :: Int -> Int -> Int
putChips owned bought = charge (owned + bought)

-- takeChips
takeChips :: Int -> Int -> Int
takeChips owned taken = let result = owned - taken in 
            if result >= 0 then result else 0
          
-- win (mit Tail-Rekursion)
win :: Int -> Int -> Int
win a b = if a > b then recurse a b 0 else recurse b a 0
    where
    recurse _ 0 p = p
    recurse 0 _ p = p
    recurse a b p = recurse (a-1) op2 $! (b + p)
                    where
                        op2 = if a `mod` 10 == 0 then b-1 else b 


-- blatt_11 starts here

-- 1a: 
data CommandS = Put | Take | Win deriving Show -- Summentyp/Aufzaehlungstyp

evalS :: CommandS -> Int -> Int -> Int
evalS c a b = case c of -- alternative zum pattern matching
    Put -> putChips a b
    Take -> takeChips a b
    Win -> win a b

-- 1b: Commands with parameters
data CommandP = PutP Int Int | TakeP Int Int | WinP Int Int deriving Show-- Summen- und Produkttyp

evalP :: CommandP -> Int
evalP (PutP a b) = putChips a b
evalP (TakeP a b) = takeChips a b
evalP (WinP a b) = win a b

-- 1c: Oh the recursiveness!
data CommandR = PutR CommandR CommandR 
              | TakeR CommandR CommandR
              | WinR CommandR CommandR 
              | ValR Int
--              | AbsR CommandR -- Zusatz Blatt 12
--              | SigR CommandR -- Zusatz Blatt 12
--              | C CommandR -- Zusatz Blatt 12 Konstruktor Zur Eingabe von CommandR in der Konsole 
              deriving Show

evalR :: CommandR -> Int
evalR (PutR c1 c2) = putChips (evalR c1) (evalR c2)
evalR (TakeR c1 c2) = takeChips (evalR c1) (evalR c2)
evalR (WinR c1 c2) = win (evalR c1) (evalR c2)
evalR (ValR v) = v
-- evalR (AbsR v) = evalR v          --Zusatz Blatt 12
-- evalR (SigR v) = evalR v          --Zusatz Blatt 12
-- evalR (C v) = evalR v             --Zusatz Blatt 12


-- some list stuff
-- 2a: repeat
rep :: Int -> [a] -> [a]
rep 0 _ = []
rep n xs = xs ++ rep (n-1) xs

-- 2b: like reverse, but not reverse
mirror :: [a] -> [a]
mirror [] = []
mirror (x:xs) = [x] ++ mirror xs ++ [x] 

-- 2c: drop2
drop2 :: [a] -> [a]
drop2 [] = []
drop2 (_:[]) = []
drop2 (_:_:xs) = xs

-- 2d: kick
kick :: [CommandR] -> [CommandR]
kick [] = []
kick (x:xs) = if evalR x > 0 then x:kick xs
                             else kick xs

-- 2e: payback
payback :: [CommandR] -> [CommandR]
payback [] = []
payback ((TakeR op1 (ValR _)):xs) = op1: payback xs
payback (x:xs) = x:payback xs

-- 2f: share
share :: CommandR -> [CommandR] -> CommandR -> (CommandR, [CommandR])
share pot [] _ = (pot, [])
share pot (p:ps) part = let (restpot, newps) = share (TakeR pot part) (ps) part in
                            (restpot, (PutR p part):newps)

-- 2*:
data ParseResult a = Success a String | Failure deriving Show

-- 3a: ascii
ascii = ['\0'..'\127']

-- 3b: isVowel
isVowel ('a') = True
isVowel ('e') = True
isVowel ('i') = True
isVowel ('o') = True
isVowel ('u') = True
isVowel _ = False

-- 3c: hasVowel
hasVowel [] = False
hasVowel ('a':xs) = True
hasVowel ('e':xs) = True
hasVowel ('i':xs) = True
hasVowel ('o':xs) = True
hasVowel ('u':xs) = True
hasVowel (_:xs) = hasVowel xs

-- 3d: print
toString :: CommandR -> String

-- try case notation
toString command = case command of 
                   ValR x -> show x
                   PutR x y -> "(" ++ (toString x) ++ " + " ++ (toString y) ++ ")"
                   TakeR x y -> "(" ++ (toString x) ++ " - " ++ (toString y) ++ ")"
                   WinR x y -> "(" ++ (toString x) ++ " * " ++ (toString y) ++ ")"
-- mit case notation

-- beispiel: toString (PutR (WinR (ValR 2) (ValR 3)) (ValR 5))
-- ausgabe: "((2 * 3) + 5)"


-- 3*

type Parser a = String -> ParseResult a
    
pEnd :: Parser ()
pEnd [] = Success () []
pEnd _ = Failure

pChar :: Char -> Parser ()
pChar _ [] = Failure
pChar expected (c:cs) = if c == expected then Success () cs else Failure

pInt :: Parser Int
pInt [] = Failure
pInt (c:cs) = if isDigit c then parse (digitToInt c) cs else Failure
    where
    parse acc [] = Success acc []
    parse acc (c:cs) = 
        if isDigit c then 
            parse (10 * acc + digitToInt c) cs
        else 
            Success acc (c:cs)
    isDigit c = '0' <= c && c <= '9'
    digitToInt c = ord c - ord '0'

pSpaces :: Parser ()
pSpaces (' ':cs) = pSpaces cs
pSpaces str = Success () str


-- 3**

spaced :: Parser a -> Parser a
spaced parser str = case pSpaces str of
    Failure -> Failure
    Success () str' -> parser str'

-- Alternativ:
-- spaced p = pSpaces ==> p


(==>) :: Parser a -> Parser b -> Parser b
(==>) p1 p2 str = case p1 str of
    Failure -> Failure
    Success _ str' -> p2 str'

-- Alternativ:
-- (==>) p1 p2 = p1 |=> (\x -> p2)


(|=>) :: Parser a -> (a -> Parser b) -> Parser b
(|=>) p1 p2 str = case p1 str of
    Failure -> Failure
    Success x str' -> p2 x str'

parens :: Parser a -> Parser a
parens p = spaced (pChar '(') ==> 
            p |=> \result -> 
            spaced (pChar ')') ==> 
            Success result

orElse :: Parser a -> Parser a -> Parser a
orElse p1 p2 str = case p1 str of
    Failure -> p2 str
    success -> success

oneOf :: [Parser a] -> Parser a
oneOf = foldr orElse (const Failure)

-- 3**

pVal = spaced pInt |=> \n -> Success (ValR n)

pBinOp =
    spaced $ oneOf
        [ pChar '+' ==> Success PutR
        , pChar '-' ==> Success TakeR
        , pChar '*' ==> Success WinR ]

pCommand = 
    oneOf 
    [ pVal
    , parens 
        ( pCommand |=> \c1 ->
          pBinOp |=> \op ->
          pCommand |=> \c2 ->
          Success (op c1 c2) )
    ]

-- magic :: Int -> Int
-- magic 1 = 0
-- magic n = n - 1 + magic (n - 1)

magicT 0 result = result
magicT n result = magicT (n-1) (result + n - 1)
magic n = magicT n 0

magicL n = foldr (+) 0 [1..(n-1)]

f :: Int -> Bool 
f x = x `mod` 2 == 0

g :: Int -> Int
g x = x + 1

komp :: Int -> Bool
komp x = (f . g) x

filterElem :: Eq a => a -> [a] -> [a]
filterElem x y = filter (\a -> (a == x)) y

includesEven [] = False 
includesEven (x : xs) = x `mod` 2 == 0 || includesEven xs 

data Sorte = Vanille | Erdbeer | Schoko
data Eis = Eis Sorte Eis | Waffel 

zweiKugelnEis :: Eis 
zweiKugelnEis = Eis Vanille (Eis Schoko Waffel)

instance Show Sorte where 
    show Vanille = "V"
    show Erdbeer = "E"
    show Schoko = "S"

instance Show Eis where
    show (Eis sorte Waffel) = "(" ++ show sorte ++ ")" ++ ">>>"
    show (Eis sorte xs) = "(" ++ show sorte ++ ")" ++ show xs

insertEis :: Eis -> Sorte -> Int -> Eis
insertEis Waffel sorte 0 = Eis sorte Waffel
insertEis Waffel _ invalidPos = Waffel
insertEis eis sorte 0 = Eis sorte eis
insertEis (Eis kugel xs) sorte pos = Eis kugel (insertEis xs sorte (pos - 1))

infinityEis :: Eis
infinityEis = let list = map (\x -> mod x 2) [0..] in
    foldr(\x y -> case x of
        1 -> Eis Schoko y
        0 -> Eis Vanille y
        ) Waffel list

data Task = Task Int String [Task]
task :: Task
task = Task 1 "Hausarbeit" [Task 2 "Kochen" [], Task 3 "Putzen" []]

instance Eq Task where
    (Task id_x b_x t_x) == (Task id_y b_y t_y) = (id_x == id_y)

collectIds :: Task -> [Int]
collectIds (Task id _ []) = [id]
collectIds (Task id desc (Task sub_id _ tasks:xs)) = sub_id : collectIds (Task id desc xs)

dependent :: [(Int, Int)] -> Int -> [Int]
dependent [] _ = []
dependent ((supporter, dependee) : rest) pillar = if supporter == pillar then dependee : dependent rest pillar
                                                                         else dependent rest pillar