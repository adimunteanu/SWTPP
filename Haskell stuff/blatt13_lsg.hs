-- 1 module siehe CommandR.hs
import CommandR

-- 2a: maxin
maxin :: Ord a => [a] -> a
--maxin [] Undefined!
maxin (x:[]) = x
maxin (x:xs) =
    let rest = maxin xs in
    max x rest

-- 2b: maybe
maxin2 :: Ord a => [a] -> Maybe a
maxin2 [] = Nothing
maxin2 l = Just (maxin l)

-- Beispiel: 
-- *Main> maxin2 [PutR (5) (2), WinP (2) (GetP (6) (2))]
-- Just (2*(6-2))


-- 3a)
cycl :: [a] -> [a]
cycl xs = xs ++ cycl xs
-- wie rep, aber kein Rekursionsanker!

-- 3b)
-- infinite list
--pows = map pot [0..]
pows = 1 : map (2 *) pows

-- 3c)
pow2 :: Int -> Int -> Int
pow2 x n = foldr (*) 1 (take n [x,x..])

-- 3d)
fibs :: [Int]
fibs = 0 : 1 : zipWith (+) fibs (tail fibs)

-- 3*)
collatz :: Int -> [Int]  
--collatz 1 = [1]  
collatz n  
    | even n = n : collatz (n `div` 2)
    | odd n = n : collatz (n*3 + 1)

isCollatz :: [Int] -> Bool
isCollatz [] = False
isCollatz (4:2:1:xs) = True
isCollatz (x:xs) = isCollatz xs

checkTill n = (foldr (&&) True . map isCollatz . map collatz) [1..n]
