================================================================================
Environment requirement
================================================================================
 
We use python 2.7 to do the final project, and also we use some package of python.
The packages we use are: numpy, scipy, and sparsesvd
In order to run our program successful, these packages should be install at first :)

"ydata-ymovies-user-movie-ratings-train-v1_0.txt" and 
"ydata-ymovies-user-movie-ratings-text-v1_0.txt"
are the date set for trainning and testing. Make sure place them at the same 
directory as the source files. 
================================================================================
run instruction
================================================================================
The source files are final_knn.py, final_slopone.py and final_svd.py
where:
final_knn.py is the implementation of k-nearest neighbours
final_slopone.py is the implementation of weighted slop one
final_svd.py is the imlementation of svd

To do the recommendation, simply use:
python [source files name] [username] [number of recommendation]

for example:
python final_knn.py 1234 5

which means recommending 5 movies for user '1234'

================================================================================
Data Set Description 
================================================================================
(1) "ydata-ymovies-user-movie-ratings-train-v1_0.txt" contains a small
    sample of Yahoo! users' ratings of movies, with the following
    fields:

    0 anonymized user_id
    1 movie_id
    2 rating(from 1(F) to 13(A+))
    3 converted rating(from 1 to 5: A-,A, A+ will be converted to 5)

    The training data contains 7,642 users (|U|), 11,915 movies/items
    (|I|), and 211,231 ratings (|R|). The average user rating
    ($\overline{R_u} = \frac{\sum_u \overline{r_u}}{|U|}$,
    macro-averaged) is 9.64 and the average item rating
    (macro-averaged) is 9.32. The average number of ratings per user
    is 27.64 and the average number of ratings per item is 17.73. All
    users have rated at least 10 items and all items are rated by at
    least one user. The density ratio (\delta = \frac{|R|}{|U|*|I|})
    is 0.0023, meaning that only 0.23% of entries in the user-item
    matrix are filled.

Snippet:

1       1800029049      12      5
1       1804857429      8       4
1       1800030906      13      5
1       1800018548      11      5
1       1800256362      9       4

=====================================================================
(2) "ydata-ymovies-user-movie-ratings-test-v1_0.txt" contains a small
    sample of Yahoo! users' ratings of movies. This test data was
    gathered chronologically after the training data. The file
    contains the following fields:

    0 anonymized user_id
    1 movie_id
    2 rating(from 1(F) to 13(A+))
    3 converted rating(from 1 to 5)

    The test data contains 2,309 users, 2,380 items, and 10,136
    ratings. There are no test users/items that do not also appear in
    the training data. The average user rating is 9.66 and the average
    item rating is 9.54. The average number of ratings/user is 4.39
    and the average number of ratings/item is 4.26. All users have
    rated at least one item and all items have been rated by at least
    one user.

Snippet:

5       1808405757      9       4
6       1800247298      12      5
6       1805540029      11      5
6       1804090611      12      5
6       1800019304      12      5

