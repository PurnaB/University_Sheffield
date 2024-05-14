Task-1 
http://localhost:8080/customers

http://localhost:8080/orders

Task-2

http://localhost:8080/getall?status=archived
or 
http://localhost:8080/getall?status=active

http://localhost:8080/getbyid?id=6&status=

Task-3
It's a scheduler call, it will run every 5 sec after application start and it will fetch the data from task-2 API and trasformed into single record with the name included(firstname+surname)
and it will call the anoter API internally and it will log the http status code.




