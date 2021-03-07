-- module (NICHT ÄNDERN!)
module CrazyhouseBot
    ( getMove
    , listMoves
    )
    where

import Data.Char
-- Weitere Modulen können hier importiert werden
import Text.Regex.Posix
import Util
import System.Random

--- external signatures (NICHT ÄNDERN!)
getMove :: String -> String
getMove state = let raw_moves = listMoves state in
                let moves = substring 1 (length raw_moves - 1) raw_moves in
                let split_moves = splitOn ',' moves in
                let seed_rand = mkStdGen (length split_moves) in
                let (rand, _) = randomR (0, (length split_moves - 1)) seed_rand in
                split_moves !! rand


listMoves :: String -> String
listMoves state = ['['] ++ flatten (getValidMoves (getState state)) ++ [']']


-- YOUR IMPLEMENTATION FOLLOWS HERE

type Position = (Int, Int)
type Field = String
type Move = String
type Color = Char
type Piece = (Position, Color)
type Row = String
type Board = [Row]
type Path = [Position]
type FieldPath = [Field]
type State = (Board, Row, Color)

substring :: Int -> Int -> String -> String
substring i j s = take (j - i) (drop i s)

getState :: String -> State
getState raw_state = let (board, reserve, color) = getState' raw_state in
                     if length board == 7 then (board ++ [reserve], "", color)
                     else (board, reserve, color)
getState' :: String -> State
getState' raw_state = let config = splitOn ' ' raw_state in
                 let board = splitOn '/' (addSpace (head config)) in
                 let reserve = board !! (length board - 1) in
                 let rows = take (length board - 1) board in
                 let player = head (config !! 1) in
                 (rows, reserve, player)

getBoard :: State -> Board
getBoard (rows, reserve, player) = rows

flatten :: [Move] -> String
flatten moves = let str = flatten' moves in
                take (length str - 1) str
flatten' :: [Move] -> String
flatten' [] = ""
flatten' (move : moves) = move ++ "," ++ flatten' moves

getValidMoves :: State -> [Move]
getValidMoves (board, reserve, color) = let moves = (getAllValidPieceMoves board color ++ getValidReserveMoves board reserve color) in
                                        getValidMoves' board color moves
getValidMoves' :: Board -> Color -> [Move] -> [Move]
getValidMoves' _ _ [] = []
getValidMoves' board color (move : moves) = if isInCheck (doMove board move) color
                                            then getValidMoves' board color moves
                                            else move : getValidMoves' board color moves


isInCheck :: Board -> Color -> Bool
isInCheck board 'w' = let kingField = positionToField (getKingPositionByColor board 'w') in
                      let enemyMoves = getAllValidPieceMoves board 'b' in
                      isInCheck' kingField enemyMoves
isInCheck board 'b' = let kingField = positionToField (getKingPositionByColor board 'b') in
                      let enemyMoves = getAllValidPieceMoves board 'w' in
                      isInCheck' kingField enemyMoves
isInCheck' :: Field -> [Move] -> Bool
isInCheck' _ [] = False
isInCheck' kingField (move : enemyMoves) = let fields = splitOn '-' move in kingField == fields !! 1 || isInCheck' kingField enemyMoves

doMove :: Board -> Move -> Board
doMove board move
  | move =~ "[a-h]{1}[1-8]{1}[-]{1}[a-h]{1}[1-8]{1}" = doPieceMove board move
  | otherwise = doReserveMove board move

doPieceMove :: Board -> Move -> Board
doPieceMove board move = let fields = splitOn '-' move in
                         let (f_y, f_x) = fieldToPosition (head fields) in
                         let (t_y, t_x) = fieldToPosition (fields !! 1) in
                         let piece = getFieldPiece board (head fields) in
                         let from_board = updateBoard board f_y (updateRow (board !! f_y) f_x '1') in
                         updateBoard from_board t_y (updateRow (from_board !! t_y) t_x piece)

doReserveMove :: Board -> Move -> Board
doReserveMove board move = let fields = splitOn '-' move in
                           let piece = head (fields !! 0) in
                           let (t_y, t_x) = fieldToPosition (fields !! 1) in
                           updateBoard board t_y (updateRow (board !! t_y) t_x piece)

updateBoard :: Board -> Int -> Row -> Board
updateBoard board index new_row = take index board ++ [new_row] ++ drop (index + 1) board

updateRow :: Row -> Int -> Char -> Row
updateRow row index new_piece = take index row ++ [new_piece] ++ drop (index + 1) row

between_Bot low high x = low <= x && x <= high
isRank_Bot = between_Bot '2' '8'

fromNumberToOnes :: Int -> String
fromNumberToOnes 1 = ['1']
fromNumberToOnes x = '1' : fromNumberToOnes (x - 1)

addSpace :: Row -> Row
addSpace [] = []
addSpace (x : xs) = if isRank_Bot x then fromNumberToOnes (digitToInt x) ++ addSpace xs else x : addSpace xs

colorToDir :: Color -> Int
colorToDir color = if color == 'w' then (-1) else 1

isFieldInBoard :: Field -> Bool
isFieldInBoard field =  let row = field !! 1 in
                        let col = head field in
                        row >= '1' && row <= '8' && col >= 'a' && col <= 'h'

isMoveInBoard :: Move -> Bool
isMoveInBoard move = let fields = splitOn '-' move in
                     let lenFrom = length (head fields) in
                     let lenTo = length (fields !! 1) in
                     not (lenTo /= 2 || lenFrom /= 2) && (isFieldInBoard (fields !! 0) && isFieldInBoard (fields !! 1))

fieldToPosition :: Field -> Position
fieldToPosition field = (8 - (ord (field !! 1) - 48), ord (head field) - 97)

positionToField :: Position -> Field
positionToField (i, j) = chr (j + 97) : [chr (56 - i)]

getAllValidPieceMoves :: Board -> Color -> [Move]
getAllValidPieceMoves board color = getAllValidPieceMoves' board board color 0
getAllValidPieceMoves' :: Board -> Board -> Color -> Int -> [Move]
getAllValidPieceMoves' _ [] _ _ = []
getAllValidPieceMoves' origin_board (row: board) color row_index = getAllValidPieceMovesByRow origin_board row row_index color ++ getAllValidPieceMoves' origin_board board color (row_index + 1)
getAllValidPieceMovesByRow :: Board -> Row -> Int -> Color -> [Move]
getAllValidPieceMovesByRow board row row_index color = getAllValidPieceMovesByRow' board row color row_index 0
getAllValidPieceMovesByRow' :: Board -> Row -> Color -> Int -> Int -> [Move]
getAllValidPieceMovesByRow' _ _ _ row_index 8 = []
getAllValidPieceMovesByRow' board (slot : row) 'b' row_index col_index = if [slot] =~ "[prnbqk]{1}"
                                              then getValidPieceMoves board slot (positionToField (row_index, col_index)) ++ getAllValidPieceMovesByRow' board row 'b' row_index (col_index + 1)
                                              else getAllValidPieceMovesByRow' board row 'b' row_index (col_index + 1)
getAllValidPieceMovesByRow' board (slot : row) 'w' row_index col_index = if [slot] =~ "[PRNBQK]{1}"
                                              then getValidPieceMoves board slot (positionToField (row_index, col_index)) ++ getAllValidPieceMovesByRow' board row 'w' row_index (col_index + 1)
                                              else getAllValidPieceMovesByRow' board row 'w' row_index (col_index + 1)

getValidPieceMoves :: Board -> Char -> Field -> [Move]
getValidPieceMoves board piece field =  validatePieceMoves board piece (getAllPieceMoves piece field)
validatePieceMoves :: Board -> Char -> [Move] -> [Move]
validatePieceMoves _ _ [] = []
validatePieceMoves board piece (move : moves)
  | [piece] =~ "p|P" = let field = head (splitOn '-' move) in
    if validatePawnMove board move (getFieldColor board field)
    then move : validatePieceMoves board piece moves
    else validatePieceMoves board piece moves
  | [piece] =~ "n|N" = let field = head (splitOn '-' move) in
    if validateKnightMove board move (getFieldColor board field)
    then move : validatePieceMoves board piece moves
    else validatePieceMoves board piece moves
  | otherwise = let field = head (splitOn '-' move) in
    if validatePathMove board move (getFieldColor board field)
    then move : validatePieceMoves board piece moves
    else validatePieceMoves board piece moves

getAllPieceMoves :: Char -> Field -> [Move]
getAllPieceMoves piece field = removeMovesOutOfBounds (getAllPieceMoves' piece field)
getAllPieceMoves' :: Char -> Field -> [Move]
getAllPieceMoves' 'p' field = getPawnMoves (fieldToPosition field, 'b')
getAllPieceMoves' 'P' field = getPawnMoves (fieldToPosition field, 'w')
getAllPieceMoves' piece field
  | [piece] =~ "n|N" = getKnightMoves (fieldToPosition field)
  | [piece] =~ "k|K" = getKingMoves (fieldToPosition field)
  | [piece] =~ "r|R" = getRookMoves (fieldToPosition field)
  | [piece] =~ "b|B" = getBishopMoves (fieldToPosition field)
  | otherwise = getQueenMoves (fieldToPosition field)

removeMovesOutOfBounds :: [Move] -> [Move]
removeMovesOutOfBounds [] = []
removeMovesOutOfBounds (x : xs) = if isMoveInBoard x then x : removeMovesOutOfBounds xs else removeMovesOutOfBounds xs

positionsToMove :: Position -> Position -> Move
positionsToMove (x1, y1) (x2, y2) = positionToField (x1, y1) ++ ['-'] ++ positionToField (x2, y2)

getPawnMoves :: Piece -> [Move]
getPawnMoves ((i, j), color) = let dir = colorToDir color in [
                                    positionsToMove (i, j) (i + dir, j),
                                    positionsToMove (i, j) (i + 2 * dir, j),
                                    positionsToMove (i, j) (i + dir, j + dir),
                                    positionsToMove (i, j) (i + dir, j - dir)
                                ]
getKnightMoves :: Position -> [Move]
getKnightMoves (i, j) = [
                                 positionsToMove (i, j) (i + 2, j + 1),
                                 positionsToMove (i, j) (i + 2, j - 1),
                                 positionsToMove (i, j) (i - 2, j + 1),
                                 positionsToMove (i, j) (i - 2, j - 1),
                                 positionsToMove (i, j) (i + 1, j + 2),
                                 positionsToMove (i, j) (i - 1, j + 2),
                                 positionsToMove (i, j) (i + 1, j - 2),
                                 positionsToMove (i, j) (i - 1, j - 2)
                                ]
getKingMoves :: Position -> [Move]
getKingMoves (i, j) = [
                                 positionsToMove (i, j) (i + 1, j - 1),
                                 positionsToMove (i, j) (i + 1, j),
                                 positionsToMove (i, j) (i + 1, j + 1),
                                 positionsToMove (i, j) (i, j - 1),
                                 positionsToMove (i, j) (i, j + 1),
                                 positionsToMove (i, j) (i - 1, j - 1),
                                 positionsToMove (i, j) (i - 1, j),
                                 positionsToMove (i, j) (i - 1, j + 1)
                                ]

getRookMoves :: Position -> [Move]
getRookMoves (i, j) = getRookMoves' (i, j) True 7 1 ++ getRookMoves' (i,j) True 7 (-1) ++ getRookMoves' (i,j) False 7 1 ++ getRookMoves' (i,j) False 7 (-1)
getRookMoves' :: Position -> Bool -> Int -> Int -> [Move]
getRookMoves' (i,j) horiz 0 dir = []
getRookMoves' (i,j) horiz steps dir = getRookMoves' (i,j) horiz (steps - 1) dir ++ [if horiz then positionsToMove (i,j) (i, j + steps * dir) else positionsToMove (i,j) (i + steps * dir, j)]

getBishopMoves :: Position -> [Move]
getBishopMoves (i, j) = getBishopMoves' (i, j) 7 1 1 ++ getBishopMoves' (i,j) 7 1 (-1) ++ getBishopMoves' (i,j) 7 (-1) 1 ++ getBishopMoves' (i,j) 7 (-1) (-1)
getBishopMoves' :: Position -> Int -> Int -> Int -> [Move]
getBishopMoves' (i,j) 0 dir_x dir_y = []
getBishopMoves' (i,j) steps dir_x dir_y = getBishopMoves' (i,j) (steps - 1) dir_x dir_y ++ [positionsToMove (i,j) (i + steps * dir_y, j + steps * dir_x)]

getQueenMoves :: Position -> [Move]
getQueenMoves (i,j) = getRookMoves (i,j) ++ getBishopMoves (i,j)


positionToReserveMove :: Char -> Position -> Move
positionToReserveMove piece pos = [piece] ++ ['-'] ++ positionToField pos

getValidReserveMoves :: Board -> String -> Color -> [Move]
getValidReserveMoves board pocket color = validateReserveMoves board (getAllReserveMoves pocket color)

validateReserveMoves :: Board -> [Move] -> [Move]
validateReserveMoves _ [] = []
validateReserveMoves board (move : moves) = if getFieldColor board ((splitOn '-' move) !! 1) == '1'
                                      then move : validateReserveMoves board moves
                                      else validateReserveMoves board moves
getAllReserveMoves :: String -> Color -> [Move]
getAllReserveMoves [] _ = []
getAllReserveMoves ('p' : pocket) 'w' = getAllReserveMoves pocket 'w'
getAllReserveMoves ('p' : pocket) 'b' = getAllReserveMoves' 'p' 0 1 6 ++ getAllReserveMoves pocket 'b'
getAllReserveMoves ('P' : pocket) 'b' = getAllReserveMoves pocket 'b'
getAllReserveMoves ('P' : pocket) 'w' = getAllReserveMoves' 'P' 0 1 6 ++ getAllReserveMoves pocket 'w'
getAllReserveMoves (piece : pocket) 'w' = if [piece] =~ "[prnbqk]{1}"
                                          then getAllReserveMoves pocket 'w'
                                          else getAllReserveMoves' piece 0 0 7 ++ getAllReserveMoves pocket 'w'
getAllReserveMoves (piece : pocket) 'b' = if [piece] =~ "[PRNBQK]{1}"
                                          then getAllReserveMoves pocket 'b'
                                          else getAllReserveMoves' piece 0 0 7 ++ getAllReserveMoves pocket 'b'

getAllReserveMoves' :: Char -> Int -> Int -> Int -> [Move]
getAllReserveMoves' piece 8 start end = getAllReserveMoves' piece 0 (start + 1) end
getAllReserveMoves' piece col start end = if start > end
                                          then []
                                          else getAllReserveMoves' piece (col + 1) start end ++ [positionToReserveMove piece (start, col)]

lookupKingInRow :: Row -> Color -> Int
lookupKingInRow row color = lookupKingInRow' row color 0
lookupKingInRow' :: Row -> Color -> Int -> Int
lookupKingInRow' [] _ _= -1
lookupKingInRow' ('K' : row) 'w' col = col
lookupKingInRow' ('k' : row) 'b' col = col
lookupKingInRow' (_ : row) color col = lookupKingInRow' row color (col + 1)

getKingPositionByColor :: Board -> Color -> Position
getKingPositionByColor board color = getKingPositionByColor' board color 0
getKingPositionByColor' :: Board -> Color -> Int -> Position
getKingPositionByColor' [] _ _ = (-1, -1)
getKingPositionByColor' (row : board) color row_index = let col_index = lookupKingInRow row color in
                                              if col_index == -1 then getKingPositionByColor' board color (row_index + 1)
                                              else (row_index, col_index)

getFieldColor :: Board -> Field -> Color
getFieldColor board field = let (i, j) = fieldToPosition field in
                            let piece = (board !! i !! j) in
                            if [piece] =~ "[prnbqk]{1}" then 'b'
                            else if [piece] =~ "[PRNBQK]{1}" then 'w'
                            else '1'
getFieldPiece :: Board -> Field -> Char
getFieldPiece board field = let (i, j) = fieldToPosition field in (board !! i !! j)

getDirFromMove :: Move -> (Int, Int)
getDirFromMove move = let fields = splitOn '-' move in
                      let (f_y, f_x) = fieldToPosition (head fields) in
                      let (t_y, t_x) = fieldToPosition (fields !! 1) in
                      let dir_y
                            | f_y < t_y = 1
                            | f_y > t_y = -1
                            | otherwise = 0 in
                      let dir_x
                            | f_x < t_x = 1
                            | f_x > t_x = -1
                            | otherwise = 0 in
                      (dir_y, dir_x)

getDistanceFromMove :: Move -> Int
getDistanceFromMove move = let fields = splitOn '-' move in
                           let (f_y, f_x) = fieldToPosition (head fields) in
                           let (t_y, t_x) = fieldToPosition (fields !! 1) in
                           max (abs (f_y - t_y)) (abs (f_x - t_x))

getPathBySteps :: Position -> Int -> Int -> Int -> Path
getPathBySteps _ 0 _ _ = []
getPathBySteps (i, j) steps dir_y dir_x = getPathBySteps (i, j) (steps - 1) dir_y dir_x ++ [(i + dir_y * steps, j + dir_x * steps)]

getPathByMove :: Move -> Path
getPathByMove move = let fields = splitOn '-' move in
                     let (f_y, f_x) = fieldToPosition (head fields) in
                     let (dir_y, dir_x) = getDirFromMove move in
                     getPathBySteps (f_y, f_x) (getDistanceFromMove move) dir_y dir_x

pathToFields :: Path -> FieldPath
pathToFields path
  = foldr (\ pos -> (++) [positionToField pos]) [] path

validatePathMove :: Board -> Move -> Color -> Bool
validatePathMove board move = validateFieldPath board (pathToFields (getPathByMove move))
validateFieldPath :: Board -> FieldPath -> Color -> Bool
validateFieldPath board path color = validateFieldPath' board path color 0 (length path)
validateFieldPath' :: Board -> FieldPath -> Color -> Int -> Int -> Bool
validateFieldPath' board (field : path) color index len = let pieceColor = getFieldColor board field in
                                         (index == (len - 1) && pieceColor /= color) || (if pieceColor == '1' then validateFieldPath' board path color (index + 1) len
                                                                                    else False)

validateKnightMove :: Board -> Move -> Color -> Bool
validateKnightMove board move color = let fields = splitOn '-' move in
                                      getFieldColor board (fields !! 1) /= color

validatePawnMove :: Board -> Move -> Color -> Bool
validatePawnMove board move color = let fields = splitOn '-' move in
                                    let (dir_y, dir_x) = getDirFromMove move in
                                    let steps = getDistanceFromMove move in
                                    let target = getFieldColor board (fields !! 1) in
                                    let path = pathToFields (getPathByMove move) in
                                    let (y, x) = fieldToPosition (head fields) in
                                    if steps == 1 && dir_x == 0 then target == '1'
                                    else if dir_x /= 0 then target /= '1' && target /= color
                                    else (color == 'w' && y == 6 || color == 'b' && y == 1) && target == '1' && getFieldColor board (path !! 0) == '1'