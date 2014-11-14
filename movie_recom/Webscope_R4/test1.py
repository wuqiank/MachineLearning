import scipy.sparse.linalg
import numpy, scipy.sparse
from numpy import dot
from scipy.linalg import sqrtm
from scipy.linalg import svd
from sparsesvd import sparsesvd
from math import sqrt
import scipy.sparse.linalg
from numpy import diag
a = [[1,2,4,6,6,0],[1,2,4,5,0,5],[1,2,5,6,6,4]]
avg = 0.0
userAvg = []
for i in range(len(a)):
	c = 0.0
	s = 0.0
	for j in range(len(a[i])):
		if a[i][j] != 0:
			c += 1
			s += a[i][j]
	userAvg.append(s/c)
print userAvg
# print a
for i in range(len(a)):
	for j in range(len(a[i])):
		if a[i][j] != 0:
			a[i][j] -= userAvg[i]
print a

smat =  scipy.sparse.csc_matrix(a)
u,s,v = sparsesvd(smat,2)
u1,s1,v1 = svd(a)
r = 0.0
# for i in range(len(u[0])):
# 	r += u[0][i] * v[i][0]
# for i in range(len(s)):
# 	s[i] = sqrt(s[i])
# print u
# print u1
# print '-------------------------------------------------'
# print s
# print s1
# print '-------------------------------------------------'
# print v
# print v1
# print '-------------------------------------------------'
# u = u*s
u = u.transpose()
# print u
# v = v.transpose()
# t = u * s
s = diag(s)
res = dot(dot(u,s),v)
print res
s1 = diag(s1,-3)[3:]
# print s1
# print dot(dot(u1,s1),v1)

for i in range(len(res)):
	for j in range(len(res[i])):
		res[i][j] += userAvg[i]

# for i in range(len(res)):
# 	for j in range(len(res[i])):
# 		res[i][j] += userAvg[i]
# print userAvg
print res

# t = [[1,2,3],[3,4,5]]
# tt = [[1,1],[1,1],[1,1]]
# print dot(t,tt)
# print diag(s)
# u.transpose()
# print u
# print v
# for i in range(len(u[0])):
# 	r += u[0][i] * v[5][i]
# print r
# print s
# ut = u*s
# # vt = s*v
# print ut
# print vt

# print ss
