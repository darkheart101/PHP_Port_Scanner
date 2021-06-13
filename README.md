<h1>Microservices Example</h1>

<p>This is a small example using microservices. A use can rate movies, check the movies 
that he has rated, check the movies that he hasn't rated.</p>
<p>An admin user has the extra feature to add a movie that it does not exist in the database.</p>

<h2>The microservices</h2>

<h3>Authentication Service</h3>
<p>The authentication service is responsible to authenticate the request, using a token.
When the token is valid then it returns the user information.</p>
<p>For this service Laravel framework was used.</p>

<h3>Eureka server</h3>
<p>Eureka Server is an application that holds the information about all client-service applications. Every Micro service will register into the Eureka server and Eureka server knows all the client applications running on each port and IP address. Eureka Server is also known as Discovery Server.</p>
<p>For this server Spring Boot was used.</p>

<h3>Movie Service</h3>
<p>Movie service holds all the data of the listed movies</p>
<p>For this service Spring Boot was used.</p>

<h3>Ratings Service</h3>
<p>Ratings service holds all the ratings for each user</p>
<p>For this service Spring Boot was used.</p>

<h2>Installation</h2>

<h3>Authentication service</h3>
<ol>
<li>rename the .env.example file</li>
<li>fill in the database information</li>
<li>run <code>composer install</code></li>
<li>run <code>php artisan jwt:secret</code></li>
</ol>

<h3>Eureka server</h3>
<p>At application.properties file you can change the port that the server listens.</p>

<h3>Movie service</h3>
<p>At application.properties file you can define the database, port, authentication service url
and the cache folder path.</p>

<h3>Ratings service</h3>
<p>At application.properties file you can define the database, port, authentication service url
and the cache folder path.</p>