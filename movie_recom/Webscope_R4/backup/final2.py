import re
from math import sqrt
from operator import itemgetter, attrgetter  
from numpy.linalg import svd
#moiveRatingTrainsetFilename = 'ydata-ymovies-user-movie-ratings-train-v1_0\ydata-ymovies-user-movie-ratings-train-v1_0.txt'
moiveRatingTrainsetFilename = 'ydata-ymovies-user-movie-ratings-train-v1_0\ydata-ymovies-user-movie-ratings-train-v1_0.txt'
testSetName = 'ydata-ymovies-user-movie-ratings-test-v1_0\ydata-ymovies-user-movie-ratings-test-v1_0.txt'


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

    def __init__(self, recomNum=5):
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
            sum += int(i[0]) - int(i[1]);
        if not scorelist:
            return (0,0)
        length = len(scorelist)
        return (float(sum) / length, length)

    def predictAll(self, knownMovies):
        predictMovies = [];
        for movie in self.allMovieRatingRecord:
            if not movie in knownMovies:
                score = self.predictOne(movie, knownMovies)
                predictMovies += [[movie, score]];
        return predictMovies

    def predictOne(self, unknownMovie, knownMovies):
        distances = 0
        i = 0
        for knownMovie in knownMovies:
            res = self.distance(unknownMovie, knownMovie);
            if res[1] != 0:
                i += res[1]
                distances += (int(knownMovies[knownMovie][0]) + res[0]) * res[1]
        s = 0
        if i == 0:
            users = self.allMovieRatingRecord[unknownMovie]
            for user in users:
                s += int(users[user][0])
            return  s / len(users)
        return distances / i 

    def recommend(self, username):
        moviesWithScore = prdictAll(self.allUserRatingRecord[username])
        moviesWithScore.sort(key =itemgetter(1),reverse = True)
        return moviesWithScore[:self.recomNum]
        
    
    def evluation(self):
        self.loadDataFileMovieBased(moiveRatingTrainsetFilename)
        self.loadDataFile(moiveRatingTrainsetFilename)

        self.loadTestSet(testSetName)
        fileHandle = open ('slopone.txt','w')
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
                        d = abs(item[1]-int(self.testSet[str(i+1)][item[0]][0]))
                        maes += d
                        rmses += pow(d,2)
                        count += 1
                        c += 1
                if c != 0:
                    # print str(i+1)+' '+str(maes/c)+' '+str(sqrt(rmses/c))
                    fileHandle.write(str(i+1)+' '+str(maes/c)+' '+str(sqrt(rmses/c))+'\n')
                maeSum += maes
                rmseSum += rmses
        # print maeSum/count, sqrt(rmseSum/count)
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
recom = movieRecommender()
# recom.convertor2matrix()
#print recom.getNearestNeighbor2('1807428619')
#print recom.allMovieRatingRecord
recom.evluation()




