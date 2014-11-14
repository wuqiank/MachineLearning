import re
from math import sqrt
from operator import itemgetter, attrgetter  
from numpy.linalg import svd
import sys
numForRecomm = int(sys.argv[2])
username = sys.argv[1]
# numForRecomm = 100000

# username = '76'
#moiveRatingTrainsetFilename = 'ydata-ymovies-user-movie-ratings-train-v1_0\ydata-ymovies-user-movie-ratings-train-v1_0.txt'
moiveRatingTrainsetFilename = 'ydata-ymovies-user-movie-ratings-train-v1_0.txt'
testSetName = 'ydata-ymovies-user-movie-ratings-test-v1_0.txt'


# k is the number of nearest neighbor need to be take into consideration
# n is number of recommendation we want to make
# distanceMeasure is used to specify the algorithm to measure the distance
#                 which can be one of:  -- 'Euclidean'
#                                       -- 'Consine'
# allUserRatingRecord is a dictionary which record every user and their rating of movies.
#                     the format is {userID: {movieID: [rating1, rating2],
#                                             movieID: [rating1, rating2],
#                                             ...                         }
#                                    userID: {...                         }
#                                    ...                                  }  
class movieRecommender:

    def __init__(self, recomNum):
        self.n = recomNum                       #-number of recommendation
        self.allUserRatingRecord = {}           #-this list is used to store every user's rating
        self.allMovieRatingRecord = {}
        self.testSet = {}

    # the format of rating is: {movieID: [rating1, rating2],
    #                           movieID: [rating1, rating2],
    #                           ...                         }

    def loadDataFile(self, dataFileName):
        dataFile = open(dataFileName,'r')
        try:
            preUserId = '0'
            rating ={}
            for line in dataFile:
                oneUserRating = re.split('[ \n\r\t]',line)
                del oneUserRating[4]                                        # delete the useless info
                if oneUserRating[0] != preUserId:
                    if preUserId != '0':
                        self.allUserRatingRecord[preUserId] = rating.copy() # add the user's rating to the list
                        rating.clear()
                rating[oneUserRating[1]] = [oneUserRating[2],oneUserRating[3]];
                preUserId = oneUserRating[0]
        finally:
            dataFile.close()

    def loadDataFileMovieBased(self, dataFileName):
        with open(dataFileName, 'r') as f:
            for line in f:
                oneUserRating = re.split('[ \n\r\t]',line)
                userName = oneUserRating[0]
                movieName = oneUserRating[1]
                rating1 = oneUserRating[2]
                rating2 = oneUserRating[3]
                if self.allMovieRatingRecord.has_key(movieName):
                    self.allMovieRatingRecord[movieName][userName] = [rating1, rating2]
                else:
                    self.allMovieRatingRecord[movieName] = {userName:[rating1, rating2]}

    def loadTestSet(self, dataFileName):
        dataFile = open(dataFileName,'r')
        try:
            preUserId = '0'
            rating ={}
            for line in dataFile:
                oneUserRating = re.split('[ \n\r\t]',line)
                del oneUserRating[4]                                        # delete the useless info
                if oneUserRating[0] != preUserId:
                    if preUserId != '0':
                        self.testSet[preUserId] = rating.copy() # add the user's rating to the list
                        rating.clear()
                rating[oneUserRating[1]] = [oneUserRating[2],oneUserRating[3]];
                preUserId = oneUserRating[0]
        finally:
            dataFile.close()

    # def preprocessing(self):
    #     users =  self.allUserRatingRecord
    #     movies = self.allMovieRatingRecord
    #     self.moviesAvg = {}
    #     for movie in movies:
    #         s = 0.0
    #         c = 0.0
    #         userEntry = movies[movie]
    #         for user in userEntry:
    #             s += int(userEntry[user][0])
    #             c += 1
    #         self.moviesAvg[movie] = s/c

    def distance(self, movie1, movie2):
        scorelist = []
        users1 = self.allMovieRatingRecord[movie1]
        users2 = self.allMovieRatingRecord[movie2]
        # print movie1,movie2
        for user in users1:
            if user in users2:
                scorelist += [[users1[user][0], users2[user][0]]]
        sum = 0;
        for i in scorelist:
            # sum += (int(i[0]) - self.moviesAvg[movie1]) - (int(i[1]) - self.moviesAvg[movie2])
            sum += int(i[0])- int(i[1]) 
            # if int(i[0]) - int(i[1]) > 12: print '----------------------'
        if not scorelist:
            return (0,0)

        length = len(scorelist)
        return (float(sum) / length, length)

    def predictAll(self, knownMovies):
        predictMovies = [];
        for movie in self.allMovieRatingRecord:
            if not movie in knownMovies:
                score = self.predictOne(movie, knownMovies)
                predictMovies += [[movie, score]]
        return predictMovies

    def predictOne(self, unknownMovie, knownMovies):
        distances = 0
        i = 0
        for knownMovie in knownMovies:
            res = self.distance(unknownMovie, knownMovie);
            if res[1] != 0:
                i += res[1]
                temp = max(min(int(knownMovies[knownMovie][0]) + res[0], 13),1)
                distances += temp * res[1]
                # if int(knownMovies[knownMovie][0]) + res[0] >20: 
                #     print int(knownMovies[knownMovie][0]), res[0]
        s = 0
        if i == 0:
            users = self.allMovieRatingRecord[unknownMovie]
            for user in users:
                s += int(users[user][0])
            # if s / len(users) > 20 : print s / len(users)
            return  s / len(users)

        return distances / i 

    def preprocessing(self):
        users = self.allUserRatingRecord
        movies = self.allMovieRatingRecord
        moviesAvg = {}
        for movie in movies:
            s = 0.0
            c = 0.0
            userEntry = movies[movie]
            for item in userEntry:
                rate = int(userEntry[item][0])
                s += rate
                c += 1
            moviesAvg[movie] = s/c
        for user in users.keys():
            movieEntry = users[user]
            for item in movieEntry.keys():
                if abs(int(movieEntry[item][0]) - moviesAvg[item]) > 5:
                    del movieEntry[item]
                    if not movieEntry:
                        del movieEntry
                    del movies[item][user]
                    if not movies[item]:
                        del movies[item] 
                    # break
    def recommend(self, username):
        self.loadDataFileMovieBased(moiveRatingTrainsetFilename)
        self.loadDataFile(moiveRatingTrainsetFilename)
        self.preprocessing()
        moviesWithScore = self.predictAll(self.allUserRatingRecord[username])
        moviesWithScore.sort(key =itemgetter(1),reverse = True)
        for item in moviesWithScore[:self.n]:
            print item[0], int(item[1])
        
    
    def evluation(self):
        self.loadDataFileMovieBased(moiveRatingTrainsetFilename)
        self.loadDataFile(moiveRatingTrainsetFilename)
        self.preprocessing()
        self.loadTestSet(testSetName)
        fileHandle = open ('slopone.adjusted.txt','w')
        maeSum = 0.0
        rmseSum = 0.0
        count = 0
        for i in range(7642):
            maes = 0.0
            rmses = 0.0
            c = 0
            if str(i+1) in self.testSet:
                predict = self.predictAll(self.allUserRatingRecord[str(i+1)])

                #print 'as to' + str(i+1) +':   '
                for item in predict:

                    if item[0] in self.testSet[str(i+1)]:
                        if item[1] == 13:
                            print item[0], item[1],int(self.testSet[str(i+1)][item[0]][0])
                        d = abs(item[1]-int(self.testSet[str(i+1)][item[0]][0]))
                        maes += d
                        rmses += pow(d,2)
                        count += 1
                        c += 1
                if c != 0:
                    print str(i+1)+' '+str(maes/c)+' '+str(sqrt(rmses/c))
                    fileHandle.write(str(i+1)+' '+str(maes/c)+' '+str(sqrt(rmses/c))+'\n')
                maeSum += maes
                rmseSum += rmses
        print maeSum/count, sqrt(rmseSum/count)
        fileHandle.write(str(maeSum/count)+' '+str(sqrt(rmseSum/count)))
        fileHandle.close

    # def convertor2matrix(self):
    #     self.loadDataFileMovieBased(moiveRatingTrainsetFilename)
    #     self.loadDataFile(moiveRatingTrainsetFilename)
    #     matrix = [[0 for x in range(0, len(self.allMovieRatingRecord))] for y in range(0, len(self.allUserRatingRecord))]
    #     users = list(self.allUserRatingRecord.keys())
    #     movies = list(self.allMovieRatingRecord.keys())
    #     for u in range(len(users)):
    #         user = users[u]
    #         userEntry = self.allUserRatingRecord[user]
    #         for movie in userEntry:
    #             matrix[u][movies.index(movie)] = int(userEntry[movie][0])
    #     # l = [n for n in matrix[1] if n != 0]
    #     # print l
    #     print svd(matrix)
                # print self.allUserRatingRecord[users[1]]
recom = movieRecommender(numForRecomm)
recom.recommend(username)

# recom.convertor2matrix()
#print recom.getNearestNeighbor2('1807428619')
#print recom.allMovieRatingRecord
# recom.evluation()


