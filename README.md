# org-hierarchy

Springboot Kotlin project to maintain organization Hierarchy . 
Deployed in Kubernetes Cluster 


## Build & Deploy 

### Build the image :

     Go inside organisation-hierarchy and run the below commands :
	
1.	Run this script  to build the image  :
./build.sh 
2.	Run this script to start the containers using docker-compose: 
./start-server.sh
3.	Run this script to stop the containers: 
./stop-server.sh




### Deploying in Kubernets cluster :
1.	Go inside K8 folder 
2.	Run “docker tag org-hierarchy:latest joy1987/org-hierarchy:latest “ 
{In the above step, “joy1987” can be replaced with appropriate docker repo name }
3.	Run “docker push”
4.	Run “helm install org-hierarchy-chart org-hierarchy-chart”
5.	Run the command to check if all the pods are in running state “kubectl get pods -w “
6.	Once all are in Running state , you can test the application 


If the application is running in minikube , run the below command to get the port in which the service is exposed :
minikube service org-hierarchy-service --url

Replace 8080 with this port 


## API Testing :

API 1: 

URL :http://localhost:8080/organization-structure
Method: POST 
Authorization : Basic Auth
Username: user
Passowrd: password

Request : 
'''
{
"Pete": "Nick",
"Barbara": "Nick",
"Nick": "Sophie",
"Sophie": "Jonas",
"Joy": "Jonas"
}
'''
Response: 200 OK



API 2 : 

URL : localhost:8080/organization-structure
Method : GET
Authorization : Basic Auth
Username: user
Passowrd: password


Response : 200 OK 
'''
{
    "Jonas": {
        "Joy": {},
        "Sophie": {
            "Nick": {
                "Barbara": {},
                "Pete": {}
            }
        }
    }
}
'''

API 3 :

URL : localhost:8080/organization-structure/Pete?levels=2
Method: GET
Authorization : Basic Auth
Username: user
Passowrd: password
'''
{
    "Nick": {
        "Sophie": {}
    }
}
'''



