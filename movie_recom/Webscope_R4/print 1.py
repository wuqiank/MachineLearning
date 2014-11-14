import numpy, scipy.sparse
from sparsesvd import sparsesvd
import scipy.sparse.linalg
import re
from math import sqrt
from operator import itemgetter, attrgetter  
import scipy.sparse.linalg
import numpy, scipy.sparse
from numpy import dot
from scipy.linalg import sqrtm
from scipy.linalg import svd
from sparsesvd import sparsesvd
from math import sqrt
import scipy.sparse.linalg
from numpy import diag
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
    def __init__(self, distanceMeasure='Consine', k=5, recomNum=5):
        self.k = k                              #-number of nearest neighbors
        self.n = recomNum                       #-number of recommendation
        self.distanceMeasure = distanceMeasure  #-specify the algorithm to computer the distance
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

    def convertor2matrix(self):
        self.loadDataFileMovieBased(moiveRatingTrainsetFilename)
        self.loadDataFile(moiveRatingTrainsetFilename)
        self.matrix = [[0 for x in range(0, len(self.allMovieRatingRecord))] for y in range(0, len(self.allUserRatingRecord))]
        self.users = list(self.allUserRatingRecord.keys())
        self.movies = list(self.allMovieRatingRecord)
        matrix = self.matrix
        for u in range(len(self.users)):
            user = self.users[u]
            userEntry = self.allUserRatingRecord[user]
            for movie in userEntry:
                matrix[u][self.movies.index(movie)] = int(userEntry[movie][0])
        
        
        avg = 0.0
        userAvg = []
        for i in range(len(matrix)):
            c = 0.0
            s = 0.0
            for j in range(len(matrix[i])):
                if matrix[i][j] != 0:
                    c += 1
                    s += matrix[i][j]
            userAvg.append(s/c)
        for i in range(len(matrix)):
            for j in range(len(matrix[i])):
                if matrix[i][j] != 0:
                    matrix[i][j] -= userAvg[i]

        smat =  scipy.sparse.csc_matrix(matrix)
        u, s, v = sparsesvd(smat,10)    
        u = u.transpose()
        s = diag(s)
        res = dot(dot(u,s),v)   
        for i in range(len(res)):
            for j in range(len(res[i])):
                res[i][j] += userAvg[i]  
        self.result = res  
        # print u
        # print s
        # print v
	# test = {'1':{'111':[0,1],'222':[1,2]}, '2':{'333':[3,4],'444':[5,6]}}
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
    def evluation(self):
        # self.loadDataFile(moiveRatingTrainsetFilename)
        self.loadTestSet(testSetName)
        # fileHandle = open (out,'w')
        maeSum = 0.0
        rmseSum = 0.0
        count = 0
        #print self.testSet
        for user in self.testSet:
            maes = 0.0
            rmses = 0.0
            c = 0
            movies = self.testSet[user]
            # print movies
            for item in movies:
                u = self.users.index(user)
                m = self.movies.index(item)
                d = abs(self.result[u][m]-int(movies[item][0]))
                maes += d
                rmses += pow(d,2)
                count += 1
                c += 1
            print user+' mae: ',maes/c,
            print '  rmse: ',sqrt(rmses/c)
                # fileHandle.write(str(i+1)+' '+str(maes/c)+' '+str(sqrt(rmses/c))+'\n')
            maeSum += maes
            rmseSum += rmses
        print maeSum/count, sqrt(rmseSum/count)
        # fileHandle.write(str(maeSum/count)+' '+str(sqrt(rmseSum/count)))
        # fileHandle.close

recom = movieRecommender()
recom.convertor2matrix()
recom.evluation()
#print recom.getNearestNeighbor2('1807428619')
#print recom.allMovieRatingRecord
# recom.evluation()




