<div align="center">
<a href="https://imgbb.com/"><img src="https://i.ibb.co/RBK67jt/logo.png" alt="logo" border="0"></a>
</div>

<a href = "https://app.swaggerhub.com/apis/FindMeTicket/find-me-ticket_api/2.0.0#/">![Swagger](https://img.shields.io/badge/swagger-2.0.0-blue.svg?cacheSeconds=2592000)</a>
<h6 align="center">Project is led by SoftServe company</h6>
This web application revolutionizes the ticket booking experience for various types of transport, including buses,
trains, airplanes, and ferries. It offers a user-friendly interface, simplifying the process of finding and redirecting
users to the sellers of the best tickets available across multiple websites in Ukraine.

***

<div align="center">
<img src="src/main/resources/image/app.gif?raw=true" alt="animation">
</div>


<details lang="java">
<summary>
 
## TechStack 🛠️
</summary>


<div>
<div>
<H3>Frontend</H3>
</div>
 <img src="https://skillicons.dev/icons?i=js" height="40" alt="javascript logo"  />
  <img width="12" />
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/react/react-original.svg" height="40" alt="react logo"  />
  <img width="12" />
 <img src="https://skillicons.dev/icons?i=css" height="40" alt="css3 logo"  />
  <img width="12" />
  <img src="https://skillicons.dev/icons?i=html" height="40" alt="html5 logo"  />
<img width="12" />
  <img src="https://skillicons.dev/icons?i=sass" height="40" alt="sass logo"  />
</div>

<div>
<div>
<H3>Backend</H3>
</div>
<img src="https://user-images.githubusercontent.com/25181517/117201156-9a724800-adec-11eb-9a9d-3cd0f67da4bc.png" alt="Java" width="40" height="40">
  <img src="https://skillicons.dev/icons?i=spring" height="40" alt="spring logo"  />
  <img width="12" />
<img src="https://skillicons.dev/icons?i=hibernate" height="40" alt="hibernate logo"  />
  <img width="12" />
<img src="https://user-images.githubusercontent.com/25181517/183892181-ad32b69e-3603-418c-b8e7-99e976c2a784.png" alt="Java" width="50" height="50">
 <img width="12" />
<img src="https://user-images.githubusercontent.com/25181517/117533873-484d4480-afef-11eb-9fad-67c8605e3592.png" alt="Java" width="50" height="">
 <img width="12" />
<img src="https://user-images.githubusercontent.com/25181517/192107858-fe19f043-c502-4009-8c47-476fc89718ad.png" alt="Java" width="40" height="40">
 <img width="12" />
</div>

<div>
<div>
<h3>Others</h3>
</div>
<img src="https://skillicons.dev/icons?i=docker" height="40" alt="docker logo"  />
  <img width="12" />
<img src="https://skillicons.dev/icons?i=postman" height="40" alt="postman logo"  />
  <img width="12" />
 <img src="https://skillicons.dev/icons?i=maven" height="40" alt="apachemaven logo"  />
  <img width="12" />
<img src="https://skillicons.dev/icons?i=postgres" height="40" alt="postgresql logo"  />
  <img width="12" />
<img src="https://user-images.githubusercontent.com/25181517/183891673-32824908-bc5d-44f8-8f72-f0415822404a.png" alt="Java" width="40" height="40">
  <img width="12" />
<img src="https://user-images.githubusercontent.com/25181517/186711335-a3729606-5a78-4496-9a36-06efcc74f800.png" alt="Java" width="40" height="42">
  <img width="12" />
</div>

</details>

<details lang="java">
<summary>
 
## Run 🐳
</summary>


**This app is Docker ready**

**It requires several environment variables to be filled in the .env.docker files** 

**The backend files are located in the main directory, while the frontend files are located in the /frontend directory**

To run use command:
````
docker.exe compose -f compose.yaml  up 
````

</details>

<details lang="java">
<summary>
 
## Api controllers description 📋
</summary>

**Login Controller** - handle sign-in (jwt tokens).

| HTTP method |       Endpoint                            |           Description               |
|:-----------:|:-----------------------------------------:|:-----------------------------------:|
|    POST     | `/oauth2/authorize/(google or facebook)*` |  Third party service authentication |
|    POST     |    `/login `                              |  Plain authentication.              |

**Logout Controller** - handle sign-out.

| HTTP method |         Endpoint         |            Description             |
|:-----------:|:------------------------:|:----------------------------------:|
|     GET     |     `/logout`            | Logout a user                      |

**Registration Controller** - handle sign-up.

| HTTP method |         Endpoint         |      Description          |
|:-----------:|:------------------------:|:-------------------------:|
|     POST    | `/resend/confirm-token ` | Resend email confirmation |
|     POST    |     `/register`          |   Register User           |
|     POST    |     `/confirm-email`     |    Email confirmation.    |

**Notification Controller** - handle operations on notifications.

| HTTP method |         Endpoint         |      Description          |
|:-----------:|:------------------------:|:-------------------------:|
|     GET     | `/notifications/enable ` |   Enable notifications    |
|     GET     | `/notifications/disable` |   Disable notifications   |

**Reset Password Controller** - handle operations on password.

| HTTP method |         Endpoint         |            Description             |
|:-----------:|:------------------------:|:----------------------------------:|
|     POST    |      `/update-password ` | Creates new password               |
|     POST    |     `/reset`             | Request for new password           |
|     POST    | `/resend/reset-token`    |  Resend password reset token       |
|     POST    |     `/new-password`      | Confirmation reset password token. |

**Ticket Controller** - handle operations on tickets.

| HTTP method |         Endpoint         |            Description             |
|:-----------:|:------------------------:|:----------------------------------:|
|     POST    |      `/sortedBy`         |              Sort tickets          |
|     POST    |  `/selectedTransport`    |    Select desired transport type   |
|     POST    |      `/searchTickets`    |     Search tickets                 |
|     GET     |     `/get/ticket/{id}`   | Get detailed info about ticket     |

**Type Ahead Controller** - handle type-ahead functionality for city search.

| HTTP method |         Endpoint         |            Description             |
|:-----------:|:------------------------:|:----------------------------------:|
|     POST    |     `/typeAhead`         |       Request cities               |

**Delete User Controller** - handle user deletion.

| HTTP method |         URL              |            Description             |
|:-----------:|:------------------------:|:----------------------------------:|
|   DELETE    |     `/delete-user`       |           Delete a user            |

**Search History Controller** - user's history search.

| HTTP method |         Endpoint         |            Description             |
|:-----------:|:------------------------:|:----------------------------------:|
|     GET     |     `/getHistory`        |        Request history             |

**Review Controller** - handle reviews operations.

| HTTP method |         Endpoint         |            Description             |
|:-----------:|:------------------------:|:----------------------------------:|
|     POST    |      `/saveReview`       |          Saves review              |
|     GET     |  `/getReviews`           |    Receive all reviews             |
|     DELETE  |      `/deleteReview`     |    Delete review                   |
|     GET     |     `/getUserReview`     |   Get authorized user review       |

</details>

## Authors 🙋‍♂️

👤 **Aleksandra Poichenko** - *Project Manager* [[LinkedIn](https://www.linkedin.com/in/aleksandra-poichenko/)]

👤 **Volodymyr Kuzmych** - *Team
Mentor* [[GitHub](https://github.com/vvkuzmych) | [LinkedIn](https://www.linkedin.com/in/volodymyr-kuzmych-9915942a/)]

👤 **Mykhailo Marchuk** - *Backend
dev* [[GitHub](https://github.com/mishaakamichael999) | [LinkedIn](https://github.com/marchuk-engineer)]

👤 **Kyrylo Peniaziev** - *Backend
dev* [[GitHub](https://github.com/KirilllP) | [LinkedIn](https://www.linkedin.com/in/kyrylo-peniaziev-9137a328a/)]

👤 **Stepan Stadniuk** - *Frontend dev* [[GitHub](https://github.com/Stepan22-prog)]

👤 **Maksim Denisenko** - *Frontend
dev* [[GitHub](https://github.com/maks2708) | [LinkedIn](https://www.linkedin.com/in/%D0%BC%D0%B0%D0%BA%D1%81%D0%B8%D0%BC-%D0%B4%D0%B5%D0%BD%D0%B8%D1%81%D0%B5%D0%BD%D0%BA%D0%BE-ba4b932b9/)]

## Support

Give a ⭐️ if this project helped you!

[!["Buy Me A Coffee"](https://www.buymeacoffee.com/assets/img/custom_images/orange_img.png)](https://www.buymeacoffee.com/findmeticket)
