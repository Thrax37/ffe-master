module Main where
import Control.Monad (void)
import Data.List (find)
import System.Environment (getArgs)
import System.Exit (exitFailure)
import Text.Parsec

-- | The world
data World = World
    { worldRound  :: Int    -- ^ The current round
    , worldTownID :: Int    -- ^ The current town ID
    , worldTowns  :: [Town] -- ^ List of all towns in the world
    }
    deriving (Show)

-- | A town in the world
data Town = Town
    { townID            :: Int -- ^ The town ID
    , townDeath         :: Int -- ^ The number of death people in the town
    , townHealthy       :: Int -- ^ The number of healthy people in the town
    , townInfected      :: Int -- ^ The number of infected people in the town
    , townInfectionRate :: Int -- ^ The infaction rate of the town
    , townContagionRate :: Int -- ^ The contagion rate of the town
    , townLethalityRate :: Int -- ^ The lethality rate of the town
    , townMigrationRate :: Int -- ^ The migration rate of the town
    }
    deriving (Show)

-- | Parse a Int
parseInt :: Parsec String () Int
parseInt = do
    sign <- option '+' $ oneOf "+-"
    numb <- read <$> many1 digit
    return $ if sign == '+'
        then numb
        else negate numb

-- | Parse a town
parseTown :: Parsec String () Town
parseTown = do
    nID <- parseInt
    void $ char '_'
    nHealthy <- parseInt
    void $ char '_'
    nInfected <- parseInt
    void $ char '_'
    nDeath <- parseInt
    void $ char '_'
    nInfectionRate <- parseInt
    void $ char '_'
    nContagionRate <- parseInt
    void $ char '_'
    nLethalityRate <- parseInt
    void $ char '_'
    nMigrationRate <- parseInt
    return Town
        { townID            = nID
        , townDeath         = nDeath
        , townHealthy       = nHealthy
        , townInfected      = nInfected
        , townInfectionRate = nInfectionRate
        , townContagionRate = nContagionRate
        , townLethalityRate = nLethalityRate
        , townMigrationRate = nMigrationRate }

-- | Parse a world
parseWorld :: Parsec String () World
parseWorld = do
    nRound <- parseInt
    void $ char ';'
    nTownID <- parseInt
    void $ char ';'
    towns <- parseTown `sepBy` char ';'
    let nTowns = length towns
    if nTowns < nTownID
        then let nExpected   = (nTownID - nTowns) in
            fail $ "expected at least " ++ show nExpected ++ " more town(s)"
        else return World
            { worldRound  = nRound
            , worldTownID = nTownID
            , worldTowns  = towns }

-- | Update a town
updateTown :: World -> Town -> String
updateTown world town = take 3 $ lastRound
                   ++ prepareForReproduction
                   ++ decreaseInfected
                   ++ decreaseMigration
                   ++ decreaseInfection
                   ++ decreaseContagion
                   ++ decreaseLethality
                   ++ decreaseWorldWide
  where
    -- | The current round number
    nRound         = worldRound world
    -- | The current number of infected
    nInfected      = townInfected town
    -- | The current lethality rate
    nLethalityRate = townLethalityRate town
    -- | The current migration rate
    nMigrationRate = townMigrationRate town
    -- | The current infection rate
    nInfectionRate = townInfectionRate town
    -- | The current contagion rate
    nContagionRate = townContagionRate town
    -- | What to do on the last round
    lastRound
        | nRound == 50 = "CCC"
        | otherwise    = ""
    -- | What to do in order to prepare for reproduction
    prepareForReproduction
        | (nRound+1) `mod` 5 == 0 = decreaseInfected
        | otherwise               = ""
    -- | What to do in order to decrease infected
    decreaseInfected
        | nInfected > 25 = "CCC"
        | nInfected > 15 = "CC"
        | nInfected > 5  = "C"
        | otherwise      = ""
    -- | What to do in order to decrease lethality
    decreaseLethality
        | nLethalityRate > 4 = "I"
        | otherwise          = ""
    -- | What to do in order to decrease migration
    decreaseMigration
        | nMigrationRate > 0 = "B"
        | otherwise          = ""
    -- | What to do in order to decrease infection
    decreaseInfection
        | nInfectionRate > 0 = "M"
        | otherwise          = ""
    -- | What to do in order to decrease contagion
    decreaseContagion
        | nContagionRate > 4 = "E"
        | otherwise          = ""
    -- | What to do if everything else has been taken care of
    decreaseWorldWide = "PPP"

-- | Update a world
updateWorld :: World -> Maybe String
updateWorld world = updateTown world <$> town
  where
    town          = find ((==worldTownID world) . townID) (worldTowns world)

-- | Main program entry point
main :: IO ()
main = do
    cmds <- concat <$> getArgs
    case parse parseWorld "stdin" cmds of
        Left err    -> print err >> exitFailure
        Right world -> case updateWorld world of
            Just cmd -> putStrLn cmd
            Nothing  -> putStrLn "Failed to update world!" >> exitFailure