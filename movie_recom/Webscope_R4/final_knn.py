import re
from math import sqrt
from operator import itemgetter, attrgetter  
import sys
# out = sys.argv[1]
# knum = int(sys.argv[2])
# numForRecomm = 0
knum = 20
numForRecomm = int(sys.argv[2])
username = sys.argv[1]
# numForRecomm = 5
# username = '1'
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

    def __init__(self, distanceMeasure, k, recomNum = numForRecomm):
        self.k = k                              #-number of nearest neighbors
        self.n = recomNum                       #-number of recommendation
        self.distanceMeasure = distanceMeasure  #-specify the algorithm to computer the distance
        self.allUserRatingRecord = {}           #-this list is used to store every user's rating
        self.allMovieRatingRecord = {}         #-this list is used to store every user's rating
        self.testSet = {}
        if distanceMeasure == 'Euclidean':
            self.getDistance = self.EuclideanDistance
        if distanceMeasure == 'Consine':
            self.getDistance = self.CosineSimilarity

    # the format of rating is: {movieID: [rating1, rating2],
    #                           movieID: [rating1, rating2],
    #                           ...                         }
    def loadDataFile(self, dataFileName):
        dataFile = open(dataFileName,'r')
        try:
            preUserId = '0'
            rating ={}
            # oneUserRating = []
            for line in dataFile:
                oneUserRating = re.split('[ \n\r\t]',line)
                del oneUserRating[4]                                        # delete the useless info
                if oneUserRating[0] != preUserId:
                    if preUserId != '0':
                        self.allUserRatingRecord[preUserId] = rating.copy() # add the user's rating to the list
                        rating.clear()
                rating[oneUserRating[1]] = [oneUserRating[2],oneUserRating[3]];
                preUserId = oneUserRating[0]
            self.allUserRatingRecord[preUserId] = rating.copy()
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


    # calculate the euclidean distance between two users
    def EuclideanDistance(self, oneUserRating_1, oneUserRating_2):
        squareSum = 0
        for movie in oneUserRating_1:
            if movie in oneUserRating_2:
                squareSum += pow(abs(int(oneUserRating_1[movie][0]) - int(oneUserRating_2[movie][0])), 2)
                #print movie
                #print square_sum
                #print '----------------------------------------------------------'
        return float('%0.3f'%sqrt(squareSum))
    


    def CosineSimilarity(self, oneUserRating_1, oneUserRating_2):
        dotProduct = 0.0
        lengthA = 0.0
        lengthB = 0.0
        for movie in oneUserRating_1:
            if movie in oneUserRating_2:
                dotProduct += int(oneUserRating_1[movie][0]) * int(oneUserRating_2[movie][0])

        for movie in oneUserRating_1:
            lengthA += pow(int(oneUserRating_1[movie][0]), 2)
        for movie in oneUserRating_2:
            lengthB += pow(int(oneUserRating_2[movie][0]), 2)
        
        lengthA = sqrt(lengthA)
        lengthB = sqrt(lengthB)
        if lengthA*lengthB == 0.0:
            return 0.0
        return float('%0.3f'%(dotProduct/(lengthA*lengthB)))+0.00001


    # distances list which is used to store all the user's name and distances to the specified user
    # the format is [(userID, distance), (userID, distance), ...]
    def getNearestNeighbor(self, oneUser):
        distances = []
        for one in self.allUserRatingRecord:
            if one != oneUser:
                distances.append((one,self.getDistance(self.allUserRatingRecord[one],self.allUserRatingRecord[oneUser])))
        if self.distanceMeasure == 'Euclidean': # if it is Euclidean distance, smaller number means more similar  
            distances.sort(key=itemgetter(1))
        else:                                   # if it is cosine similarity, bigger number means more similar
            distances.sort(key=itemgetter(1), reverse = True)
        return distances
    


    # recommend is a dictionary to record the movie which will be recommend, but the rating need to to compute
    #           the format is {movieID: [(neighbour's distance, neighor's rating),
    #                                    (neigbhor's distance, neighor's rating),
    #                                     ...                                   ]
    #                          movieID: [ ...                                   ]
    #                          ...                                               }
    def recommend(self, username):
        self.loadDataFile(moiveRatingTrainsetFilename)
        self.loadDataFileMovieBased(moiveRatingTrainsetFilename)
        movies2recommend = []
        recommend = {}
        allUserRating =  self.allUserRatingRecord
        nearestNeighbors = self.getNearestNeighbor(username)
        for i in range(self.k):
            neigbhorId = nearestNeighbors[i][0]
            ratingOfNeighbor = allUserRating[neigbhorId]  
            for movie in ratingOfNeighbor:
                if movie not in allUserRating[username]:
                    if movie in recommend:
                        recommend[movie].append((nearestNeighbors[i][1], int(ratingOfNeighbor[movie][0])))
                    else:
                        recommend[movie] = [(nearestNeighbors[i][1], int(ratingOfNeighbor[movie][0]))]
            
        for movie in recommend:
            distanceSum = 0.0
            rating = 0.0
            # print recommend[movie]
            for key in recommend[movie]:
                distanceSum += (key[0])
            for key in recommend[movie]:
                weight = (key[0])/distanceSum
                rating += weight*key[1]
            if rating > 13: rating = 13
            if rating < 1: rating = 1 
            movies2recommend.append((movie,rating))
        # return movies2recommend
        movies2recommend.sort(key =itemgetter(1),reverse = True)
        for item in movies2recommend[:self.n]:
            rate =  "%.0f" %(item[1])
            rate = int(rate)
            if rate > 13:
                rate = 13 
            print item[0], rate

    def filter(self):
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
        
    def evluation(self):
        self.loadDataFile(moiveRatingTrainsetFilename)
        self.loadDataFileMovieBased(moiveRatingTrainsetFilename)
        self.loadTestSet(testSetName)
        self.filter()
        fileHandle = open (out,'w')
        maeSum = 0.0
        rmseSum = 0.0
        count = 0
        #print self.testSet
        for i in range(7642):
            maes = 0.0
            rmses = 0.0
            c = 0
            if str(i+1) in self.testSet:
                predict = self.recommend(str(i+1))
                #print 'as to' + str(i+1) +':   '
                for item in predict:
                    if item[0] in self.testSet[str(i+1)]:
                        '''if i+1 == 151:
                            print 'predict: ', 
                            print  item[0], item[1]
                            print 'truth: ',
                            print  item[0], self.testSet[str(i+1)][item[0]][0]
                            print  pow(item[1]-int(self.testSet[str(i+1)][item[0]][0]), 2)                    
                        '''
                        d = abs(item[1]-int(self.testSet[str(i+1)][item[0]][0]))
                        maes += d
                        rmses += pow(d,2)
                        count += 1
                        c += 1
                if c != 0:
                    print str(i+1)+' mae: ',maes/c,
                    print '  rmse: ',sqrt(rmses/c)
                    fileHandle.write(str(i+1)+' '+str(maes/c)+' '+str(sqrt(rmses/c))+'\n')
                maeSum += maes
                rmseSum += rmses
        print maeSum/count, sqrt(rmseSum/count)
        fileHandle.write(str(maeSum/count)+' '+str(sqrt(rmseSum/count)))
        fileHandle.close

recom = movieRecommender('Consine', knum, numForRecomm)
recom.recommend(username)
# recom.evluation()




