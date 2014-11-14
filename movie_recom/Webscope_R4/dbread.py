movieDescrFileName = 'ydata-ymovies-movie-content-descr-v1_0/movie_db_yoda'

movieInfo = {}
catalog = ['id','title','synopsis','running_time','MPAA_rating','reasons_MPAA','release_date','distributor','URL',
'genres','directors','director_ids','crew_members','crew_ids','types_of_crew','actors','actor_ids',
'average_critic_rating','number_of_critic_ratings', 'number_of_awards_won', 'number_of_awards_nominated',
 'awards_won','awards_nominated', 'Movie Mom rating', 'review', 'review summaries', 'review owners',
           'captions', 'URL_Greg_Preview', 'URL_of_DVD_review', 'GNPP', 'average rating', 'number_of_users', "what"]
dataFile = open(movieDescrFileName,'r')
print len(catalog)
try:

    #print dataFile[2]
    for line in dataFile:
        ss = line.split('\t')
        name = ''
        i = 0;
        for s in ss:
            if i==0:
                movieInfo[s] = {}
                name = s.strip();
            else:
                movieInfo[name][catalog[i]] = s.strip();
            i+=1
finally:
    dataFile.close()
